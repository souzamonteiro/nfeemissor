/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.souzamonteiro.nfe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "vw_vendas_completas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwVendasCompletas.findAll", query = "SELECT v FROM VwVendasCompletas v"),
    @NamedQuery(name = "VwVendasCompletas.findById", query = "SELECT v FROM VwVendasCompletas v WHERE v.id = :id"),
    @NamedQuery(name = "VwVendasCompletas.findByDataVenda", query = "SELECT v FROM VwVendasCompletas v WHERE v.dataVenda = :dataVenda"),
    @NamedQuery(name = "VwVendasCompletas.findByNumeroNfe", query = "SELECT v FROM VwVendasCompletas v WHERE v.numeroNfe = :numeroNfe"),
    @NamedQuery(name = "VwVendasCompletas.findByChaveNfe", query = "SELECT v FROM VwVendasCompletas v WHERE v.chaveNfe = :chaveNfe"),
    @NamedQuery(name = "VwVendasCompletas.findByStatus", query = "SELECT v FROM VwVendasCompletas v WHERE v.status = :status"),
    @NamedQuery(name = "VwVendasCompletas.findByValorTotal", query = "SELECT v FROM VwVendasCompletas v WHERE v.valorTotal = :valorTotal"),
    @NamedQuery(name = "VwVendasCompletas.findByClienteNome", query = "SELECT v FROM VwVendasCompletas v WHERE v.clienteNome = :clienteNome"),
    @NamedQuery(name = "VwVendasCompletas.findByClienteCpf", query = "SELECT v FROM VwVendasCompletas v WHERE v.clienteCpf = :clienteCpf"),
    @NamedQuery(name = "VwVendasCompletas.findByClienteCnpj", query = "SELECT v FROM VwVendasCompletas v WHERE v.clienteCnpj = :clienteCnpj")})
public class VwVendasCompletas implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "data_venda")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVenda;
    
    @Column(name = "numero_nfe")
    private Integer numeroNfe;
    
    @Column(name = "chave_nfe")
    private String chaveNfe;
    
    @Column(name = "status")
    private String status;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
    
    @Basic(optional = false)
    @Column(name = "cliente_nome")
    private String clienteNome;
    
    @Column(name = "cliente_cpf")
    private String clienteCpf;
    
    @Column(name = "cliente_cnpj")
    private String clienteCnpj;

    public VwVendasCompletas() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Integer getNumeroNfe() {
        return numeroNfe;
    }

    public void setNumeroNfe(Integer numeroNfe) {
        this.numeroNfe = numeroNfe;
    }

    public String getChaveNfe() {
        return chaveNfe;
    }

    public void setChaveNfe(String chaveNfe) {
        this.chaveNfe = chaveNfe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getClienteCpf() {
        return clienteCpf;
    }

    public void setClienteCpf(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }

    public String getClienteCnpj() {
        return clienteCnpj;
    }

    public void setClienteCnpj(String clienteCnpj) {
        this.clienteCnpj = clienteCnpj;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof VwVendasCompletas)) {
            return false;
        }
        VwVendasCompletas other = (VwVendasCompletas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.souzamonteiro.nfe.model.VwVendasCompletas[ id=" + id + " ]";
    }
}