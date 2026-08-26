package com.souzamonteiro.nfe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;

import com.souzamonteiro.nfe.model.Produto;

class TributacaoCalculatorTest {

    @Test
    void deveCalcularIcmsCst00() {
        Produto produto = new Produto();
        produto.setCstIcms("00");
        produto.setPicms(new BigDecimal("18.00"));

        BigDecimal resultado = TributacaoCalculator.calcularValorIcms(produto, new BigDecimal("1000.00"));

        assertEquals(new BigDecimal("180.00").setScale(2, RoundingMode.HALF_UP), resultado.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void deveCalcularIcmsComReducaoDeBase() {
        Produto produto = new Produto();
        produto.setCstIcms("20");
        produto.setPicms(new BigDecimal("18.00"));
        produto.setPredbcIcms(new BigDecimal("10.00"));

        BigDecimal resultado = TributacaoCalculator.calcularValorIcms(produto, new BigDecimal("1000.00"));

        assertEquals(new BigDecimal("162.00").setScale(2, RoundingMode.HALF_UP), resultado.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void deveCalcularPISECOFINS() {
        Produto produto = new Produto();
        produto.setPpis(new BigDecimal("1.65"));
        produto.setPcofins(new BigDecimal("7.60"));

        assertEquals(new BigDecimal("16.50"), TributacaoCalculator.calcularValorPis(produto, new BigDecimal("1000.00")).setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("76.00"), TributacaoCalculator.calcularValorCofins(produto, new BigDecimal("1000.00")).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void deveCalcularIbsCbs() {
        Produto produto = new Produto();
        produto.setPibsUf(new BigDecimal("0.10"));
        produto.setPibsMun(new BigDecimal("0.00"));
        produto.setPcbs(new BigDecimal("0.90"));

        assertEquals(new BigDecimal("1.00"), TributacaoCalculator.calcularValorIbsUf(produto, new BigDecimal("1000.00")).setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("9.00"), TributacaoCalculator.calcularValorCbs(produto, new BigDecimal("1000.00")).setScale(2, RoundingMode.HALF_UP));
    }
}
