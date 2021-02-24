package controllers;

import models.*;
import serializers.ParecerCoordenadorSerializer;
import utils.Mensagem;

import javax.validation.ValidationException;
import java.util.Comparator;

public class PareceresCoordenadores extends InternalController {

	public static void concluirParecerCoordenador(ParecerCoordenadorAnaliseGeo parecerCoordenadorAnaliseGeo) throws Exception {

		returnIfNull(parecerCoordenadorAnaliseGeo, "ParecerCoordenadorAnaliseGeo");

		AnaliseGeo analiseGeo = AnaliseGeo.findById(parecerCoordenadorAnaliseGeo.analiseGeo.id);

		UsuarioAnalise coordenador = getUsuarioSessao();

		parecerCoordenadorAnaliseGeo.finalizar(analiseGeo, coordenador);

		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

	}

	public static void concluirParecerTecnicoCoordenador(ParecerCoordenadorAnaliseTecnica parecerCoordenadorAnaliseTecnica) {

		returnIfNull(parecerCoordenadorAnaliseTecnica, "ParecerCoordenadorAnaliseGeo");

		AnaliseTecnica analiseTecnica = AnaliseTecnica.findById(parecerCoordenadorAnaliseTecnica.analiseTecnica.id);

		UsuarioAnalise coordenador = getUsuarioSessao();

		parecerCoordenadorAnaliseTecnica.finalizar(analiseTecnica, coordenador);

		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

	}

	public static void findParecerByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

		ParecerCoordenadorAnaliseGeo parecerCoordenadorAnaliseGeo = ParecerCoordenadorAnaliseGeo.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

		renderJSON(parecerCoordenadorAnaliseGeo, ParecerCoordenadorSerializer.findByIdHistoricoTramitacao);

	}

	public static void findParecerTecnicoByIdHistoricoTramitacao(Long idHistoricoTramitacao) {

		ParecerCoordenadorAnaliseTecnica parecerCoordenadorAnaliseTecnica = ParecerCoordenadorAnaliseTecnica.find("idHistoricoTramitacao", idHistoricoTramitacao).first();

		renderJSON(parecerCoordenadorAnaliseTecnica, ParecerCoordenadorSerializer.findByIdHistoricoTramitacao);

	}

	public static void findJustificativaParecerByIdAnaliseGeo(Long idAnaliseGeo) {

		AnaliseGeo analiseGeo = AnaliseGeo.findById(idAnaliseGeo);

		renderText(analiseGeo.getJustificativaUltimoParecer());

	}

	public static void findJustificativaParecerByIdAnaliseTecnica(Long idAnaliseTecnica) {

		AnaliseTecnica analiseTecnica = AnaliseTecnica.findById(idAnaliseTecnica);

		renderText(analiseTecnica.getJustificativaUltimoParecer());

	}

	public static void getUltimoParecerCoordenadorAnaliseTecnica(Long id) {

		AnaliseTecnica analiseTecnica = AnaliseTecnica.findById(id);

		renderJSON(analiseTecnica.pareceresCoordenadorAnaliseTecnica.stream().max(Comparator.comparing(ParecerCoordenadorAnaliseTecnica::getDataParecer)).orElseThrow(ValidationException::new), ParecerCoordenadorSerializer.findByIdHistoricoTramitacao);

	}

	public static void getUltimoParecerCoordenadorAnaliseGeo(Long id) {

		AnaliseGeo analiseGeo = AnaliseGeo.findById(id);

		renderJSON(analiseGeo.pareceresCoordenadorAnaliseGeo.stream().max(Comparator.comparing(ParecerCoordenadorAnaliseGeo::getDataParecer)).orElseThrow(ValidationException::new), ParecerCoordenadorSerializer.findByIdHistoricoTramitacao);

	}

}
