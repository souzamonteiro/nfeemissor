package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "empresa")
public class Empresa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "CNPJ é obrigatório")
    //@CNPJ(message = "CNPJ inválido")
    @Column(name = "cnpj", unique = true, nullable = false, length = 14)
    private String cnpj;
    
    @NotBlank(message = "Razão Social é obrigatória")
    @Column(name = "xnome", nullable = false, length = 200)
    private String xnome;
    
    @NotBlank(message = "Nome Fantasia é obrigatório")
    @Column(name = "xfant", nullable = false, length = 200)
    private String xfant;
    
    @NotBlank(message = "Inscrição Estadual é obrigatória")
    @Column(name = "ie", nullable = false, length = 20)
    private String ie;
    
    @NotBlank(message = "Regime Tributário é obrigatório")
    @Column(name = "crt", nullable = false, length = 1)
    private String crt = "3";
    
    // Endereço
    @NotBlank(message = "Logradouro é obrigatório")
    @Column(name = "xlgr", nullable = false, length = 200)
    private String xlgr;
    
    @NotBlank(message = "Número é obrigatório")
    @Column(name = "nro", nullable = false, length = 10)
    private String nro;
    
    @Column(name = "xcpl", length = 100)
    private String xcpl;
    
    @NotBlank(message = "Bairro é obrigatório")
    @Column(name = "xbairro", nullable = false, length = 100)
    private String xbairro;
    
    @NotBlank(message = "Código do município é obrigatório")
    @Column(name = "cmun", nullable = false, length = 7)
    private String cmun;
    
    @NotBlank(message = "Município é obrigatório")
    @Column(name = "xmun", nullable = false, length = 100)
    private String xmun;
    
    @NotBlank(message = "UF é obrigatória")
    @Column(name = "uf", nullable = false, length = 2)
    private String uf;
    
    @NotBlank(message = "CEP é obrigatório")
    @Column(name = "cep", nullable = false, length = 8)
    private String cep;
    
    @NotBlank(message = "Código do país é obrigatório")
    @Column(name = "cpais", nullable = false, length = 4)
    private String cpais = "1058";
    
    @NotBlank(message = "País é obrigatório")
    @Column(name = "xpais", nullable = false, length = 50)
    private String xpais = "Brasil";
    
    @Column(name = "fone", length = 15)
    private String fone;
    
    @Column(name = "email", length = 100)
    private String email;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    
    public String getXnome() { return xnome; }
    public void setXnome(String xnome) { this.xnome = xnome; }
    
    public String getXfant() { return xfant; }
    public void setXfant(String xfant) { this.xfant = xfant; }
    
    public String getIe() { return ie; }
    public void setIe(String ie) { this.ie = ie; }
    
    public String getCrt() { return crt; }
    public void setCrt(String crt) { this.crt = crt; }
    
    public String getXlgr() { return xlgr; }
    public void setXlgr(String xlgr) { this.xlgr = xlgr; }
    
    public String getNro() { return nro; }
    public void setNro(String nro) { this.nro = nro; }
    
    public String getXcpl() { return xcpl; }
    public void setXcpl(String xcpl) { this.xcpl = xcpl; }
    
    public String getXbairro() { return xbairro; }
    public void setXbairro(String xbairro) { this.xbairro = xbairro; }
    
    public String getCmun() { return cmun; }
    public void setCmun(String cmun) { this.cmun = cmun; }
    
    public String getXmun() { return xmun; }
    public void setXmun(String xmun) { this.xmun = xmun; }
    
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
    
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    
    public String getCpais() { return cpais; }
    public void setCpais(String cpais) { this.cpais = cpais; }
    
    public String getXpais() { return xpais; }
    public void setXpais(String xpais) { this.xpais = xpais; }
    
    public String getFone() { return fone; }
    public void setFone(String fone) { this.fone = fone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}