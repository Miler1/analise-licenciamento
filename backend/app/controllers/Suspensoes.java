package controllers;

import models.Suspensao;
import models.portalSeguranca.Usuario;
import security.Acao;
import security.UsuarioSessao;
import utils.Mensagem;

public class Suspensoes extends InternalController {

    public static void suspenderLicenca(Suspensao suspensao) {
    	
    	verificarPermissao(Acao.SUSPENDER_LICENCA_EMITIDA);
		
    	returnIfNull(suspensao, "Suspensao");
    	
		UsuarioSessao usuarioSessao = getUsuarioSessao();
	
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);
    	
    	suspensao.suspenderLicenca(usuarioExecutor);
    	
    	renderMensagem(Mensagem.LICENCA_SUSPENSA_SUCESSO);
        
    }

}
