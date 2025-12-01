/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.souzamonteiro.nfe.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author roberto
 */
@Entity
@Table(name = "empresa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e"),
    @NamedQuery(name = "Empresa.findById", query = "SELECT e FROM Empresa e WHERE e.id = :id"),
    @NamedQuery(name = "Empresa.findByCnpj", query = "SELECT e FROM Empresa e WHERE e.cnpj = :cnpj"),
    @NamedQuery(name = "Empresa.findByXnome", query = "SELECT e FROM Empresa e WHERE e.xnome = :xnome"),
    @NamedQuery(name = "Empresa.findByXfant", query = "SELECT e FROM Empresa e WHERE e.xfant = :xfant"),
    @NamedQuery(name = "Empresa.findByIe", query = "SELECT e FROM Empresa e WHERE e.ie = :ie"),
    @NamedQuery(name = "Empresa.findByCrt", query = "SELECT e FROM Empresa e WHERE e.crt = :crt"),
    @NamedQuery(name = "Empresa.findByXlgr", query = "SELECT e FROM Empresa e WHERE e.xlgr = :xlgr"),
    @NamedQuery(name = "Empresa.findByNro", query = "SELECT e FROM Empresa e WHERE e.nro = :nro"),
    @NamedQuery(name = "Empresa.findByXcpl", query = "SELECT e FROM Empresa e WHERE e.xcpl = :xcpl"),
    @NamedQuery(name = "Empresa.findByXbairro", query = "SELECT e FROM Empresa e WHERE e.xbairro = :xbairro"),
    @NamedQuery(name = "Empresa.findByCmun", query = "SELECT e FROM Empresa e WHERE e.cmun = :cmun"),
    @NamedQuery(name = "Empresa.findByXmun", query = "SELECT e FROM Empresa e WHERE e.xmun = :xmun"),
    @NamedQuery(name = "Empresa.findByUf", query = "SELECT e FROM Empresa e WHERE e.uf = :uf"),
    @NamedQuery(name = "Empresa.findByCep", query = "SELECT e FROM Empresa e WHERE e.cep = :cep"),
    @NamedQuery(name = "Empresa.findByCpais", query = "SELECT e FROM Empresa e WHERE e.cpais = :cpais"),
    @NamedQuery(name = "Empresa.findByXpais", query = "SELECT e FROM Empresa e WHERE e.xpais = :xpais"),
    @NamedQuery(name = "Empresa.findByFone", query = "SELECT e FROM Empresa e WHERE e.fone = :fone"),
    @NamedQuery(name = "Empresa.findByEmail", query = "SELECT e FROM Empresa e WHERE e.email = :email"),
    @NamedQuery(name = "Empresa.findByDataCriacao", query = "SELECT e FROM Empresa e WHERE e.dataCriacao = :dataCriacao"),
    @NamedQuery(name = "Empresa.findByDataAtualizacao", query = "SELECT e FROM Empresa e WHERE e.dataAtualizacao = :dataAtualizacao")})
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "cnpj")
    private String cnpj;
    @Basic(optional = false)
    @Column(name = "xnome")
    private String xnome;
    @Basic(optional = false)
    @Column(name = "xfant")
    private String xfant;
    @Basic(optional = false)
    @Column(name = "ie")
    private String ie;
    @Basic(optional = false)
    @Column(name = "crt")
    private String crt;
    @Basic(optional = false)
    @Column(name = "xlgr")
    private String xlgr;
    @Basic(optional = false)
    @Column(name = "nro")
    private String nro;
    @Column(name = "xcpl")
    private String xcpl;
    @Basic(optional = false)
    @Column(name = "xbairro")
    private String xbairro;
    @Basic(optional = false)
    @Column(name = "cmun")
    private String cmun;
    @Basic(optional = false)
    @Column(name = "xmun")
    private String xmun;
    @Basic(optional = false)
    @Column(name = "uf")
    private String uf;
    @Basic(optional = false)
    @Column(name = "cep")
    private String cep;
    @Column(name = "cpais")
    private String cpais;
    @Column(name = "xpais")
    private String xpais;
    @Column(name = "fone")
    private String fone;
    @Column(name = "email")
    private String email;
    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @Column(name = "data_atualizacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    public Empresa() {
    }

    public Empresa(Integer id) {
        this.id = id;
    }

    public Empresa(Integer id, String cnpj, String xnome, String xfant, String ie, String crt, String xlgr, String nro, String xbairro, String cmun, String xmun, String uf, String cep) {
        this.id = id;
        this.cnpj = cnpj;
        this.xnome = xnome;
        this.xfant = xfant;
        this.ie = ie;
        this.crt = crt;
        this.xlgr = xlgr;
        this.nro = nro;
        this.xbairro = xbairro;
        this.cmun = cmun;
        this.xmun = xmun;
        this.uf = uf;
        this.cep = cep;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getXnome() {
        return xnome;
    }

    public void setXnome(String xnome) {
        this.xnome = xnome;
    }

    public String getXfant() {
        return xfant;
    }

    public void setXfant(String xfant) {
        this.xfant = xfant;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getCrt() {
        return crt;
    }

    public void setCrt(String crt) {
        this.crt = crt;
    }

    public String getXlgr() {
        return xlgr;
    }

    public void setXlgr(String xlgr) {
        this.xlgr = xlgr;
    }

    public String getNro() {
        return nro;
    }

    public void setNro(String nro) {
        this.nro = nro;
    }

    public String getXcpl() {
        return xcpl;
    }

    public void setXcpl(String xcpl) {
        this.xcpl = xcpl;
    }

    public String getXbairro() {
        return xbairro;
    }

    public void setXbairro(String xbairro) {
        this.xbairro = xbairro;
    }

    public String getCmun() {
        return cmun;
    }

    public void setCmun(String cmun) {
        this.cmun = cmun;
    }

    public String getXmun() {
        return xmun;
    }

    public void setXmun(String xmun) {
        this.xmun = xmun;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCpais() {
        return cpais;
    }

    public void setCpais(String cpais) {
        this.cpais = cpais;
    }

    public String getXpais() {
        return xpais;
    }

    public void setXpais(String xpais) {
        this.xpais = xpais;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.souzamonteiro.nfe.model.Empresa[ id=" + id + " ]";
    }
    
}
