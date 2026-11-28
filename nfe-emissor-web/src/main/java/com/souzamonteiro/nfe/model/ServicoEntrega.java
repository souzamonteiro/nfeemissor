package com.souzamonteiro.nfe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "servico_entrega")
public class ServicoEntrega implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "transportadora_id")
    private Transportadora transportadora;
    @Column(nullable = false, length = 80)
    private String nome;
    @Column(name = "cep_inicio", nullable = false, length = 8)
    private String cepInicio;
    @Column(name = "cep_fim", nullable = false, length = 8)
    private String cepFim;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;
    @Column(name = "prazo_dias", nullable = false)
    private Integer prazoDias;
    private boolean ativo = true;

    public Integer getId() { return id; }
    public Transportadora getTransportadora() { return transportadora; }
    public void setTransportadora(Transportadora transportadora) { this.transportadora = transportadora; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCepInicio() { return cepInicio; }
    public void setCepInicio(String cepInicio) { this.cepInicio = cepInicio; }
    public String getCepFim() { return cepFim; }
    public void setCepFim(String cepFim) { this.cepFim = cepFim; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public Integer getPrazoDias() { return prazoDias; }
    public void setPrazoDias(Integer prazoDias) { this.prazoDias = prazoDias; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Transient
    public String getDescricao() {
        return transportadora.getDados().getNome() + " · " + nome + " · "
            + java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("pt", "BR")).format(valor)
            + " · " + prazoDias + (prazoDias == 1 ? " dia útil" : " dias úteis");
    }
}
