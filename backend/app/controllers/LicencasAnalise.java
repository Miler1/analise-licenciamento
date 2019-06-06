package controllers;

import models.LicencaAnalise;
import models.UsuarioAnalise;
import utils.Mensagem;

public class LicencasAnalise extends InternalController {

	public static void emitirLicencaAnalise(LicencaAnalise... licencasAnalise) {

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		LicencaAnalise.emitirLicencas(licencasAnalise, usuarioExecutor);

		renderMensagem(Mensagem.LICENCAS_EMITIDAS_SUCESSO);

	}

}
