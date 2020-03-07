package security;

import models.EntradaUnica.Usuario;
import models.UsuarioAnalise;
import play.Logger;
import play.cache.Cache;
import play.mvc.Scope.Session;
import security.cadastrounificado.CadastroUnificadoWS;

public class Auth implements AuthService {

	private static final String CACHE_PREFIX = "LIC_USER_";

	public static void logout(Session session) {

		String key = CACHE_PREFIX +  session.getId();

		if (Cache.get(key) != null)
			Cache.delete(key);

		session.current().clear();
	}

	public static UsuarioAnalise getAuthenticatedUser(Session session) {

		return Cache.get(CACHE_PREFIX +  session.current().getId(), UsuarioAnalise.class);
	}

	public static void setUsuarioSessao(UsuarioAnalise usuario, Session session) {

		Cache.set(CACHE_PREFIX +  session.getId(), usuario);
	}

	public static UsuarioAnalise getUsuarioSessao() {

		return getAuthenticatedUser(Session.current());
	}

	@Override
	public Usuario usuarioLogadoBySessionKey(String sessionKey) {

		br.ufla.lemaf.beans.pessoa.Usuario usuarioEntradaUnica;

		try {

			if(CadastroUnificadoWS.ws == null) {
				throw new RuntimeException("Não foi possível realizar a autenticação. Contate o administrador do sistema.");
			}

			// Login no portal do Cadastro Unificado
			usuarioEntradaUnica = CadastroUnificadoWS.ws.searchBySessionKey(sessionKey);

			Logger.info("[CADASTRO-UNIFICADO-AUTHENTICATION - autenticar()]");

			Usuario usuarioSessao = new Usuario(usuarioEntradaUnica);

			usuarioSessao.perfilSelecionado = usuarioEntradaUnica.perfilSelecionado;
			usuarioSessao.setorSelecionado = usuarioEntradaUnica.setorSelecionado;

			return usuarioSessao;
		}
		catch (Exception e) {

			e.printStackTrace();

			Logger.error(e.getMessage());

			Logger.error("[CADASTRO-UNIFICADO-AUTHENTICATION - autenticar()]");

			return null;
		}
	}
}