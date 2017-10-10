package controllers;

import models.DlaCancelada;
import models.portalSeguranca.Usuario;
import security.Acao;
import security.UsuarioSessao;
import utils.Mensagem;

public class DlaCanceladas extends InternalController {
	
	public static void cancelarDla (DlaCancelada dlaCancelada){
		
		verificarPermissao(Acao.CANCELAR_LICENCA_EMITIDA);
		
		returnIfNull(dlaCancelada, "DlaCancelada");
    	
		UsuarioSessao usuarioSessao = getUsuarioSessao();
	
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);
		
		dlaCancelada.cancelarDla(usuarioExecutor);
		
		renderMensagem(Mensagem.LICENCA_CANCELADA_SUCESSO);
	}

}
