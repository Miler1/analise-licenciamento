package controllers;

import models.*;
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

	public static void concluirParecerTecnicoGerente(ParecerGerenteAnaliseTecnica parecerGerenteAnaliseTecnica) {

		returnIfNull(parecerGerenteAnaliseTecnica, "ParecerGerenteAnaliseGeo");

		AnaliseTecnica analiseTecnica = AnaliseTecnica.findById(parecerGerenteAnaliseTecnica.analiseTecnica.id);

		UsuarioAnalise gerente = getUsuarioSessao();

		parecerGerenteAnaliseTecnica.finalizar(analiseTecnica, gerente);

		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

	}

	public static void findParecerByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

		ParecerGerenteAnaliseGeo parecerGerenteAnaliseGeo = ParecerGerenteAnaliseGeo.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

		renderJSON(parecerGerenteAnaliseGeo, ParecerGerenteSerializer.findByIdHistoricoTramitacao);

	}

	public static void findParecerTecnicoByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

		ParecerGerenteAnaliseTecnica parecerGerenteAnaliseTecnica = ParecerGerenteAnaliseTecnica.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

		renderJSON(parecerGerenteAnaliseTecnica, ParecerGerenteSerializer.findByIdHistoricoTramitacao);

	}

	public static void findJustificativaParecerByIdAnaliseGeo(Long idAnaliseGeo) {

		AnaliseGeo analiseGeo = AnaliseGeo.findById(idAnaliseGeo);

		renderText(analiseGeo.getJustificativaUltimoParecer());

	}

	public static void findJustificativaParecerByIdAnaliseTecnica(Long idAnaliseTecnica) {

		AnaliseTecnica analiseTecnica = AnaliseTecnica.findById(idAnaliseTecnica);

		renderText(analiseTecnica.getJustificativaUltimoParecer());

	}

}
