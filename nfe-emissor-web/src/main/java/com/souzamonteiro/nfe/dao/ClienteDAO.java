package com.souzamonteiro.nfe.dao;

import com.souzamonteiro.nfe.model.Cliente;
//import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;

//@Stateless
public class ClienteDAO extends GenericDAO<Cliente, Long> {
    
    public ClienteDAO() {
        super(Cliente.class);
    }
    
    @Override
    protected boolean isNew(Cliente cliente) {
        return cliente.getId() == null;
    }
    
    public List<Cliente> findAtivos() {
        TypedQuery<Cliente> query = em.createQuery(
            "SELECT c FROM Cliente c WHERE c.ativo = true ORDER BY c.xnome", 
            Cliente.class
        );
        return query.getResultList();
    }
    
    public Cliente findByDocumento(String documento) {
        try {
            TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c WHERE c.cpf = :doc OR c.cnpj = :doc", 
                Cliente.class
            );
            query.setParameter("doc", documento);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}