package com.souzamonteiro.geradorqrcodepix;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.Map;

public class GeradorQRCodePix {

    /**
     * Gera um QR Code (PNG ou JPG) a partir de um payload de PIX
     *
     * @param payloadPix   String completa do "copia e cola" do PIX
     * @param nomeArquivo  Nome do arquivo de saída (ex.: "qrcode.png")
     */
    public static void gerarQRCode(String payloadPix, String nomeArquivo) throws Exception {
        if (payloadPix == null || payloadPix.trim().isEmpty()) {
            throw new IllegalArgumentException("O payload PIX não pode ser vazio.");
        }

        if (nomeArquivo == null || nomeArquivo.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do arquivo não pode ser vazio.");
        }

        int tamanho = 500;  // resolução do QR Code (500x500px)

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1); // margem mínima

        BitMatrix matrix = new MultiFormatWriter()
                .encode(payloadPix, BarcodeFormat.QR_CODE, tamanho, tamanho, hints);

        Path caminho = FileSystems.getDefault().getPath(nomeArquivo);
        MatrixToImageWriter.writeToPath(matrix, pegarExtensao(nomeArquivo), caminho);

        System.out.println("QR Code gerado com sucesso em: " + nomeArquivo);
    }

    /**
     * Obtém a extensão do arquivo (png/jpg/jpeg)
     */
    private static String pegarExtensao(String nome) {
        int i = nome.lastIndexOf('.');
        if (i == -1) throw new IllegalArgumentException("O arquivo deve ter extensão .png ou .jpg");
        return nome.substring(i + 1).toLowerCase();
    }

    /**
     * Exemplo de uso
     */
    public static void main(String[] args) throws Exception {

        // Exemplo de payload PIX (substitua pelo seu)
        String payload = "00020126330014br.gov.bcb.pix01111234567890152040000530398654041.005802BR5925ROBERTO LUIZ SOUZA MONTEI6008SALVADOR62090505TESTE630408B9";

        // Arquivo de saída
        String arquivo = "meu_qrcode_pix.png";

        gerarQRCode(payload, arquivo);
    }
}

