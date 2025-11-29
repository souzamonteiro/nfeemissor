package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.EmpresaDAO;
import com.souzamonteiro.nfe.model.Empresa;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class EmpresaController implements Serializable {
    
    @Inject
    private EmpresaDAO empresaDAO;
    
    private Empresa empresa;
    
    @PostConstruct
    public void init() {
        carregarEmpresa();
    }
    
    public void carregarEmpresa() {
        empresa = empresaDAO.getEmpresa();
        if (empresa == null) {
            empresa = new Empresa();
        }
    }
    
    public void salvarEmpresa() {
        try {
            empresaDAO.save(empresa);
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Sucesso", "Dados da empresa salvos com sucesso."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Erro ao salvar dados da empresa: " + e.getMessage()));
        }
    }
    
    // Getters e Setters
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
}