package controllers;

import models.UsuarioAnalise;
import play.mvc.Before;
import play.mvc.Http;
import security.Acao;
import security.Auth;
import utils.JPACallable;
import utils.Mensagem;
import utils.PlayCallable;
import utils.Pool;

import java.util.concurrent.Callable;

public class InternalController extends GenericController {

	@Before
	public static void verificarAutenticacao() {
		
		UsuarioAnalise usuario = Auth.getAuthenticatedUser(session.current());
		
		if (usuario == null)
			unauthorized();
			
	}
	
	protected static UsuarioAnalise getUsuarioSessao() {
		
		return Auth.getAuthenticatedUser(session.current());
	}

	protected static <T> T async(Callable<T> callable) {
		return async(callable, true);
	}

	protected static <T> T async(Callable<T> callable, Boolean jpaReadonly) {
		return await(Pool.global().submit(new PlayCallable<T>(new JPACallable<T>(callable, jpaReadonly))));
	}
	
	protected static void verificarPermissao(Acao ... acoes) {
		
		UsuarioAnalise usuario = getUsuarioSessao();
		
		boolean permitido = false;
		
		for (Acao acao : acoes)
			permitido = permitido || usuario.usuarioEntradaUnica.hasPermissao(acao.codigo);
		
		if (!permitido) {
			
			response.status = Http.StatusCode.FORBIDDEN;
			renderMensagem(Mensagem.PERMISSAO_NEGADA);
		}
	}
}
