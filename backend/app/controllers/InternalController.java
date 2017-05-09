package controllers;

import play.mvc.Before;
import play.mvc.Http;
import security.Acao;
import security.Auth;
import security.UsuarioSessao;
import utils.Mensagem;

public class InternalController extends GenericController {

	@Before
	public static void verificarAutenticacao() {
		
		UsuarioSessao usuario = Auth.getUsuarioSessao(session.current());
		
		if (usuario == null)
			unauthorized();
			
	}
	
	protected static UsuarioSessao getUsuarioSessao() {
		
		return Auth.getUsuarioSessao(session.current());
	}
	
	protected static void verificarPermissao(Acao ... acoes) {
		
		UsuarioSessao usuario = getUsuarioSessao();
		
		boolean permitido = false;
		
		for (Acao acao : acoes)
			permitido = permitido || usuario.possuiPermissao(acao);
		
		if (!permitido) {
			
			response.status = Http.StatusCode.FORBIDDEN;
			renderMensagem(Mensagem.PERMISSAO_NEGADA);
		}
	}
}
