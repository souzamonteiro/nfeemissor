package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.ProdutoDAO;
import com.souzamonteiro.nfe.model.Produto;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ProdutoController implements Serializable {
    
    @Inject
    private ProdutoDAO produtoDAO;
    
    private List<Produto> produtos;
    private Produto produto;
    private Produto produtoSelecionado;
    private boolean editando;
    
    @PostConstruct
    public void init() {
        carregarProdutos();
        novoProduto();
    }
    
    public void carregarProdutos() {
        produtos = produtoDAO.findAtivos();
        editando = false;
    }
    
    public void novoProduto() {
        produto = new Produto();
        editando = true;
    }
    
    public void editarProduto() {
        if (produtoSelecionado != null) {
            produto = produtoSelecionado;
            editando = true;
        }
    }
    
    public void salvarProduto() {
        try {
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
                
            PrimeFaces.current().executeScript("PF('produtoDialog').hide()");
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