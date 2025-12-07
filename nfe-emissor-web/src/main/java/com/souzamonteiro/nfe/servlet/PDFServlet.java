package com.souzamonteiro.nfe.servlet;

import com.souzamonteiro.nfe.dao.ConfiguracaoDAO;
import com.souzamonteiro.nfe.model.Configuracao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

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
            
            String caminhoPDF = config.getCaminhoXml() + "/" + chave + ".pdf";
            File pdfFile = new File(caminhoPDF);
            
            if (!pdfFile.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, 
                    "PDF não encontrado para a chave: " + chave);
                return;
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