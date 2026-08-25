package com.souzamonteiro.nfeemissor;

import br.com.swconsultoria.nfe.ConsultaTributacao;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class AtualizarTabelaRtc {

    public static void main(String[] args) throws Exception {
        String baseDir = System.getProperty("user.dir");
        ConfiguracoesNfe config = Config.iniciaConfiguracoes();

        String jsonTabela = ConsultaTributacao.getJson(config);

        Path pasta = Path.of(baseDir, "tabelas");
        Files.createDirectories(pasta);

        Path arquivo = pasta.resolve("consulta-tributacao-rtc.json");
        Files.writeString(arquivo, jsonTabela, StandardCharsets.UTF_8);

        Path meta = pasta.resolve("consulta-tributacao-rtc.meta.txt");
        String metadata = "AtualizadoEm=" + LocalDateTime.now() + System.lineSeparator()
                + "UF=" + config.getEstado().name() + System.lineSeparator()
                + "Ambiente=" + config.getAmbiente().name() + System.lineSeparator()
                + "Origem=ConsultaTributacao.getJson(java-nfe)" + System.lineSeparator();
        Files.writeString(meta, metadata, StandardCharsets.UTF_8);

        System.out.println("Tabela RTC atualizada em: " + arquivo);
        System.out.println("Metadata: " + meta);
    }
}
