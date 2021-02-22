package controllers;

import models.Analise;
import security.Acao;
import serializers.AnalisesSerializer;
import utils.Mensagem;

public class Analises extends InternalController {
	
	public static void findById(Long id) {
		
		renderJSON(Analise.findById(id), AnalisesSerializer.findInfo);
	}

	public static void iniciar(Long idAnalise) {

		verificarPermissao(Acao.INICIAR_PARECER_DIRETOR);

		Analise analiseAAlterar = Analise.findById(idAnalise);

		analiseAAlterar.iniciar(getUsuarioSessao());

		renderMensagem(Mensagem.ANALISE_DIRETOR_INICIADA_SUCESSO);

	}

	public static void iniciarAnaliseSecretario(Long idAnalise) {

		verificarPermissao(Acao.INICIAR_PARECER_SECRETARIO);

		Analise analiseAAlterar = Analise.findById(idAnalise);

		analiseAAlterar.iniciarAnaliseSecretario(getUsuarioSessao());

		renderMensagem(Mensagem.ANALISE_SECRETARIO_INICIADA_SUCESSO);

	}

}