package controllers;

import models.manejoDigital.AnaliseTecnicaManejo;
import play.data.Upload;
import security.Acao;
import serializers.AnalisesManejoSerializer;
import utils.Mensagem;

import java.io.IOException;

public class AnalisesTecnicaManejo extends InternalController {

	public static void uploadAnexo(Long id, Upload file) throws IOException {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");
		returnIfNull(file, "Upload");

		AnaliseTecnicaManejo analise = AnaliseTecnicaManejo.findById(id);
		analise.saveAnexo(file);

		renderMensagem(Mensagem.ANEXO_SALVO_SUCESSO);
	}

	public static void deleteAnexo(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");

		AnaliseTecnicaManejo analiseTecnicaManejo = AnaliseTecnicaManejo.findById(id);
		analiseTecnicaManejo.deleteAnexo();

		renderMensagem(Mensagem.ANEXO_REMOVIDO_SUCESSO);
	}

	public static void findById(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");

		AnaliseTecnicaManejo analise = AnaliseTecnicaManejo.findById(id);

		renderJSON(analise, AnalisesManejoSerializer.findById);
	}

	public static void finalizar(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");

		AnaliseTecnicaManejo analise = AnaliseTecnicaManejo.findById(id);

		analise.finalizar();

		//TODO ENVIAR REQUISIÇÃO DE CONCLUSÃO DA ANÁLISE NO SIMLAM

		renderMensagem(Mensagem.ANALISE_FINALIZADA_SUCESSO);
	}
}
