package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.UsuarioDAO;
import com.souzamonteiro.nfe.model.Usuario;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginController implements Serializable {
    
    @Inject
    private UsuarioDAO usuarioDAO;
    
    private String login;
    private String senha;
    private Usuario usuarioLogado;
    
    public String login() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            
            // Tentar autenticar via container
            request.login(login, senha);
            
            // Buscar usuário no banco
            usuarioLogado = usuarioDAO.findByLogin(login);
            if (usuarioLogado == null) {
                usuarioLogado = usuarioDAO.findByEmail(login);
            }
            
            if (usuarioLogado != null && usuarioLogado.getAtivo()) {
                return "index?faces-redirect=true";
            } else {
                request.logout();
                context.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Usuário inativo ou não encontrado."));
                return null;
            }
            
        } catch (ServletException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Login ou senha inválidos."));
            return null;
        }
    }
    
    public String logout() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.logout();
            
            usuarioLogado = null;
            login = null;
            senha = null;
            
            return "/login?faces-redirect=true";
        } catch (ServletException e) {
            return "/login?faces-redirect=true";
        }
    }
    
    public boolean isLoggedIn() {
        return usuarioLogado != null;
    }
    
    public boolean isAdmin() {
        return usuarioLogado != null && "ADMIN".equals(usuarioLogado.getPerfil());
    }
    
    // Getters e Setters
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public Usuario getUsuarioLogado() { return usuarioLogado; }
    public void setUsuarioLogado(Usuario usuarioLogado) { this.usuarioLogado = usuarioLogado; }
}