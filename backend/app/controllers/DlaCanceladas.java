package controllers;

import models.DlaCancelada;
import models.portalSeguranca.UsuarioLicenciamento;
import security.Acao;
import utils.Mensagem;

public class DlaCanceladas extends InternalController {
	
	public static void cancelarDla (DlaCancelada dlaCancelada){
		
		verificarPermissao(Acao.CANCELAR_LICENCA_EMITIDA);
		
		returnIfNull(dlaCancelada, "DlaCancelada");

		UsuarioLicenciamento usuarioExecutor = getUsuarioSessao();
		
		dlaCancelada.cancelarDla(usuarioExecutor);
		
		renderMensagem(Mensagem.LICENCA_CANCELADA_SUCESSO);
	}

}
