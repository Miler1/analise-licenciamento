package controllers;

import models.LicencaCancelada;
import models.portalSeguranca.UsuarioLicenciamento;
import security.Acao;
import utils.Mensagem;

public class LicencaCanceladas extends InternalController {
	
	public static void cancelarLicenca(LicencaCancelada licencaCancelada) {
		
		verificarPermissao(Acao.CANCELAR_LICENCA_EMITIDA);
		
		returnIfNull(licencaCancelada, "LicencaCancelada");
    	
		UsuarioLicenciamento usuarioSessao = getUsuarioSessao();

		UsuarioLicenciamento usuarioExecutor = UsuarioLicenciamento.findById(usuarioSessao.id, usuarioSessao.perfilSelecionado, usuarioSessao.setorSelecionado);
		
		licencaCancelada.cancelarLicenca(usuarioExecutor);
		
		renderMensagem(Mensagem.LICENCA_CANCELADA_SUCESSO);
		
	}

}
