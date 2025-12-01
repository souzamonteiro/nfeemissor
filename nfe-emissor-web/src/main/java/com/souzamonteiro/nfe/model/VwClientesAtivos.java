/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.souzamonteiro.nfe.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author roberto
 */
@Entity
@Table(name = "vw_clientes_ativos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwClientesAtivos.findAll", query = "SELECT v FROM VwClientesAtivos v"),
    @NamedQuery(name = "VwClientesAtivos.findById", query = "SELECT v FROM VwClientesAtivos v WHERE v.id = :id"),
    @NamedQuery(name = "VwClientesAtivos.findByXnome", query = "SELECT v FROM VwClientesAtivos v WHERE v.xnome = :xnome"),
    @NamedQuery(name = "VwClientesAtivos.findByDocumento", query = "SELECT v FROM VwClientesAtivos v WHERE v.documento = :documento"),
    @NamedQuery(name = "VwClientesAtivos.findByXmun", query = "SELECT v FROM VwClientesAtivos v WHERE v.xmun = :xmun"),
    @NamedQuery(name = "VwClientesAtivos.findByUf", query = "SELECT v FROM VwClientesAtivos v WHERE v.uf = :uf"),
    @NamedQuery(name = "VwClientesAtivos.findByAtivo", query = "SELECT v FROM VwClientesAtivos v WHERE v.ativo = :ativo")})
public class VwClientesAtivos implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "xnome")
    private String xnome;
    
    @Column(name = "documento")
    private String documento;
    
    @Basic(optional = false)
    @Column(name = "xmun")
    private String xmun;
    
    @Basic(optional = false)
    @Column(name = "uf")
    private String uf;
    
    @Column(name = "ativo")
    private Boolean ativo;

    public VwClientesAtivos() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getXnome() {
        return xnome;
    }

    public void setXnome(String xnome) {
        this.xnome = xnome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof VwClientesAtivos)) {
            return false;
        }
        VwClientesAtivos other = (VwClientesAtivos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.souzamonteiro.nfe.model.VwClientesAtivos[ id=" + id + " ]";
    }
}