package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.model.Venda;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VendaControllerFilterTest {

    @Test
    void deveFiltrarVendasPorStatusPendentes() {
        VendaController controller = new VendaController();
        Venda emitida = new Venda();
        emitida.setStatus("EMITIDA");

        Venda pendente = new Venda();
        pendente.setStatus("PENDENTE");

        controller.setFiltroStatus("PENDENTE");

        List<Venda> filtradas = controller.aplicarFiltroStatus(Arrays.asList(emitida, pendente));

        assertNotNull(filtradas);
        assertEquals(1, filtradas.size());
        assertEquals("PENDENTE", filtradas.get(0).getStatus());
    }

    @Test
    void deveFiltrarVendasPorPeriodo7Dias() {
        VendaController controller = new VendaController();
        controller.setFiltroPeriodo("7D");

        Venda antiga = new Venda();
        Calendar antigaCalendar = Calendar.getInstance();
        antigaCalendar.add(Calendar.DAY_OF_YEAR, -10);
        antiga.setDataVenda(antigaCalendar.getTime());

        Venda recente = new Venda();
        Calendar recenteCalendar = Calendar.getInstance();
        recente.setDataVenda(recenteCalendar.getTime());

        List<Venda> filtradas = controller.aplicarFiltroPeriodo(Arrays.asList(antiga, recente));

        assertNotNull(filtradas);
        assertEquals(1, filtradas.size());
        assertEquals(recente.getDataVenda(), filtradas.get(0).getDataVenda());
    }

    @Test
    void devePriorizarTipoDoRelatorioSobreStatus() {
        VendaController controller = new VendaController();
        controller.setFiltroRelatorioTipo("EMITIDAS");
        controller.setFiltroRelatorioStatus("ERRO");

        Venda emitida = new Venda();
        emitida.setStatus("EMITIDA");

        Venda pendente = new Venda();
        pendente.setStatus("PENDENTE");

        List<Venda> filtradas = controller.obterVendasRelatorio(Arrays.asList(emitida, pendente));

        assertNotNull(filtradas);
        assertEquals(1, filtradas.size());
        assertEquals("EMITIDA", filtradas.get(0).getStatus());
    }
}
