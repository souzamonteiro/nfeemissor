package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.GenericDAO;
import com.souzamonteiro.nfe.model.DadosTransportadora;
import com.souzamonteiro.nfe.model.ServicoEntrega;
import com.souzamonteiro.nfe.model.Transportadora;
import com.souzamonteiro.nfe.model.Usuario;
import com.souzamonteiro.nfe.service.LojaService;
import java.io.Serializable;
import java.math.RoundingMode;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class EntregaController implements Serializable {
    private final GenericDAO<Transportadora> transportadoras = new GenericDAO<>(Transportadora.class);
    private final GenericDAO<ServicoEntrega> servicos = new GenericDAO<>(ServicoEntrega.class);
    private Transportadora transportadora = new Transportadora();
    private ServicoEntrega servico = new ServicoEntrega();
    private Integer transportadoraId;

    public Transportadora getTransportadora() { return transportadora; }
    public ServicoEntrega getServico() { return servico; }
    public Integer getTransportadoraId() { return transportadoraId; }
    public void setTransportadoraId(Integer transportadoraId) { this.transportadoraId = transportadoraId; }
    public List<Transportadora> getTransportadoras() { exigirEquipe(); return transportadoras.findAll(); }
    public List<ServicoEntrega> getServicos() { exigirEquipe(); return servicos.findAll(); }

    public void editarTransportadora(Integer id) {
        exigirEquipe();
        transportadora = transportadoras.findById(id);
    }

    public void editarServico(Integer id) {
        exigirEquipe();
        servico = servicos.findById(id);
        transportadoraId = servico.getTransportadora().getId();
    }

    public void novaTransportadora() { transportadora = new Transportadora(); }
    public void novoServico() { servico = new ServicoEntrega(); transportadoraId = null; }

    public void salvarTransportadora() {
        executar(() -> {
            DadosTransportadora dados = transportadora.getDados();
            String documento = dados.getDocumento().replaceAll("[. /-]", "");
            if (!documento.matches("[0-9]{11}|[0-9]{14}")) {
                throw new IllegalArgumentException("Informe o CPF ou CNPJ da transportadora.");
            }
            dados.setDocumento(documento);
            dados.setUf(dados.getUf().trim().toUpperCase(java.util.Locale.ROOT));
            if (!dados.getUf().matches("AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO")) {
                throw new IllegalArgumentException("Informe uma UF brasileira válida.");
            }
            String ie = dados.getInscricaoEstadual();
            if (ie != null && !ie.isEmpty() && !ie.matches("[0-9]{2,14}|ISENTO")) {
                throw new IllegalArgumentException("Informe a inscrição estadual sem pontuação ou ISENTO.");
            }
            transportadoras.save(transportadora);
            novaTransportadora();
        }, "Transportadora salva.");
    }

    public void salvarServico() {
        executar(() -> {
            Transportadora selecionada = transportadoras.findById(transportadoraId);
            if (selecionada == null) throw new IllegalArgumentException("Escolha uma transportadora.");
            String inicio = servico.getCepInicio().replace("-", "");
            String fim = servico.getCepFim().replace("-", "");
            if (!inicio.matches("[0-9]{8}") || !fim.matches("[0-9]{8}") || inicio.compareTo(fim) > 0) {
                throw new IllegalArgumentException("Confira os CEPs inicial e final da faixa.");
            }
            servico.setCepInicio(inicio);
            servico.setCepFim(fim);
            if (servico.getValor().signum() < 0 || servico.getPrazoDias() < 1) {
                throw new IllegalArgumentException("Informe um valor não negativo e um prazo de pelo menos um dia útil.");
            }
            servico.setValor(servico.getValor().setScale(2, RoundingMode.HALF_UP));
            servico.setTransportadora(selecionada);
            servicos.save(servico);
            novoServico();
        }, "Serviço de entrega salvo.");
    }

    private void executar(Runnable acao, String mensagem) {
        exigirEquipe();
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            acao.run();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, null));
        } catch (IllegalArgumentException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    private static void exigirEquipe() {
        Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
        if (!LojaService.equipe(usuario)) throw new SecurityException();
    }
}
