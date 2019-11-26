package controllers;

import models.ParecerAnalistaGeo;
import models.UsuarioAnalise;
import security.Acao;
import serializers.ParecerAnalistaGeoSerializer;
import utils.Mensagem;

public class PareceresAnalistasGeo extends InternalController {

	public static void concluir(ParecerAnalistaGeo parecerAnalistaGeo) throws Exception {

		verificarPermissao(Acao.INICIAR_PARECER_GEO);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		parecerAnalistaGeo.finalizar(usuarioExecutor);

		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

	}

	public static void findParecerByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

		ParecerAnalistaGeo parecerAnalistaGeo = ParecerAnalistaGeo.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

		renderJSON(parecerAnalistaGeo, ParecerAnalistaGeoSerializer.findByIdHistoricoTramitacao);

	}

}
