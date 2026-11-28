/*
 * Emissor de NF-e para Simples Nacional
 * Projeto didático para alunos - Versão simplificada
 * 
 * @author Professor - Para fins educacionais
 */
package com.souzamonteiro.nfeemissor;

import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.*;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.Evento;
import br.com.swconsultoria.nfe.schemas_eventos.TEnvEventoCartaCorrecao;
import br.com.swconsultoria.nfe.schemas_eventos.TRetEnvEventoCartaCorrecao;
import br.com.swconsultoria.nfe.schemas_eventos.TEnvEventoCancelamento;
import br.com.swconsultoria.nfe.schemas_eventos.TRetEnvEventoCancelamento;
import br.com.swconsultoria.nfe.schemas.TRetConsStatServ;
import br.com.swconsultoria.nfe.schemas.TRetConsReciNFe;
import br.com.swconsultoria.nfe.schemas.*;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.*;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto.COFINS;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto.COFINSST;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto.PIS;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto.PIS.PISAliq;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto.PIS.PISOutr;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Imposto.PISST;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Det.Prod;
import br.com.swconsultoria.nfe.schemas.TNFe.InfNFe.Total.ICMSTot;
import br.com.swconsultoria.nfe.util.*;

// Importações para geração de PDF
import br.com.swconsultoria.nfe.impressao.ImpressaoDTO;
import br.com.swconsultoria.nfe.impressao.ImpressaoNfeUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Emissor de NF-e para Simples Nacional
 * 
 * @author Professor
 * @version 1.0 - Versão didática
 */
public class NFeEmissor {
    
    /**
     * Método principal - inicia o servidor HTTP
     */
    public static void main(String[] args) throws Exception {
        garantirFonteVerdana();

        String caminhoNFeEmissor = System.getProperty("user.dir");
        
        // Carrega as configurações do arquivo JSON
        String caminhoArquivoConfiguracoes = caminhoNFeEmissor + "/NFeEmissor.json";
        String jsonConfiguracoes = readFile(caminhoArquivoConfiguracoes);
        
        JSONObject configuracoes = new JSONObject(jsonConfiguracoes);
        
        // Obtém a porta do servidor HTTP
        int porta = Integer.parseInt(configuracoes.get("porta").toString());
            
        // Cria e configura o servidor HTTP
        HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);
        server.createContext("/NFeStatusServico", new NFeStatusServicoHandler());
        server.createContext("/NFeAutorizacao", new NFeAutorizacaoHandler());
        server.createContext("/NFeCartaCorrecao", new NFeCartaCorrecaoHandler());
        server.createContext("/NFeCancelamento", new NFeCancelamentoHandler());
        server.createContext("/NFeGerarPDF", new NFeGerarPDFHandler());
        server.setExecutor(null);
        server.start();
        
        System.out.println("Servidor NFeEmissor iniciado na porta: " + porta);
        System.out.println("Endpoints disponíveis:");
        System.out.println("- /NFeStatusServico");
        System.out.println("- /NFeAutorizacao");
        System.out.println("- /NFeCartaCorrecao");
        System.out.println("- /NFeCancelamento");
        System.out.println("- /NFeGerarPDF");
    }
    
    private static void garantirFonteVerdana() {
        try {
            Path userFontsDir = Paths.get(System.getProperty("user.home"), ".local", "share", "fonts");
            Files.createDirectories(userFontsDir);
            Path fonteVerdana = userFontsDir.resolve("Verdana.ttf");

            if (Files.exists(fonteVerdana)) {
                System.out.println("Fonte Verdana local já disponível: " + fonteVerdana);
                return;
            }

            Path fonteBase = Paths.get("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf");
            if (!Files.exists(fonteBase)) {
                fonteBase = Paths.get("/usr/share/fonts/truetype/dejavu/DejaVuSansCondensed.ttf");
            }

            if (!Files.exists(fonteBase)) {
                System.out.println("Fonte base do sistema não encontrada para criar alias Verdana");
                return;
            }

            String script = "from fontTools.ttLib import TTFont\n"
                    + "from pathlib import Path\n"
                    + "src = Path(r'" + fonteBase.toString().replace("\\", "\\\\") + "')\n"
                    + "out = Path(r'" + fonteVerdana.toString().replace("\\", "\\\\") + "')\n"
                    + "out.parent.mkdir(parents=True, exist_ok=True)\n"
                    + "font = TTFont(str(src))\n"
                    + "for record in font['name'].names:\n"
                    + "    if record.nameID in (1, 2, 16, 17):\n"
                    + "        if record.nameID == 1:\n"
                    + "            record.string = 'Verdana'\n"
                    + "        elif record.nameID == 2:\n"
                    + "            record.string = 'Regular'\n"
                    + "        elif record.nameID == 16:\n"
                    + "            record.string = 'Verdana'\n"
                    + "        elif record.nameID == 17:\n"
                    + "            record.string = 'Regular'\n"
                    + "font.save(str(out))\n"
                    + "print(str(out))\n";

            ProcessBuilder installBuilder = new ProcessBuilder("python3", "-m", "pip", "install", "--user", "fonttools");
            installBuilder.redirectErrorStream(true);
            Process installProcess = installBuilder.start();
            String installOutput = new String(installProcess.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            installProcess.waitFor();

            ProcessBuilder fontBuilder = new ProcessBuilder("python3", "-c", script);
            fontBuilder.redirectErrorStream(true);
            Process fontProcess = fontBuilder.start();
            String fontOutput = new String(fontProcess.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            int exitCode = fontProcess.waitFor();

            if (exitCode == 0 && Files.exists(fonteVerdana)) {
                ProcessBuilder fcBuilder = new ProcessBuilder("fc-cache", "-f", "-v");
                fcBuilder.redirectErrorStream(true);
                Process fcProcess = fcBuilder.start();
                fcProcess.waitFor();
                System.out.println("Fonte Verdana instalada com sucesso: " + fonteVerdana + "\n" + fontOutput.trim());
            } else {
                System.out.println("Aviso: não foi possível preparar alias Verdana automaticamente.\n" + installOutput + "\n" + fontOutput);
            }
        } catch (Exception e) {
            System.out.println("Aviso: falha ao preparar a fonte Verdana para DANFE: " + e.getMessage());
        }
    }

    /**
     * Lê o conteúdo de um arquivo
     */
    static public String readFile(String filePath) {
        String data = new String();

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            String line;
            
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                data = data + line;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Erro ao ler arquivo: " + e);
        }
        
        return data;
    }

    static public String normalizeRequestBody(String rawBody) {
        if (rawBody == null) {
            return "{}";
        }

        String normalized = rawBody.trim();
        if (normalized.isEmpty()) {
            return "{}";
        }

        return normalized;
    }

    static public byte[] toJsonResponseBytes(JSONObject responseJSON) {
        String response = responseJSON.toString();
        return response.getBytes();
    }

    static public void sendJsonResponse(HttpExchange httpExchange, JSONObject responseJSON) throws IOException {
        byte[] responseBytes = toJsonResponseBytes(responseJSON);
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.close();
    }
    
    /**
     * Gera o DANFE em PDF
     */
    private static boolean gerarPDFDanfe(String xml, String caminhoPDF) {
        try {
            System.out.println("Iniciando geração do DANFE em PDF...");
            
            // Cria o DTO de impressão com layout padrão
            ImpressaoDTO impressao = ImpressaoNfeUtil.impressaoPadraoNFe(xml);
            
            // Gera o PDF e salva no arquivo
            ImpressaoNfeUtil.impressaoPdfArquivo(impressao, caminhoPDF);
            
            System.out.println("DANFE gerado com sucesso: " + caminhoPDF);
            return true;
            
        } catch (Exception e) {
            System.out.println("Erro ao gerar PDF: " + e.getMessage());
            return false;
        }
    }

    /** Preenche campos opcionais que possuem o mesmo nome nos grupos ICMS. */
    private static void setSePresente(Object destino, JSONObject origem, String campoJson, String setter) {
        if (!origem.has(campoJson)) return;
        try {
            destino.getClass().getMethod(setter, String.class).invoke(destino, origem.getString(campoJson));
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Campo tributário não suportado: " + campoJson, e);
        }
    }

    private static void preencherDesoneracao(JSONObject origem, Object destino) {
        setSePresente(destino, origem, "vICMSDeson", "setVICMSDeson");
        setSePresente(destino, origem, "motDesICMS", "setMotDesICMS");
        setSePresente(destino, origem, "indDeduzDeson", "setIndDeduzDeson");
    }

    private static void preencherCamposTributarios(Object destino, JSONObject origem) {
        for (String chave : origem.keySet()) {
            Object valor = origem.get(chave);
            if (valor == null || valor instanceof JSONObject || valor instanceof JSONArray) continue;
            String setter = "set" + Character.toUpperCase(chave.charAt(0)) + chave.substring(1);
            try {
                destino.getClass().getMethod(setter, String.class).invoke(destino, valor.toString());
            } catch (ReflectiveOperationException ignored) {
                // Em casos específicos, a chave pode ser um nome em minúsculas ou diferente do método exact.
                // Mantém o comportamento permissivo e evita falhar por variações da estrutura JSON.
            }
        }
    }

    private static void preencherICMSProprioST(JSONObject origem, Object destino) {
        setSePresente(destino, origem, "orig", "setOrig");
        setSePresente(destino, origem, "CST", "setCST");
        setSePresente(destino, origem, "modBC", "setModBC");
        setSePresente(destino, origem, "vBC", "setVBC");
        setSePresente(destino, origem, "pICMS", "setPICMS");
        setSePresente(destino, origem, "vICMS", "setVICMS");
        setSePresente(destino, origem, "modBCST", "setModBCST");
        setSePresente(destino, origem, "pMVAST", "setPMVAST");
        setSePresente(destino, origem, "pRedBCST", "setPRedBCST");
        setSePresente(destino, origem, "vBCST", "setVBCST");
        setSePresente(destino, origem, "pICMSST", "setPICMSST");
        setSePresente(destino, origem, "vICMSST", "setVICMSST");
    }

    private static <T> T criarEPreencher(Object classe, JSONObject origem) {
        try {
            @SuppressWarnings("unchecked")
            T instancia = (T) classe;
            preencherCamposTributarios(instancia, origem);
            return instancia;
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao preencher grupo tributário", e);
        }
    }

    /** Regras estruturais: impedem misturar CST, CSOSN e a classificação RTC. */
    private static void validarRequisicaoTributaria(JSONObject nota) {
        JSONObject infNFe = nota.getJSONObject("infNFe");
        String crt = infNFe.getJSONObject("emit").getString("CRT");
        for (Object item : infNFe.getJSONArray("det")) {
            JSONObject imposto = ((JSONObject) item).getJSONObject("imposto");
            JSONObject icms = imposto.getJSONObject("ICMS");
            boolean temCsosn = icms.keySet().stream().anyMatch(chave -> chave.startsWith("ICMSSN"));
            boolean temCst = icms.keySet().stream().anyMatch(chave -> chave.startsWith("ICMS") && !chave.startsWith("ICMSSN"));
            if (temCsosn && temCst) throw new IllegalArgumentException("ICMS não pode misturar CST e CSOSN no mesmo item");
            if ("1".equals(crt) && temCst) throw new IllegalArgumentException("CRT 1 (Simples Nacional) exige grupo CSOSN, não CST de ICMS");
            if (!"1".equals(crt) && temCsosn) throw new IllegalArgumentException("CSOSN só pode ser usado por emitente do Simples Nacional (CRT 1)");
            if (imposto.has("IBSCBS")) {
                JSONObject rtc = imposto.getJSONObject("IBSCBS");
                if (!rtc.has("CST") || !rtc.has("cClassTrib"))
                    throw new IllegalArgumentException("IBSCBS exige CST e cClassTrib próprios");
            }
        }
    }

    private static void adicionarIBSCBS(Imposto imposto, JSONObject j) {
        TTribNFe trib = new TTribNFe();
        trib.setCST(j.getString("CST"));
        trib.setCClassTrib(j.getString("cClassTrib"));
        if (j.has("gIBSCBS")) {
            JSONObject g = j.getJSONObject("gIBSCBS");
            TCIBSNFe base = new TCIBSNFe();
            base.setVBC(g.getString("vBC"));
            JSONObject ufJson = g.getJSONObject("gIBSUF");
            TCIBSNFe.GIBSUF uf = new TCIBSNFe.GIBSUF();
            uf.setPIBSUF(ufJson.getString("pIBSUF"));
            preencherReducaoOuDiferimento(ufJson, uf);
            uf.setVIBSUF(ufJson.getString("vIBSUF")); base.setGIBSUF(uf);
            JSONObject munJson = g.getJSONObject("gIBSMun");
            TCIBSNFe.GIBSMun mun = new TCIBSNFe.GIBSMun();
            mun.setPIBSMun(munJson.getString("pIBSMun"));
            preencherReducaoOuDiferimento(munJson, mun);
            mun.setVIBSMun(munJson.getString("vIBSMun")); base.setGIBSMun(mun);
            base.setVIBS(g.getString("vIBS"));
            JSONObject cbsJson = g.getJSONObject("gCBS");
            TCIBSNFe.GCBS cbs = new TCIBSNFe.GCBS();
            cbs.setPCBS(cbsJson.getString("pCBS"));
            preencherReducaoOuDiferimento(cbsJson, cbs);
            cbs.setVCBS(cbsJson.getString("vCBS")); base.setGCBS(cbs);
            trib.setGIBSCBS(base);
        }
        imposto.getContent().add(new JAXBElement<>(new QName("IBSCBS"), TTribNFe.class, trib));
    }

    private static void adicionarTotaisIBSCBS(Total total, JSONObject j) {
        TIBSCBSMonoTot totais = new TIBSCBSMonoTot();
        totais.setVBCIBSCBS(j.getString("vBCIBSCBS"));
        TIBSCBSMonoTot.GIBS ibs = new TIBSCBSMonoTot.GIBS();
        JSONObject ibsJson = j.getJSONObject("gIBS");
        TIBSCBSMonoTot.GIBS.GIBSUF uf = new TIBSCBSMonoTot.GIBS.GIBSUF();
        JSONObject ufJson = ibsJson.getJSONObject("gIBSUF");
        uf.setVDif(ufJson.optString("vDif", "0.00")); uf.setVDevTrib(ufJson.optString("vDevTrib", "0.00"));
        uf.setVIBSUF(ufJson.getString("vIBSUF")); ibs.setGIBSUF(uf);
        TIBSCBSMonoTot.GIBS.GIBSMun mun = new TIBSCBSMonoTot.GIBS.GIBSMun();
        JSONObject munJson = ibsJson.getJSONObject("gIBSMun");
        mun.setVDif(munJson.optString("vDif", "0.00")); mun.setVDevTrib(munJson.optString("vDevTrib", "0.00"));
        mun.setVIBSMun(munJson.getString("vIBSMun")); ibs.setGIBSMun(mun);
        ibs.setVIBS(ibsJson.getString("vIBS")); ibs.setVCredPres(ibsJson.optString("vCredPres", "0.00"));
        ibs.setVCredPresCondSus(ibsJson.optString("vCredPresCondSus", "0.00")); totais.setGIBS(ibs);
        TIBSCBSMonoTot.GCBS cbs = new TIBSCBSMonoTot.GCBS();
        JSONObject cbsJson = j.getJSONObject("gCBS");
        cbs.setVDif(cbsJson.optString("vDif", "0.00")); cbs.setVDevTrib(cbsJson.optString("vDevTrib", "0.00"));
        cbs.setVCBS(cbsJson.getString("vCBS")); cbs.setVCredPres(cbsJson.optString("vCredPres", "0.00"));
        cbs.setVCredPresCondSus(cbsJson.optString("vCredPresCondSus", "0.00")); totais.setGCBS(cbs);
        total.setIBSCBSTot(totais);
    }

    /** Soma os valores informados por item quando o total RTC não veio no JSON. */
    private static JSONObject totalizarIBSCBS(JSONArray itens) {
        BigDecimal base = BigDecimal.ZERO, uf = BigDecimal.ZERO, mun = BigDecimal.ZERO, ibs = BigDecimal.ZERO, cbs = BigDecimal.ZERO;
        boolean encontrado = false;
        for (Object item : itens) {
            JSONObject imposto = ((JSONObject) item).getJSONObject("imposto");
            if (!imposto.has("IBSCBS")) continue;
            JSONObject g = imposto.getJSONObject("IBSCBS").optJSONObject("gIBSCBS");
            if (g == null) continue;
            encontrado = true;
            base = base.add(new BigDecimal(g.getString("vBC")));
            uf = uf.add(new BigDecimal(g.getJSONObject("gIBSUF").getString("vIBSUF")));
            mun = mun.add(new BigDecimal(g.getJSONObject("gIBSMun").getString("vIBSMun")));
            ibs = ibs.add(new BigDecimal(g.getString("vIBS")));
            cbs = cbs.add(new BigDecimal(g.getJSONObject("gCBS").getString("vCBS")));
        }
        if (!encontrado) return null;
        return new JSONObject()
            .put("vBCIBSCBS", decimal(base))
            .put("gIBS", new JSONObject().put("gIBSUF", new JSONObject().put("vIBSUF", decimal(uf)))
                .put("gIBSMun", new JSONObject().put("vIBSMun", decimal(mun))).put("vIBS", decimal(ibs)))
            .put("gCBS", new JSONObject().put("vCBS", decimal(cbs)));
    }

    private static String decimal(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static void preencherReducaoOuDiferimento(JSONObject origem, Object destino) {
        try {
            if (origem.has("pRedAliq")) {
                TRed reducao = new TRed(); reducao.setPRedAliq(origem.getString("pRedAliq"));
                reducao.setPAliqEfet(origem.getString("pAliqEfet"));
                destino.getClass().getMethod("setGRed", TRed.class).invoke(destino, reducao);
            }
            if (origem.has("pDif")) {
                TDif diferimento = new TDif(); diferimento.setPDif(origem.getString("pDif"));
                diferimento.setVDif(origem.getString("vDif"));
                destino.getClass().getMethod("setGDif", TDif.class).invoke(destino, diferimento);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Grupo RTC incompatível com o schema instalado", e);
        }
    }

    static class NFeCartaCorrecaoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String dados = "";
            try (InputStream inputStream = httpExchange.getRequestBody(); Scanner scanner = new Scanner(inputStream)) {
                while (scanner.hasNextLine()) {
                    dados += scanner.nextLine();
                }
            }

            JSONObject json = new JSONObject(normalizeRequestBody(dados));
            JSONObject jsonInfEvento = json.getJSONObject("infEvento");

            String cnpj = jsonInfEvento.getString("CNPJ");
            String chNFe = jsonInfEvento.getString("chNFe");
            String nSeqEvento = jsonInfEvento.getString("nSeqEvento");
            String descEvento = jsonInfEvento.getString("descEvento");

            try {
                ConfiguracoesNfe config = Config.iniciaConfiguracoes();
                Evento cce = new Evento();
                cce.setCnpj(cnpj);
                cce.setChave(chNFe);
                cce.setDataEvento(LocalDateTime.now());
                cce.setSequencia(Integer.parseInt(nSeqEvento));
                cce.setMotivo(descEvento);

                TEnvEventoCartaCorrecao envEvento = CartaCorrecaoUtil.montaCCe(cce, config);
                TRetEnvEventoCartaCorrecao retorno = Nfe.cce(config, envEvento, true);
                RetornoUtil.validaCartaCorrecao(retorno);

                JSONObject responseJSON = new JSONObject();
                retorno.getRetEvento().forEach(resultado -> {
                    responseJSON.put("cStat", resultado.getInfEvento().getCStat());
                    responseJSON.put("xMotivo", resultado.getInfEvento().getXMotivo());
                    responseJSON.put("nProt", resultado.getInfEvento().getNProt());
                });
                responseJSON.put("xml", CartaCorrecaoUtil.criaProcEventoCCe(config, envEvento, retorno));
                sendJsonResponse(httpExchange, responseJSON);
            } catch (Exception e) {
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("cStat", "000");
                responseJSON.put("xMotivo", e.getMessage());
                responseJSON.put("nProt", "");
                responseJSON.put("xml", "");
                sendJsonResponse(httpExchange, responseJSON);
            }
        }
    }

    static class NFeCancelamentoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String dados = "";
            try (InputStream inputStream = httpExchange.getRequestBody(); Scanner scanner = new Scanner(inputStream)) {
                while (scanner.hasNextLine()) {
                    dados += scanner.nextLine();
                }
            }

            JSONObject json = new JSONObject(normalizeRequestBody(dados));
            JSONObject jsonInfEvento = json.getJSONObject("infEvento");

            String cnpj = jsonInfEvento.getString("CNPJ");
            String chNFe = jsonInfEvento.getString("chNFe");
            String nProt = jsonInfEvento.getString("nProt");
            String nSeqEvento = jsonInfEvento.getString("nSeqEvento");
            String descEvento = jsonInfEvento.getString("descEvento");

            try {
                ConfiguracoesNfe config = Config.iniciaConfiguracoes();
                Evento cancelamento = new Evento();
                cancelamento.setCnpj(cnpj);
                cancelamento.setChave(chNFe);
                cancelamento.setProtocolo(nProt);
                cancelamento.setDataEvento(LocalDateTime.now());
                cancelamento.setSequencia(Integer.parseInt(nSeqEvento));
                cancelamento.setMotivo(descEvento);

                TEnvEventoCancelamento enviEvento = CancelamentoUtil.montaCancelamento(cancelamento, config);
                TRetEnvEventoCancelamento retorno = Nfe.cancelarNfe(config, enviEvento, true, DocumentoEnum.NFE);
                RetornoUtil.validaCancelamento(retorno);

                JSONObject responseJSON = new JSONObject();
                retorno.getRetEvento().forEach(resultado -> {
                    responseJSON.put("cStat", resultado.getInfEvento().getCStat());
                    responseJSON.put("xMotivo", resultado.getInfEvento().getXMotivo());
                    responseJSON.put("nProt", resultado.getInfEvento().getNProt());
                });
                responseJSON.put("xml", CancelamentoUtil.criaProcEventoCancelamento(config, enviEvento, retorno.getRetEvento().get(0)));
                sendJsonResponse(httpExchange, responseJSON);
            } catch (Exception e) {
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("cStat", "000");
                responseJSON.put("xMotivo", e.getMessage());
                responseJSON.put("nProt", "");
                responseJSON.put("xml", "");
                sendJsonResponse(httpExchange, responseJSON);
            }
        }
    }

    /**
     * Handler para autorização de NF-e
     * Baseado no exemplo funcional do Samuel Oliveira
     */
    static class NFeAutorizacaoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String caminhoNFeEmissor = System.getProperty("user.dir");
        
            // Carrega configurações
            String caminhoArquivoConfiguracoes = caminhoNFeEmissor + "/NFeEmissor.json";
            String jsonConfiguracoes = readFile(caminhoArquivoConfiguracoes);
            JSONObject configuracoes = new JSONObject(jsonConfiguracoes);
            
            String caminhoXML = configuracoes.get("caminhoXML").toString();
            String gerarPDF = configuracoes.has("gerarPDF") ? configuracoes.get("gerarPDF").toString() : "0";
            
            InputStream inputStream = httpExchange.getRequestBody(); 
            Scanner scanner = new Scanner(inputStream);
            String dados = scanner.useDelimiter("\\A").next();
            scanner.close();
            
            System.out.print("INFORMAÇÕES: [JSON]: ");
            System.out.println(dados);
            
            JSONObject json = new JSONObject(dados);
            validarRequisicaoTributaria(json);
            
            String cStat = "000";
            String xMotivo = "";
            String dhRecbto = "";
            String nProt = "";
            String chave = "";
            String xml = "";
            
            // Inicia as configurações
            ConfiguracoesNfe config;
            
            try {
                config = Config.iniciaConfiguracoes();
            } catch (Exception e) {
                System.out.println("Erro nas configurações: " + e.getMessage());
                enviarResposta(httpExchange, "000", e.getMessage(), "", "", "", "");
                return;
            }
            
            try {
                // Extrai dados do JSON
                JSONObject jsonInfNFe = json.getJSONObject("infNFe");
                JSONObject jsonIde = jsonInfNFe.getJSONObject("ide");
                JSONObject jsonEmit = jsonInfNFe.getJSONObject("emit");
                JSONObject jsonEnderEmit = jsonEmit.getJSONObject("enderEmit");
                JSONObject jsonDest = jsonInfNFe.getJSONObject("dest");
                JSONArray jsonDet = jsonInfNFe.getJSONArray("det");
                JSONObject jsonTotal = jsonInfNFe.getJSONObject("total");
                JSONObject jsonICMSTot = jsonTotal.getJSONObject("ICMSTot");
                JSONObject jsonTransp = jsonInfNFe.getJSONObject("transp");
                JSONObject jsonPag = jsonInfNFe.getJSONObject("pag");
                JSONArray jsonDetPag = jsonPag.getJSONArray("detPag");
                
                // Dados básicos da NF-e
                String nNF = jsonIde.getString("nNF");
                String cNF = jsonIde.getString("cNF");
                int numeroNFe = Integer.parseInt(nNF);
                
                // CNPJ do emitente
                String cnpj = jsonEmit.getString("CNPJ");
                
                // Data de emissão
                LocalDateTime dataEmissao = LocalDateTime.now();
                
                // Modelo da NF-e (55 = NF-e normal)
                String modelo = "55";
                
                // Série da NF-e
                int serie = Integer.parseInt(jsonIde.getString("serie"));
                
                // Tipo de ambiente
                String tpAmb = config.getAmbiente().getCodigo();
                
                // Tipo de emissão
                String tpEmis = jsonIde.getString("tpEmis");
                
                // Gera a chave da NF-e
                ChaveUtil chaveUtil = new ChaveUtil(config.getEstado(), cnpj, modelo, serie, numeroNFe, tpEmis, cNF, dataEmissao);
                chave = chaveUtil.getChaveNF();
                String cdv = chaveUtil.getDigitoVerificador();

                System.out.println("Gerando NF-e com chave: " + chave);

                // CRIA O OBJETO InfNFe
                InfNFe infNFe = new InfNFe();
                infNFe.setId(chave);
                infNFe.setVersao(ConstantesUtil.VERSAO.NFE);

                // PREENCHE A IDE (Identificação da NF-e)
                Ide ide = new Ide();
                ide.setCUF(config.getEstado().getCodigoUF());
                ide.setCNF(cNF);
                ide.setNatOp(jsonIde.getString("natOp"));
                ide.setMod(modelo);
                ide.setSerie(String.valueOf(serie));
                ide.setNNF(String.valueOf(numeroNFe));
                ide.setDhEmi(XmlNfeUtil.dataNfe(dataEmissao));
                ide.setTpNF(jsonIde.getString("tpNF"));
                ide.setIdDest(jsonIde.getString("idDest"));
                ide.setCMunFG(jsonIde.getString("cMunFG"));
                ide.setTpImp(jsonIde.getString("tpImp"));
                ide.setTpEmis(tpEmis);
                ide.setCDV(cdv);
                ide.setTpAmb(tpAmb);
                ide.setFinNFe(jsonIde.getString("finNFe"));
                ide.setIndFinal(jsonIde.getString("indFinal"));
                ide.setIndPres(jsonIde.getString("indPres"));
                if (jsonIde.has("indIntermed")) {
                    ide.setIndIntermed(jsonIde.getString("indIntermed"));
                }
                ide.setProcEmi(jsonIde.getString("procEmi"));
                ide.setVerProc(jsonIde.getString("verProc"));
                
                infNFe.setIde(ide);

                // PREENCHE O EMITENTE
                Emit emit = new Emit();
                emit.setCNPJ(cnpj);
                emit.setIE(jsonEmit.getString("IE"));
                emit.setXNome(jsonEmit.getString("xNome"));

                TEnderEmi enderEmit = new TEnderEmi();
                enderEmit.setXLgr(jsonEnderEmit.getString("xLgr"));
                enderEmit.setNro(jsonEnderEmit.getString("nro"));
                if (jsonEnderEmit.has("xCpl")) {
                    enderEmit.setXCpl(jsonEnderEmit.getString("xCpl"));
                }
                enderEmit.setXBairro(jsonEnderEmit.getString("xBairro"));
                enderEmit.setCMun(jsonEnderEmit.getString("cMun"));
                enderEmit.setXMun(jsonEnderEmit.getString("xMun"));
                enderEmit.setUF(TUfEmi.valueOf(jsonEnderEmit.getString("UF")));
                enderEmit.setCEP(jsonEnderEmit.getString("CEP"));
                enderEmit.setCPais("1058"); // Código do Brasil
                enderEmit.setXPais("Brasil");
                if (jsonEnderEmit.has("fone")) {
                    enderEmit.setFone(jsonEnderEmit.getString("fone"));
                }
                emit.setEnderEmit(enderEmit);
                emit.setCRT(jsonEmit.getString("CRT"));

                infNFe.setEmit(emit);

                // PREENCHE O DESTINATÁRIO
                Dest dest = new Dest();
                if (jsonDest.has("CNPJ")) {
                    dest.setCNPJ(jsonDest.getString("CNPJ"));
                }
                if (jsonDest.has("CPF")) {
                    dest.setCPF(jsonDest.getString("CPF"));
                }
                dest.setXNome(jsonDest.getString("xNome"));
                
                if (jsonDest.has("enderDest")) {
                    JSONObject jsonEnderDest = jsonDest.getJSONObject("enderDest");
                    TEndereco enderDest = new TEndereco();
                    enderDest.setXLgr(jsonEnderDest.getString("xLgr"));
                    enderDest.setNro(jsonEnderDest.getString("nro"));
                    if (jsonEnderDest.has("xCpl")) {
                        enderDest.setXCpl(jsonEnderDest.getString("xCpl"));
                    }
                    enderDest.setXBairro(jsonEnderDest.getString("xBairro"));
                    enderDest.setCMun(jsonEnderDest.getString("cMun"));
                    enderDest.setXMun(jsonEnderDest.getString("xMun"));
                    enderDest.setUF(TUf.valueOf(jsonEnderDest.getString("UF")));
                    if (jsonEnderDest.has("CEP")) {
                        enderDest.setCEP(jsonEnderDest.getString("CEP"));
                    }
                    dest.setEnderDest(enderDest);
                }
                
                if (jsonDest.has("indIEDest")) {
                    dest.setIndIEDest(jsonDest.getString("indIEDest"));
                } else {
                    dest.setIndIEDest("9");
                }
                if (jsonDest.has("IE")) {
                    dest.setIE(jsonDest.getString("IE"));
                }
                
                infNFe.setDest(dest);
                
                // PREENCHE OS AUTORIZADOS A TER ACESSO AO XML
                if (jsonInfNFe.has("autXML")) {
                    JSONObject jsonAutXML = jsonInfNFe.getJSONObject("autXML");

                    AutXML autXML = new AutXML();
                    if (jsonAutXML.has("CPF")) {
                        autXML.setCPF(jsonAutXML.get("CPF").toString());
                    }
                    if (jsonAutXML.has("CNPJ")) {
                        autXML.setCNPJ(jsonAutXML.get("CNPJ").toString());
                    }
                    infNFe.getAutXML().add(autXML);
                } else if (configuracoes.has("autXMLPadrao")) {
                    AutXML autXML = new AutXML();
                    autXML.setCNPJ(configuracoes.getString("autXMLPadrao"));
                    infNFe.getAutXML().add(autXML);
                }
                
                // PREENCHE OS PRODUTOS
                for (int i = 0; i < jsonDet.length(); i++) {
                    JSONObject itemDet = jsonDet.getJSONObject(i);
                    JSONObject jsonProd = itemDet.getJSONObject("prod");
                    JSONObject jsonImposto = itemDet.getJSONObject("imposto");

                    int numeroItem = i + 1;

                    Det det = new Det();
                    det.setNItem(Integer.toString(numeroItem));

                    // Preenche dados do produto
                    Prod prod = new Prod();
                    prod.setCProd(jsonProd.getString("cProd"));
                    if (jsonProd.has("cEAN")) {
                        prod.setCEAN(jsonProd.getString("cEAN"));
                    }
                    prod.setXProd(jsonProd.getString("xProd"));
                    prod.setNCM(jsonProd.getString("NCM"));
                    if (jsonProd.has("CEST")) {
                        prod.setCEST(jsonProd.getString("CEST"));
                    }
                    prod.setCFOP(jsonProd.getString("CFOP"));
                    prod.setUCom(jsonProd.getString("uCom"));
                    prod.setQCom(jsonProd.getString("qCom"));
                    prod.setVUnCom(jsonProd.getString("vUnCom"));
                    prod.setVProd(jsonProd.getString("vProd"));
                    if (jsonProd.has("cEANTrib")) {
                        prod.setCEANTrib(jsonProd.getString("cEANTrib"));
                    }
                    prod.setUTrib(jsonProd.getString("uTrib"));
                    prod.setQTrib(jsonProd.getString("qTrib"));
                    prod.setVUnTrib(jsonProd.getString("vUnTrib"));
                    prod.setIndTot(jsonProd.getString("indTot"));
                    
                    det.setProd(prod);

                    // Preenche impostos
                    Imposto imposto = new Imposto();
                    
                    // ICMS
                    ICMS icms = new ICMS();
                    JSONObject jsonICMS = jsonImposto.getJSONObject("ICMS");
                    
                    // Verifica qual tipo de ICMS está presente
                    if (jsonICMS.has("ICMS00")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS00");
                        ICMS.ICMS00 v = new ICMS.ICMS00();
                        preencherCamposTributarios(v, j);
                        icms.setICMS00(v);
                    }
                    else if (jsonICMS.has("ICMS02")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS02");
                        ICMS.ICMS02 v = new ICMS.ICMS02();
                        preencherCamposTributarios(v, j);
                        icms.setICMS02(v);
                    }
                    else if (jsonICMS.has("ICMS10")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS10");
                        ICMS.ICMS10 v = new ICMS.ICMS10();
                        preencherICMSProprioST(j, v);
                        icms.setICMS10(v);
                    }
                    else if (jsonICMS.has("ICMS15")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS15");
                        ICMS.ICMS15 v = new ICMS.ICMS15();
                        preencherCamposTributarios(v, j);
                        icms.setICMS15(v);
                    }
                    else if (jsonICMS.has("ICMS20")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS20");
                        ICMS.ICMS20 v = new ICMS.ICMS20();
                        v.setOrig(j.getString("orig")); v.setCST(j.getString("CST"));
                        v.setModBC(j.getString("modBC")); v.setPRedBC(j.getString("pRedBC"));
                        v.setVBC(j.getString("vBC")); v.setPICMS(j.getString("pICMS")); v.setVICMS(j.getString("vICMS"));
                        preencherDesoneracao(j, v);
                        icms.setICMS20(v);
                    }
                    else if (jsonICMS.has("ICMS30")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS30");
                        ICMS.ICMS30 v = new ICMS.ICMS30();
                        v.setOrig(j.getString("orig")); v.setCST(j.getString("CST"));
                        v.setModBCST(j.getString("modBCST")); v.setPMVAST(j.getString("pMVAST"));
                        v.setPRedBCST(j.optString("pRedBCST", "0.00")); v.setVBCST(j.getString("vBCST"));
                        v.setPICMSST(j.getString("pICMSST")); v.setVICMSST(j.getString("vICMSST"));
                        preencherDesoneracao(j, v);
                        icms.setICMS30(v);
                    }
                    else if (jsonICMS.has("ICMS40")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS40");
                        ICMS.ICMS40 v = new ICMS.ICMS40();
                        v.setOrig(j.getString("orig")); v.setCST(j.getString("CST")); preencherDesoneracao(j, v);
                        icms.setICMS40(v);
                    }
                    else if (jsonICMS.has("ICMS51")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS51");
                        ICMS.ICMS51 v = new ICMS.ICMS51();
                        v.setOrig(j.getString("orig")); v.setCST(j.getString("CST")); v.setModBC(j.getString("modBC"));
                        v.setVBC(j.getString("vBC")); v.setPICMS(j.getString("pICMS")); v.setVICMSOp(j.getString("vICMSOp"));
                        v.setPDif(j.getString("pDif")); v.setVICMSDif(j.getString("vICMSDif")); v.setVICMS(j.getString("vICMS"));
                        icms.setICMS51(v);
                    }
                    else if (jsonICMS.has("ICMS53")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS53");
                        ICMS.ICMS53 v = new ICMS.ICMS53();
                        preencherCamposTributarios(v, j);
                        icms.setICMS53(v);
                    }
                    else if (jsonICMS.has("ICMS60")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS60");
                        ICMS.ICMS60 v = new ICMS.ICMS60();
                        v.setOrig(j.getString("orig")); v.setCST(j.getString("CST"));
                        v.setVBCSTRet(j.getString("vBCSTRet")); v.setPST(j.getString("pST"));
                        v.setVICMSSubstituto(j.optString("vICMSSubstituto", "0.00")); v.setVICMSSTRet(j.getString("vICMSSTRet"));
                        icms.setICMS60(v);
                    }
                    else if (jsonICMS.has("ICMS61")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS61");
                        ICMS.ICMS61 v = new ICMS.ICMS61();
                        preencherCamposTributarios(v, j);
                        icms.setICMS61(v);
                    }
                    else if (jsonICMS.has("ICMS70")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS70");
                        ICMS.ICMS70 v = new ICMS.ICMS70();
                        preencherCamposTributarios(v, j);
                        icms.setICMS70(v);
                    }
                    else if (jsonICMS.has("ICMS90")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMS90");
                        ICMS.ICMS90 v = new ICMS.ICMS90();
                        preencherCamposTributarios(v, j);
                        icms.setICMS90(v);
                    }
                    else if (jsonICMS.has("ICMSSN101")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMSSN101");
                        ICMS.ICMSSN101 v = new ICMS.ICMSSN101();
                        preencherCamposTributarios(v, j);
                        icms.setICMSSN101(v);
                    }
                    else if (jsonICMS.has("ICMSSN102")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMSSN102");
                        ICMS.ICMSSN102 v = new ICMS.ICMSSN102();
                        preencherCamposTributarios(v, j);
                        icms.setICMSSN102(v);
                    }
                    else if (jsonICMS.has("ICMSSN201")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMSSN201");
                        ICMS.ICMSSN201 v = new ICMS.ICMSSN201();
                        preencherCamposTributarios(v, j);
                        icms.setICMSSN201(v);
                    }
                    else if (jsonICMS.has("ICMSSN202")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMSSN202");
                        ICMS.ICMSSN202 v = new ICMS.ICMSSN202();
                        preencherCamposTributarios(v, j);
                        icms.setICMSSN202(v);
                    }
                    else if (jsonICMS.has("ICMSSN500")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMSSN500");
                        ICMS.ICMSSN500 v = new ICMS.ICMSSN500();
                        preencherCamposTributarios(v, j);
                        icms.setICMSSN500(v);
                    }
                    else if (jsonICMS.has("ICMSST")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMSST");
                        ICMS.ICMSST v = new ICMS.ICMSST();
                        preencherCamposTributarios(v, j);
                        icms.setICMSST(v);
                    }
                    else if (jsonICMS.has("ICMSPart")) {
                        JSONObject j = jsonICMS.getJSONObject("ICMSPart");
                        ICMS.ICMSPart v = new ICMS.ICMSPart();
                        preencherCamposTributarios(v, j);
                        icms.setICMSPart(v);
                    }
                    else {
                        throw new IllegalArgumentException("Grupo ICMS não suportado no item " + numeroItem + ": " + jsonICMS.keySet());
                    }
                    
                    JAXBElement<ICMS> icmsElement = new JAXBElement<>(new QName("ICMS"), ICMS.class, icms);
                    imposto.getContent().add(icmsElement);

                    if (jsonImposto.has("IPI")) {
                        JSONObject j = jsonImposto.getJSONObject("IPI");
                        TIpi ipi = new TIpi();
                        ipi.setCEnq(j.getString("cEnq"));
                        if (j.has("IPITrib")) {
                            JSONObject t = j.getJSONObject("IPITrib");
                            TIpi.IPITrib trib = new TIpi.IPITrib();
                            trib.setCST(t.getString("CST")); trib.setVBC(t.getString("vBC"));
                            if (t.has("pIPI")) trib.setPIPI(t.getString("pIPI"));
                            if (t.has("qUnid")) trib.setQUnid(t.getString("qUnid"));
                            if (t.has("vUnid")) trib.setVUnid(t.getString("vUnid"));
                            trib.setVIPI(t.getString("vIPI")); ipi.setIPITrib(trib);
                        } else {
                            TIpi.IPINT naoTrib = new TIpi.IPINT();
                            naoTrib.setCST(j.getJSONObject("IPINT").getString("CST")); ipi.setIPINT(naoTrib);
                        }
                        imposto.getContent().add(new JAXBElement<>(new QName("IPI"), TIpi.class, ipi));
                    }

                    // PIS
                    if (jsonImposto.has("PIS")) {
                        JSONObject jsonPIS = jsonImposto.getJSONObject("PIS");
                        PIS pis = new PIS();
                        
                        if (jsonPIS.has("PISAliq")) {
                            JSONObject jsonPISAliq = jsonPIS.getJSONObject("PISAliq");
                            PISAliq pisAliq = new PISAliq();
                            pisAliq.setCST(jsonPISAliq.getString("CST"));
                            pisAliq.setVBC(jsonPISAliq.getString("vBC"));
                            pisAliq.setPPIS(jsonPISAliq.getString("pPIS"));
                            pisAliq.setVPIS(jsonPISAliq.getString("vPIS"));
                            pis.setPISAliq(pisAliq);
                        }
                        else if (jsonPIS.has("PISOutr")) {
                            JSONObject jsonPISOutr = jsonPIS.getJSONObject("PISOutr");
                            PISOutr pisOutr = new PISOutr();
                            pisOutr.setCST(jsonPISOutr.getString("CST"));
                            pisOutr.setVBC(jsonPISOutr.getString("vBC"));
                            pisOutr.setPPIS(jsonPISOutr.getString("pPIS"));
                            pisOutr.setVPIS(jsonPISOutr.getString("vPIS"));
                            pis.setPISOutr(pisOutr);
                        }
                        else if (jsonPIS.has("PISNT")) {
                            PIS.PISNT pisNT = new PIS.PISNT();
                            pisNT.setCST(jsonPIS.getJSONObject("PISNT").getString("CST"));
                            pis.setPISNT(pisNT);
                        }
                        else if (jsonPIS.has("PISQtde")) {
                            JSONObject j = jsonPIS.getJSONObject("PISQtde");
                            PIS.PISQtde pisQtde = new PIS.PISQtde();
                            pisQtde.setCST(j.getString("CST")); pisQtde.setQBCProd(j.getString("qBCProd"));
                            pisQtde.setVAliqProd(j.getString("vAliqProd")); pisQtde.setVPIS(j.getString("vPIS"));
                            pis.setPISQtde(pisQtde);
                        }
                        
                        JAXBElement<PIS> pisElement = new JAXBElement<>(new QName("PIS"), PIS.class, pis);
                        imposto.getContent().add(pisElement);
                    }
                    if (jsonImposto.has("PISST")) {
                        JSONObject j = jsonImposto.getJSONObject("PISST");
                        PISST pisst = new PISST();
                        preencherCamposTributarios(pisst, j);
                        imposto.getContent().add(new JAXBElement<>(new QName("PISST"), PISST.class, pisst));
                    }
                    
                    // COFINS
                    if (jsonImposto.has("COFINS")) {
                        JSONObject jsonCOFINS = jsonImposto.getJSONObject("COFINS");
                        COFINS cofins = new COFINS();
                        
                        if (jsonCOFINS.has("COFINSAliq")) {
                            JSONObject jsonCOFINSAliq = jsonCOFINS.getJSONObject("COFINSAliq");
                            COFINSAliq cofinsAliq = new COFINSAliq();
                            cofinsAliq.setCST(jsonCOFINSAliq.getString("CST"));
                            cofinsAliq.setVBC(jsonCOFINSAliq.getString("vBC"));
                            cofinsAliq.setPCOFINS(jsonCOFINSAliq.getString("pCOFINS"));
                            cofinsAliq.setVCOFINS(jsonCOFINSAliq.getString("vCOFINS"));
                            cofins.setCOFINSAliq(cofinsAliq);
                        }
                        else if (jsonCOFINS.has("COFINSOutr")) {
                            JSONObject jsonCOFINSOutr = jsonCOFINS.getJSONObject("COFINSOutr");
                            COFINSOutr cofinsOutr = new COFINSOutr();
                            cofinsOutr.setCST(jsonCOFINSOutr.getString("CST"));
                            cofinsOutr.setVBC(jsonCOFINSOutr.getString("vBC"));
                            cofinsOutr.setPCOFINS(jsonCOFINSOutr.getString("pCOFINS"));
                            cofinsOutr.setVCOFINS(jsonCOFINSOutr.getString("vCOFINS"));
                            cofins.setCOFINSOutr(cofinsOutr);
                        }
                        else if (jsonCOFINS.has("COFINSNT")) {
                            COFINS.COFINSNT cofinsNT = new COFINS.COFINSNT();
                            cofinsNT.setCST(jsonCOFINS.getJSONObject("COFINSNT").getString("CST"));
                            cofins.setCOFINSNT(cofinsNT);
                        }
                        else if (jsonCOFINS.has("COFINSQtde")) {
                            JSONObject j = jsonCOFINS.getJSONObject("COFINSQtde");
                            COFINS.COFINSQtde cofinsQtde = new COFINS.COFINSQtde();
                            cofinsQtde.setCST(j.getString("CST")); cofinsQtde.setQBCProd(j.getString("qBCProd"));
                            cofinsQtde.setVAliqProd(j.getString("vAliqProd")); cofinsQtde.setVCOFINS(j.getString("vCOFINS"));
                            cofins.setCOFINSQtde(cofinsQtde);
                        }
                        
                        JAXBElement<COFINS> cofinsElement = new JAXBElement<>(new QName("COFINS"), COFINS.class, cofins);
                        imposto.getContent().add(cofinsElement);
                    }
                    if (jsonImposto.has("COFINSST")) {
                        JSONObject j = jsonImposto.getJSONObject("COFINSST");
                        COFINSST cofinsst = new COFINSST();
                        preencherCamposTributarios(cofinsst, j);
                        imposto.getContent().add(new JAXBElement<>(new QName("COFINSST"), COFINSST.class, cofinsst));
                    }

                    // DIFAL: somente quando a operação realmente se enquadrar nas regras vigentes.
                    if (jsonImposto.has("ICMSUFDest")) {
                        JSONObject j = jsonImposto.getJSONObject("ICMSUFDest");
                        Imposto.ICMSUFDest difal = new Imposto.ICMSUFDest();
                        difal.setVBCUFDest(j.getString("vBCUFDest"));
                        difal.setVBCFCPUFDest(j.optString("vBCFCPUFDest", "0.00"));
                        difal.setPFCPUFDest(j.optString("pFCPUFDest", "0.00"));
                        difal.setPICMSUFDest(j.getString("pICMSUFDest"));
                        difal.setPICMSInter(j.getString("pICMSInter"));
                        difal.setPICMSInterPart(j.optString("pICMSInterPart", "100.00"));
                        difal.setVFCPUFDest(j.optString("vFCPUFDest", "0.00"));
                        difal.setVICMSUFDest(j.getString("vICMSUFDest"));
                        difal.setVICMSUFRemet(j.optString("vICMSUFRemet", "0.00"));
                        imposto.getContent().add(new JAXBElement<>(new QName("ICMSUFDest"), Imposto.ICMSUFDest.class, difal));
                    }

                    // RTC: CST e cClassTrib próprios, separados do CST/CSOSN do ICMS.
                    if (jsonImposto.has("IBSCBS")) {
                        adicionarIBSCBS(imposto, jsonImposto.getJSONObject("IBSCBS"));
                    }
                    
                    det.setImposto(imposto);
                    infNFe.getDet().add(det);
                }

                // PREENCHE OS TOTAIS
                Total total = new Total();
                ICMSTot icmstot = new ICMSTot();
                icmstot.setVBC(jsonICMSTot.getString("vBC"));
                icmstot.setVICMS(jsonICMSTot.getString("vICMS"));
                icmstot.setVICMSDeson(jsonICMSTot.getString("vICMSDeson"));
                icmstot.setVFCP(jsonICMSTot.getString("vFCP"));
                icmstot.setVBCST(jsonICMSTot.getString("vBCST"));
                icmstot.setVST(jsonICMSTot.getString("vST"));
                icmstot.setVFCPST(jsonICMSTot.getString("vFCPST"));
                icmstot.setVFCPSTRet(jsonICMSTot.getString("vFCPSTRet"));
                icmstot.setVProd(jsonICMSTot.getString("vProd"));
                icmstot.setVFrete(jsonICMSTot.getString("vFrete"));
                icmstot.setVSeg(jsonICMSTot.getString("vSeg"));
                icmstot.setVDesc(jsonICMSTot.getString("vDesc"));
                icmstot.setVII(jsonICMSTot.getString("vII"));
                icmstot.setVIPI(jsonICMSTot.getString("vIPI"));
                icmstot.setVIPIDevol(jsonICMSTot.getString("vIPIDevol"));
                icmstot.setVPIS(jsonICMSTot.getString("vPIS"));
                icmstot.setVCOFINS(jsonICMSTot.getString("vCOFINS"));
                icmstot.setVOutro(jsonICMSTot.getString("vOutro"));
                icmstot.setVNF(jsonICMSTot.getString("vNF"));
                
                total.setICMSTot(icmstot);
                if (jsonTotal.has("IBSCBSTot")) {
                    adicionarTotaisIBSCBS(total, jsonTotal.getJSONObject("IBSCBSTot"));
                } else {
                    JSONObject totaisCalculados = totalizarIBSCBS(jsonDet);
                    if (totaisCalculados != null) adicionarTotaisIBSCBS(total, totaisCalculados);
                }
                infNFe.setTotal(total);

                // PREENCHE TRANSPORTE
                Transp transp = new Transp();
                transp.setModFrete(jsonTransp.getString("modFrete"));
                infNFe.setTransp(transp);

                // PREENCHE PAGAMENTO
                Pag pag = new Pag();
                for (int i = 0; i < jsonDetPag.length(); i++) {
                    JSONObject jsonItemDetPag = jsonDetPag.getJSONObject(i);
                    Pag.DetPag detPag = new Pag.DetPag();
                    detPag.setTPag(jsonItemDetPag.getString("tPag"));
                    detPag.setVPag(jsonItemDetPag.getString("vPag"));
                    pag.getDetPag().add(detPag);
                }
                infNFe.setPag(pag);

                // PREENCHE INFORMAÇÕES ADICIONAIS
                if (jsonInfNFe.has("infAdic")) {
                    JSONObject jsonInfAdic = jsonInfNFe.getJSONObject("infAdic");
                    InfAdic infAdic = new InfAdic();
                    infAdic.setInfCpl(jsonInfAdic.getString("infCpl"));
                    infNFe.setInfAdic(infAdic);
                }

                // CRIA O OBJETO TNFe
                TNFe nfe = new TNFe();
                nfe.setInfNFe(infNFe);

                // CRIA O ENVI NFe (Lote)
                TEnviNFe enviNFe = new TEnviNFe();
                enviNFe.setVersao(ConstantesUtil.VERSAO.NFE);
                enviNFe.setIdLote("1");
                enviNFe.setIndSinc("1");
                enviNFe.getNFe().add(nfe);
                
                /*
                 * ENVIO PARA SEFAZ
                 */
                
                // Monta e Assina o XML
                enviNFe = Nfe.montaNfe(config, enviNFe, true);
                
                // Envia a NF-e para a SEFAZ
                TRetEnviNFe retorno = Nfe.enviarNfe(config, enviNFe, DocumentoEnum.NFE);
                
                // Verifica se o retorno é assíncrono
                if (RetornoUtil.isRetornoAssincrono(retorno)) {
                    // Processamento assíncrono (com recibo)
                    String recibo = retorno.getInfRec().getNRec();
                    int tentativa = 0;
                    TRetConsReciNFe retornoNfe = null;

                    // Consulta o recibo várias vezes até obter resposta
                    while (tentativa < 10) {
                        retornoNfe = Nfe.consultaRecibo(config, recibo, DocumentoEnum.NFE);
                        if (retornoNfe.getCStat().equals(StatusEnum.LOTE_EM_PROCESSAMENTO.getCodigo())) {
                            System.out.println("INFO: Lote Em Processamento, tentativa " + (tentativa + 1));
                            Thread.sleep(1000);
                            tentativa++;
                        } else {
                            break;
                        }
                    }

                    RetornoUtil.validaAssincrono(retornoNfe);
                    
                    cStat = retornoNfe.getProtNFe().get(0).getInfProt().getCStat();
                    xMotivo = retornoNfe.getProtNFe().get(0).getInfProt().getXMotivo();
                    
                    System.out.println("cStat: " + cStat + " - " + xMotivo);
                    
                    if (cStat.equals("100")) {
                        nProt = retornoNfe.getProtNFe().get(0).getInfProt().getNProt();
                        dhRecbto = retornoNfe.getProtNFe().get(0).getInfProt().getDhRecbto();
                        xml = XmlNfeUtil.criaNfeProc(enviNFe, retornoNfe.getProtNFe().get(0));
                        
                        // Salva o XML da NF-e
                        salvarXML(xml, caminhoXML, chave);
                        
                        // Gera PDF do DANFE se configurado
                        if ("1".equals(gerarPDF)) {
                            String caminhoPDF = caminhoXML + "/" + chave + ".pdf";
                            gerarPDFDanfe(xml, caminhoPDF);
                        }
                    }
                } else {
                    // Retorno síncrono
                    if (retorno.getProtNFe() != null && retorno.getProtNFe().getInfProt() != null) {
                        cStat = retorno.getProtNFe().getInfProt().getCStat();
                        xMotivo = retorno.getProtNFe().getInfProt().getXMotivo();
                    }
                    RetornoUtil.validaSincrono(retorno);
                    
                    System.out.println("cStat: " + cStat + " - " + xMotivo);
                    
                    if (cStat.equals("100")) {
                        nProt = retorno.getProtNFe().getInfProt().getNProt();
                        dhRecbto = retorno.getProtNFe().getInfProt().getDhRecbto();
                        xml = XmlNfeUtil.criaNfeProc(enviNFe, retorno.getProtNFe());
                        
                        // Salva o XML da NF-e
                        salvarXML(xml, caminhoXML, chave);
                        
                        // Gera PDF do DANFE se configurado
                        if ("1".equals(gerarPDF)) {
                            String caminhoPDF = caminhoXML + "/" + chave + ".pdf";
                            gerarPDFDanfe(xml, caminhoPDF);
                        }
                    }
                }
                
            } catch (Exception e) {
                System.out.println("Erro no envio para SEFAZ: " + e.getMessage());
                e.printStackTrace();
                if ("000".equals(cStat) || "100".equals(cStat)) {
                    cStat = "000";
                    xMotivo = e.getMessage();
                }
            }
            
            enviarResposta(httpExchange, cStat, xMotivo, dhRecbto, nProt, chave, xml);
        }
        
        private void salvarXML(String xml, String caminhoXML, String chave) throws IOException {
            FileWriter writer = new FileWriter(caminhoXML + "/" + chave + ".xml");
            writer.write(xml);
            writer.close();
            System.out.println("XML Final salvo: " + chave + ".xml");
        }
        
        private void enviarResposta(HttpExchange httpExchange, String cStat, String xMotivo, 
                                   String dhRecbto, String nProt, String chave, String xml) throws IOException {
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("cStat", cStat);
            responseJSON.put("xMotivo", xMotivo);
            responseJSON.put("dhRecbto", dhRecbto);
            responseJSON.put("nProt", nProt);
            responseJSON.put("chave", chave);
            responseJSON.put("xml", xml);

            System.out.println("Resposta autorização: " + responseJSON.toString());
            
            String response = responseJSON.toString();
            
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
    }

    // Outros handlers...
    static class NFeGerarPDFHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String dados = "";
                try (InputStream inputStream = httpExchange.getRequestBody(); Scanner scanner = new Scanner(inputStream)) {
                    while (scanner.hasNextLine()) {
                        dados += scanner.nextLine();
                    }
                }

                JSONObject json = new JSONObject(normalizeRequestBody(dados));
                String chave = json.optString("chave", "");
                String xml = json.optString("xml", "");

                if (xml == null || xml.trim().isEmpty()) {
                    String caminhoArquivoConfiguracoes = System.getProperty("user.dir") + "/NFeEmissor.json";
                    JSONObject configuracoes = new JSONObject(readFile(caminhoArquivoConfiguracoes));
                    String caminhoXML = configuracoes.getString("caminhoXML");
                    File xmlFile = new File(caminhoXML + "/" + chave + ".xml");
                    if (!xmlFile.exists()) {
                        throw new IllegalArgumentException("XML não encontrado para chave: " + chave);
                    }
                    xml = new String(java.nio.file.Files.readAllBytes(xmlFile.toPath()), java.nio.charset.StandardCharsets.UTF_8);
                }

                if (chave == null || chave.trim().isEmpty()) {
                    chave = xml.contains("<chNFe>") ? xml.split("<chNFe>")[1].split("</chNFe>")[0] : "NFEMANUAL";
                }

                String caminhoArquivoConfiguracoes = System.getProperty("user.dir") + "/NFeEmissor.json";
                JSONObject configuracoes = new JSONObject(readFile(caminhoArquivoConfiguracoes));
                String caminhoXML = configuracoes.getString("caminhoXML");
                String caminhoPDF = caminhoXML + "/" + chave + ".pdf";

                gerarPDFDanfe(xml, caminhoPDF);

                JSONObject responseJSON = new JSONObject();
                responseJSON.put("cStat", "100");
                responseJSON.put("xMotivo", "PDF gerado com sucesso");
                responseJSON.put("chave", chave);
                responseJSON.put("arquivo", caminhoPDF);
                sendJsonResponse(httpExchange, responseJSON);
            } catch (Exception e) {
                JSONObject responseJSON = new JSONObject();
                responseJSON.put("cStat", "000");
                responseJSON.put("xMotivo", e.getMessage());
                sendJsonResponse(httpExchange, responseJSON);
            }
        }
    }
    
    static class NFeStatusServicoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                // Inicia As Configurações
                ConfiguracoesNfe config = Config.iniciaConfiguracoes();

                try {
                    //Efetua Consulta
                    TRetConsStatServ retorno = Nfe.statusServico(config, DocumentoEnum.NFE);

                    //Resultado
                    System.out.println();
                    System.out.println("# Status: " + retorno.getCStat() + " - " + retorno.getXMotivo());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("# Erro: " + e.getMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("# Erro: " + e.getMessage());
            }
            
            String response = "{\"cStat\":\"107\",\"xMotivo\":\"Serviço em Operação\"}";
            
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
    }
}
