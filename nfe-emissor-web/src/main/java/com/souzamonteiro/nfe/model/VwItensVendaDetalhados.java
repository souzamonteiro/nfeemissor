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
@Table(name = "vw_itens_venda_detalhados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwItensVendaDetalhados.findAll", query = "SELECT v FROM VwItensVendaDetalhados v"),
    @NamedQuery(name = "VwItensVendaDetalhados.findById", query = "SELECT v FROM VwItensVendaDetalhados v WHERE v.id = :id"),
    @NamedQuery(name = "VwItensVendaDetalhados.findByVendaId", query = "SELECT v FROM VwItensVendaDetalhados v WHERE v.vendaId = :vendaId"),
    @NamedQuery(name = "VwItensVendaDetalhados.findByProdutoId", query = "SELECT v FROM VwItensVendaDetalhados v WHERE v.produtoId = :produtoId"),
    @NamedQuery(name = "VwItensVendaDetalhados.findByCprod", query = "SELECT v FROM VwItensVendaDetalhados v WHERE v.cprod = :cprod"),
    @NamedQuery(name = "VwItensVendaDetalhados.findByXprod", query = "SELECT v FROM VwItensVendaDetalhados v WHERE v.xprod = :xprod"),
    @NamedQuery(name = "VwItensVendaDetalhados.findByNcm", query = "SELECT v FROM VwItensVendaDetalhados v WHERE v.ncm = :ncm"),
    @NamedQuery(name = "VwItensVendaDetalhados.findByQuantidade", query = "SELECT v FROM VwItensVendaDetalhados v WHERE v.quantidade = :quantidade"),
    @NamedQuery(name = "VwItensVendaDetalhados.findByValorUnitario", query = "SELECT v FROM VwItensVendaDetalhados v WHERE v.valorUnitario = :valorUnitario"),
    @NamedQuery(name = "VwItensVendaDetalhados.findByValorTotal", query = "SELECT v FROM VwItensVendaDetalhados v WHERE v.valorTotal = :valorTotal")})
public class VwItensVendaDetalhados implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;  // Mudado de int para Integer e adicionado @Id
    
    @Basic(optional = false)
    @Column(name = "venda_id")
    private int vendaId;
    
    @Basic(optional = false)
    @Column(name = "produto_id")
    private int produtoId;
    
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
    @Column(name = "quantidade")
    private BigDecimal quantidade;
    
    @Basic(optional = false)
    @Column(name = "valor_unitario")
    private BigDecimal valorUnitario;
    
    @Basic(optional = false)
    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    public VwItensVendaDetalhados() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
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

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof VwItensVendaDetalhados)) {
            return false;
        }
        VwItensVendaDetalhados other = (VwItensVendaDetalhados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.souzamonteiro.nfe.model.VwItensVendaDetalhados[ id=" + id + " ]";
    }
}