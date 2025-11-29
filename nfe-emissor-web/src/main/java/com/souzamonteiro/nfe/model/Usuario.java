package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    
    @NotBlank(message = "Login é obrigatório")
    @Column(name = "login", unique = true, nullable = false, length = 50)
    private String login;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    @Column(name = "senha", nullable = false, length = 255)
    private String senha;
    
    @NotBlank(message = "Perfil é obrigatório")
    @Column(name = "perfil", nullable = false, length = 20)
    private String perfil = "USER";
    
    @Column(name = "ativo")
    private Boolean ativo = true;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    public boolean isAdmin() {
        return "ADMIN".equals(perfil);
    }
}