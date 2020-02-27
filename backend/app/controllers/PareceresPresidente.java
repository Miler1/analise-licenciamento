package controllers;

import models.Analise;
import models.ParecerDiretorTecnico;
import models.ParecerPresidente;
import models.UsuarioAnalise;
import serializers.ParecerDiretorSerializer;
import serializers.ParecerPresidenteSerializer;
import utils.Mensagem;

public class PareceresPresidente extends InternalController {

	public static void concluirParecerPresidente(ParecerPresidente parecerPresidente) {

		returnIfNull(parecerPresidente, "ParecerPresidente");

		Analise analise = Analise.findById(parecerPresidente.analise.id);

		UsuarioAnalise presidente = getUsuarioSessao();

		parecerPresidente.finalizar(analise, presidente);

		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

	}

	public static void findParecerByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

		ParecerPresidente parecerPresidente = ParecerPresidente.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

		renderJSON(parecerPresidente, ParecerPresidenteSerializer.findByIdHistoricoTramitacao);

	}

}
