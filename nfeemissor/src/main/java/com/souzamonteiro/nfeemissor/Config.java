/*
 * Classe de configuração para o emissor de NF-e
 * Suporta apenas os estados BA (Bahia) e AM (Amazonas) para simplificação
 * 
 * @author Professor - Para fins didáticos
 */
package com.souzamonteiro.nfeemissor;

import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.dom.enuns.EstadosEnum;
import br.com.swconsultoria.nfe.dom.enuns.AmbienteEnum;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.exception.NfeException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONObject;

/**
 * Classe responsável pelas configurações do emissor de NF-e
 * Configura certificado digital, ambiente e estados suportados
 */
public class Config {

    private static final String SCHEMA_PRINCIPAL = "leiauteNFe_v4.00.xsd";
    
    /**
     * Carrega o certificado A1 no formato PFX
     * @return Certificado digital carregado
     * @throws CertificadoException em caso de erro no certificado
     */
    private static Certificado certificadoA1Pfx() throws CertificadoException {
        // Obtém o diretório atual do projeto
        String caminhoNFeEmissor = System.getProperty("user.dir");
        
        // Define o caminho do arquivo de configuração
        String caminhoArquivoConfiguracoes = caminhoNFeEmissor + "/NFeEmissor.json";
        String jsonConfiguracoes = readFile(caminhoArquivoConfiguracoes);
        
        JSONObject configuracoes = new JSONObject(jsonConfiguracoes);
        
        try {
            // Obtém caminho e senha do certificado do arquivo de configuração
            String caminhoCertificado = configuracoes.get("caminhoCertificado").toString();
            String senhaCertificado = configuracoes.get("senhaCertificado").toString();

            // Carrega o certificado digital
            return CertificadoService.certificadoPfx(caminhoCertificado, senhaCertificado);
        } catch (Exception e)  {
            throw new CertificadoException("Erro ao carregar certificado digital: " + e.getMessage(), e);
        }
    }

    private static boolean schemaSuportaRtc(Path pastaSchemas) {
        Path schema = pastaSchemas.resolve(SCHEMA_PRINCIPAL);
        if (!Files.exists(schema)) {
            return false;
        }
        try {
            String conteudo = Files.readString(schema, StandardCharsets.UTF_8);
            return conteudo.contains("IBSCBS") && conteudo.contains("IBSCBSTot");
        } catch (IOException e) {
            return false;
        }
    }

    private static void extrairZipSchemas(Path zipPath, Path destino) throws IOException {
        Files.createDirectories(destino);
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipPath.toFile())))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path out = destino.resolve(entry.getName()).normalize();
                if (!out.startsWith(destino)) {
                    throw new IOException("Entrada de zip inválida: " + entry.getName());
                }
                if (entry.isDirectory()) {
                    Files.createDirectories(out);
                } else {
                    Files.createDirectories(out.getParent());
                    try (FileOutputStream fos = new FileOutputStream(out.toFile())) {
                        zis.transferTo(fos);
                    }
                }
            }
        }
    }

    private static String resolverCaminhoSchemas(String caminhoBaseProjeto, String caminhoSchemasConfigurado) throws IOException {
        Path caminhoConfigurado = Path.of(caminhoSchemasConfigurado);
        if (!caminhoConfigurado.isAbsolute()) {
            caminhoConfigurado = Path.of(caminhoBaseProjeto, caminhoSchemasConfigurado);
        }
        caminhoConfigurado = caminhoConfigurado.normalize();

        Path zipInterno = Path.of(caminhoBaseProjeto, "schemas.zip");
        Path pastaInterna = Path.of(caminhoBaseProjeto, "schemas");
        Path pastaInternaComSubpasta = pastaInterna.resolve("schemas");

        if ((!Files.isDirectory(caminhoConfigurado) || !schemaSuportaRtc(caminhoConfigurado)) && Files.exists(zipInterno)) {
            if (!schemaSuportaRtc(pastaInternaComSubpasta)) {
                extrairZipSchemas(zipInterno, pastaInterna);
            }
            if (schemaSuportaRtc(pastaInternaComSubpasta)) {
                System.out.println("Usando schemas RTC atualizados do pacote local: " + pastaInternaComSubpasta);
                return pastaInternaComSubpasta.toString();
            }
        }

        return caminhoConfigurado.toString();
    }
    
    /**
     * Inicializa as configurações para comunicação com a SEFAZ
     * @return Configurações da NF-e
     * @throws NfeException em caso de erro na configuração
     */
    public static ConfiguracoesNfe iniciaConfiguracoes() throws NfeException {
        String caminhoNFeEmissor = System.getProperty("user.dir");
        
        String caminhoArquivoConfiguracoes = caminhoNFeEmissor + "/NFeEmissor.json";
        String jsonConfiguracoes = readFile(caminhoArquivoConfiguracoes);
        
        JSONObject configuracoes = new JSONObject(jsonConfiguracoes);
        
        try {
            // Obtém configurações do arquivo JSON
            String webserviceUF = configuracoes.get("webserviceUF").toString();
            String webserviceAmbiente = configuracoes.get("webserviceAmbiente").toString();
            String caminhoSchemas = resolverCaminhoSchemas(caminhoNFeEmissor, configuracoes.get("caminhoSchemas").toString());

            if (!schemaSuportaRtc(Path.of(caminhoSchemas))) {
                System.out.println("Aviso: schemas sem RTC/IBSCBS detectados em " + caminhoSchemas);
                System.out.println("Atualize os schemas para evitar rejeições na autorização.");
            }
            
            // Carrega o certificado digital
            Certificado certificado = certificadoA1Pfx();
            if (certificado == null) {
                throw new IllegalStateException("Certificado digital inválido ou não carregado.");
            }

            // Configurações simplificadas - apenas BA e AM
            // Para ambiente de produção (1) ou homologação (2)
            if (webserviceUF.equals("BA")) {
                if (webserviceAmbiente.equals("1")) {
                    return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.BA, AmbienteEnum.PRODUCAO, certificado, caminhoSchemas);
                } else {
                    return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.BA, AmbienteEnum.HOMOLOGACAO, certificado, caminhoSchemas);
                }
            } else if (webserviceUF.equals("AM")) {
                if (webserviceAmbiente.equals("1")) {
                    return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.AM, AmbienteEnum.PRODUCAO, certificado, caminhoSchemas);
                } else {
                    return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.AM, AmbienteEnum.HOMOLOGACAO, certificado, caminhoSchemas);
                }
            } else {
                // Estado padrão em caso de configuração inválida
                System.out.println("Estado não suportado. Usando BA como padrão.");
                if (webserviceAmbiente.equals("1")) {
                    return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.BA, AmbienteEnum.PRODUCAO, certificado, caminhoSchemas);
                } else {
                    return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.BA, AmbienteEnum.HOMOLOGACAO, certificado, caminhoSchemas);
                }
            }
        } catch (Exception e)  {
            throw new NfeException("Erro nas configurações: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtém o estado configurado para emissão
     * @return Enum do estado
     */
    public static EstadosEnum getEstado() {
        String caminhoNFeEmissor = System.getProperty("user.dir");
        
        String caminhoArquivoConfiguracoes = caminhoNFeEmissor + "/NFeEmissor.json";
        String jsonConfiguracoes = readFile(caminhoArquivoConfiguracoes);
        
        JSONObject configuracoes = new JSONObject(jsonConfiguracoes);
        
        String emitenteUF = configuracoes.get("emitenteUF").toString();
        
        // Retorna apenas BA ou AM - simplificado para ensino
        if (emitenteUF.equals("BA")) {
            return EstadosEnum.BA;
        } else if (emitenteUF.equals("AM")) {
            return EstadosEnum.AM;
        } else {
            System.out.println("Estado não suportado. Usando BA como padrão.");
            return EstadosEnum.BA;
        }
    }
    
    /**
     * Lê o conteúdo de um arquivo
     * @param filePath Caminho do arquivo
     * @return Conteúdo do arquivo como String
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
            System.out.println("Arquivo não encontrado: " + e);
        }
        
        return data;
    }
}