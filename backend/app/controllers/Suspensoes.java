package controllers;

import models.Suspensao;
import models.SuspensaoDispensa;
import models.UsuarioAnalise;
import security.Acao;
import utils.Mensagem;

public class Suspensoes extends InternalController {

    public static void suspenderLicenca(Suspensao suspensao) {
    	
    	verificarPermissao(Acao.SUSPENDER_LICENCA_EMITIDA);
		
    	returnIfNull(suspensao, "Suspensao");

	    UsuarioAnalise usuarioExecutor = getUsuarioSessao();
    	
    	suspensao.suspenderLicenca(usuarioExecutor);
    	
    	renderMensagem(Mensagem.LICENCA_SUSPENSA_SUCESSO);
        
    }

	public static void suspenderDispensa(SuspensaoDispensa suspensaoDispensa) {

		verificarPermissao(Acao.SUSPENDER_LICENCA_EMITIDA);

		returnIfNull(suspensaoDispensa, "Suspensao");

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		suspensaoDispensa.suspenderDispensa(usuarioExecutor);

		renderMensagem(Mensagem.LICENCA_SUSPENSA_SUCESSO);

	}

}
