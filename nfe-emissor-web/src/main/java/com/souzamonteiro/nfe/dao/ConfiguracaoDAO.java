package com.souzamonteiro.nfe.dao;

import com.souzamonteiro.nfe.model.Configuracao;
//import jakarta.ejb.Stateless;

//@Stateless
public class ConfiguracaoDAO extends GenericDAO<Configuracao, Long> {
    
    public ConfiguracaoDAO() {
        super(Configuracao.class);
    }
    
    @Override
    protected boolean isNew(Configuracao configuracao) {
        return configuracao.getId() == null;
    }
    
    public Configuracao getConfiguracao() {
        try {
            return findAll().get(0);
        } catch (Exception e) {
            return null;
        }
    }
}