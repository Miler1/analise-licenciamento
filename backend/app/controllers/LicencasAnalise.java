package controllers;

import models.LicencaAnalise;
import models.portalSeguranca.UsuarioLicenciamento;
import utils.Mensagem;

public class LicencasAnalise extends InternalController {

	public static void emitirLicencaAnalise(LicencaAnalise... licencasAnalise) {

		UsuarioLicenciamento usuarioSessao = getUsuarioSessao();
		UsuarioLicenciamento usuarioExecutor = UsuarioLicenciamento.findById(usuarioSessao.id, usuarioSessao.perfilSelecionado, usuarioSessao.setorSelecionado);

		LicencaAnalise.emitirLicencas(licencasAnalise, usuarioExecutor);

		renderMensagem(Mensagem.LICENCAS_EMITIDAS_SUCESSO);

	}

}
