package controllers;

import models.AnaliseGeo;
import models.ParecerGerenteAnaliseGeo;
import models.UsuarioAnalise;
import utils.Mensagem;

public class PareceresGerentes extends InternalController {

		public static void concluirParecerGerente(ParecerGerenteAnaliseGeo parecerGerenteAnaliseGeo) {

		returnIfNull(parecerGerenteAnaliseGeo, "ParecerGerenteAnaliseGeo");

		AnaliseGeo analiseGeo = AnaliseGeo.findById(parecerGerenteAnaliseGeo.analiseGeo.id);

		UsuarioAnalise gerente = getUsuarioSessao();

		parecerGerenteAnaliseGeo.finalizar(analiseGeo, gerente);

		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

	}

}
