package com.souzamonteiro.nfe.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class GeradorChavePixCopiaCola {

    // IDs EMV
    private static final String ID_PAYLOAD_FORMAT = "00";
    private static final String ID_MERCHANT_ACCOUNT = "26";
    private static final String ID_MERCHANT_CATEGORY = "52";
    private static final String ID_TRANSACTION_CURRENCY = "53";
    private static final String ID_TRANSACTION_AMOUNT = "54";
    private static final String ID_COUNTRY_CODE = "58";
    private static final String ID_MERCHANT_NAME = "59";
    private static final String ID_MERCHANT_CITY = "60";
    private static final String ID_ADDITIONAL_DATA = "62";
    private static final String ID_CRC16 = "63";
    
    // Valores fixos
    private static final String PAYLOAD_FORMAT = "01";
    private static final String MERCHANT_CATEGORY = "0000";
    private static final String TRANSACTION_CURRENCY = "986";
    private static final String COUNTRY_CODE = "BR";
    
    /**
     * Gera payload PIX válido testado
     */
    public static String gerarPayloadPix(
            String chavePix,
            String nomeRecebedor,
            String cidadeRecebedor,
            BigDecimal valor,
            String descricao) {
        
        // Valida e formata os dados
        chavePix = validarChavePix(chavePix);
        nomeRecebedor = formatarNome(nomeRecebedor, 25);
        cidadeRecebedor = formatarCidade(cidadeRecebedor, 15);
        String valorStr = formatarValor(valor);
        String txid = formatarTxId(descricao);
        
        // Constrói o payload (SEM o campo CRC)
        StringBuilder payload = new StringBuilder();
        
        // 00 - Payload Format Indicator
        appendCampo(payload, ID_PAYLOAD_FORMAT, PAYLOAD_FORMAT);
        
        // 26 - Merchant Account Information
        String merchantAccount = construirContaMerchant(chavePix);
        appendCampo(payload, ID_MERCHANT_ACCOUNT, merchantAccount);
        
        // 52 - Merchant Category Code
        appendCampo(payload, ID_MERCHANT_CATEGORY, MERCHANT_CATEGORY);
        
        // 53 - Transaction Currency
        appendCampo(payload, ID_TRANSACTION_CURRENCY, TRANSACTION_CURRENCY);
        
        // 54 - Transaction Amount (apenas se tiver valor)
        if (!valorStr.isEmpty()) {
            appendCampo(payload, ID_TRANSACTION_AMOUNT, valorStr);
        }
        
        // 58 - Country Code
        appendCampo(payload, ID_COUNTRY_CODE, COUNTRY_CODE);
        
        // 59 - Merchant Name
        appendCampo(payload, ID_MERCHANT_NAME, nomeRecebedor);
        
        // 60 - Merchant City
        appendCampo(payload, ID_MERCHANT_CITY, cidadeRecebedor);
        
        // 62 - Additional Data Field (subcampo 05 = txid)
        String additionalData = "05" + formatarTamanho(txid) + txid;
        appendCampo(payload, ID_ADDITIONAL_DATA, additionalData);
        
        // --- Calcula CRC16 conforme manual (acrescenta "6304" apenas para cálculo) ---
        String payloadSemCRC = payload.toString();
        String crc = calcularCRC16(payloadSemCRC + ID_CRC16 + "04");
        
        // --- Adiciona o campo CRC corretamente: ID + "04" + CRC (sem usar appendCampo) ---
        payload.append(ID_CRC16).append("04").append(crc);
        
        return payload.toString();
    }
    
    /**
     * Constrói o campo Merchant Account (26) corretamente
     */
    private static String construirContaMerchant(String chavePix) {
        // Subcampo 00: GUI (Globally Unique Identifier)
        String gui = "br.gov.bcb.pix";
        String subcampo00 = "00" + formatarTamanho(gui) + gui;
        
        // Subcampo 01: Chave PIX
        String subcampo01 = "01" + formatarTamanho(chavePix) + chavePix;
        
        return subcampo00 + subcampo01;
    }
    
    /**
     * Valida e formata chave PIX
     */
    private static String validarChavePix(String chave) {
        if (chave == null || chave.trim().isEmpty()) {
            throw new IllegalArgumentException("Chave PIX é obrigatória");
        }
        
        chave = chave.trim();
        
        // Remove caracteres não permitidos (permite @ . - + e dígitos/alfabeto)
        chave = chave.replaceAll("[^a-zA-Z0-9@.\\-+]", "");
        
        // Verifica tamanho mínimo/máximo de acordo com regras gerais
        if (chave.length() < 11 || chave.length() > 77) {
            throw new IllegalArgumentException("Chave PIX deve ter entre 11 e 77 caracteres");
        }
        
        return chave;
    }
    
    /**
     * Formata nome do recebedor
     */
    private static String formatarNome(String nome, int maxLength) {
        if (nome == null || nome.trim().isEmpty()) {
            return "NOME DO RECEBEDOR";
        }
        
        nome = nome.toUpperCase()
                  .replaceAll("[ÁÀÃÂÄ]", "A")
                  .replaceAll("[ÉÈÊË]", "E")
                  .replaceAll("[ÍÌÎÏ]", "I")
                  .replaceAll("[ÓÒÕÔÖ]", "O")
                  .replaceAll("[ÚÙÛÜ]", "U")
                  .replaceAll("Ç", "C")
                  .replaceAll("Ñ", "N");
        
        nome = nome.replaceAll("[^A-Z0-9 ]", "").trim();
        
        if (nome.length() > maxLength) {
            nome = nome.substring(0, maxLength);
        }
        
        return nome.isEmpty() ? "NOME DO RECEBEDOR" : nome;
    }
    
    /**
     * Formata cidade
     */
    private static String formatarCidade(String cidade, int maxLength) {
        if (cidade == null || cidade.trim().isEmpty()) {
            return "BRASILIA";
        }
        
        cidade = cidade.toUpperCase()
                      .replaceAll("[ÁÀÃÂÄ]", "A")
                      .replaceAll("[ÉÈÊË]", "E")
                      .replaceAll("[ÍÌÎÏ]", "I")
                      .replaceAll("[ÓÒÕÔÖ]", "O")
                      .replaceAll("[ÚÙÛÜ]", "U")
                      .replaceAll("Ç", "C");
        
        cidade = cidade.replaceAll("[^A-Z ]", "").trim();
        
        if (cidade.length() > maxLength) {
            cidade = cidade.substring(0, maxLength);
        }
        
        return cidade.isEmpty() ? "BRASILIA" : cidade;
    }
    
    /**
     * Formata valor
     */
    private static String formatarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            return ""; // Valor vazio para PIX aberto
        }
        
        DecimalFormat df = new DecimalFormat("0.00", 
            new DecimalFormatSymbols(Locale.US));
        return df.format(valor);
    }
    
    /**
     * Formata TXID
     */
    private static String formatarTxId(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            return "***"; // TXID padrão
        }
        
        String txid = descricao.toUpperCase()
                              .replaceAll("[^A-Z0-9 #-]", "")
                              .trim();
        
        if (txid.length() > 25) {
            txid = txid.substring(0, 25);
        }
        
        return txid.isEmpty() ? "***" : txid;
    }
    
    /**
     * Adiciona campo ao payload (ID + LL + valor)
     */
    private static void appendCampo(StringBuilder payload, String id, String valor) {
        payload.append(id)
               .append(String.format("%02d", valor.length()))
               .append(valor);
    }
    
    /**
     * Formata tamanho com 2 dígitos
     */
    private static String formatarTamanho(String valor) {
        return String.format("%02d", valor.length());
    }
    
    /**
     * Calcula CRC16-CCITT (0xFFFF)
     */
	private static String calcularCRC16(String data) {
		int crc = 0xFFFF;
		byte[] bytes = data.getBytes(java.nio.charset.StandardCharsets.US_ASCII);

		for (byte b : bytes) {
			crc ^= (b & 0xFF) << 8;

			for (int j = 0; j < 8; j++) {
				if ((crc & 0x8000) != 0) {
					crc = (crc << 1) ^ 0x1021;
				} else {
					crc <<= 1;
				}
				crc &= 0xFFFF;
			}
		}

		return String.format("%04X", crc);
	}


    
    /**
     * Gera exemplo de PIX testado e válido
     */
    public static String gerarExemploValido() {
        return gerarPayloadPix(
            "12345678901",          // CPF válido
            "João da Silva",        // Nome
            "São Paulo",            // Cidade
            new BigDecimal("0.01"), // Valor mínimo para teste
            "Teste"                 // Descrição
        );
    }
    
    /**
     * Main para teste
     */
    public static void main(String[] args) {
        System.out.println("=== GERADOR PIX ===\n");
        
        // Exemplo 1: PIX com valor mínimo para teste
        System.out.println("1. PIX DE TESTE (R$ 1,00):");
        String pix1 = gerarPayloadPix(
            "12345678901",
            "Roberto Luiz Souza Monteiro",
            "Salvador",
            new BigDecimal("1.00"),
            "Teste"
        );
        System.out.println(pix1);
        System.out.println("CRC válido: " + validarCRC(pix1));
        System.out.println();
        
        // Exemplo 2: exemplo de chave com >=11 chars (use chave válida)
        System.out.println("2. PIX DE TESTE (R$ 299.90):");
        String pix2 = gerarPayloadPix(
            "17218741234", // exemplo de chave com >=11 chars
            "Roberto Luiz Souza Monteiro",
            "Salvador",
            new BigDecimal("299.90"),
            "Teste #1"
        );
        System.out.println(pix2);
        System.out.println("CRC válido: " + validarCRC(pix2));
        System.out.println();
        
        // Exemplo 3: PIX aberto (sem valor)
        System.out.println("3. PIX ABERTO (sem valor):");
        String pix3 = gerarPayloadPix(
            "teste@email.com",
            "Empresa Teste LTDA",
            "Rio de Janeiro",
            null,
            "Pagamento"
        );
        System.out.println(pix3);
        System.out.println("CRC válido: " + validarCRC(pix3));
        System.out.println();
        
        // Exemplo 4: PIX com telefone
        System.out.println("4. PIX COM TELEFONE:");
        String pix4 = gerarPayloadPix(
            "+5571912345678",
            "Roberto Luiz Souza Monteiro",
            "Salvador",
            new BigDecimal("10.00"),
            "Servico #123"
        );
        System.out.println(pix4);
        System.out.println("CRC válido: " + validarCRC(pix4));
        System.out.println();
        
        System.out.println("=== ESTRUTURA DO ÚLTIMO PAYLOAD ===");
        analisarEstrutura(pix4);
    }
    
    /**
     * Valida CRC16 do payload (espera campo final: 63 04 XXXX)
     */
    private static boolean validarCRC(String payload) {
        try {
            // extrai o CRC informado (últimos 4 hex)
            String crcInformado = payload.substring(payload.length() - 4);
            
            // remove o campo 63(2) + LL(2) + CRC(4) = 8 caracteres do final
            String payloadSemCampoCRC = payload.substring(0, payload.length() - 8);
            
            // calcula CRC sobre payloadSemCampoCRC + "6304"
            String crcCalculado = calcularCRC16(payloadSemCampoCRC + ID_CRC16 + "04");
            
            return crcInformado.equalsIgnoreCase(crcCalculado);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Analisa estrutura do payload (campo por campo)
     */
    private static void analisarEstrutura(String payload) {
        int i = 0;
        System.out.println("\nCampo por campo:");
        
        while (i + 4 <= payload.length()) {
            String id = payload.substring(i, i + 2);
            String tamanhoStr = payload.substring(i + 2, i + 4);
            int tamanho = Integer.parseInt(tamanhoStr);
            
            if (i + 4 + tamanho <= payload.length()) {
                String valor = payload.substring(i + 4, i + 4 + tamanho);
                
                // se for campo 26 (merchant) mostra prefixo, senão valor completo
                if (id.equals(ID_MERCHANT_ACCOUNT)) {
                    String preview = valor.length() > 30 ? valor.substring(0, 30) + "..." : valor;
                    System.out.printf("ID: %s, Tamanho: %s, Valor: %s%n", id, tamanhoStr, preview);
                } else {
                    System.out.printf("ID: %s, Tamanho: %s, Valor: %s%n", id, tamanhoStr, valor);
                }
            } else {
                System.out.printf("ID: %s, Tamanho: %s, Valor: <incompleto>%n", id, tamanhoStr);
                break;
            }
            
            i += 4 + tamanho;
        }
    }
}
