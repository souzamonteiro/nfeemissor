package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.UsuarioDAO;
import com.souzamonteiro.nfe.model.Cliente;
import com.souzamonteiro.nfe.model.Produto;
import com.souzamonteiro.nfe.model.ServicoEntrega;
import com.souzamonteiro.nfe.model.Usuario;
import com.souzamonteiro.nfe.model.Venda;
import com.souzamonteiro.nfe.service.LojaService;
import com.souzamonteiro.nfe.service.SenhaCliente;
import com.souzamonteiro.nfe.util.GeradorQRCodePix;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class LojaController implements Serializable {
    private static final Logger LOG = Logger.getLogger(LojaController.class.getName());
    private final Map<Integer, Integer> carrinho = new LinkedHashMap<>();
    private Integer compraDireta;
    private Integer servicoEntregaId = 0;
    private BigDecimal freteExibido;
    private final Map<Integer, String> rastreios = new LinkedHashMap<>();
    private String referencia = UUID.randomUUID().toString();
    private String busca;
    private String categoria;
    private String email;
    private transient String senha;
    private Cliente cadastro = new Cliente();

    public List<Produto> getCatalogo() { return new LojaService().catalogo(busca, categoria); }
    public List<String> getCategorias() { return new LojaService().categorias(); }
    public String getBusca() { return busca; }
    public void setBusca(String busca) { this.busca = busca; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Cliente getCadastro() { return cadastro; }
    public boolean isCliente() { return LojaService.cliente(usuario()); }
    public boolean isEquipe() { return LojaService.equipe(usuario()); }
    public synchronized int getQuantidadeCarrinho() { return carrinho.values().stream().mapToInt(Integer::intValue).sum(); }
    public Integer getServicoEntregaId() { return servicoEntregaId; }
    public void setServicoEntregaId(Integer servicoEntregaId) { this.servicoEntregaId = servicoEntregaId; }
    public Map<Integer, String> getRastreios() { return rastreios; }

    public List<ServicoEntrega> getOpcoesEntrega() {
        Usuario conta = usuario();
        return LojaService.cliente(conta) ? new LojaService().opcoesEntrega(conta.getLojaClienteId()) : Collections.emptyList();
    }

    public BigDecimal getFreteSelecionado() {
        if (servicoEntregaId == null || servicoEntregaId == 0) return BigDecimal.ZERO;
        freteExibido = getOpcoesEntrega().stream().filter(opcao -> servicoEntregaId.equals(opcao.getId()))
            .map(ServicoEntrega::getValor).findFirst().orElse(null);
        return freteExibido == null ? BigDecimal.ZERO : freteExibido;
    }

    public synchronized boolean isCompraDireta() { return compraDireta != null; }

    public synchronized void adicionar(Integer id) {
        executar(() -> {
            new LojaService().produto(id);
            int quantidade = carrinho.getOrDefault(id, 0);
            if (quantidade >= 999) throw new IllegalArgumentException("Limite de 999 unidades por produto.");
            carrinho.put(id, quantidade + 1);
            informar("Produto adicionado ao carrinho.");
        });
    }

    public synchronized String comprar(Integer id) {
        new LojaService().produto(id);
        compraDireta = id;
        servicoEntregaId = 0;
        referencia = UUID.randomUUID().toString();
        return "/loja/carrinho.xhtml?faces-redirect=true";
    }

    public synchronized String abrirCarrinho() {
        compraDireta = null;
        servicoEntregaId = 0;
        referencia = UUID.randomUUID().toString();
        return "/loja/carrinho.xhtml?faces-redirect=true";
    }

    public synchronized void alterar(Integer id, int incremento) {
        int quantidade = carrinho.getOrDefault(id, 0) + incremento;
        if (quantidade <= 0) carrinho.remove(id);
        else if (quantidade <= 999) carrinho.put(id, quantidade);
    }

    public synchronized void remover(Integer id) { carrinho.remove(id); }

    public synchronized List<Linha> getLinhas() {
        List<Linha> linhas = new ArrayList<>();
        LojaService loja = new LojaService();
        for (Map.Entry<Integer, Integer> item : itens().entrySet()) {
            Produto produto;
            try {
                produto = loja.produto(item.getKey());
            } catch (IllegalArgumentException e) {
                produto = null;
            }
            linhas.add(new Linha(item.getKey(), item.getValue(), produto));
        }
        return linhas;
    }

    public BigDecimal getTotal() {
        return getLinhas().stream().map(Linha::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add)
            .add(getFreteSelecionado());
    }

    public synchronized String finalizar() {
        if (!isCliente()) return "/loja/entrar.xhtml?faces-redirect=true";
        try {
            Integer id = new LojaService().finalizar(usuario().getId(), referencia, itens(), servicoEntregaId, freteExibido);
            if (compraDireta == null) carrinho.clear();
            compraDireta = null;
            servicoEntregaId = 0;
            referencia = UUID.randomUUID().toString();
            return "/loja/pedidos.xhtml?faces-redirect=true&pedido=" + id;
        } catch (RuntimeException e) {
            falha(e);
            return null;
        }
    }

    private Map<Integer, Integer> itens() {
        return compraDireta == null ? new LinkedHashMap<>(carrinho) : Collections.singletonMap(compraDireta, 1);
    }

    public String criarConta() {
        try {
            String documento = cadastro.getCpf().replaceAll("[. /-]", "");
            if (!documento.matches("[0-9]{11}|[0-9]{14}")) {
                throw new IllegalArgumentException("Informe um CPF ou CNPJ válido.");
            }
            if (!cadastro.getCep().matches("[0-9]{8}") || !cadastro.getCmun().matches("[0-9]{7}")) {
                throw new IllegalArgumentException("Confira o CEP e o código IBGE do município.");
            }
            cadastro.setCpf(documento.length() == 11 ? documento : null);
            cadastro.setCnpj(documento.length() == 14 ? documento : null);
            cadastro.setUf(cadastro.getUf().toUpperCase(Locale.ROOT));
            cadastro.setEmail(cadastro.getEmail().trim().toLowerCase(Locale.ROOT));
            cadastro.setIndiedest("9");
            UsuarioDAO usuarios = new UsuarioDAO();
            if (usuarios.findByEmail(cadastro.getEmail()) != null) {
                throw new IllegalArgumentException("Já existe uma conta com este e-mail.");
            }
            new LojaService().cadastrar(cadastro, senha);
            email = cadastro.getEmail();
            cadastro = new Cliente();
            informar("Conta criada. Entre para continuar.");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "/loja/entrar.xhtml?faces-redirect=true";
        } catch (RuntimeException e) {
            cadastro.setId(null);
            falha(e);
            return null;
        } finally {
            senha = null;
        }
    }

    public String entrar() {
        try {
            Usuario conta = new UsuarioDAO().findByEmail(email.trim().toLowerCase(Locale.ROOT));
            if (!LojaService.cliente(conta) || !Boolean.TRUE.equals(conta.getAtivo())
                    || !SenhaCliente.conferir(senha, conta.getSenha())) {
                throw new IllegalArgumentException("E-mail ou senha inválidos.");
            }
            ExternalContext externo = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) externo.getRequest();
            request.getSession();
            request.changeSessionId();
            externo.getSessionMap().remove("loginController");
            externo.getSessionMap().put("usuarioLogado", conta);
            return (compraDireta != null || !carrinho.isEmpty() ? "/loja/carrinho.xhtml" : "/loja/pedidos.xhtml")
                + "?faces-redirect=true";
        } catch (RuntimeException e) {
            falha(e);
            return null;
        } finally {
            senha = null;
        }
    }

    public String sair() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/loja/index.xhtml?faces-redirect=true";
    }

    public List<Venda> getMeusPedidos() {
        Usuario conta = usuario();
        if (!LojaService.cliente(conta) || conta.getLojaClienteId() == null) return Collections.emptyList();
        return new LojaService().pedidos(conta.getLojaClienteId());
    }

    public List<Venda> getPedidosEquipe() {
        return isEquipe() ? new LojaService().pedidos(null) : Collections.emptyList();
    }

    public void confirmar(Integer id) {
        operar(() -> new LojaService().confirmarPix(id, usuario().getId()));
    }

    public void emitir(Integer id) {
        operar(() -> VendaController.emitirPedidoLoja(id, usuario().getId()));
    }

    public void retirar(Integer id) {
        operar(() -> new LojaService().registrarRetirada(id, usuario().getId()));
    }

    public void enviar(Integer id) {
        operar(() -> {
            new LojaService().registrarEnvio(id, usuario().getId(), rastreios.get(id));
            rastreios.remove(id);
        });
    }

    public void entregar(Integer id) {
        operar(() -> new LojaService().registrarEntrega(id, usuario().getId()));
    }

    public String qrCode(Venda pedido) {
        try {
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(
                GeradorQRCodePix.gerarQRCodeBytes(pedido.getChavePix(), 220));
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Falha ao gerar QR Code do pedido " + pedido.getId(), e);
            return "";
        }
    }

    private void operar(Runnable acao) {
        if (!isEquipe()) throw new SecurityException();
        executar(acao);
    }

    private void executar(Runnable acao) {
        try {
            acao.run();
        } catch (RuntimeException e) {
            falha(e);
        }
    }

    private static Usuario usuario() {
        return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
    }

    private static void informar(String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, null));
    }

    private static void falha(RuntimeException e) {
        String msg = "Não foi possível concluir a operação.";
        if (e instanceof IllegalArgumentException || e instanceof IllegalStateException) msg = e.getMessage();
        else LOG.log(Level.WARNING, msg, e);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    public static class Linha {
        private final Integer id;
        private final int quantidade;
        private final Produto produto;

        Linha(Integer id, int quantidade, Produto produto) {
            this.id = id;
            this.quantidade = quantidade;
            this.produto = produto;
        }

        public Integer getId() { return id; }
        public int getQuantidade() { return quantidade; }
        public Produto getProduto() { return produto; }
        public BigDecimal getTotal() {
            return produto == null ? BigDecimal.ZERO
                : produto.getPrecoVitrine().multiply(BigDecimal.valueOf(quantidade)).setScale(2, RoundingMode.HALF_UP);
        }
    }
}
