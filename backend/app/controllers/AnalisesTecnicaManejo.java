package controllers;

import models.manejoDigital.analise.analiseTecnica.AnaliseTecnicaManejo;
import models.manejoDigital.PassoAnaliseManejo;
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

	public static void finalizar(AnaliseTecnicaManejo analise) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		returnIfNull(analise, "AnaliseTecnicaManejo");

		AnaliseTecnicaManejo analiseSalva = AnaliseTecnicaManejo.findById(analise.id);

		analiseSalva.finalizar(analise);

		//TODO ENVIAR REQUISIÇÃO DE CONCLUSÃO DA ANÁLISE NO SIMLAM

		renderMensagem(Mensagem.ANALISE_FINALIZADA_SUCESSO);
	}

	public static void updateExibirPdf(PassoAnaliseManejo passo, AnaliseTecnicaManejo novaAnalise) {

		verificarPermissao(Acao.ANALISAR_PROCESSO_MANEJO);

		AnaliseTecnicaManejo analise = AnaliseTecnicaManejo.findById(novaAnalise.id);

		analise.updateExibirPdf(novaAnalise, passo);
	}
}
