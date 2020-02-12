package controllers;

import models.AnaliseTecnica;
import models.ParecerAnalistaTecnico;
import models.UsuarioAnalise;
import security.Acao;
import serializers.ParecerAnalistaTecnicoSerializer;
import utils.Mensagem;

public class PareceresAnalistasTecnico extends InternalController {

	public static void concluir(ParecerAnalistaTecnico parecerAnalistaTecnico) throws Exception {

		verificarPermissao(Acao.INICIAR_PARECER_TECNICO);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		parecerAnalistaTecnico.finalizar(usuarioExecutor);

		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

	}

	public static void findParecerByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

		ParecerAnalistaTecnico parecerAnalistaTecnico = ParecerAnalistaTecnico.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

		renderJSON(parecerAnalistaTecnico, ParecerAnalistaTecnicoSerializer.findByIdHistoricoTramitacao);

	}

	public static void findByNumeroProcesso() {

		String numeroProcesso = getParamAsString("numeroProcesso");

		AnaliseTecnica analiseTecnica = AnaliseTecnica.find("analise.processo.numero = :numeroProcesso ORDER BY id DESC")
				.setParameter("numeroProcesso", numeroProcesso)
				.first();

		if(analiseTecnica == null || analiseTecnica.pareceresAnalistaTecnico == null ||  analiseTecnica.pareceresAnalistaTecnico.isEmpty()) {

			renderMensagem(Mensagem.PARECER_NAO_ENCONTRADO);

		} else if(!analiseTecnica.inconsistenciasTecnica.isEmpty()) {

			renderMensagem(Mensagem.CLONAR_PARECER_COM_INCONSISTENCIA);

		} else {

			renderJSON(ParecerAnalistaTecnico.getUltimoParecer(analiseTecnica.pareceresAnalistaTecnico), ParecerAnalistaTecnicoSerializer.findByIdNumeroProcesso);

		}

	}

}
