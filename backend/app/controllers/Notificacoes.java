package controllers;

import models.AnaliseJuridica;
import models.Documento;
import models.Notificacao;

public class Notificacoes extends InternalController {

    public static void downloadPDFParecer(Notificacao notificacao) throws Exception {

        Notificacao notificacaoSalva = Notificacao.findById(notificacao.id);

        Documento pdfNotificacao = notificacaoSalva.gerarPDFNotificacaoAnaliseJuridica();

        String nome = pdfNotificacao.tipo.nome +  "_" + notificacaoSalva.id + ".pdf";
        nome = nome.replace(' ', '_');
        response.setHeader("Content-Disposition", "attachment; filename=" + nome);
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", "application/pdf");

        renderBinary(pdfNotificacao.arquivo, nome);

    }
}
