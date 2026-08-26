package com.souzamonteiro.nfe.servlet;

import com.souzamonteiro.nfe.dao.ConfiguracaoDAO;
import com.souzamonteiro.nfe.model.Configuracao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.json.JSONObject;

@WebServlet("/pdf/*")
public class PDFServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = null;
        String chave = request.getParameter("chave");
        
        // Determinar ação baseada na URL
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            if (pathInfo.startsWith("/view")) {
                action = "view";
            } else if (pathInfo.startsWith("/download")) {
                action = "download";
            } else {
                // Tentar extrair chave da URL
                chave = pathInfo.substring(1);
                if (chave.endsWith(".pdf")) {
                    chave = chave.substring(0, chave.length() - 4);
                }
                action = "view";
            }
        }
        
        if (chave == null || chave.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Chave da NF-e não informada");
            return;
        }
        
        try {
            ConfiguracaoDAO configDAO = new ConfiguracaoDAO();
            Configuracao config = configDAO.getConfiguracao();
            
            if (config == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    "Configurações não encontradas");
                return;
            }

            String caminhoXML = config.getCaminhoXml();
            String[] caminhos = new String[] {
                caminhoXML,
                "/home/roberto/xml",
                "/home/xml",
                System.getProperty("user.home") + "/xml"
            };

            String diretorioPdf = null;
            File pdfFile = null;
            for (String caminho : caminhos) {
                if (caminho == null || caminho.trim().isEmpty()) continue;
                File dir = new File(caminho);
                File candidatoPdf = new File(dir, chave + ".pdf");
                if (candidatoPdf.exists()) {
                    pdfFile = candidatoPdf;
                    diretorioPdf = dir.getAbsolutePath();
                    break;
                }
            }

            if (pdfFile == null) {
                File xmlFile = null;
                String diretorioXml = null;
                for (String caminho : caminhos) {
                    if (caminho == null || caminho.trim().isEmpty()) continue;
                    File dir = new File(caminho);
                    File candidatoXml = new File(dir, chave + ".xml");
                    if (candidatoXml.exists()) {
                        xmlFile = candidatoXml;
                        diretorioXml = dir.getAbsolutePath();
                        break;
                    }
                }

                if (xmlFile != null) {
                    try {
                        String xml = new String(Files.readAllBytes(xmlFile.toPath()), StandardCharsets.UTF_8);
                        String emitterUrl = "http://localhost:" + config.getPortaServidor() + "/NFeGerarPDF";
                        JSONObject payload = new JSONObject();
                        payload.put("chave", chave);
                        payload.put("xml", xml);
                        HttpURLConnection conn = (HttpURLConnection) new URL(emitterUrl).openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setDoOutput(true);
                        try (OutputStream os = conn.getOutputStream()) {
                            os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
                        }
                        int code = conn.getResponseCode();
                        if (code >= 400) {
                            throw new RuntimeException("Erro ao gerar PDF no emissor: HTTP " + code);
                        }

                        File dirDestino = new File(diretorioXml != null ? diretorioXml : caminhoXML);
                        dirDestino.mkdirs();
                        pdfFile = new File(dirDestino, chave + ".pdf");
                        if (!pdfFile.exists()) {
                            throw new RuntimeException("PDF não foi gerado no diretório configurado: " + pdfFile.getAbsolutePath());
                        }
                    } catch (Exception e) {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "XML encontrado, mas não foi possível gerar o PDF: " + e.getMessage());
                        return;
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "PDF e XML não encontrados para a chave: " + chave);
                    return;
                }
            }
            
            // Configurar headers baseados na ação
            if ("download".equals(action)) {
                // Para download
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", 
                    "attachment; filename=\"" + chave + ".pdf\"");
                response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            } else {
                // Para visualização
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=\"" + chave + ".pdf\"");
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "0");
            }
            
            // Enviar arquivo
            try (InputStream in = new FileInputStream(pdfFile);
                 OutputStream out = response.getOutputStream()) {
                
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }
            
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Erro ao processar PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}