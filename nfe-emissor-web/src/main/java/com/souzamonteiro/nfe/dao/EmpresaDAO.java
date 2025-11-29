package com.souzamonteiro.nfe.dao;

import com.souzamonteiro.nfe.model.Empresa;
//import jakarta.ejb.Stateless;

//@Stateless
public class EmpresaDAO extends GenericDAO<Empresa, Long> {
    
    public EmpresaDAO() {
        super(Empresa.class);
    }
    
    @Override
    protected boolean isNew(Empresa empresa) {
        return empresa.getId() == null;
    }
    
    public Empresa getEmpresa() {
        try {
            return findAll().get(0);
        } catch (Exception e) {
            return null;
        }
    }
}