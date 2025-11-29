package com.souzamonteiro.nfe.dao;

import com.souzamonteiro.nfe.model.Venda;
//import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;

//@Stateless
public class VendaDAO extends GenericDAO<Venda, Long> {
    
    public VendaDAO() {
        super(Venda.class);
    }
    
    @Override
    protected boolean isNew(Venda venda) {
        return venda.getId() == null;
    }
    
    public List<Venda> findPendentes() {
        TypedQuery<Venda> query = em.createQuery(
            "SELECT v FROM Venda v WHERE v.status = 'PENDENTE' ORDER BY v.dataVenda", 
            Venda.class
        );
        return query.getResultList();
    }
    
    public List<Venda> findEmitidas() {
        TypedQuery<Venda> query = em.createQuery(
            "SELECT v FROM Venda v WHERE v.status = 'EMITIDA' ORDER BY v.dataVenda DESC", 
            Venda.class
        );
        return query.getResultList();
    }
    
    public Integer getProximoNumeroNFe() {
        try {
            TypedQuery<Integer> query = em.createQuery(
                "SELECT COALESCE(MAX(v.numeroNFe), 0) + 1 FROM Venda v WHERE v.numeroNFe IS NOT NULL", 
                Integer.class
            );
            return query.getSingleResult();
        } catch (Exception e) {
            return 1;
        }
    }
}