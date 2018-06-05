package controllers;

import models.AnaliseJuridica;
import models.Documento;
import models.Notificacao;
import org.apache.commons.collections.FastHashMap;
import play.libs.Crypto;
import utils.Configuracoes;

import java.io.UnsupportedEncodingException;
import java.util.Map;

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


    public static void qrcodeView(String idNotificacao) throws UnsupportedEncodingException {

        Notificacao notificacao = Notificacao.findById(Crypto.decryptAES(idNotificacao));

        String url = Configuracoes.APP_URL + "/licenca/" + Crypto.encryptAES(notificacao.id.toString()) + "/download";
        String nomeArquivo = notificacao.tipoDocumento + "_" + notificacao.id + ".pdf";

        Map<String, Object> args = new FastHashMap(3);
        args.put("notificacao", notificacao);
        args.put("urlDownload", url);
        args.put("nomeArquivo", nomeArquivo);

        renderTemplate(Configuracoes.PDF_TEMPLATES_FOLDER_PATH + "/qrcode/informacoes.html", args);

    }
}
