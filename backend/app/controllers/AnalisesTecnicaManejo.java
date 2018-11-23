package controllers;

import models.manejoDigital.AnaliseTecnicaManejo;
import security.Acao;
import serializers.AnalisesTecnicaManejoSerializer;
import utils.Mensagem;

public class AnalisesTecnicaManejo extends InternalController {

	public static void findById(Long id) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(id, "Long");

		AnaliseTecnicaManejo analise = AnaliseTecnicaManejo.findById(id);

		renderJSON(analise, AnalisesTecnicaManejoSerializer.findById);
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
