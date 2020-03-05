package controllers;

import models.Analise;
import models.ParecerPresidente;
import models.UsuarioAnalise;
import utils.Mensagem;

public class PareceresPresidente extends InternalController {

	public static void concluirParecerPresidente(ParecerPresidente parecerPresidente) throws Exception {

		returnIfNull(parecerPresidente, "ParecerPresidente");

		Analise analise = Analise.findById(parecerPresidente.analise.id);

		UsuarioAnalise presidente = getUsuarioSessao();

		parecerPresidente.finalizar(analise, presidente);

		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);

	}

}
