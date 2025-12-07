package com.souzamonteiro.nfe.service;

import com.souzamonteiro.nfe.model.Empresa;
import com.souzamonteiro.nfe.model.Venda;
import com.souzamonteiro.nfe.util.GeradorChavePixCopiaCola;

public class PixService {
    
    /**
     * Gera a chave PIX copia e cola para uma venda
     */
    public static String gerarChavePixParaVenda(Venda venda, Empresa empresa) {
        if (venda == null || empresa == null) {
            return null;
        }
        
        try {
            // Usar o telefone da empresa como chave PIX (ou CNPJ se não houver telefone)
            String chavePix = empresa.getFone();
            if (chavePix == null || chavePix.trim().isEmpty()) {
                chavePix = empresa.getCnpj(); // Usar CNPJ como fallback
            } else {
                chavePix = "+55" + chavePix;
            }
            
            // Garantir que a chave PIX tenha pelo menos 11 caracteres
            if (chavePix == null || chavePix.replaceAll("\\D", "").length() < 11) {
                // Usar CPF/CNPJ formatado se o telefone não for adequado
                chavePix = empresa.getCnpj().replaceAll("\\D", "");
            }
            
            // Formatar descrição com número da NF-e
            String descricao = "NF-e " + venda.getNumeroNfe();
            if (venda.getChaveNfe() != null) {
                descricao += " - " + venda.getChaveNfe().substring(25, 34); // Últimos dígitos da chave
            }
            
            // Gerar payload PIX usando a classe existente
            return GeradorChavePixCopiaCola.gerarPayloadPix(
                chavePix,
                empresa.getXfant(),
                empresa.getXmun(),
                venda.getValorTotal(),
                descricao
            );
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Verifica se uma chave PIX é válida
     */
    public static boolean isChavePixValida(String chavePix) {
        return chavePix != null && chavePix.length() > 50; // PIX válido tem mais de 50 caracteres
    }
}