package controllers;

import models.manejoDigital.AnaliseTecnicaManejo;
import play.data.Upload;
import security.Acao;
import serializers.AnalisesManejoSerializer;
import utils.Mensagem;

import java.io.IOException;

public class AnalisesTecnicaManejo extends InternalController {

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
