/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.souzamonteiro.nfe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author roberto
 */
@Entity
@Table(name = "produto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Produto.findAll", query = "SELECT p FROM Produto p"),
    @NamedQuery(name = "Produto.findById", query = "SELECT p FROM Produto p WHERE p.id = :id"),
    @NamedQuery(name = "Produto.findByCprod", query = "SELECT p FROM Produto p WHERE p.cprod = :cprod"),
    @NamedQuery(name = "Produto.findByCean", query = "SELECT p FROM Produto p WHERE p.cean = :cean"),
    @NamedQuery(name = "Produto.findByXprod", query = "SELECT p FROM Produto p WHERE p.xprod = :xprod"),
    @NamedQuery(name = "Produto.findByNcm", query = "SELECT p FROM Produto p WHERE p.ncm = :ncm"),
    @NamedQuery(name = "Produto.findByCest", query = "SELECT p FROM Produto p WHERE p.cest = :cest"),
    @NamedQuery(name = "Produto.findByCfop", query = "SELECT p FROM Produto p WHERE p.cfop = :cfop"),
    @NamedQuery(name = "Produto.findByUcom", query = "SELECT p FROM Produto p WHERE p.ucom = :ucom"),
    @NamedQuery(name = "Produto.findByQcom", query = "SELECT p FROM Produto p WHERE p.qcom = :qcom"),
    @NamedQuery(name = "Produto.findByVuncom", query = "SELECT p FROM Produto p WHERE p.vuncom = :vuncom"),
    @NamedQuery(name = "Produto.findByVprod", query = "SELECT p FROM Produto p WHERE p.vprod = :vprod"),
    @NamedQuery(name = "Produto.findByCeantrib", query = "SELECT p FROM Produto p WHERE p.ceantrib = :ceantrib"),
    @NamedQuery(name = "Produto.findByUtrib", query = "SELECT p FROM Produto p WHERE p.utrib = :utrib"),
    @NamedQuery(name = "Produto.findByQtrib", query = "SELECT p FROM Produto p WHERE p.qtrib = :qtrib"),
    @NamedQuery(name = "Produto.findByVuntrib", query = "SELECT p FROM Produto p WHERE p.vuntrib = :vuntrib"),
    @NamedQuery(name = "Produto.findByIndtot", query = "SELECT p FROM Produto p WHERE p.indtot = :indtot"),
    @NamedQuery(name = "Produto.findByOrig", query = "SELECT p FROM Produto p WHERE p.orig = :orig"),
    @NamedQuery(name = "Produto.findByCstIcms", query = "SELECT p FROM Produto p WHERE p.cstIcms = :cstIcms"),
    @NamedQuery(name = "Produto.findByModbcIcms", query = "SELECT p FROM Produto p WHERE p.modbcIcms = :modbcIcms"),
    @NamedQuery(name = "Produto.findByPicms", query = "SELECT p FROM Produto p WHERE p.picms = :picms"),
    @NamedQuery(name = "Produto.findByCstPis", query = "SELECT p FROM Produto p WHERE p.cstPis = :cstPis"),
    @NamedQuery(name = "Produto.findByPpis", query = "SELECT p FROM Produto p WHERE p.ppis = :ppis"),
    @NamedQuery(name = "Produto.findByCstCofins", query = "SELECT p FROM Produto p WHERE p.cstCofins = :cstCofins"),
    @NamedQuery(name = "Produto.findByPcofins", query = "SELECT p FROM Produto p WHERE p.pcofins = :pcofins"),
    @NamedQuery(name = "Produto.findByAtivo", query = "SELECT p FROM Produto p WHERE p.ativo = :ativo"),
    @NamedQuery(name = "Produto.findByDataCriacao", query = "SELECT p FROM Produto p WHERE p.dataCriacao = :dataCriacao"),
    @NamedQuery(name = "Produto.findByDataAtualizacao", query = "SELECT p FROM Produto p WHERE p.dataAtualizacao = :dataAtualizacao")})
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "cprod")
    private String cprod;
    @Column(name = "cean")
    private String cean;
    @Basic(optional = false)
    @Column(name = "xprod")
    private String xprod;
    @Basic(optional = false)
    @Column(name = "ncm")
    private String ncm;
    @Column(name = "cest")
    private String cest;
    @Basic(optional = false)
    @Column(name = "cfop")
    private String cfop;
    @Basic(optional = false)
    @Column(name = "ucom")
    private String ucom;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qcom")
    private BigDecimal qcom;
    @Basic(optional = false)
    @Column(name = "vuncom")
    private BigDecimal vuncom;
    @Column(name = "vprod")
    private BigDecimal vprod;
    @Column(name = "ceantrib")
    private String ceantrib;
    @Column(name = "utrib")
    private String utrib;
    @Column(name = "qtrib")
    private BigDecimal qtrib;
    @Column(name = "vuntrib")
    private BigDecimal vuntrib;
    @Column(name = "indtot")
    private String indtot;
    @Column(name = "orig")
    private String orig;
    @Column(name = "cst_icms")
    private String cstIcms;
    @Column(name = "modbc_icms")
    private String modbcIcms;
    @Column(name = "picms")
    private BigDecimal picms;
    @Column(name = "cst_pis")
    private String cstPis;
    @Column(name = "ppis")
    private BigDecimal ppis;
    @Column(name = "cst_cofins")
    private String cstCofins;
    @Column(name = "pcofins")
    private BigDecimal pcofins;
    @Column(name = "ativo")
    private Boolean ativo;
    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @Column(name = "data_atualizacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "produtoId")
    private Collection<ItemVenda> itemVendaCollection;

    public Produto() {
    }

    public Produto(Integer id) {
        this.id = id;
    }

    public Produto(Integer id, String cprod, String xprod, String ncm, String cfop, String ucom, BigDecimal vuncom) {
        this.id = id;
        this.cprod = cprod;
        this.xprod = xprod;
        this.ncm = ncm;
        this.cfop = cfop;
        this.ucom = ucom;
        this.vuncom = vuncom;
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

    public String getCean() {
        return cean;
    }

    public void setCean(String cean) {
        this.cean = cean;
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

    public String getCest() {
        return cest;
    }

    public void setCest(String cest) {
        this.cest = cest;
    }

    public String getCfop() {
        return cfop;
    }

    public void setCfop(String cfop) {
        this.cfop = cfop;
    }

    public String getUcom() {
        return ucom;
    }

    public void setUcom(String ucom) {
        this.ucom = ucom;
    }

    public BigDecimal getQcom() {
        return qcom;
    }

    public void setQcom(BigDecimal qcom) {
        this.qcom = qcom;
    }

    public BigDecimal getVuncom() {
        return vuncom;
    }

    public void setVuncom(BigDecimal vuncom) {
        this.vuncom = vuncom;
    }

    public BigDecimal getVprod() {
        return vprod;
    }

    public void setVprod(BigDecimal vprod) {
        this.vprod = vprod;
    }

    public String getCeantrib() {
        return ceantrib;
    }

    public void setCeantrib(String ceantrib) {
        this.ceantrib = ceantrib;
    }

    public String getUtrib() {
        return utrib;
    }

    public void setUtrib(String utrib) {
        this.utrib = utrib;
    }

    public BigDecimal getQtrib() {
        return qtrib;
    }

    public void setQtrib(BigDecimal qtrib) {
        this.qtrib = qtrib;
    }

    public BigDecimal getVuntrib() {
        return vuntrib;
    }

    public void setVuntrib(BigDecimal vuntrib) {
        this.vuntrib = vuntrib;
    }

    public String getIndtot() {
        return indtot;
    }

    public void setIndtot(String indtot) {
        this.indtot = indtot;
    }

    public String getOrig() {
        return orig;
    }

    public void setOrig(String orig) {
        this.orig = orig;
    }

    public String getCstIcms() {
        return cstIcms;
    }

    public void setCstIcms(String cstIcms) {
        this.cstIcms = cstIcms;
    }

    public String getModbcIcms() {
        return modbcIcms;
    }

    public void setModbcIcms(String modbcIcms) {
        this.modbcIcms = modbcIcms;
    }

    public BigDecimal getPicms() {
        return picms;
    }

    public void setPicms(BigDecimal picms) {
        this.picms = picms;
    }

    public String getCstPis() {
        return cstPis;
    }

    public void setCstPis(String cstPis) {
        this.cstPis = cstPis;
    }

    public BigDecimal getPpis() {
        return ppis;
    }

    public void setPpis(BigDecimal ppis) {
        this.ppis = ppis;
    }

    public String getCstCofins() {
        return cstCofins;
    }

    public void setCstCofins(String cstCofins) {
        this.cstCofins = cstCofins;
    }

    public BigDecimal getPcofins() {
        return pcofins;
    }

    public void setPcofins(BigDecimal pcofins) {
        this.pcofins = pcofins;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
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

    @XmlTransient
    public Collection<ItemVenda> getItemVendaCollection() {
        return itemVendaCollection;
    }

    public void setItemVendaCollection(Collection<ItemVenda> itemVendaCollection) {
        this.itemVendaCollection = itemVendaCollection;
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
        if (!(object instanceof Produto)) {
            return false;
        }
        Produto other = (Produto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.souzamonteiro.nfe.model.Produto[ id=" + id + " ]";
    }
    
}
