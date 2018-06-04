package controllers;

import models.AnaliseJuridica;
import models.Documento;
import models.Notificacao;

public class Notificacoes extends InternalController {

    public static void downloadPDF(Integer idTramitacao) throws Exception {

        Notificacao notificacao = Notificacao.find("historicoTramitacao.id", idTramitacao).first();

        Documento pdfNotificacoes = notificacao.gerarPDF();

        String nome = pdfNotificacoes.tipo.nome +  "_" + notificacao.id + ".pdf";
        nome = nome.replace(' ', '_');
        response.setHeader("Content-Disposition", "attachment; filename=" + nome);
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", "application/pdf");

        renderBinary(pdfNotificacoes.arquivo, nome);

        if(notificacao.analiseJuridica == null) {

            AnaliseJuridica analiseJuridica = notificacao.analiseJuridica;

            Documento pdfAnalise = analiseJuridica.gerarPDFAnalise

            String nome = pdfNotificacao.tipo.nome +  "_" + notificacaoSalva.id + ".pdf";
            nome = nome.replace(' ', '_');
            response.setHeader("Content-Disposition", "attachment; filename=" + nome);
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Type", "application/pdf");

            renderBinary(pdfNotificacao.arquivo, nome);

        } else {

            Documento pdfNotificacao = notificacaoSalva.gerarPDFNotificacaoAnaliseJuridica();

            String nome = pdfNotificacao.tipo.nome +  "_" + notificacaoSalva.id + ".pdf";
            nome = nome.replace(' ', '_');
            response.setHeader("Content-Disposition", "attachment; filename=" + nome);
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Type", "application/pdf");

            renderBinary(pdfNotificacao.arquivo, nome);
        }


    }
}
