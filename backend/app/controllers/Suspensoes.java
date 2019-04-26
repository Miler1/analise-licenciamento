package controllers;

import models.Suspensao;
import models.portalSeguranca.UsuarioLicenciamento;
import security.Acao;
import utils.Mensagem;

public class Suspensoes extends InternalController {

    public static void suspenderLicenca(Suspensao suspensao) {
    	
    	verificarPermissao(Acao.SUSPENDER_LICENCA_EMITIDA);
		
    	returnIfNull(suspensao, "Suspensao");

	    UsuarioLicenciamento usuarioExecutor = getUsuarioSessao();
    	
    	suspensao.suspenderLicenca(usuarioExecutor);
    	
    	renderMensagem(Mensagem.LICENCA_SUSPENSA_SUCESSO);
        
    }

}
