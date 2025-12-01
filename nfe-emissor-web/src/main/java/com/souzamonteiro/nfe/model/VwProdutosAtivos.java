/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.souzamonteiro.nfe.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "vw_produtos_ativos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwProdutosAtivos.findAll", query = "SELECT v FROM VwProdutosAtivos v"),
    @NamedQuery(name = "VwProdutosAtivos.findById", query = "SELECT v FROM VwProdutosAtivos v WHERE v.id = :id"),
    @NamedQuery(name = "VwProdutosAtivos.findByCprod", query = "SELECT v FROM VwProdutosAtivos v WHERE v.cprod = :cprod"),
    @NamedQuery(name = "VwProdutosAtivos.findByXprod", query = "SELECT v FROM VwProdutosAtivos v WHERE v.xprod = :xprod"),
    @NamedQuery(name = "VwProdutosAtivos.findByNcm", query = "SELECT v FROM VwProdutosAtivos v WHERE v.ncm = :ncm"),
    @NamedQuery(name = "VwProdutosAtivos.findByVuncom", query = "SELECT v FROM VwProdutosAtivos v WHERE v.vuncom = :vuncom"),
    @NamedQuery(name = "VwProdutosAtivos.findByAtivo", query = "SELECT v FROM VwProdutosAtivos v WHERE v.ativo = :ativo")})
public class VwProdutosAtivos implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "cprod")
    private String cprod;
    
    @Basic(optional = false)
    @Column(name = "xprod")
    private String xprod;
    
    @Basic(optional = false)
    @Column(name = "ncm")
    private String ncm;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "vuncom")
    private BigDecimal vuncom;
    
    @Column(name = "ativo")
    private Boolean ativo;

    public VwProdutosAtivos() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCprod() {
        return cprod;
    }

    public void setCprod(String cprod) {
        this.cprod = cprod;
    }

    public String getXprod() {
        return xprod;
    }

    public void setXprod(String xprod) {
        this.xprod = xprod;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public BigDecimal getVuncom() {
        return vuncom;
    }

    public void setVuncom(BigDecimal vuncom) {
        this.vuncom = vuncom;
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
        if (!(object instanceof VwProdutosAtivos)) {
            return false;
        }
        VwProdutosAtivos other = (VwProdutosAtivos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.souzamonteiro.nfe.model.VwProdutosAtivos[ id=" + id + " ]";
    }
}