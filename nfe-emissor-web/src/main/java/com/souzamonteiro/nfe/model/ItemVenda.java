package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_venda")
public class ItemVenda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;
    
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(name = "quantidade", precision = 15, scale = 4, nullable = false)
    private BigDecimal quantidade = BigDecimal.ONE;
    
    @Column(name = "valor_unitario", precision = 15, scale = 4, nullable = false)
    private BigDecimal valorUnitario;
    
    @Column(name = "valor_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal valorTotal;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }
    
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { 
        this.quantidade = quantidade; 
        calcularTotal();
    }
    
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { 
        this.valorUnitario = valorUnitario; 
        calcularTotal();
    }
    
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    
    public void calcularTotal() {
        if (quantidade != null && valorUnitario != null) {
            this.valorTotal = quantidade.multiply(valorUnitario);
        }
    }
}