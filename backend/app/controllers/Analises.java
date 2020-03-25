package controllers;

import models.Analise;
import models.AnaliseTecnica;
import models.UsuarioAnalise;
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

	public static void iniciarAnalisePresidente(Long idAnalise) {

		verificarPermissao(Acao.INICIAR_PARECER_PRESIDENTE);

		Analise analiseAAlterar = Analise.findById(idAnalise);

		analiseAAlterar.iniciarAnalisePresidente(getUsuarioSessao());

		renderMensagem(Mensagem.ANALISE_PRESIDENTE_INICIADA_SUCESSO);

	}

}
