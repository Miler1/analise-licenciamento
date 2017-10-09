package controllers;

import models.LicencaCancelada;
import models.Suspensao;
import models.portalSeguranca.Usuario;
import security.Acao;
import security.UsuarioSessao;
import utils.Mensagem;

public class LicencaCanceladas extends InternalController {
	
	public static void cancelarLicenca(LicencaCancelada licencaCancelada) {
		
		verificarPermissao(Acao.CANCELAR_LICENCA_EMITIDA);
		returnIfNull(licencaCancelada, "LicencaCancelada");
    	
		UsuarioSessao usuarioSessao = getUsuarioSessao();
	
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);
		
		licencaCancelada.cancelarLicenca(usuarioExecutor);
		
		renderMensagem(Mensagem.LICENCA_CANCELADA_SUCESSO);
		
	}

}
