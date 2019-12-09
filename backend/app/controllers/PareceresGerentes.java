package controllers;

import models.Analise;
import models.AnaliseGeo;
import models.ParecerGerenteAnaliseGeo;
import models.UsuarioAnalise;
import serializers.ParecerGerenteSerializer;
import utils.Mensagem;

public class PareceresGerentes extends InternalController {

	public static void concluirParecerGerente(ParecerGerenteAnaliseGeo parecerGerenteAnaliseGeo) {

		returnIfNull(parecerGerenteAnaliseGeo, "ParecerGerenteAnaliseGeo");

		AnaliseGeo analiseGeo = AnaliseGeo.findById(parecerGerenteAnaliseGeo.analiseGeo.id);

		UsuarioAnalise gerente = getUsuarioSessao();

		parecerGerenteAnaliseGeo.finalizar(analiseGeo, gerente);

		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

	}

	public static void findParecerByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

		ParecerGerenteAnaliseGeo parecerAnalistaGeo = ParecerGerenteAnaliseGeo.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

		renderJSON(parecerAnalistaGeo, ParecerGerenteSerializer.findByIdHistoricoTramitacao);

	}

	public static void findJustificativaParecerByIdAnaliseGeo(Long idAnaliseGeo) {

		AnaliseGeo analiseGeo = AnaliseGeo.findById(idAnaliseGeo);

		renderText(analiseGeo.getJustificativaUltimoParecer());

	}

}
