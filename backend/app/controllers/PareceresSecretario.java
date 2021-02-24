package controllers;

import models.Analise;
import models.ParecerSecretario;
import models.UsuarioAnalise;
import serializers.ParecerSecretarioSerializer;
import utils.Mensagem;

public class PareceresSecretario extends InternalController {

	public static void concluirParecerSecretario(ParecerSecretario parecerSecretario) throws Exception {

		returnIfNull(parecerSecretario, "ParecerSecretario");

		Analise analise = Analise.findById(parecerSecretario.analise.id);

		UsuarioAnalise secretario = getUsuarioSessao();

		parecerSecretario.finalizar(analise, secretario);

		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

	}

	public static void findParecerByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

		ParecerSecretario parecerSecretario = ParecerSecretario.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

		renderJSON(parecerSecretario, ParecerSecretarioSerializer.findByIdHistoricoTramitacao);

	}

}
