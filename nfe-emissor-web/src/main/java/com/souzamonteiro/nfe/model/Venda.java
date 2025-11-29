package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venda")
public class Venda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda;
    
    @Column(name = "numero_nfe")
    private Integer numeroNFe;
    
    @Column(name = "chave_nfe", length = 44)
    private String chaveNFe;
    
    @Column(name = "protocolo_nfe", length = 50)
    private String protocoloNFe;
    
    @Column(name = "status", length = 20)
    private String status = "PENDENTE";
    
    @Column(name = "valor_total", precision = 15, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;
    
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }
    
    public Integer getNumeroNFe() { return numeroNFe; }
    public void setNumeroNFe(Integer numeroNFe) { this.numeroNFe = numeroNFe; }
    
    public String getChaveNFe() { return chaveNFe; }
    public void setChaveNFe(String chaveNFe) { this.chaveNFe = chaveNFe; }
    
    public String getProtocoloNFe() { return protocoloNFe; }
    public void setProtocoloNFe(String protocoloNFe) { this.protocoloNFe = protocoloNFe; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    
    public List<ItemVenda> getItens() { return itens; }
    public void setItens(List<ItemVenda> itens) { this.itens = itens; }
    
    public void adicionarItem(ItemVenda item) {
        item.setVenda(this);
        this.itens.add(item);
        calcularTotal();
    }
    
    public void removerItem(ItemVenda item) {
        this.itens.remove(item);
        calcularTotal();
    }
    
    public void calcularTotal() {
        this.valorTotal = itens.stream()
            .map(ItemVenda::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}