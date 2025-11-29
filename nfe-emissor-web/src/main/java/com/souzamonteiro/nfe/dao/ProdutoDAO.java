package com.souzamonteiro.nfe.dao;

import com.souzamonteiro.nfe.model.Produto;
//import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;

//@Stateless
public class ProdutoDAO extends GenericDAO<Produto, Long> {
    
    public ProdutoDAO() {
        super(Produto.class);
    }
    
    @Override
    protected boolean isNew(Produto produto) {
        return produto.getId() == null;
    }
    
    public List<Produto> findAtivos() {
        TypedQuery<Produto> query = em.createQuery(
            "SELECT p FROM Produto p WHERE p.ativo = true ORDER BY p.xprod", 
            Produto.class
        );
        return query.getResultList();
    }
    
    public Produto findByCodigo(String codigo) {
        try {
            TypedQuery<Produto> query = em.createQuery(
                "SELECT p FROM Produto p WHERE p.cprod = :codigo", 
                Produto.class
            );
            query.setParameter("codigo", codigo);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}