package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "configuracao")
public class Configuracao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "UF do WebService é obrigatória")
    @Column(name = "webservice_uf", nullable = false, length = 2)
    private String webserviceUF = "BA";
    
    @NotBlank(message = "UF do Emitente é obrigatória")
    @Column(name = "emitente_uf", nullable = false, length = 2)
    private String emitenteUF = "BA";
    
    @NotBlank(message = "Ambiente é obrigatório")
    @Column(name = "webservice_ambiente", nullable = false, length = 1)
    private String webserviceAmbiente = "2";
    
    @NotBlank(message = "Caminho dos Schemas é obrigatório")
    @Column(name = "caminho_schemas", nullable = false, length = 500)
    private String caminhoSchemas;
    
    @NotBlank(message = "Caminho do Certificado é obrigatório")
    @Column(name = "caminho_certificado", nullable = false, length = 500)
    private String caminhoCertificado;
    
    @NotBlank(message = "Senha do Certificado é obrigatória")
    @Column(name = "senha_certificado", nullable = false, length = 100)
    private String senhaCertificado;
    
    @NotBlank(message = "Caminho do XML é obrigatório")
    @Column(name = "caminho_xml", nullable = false, length = 500)
    private String caminhoXML;
    
    @Column(name = "gerar_pdf", length = 1)
    private String gerarPDF = "1";
    
    @Column(name = "porta_servidor", length = 5)
    private String portaServidor = "3435";
    
    @Column(name = "serie_nfe", length = 3)
    private String serieNFe = "1";
    
    @Column(name = "natureza_operacao", length = 100)
    private String naturezaOperacao = "VENDA";
    
    @Column(name = "finalidade_emissao", length = 1)
    private String finalidadeEmissao = "1";
    
    @Column(name = "consumidor_final", length = 1)
    private String consumidorFinal = "1";
    
    @Column(name = "presenca_comprador", length = 1)
    private String presencaComprador = "1";
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getWebserviceUF() { return webserviceUF; }
    public void setWebserviceUF(String webserviceUF) { this.webserviceUF = webserviceUF; }
    
    public String getEmitenteUF() { return emitenteUF; }
    public void setEmitenteUF(String emitenteUF) { this.emitenteUF = emitenteUF; }
    
    public String getWebserviceAmbiente() { return webserviceAmbiente; }
    public void setWebserviceAmbiente(String webserviceAmbiente) { this.webserviceAmbiente = webserviceAmbiente; }
    
    public String getCaminhoSchemas() { return caminhoSchemas; }
    public void setCaminhoSchemas(String caminhoSchemas) { this.caminhoSchemas = caminhoSchemas; }
    
    public String getCaminhoCertificado() { return caminhoCertificado; }
    public void setCaminhoCertificado(String caminhoCertificado) { this.caminhoCertificado = caminhoCertificado; }
    
    public String getSenhaCertificado() { return senhaCertificado; }
    public void setSenhaCertificado(String senhaCertificado) { this.senhaCertificado = senhaCertificado; }
    
    public String getCaminhoXML() { return caminhoXML; }
    public void setCaminhoXML(String caminhoXML) { this.caminhoXML = caminhoXML; }
    
    public String getGerarPDF() { return gerarPDF; }
    public void setGerarPDF(String gerarPDF) { this.gerarPDF = gerarPDF; }
    
    public String getPortaServidor() { return portaServidor; }
    public void setPortaServidor(String portaServidor) { this.portaServidor = portaServidor; }
    
    public String getSerieNFe() { return serieNFe; }
    public void setSerieNFe(String serieNFe) { this.serieNFe = serieNFe; }
    
    public String getNaturezaOperacao() { return naturezaOperacao; }
    public void setNaturezaOperacao(String naturezaOperacao) { this.naturezaOperacao = naturezaOperacao; }
    
    public String getFinalidadeEmissao() { return finalidadeEmissao; }
    public void setFinalidadeEmissao(String finalidadeEmissao) { this.finalidadeEmissao = finalidadeEmissao; }
    
    public String getConsumidorFinal() { return consumidorFinal; }
    public void setConsumidorFinal(String consumidorFinal) { this.consumidorFinal = consumidorFinal; }
    
    public String getPresencaComprador() { return presencaComprador; }
    public void setPresencaComprador(String presencaComprador) { this.presencaComprador = presencaComprador; }
}