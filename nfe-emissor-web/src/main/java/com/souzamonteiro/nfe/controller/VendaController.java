package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.*;
import com.souzamonteiro.nfe.model.*;
import com.souzamonteiro.nfe.service.PixService;
import com.souzamonteiro.nfe.service.LojaService;
import com.souzamonteiro.nfe.service.TributacaoCalculator;
import com.souzamonteiro.nfe.util.GeradorQRCodePix;

import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@ManagedBean
@ViewScoped
public class VendaController implements Serializable {
    
    private transient VendaDAO vendaDAO = new VendaDAO();
    private transient ClienteDAO clienteDAO = new ClienteDAO();
    private transient ProdutoDAO produtoDAO = new ProdutoDAO();
    private transient ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();
    private transient EmpresaDAO empresaDAO = new EmpresaDAO();
    
    private List<Venda> vendas;
    private List<Venda> vendasRecentes;
    private Venda venda;
    private List<Cliente> clientes;
    private List<Produto> produtos;
    private Cliente clienteSelecionado;
    private Produto produtoSelecionado;
    private ItemVenda itemVenda;
    private boolean editando = false; // Inicialize como false
    private String filtroStatus = "TODOS";
    private String filtroPeriodo = "30D";
    private String filtroOperacoesRecentes = "TODOS";
    private String filtroRelatorioTipo = "RESUMO";
    private String filtroRelatorioStatus = "TODOS";
    private Date dataInicio;
    private Date dataFim;
    private Date filtroRelatorioInicio;
    private Date filtroRelatorioFim;
    private List<Venda> relatorioVendas = new ArrayList<>();
    private BigDecimal relatorioValorTotal = BigDecimal.ZERO;
    private BigDecimal relatorioTicketMedio = BigDecimal.ZERO;
    private long relatorioQuantidade;
    private long vendasHoje;
    private long totalVendasEmitidas;
    private long totalVendasPendentes;
    private BigDecimal faturamentoTotal;
    private BigDecimal faturamentoMes;
    private BigDecimal faturamentoHoje;
    private BigDecimal ticketMedio;
    private BigDecimal variacaoDia;
    private BarChartModel dashboardChart;
    private PieChartModel statusChart;
    
    @PostConstruct
    public void init() {
        carregarVendas();
        carregarRelatorio();
        carregarClientes();
        carregarProdutos();
    }
    
    public void carregarVendas() {
        vendas = obterVendasPorFiltro();
        atualizarMetricasDashboard();
        editando = false;
    }

    public List<Venda> obterVendasPorFiltro() {
        List<Venda> lista = vendaDAO.findAll();
        if (lista == null) {
            return new ArrayList<>();
        }

        if (filtroStatus != null && !"TODOS".equalsIgnoreCase(filtroStatus)) {
            lista = aplicarFiltroStatus(lista);
        }

        if (filtroPeriodo != null && !"TODOS".equalsIgnoreCase(filtroPeriodo)) {
            lista = aplicarFiltroPeriodo(lista);
        }

        return lista;
    }

    public List<Venda> aplicarFiltroStatus(List<Venda> lista) {
        if (lista == null || lista.isEmpty()) {
            return new ArrayList<>();
        }
        if (filtroStatus == null || "TODOS".equalsIgnoreCase(filtroStatus)) {
            return lista;
        }

        List<Venda> filtradas = new ArrayList<>();
        for (Venda vendaAtual : lista) {
            if (vendaAtual != null && filtroStatus.equalsIgnoreCase(vendaAtual.getStatus())) {
                filtradas.add(vendaAtual);
            }
        }
        return filtradas;
    }

    public List<Venda> aplicarFiltroPeriodo(List<Venda> lista) {
        if (lista == null || lista.isEmpty()) {
            return new ArrayList<>();
        }

        Calendar inicio = Calendar.getInstance();
        Calendar fim = Calendar.getInstance();

        if ("HOJE".equalsIgnoreCase(filtroPeriodo)) {
            inicio.setTime(new Date());
            fim.setTime(new Date());
            inicio.set(Calendar.HOUR_OF_DAY, 0);
            inicio.set(Calendar.MINUTE, 0);
            inicio.set(Calendar.SECOND, 0);
            inicio.set(Calendar.MILLISECOND, 0);
            fim.set(Calendar.HOUR_OF_DAY, 23);
            fim.set(Calendar.MINUTE, 59);
            fim.set(Calendar.SECOND, 59);
            fim.set(Calendar.MILLISECOND, 999);
        } else if ("7D".equalsIgnoreCase(filtroPeriodo)) {
            inicio.add(Calendar.DAY_OF_YEAR, -6);
            inicio.set(Calendar.HOUR_OF_DAY, 0);
            inicio.set(Calendar.MINUTE, 0);
            inicio.set(Calendar.SECOND, 0);
            inicio.set(Calendar.MILLISECOND, 0);
        } else if ("30D".equalsIgnoreCase(filtroPeriodo)) {
            inicio.add(Calendar.DAY_OF_YEAR, -29);
            inicio.set(Calendar.HOUR_OF_DAY, 0);
            inicio.set(Calendar.MINUTE, 0);
            inicio.set(Calendar.SECOND, 0);
            inicio.set(Calendar.MILLISECOND, 0);
        } else {
            return lista;
        }

        List<Venda> filtradas = new ArrayList<>();
        for (Venda vendaAtual : lista) {
            if (vendaAtual == null || vendaAtual.getDataVenda() == null) {
                continue;
            }
            Date dataVenda = vendaAtual.getDataVenda();
            Calendar data = Calendar.getInstance();
            data.setTime(dataVenda);

            if (!data.before(inicio) && !data.after(fim)) {
                filtradas.add(vendaAtual);
            }
        }
        return filtradas;
    }

    public void aplicarFiltros() {
        vendas = obterVendasPorFiltro();
        atualizarMetricasDashboard();
    }

    public void limparFiltros() {
        filtroStatus = "TODOS";
        filtroPeriodo = "30D";
        dataInicio = null;
        dataFim = null;
        carregarVendas();
    }

    public void aplicarFiltroOperacoesRecentes() {
        atualizarMetricasDashboard();
    }

    public List<Venda> obterVendasRelatorio(List<Venda> base) {
        List<Venda> lista = base == null ? new ArrayList<>() : new ArrayList<>(base);
        if (lista.isEmpty()) {
            return lista;
        }

        String statusRelatorio = filtroRelatorioStatus;
        if ("PENDENTES".equalsIgnoreCase(filtroRelatorioTipo)) {
            statusRelatorio = "PENDENTE";
        } else if ("EMITIDAS".equalsIgnoreCase(filtroRelatorioTipo)) {
            statusRelatorio = "EMITIDA";
        }

        if (statusRelatorio != null && !"TODOS".equalsIgnoreCase(statusRelatorio)) {
            List<Venda> filtradasStatus = new ArrayList<>();
            for (Venda vendaAtual : lista) {
                if (vendaAtual != null && statusRelatorio.equalsIgnoreCase(vendaAtual.getStatus())) {
                    filtradasStatus.add(vendaAtual);
                }
            }
            lista = filtradasStatus;
        }

        if (filtroRelatorioInicio != null || filtroRelatorioFim != null) {
            List<Venda> filtradasData = new ArrayList<>();
            for (Venda vendaAtual : lista) {
                if (vendaAtual == null || vendaAtual.getDataVenda() == null) {
                    continue;
                }
                Date dataVenda = vendaAtual.getDataVenda();
                boolean ok = true;
                if (filtroRelatorioInicio != null && dataVenda.before(filtroRelatorioInicio)) {
                    ok = false;
                }
                if (filtroRelatorioFim != null && dataVenda.after(filtroRelatorioFim)) {
                    ok = false;
                }
                if (ok) {
                    filtradasData.add(vendaAtual);
                }
            }
            lista = filtradasData;
        }

        return lista;
    }

    public void carregarRelatorio() {
        List<Venda> base = vendaDAO.findAll();
        if (base == null) {
            base = new ArrayList<>();
        }

        relatorioVendas = obterVendasRelatorio(base);
        relatorioQuantidade = relatorioVendas.size();
        relatorioValorTotal = BigDecimal.ZERO;
        relatorioTicketMedio = BigDecimal.ZERO;

        for (Venda vendaAtual : relatorioVendas) {
            if (vendaAtual != null && vendaAtual.getValorTotal() != null) {
                relatorioValorTotal = relatorioValorTotal.add(vendaAtual.getValorTotal());
            }
        }

        if (relatorioQuantidade > 0 && relatorioValorTotal.compareTo(BigDecimal.ZERO) > 0) {
            relatorioTicketMedio = relatorioValorTotal.divide(BigDecimal.valueOf(relatorioQuantidade), 2, java.math.RoundingMode.HALF_UP);
        }
    }

    public void limparRelatorio() {
        filtroRelatorioTipo = "RESUMO";
        filtroRelatorioStatus = "TODOS";
        filtroRelatorioInicio = null;
        filtroRelatorioFim = null;
        carregarRelatorio();
    }

    private void atualizarMetricasDashboard() {
        vendasHoje = 0;
        totalVendasEmitidas = 0;
        totalVendasPendentes = 0;
        faturamentoTotal = BigDecimal.ZERO;
        faturamentoMes = BigDecimal.ZERO;
        faturamentoHoje = BigDecimal.ZERO;
        ticketMedio = BigDecimal.ZERO;
        variacaoDia = BigDecimal.ZERO;

        if (vendas == null) {
            vendas = new ArrayList<>();
        }

        Calendar hoje = Calendar.getInstance();
        int mesAtual = hoje.get(Calendar.MONTH);
        int anoAtual = hoje.get(Calendar.YEAR);

        Calendar ontem = Calendar.getInstance();
        ontem.add(Calendar.DAY_OF_YEAR, -1);
        BigDecimal faturamentoOntem = BigDecimal.ZERO;

        for (Venda vendaAtual : vendas) {
            if ("EMITIDA".equalsIgnoreCase(vendaAtual.getStatus())) {
                totalVendasEmitidas++;
            } else if ("PENDENTE".equalsIgnoreCase(vendaAtual.getStatus())) {
                totalVendasPendentes++;
            }

            if (vendaAtual.getValorTotal() != null) {
                faturamentoTotal = faturamentoTotal.add(vendaAtual.getValorTotal());
            }

            if (vendaAtual.getDataVenda() != null) {
                Calendar dataVenda = Calendar.getInstance();
                dataVenda.setTime(vendaAtual.getDataVenda());

                if (dataVenda.get(Calendar.DAY_OF_YEAR) == hoje.get(Calendar.DAY_OF_YEAR)
                        && dataVenda.get(Calendar.YEAR) == hoje.get(Calendar.YEAR)) {
                    vendasHoje++;
                    if (vendaAtual.getValorTotal() != null) {
                        faturamentoHoje = faturamentoHoje.add(vendaAtual.getValorTotal());
                    }
                }

                if (dataVenda.get(Calendar.DAY_OF_YEAR) == ontem.get(Calendar.DAY_OF_YEAR)
                        && dataVenda.get(Calendar.YEAR) == ontem.get(Calendar.YEAR)) {
                    if (vendaAtual.getValorTotal() != null) {
                        faturamentoOntem = faturamentoOntem.add(vendaAtual.getValorTotal());
                    }
                }

                if (dataVenda.get(Calendar.MONTH) == mesAtual
                        && dataVenda.get(Calendar.YEAR) == anoAtual) {
                    if (vendaAtual.getValorTotal() != null) {
                        faturamentoMes = faturamentoMes.add(vendaAtual.getValorTotal());
                    }
                }
            }
        }

        if (vendasHoje > 0) {
            ticketMedio = faturamentoHoje.divide(BigDecimal.valueOf(vendasHoje), 2, java.math.RoundingMode.HALF_UP);
        }

        if (faturamentoOntem.compareTo(BigDecimal.ZERO) > 0) {
            variacaoDia = faturamentoHoje.subtract(faturamentoOntem)
                    .divide(faturamentoOntem, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        } else if (faturamentoHoje.compareTo(BigDecimal.ZERO) > 0) {
            variacaoDia = new BigDecimal("100");
        } else {
            variacaoDia = BigDecimal.ZERO;
        }

        List<Venda> recentesVisiveis = new ArrayList<>();
        for (Venda vendaAtual : vendas) {
            if ("TODOS".equalsIgnoreCase(filtroOperacoesRecentes) || filtroOperacoesRecentes.equalsIgnoreCase(vendaAtual.getStatus())) {
                recentesVisiveis.add(vendaAtual);
            }
        }

        vendasRecentes = new ArrayList<>();
        int qtdRecentes = 0;
        for (int i = recentesVisiveis.size() - 1; i >= 0 && qtdRecentes < 5; i--) {
            vendasRecentes.add(recentesVisiveis.get(i));
            qtdRecentes++;
        }

        dashboardChart = criarGraficoUltimos7Dias();
        statusChart = criarGraficoStatusOperacao();
    }

    private PieChartModel criarGraficoStatusOperacao() {
        PieChartModel model = new PieChartModel();
        long emitidas = totalVendasEmitidas;
        long pendentes = totalVendasPendentes;
        long erros = 0;

        for (Venda vendaAtual : vendaDAO.findAll()) {
            if ("ERRO".equalsIgnoreCase(vendaAtual.getStatus())) {
                erros++;
            }
        }

        if (emitidas > 0) {
            model.set("Emitidas", emitidas);
        }
        if (pendentes > 0) {
            model.set("Pendentes", pendentes);
        }
        if (erros > 0) {
            model.set("Erros", erros);
        }

        if (emitidas == 0 && pendentes == 0 && erros == 0) {
            model.set("Sem dados", 1);
        }

        model.setTitle("Status da operação");
        model.setLegendPosition("e");
        model.setShadow(false);
        model.setShowDataLabels(true);
        model.setDiameter(150);
        return model;
    }

    private BarChartModel criarGraficoUltimos7Dias() {
        BarChartModel model = new BarChartModel();
        ChartSeries series = new ChartSeries();
        series.setLabel("NF-e emitidas");

        for (int i = 6; i >= 0; i--) {
            Calendar data = Calendar.getInstance();
            data.add(Calendar.DAY_OF_YEAR, -i);
            String label = new SimpleDateFormat("dd/MM").format(data.getTime());
            int quantidade = 0;

            for (Venda vendaAtual : vendas) {
                if (vendaAtual.getDataVenda() == null) {
                    continue;
                }

                Calendar vendaCalendar = Calendar.getInstance();
                vendaCalendar.setTime(vendaAtual.getDataVenda());

                if (vendaCalendar.get(Calendar.DAY_OF_YEAR) == data.get(Calendar.DAY_OF_YEAR)
                        && vendaCalendar.get(Calendar.YEAR) == data.get(Calendar.YEAR)) {
                    quantidade++;
                }
            }

            series.set(label, quantidade);
        }

        model.addSeries(series);
        model.setTitle("Emissões dos últimos 7 dias");
        model.setLegendPosition("ne");
        model.setShowPointLabels(true);
        model.setAnimate(true);
        model.setShadow(false);

        Axis xAxis = model.getAxis(AxisType.X);
        xAxis.setLabel("Dia");

        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("Quantidade");
        yAxis.setMin(0);

        return model;
    }
    
    public void novaVenda() {
        venda = new Venda();
        venda.setDataVenda(new Date());
        venda.setStatus("PENDENTE");
        venda.setValorTotal(BigDecimal.ZERO);
        venda.setItemVendaCollection(new ArrayList<>()); // Inicializar a coleção
        itemVenda = new ItemVenda();
        itemVenda.setQuantidade(BigDecimal.ONE);
        clienteSelecionado = null;
        produtoSelecionado = null;
        editando = true;
    }
    
    public void carregarClientes() {
        clientes = clienteDAO.findAtivos();
    }
    
    public void carregarProdutos() {
        produtos = produtoDAO.findAtivos();
    }
    
    public void adicionarItem() {
        if (produtoSelecionado != null && itemVenda.getQuantidade() != null && 
            itemVenda.getQuantidade().compareTo(BigDecimal.ZERO) > 0) {

            // Verificar se produto já está na venda
            for (ItemVenda item : venda.getItemVendaCollection()) {
                if (item.getProdutoId() != null && 
                    item.getProdutoId().getId().equals(produtoSelecionado.getId())) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, 
                        "Aviso", "Produto já adicionado à venda."));
                    return;
                }
            }

            ItemVenda novoItem = new ItemVenda();
            novoItem.setProdutoId(produtoSelecionado);
            novoItem.setQuantidade(itemVenda.getQuantidade());
            novoItem.setValorUnitario(produtoSelecionado.getVuncom());
            novoItem.setValorTotal(itemVenda.getQuantidade().multiply(produtoSelecionado.getVuncom()));
            novoItem.setDataCriacao(new Date()); // Adicionar data de criação
            
            // *** CORREÇÃO CRÍTICA: Associar a venda ao item ***
            novoItem.setVendaId(venda);

            venda.getItemVendaCollection().add(novoItem);

            // Calcular valor total da venda
            calcularTotalVenda();

            // Limpar seleção
            produtoSelecionado = null;
            itemVenda = new ItemVenda();
            itemVenda.setQuantidade(BigDecimal.ONE);

            // Atualizar componente via AJAX
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds()
                .add("form:vendaItens");
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds()
                .add("form:totalVenda");
        }
    }
    
    public void removerItem(ItemVenda item) {
        if (venda.getItemVendaCollection() != null) {
            venda.getItemVendaCollection().remove(item); // Corrigido: usando getItemVendaCollection()
            calcularTotalVenda();
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("form:vendaItens");
        }
    }
    
    private void calcularTotalVenda() {
        BigDecimal total = BigDecimal.ZERO;
        if (venda.getItemVendaCollection() != null) {
            for (ItemVenda item : venda.getItemVendaCollection()) {
                if (item.getValorTotal() != null) {
                    total = total.add(item.getValorTotal());
                }
            }
        }
        venda.setValorTotal(total);
    }
    
    private static final Object BLOQUEIO_EMISSAO = new Object();

    public void finalizarVenda() {
        synchronized (BLOQUEIO_EMISSAO) {
            finalizarVendaInterna();
        }
    }

    private void finalizarVendaInterna() {
        if (venda.getClienteId() == null) {
            System.out.println("ERRO: Cliente é null");
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Selecione um cliente."));
            return;
        }
        
        if (venda.getItemVendaCollection() == null || venda.getItemVendaCollection().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Adicione pelo menos um item à venda."));
            return;
        }
        
        try {
            // *** CORREÇÃO CRÍTICA: Garantir que todos os itens tenham referência à venda ***
            for (ItemVenda item : venda.getItemVendaCollection()) {
                if (item.getVendaId() == null) {
                    item.setVendaId(venda);
                }
                if (item.getDataCriacao() == null) {
                    item.setDataCriacao(new Date());
                }
            }
            
            // Associar cliente à venda
            venda.setClienteId(clienteSelecionado);
            
            // Preencher datas obrigatórias
            venda.setDataCriacao(new Date());
            
            Empresa empresa = empresaDAO.getEmpresa();
            Configuracao config = configuracaoDAO.getConfiguracao();
            
            // Gerar número da NF-e
            Integer numeroNFe = config.getNumeroNfe();
            venda.setNumeroNfe(numeroNFe);
            
            // Salvar venda
            vendaDAO.save(venda);
            
            // Emitir NF-e
            boolean sucesso = emitirNFe(venda);
            
            if (sucesso) {
                venda.setStatus("EMITIDA");
                venda.setDataAtualizacao(new Date());
                
                String chavePix = PixService.gerarChavePixParaVenda(venda, empresa);
                if (PixService.isChavePixValida(chavePix)) {
                    venda.setChavePix(chavePix);
                }
                
                vendaDAO.save(venda);
                
                config.setNumeroNfe(numeroNFe + 1);
                configuracaoDAO.save(config);
                
                abrirPDFAposEmissao();

                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "NF-e emitida com sucesso", "Venda finalizada. Confira o DANFE e as ações rápidas abaixo."));

                carregarVendas();
            } else {
                venda.setStatus("ERRO");
                venda.setDataAtualizacao(new Date());
                vendaDAO.save(venda);
                
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Erro ao emitir NF-e. Venda salva como pendente."));
            }
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Erro ao finalizar venda: " + e.getMessage()));
            e.printStackTrace(); // Para debug
        }
    }
    
    public void cancelarVenda() {
        venda = null;
        itemVenda = null;
        clienteSelecionado = null;
        produtoSelecionado = null;
        editando = false;
        carregarVendas();
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO,
            "Operação cancelada", "A venda foi descartada e o formulário foi fechado."));
    }
    
    static void emitirPedidoLoja(Integer id, Integer operadorId) {
        synchronized (BLOQUEIO_EMISSAO) {
            LojaService loja = new LojaService();
            VendaController emissor = new VendaController();
            Configuracao config = emissor.configuracaoDAO.getConfiguracao();
            Empresa empresa = emissor.empresaDAO.getEmpresa();
            if (config == null || empresa == null || config.getPortaServidor() == null) {
                throw new IllegalStateException("Configure a empresa e o serviço de emissão de NF-e.");
            }
            Venda pedido = loja.reservarEmissao(id, operadorId);
            JSONObject nota;
            try {
                nota = emissor.construirJsonNFe(pedido, empresa, config);
            } catch (RuntimeException e) {
                loja.liberarEmissaoNaoEnviada(id, operadorId);
                throw e;
            }
            try {
                if (emissor.enviarNFe(pedido, config, nota)) {
                    loja.registrarAutorizacao(pedido);
                }
            } catch (RejeicaoSemIntermediador e) {
                loja.liberarEmissaoNaoEnviada(id, operadorId);
                mensagemErroEmissao(e.getMessage());
            } catch (IllegalStateException e) {
                if (e.getCause() instanceof java.net.ConnectException) loja.liberarEmissaoNaoEnviada(id, operadorId);
                throw e;
            }
        }
    }

    private boolean emitirNFe(Venda venda) {
        try {
            Configuracao config = configuracaoDAO.getConfiguracao();
            Empresa empresa = empresaDAO.getEmpresa();
            if (config == null || empresa == null) {
                mensagemErroEmissao("Configure empresa e configurações antes de emitir NF-e.");
                return false;
            }
            return enviarNFe(venda, config, construirJsonNFe(venda, empresa, config));
        } catch (RuntimeException e) {
            mensagemErroEmissao(e.getMessage());
            return false;
        }
    }

    private boolean enviarNFe(Venda venda, Configuracao config, JSONObject nota) {
        try {
            String url = "http://localhost:" + config.getPortaServidor() + "/NFeAutorizacao";
            JSONObject resposta = new JSONObject(enviarParaServidor(url, nota.toString()));
            String status = resposta.optString("cStat");
            String motivo = resposta.optString("xMotivo", "O emissor não confirmou a autorização.");
            if ("100".equals(status)) {
                String chave = resposta.optString("chave").replaceFirst("^NFe", "");
                String protocolo = resposta.optString("nProt");
                if (chave.matches("[0-9]{44}") && !protocolo.isEmpty()) {
                    venda.setChaveNfe(chave);
                    venda.setProtocoloNfe(protocolo);
                    return true;
                }
                mensagemErroEmissao("A resposta da autorização não trouxe chave e protocolo válidos.");
            } else if (venda.getPedidoLoja() != null && ("434".equals(status)
                    || ("000".equals(status) && motivo.startsWith("434 - ")))) {
                throw new RejeicaoSemIntermediador(motivo);
            } else {
                mensagemErroEmissao(motivo);
            }
        } catch (RejeicaoSemIntermediador e) {
            throw e;
        } catch (java.net.ConnectException e) {
            if (venda.getPedidoLoja() != null) throw new IllegalStateException("Serviço de emissão indisponível.", e);
            mensagemErroEmissao(e.getMessage());
        } catch (Exception e) {
            mensagemErroEmissao(e.getMessage());
        }
        return false;
    }

    private static class RejeicaoSemIntermediador extends RuntimeException {
        RejeicaoSemIntermediador(String mensagem) {
            super(mensagem);
        }
    }

    private static void mensagemErroEmissao(String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao emitir NF-e", detalhe));
    }

    private JSONObject construirJsonNFe(Venda venda, Empresa empresa, Configuracao config) {
        JSONObject nfeJson = new JSONObject();
        JSONObject infNFe = new JSONObject();

        // 1. IDE (Identificação da NF-e)
        JSONObject ide = new JSONObject();
        ide.put("cUF", getCodigoUF(config.getEmitenteUf()));

        // cNF deve ser string com 8 dígitos
        String cnfStr = String.format("%08d", venda.getNumeroNfe() + 1);
        ide.put("cNF", cnfStr);

        ide.put("natOp", config.getNaturezaOperacao());
        ide.put("mod", "55");

        // serie deve ser string
        ide.put("serie", config.getSerieNfe().toString());

        // nNF deve ser string - CORREÇÃO AQUI
        String nNFStr = venda.getNumeroNfe().toString();
        ide.put("nNF", nNFStr);

        // Formatar data no formato correto
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ide.put("dhEmi", sdf.format(venda.getDataVenda()));

        ide.put("tpNF", "1");
        boolean pedidoLoja = venda.getPedidoLoja() != null;
        boolean outraUf = !empresa.getUf().equalsIgnoreCase(venda.getClienteId().getUf());
        ide.put("idDest", pedidoLoja && outraUf ? "2" : "1");
        ide.put("cMunFG", empresa.getCmun());
        ide.put("tpImp", "1");
        ide.put("tpEmis", "1");
        ide.put("cDV", "6"); // Para homologação
        ide.put("tpAmb", config.getWebserviceAmbiente());
        ide.put("finNFe", config.getFinalidadeEmissao());
        ide.put("indFinal", pedidoLoja ? "1" : config.getConsumidorFinal());
        ide.put("indPres", pedidoLoja ? "2" : config.getPresencaComprador());
        if (pedidoLoja) ide.put("indIntermed", "0");
        ide.put("procEmi", "0");
        ide.put("verProc", "1.0");

        infNFe.put("ide", ide);

        // 2. EMITENTE
        JSONObject emit = new JSONObject();
        emit.put("CNPJ", empresa.getCnpj());
        emit.put("xNome", empresa.getXnome());
        emit.put("IE", empresa.getIe());
        emit.put("CRT", empresa.getCrt());

        JSONObject enderEmit = new JSONObject();
        enderEmit.put("xLgr", empresa.getXlgr());
        enderEmit.put("nro", empresa.getNro());
        if (empresa.getXcpl() != null) {
            enderEmit.put("xCpl", empresa.getXcpl());
        }
        enderEmit.put("xBairro", empresa.getXbairro());
        enderEmit.put("cMun", empresa.getCmun());
        enderEmit.put("xMun", empresa.getXmun());
        enderEmit.put("UF", empresa.getUf());
        enderEmit.put("CEP", empresa.getCep());
        enderEmit.put("cPais", "1058");
        enderEmit.put("xPais", "Brasil");
        enderEmit.put("fone", empresa.getFone() != null ? empresa.getFone() : "");
        if (empresa.getFone() != null) {
            enderEmit.put("fone", empresa.getFone());
        }
        
        emit.put("enderEmit", enderEmit);
        infNFe.put("emit", emit);

        // 3. DESTINATÁRIO
        JSONObject dest = new JSONObject();
        Cliente cliente = venda.getClienteId();

        if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
            dest.put("CPF", cliente.getCpf().replaceAll("\\D", ""));
            dest.put("xNome", cliente.getXnome());
            String xNome = cliente.getXnome();
            if ("2".equals(config.getWebserviceAmbiente())) { // Se for homologação
                xNome = "NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL";
            }
            dest.put("xNome", xNome);
        } else if (cliente.getCnpj() != null && !cliente.getCnpj().trim().isEmpty()) {
            dest.put("CNPJ", cliente.getCnpj().replaceAll("\\D", ""));
            String xNome = cliente.getXnome();
            if ("2".equals(config.getWebserviceAmbiente())) { // Se for homologação
                xNome = "NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL";
            }
            dest.put("xNome", xNome);
        } else {
            // Consumidor não identificado
            dest.put("CPF", "00000000000");
            dest.put("xNome", "CONSUMIDOR FINAL");
        }

        JSONObject enderDest = new JSONObject();
        enderDest.put("xLgr", cliente.getXlgr());
        enderDest.put("nro", cliente.getNro());
        if (cliente.getXcpl() != null) {
            enderDest.put("xCpl", cliente.getXcpl());
        }
        enderDest.put("xBairro", cliente.getXbairro());
        enderDest.put("cMun", cliente.getCmun());
        enderDest.put("xMun", cliente.getXmun());
        enderDest.put("UF", cliente.getUf());
        enderDest.put("CEP", cliente.getCep().replaceAll("\\D", ""));
        enderDest.put("cPais", "1058");
        enderDest.put("xPais", "Brasil");
        if (cliente.getFone() != null) {
            enderDest.put("fone", cliente.getFone());
        }
        
        dest.put("enderDest", enderDest);
        infNFe.put("dest", dest);

        // 4. AUTORIZAÇÃO XML (para homologação)
        JSONObject autXML = new JSONObject();
        autXML.put("CNPJ", "13937073000156"); // CNPJ da SEFAZ de homologação
        infNFe.put("autXML", autXML);

        // 5. DETALHES (produtos)
        JSONArray detArray = new JSONArray();
        
        BigDecimal freteLoja = venda.getFreteLoja();
        BigDecimal subtotal = venda.getValorTotal().subtract(freteLoja);
        BigDecimal produtosAcumulados = BigDecimal.ZERO;
        BigDecimal freteAcumulado = BigDecimal.ZERO;
        for (ItemVenda item : venda.getItemVendaCollection()) {
            Produto produto = item.getProdutoId();

            JSONObject det = new JSONObject();

            JSONObject prod = new JSONObject();
            prod.put("cProd", produto.getCprod());

            // Código EAN
            String cean = produto.getCean();
            if (cean == null || cean.trim().isEmpty() || "SEM GTIN".equalsIgnoreCase(cean)) {
                prod.put("cEAN", "7898480650104"); // Código genérico para homologação
            } else {
                prod.put("cEAN", cean);
            }

            // **APENAS para homologação: usar mensagem obrigatória**
            // Mantenha a descrição real do produto, mas adicione a mensagem de homologação
            String xProd = produto.getXprod();
            if ("2".equals(config.getWebserviceAmbiente())) { // Se for homologação
                xProd = "NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL";
            }
            prod.put("xProd", xProd);

            prod.put("NCM", produto.getNcm());

            // CEST (se disponível)
            if (produto.getCest() != null && !produto.getCest().trim().isEmpty()) {
                prod.put("CEST", produto.getCest());
            }

            prod.put("indEscala", "S"); // Produzido em escala relevante
            prod.put("CFOP", produto.getCfop());
            prod.put("uCom", produto.getUcom());

            // Formatar números com casas decimais corretas
            prod.put("qCom", String.format(Locale.US, "%.4f", item.getQuantidade()));
            prod.put("vUnCom", String.format(Locale.US, "%.4f", item.getValorUnitario()));
            prod.put("vProd", String.format(Locale.US, "%.2f", item.getValorTotal()));
            if (freteLoja.signum() > 0) {
                produtosAcumulados = produtosAcumulados.add(item.getValorTotal());
                BigDecimal freteAteItem = freteLoja.multiply(produtosAcumulados)
                    .divide(subtotal, 2, java.math.RoundingMode.HALF_UP);
                prod.put("vFrete", formatar2(freteAteItem.subtract(freteAcumulado)));
                freteAcumulado = freteAteItem;
            }

            // Código EAN tributário
            prod.put("cEANTrib", prod.getString("cEAN"));
            prod.put("uTrib", produto.getUcom());
            prod.put("qTrib", String.format(Locale.US, "%.4f", item.getQuantidade()));
            prod.put("vUnTrib", String.format(Locale.US, "%.4f", item.getValorUnitario()));
            prod.put("indTot", "1");

            det.put("prod", prod);

            // IMPOSTOS
            JSONObject imposto = new JSONObject();

            // ICMS
            JSONObject icms = montarIcmsJson(produto, item.getValorTotal());
            imposto.put("ICMS", icms);

            // PIS
            JSONObject pis = new JSONObject();
            JSONObject pisAliq = new JSONObject();
            pisAliq.put("CST", produto.getCstPis() != null ? produto.getCstPis() : "01");
            pisAliq.put("vBC", String.format(Locale.US, "%.2f", item.getValorTotal()));

            String pPIS;
            if (produto.getPpis() != null) {
                pPIS = String.format(Locale.US, "%.2f", produto.getPpis());
            } else {
                pPIS = "1.65"; // Valor padrão para homologação
            }
            pisAliq.put("pPIS", pPIS);

            BigDecimal valorPIS = TributacaoCalculator.calcularValorPis(produto, item.getValorTotal());
            pisAliq.put("vPIS", String.format(Locale.US, "%.2f", valorPIS));

            pis.put("PISAliq", pisAliq);
            imposto.put("PIS", pis);

            // COFINS
            JSONObject cofins = new JSONObject();
            JSONObject cofinsAliq = new JSONObject();
            cofinsAliq.put("CST", produto.getCstCofins() != null ? produto.getCstCofins() : "01");
            cofinsAliq.put("vBC", String.format(Locale.US, "%.2f", item.getValorTotal()));

            String pCOFINS;
            if (produto.getPcofins() != null) {
                pCOFINS = String.format(Locale.US, "%.2f", produto.getPcofins());
            } else {
                pCOFINS = "7.60"; // Valor padrão para homologação
            }
            cofinsAliq.put("pCOFINS", pCOFINS);

            BigDecimal valorCOFINS = TributacaoCalculator.calcularValorCofins(produto, item.getValorTotal());
            cofinsAliq.put("vCOFINS", String.format(Locale.US, "%.2f", valorCOFINS));

            cofins.put("COFINSAliq", cofinsAliq);
            imposto.put("COFINS", cofins);

            // RTC / IBS-CBS
            String cstRtc = valorTexto(produto.getCstRtc(), valorTexto(config.getCstRtcPadrao(), "000"));
            String cClassTribRtc = valorTexto(produto.getCclasstribRtc(), valorTexto(config.getCclasstribRtcPadrao(), "000001"));
            BigDecimal pIbsUf = valorDecimal(produto.getPibsUf(), config.getPibsUfPadrao(), new BigDecimal("0.10"));
            BigDecimal pIbsMun = valorDecimal(produto.getPibsMun(), config.getPibsMunPadrao(), BigDecimal.ZERO);
            BigDecimal pCbs = valorDecimal(produto.getPcbs(), config.getPcbsPadrao(), new BigDecimal("0.90"));

            BigDecimal vIbsUf = TributacaoCalculator.calcularValorIbsUf(produto, item.getValorTotal());
            BigDecimal vIbsMun = TributacaoCalculator.calcularValorIbsMun(produto, item.getValorTotal());
            BigDecimal vIbs = vIbsUf.add(vIbsMun);
            BigDecimal vCbs = TributacaoCalculator.calcularValorCbs(produto, item.getValorTotal());

            JSONObject ibscbs = new JSONObject();
            ibscbs.put("CST", cstRtc);
            ibscbs.put("cClassTrib", cClassTribRtc);

            JSONObject gIBSCBS = new JSONObject();
            gIBSCBS.put("vBC", formatar2(item.getValorTotal()));

            JSONObject gIBSUF = new JSONObject();
            gIBSUF.put("pIBSUF", formatar2(pIbsUf));
            gIBSUF.put("vIBSUF", formatar2(vIbsUf));
            gIBSCBS.put("gIBSUF", gIBSUF);

            JSONObject gIBSMun = new JSONObject();
            gIBSMun.put("pIBSMun", formatar2(pIbsMun));
            gIBSMun.put("vIBSMun", formatar2(vIbsMun));
            gIBSCBS.put("gIBSMun", gIBSMun);

            gIBSCBS.put("vIBS", formatar2(vIbs));

            JSONObject gCBS = new JSONObject();
            gCBS.put("pCBS", formatar2(pCbs));
            gCBS.put("vCBS", formatar2(vCbs));
            gIBSCBS.put("gCBS", gCBS);

            ibscbs.put("gIBSCBS", gIBSCBS);
            imposto.put("IBSCBS", ibscbs);

            det.put("imposto", imposto);
            detArray.put(det);
        }

        infNFe.put("det", detArray);

        // 6. TOTAL
        JSONObject total = new JSONObject();
        JSONObject icmsTot = new JSONObject();

        // Calcular totais
        BigDecimal totalVenda = subtotal;
        BigDecimal totalICMS = BigDecimal.ZERO;
        BigDecimal totalICMSDeson = BigDecimal.ZERO;
        BigDecimal totalBCST = BigDecimal.ZERO;
        BigDecimal totalST = BigDecimal.ZERO;
        BigDecimal totalPIS = BigDecimal.ZERO;
        BigDecimal totalCOFINS = BigDecimal.ZERO;
        BigDecimal totalIBSUF = BigDecimal.ZERO;
        BigDecimal totalIBSMun = BigDecimal.ZERO;
        BigDecimal totalIBS = BigDecimal.ZERO;
        BigDecimal totalCBS = BigDecimal.ZERO;

        for (ItemVenda item : venda.getItemVendaCollection()) {
            Produto produto = item.getProdutoId();
            BigDecimal valorItem = item.getValorTotal();

            // ICMS
            BigDecimal valorIcmsItem = calcularValorIcmsItem(produto, valorItem);
            totalICMS = totalICMS.add(valorIcmsItem);
            if (produto.getVicmsdeson() != null) {
                totalICMSDeson = totalICMSDeson.add(produto.getVicmsdeson());
            }
            if (produto.getVbcst() != null) {
                totalBCST = totalBCST.add(produto.getVbcst());
            }
            if (produto.getVicmsst() != null) {
                totalST = totalST.add(produto.getVicmsst());
            }

            // PIS
            totalPIS = totalPIS.add(TributacaoCalculator.calcularValorPis(produto, valorItem));

            // COFINS
            totalCOFINS = totalCOFINS.add(TributacaoCalculator.calcularValorCofins(produto, valorItem));

            // RTC / IBS-CBS
            BigDecimal pIbsUf = valorDecimal(produto.getPibsUf(), config.getPibsUfPadrao(), new BigDecimal("0.10"));
            BigDecimal pIbsMun = valorDecimal(produto.getPibsMun(), config.getPibsMunPadrao(), BigDecimal.ZERO);
            BigDecimal pCbs = valorDecimal(produto.getPcbs(), config.getPcbsPadrao(), new BigDecimal("0.90"));

            BigDecimal vIbsUf = TributacaoCalculator.calcularValorIbsUf(produto, valorItem);
            BigDecimal vIbsMun = TributacaoCalculator.calcularValorIbsMun(produto, valorItem);
            BigDecimal vCbs = TributacaoCalculator.calcularValorCbs(produto, valorItem);

            totalIBSUF = totalIBSUF.add(vIbsUf);
            totalIBSMun = totalIBSMun.add(vIbsMun);
            totalIBS = totalIBS.add(vIbsUf.add(vIbsMun));
            totalCBS = totalCBS.add(vCbs);
        }

        icmsTot.put("vBC", String.format(Locale.US, "%.2f", totalVenda));
        icmsTot.put("vICMS", String.format(Locale.US, "%.2f", totalICMS));
        icmsTot.put("vICMSDeson", formatar2(totalICMSDeson));
        icmsTot.put("vFCP", "0.00");
        icmsTot.put("vBCST", formatar2(totalBCST));
        icmsTot.put("vST", formatar2(totalST));
        icmsTot.put("vFCPST", "0.00");
        icmsTot.put("vFCPSTRet", "0.00");
        icmsTot.put("vProd", String.format(Locale.US, "%.2f", totalVenda));
        icmsTot.put("vFrete", formatar2(freteLoja));
        icmsTot.put("vSeg", "0.00");
        icmsTot.put("vDesc", "0.00");
        icmsTot.put("vII", "0.00");
        icmsTot.put("vIPI", "0.00");
        icmsTot.put("vIPIDevol", "0.00");
        icmsTot.put("vPIS", String.format(Locale.US, "%.2f", totalPIS));
        icmsTot.put("vCOFINS", String.format(Locale.US, "%.2f", totalCOFINS));
        icmsTot.put("vOutro", "0.00");
        icmsTot.put("vNF", formatar2(venda.getValorTotal()));

        total.put("ICMSTot", icmsTot);

    JSONObject ibscbsTot = new JSONObject();
    ibscbsTot.put("vBCIBSCBS", formatar2(totalVenda));

    JSONObject gIBS = new JSONObject();
    gIBS.put("gIBSUF", new JSONObject().put("vIBSUF", formatar2(totalIBSUF)));
    gIBS.put("gIBSMun", new JSONObject().put("vIBSMun", formatar2(totalIBSMun)));
    gIBS.put("vIBS", formatar2(totalIBS));
    gIBS.put("vCredPres", "0.00");
    gIBS.put("vCredPresCondSus", "0.00");

    JSONObject gCBS = new JSONObject();
    gCBS.put("vCBS", formatar2(totalCBS));
    gCBS.put("vDif", "0.00");
    gCBS.put("vDevTrib", "0.00");
    gCBS.put("vCredPres", "0.00");
    gCBS.put("vCredPresCondSus", "0.00");

    ibscbsTot.put("gIBS", gIBS);
    ibscbsTot.put("gCBS", gCBS);
    total.put("IBSCBSTot", ibscbsTot);

        infNFe.put("total", total);

        // 7. TRANSPORTE
        JSONObject transp = new JSONObject();
        transp.put("modFrete", venda.isEntregaLoja() ? "0" : "9");
        if (venda.isEntregaLoja()) transp.put("transporta", venda.getTransporteLoja().paraNfe());
        infNFe.put("transp", transp);

        // 8. PAGAMENTO
        JSONObject pag = new JSONObject();
        JSONArray detPagArray = new JSONArray();

        JSONObject detPag = new JSONObject();
        detPag.put("tPag", venda.getPedidoLoja() == null ? valorTexto(config.getTpagPadrao(), "01") : "17");
        detPag.put("vPag", String.format(Locale.US, "%.2f", venda.getValorTotal()));

        detPagArray.put(detPag);
        pag.put("detPag", detPagArray);
        infNFe.put("pag", pag);

        // 9. INFORMAÇÕES ADICIONAIS
        JSONObject infAdic = new JSONObject();

        // Calcular total de tributos (Lei Federal 12.741/2012)
        BigDecimal totalTributos = totalICMS.add(totalPIS).add(totalCOFINS);
        String infCpl = String.format(Locale.US, "Tributos Totais Incidentes (Lei Federal 12.741/2012): R$%.2f", totalTributos);
        
        infAdic.put("infCpl", infCpl);
        infNFe.put("infAdic", infAdic);

        // Montar JSON final
        nfeJson.put("infNFe", infNFe);

        return nfeJson;
    }

    private String valorTexto(String valor, String padrao) {
        if (valor == null || valor.trim().isEmpty()) {
            return padrao;
        }
        return valor.trim();
    }

    private BigDecimal valorDecimal(BigDecimal valorProduto, BigDecimal valorPadrao, BigDecimal fallback) {
        if (valorProduto != null) {
            return valorProduto;
        }
        if (valorPadrao != null) {
            return valorPadrao;
        }
        return fallback;
    }

    private BigDecimal calcularPercentual(BigDecimal base, BigDecimal percentual) {
        return base.multiply(percentual).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    private JSONObject montarIcmsJson(Produto produto, BigDecimal valorItem) {
        String cst = valorTexto(produto.getCstIcms(), "00");
        String orig = valorTexto(produto.getOrig(), "0");
        BigDecimal pIcms = valorDecimal(produto.getPicms(), null, new BigDecimal("7.00"));
        String modBC = valorTexto(produto.getModbcIcms(), "3");

        JSONObject icms = new JSONObject();

        if ("20".equals(cst)) {
            BigDecimal pRedBc = valorDecimal(produto.getPredbcIcms(), null, BigDecimal.ZERO);
            BigDecimal vBcReduzida = valorItem.multiply(BigDecimal.ONE.subtract(pRedBc.divide(new BigDecimal("100"), 6, java.math.RoundingMode.HALF_UP)))
                .setScale(2, java.math.RoundingMode.HALF_UP);
            BigDecimal vIcms = calcularPercentual(vBcReduzida, pIcms);

            JSONObject icms20 = new JSONObject();
            icms20.put("orig", orig);
            icms20.put("CST", cst);
            icms20.put("modBC", modBC);
            icms20.put("pRedBC", formatar2(pRedBc));
            icms20.put("vBC", formatar2(vBcReduzida));
            icms20.put("pICMS", formatar2(pIcms));
            icms20.put("vICMS", formatar2(vIcms));

            BigDecimal vIcmsDeson = valorDecimal(produto.getVicmsdeson(), null, BigDecimal.ZERO);
            if (vIcmsDeson.compareTo(BigDecimal.ZERO) > 0) {
                icms20.put("vICMSDeson", formatar2(vIcmsDeson));
                icms20.put("motDesICMS", valorTexto(produto.getMotDesIcms(), "9"));
            }
            icms.put("ICMS20", icms20);
            return icms;
        }

        if ("40".equals(cst) || "41".equals(cst)) {
            JSONObject icms40 = new JSONObject();
            icms40.put("orig", orig);
            icms40.put("CST", cst);
            BigDecimal vIcmsDeson = valorDecimal(produto.getVicmsdeson(), null, BigDecimal.ZERO);
            if (vIcmsDeson.compareTo(BigDecimal.ZERO) > 0) {
                icms40.put("vICMSDeson", formatar2(vIcmsDeson));
                icms40.put("motDesICMS", valorTexto(produto.getMotDesIcms(), "9"));
            }
            icms.put("ICMS40", icms40);
            return icms;
        }

        if ("60".equals(cst)) {
            JSONObject icms60 = new JSONObject();
            icms60.put("orig", orig);
            icms60.put("CST", cst);
            icms60.put("vBCSTRet", formatar2(valorDecimal(produto.getVbcstret(), null, BigDecimal.ZERO)));
            icms60.put("pST", formatar2(valorDecimal(produto.getPst(), null, BigDecimal.ZERO)));
            icms60.put("vICMSSubstituto", formatar2(valorDecimal(produto.getVicmssubstituto(), null, BigDecimal.ZERO)));
            icms60.put("vICMSSTRet", formatar2(valorDecimal(produto.getVicmsstret(), null, BigDecimal.ZERO)));
            icms.put("ICMS60", icms60);
            return icms;
        }

        JSONObject icms00 = new JSONObject();
        icms00.put("orig", orig);
        icms00.put("CST", "00");
        icms00.put("modBC", modBC);
        icms00.put("vBC", formatar2(valorItem));
        icms00.put("pICMS", formatar2(pIcms));
        icms00.put("vICMS", formatar2(calcularPercentual(valorItem, pIcms)));
        icms.put("ICMS00", icms00);
        return icms;
    }

    private BigDecimal calcularValorIcmsItem(Produto produto, BigDecimal valorItem) {
        String cst = valorTexto(produto.getCstIcms(), "00");
        BigDecimal pIcms = valorDecimal(produto.getPicms(), null, new BigDecimal("7.00"));
        if ("20".equals(cst)) {
            BigDecimal pRedBc = valorDecimal(produto.getPredbcIcms(), null, BigDecimal.ZERO);
            BigDecimal vBcReduzida = valorItem.multiply(BigDecimal.ONE.subtract(pRedBc.divide(new BigDecimal("100"), 6, java.math.RoundingMode.HALF_UP)))
                .setScale(2, java.math.RoundingMode.HALF_UP);
            return calcularPercentual(vBcReduzida, pIcms);
        }
        if ("40".equals(cst) || "41".equals(cst) || "60".equals(cst)) {
            return BigDecimal.ZERO;
        }
        return calcularPercentual(valorItem, pIcms);
    }

    private String formatar2(BigDecimal valor) {
        return String.format(Locale.US, "%.2f", valor);
    }
    
    private String enviarParaServidor(String urlString, String json) throws Exception {
        System.out.println("=== ENVIANDO PARA SERVIDOR NF-e ===");
        System.out.println("URL: " + urlString);

        System.out.println("===================================");

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        System.out.println("Código de resposta HTTP: " + responseCode);

        if (responseCode >= 400) {
            // Ler erro
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                StringBuilder errorResponse = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    errorResponse.append(responseLine.trim());
                }
                throw new RuntimeException("HTTP Error: " + responseCode + " - " + errorResponse.toString());
            }
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            System.out.println("Resposta do servidor:");
            System.out.println("===================================");

            return response.toString();
        }
    }
    
    private String getCodigoUF(String uf) {
        switch (uf.toUpperCase()) {
            case "AC": return "12";
            case "AL": return "27";
            case "AM": return "13";
            case "AP": return "16";
            case "BA": return "29";
            case "CE": return "23";
            case "DF": return "53";
            case "ES": return "32";
            case "GO": return "52";
            case "MA": return "21";
            case "MG": return "31";
            case "MS": return "50";
            case "MT": return "51";
            case "PA": return "15";
            case "PB": return "25";
            case "PE": return "26";
            case "PI": return "22";
            case "PR": return "41";
            case "RJ": return "33";
            case "RN": return "24";
            case "RO": return "11";
            case "RR": return "14";
            case "RS": return "43";
            case "SC": return "42";
            case "SE": return "28";
            case "SP": return "35";
            case "TO": return "17";
            default: return "29";
        }
    }
    
    // *** CORREÇÃO: Getters e Setters simplificados ***
    public List<Venda> getVendas() { return vendas; }
    public void setVendas(List<Venda> vendas) { this.vendas = vendas; }

    public List<Venda> getVendasRecentes() { return vendasRecentes; }
    public void setVendasRecentes(List<Venda> vendasRecentes) { this.vendasRecentes = vendasRecentes; }
    
    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }
    
    public List<Cliente> getClientes() { return clientes; }
    public void setClientes(List<Cliente> clientes) { this.clientes = clientes; }
    
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }

    public String getFiltroStatus() { return filtroStatus; }
    public void setFiltroStatus(String filtroStatus) { this.filtroStatus = filtroStatus; }

    public String getFiltroPeriodo() { return filtroPeriodo; }
    public void setFiltroPeriodo(String filtroPeriodo) { this.filtroPeriodo = filtroPeriodo; }

    public String getFiltroOperacoesRecentes() { return filtroOperacoesRecentes; }
    public void setFiltroOperacoesRecentes(String filtroOperacoesRecentes) { this.filtroOperacoesRecentes = filtroOperacoesRecentes; }

    public String getFiltroRelatorioTipo() { return filtroRelatorioTipo; }
    public void setFiltroRelatorioTipo(String filtroRelatorioTipo) { this.filtroRelatorioTipo = filtroRelatorioTipo; }

    public String getFiltroRelatorioStatus() { return filtroRelatorioStatus; }
    public void setFiltroRelatorioStatus(String filtroRelatorioStatus) { this.filtroRelatorioStatus = filtroRelatorioStatus; }

    public Date getFiltroRelatorioInicio() { return filtroRelatorioInicio; }
    public void setFiltroRelatorioInicio(Date filtroRelatorioInicio) { this.filtroRelatorioInicio = filtroRelatorioInicio; }

    public Date getFiltroRelatorioFim() { return filtroRelatorioFim; }
    public void setFiltroRelatorioFim(Date filtroRelatorioFim) { this.filtroRelatorioFim = filtroRelatorioFim; }

    public List<Venda> getRelatorioVendas() { return relatorioVendas; }
    public void setRelatorioVendas(List<Venda> relatorioVendas) { this.relatorioVendas = relatorioVendas; }

    public BigDecimal getRelatorioValorTotal() { return relatorioValorTotal; }
    public void setRelatorioValorTotal(BigDecimal relatorioValorTotal) { this.relatorioValorTotal = relatorioValorTotal; }

    public BigDecimal getRelatorioTicketMedio() { return relatorioTicketMedio; }
    public void setRelatorioTicketMedio(BigDecimal relatorioTicketMedio) { this.relatorioTicketMedio = relatorioTicketMedio; }

    public long getRelatorioQuantidade() { return relatorioQuantidade; }
    public void setRelatorioQuantidade(long relatorioQuantidade) { this.relatorioQuantidade = relatorioQuantidade; }

    public Date getDataInicio() { return dataInicio; }
    public void setDataInicio(Date dataInicio) { this.dataInicio = dataInicio; }

    public Date getDataFim() { return dataFim; }
    public void setDataFim(Date dataFim) { this.dataFim = dataFim; }

    public long getVendasHoje() { return vendasHoje; }
    public void setVendasHoje(long vendasHoje) { this.vendasHoje = vendasHoje; }

    public long getTotalVendasEmitidas() { return totalVendasEmitidas; }
    public void setTotalVendasEmitidas(long totalVendasEmitidas) { this.totalVendasEmitidas = totalVendasEmitidas; }

    public long getTotalVendasPendentes() { return totalVendasPendentes; }
    public void setTotalVendasPendentes(long totalVendasPendentes) { this.totalVendasPendentes = totalVendasPendentes; }

    public BigDecimal getFaturamentoTotal() { return faturamentoTotal; }
    public void setFaturamentoTotal(BigDecimal faturamentoTotal) { this.faturamentoTotal = faturamentoTotal; }

    public BigDecimal getFaturamentoMes() { return faturamentoMes; }
    public void setFaturamentoMes(BigDecimal faturamentoMes) { this.faturamentoMes = faturamentoMes; }

    public BigDecimal getFaturamentoHoje() { return faturamentoHoje; }
    public void setFaturamentoHoje(BigDecimal faturamentoHoje) { this.faturamentoHoje = faturamentoHoje; }

    public BigDecimal getTicketMedio() { return ticketMedio; }
    public void setTicketMedio(BigDecimal ticketMedio) { this.ticketMedio = ticketMedio; }

    public BigDecimal getVariacaoDia() { return variacaoDia; }
    public void setVariacaoDia(BigDecimal variacaoDia) { this.variacaoDia = variacaoDia; }

    public BarChartModel getDashboardChart() { return dashboardChart; }
    public void setDashboardChart(BarChartModel dashboardChart) { this.dashboardChart = dashboardChart; }

    public PieChartModel getStatusChart() { return statusChart; }
    public void setStatusChart(PieChartModel statusChart) { this.statusChart = statusChart; }
    
    // *** CORREÇÃO: Usar apenas uma propriedade para o cliente ***
    public Cliente getClienteSelecionado() { 
        // Se já temos cliente na venda, retorna ele
        if (venda != null && venda.getClienteId() != null) {
            return venda.getClienteId();
        }
        // Caso contrário, retorna o selecionado temporariamente
        return clienteSelecionado; 
    }
    
    public void setClienteSelecionado(Cliente clienteSelecionado) { 
        this.clienteSelecionado = clienteSelecionado;
        // Atualiza também na venda
        if (venda != null) {
            venda.setClienteId(clienteSelecionado);
        }
    }
    
    public Produto getProdutoSelecionado() { return produtoSelecionado; }
    public void setProdutoSelecionado(Produto produtoSelecionado) { this.produtoSelecionado = produtoSelecionado; }
    
    public ItemVenda getItemVenda() { return itemVenda; }
    public void setItemVenda(ItemVenda itemVenda) { this.itemVenda = itemVenda; }
    
    public boolean isEditando() { return editando; }
    public void setEditando(boolean editando) { this.editando = editando; }
    
    // *** MANTER os métodos auxiliares para compatibilidade com a página ***
    public Cliente getClienteSelecionadoParaVenda() {
        if (venda != null && venda.getClienteId() != null) {
            return venda.getClienteId();
        }
        return null;
    }

    public void setClienteSelecionadoParaVenda(Cliente cliente) {
        if (venda != null) {
            venda.setClienteId(cliente);
            this.clienteSelecionado = cliente;
        }
    }

    public List<ItemVenda> getItensVenda() {
        if (venda != null && venda.getItemVendaCollection() != null) {
            return new ArrayList<>(venda.getItemVendaCollection());
        }
        return new ArrayList<>();
    }

    public String getChavePixFormatada() {
        if (venda != null && venda.getChavePix() != null) {
            return venda.getChavePix();
        }
        return null;
    }
    
    public String getQrCodePixBase64() {
        try {
            if (venda != null && venda.getChavePix() != null && !venda.getChavePix().isEmpty()) {
                byte[] qrCodeBytes = GeradorQRCodePix.gerarQRCodeBytes(venda.getChavePix(), 250);
                String base64 = Base64.getEncoder().encodeToString(qrCodeBytes);
                return "data:image/png;base64," + base64;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback para Google Charts se der erro
            try {
                String chavePix = venda != null ? venda.getChavePix() : "";
                if (chavePix != null && !chavePix.isEmpty()) {
                    String encoded = java.net.URLEncoder.encode(chavePix, "UTF-8");
                    return "https://chart.googleapis.com/chart?cht=qr&chs=250x250&chl=" + encoded + "&chld=L|1";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
    
    public void downloadPDF() {
        if (venda != null && venda.getChaveNfe() != null) {
            try {
                // Armazenar chave na sessão temporariamente
                Map<String, Object> sessionMap = FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap();
                sessionMap.put("pdfDownloadChave", venda.getChaveNfe());

                // Fechar o dialog primeiro
                PrimeFaces.current().executeScript("PF('pdfDialog').hide();");

                // Redirecionar para o servlet de download (fora do AJAX)
                String contextPath = FacesContext.getCurrentInstance()
                    .getExternalContext().getRequestContextPath();
                String downloadUrl = contextPath + "/pdf/download?chave=" + venda.getChaveNfe();

                // Usar redirect não-AJAX
                FacesContext.getCurrentInstance().getExternalContext()
                    .redirect(downloadUrl);

            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro", "Erro ao preparar download: " + e.getMessage()));
                e.printStackTrace();
            }
        }
    }

    public void abrirPDF() {
        if (venda != null && venda.getChaveNfe() != null) {
            try {
                String contextPath = FacesContext.getCurrentInstance()
                    .getExternalContext().getRequestContextPath();
                String pdfUrl = contextPath + "/pdf/view?chave=" + venda.getChaveNfe();

                // Abrir em nova aba
                String script = "window.open('" + pdfUrl + "', '_blank');";
                PrimeFaces.current().executeScript(script);

                // Fechar dialog
                PrimeFaces.current().executeScript("PF('pdfDialog').hide();");

            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro", "Erro ao abrir PDF: " + e.getMessage()));
                e.printStackTrace();
            }
        }
    }
    
    // Método para preparar download (será chamado pelo h:commandLink)
    public String prepararDownload() {
        String chave = FacesContext.getCurrentInstance()
            .getExternalContext().getRequestParameterMap().get("chave");

        if (chave != null && !chave.isEmpty()) {
            // Redirecionar para o servlet de download
            return "/pdf/download?chave=" + chave + "&faces-redirect=true";
        }

        return null;
    }

    // Método para abrir automaticamente após emissão
    private void abrirPDFAposEmissao() {
        PrimeFaces.current().ajax().update("form:growl,form:pdfDialog");

        PrimeFaces.current().executeScript(
            "setTimeout(function() { " +
            "  if (PF('pdfDialog')) { " +
            "    PF('pdfDialog').show(); " +
            "  } " +
            "}, 200);"
        );
    }
}