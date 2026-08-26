package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.ProdutoDAO;
import com.souzamonteiro.nfe.model.Produto;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.annotation.PostConstruct;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ManagedBean
@ViewScoped
public class ProdutoController implements Serializable {
    
    private transient ProdutoDAO produtoDAO = new ProdutoDAO();
    private List<Produto> produtos;
    private Produto produto;
    private Produto produtoSelecionado;
    private boolean editando = false; // Inicialize como false
    
    @PostConstruct
    public void init() {
        carregarProdutos();
    }
    
    public void carregarProdutos() {
        produtos = produtoDAO.findAtivos();
        editando = false;
        produtoSelecionado = null;
        produto = null;
    }
    
    public void novoProduto() {
        produto = new Produto();
        produto.setAtivo(true);
        produto.setOrig("0");
        produto.setCstIcms("00");
        produto.setModbcIcms("3");
        produto.setPredbcIcms(BigDecimal.ZERO);
        produto.setPicms(new BigDecimal("18.00"));
        produto.setVbcst(BigDecimal.ZERO);
        produto.setPst(BigDecimal.ZERO);
        produto.setVicmsst(BigDecimal.ZERO);
        produto.setVicmsdeson(BigDecimal.ZERO);
        produto.setMotDesIcms("9");
        produto.setVbcstret(BigDecimal.ZERO);
        produto.setVicmssubstituto(BigDecimal.ZERO);
        produto.setVicmsstret(BigDecimal.ZERO);
        produto.setCstPis("01");
        produto.setPpis(new BigDecimal("1.65"));
        produto.setCstCofins("01");
        produto.setPcofins(new BigDecimal("7.60"));
        produto.setCstRtc("000");
        produto.setCclasstribRtc("000001");
        produto.setPibsUf(new BigDecimal("0.10"));
        produto.setPibsMun(BigDecimal.ZERO);
        produto.setPcbs(new BigDecimal("0.90"));
        editando = true;
        produtoSelecionado = null;
    }
    
    public void editarProduto() {
        if (produtoSelecionado != null) {
            produto = produtoSelecionado;
            if (produto.getModbcIcms() == null || produto.getModbcIcms().trim().isEmpty()) {
                produto.setModbcIcms("3");
            }
            if (produto.getPredbcIcms() == null) {
                produto.setPredbcIcms(BigDecimal.ZERO);
            }
            if (produto.getVbcst() == null) {
                produto.setVbcst(BigDecimal.ZERO);
            }
            if (produto.getPst() == null) {
                produto.setPst(BigDecimal.ZERO);
            }
            if (produto.getVicmsst() == null) {
                produto.setVicmsst(BigDecimal.ZERO);
            }
            if (produto.getVicmsdeson() == null) {
                produto.setVicmsdeson(BigDecimal.ZERO);
            }
            if (produto.getMotDesIcms() == null || produto.getMotDesIcms().trim().isEmpty()) {
                produto.setMotDesIcms("9");
            }
            if (produto.getVbcstret() == null) {
                produto.setVbcstret(BigDecimal.ZERO);
            }
            if (produto.getVicmssubstituto() == null) {
                produto.setVicmssubstituto(BigDecimal.ZERO);
            }
            if (produto.getVicmsstret() == null) {
                produto.setVicmsstret(BigDecimal.ZERO);
            }
            if (produto.getCstRtc() == null || produto.getCstRtc().trim().isEmpty()) {
                produto.setCstRtc("000");
            }
            if (produto.getCclasstribRtc() == null || produto.getCclasstribRtc().trim().isEmpty()) {
                produto.setCclasstribRtc("000001");
            }
            if (produto.getPibsUf() == null) {
                produto.setPibsUf(new BigDecimal("0.10"));
            }
            if (produto.getPibsMun() == null) {
                produto.setPibsMun(BigDecimal.ZERO);
            }
            if (produto.getPcbs() == null) {
                produto.setPcbs(new BigDecimal("0.90"));
            }
            editando = true;
        } else {
            FacesContext.getCurrentInstance().addMessage("form:growl",
                new FacesMessage(FacesMessage.SEVERITY_WARN, 
                "Aviso", "Selecione um produto para editar."));
        }
    }
    
    public void salvarProduto() {
        try {
            if (produto.getModbcIcms() == null || produto.getModbcIcms().trim().isEmpty()) {
                produto.setModbcIcms("3");
            }
            if (produto.getPredbcIcms() == null) {
                produto.setPredbcIcms(BigDecimal.ZERO);
            }
            if (produto.getVbcst() == null) {
                produto.setVbcst(BigDecimal.ZERO);
            }
            if (produto.getPst() == null) {
                produto.setPst(BigDecimal.ZERO);
            }
            if (produto.getVicmsst() == null) {
                produto.setVicmsst(BigDecimal.ZERO);
            }
            if (produto.getVicmsdeson() == null) {
                produto.setVicmsdeson(BigDecimal.ZERO);
            }
            if (produto.getMotDesIcms() == null || produto.getMotDesIcms().trim().isEmpty()) {
                produto.setMotDesIcms("9");
            }
            if (produto.getVbcstret() == null) {
                produto.setVbcstret(BigDecimal.ZERO);
            }
            if (produto.getVicmssubstituto() == null) {
                produto.setVicmssubstituto(BigDecimal.ZERO);
            }
            if (produto.getVicmsstret() == null) {
                produto.setVicmsstret(BigDecimal.ZERO);
            }
            if (produto.getCstRtc() == null || produto.getCstRtc().trim().isEmpty()) {
                produto.setCstRtc("000");
            }
            if (produto.getCclasstribRtc() == null || produto.getCclasstribRtc().trim().isEmpty()) {
                produto.setCclasstribRtc("000001");
            }
            if (produto.getPibsUf() == null) {
                produto.setPibsUf(new BigDecimal("0.10"));
            }
            if (produto.getPibsMun() == null) {
                produto.setPibsMun(BigDecimal.ZERO);
            }
            if (produto.getPcbs() == null) {
                produto.setPcbs(new BigDecimal("0.90"));
            }

            // Verificar se código já existe
            Produto existente = produtoDAO.findByCodigo(produto.getCprod());
            if (existente != null && !existente.getId().equals(produto.getId())) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Já existe um produto com este código."));
                return;
            }
            
            produtoDAO.save(produto);
            carregarProdutos();
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Sucesso", "Produto salvo com sucesso."));
                
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Erro ao salvar produto: " + e.getMessage()));
        }
    }
    
    public void excluirProduto() {
        if (produtoSelecionado != null) {
            try {
                produtoSelecionado.setAtivo(false);
                produtoDAO.save(produtoSelecionado);
                carregarProdutos();
                
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Sucesso", "Produto excluído com sucesso."));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Erro ao excluir produto: " + e.getMessage()));
            }
        }
    }
    
    public void cancelarEdicao() {
        carregarProdutos();
    }
    
    // Métodos para manipular seleção no dataTable
    public void onRowSelect(SelectEvent<Produto> event) {
        produtoSelecionado = event.getObject();
        FacesContext.getCurrentInstance().addMessage("form:growl",
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
            "Selecionado", "Usuário selecionado: " + produtoSelecionado.getXprod()));
    }
    
    public void onRowUnselect(UnselectEvent<Produto> event) {
        produtoSelecionado = null;
    }
    
    // Getters e Setters
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
    
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    
    public Produto getProdutoSelecionado() { return produtoSelecionado; }
    public void setProdutoSelecionado(Produto produtoSelecionado) { this.produtoSelecionado = produtoSelecionado; }
    
    public boolean isEditando() { return editando; }
    public void setEditando(boolean editando) { this.editando = editando; }
}