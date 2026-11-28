package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.UsuarioDAO;
import com.souzamonteiro.nfe.model.Usuario;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.annotation.PostConstruct;
import java.io.Serializable;

@ManagedBean(name = "loginController")
@SessionScoped
public class LoginController implements Serializable {
    
    private String login;
    private String senha;
    private Usuario usuarioLogado;
    private transient UsuarioDAO usuarioDAO;
    
    public LoginController() {
        System.out.println("=== LoginController INSTANCIADO ===");
        this.usuarioDAO = new UsuarioDAO();
    }
    
    @PostConstruct
    public void init() {
        System.out.println("=== LoginController @PostConstruct ===");
        System.out.println("loginController hashcode: " + this.hashCode());
        if (this.usuarioDAO == null) {
            this.usuarioDAO = new UsuarioDAO();
        }
    }

    private UsuarioDAO getUsuarioDAO() {
        if (this.usuarioDAO == null) {
            this.usuarioDAO = new UsuarioDAO();
        }
        return this.usuarioDAO;
    }
    
    public String login() {
        System.out.println("Tentando login com: " + login);
        try {
            // Buscar usuário no banco
            usuarioLogado = getUsuarioDAO().findByLogin(login);

            if (usuarioLogado == null) {
                usuarioLogado = getUsuarioDAO().findByEmail(login);

            }
            
            // Verificar se usuário existe, está ativo e a senha está correta
            if (usuarioLogado != null && 
                usuarioLogado.getAtivo() != null && 
                usuarioLogado.getAtivo() &&
                com.souzamonteiro.nfe.service.LojaService.equipe(usuarioLogado) &&
                usuarioLogado.getSenha() != null && 
                usuarioLogado.getSenha().equals(senha)) {
                
                System.out.println("Login bem sucedido para: " + usuarioLogado.getNome());
                
                javax.servlet.http.HttpServletRequest request =
                    (javax.servlet.http.HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                if (request.getSession(false) != null) request.changeSessionId();

                // Salvar usuário na sessão
                FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("usuarioLogado", usuarioLogado);
                
                // Limpar senha por segurança
                senha = null;
                
                // Login bem sucedido - REDIRECIONAMENTO CORRETO
                return "/index.xhtml?faces-redirect=true";
            } else {
                System.out.println("Login falhou - usuário não encontrado ou senha incorreta");
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Login ou senha inválidos."));
                return null;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Erro ao realizar login: " + e.getMessage()));
            return null;
        }
    }
    
    public String logout() {
        System.out.println("Fazendo logout...");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        usuarioLogado = null;
        login = null;
        senha = null;
        return "/login.xhtml?faces-redirect=true";
    }
    
    // Métodos para o template.xhtml
    public boolean isAdmin() {
        boolean admin = usuarioLogado != null && "ADMIN".equals(usuarioLogado.getPerfil());
        System.out.println("isAdmin() chamado: " + admin);
        return admin;
    }
    
    public boolean isLoggedIn() {
        boolean loggedIn = usuarioLogado != null;
        System.out.println("isLoggedIn() chamado: " + loggedIn);
        return loggedIn;
    }
    
    // Getters para EL expressions (JSF usa getters, não métodos booleanos diretamente)
    public boolean getAdmin() {
        return isAdmin();
    }
    
    public boolean getLoggedIn() {
        return isLoggedIn();
    }
    
    // Getters e Setters
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public Usuario getUsuarioLogado() { return usuarioLogado; }
    public void setUsuarioLogado(Usuario usuarioLogado) { this.usuarioLogado = usuarioLogado; }
}