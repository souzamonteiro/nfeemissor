package com.souzamonteiro.nfe.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.json.JSONObject;

@Embeddable
public class DadosTransportadora implements Serializable {
    @Column(name = "transportadora_nome", length = 60)
    private String nome;
    @Column(name = "transportadora_documento", length = 14)
    private String documento;
    @Column(name = "transportadora_ie", length = 14)
    private String inscricaoEstadual;
    @Column(name = "transportadora_endereco", length = 60)
    private String endereco;
    @Column(name = "transportadora_municipio", length = 60)
    private String municipio;
    @Column(name = "transportadora_uf", length = 2)
    private String uf;

    public DadosTransportadora() {}

    public DadosTransportadora(DadosTransportadora origem) {
        nome = origem.nome;
        documento = origem.documento;
        inscricaoEstadual = origem.inscricaoEstadual;
        endereco = origem.endereco;
        municipio = origem.municipio;
        uf = origem.uf;
    }

    public JSONObject paraNfe() {
        JSONObject json = new JSONObject()
            .put(documento.length() == 11 ? "CPF" : "CNPJ", documento)
            .put("xNome", nome).put("xEnder", endereco).put("xMun", municipio).put("UF", uf);
        if (inscricaoEstadual != null && !inscricaoEstadual.isEmpty()) json.put("IE", inscricaoEstadual);
        return json;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getInscricaoEstadual() { return inscricaoEstadual; }
    public void setInscricaoEstadual(String inscricaoEstadual) { this.inscricaoEstadual = inscricaoEstadual; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
}
