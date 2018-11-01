package security;

import main.java.br.ufla.lemaf.beans.pessoa.Usuario;
import models.portalSeguranca.UsuarioLicenciamento;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.mvc.Scope;
import play.mvc.Scope.Session;

import static utils.Configuracoes.oAuthClient;

public class Auth {

	private static final String CACHE_PREFIX = "LIC_USER_";

	public static boolean autenticar(Scope.Session session) {

		UsuarioLicenciamento usuario = getAuthenticatedUser(session);

		if (usuario == null) {

			return false;
		}

		setUsuarioSessao(usuario, session);
		return true;
	}

//	public static boolean autenticar(Http.Request request, Scope.Session session) {
//
//		AuthService authService = new AuthServiceFactory().getInstance();
//
//		UsuarioSessao usuarioSessao = authService.autenticar(request);
//
//		if (usuarioSessao == null)
//			return false;
//
//		setUsuarioSessao(usuarioSessao, session);
//		return true;
//	}

	public static void logout(Session session) {

		String key = CACHE_PREFIX +  session.getId();

		if (Cache.get(key) != null)
			Cache.delete(key);

		session.current().clear();
	}

	public static UsuarioLicenciamento getAuthenticatedUser(Session session) {

		return Cache.get(CACHE_PREFIX +  session.current().getId(), UsuarioLicenciamento.class);
	}

	public static UsuarioLicenciamento getUsuarioSessao() {

		return getAuthenticatedUser(Session.current());
	}

	public static void setUsuarioSessao(UsuarioLicenciamento usuario, Session session) {

		Cache.set(CACHE_PREFIX +  session.getId(), usuario);
	}

	public static boolean autenticarEntradaUnica(String sessionKeyEntradaUnica, Session session) {

		Usuario usuario = oAuthClient.searchBySessionKey(sessionKeyEntradaUnica);

		if (usuario == null) {

			return false;
		}

		UsuarioLicenciamento usuarioLicenciamento = UsuarioLicenciamento.find("login", usuario.login).first();
		usuarioLicenciamento.usuarioEntradaUnica = usuario;

		Logger.debug("ID da Sessão: %s", new Object[]{session.getId()});

		Cache.set(session.getId(), usuarioLicenciamento, Play.configuration.getProperty("application.session.maxAge"));

		return true;
	}
}