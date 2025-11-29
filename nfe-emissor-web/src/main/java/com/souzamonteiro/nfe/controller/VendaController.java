package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.*;
import com.souzamonteiro.nfe.model.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class VendaController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private VendaDAO vendaDAO;
    
    @Inject
    private ClienteDAO clienteDAO;
    
    @Inject
    private ProdutoDAO produtoDAO;
    
    @Inject
    private ConfiguracaoDAO configuracaoDAO;
    
    @Inject
    private EmpresaDAO empresaDAO;
    
    private List<Venda> vendas;
    private Venda venda;
    private List<Cliente> clientes;
    private List<Produto> produtos;
    private Cliente clienteSelecionado;
    private Produto produtoSelecionado;
    private ItemVenda itemVenda;
    
    @PostConstruct
    public void init() {
        carregarVendas();
        novaVenda();
        carregarClientes();
        carregarProdutos();
    }
    
    public void carregarVendas() {
        vendas = vendaDAO.findEmitidas();
    }
    
    public void novaVenda() {
        venda = new Venda();
        venda.setDataVenda(LocalDateTime.now());
        venda.setItens(new ArrayList<>());
        itemVenda = new ItemVenda();
    }
    
    public void carregarClientes() {
        clientes = clienteDAO.findAtivos();
    }
    
    public void carregarProdutos() {
        produtos = produtoDAO.findAtivos();
    }
    
    public void adicionarItem() {
        if (produtoSelecionado != null && itemVenda.getQuantidade() != null 
            && itemVenda.getQuantidade().compareTo(BigDecimal.ZERO) > 0) {
            
            // Verificar se produto já está na venda
            boolean produtoJaAdicionado = venda.getItens().stream()
                .anyMatch(item -> item.getProduto().getId().equals(produtoSelecionado.getId()));
            
            if (produtoJaAdicionado) {
                addMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Produto já adicionado à venda.");
                return;
            }
            
            ItemVenda novoItem = new ItemVenda();
            novoItem.setProduto(produtoSelecionado);
            novoItem.setQuantidade(itemVenda.getQuantidade());
            novoItem.setValorUnitario(produtoSelecionado.getVuncom());
            novoItem.calcularTotal();
            
            venda.adicionarItem(novoItem);
            
            // Limpar seleção
            produtoSelecionado = null;
            itemVenda = new ItemVenda();
            
            // Atualização elegante - sem redirect forçado
            addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Produto adicionado à venda.");
        } else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Selecione um produto e informe a quantidade.");
        }
    }
    
    public void removerItem(ItemVenda item) {
        venda.removerItem(item);
        addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Item removido da venda.");
    }
    
    public void finalizarVenda() {
        if (venda.getCliente() == null) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Selecione um cliente.");
            return;
        }
        
        if (venda.getItens().isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Adicione pelo menos um item à venda.");
            return;
        }
        
        try {
            // Gerar número da NF-e
            Integer numeroNFe = vendaDAO.getProximoNumeroNFe();
            venda.setNumeroNFe(numeroNFe);
            
            // Salvar venda
            vendaDAO.save(venda);
            
            // Emitir NF-e
            boolean sucesso = emitirNFe(venda);
            
            if (sucesso) {
                venda.setStatus("EMITIDA");
                vendaDAO.save(venda);
                
                addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", 
                    "Venda finalizada e NF-e emitida com sucesso! Número: " + numeroNFe);
                
                // Nova venda
                novaVenda();
                carregarVendas();
            } else {
                venda.setStatus("ERRO");
                vendaDAO.save(venda);
                
                addMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                    "Erro ao emitir NF-e. Venda salva como pendente.");
            }
            
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                "Erro ao finalizar venda: " + e.getMessage());
        }
    }
    
    public void cancelarVenda() {
        novaVenda();
        addMessage(FacesMessage.SEVERITY_INFO, "Informação", "Venda cancelada.");
    }
    
    private boolean emitirNFe(Venda venda) {
        try {
            Configuracao config = configuracaoDAO.getConfiguracao();
            Empresa empresa = empresaDAO.getEmpresa();
            
            if (config == null || empresa == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                    "Configure empresa e configurações antes de emitir NF-e.");
                return false;
            }
            
            // Construir JSON para envio ao servidor de NF-e
            JSONObject nfeJson = construirJsonNFe(venda, empresa, config);
            
            // Enviar para servidor de NF-e
            String url = "http://localhost:" + config.getPortaServidor() + "/NFeAutorizacao";
            String resposta = enviarParaServidor(url, nfeJson.toString());
            
            // Processar resposta
            JSONObject respostaJson = new JSONObject(resposta);
            String cStat = respostaJson.getString("cStat");
            
            if ("100".equals(cStat)) {
                venda.setChaveNFe(respostaJson.getString("chave"));
                venda.setProtocoloNFe(respostaJson.getString("nProt"));
                return true;
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Erro NF-e", 
                    respostaJson.getString("xMotivo"));
                return false;
            }
            
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                "Erro ao emitir NF-e: " + e.getMessage());
            return false;
        }
    }
    
    // Métodos auxiliares para construção do JSON e comunicação...
    // (manter os métodos construirJsonNFe e enviarParaServidor existentes)
    
    private JSONObject construirJsonNFe(Venda venda, Empresa empresa, Configuracao config) {
        // Implementação existente - manter igual
        return new JSONObject();
    }
    
    private String enviarParaServidor(String urlString, String json) throws Exception {
        // Implementação existente - manter igual
        return "";
    }
    
    private String getCodigoUF(String uf) {
        // Implementação existente - manter igual
        return "29";
    }
    
    // Helper method para mensagens
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(severity, summary, detail));
    }
    
    // Getters e Setters
    public List<Venda> getVendas() { return vendas; }
    public void setVendas(List<Venda> vendas) { this.vendas = vendas; }
    
    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }
    
    public List<Cliente> getClientes() { return clientes; }
    public void setClientes(List<Cliente> clientes) { this.clientes = clientes; }
    
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
    
    public Cliente getClienteSelecionado() { return clienteSelecionado; }
    public void setClienteSelecionado(Cliente clienteSelecionado) { this.clienteSelecionado = clienteSelecionado; }
    
    public Produto getProdutoSelecionado() { return produtoSelecionado; }
    public void setProdutoSelecionado(Produto produtoSelecionado) { this.produtoSelecionado = produtoSelecionado; }
    
    public ItemVenda getItemVenda() { return itemVenda; }
    public void setItemVenda(ItemVenda itemVenda) { this.itemVenda = itemVenda; }
}