package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.ConfiguracaoDAO;
import com.souzamonteiro.nfe.model.Configuracao;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class ConfiguracaoController implements Serializable {
    
    @Inject
    private ConfiguracaoDAO configuracaoDAO;
    
    private Configuracao configuracao;
    
    @PostConstruct
    public void init() {
        carregarConfiguracao();
    }
    
    public void carregarConfiguracao() {
        configuracao = configuracaoDAO.getConfiguracao();
        if (configuracao == null) {
            configuracao = new Configuracao();
        }
    }
    
    public void salvarConfiguracao() {
        try {
            configuracaoDAO.save(configuracao);
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Sucesso", "Configurações salvas com sucesso."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Erro ao salvar configurações: " + e.getMessage()));
        }
    }
    
    // Getters e Setters
    public Configuracao getConfiguracao() { return configuracao; }
    public void setConfiguracao(Configuracao configuracao) { this.configuracao = configuracao; }
}