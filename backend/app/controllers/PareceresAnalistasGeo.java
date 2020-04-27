package controllers;

import models.*;
import security.Acao;
import serializers.ParecerAnalistaGeoSerializer;
import serializers.ParecerAnalistaTecnicoSerializer;
import serializers.ParecerGerenteSerializer;
import utils.Mensagem;

import javax.validation.ValidationException;
import java.util.Comparator;
import java.util.List;

public class PareceresAnalistasGeo extends InternalController {

	public static void concluir(ParecerAnalistaGeo parecerAnalistaGeo) throws Exception {

		verificarPermissao(Acao.INICIAR_PARECER_GEO);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		parecerAnalistaGeo.finalizar(usuarioExecutor);

		renderMensagem(Mensagem.ANALISE_FINALIZADA_SUCESSO);

	}

	public static void findParecerByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

		ParecerAnalistaGeo parecerAnalistaGeo = ParecerAnalistaGeo.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

		renderJSON(parecerAnalistaGeo, ParecerAnalistaGeoSerializer.findByIdHistoricoTramitacao);

	}

	public static void findByNumeroProcesso() {

		String numeroProcesso = getParamAsString("numeroProcesso");

		AnaliseGeo analiseGeo = AnaliseGeo.find("analise.processo.numero = :numeroProcesso ORDER BY id DESC")
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

	public static void findParecerByIdAnaliseGeo(Long idAnaliseGeo) {

		List<ParecerAnalistaGeo> pareceres = ParecerAnalistaGeo.find("id_analise_geo = :analiseGeo")
				.setParameter("analiseGeo", idAnaliseGeo).fetch();

		ParecerAnalistaGeo parecerFinal = pareceres.stream().max( Comparator.comparing(parecerAnalistaGeo -> parecerAnalistaGeo.id )).get();

		renderJSON(parecerFinal, ParecerAnalistaGeoSerializer.findByIdAnaliseGeo);

	}

	public static void getUltimoParecerAnaliseGeo(Long id) {

		AnaliseGeo analiseGeo = AnaliseGeo.findById(id);

		renderJSON(analiseGeo.pareceresAnalistaGeo.stream().max(Comparator.comparing(ParecerAnalistaGeo::getDataParecer)).orElseThrow(ValidationException::new), ParecerAnalistaGeoSerializer.findByIdHistoricoTramitacao);

	}

}
