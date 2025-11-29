package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produto")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Código do produto é obrigatório")
    @Column(name = "cprod", unique = true, nullable = false)
    private String cprod;
    
    @Column(name = "cean")
    private String cean;
    
    @NotBlank(message = "Descrição do produto é obrigatória")
    @Column(name = "xprod", nullable = false, length = 500)
    private String xprod;
    
    @NotBlank(message = "NCM é obrigatório")
    @Column(name = "ncm", nullable = false, length = 8)
    private String ncm;
    
    @Column(name = "cest")
    private String cest;
    
    @NotBlank(message = "CFOP é obrigatório")
    @Column(name = "cfop", nullable = false, length = 4)
    private String cfop;
    
    @NotBlank(message = "Unidade de medida é obrigatória")
    @Column(name = "ucom", nullable = false, length = 6)
    private String ucom;
    
    @NotNull(message = "Valor unitário é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor unitário deve ser maior que zero")
    @Column(name = "vuncom", precision = 15, scale = 4, nullable = false)
    private BigDecimal vuncom;
    
    // Campos para impostos
    @Column(name = "orig", length = 1)
    private String orig = "0";
    
    @Column(name = "cst_icms", length = 3)
    private String cstIcms = "00";
    
    @Column(name = "modbc_icms", length = 1)
    private String modbcIcms = "0";
    
    @Column(name = "picms", precision = 5, scale = 2)
    private BigDecimal picms = BigDecimal.valueOf(7.00);
    
    @Column(name = "cst_pis", length = 2)
    private String cstPis = "01";
    
    @Column(name = "ppis", precision = 5, scale = 2)
    private BigDecimal ppis = BigDecimal.valueOf(1.65);
    
    @Column(name = "cst_cofins", length = 2)
    private String cstCofins = "01";
    
    @Column(name = "pcofins", precision = 5, scale = 2)
    private BigDecimal pcofins = BigDecimal.valueOf(7.60);
    
    @Column(name = "ativo")
    private Boolean ativo = true;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCprod() { return cprod; }
    public void setCprod(String cprod) { this.cprod = cprod; }
    
    public String getCean() { return cean; }
    public void setCean(String cean) { this.cean = cean; }
    
    public String getXprod() { return xprod; }
    public void setXprod(String xprod) { this.xprod = xprod; }
    
    public String getNcm() { return ncm; }
    public void setNcm(String ncm) { this.ncm = ncm; }
    
    public String getCest() { return cest; }
    public void setCest(String cest) { this.cest = cest; }
    
    public String getCfop() { return cfop; }
    public void setCfop(String cfop) { this.cfop = cfop; }
    
    public String getUcom() { return ucom; }
    public void setUcom(String ucom) { this.ucom = ucom; }
    
    public BigDecimal getVuncom() { return vuncom; }
    public void setVuncom(BigDecimal vuncom) { this.vuncom = vuncom; }
    
    public String getOrig() { return orig; }
    public void setOrig(String orig) { this.orig = orig; }
    
    public String getCstIcms() { return cstIcms; }
    public void setCstIcms(String cstIcms) { this.cstIcms = cstIcms; }
    
    public String getModbcIcms() { return modbcIcms; }
    public void setModbcIcms(String modbcIcms) { this.modbcIcms = modbcIcms; }
    
    public BigDecimal getPicms() { return picms; }
    public void setPicms(BigDecimal picms) { this.picms = picms; }
    
    public String getCstPis() { return cstPis; }
    public void setCstPis(String cstPis) { this.cstPis = cstPis; }
    
    public BigDecimal getPpis() { return ppis; }
    public void setPpis(BigDecimal ppis) { this.ppis = ppis; }
    
    public String getCstCofins() { return cstCofins; }
    public void setCstCofins(String cstCofins) { this.cstCofins = cstCofins; }
    
    public BigDecimal getPcofins() { return pcofins; }
    public void setPcofins(BigDecimal pcofins) { this.pcofins = pcofins; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}