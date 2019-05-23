package controllers;

import models.DlaCancelada;
import models.UsuarioAnalise;
import security.Acao;
import utils.Mensagem;

public class DlaCanceladas extends InternalController {
	
	public static void cancelarDla (DlaCancelada dlaCancelada){
		
		verificarPermissao(Acao.CANCELAR_LICENCA_EMITIDA);
		
		returnIfNull(dlaCancelada, "DlaCancelada");

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();
		
		dlaCancelada.cancelarDla(usuarioExecutor);
		
		renderMensagem(Mensagem.LICENCA_CANCELADA_SUCESSO);
	}

}
