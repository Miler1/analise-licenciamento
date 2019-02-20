package security;

import models.portalSeguranca.UsuarioLicenciamento;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.mvc.Http;
import play.mvc.Scope.Session;

public class Auth {

	private static final String CACHE_PREFIX = "LIC_USER_";

//	public static boolean autenticar(Scope.Session session) {
//
//		UsuarioLicenciamento usuario = getAuthenticatedUser(session);
//
//		if (usuario == null) {
//
//			return false;
//		}
//
//		setUsuarioSessao(usuario, session);
//		return true;
//	}

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

	public static boolean autenticar(Http.Request request, Session session) {

		AuthService authService = new AuthServiceFactory().getInstance();

		UsuarioLicenciamento usuarioLicenciamento = authService.autenticar(request);

		if (usuarioLicenciamento == null) {

			return false;
		}

		Logger.debug("ID da Sessão: %s", new Object[]{session.getId()});

		Cache.set(CACHE_PREFIX + session.getId(), usuarioLicenciamento, Play.configuration.getProperty("application.session.maxAge"));

		return true;
	}
}