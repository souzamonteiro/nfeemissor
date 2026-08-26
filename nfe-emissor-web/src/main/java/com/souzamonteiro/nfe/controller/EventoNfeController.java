package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.ConfiguracaoDAO;
import com.souzamonteiro.nfe.dao.EmpresaDAO;
import com.souzamonteiro.nfe.model.Configuracao;
import com.souzamonteiro.nfe.model.Empresa;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import org.json.JSONObject;

@ManagedBean
@ViewScoped
public class EventoNfeController implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();
    private transient EmpresaDAO empresaDAO = new EmpresaDAO();

    private String tipoEvento = "CANCELAMENTO";
    private String chaveNfe;
    private String protocolo;
    private String numeroSequencial = "1";
    private String descricao;
    private String resultado;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("tipoEvento")) {
            this.tipoEvento = params.get("tipoEvento");
        }
        if (this.descricao == null || this.descricao.trim().isEmpty()) {
            if ("CANCELAMENTO".equalsIgnoreCase(this.tipoEvento)) {
                this.descricao = "Cancelamento de NF-e conforme solicitação do usuário.";
            } else {
                this.descricao = "Carta de correção para ajuste de dados da emissão.";
            }
        }
    }

    public void limpar() {
        this.tipoEvento = "CANCELAMENTO";
        this.chaveNfe = null;
        this.protocolo = null;
        this.numeroSequencial = "1";
        this.descricao = "Cancelamento de NF-e conforme solicitação do usuário.";
        this.resultado = null;
    }

    public void enviarEvento() {
        try {
            if (chaveNfe == null || chaveNfe.trim().isEmpty()) {
                throw new IllegalArgumentException("Informe a chave da NF-e.");
            }
            if (numeroSequencial == null || numeroSequencial.trim().isEmpty()) {
                numeroSequencial = "1";
            }
            if (descricao == null || descricao.trim().isEmpty()) {
                throw new IllegalArgumentException("Informe a justificativa ou texto do evento.");
            }

            Configuracao config = configuracaoDAO.getConfiguracao();
            Empresa empresa = empresaDAO.getEmpresa();
            if (config == null || empresa == null) {
                throw new IllegalArgumentException("Configure a empresa e as configurações do sistema antes de enviar o evento.");
            }

            JSONObject payload = new JSONObject();
            JSONObject infEvento = new JSONObject();
            infEvento.put("CNPJ", empresa.getCnpj());
            infEvento.put("chNFe", chaveNfe.trim());
            infEvento.put("nSeqEvento", numeroSequencial.trim());
            infEvento.put("descEvento", descricao.trim());
            if ("CANCELAMENTO".equalsIgnoreCase(tipoEvento)) {
                if (protocolo == null || protocolo.trim().isEmpty()) {
                    throw new IllegalArgumentException("Informe o número do protocolo da NF-e para cancelamento.");
                }
                infEvento.put("nProt", protocolo.trim());
            }
            payload.put("infEvento", infEvento);

            String endpoint = "CANCELAMENTO".equalsIgnoreCase(tipoEvento) ? "/NFeCancelamento" : "/NFeCartaCorrecao";
            String url = "http://localhost:" + config.getPortaServidor() + endpoint;
            String resposta = enviarParaServidor(url, payload.toString());

            JSONObject respostaJson = new JSONObject(resposta);
            String cStat = respostaJson.optString("cStat", "000");
            String xMotivo = respostaJson.optString("xMotivo", "Resposta vazia do emissor.");
            String xml = respostaJson.optString("xml", "");

            this.resultado = "cStat=" + cStat + " | xMotivo=" + xMotivo;
            if (xml != null && !xml.trim().isEmpty()) {
                this.resultado += " | XML gerado";
            }

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Evento NF-e", xMotivo));
        } catch (Exception e) {
            this.resultado = "Erro: " + e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    private String enviarParaServidor(String urlString, String json) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode >= 400) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                StringBuilder errorResponse = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    errorResponse.append(responseLine.trim());
                }
                throw new RuntimeException("HTTP Error: " + responseCode + " - " + errorResponse.toString());
            }
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }
    public String getChaveNfe() { return chaveNfe; }
    public void setChaveNfe(String chaveNfe) { this.chaveNfe = chaveNfe; }
    public String getProtocolo() { return protocolo; }
    public void setProtocolo(String protocolo) { this.protocolo = protocolo; }
    public String getNumeroSequencial() { return numeroSequencial; }
    public void setNumeroSequencial(String numeroSequencial) { this.numeroSequencial = numeroSequencial; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
}
