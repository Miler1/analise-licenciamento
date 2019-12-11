package controllers;

import models.AnaliseGeo;
import models.ParecerAnalistaGeo;
import models.Processo;
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

	public static void findByNumeroProcesso() {

		String numeroProcesso = getParamAsString("numeroProcesso");

		AnaliseGeo analiseGeo = AnaliseGeo.find("analise.processo.numero = :numeroProcesso AND ativo = true")
				.setParameter("numeroProcesso", numeroProcesso)
				.first();

		if(analiseGeo == null || analiseGeo.pareceresAnalistaGeo == null ||  analiseGeo.pareceresAnalistaGeo.isEmpty()) {

			renderMensagem(Mensagem.PARECER_NAO_ENCONTRADO);

		} else if(!analiseGeo.inconsistencias.isEmpty()) {

			renderMensagem(Mensagem.CLONAR_PARECER_COM_INCONSISTENCIA);

		} else {

			renderJSON(ParecerAnalistaGeo.getUltimoParecer(analiseGeo.pareceresAnalistaGeo), ParecerAnalistaGeoSerializer.findByIdNumeroProcesso);

		}

	}

	public static void findParecerByIdProcesso(Long idProcesso) {

		Processo processo = Processo.findById(idProcesso);

		renderJSON(ParecerAnalistaGeo.findParecerByProcesso(processo), ParecerAnalistaGeoSerializer.findByIdProcesso);

	}

}
