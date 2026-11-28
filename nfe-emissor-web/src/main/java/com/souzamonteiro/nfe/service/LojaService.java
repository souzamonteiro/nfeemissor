package com.souzamonteiro.nfe.service;

import com.souzamonteiro.nfe.model.Cliente;
import com.souzamonteiro.nfe.model.Configuracao;
import com.souzamonteiro.nfe.model.Empresa;
import com.souzamonteiro.nfe.model.ItemVenda;
import com.souzamonteiro.nfe.model.Produto;
import com.souzamonteiro.nfe.model.Usuario;
import com.souzamonteiro.nfe.model.Venda;
import com.souzamonteiro.nfe.util.HibernateUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;

public class LojaService {
    public static final String CLIENTE = "CLIENTE";

    public static boolean cliente(Usuario usuario) {
        return usuario != null && CLIENTE.equals(usuario.getPerfil());
    }

    public static boolean equipe(Usuario usuario) {
        return usuario != null && ("ADMIN".equals(usuario.getPerfil()) || "USER".equals(usuario.getPerfil()));
    }

    public List<Produto> catalogo(String busca, String categoria) {
        return consultar(em -> em.createQuery(
            "SELECT p FROM Produto p WHERE p.ativo = true AND p.publicadoLoja = true "
                + "AND COALESCE(p.precoLoja, p.vuncom) > 0 AND "
                + "(:busca = '' OR LOCATE(:busca, LOWER(p.xprod)) > 0 OR LOCATE(:busca, LOWER(p.cprod)) > 0) "
                + "AND (:categoria = '' OR p.categoriaLoja = :categoria) ORDER BY p.xprod", Produto.class)
            .setParameter("busca", busca == null ? "" : busca.trim().toLowerCase(Locale.ROOT))
            .setParameter("categoria", categoria == null ? "" : categoria).getResultList());
    }

    public List<String> categorias() {
        return consultar(em -> em.createQuery(
            "SELECT DISTINCT p.categoriaLoja FROM Produto p WHERE p.ativo = true AND p.publicadoLoja = true "
                + "AND p.categoriaLoja IS NOT NULL AND p.categoriaLoja <> '' ORDER BY p.categoriaLoja", String.class)
            .getResultList());
    }

    public Produto produto(Integer id) {
        return consultar(em -> produtoPublicado(em, id));
    }

    private Produto produtoPublicado(EntityManager em, Integer id) {
        Produto p = em.find(Produto.class, id);
        if (p == null || !Boolean.TRUE.equals(p.getAtivo()) || !p.getPublicadoLoja()
                || p.getPrecoVitrine() == null || p.getPrecoVitrine().signum() <= 0) {
            throw new IllegalArgumentException("Um dos produtos saiu da vitrine. Atualize o carrinho.");
        }
        return p;
    }

    public void cadastrar(Cliente cliente, String senha) {
        transacao(em -> {
            Date agora = new Date();
            cliente.setAtivo(true);
            cliente.setDataCriacao(agora);
            em.persist(cliente);

            Usuario usuario = new Usuario();
            usuario.setNome(cliente.getXnome().substring(0, Math.min(cliente.getXnome().length(), 100)));
            // O identificador interno evita o limite de 50 caracteres do login existente.
            usuario.setLogin(java.util.UUID.randomUUID().toString());
            usuario.setEmail(cliente.getEmail());
            usuario.setSenha(SenhaCliente.gerar(senha));
            usuario.setAtivo(true);
            usuario.setPerfil(CLIENTE);
            usuario.setLojaClienteId(cliente.getId());
            usuario.setDataCriacao(agora);
            em.persist(usuario);
            return null;
        });
    }

    public Integer finalizar(Integer usuarioId, String referencia, Map<Integer, Integer> carrinho) {
        return transacao(em -> {
            Usuario usuario = em.find(Usuario.class, usuarioId, LockModeType.PESSIMISTIC_WRITE);
            if (!cliente(usuario) || !Boolean.TRUE.equals(usuario.getAtivo()) || usuario.getLojaClienteId() == null) {
                throw new SecurityException();
            }
            List<Integer> anteriores = em.createQuery(
                "SELECT v.id FROM Venda v WHERE v.pedidoLoja = :ref AND v.clienteId.id = :cliente", Integer.class)
                .setParameter("ref", referencia).setParameter("cliente", usuario.getLojaClienteId()).getResultList();
            if (!anteriores.isEmpty()) return anteriores.get(0);
            if (carrinho.isEmpty()) throw new IllegalArgumentException("Seu carrinho está vazio.");

            Date agora = new Date();
            Venda venda = new Venda();
            venda.setPedidoLoja(referencia);
            venda.setDataVenda(agora);
            venda.setDataCriacao(agora);
            venda.setStatus("AGUARDANDO_PIX");
            venda.setClienteId(em.getReference(Cliente.class, usuario.getLojaClienteId()));

            List<ItemVenda> itens = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;
            for (Map.Entry<Integer, Integer> linha : carrinho.entrySet()) {
                if (linha.getValue() < 1 || linha.getValue() > 999) {
                    throw new IllegalArgumentException("Informe uma quantidade de 1 a 999.");
                }
                Produto produto = produtoPublicado(em, linha.getKey());
                ItemVenda item = new ItemVenda();
                item.setVendaId(venda);
                item.setProdutoId(produto);
                item.setQuantidade(BigDecimal.valueOf(linha.getValue()));
                item.setValorUnitario(produto.getPrecoVitrine());
                item.setValorTotal(item.getValorUnitario().multiply(item.getQuantidade()).setScale(2, RoundingMode.HALF_UP));
                item.setDataCriacao(agora);
                itens.add(item);
                total = total.add(item.getValorTotal());
            }
            if (total.signum() <= 0) throw new IllegalArgumentException("O pedido deve ter um total positivo.");
            venda.setItemVendaCollection(itens);
            venda.setValorTotal(total);
            em.persist(venda);
            Empresa empresa = em.createQuery("SELECT e FROM Empresa e ORDER BY e.id", Empresa.class)
                .setMaxResults(1).getResultList().stream().findFirst().orElse(null);
            String pix = PixService.gerarPix(venda, empresa, "PEDIDO " + venda.getId());
            if (!PixService.isChavePixValida(pix)) {
                throw new IllegalStateException("Pix indisponível. Tente novamente mais tarde.");
            }
            venda.setChavePix(pix);
            return venda.getId();
        });
    }

    public List<Venda> pedidos(Integer clienteId) {
        return consultar(em -> {
            TypedQuery<Venda> query = em.createQuery(
                "SELECT DISTINCT v FROM Venda v JOIN FETCH v.clienteId LEFT JOIN FETCH v.itemVendaCollection i "
                    + "LEFT JOIN FETCH i.produtoId WHERE v.pedidoLoja IS NOT NULL "
                    + (clienteId == null ? "" : "AND v.clienteId.id = :cliente ") + "ORDER BY v.id DESC", Venda.class);
            if (clienteId != null) query.setParameter("cliente", clienteId);
            return query.getResultList();
        });
    }

    public boolean possuiDocumento(Integer clienteId, String chave) {
        if (clienteId == null) return false;
        return consultar(em -> em.createQuery(
            "SELECT COUNT(v) FROM Venda v WHERE v.pedidoLoja IS NOT NULL "
                + "AND v.clienteId.id = :cliente AND v.chaveNfe = :chave AND v.protocoloNfe IS NOT NULL", Long.class)
            .setParameter("cliente", clienteId).setParameter("chave", chave).getSingleResult() > 0);
    }

    public void confirmarPix(Integer id, Integer operadorId) {
        transacao(em -> {
            Venda pedido = bloquearPedido(em, id, operadorId);
            if (pedido.getPixConfirmadoEm() == null && "AGUARDANDO_PIX".equals(pedido.getStatus())) {
                pedido.setPixConfirmadoEm(new Date());
                pedido.setStatus("PIX_CONFIRMADO");
            }
            return null;
        });
    }

    public Venda reservarEmissao(Integer id, Integer operadorId) {
        return transacao(em -> {
            Venda pedido = bloquearPedido(em, id, operadorId);
            if (!"PIX_CONFIRMADO".equals(pedido.getStatus())) {
                throw new IllegalStateException("O pedido precisa de Pix confirmado e emissão pendente.");
            }
            Configuracao config = em.createQuery("SELECT c FROM Configuracao c ORDER BY c.id", Configuracao.class)
                .setMaxResults(1).setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultStream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Configure a numeração fiscal."));
            if (config.getNumeroNfe() == null || config.getSerieNfe() == null) {
                throw new IllegalStateException("Configure a numeração fiscal.");
            }
            if (pedido.getNumeroNfe() == null) {
                pedido.setNumeroNfe(config.getNumeroNfe());
                config.setNumeroNfe(config.getNumeroNfe() + 1);
                pedido.setDataVenda(new Date());
            }
            pedido.setStatus("REVISAO_FISCAL");
            // Inicializa as relações usadas pelo emissor após o fechamento da transação.
            pedido.getClienteId().getXnome();
            for (ItemVenda item : pedido.getItemVendaCollection()) item.getProdutoId().getXprod();
            return pedido;
        });
    }

    public void liberarEmissaoNaoEnviada(Integer id, Integer operadorId) {
        transacao(em -> {
            Venda pedido = bloquearPedido(em, id, operadorId);
            if ("REVISAO_FISCAL".equals(pedido.getStatus())) pedido.setStatus("PIX_CONFIRMADO");
            return null;
        });
    }

    public void registrarAutorizacao(Venda autorizado) {
        transacao(em -> {
            Venda pedido = em.find(Venda.class, autorizado.getId(), LockModeType.PESSIMISTIC_WRITE);
            pedido.setChaveNfe(autorizado.getChaveNfe());
            pedido.setProtocoloNfe(autorizado.getProtocoloNfe());
            pedido.setStatus("EMITIDA");
            pedido.setDataAtualizacao(new Date());
            return null;
        });
    }

    public void registrarRetirada(Integer id, Integer operadorId) {
        transacao(em -> {
            Venda pedido = bloquearPedido(em, id, operadorId);
            if (!"EMITIDA".equals(pedido.getStatus())) throw new IllegalStateException("A NF-e precisa estar autorizada.");
            if (pedido.getRetiradoEm() == null) pedido.setRetiradoEm(new Date());
            return null;
        });
    }

    private Venda bloquearPedido(EntityManager em, Integer id, Integer operadorId) {
        Usuario operador = em.find(Usuario.class, operadorId);
        if (!equipe(operador) || !Boolean.TRUE.equals(operador.getAtivo())) throw new SecurityException();
        Venda pedido = em.find(Venda.class, id, LockModeType.PESSIMISTIC_WRITE);
        if (pedido == null || pedido.getPedidoLoja() == null) throw new IllegalArgumentException("Pedido inexistente.");
        return pedido;
    }

    private <T> T consultar(Function<EntityManager, T> operacao) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return operacao.apply(em);
        } finally {
            em.close();
        }
    }

    private <T> T transacao(Function<EntityManager, T> operacao) {
        return consultar(em -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                T resultado = operacao.apply(em);
                tx.commit();
                return resultado;
            } catch (RuntimeException e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        });
    }
}
