package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
//import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "cliente")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "xnome", nullable = false, length = 200)
    private String xnome;
    
    //@CPF(message = "CPF inválido")
    @Column(name = "cpf", unique = true, length = 11)
    private String cpf;
    
    //@CNPJ(message = "CNPJ inválido")
    @Column(name = "cnpj", unique = true, length = 14)
    private String cnpj;
    
    @Column(name = "ie", length = 20)
    private String ie;
    
    @Column(name = "indiedest", length = 1)
    private String indIEDest = "9";
    
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
    
    @Column(name = "fone", length = 15)
    private String fone;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "ativo")
    private Boolean ativo = true;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getXnome() { return xnome; }
    public void setXnome(String xnome) { this.xnome = xnome; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    
    public String getIe() { return ie; }
    public void setIe(String ie) { this.ie = ie; }
    
    public String getIndIEDest() { return indIEDest; }
    public void setIndIEDest(String indIEDest) { this.indIEDest = indIEDest; }
    
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
    
    public String getFone() { return fone; }
    public void setFone(String fone) { this.fone = fone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    public String getDocumento() {
        return cpf != null ? cpf : cnpj;
    }
    
    public String getTipoDocumento() {
        return cpf != null ? "F" : "J";
    }
}