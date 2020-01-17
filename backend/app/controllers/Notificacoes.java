package controllers;

import models.Documento;
import models.Notificacao;
import models.Processo;
import security.Acao;
import serializers.NotificacaoSerializer;

import java.util.*;
import java.util.stream.Collectors;

public class Notificacoes extends InternalController {

    public static void downloadPDF(Integer idTramitacao) throws Exception {

        Notificacao notificacao = Notificacao.find("id_historico_tramitacao", idTramitacao).first();

        Documento pdfNotificacao = notificacao.gerarPDF();

        String nome = pdfNotificacao.tipo.nome +  "_" + notificacao.id + ".pdf";
        nome = nome.replace(' ', '_');
        response.setHeader("Content-Disposition", "attachment; filename=" + nome);
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", "application/pdf");

        renderBinary(pdfNotificacao.arquivo, nome);
    }

    public static void findByIdProcesso(Long id) {

        verificarPermissao(Acao.VISUALIZAR_NOTIFICACAO);

        Processo processo = Processo.findById(id);

        List<Notificacao> notificacoes = processo.analise.getAnaliseGeo().notificacoes;

        for (Notificacao notificacao:notificacoes) {

            notificacao.setJustificativa();
            notificacao.setDocumentosParecer(notificacao.parecerAnalistaGeo.documentos);
        }

        renderJSON(notificacoes.stream().sorted(Comparator.comparing(Notificacao::getDataNotificacao).reversed()).collect(Collectors.toList()), NotificacaoSerializer.findAll);
    }

    public static void findByIdProcessoTecnico(Long id) {

        verificarPermissao(Acao.VISUALIZAR_NOTIFICACAO);

        Processo processo = Processo.findById(id);

        List<Notificacao> notificacoes = processo.analise.getAnaliseTecnica().notificacoes;

        for (Notificacao notificacao:notificacoes) {

            notificacao.setJustificativaTecnica();
            notificacao.setDocumentosParecerTecnico(notificacao.parecerAnalistaTecnico.documentos);
        }

        renderJSON(notificacoes.stream().sorted(Comparator.comparing(Notificacao::getDataNotificacao).reversed()).collect(Collectors.toList()), NotificacaoSerializer.findAll);
    }

}
