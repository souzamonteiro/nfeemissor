package com.souzamonteiro.nfe.dao;

import com.souzamonteiro.nfe.model.Usuario;
//import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;

//@Stateless
public class UsuarioDAO extends GenericDAO<Usuario, Long> {
    
    public UsuarioDAO() {
        super(Usuario.class);
    }
    
    @Override
    protected boolean isNew(Usuario usuario) {
        return usuario.getId() == null;
    }
    
    public List<Usuario> findAtivos() {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.ativo = true ORDER BY u.nome", 
            Usuario.class
        );
        return query.getResultList();
    }
    
    public Usuario findByLogin(String login) {
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.login = :login AND u.ativo = true", 
                Usuario.class
            );
            query.setParameter("login", login);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Usuario findByEmail(String email) {
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email AND u.ativo = true", 
                Usuario.class
            );
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}