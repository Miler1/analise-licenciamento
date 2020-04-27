package controllers;

import models.Documento;
import models.Notificacao;
import models.Processo;
import security.Acao;
import serializers.NotificacaoSerializer;

import java.util.ArrayList;
import java.util.List;

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

        List<Notificacao> notificacoes = new ArrayList<>();

        while(processo.processoAnterior != null) {

            processo = Processo.findById(processo.processoAnterior.id);

            notificacoes.addAll(processo.inicializaNotificacoes());
        }

        renderJSON(notificacoes, NotificacaoSerializer.findAll);
    }

    public static void findByIdParecer(Long id) {

        verificarPermissao(Acao.VISUALIZAR_NOTIFICACAO);

        List<Notificacao> notificacoes = Notificacao.findByIdParecer(id);

        renderJSON(notificacoes, NotificacaoSerializer.findAll);
    }

    public static void findByIdParecerTecnico(Long id) {

        verificarPermissao(Acao.VISUALIZAR_NOTIFICACAO);

        List<Notificacao> notificacoes = Notificacao.findByIdParecerTecnico(id);

        renderJSON(notificacoes, NotificacaoSerializer.findAll);

    }

}
