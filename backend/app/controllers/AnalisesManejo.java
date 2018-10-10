package controllers;

import models.AnaliseJuridica;
import models.Documento;
import models.manejoDigital.AnaliseManejo;
import play.data.Upload;
import security.Acao;
import serializers.AnalisesManejoSerializer;
import utils.Mensagem;

import java.io.IOException;

public class AnalisesManejo extends InternalController {

	public static void uploadAnexo(Long id, Upload file) throws IOException {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");
		returnIfNull(file, "Upload");

		AnaliseManejo analise = AnaliseManejo.findById(id);
		analise.saveAnexo(file);

		renderMensagem(Mensagem.ANEXO_SALVO_SUCESSO);
	}

	public static void deleteAnexo(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");

		AnaliseManejo analiseManejo = AnaliseManejo.findById(id);
		analiseManejo.deleteAnexo();

		renderMensagem(Mensagem.ANEXO_REMOVIDO_SUCESSO);
	}

	public static void findById(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");

		AnaliseManejo analise = AnaliseManejo.findById(id);

		renderJSON(analise, AnalisesManejoSerializer.findById);
	}

	public static void finalizar(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");

		AnaliseManejo analise = AnaliseManejo.findById(id);

		analise.finalizar();

		//TODO ENVIAR REQUISIÇÃO DE CONCLUSÃO DA ANÁLISE NO SIMLAM

		renderMensagem(Mensagem.ANALISE_FINALIZADA_SUCESSO);
	}

	public static void downloadPDFAnalise(AnaliseManejo analiseManejo) throws Exception {

		verificarPermissao(Acao.VISUALIZAR_PROCESSO_MANEJO);

		Documento pdfParecer = analiseManejo.gerarPDFAnalise();

		String nome = pdfParecer.tipo.nome +  "_" + analiseManejo.id + ".pdf";
		nome = nome.replace(' ', '_');
		response.setHeader("Content-Disposition", "attachment; filename=" + nome);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "application/pdf");

		renderBinary(pdfParecer.arquivo, nome);

	}
}
