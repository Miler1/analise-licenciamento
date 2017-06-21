package controllers;

import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.portalSeguranca.Usuario;
import security.UsuarioSessao;
import utils.Mensagem;

public class AnalisesTecnicas extends InternalController {
		
	public static void iniciar(AnaliseTecnica analise) {
		
		AnaliseTecnica analiseAAlterar = AnaliseTecnica.findById(analise.id);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);		
				
		analiseAAlterar.iniciar(usuarioExecutor);
				
		renderMensagem(Mensagem.ANALISE_TECNICA_INICIADA_SUCESSO);	

	}
}