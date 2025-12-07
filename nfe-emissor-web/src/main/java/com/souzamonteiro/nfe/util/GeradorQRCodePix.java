package com.souzamonteiro.nfe.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class GeradorQRCodePix {

    /**
     * Gera um QR Code e retorna os bytes da imagem PNG
     *
     * @param payloadPix   String completa do "copia e cola" do PIX
     * @param tamanho      Tamanho em pixels (ex: 250, 300)
     * @return byte[] da imagem PNG
     */
    public static byte[] gerarQRCodeBytes(String payloadPix, int tamanho) throws Exception {
        if (payloadPix == null || payloadPix.trim().isEmpty()) {
            throw new IllegalArgumentException("O payload PIX não pode ser vazio.");
        }

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1); // margem mínima

        BitMatrix matrix = new MultiFormatWriter()
                .encode(payloadPix, BarcodeFormat.QR_CODE, tamanho, tamanho, hints);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
        
        return baos.toByteArray();
    }

    /**
     * Gera um QR Code (PNG ou JPG) a partir de um payload de PIX
     * (Método original mantido para compatibilidade)
     */
    public static void gerarQRCode(String payloadPix, String nomeArquivo) throws Exception {
        byte[] bytes = gerarQRCodeBytes(payloadPix, 500);
        java.nio.file.Files.write(java.nio.file.Paths.get(nomeArquivo), bytes);
        System.out.println("QR Code gerado com sucesso em: " + nomeArquivo);
    }

    /**
     * Exemplo de uso
     */
    public static void main(String[] args) throws Exception {
        // Exemplo de payload PIX
        String payload = "00020126330014br.gov.bcb.pix01111234567890152040000530398654041.005802BR5925ROBERTO LUIZ SOUZA MONTEI6008SALVADOR62090505TESTE630408B9";

        // Gerar bytes
        byte[] qrCodeBytes = gerarQRCodeBytes(payload, 300);
        System.out.println("QR Code gerado com " + qrCodeBytes.length + " bytes");
        
        // Se quiser salvar também
        gerarQRCode(payload, "meu_qrcode_pix.png");
    }
}