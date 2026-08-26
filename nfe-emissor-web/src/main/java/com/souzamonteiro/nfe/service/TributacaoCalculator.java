package com.souzamonteiro.nfe.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.souzamonteiro.nfe.model.Produto;

public final class TributacaoCalculator {

    private TributacaoCalculator() {
    }

    public static BigDecimal calcularValorIcms(Produto produto, BigDecimal valorItem) {
        if (valorItem == null) {
            return BigDecimal.ZERO;
        }

        String cst = produto != null && produto.getCstIcms() != null ? produto.getCstIcms() : "00";
        BigDecimal aliquota = produto != null && produto.getPicms() != null ? produto.getPicms() : BigDecimal.ZERO;

        if ("20".equals(cst)) {
            BigDecimal percentualReducao = produto != null && produto.getPredbcIcms() != null ? produto.getPredbcIcms() : BigDecimal.ZERO;
            BigDecimal baseReduzida = valorItem.multiply(BigDecimal.ONE.subtract(percentualReducao.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP)))
                .setScale(2, RoundingMode.HALF_UP);
            return calcularPercentual(baseReduzida, aliquota);
        }

        if ("40".equals(cst) || "41".equals(cst) || "60".equals(cst)) {
            return BigDecimal.ZERO;
        }

        return calcularPercentual(valorItem, aliquota);
    }

    public static BigDecimal calcularValorPis(Produto produto, BigDecimal valorItem) {
        BigDecimal aliquota = produto != null && produto.getPpis() != null ? produto.getPpis() : new BigDecimal("1.65");
        return calcularPercentual(valorItem, aliquota);
    }

    public static BigDecimal calcularValorCofins(Produto produto, BigDecimal valorItem) {
        BigDecimal aliquota = produto != null && produto.getPcofins() != null ? produto.getPcofins() : new BigDecimal("7.60");
        return calcularPercentual(valorItem, aliquota);
    }

    public static BigDecimal calcularValorIbsUf(Produto produto, BigDecimal valorItem) {
        BigDecimal aliquota = produto != null && produto.getPibsUf() != null ? produto.getPibsUf() : BigDecimal.ZERO;
        return calcularPercentual(valorItem, aliquota);
    }

    public static BigDecimal calcularValorIbsMun(Produto produto, BigDecimal valorItem) {
        BigDecimal aliquota = produto != null && produto.getPibsMun() != null ? produto.getPibsMun() : BigDecimal.ZERO;
        return calcularPercentual(valorItem, aliquota);
    }

    public static BigDecimal calcularValorCbs(Produto produto, BigDecimal valorItem) {
        BigDecimal aliquota = produto != null && produto.getPcbs() != null ? produto.getPcbs() : BigDecimal.ZERO;
        return calcularPercentual(valorItem, aliquota);
    }

    public static BigDecimal calcularValorIcmsSt(Produto produto, BigDecimal baseSt, BigDecimal aliquotaInterna) {
        if (produto == null || baseSt == null || aliquotaInterna == null) {
            return BigDecimal.ZERO;
        }

        return calcularPercentual(baseSt, aliquotaInterna);
    }

    private static BigDecimal calcularPercentual(BigDecimal base, BigDecimal percentual) {
        if (base == null || percentual == null) {
            return BigDecimal.ZERO;
        }
        return base.multiply(percentual).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
}
