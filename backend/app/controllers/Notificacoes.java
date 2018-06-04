package controllers;

import models.AnaliseJuridica;
import models.Documento;
import models.Notificacao;

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
}
