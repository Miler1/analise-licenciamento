package security;

import play.cache.Cache;
import play.mvc.Http;
import play.mvc.Scope;
import play.mvc.Scope.Session;

public class Auth {

	private static final String CACHE_PREFIX = "LIC_USER_";
	
	public static boolean autenticar(Http.Request request, Scope.Session session) {
		
		AuthService authService = new AuthServiceFactory().getInstance();
		
		UsuarioSessao usuarioSessao = authService.autenticar(request);
		
		if (usuarioSessao == null)
			return false;
		
		setUsuarioSessao(usuarioSessao, session);
		return true;
	}
	
	public static void logout(Session session) {
		
		String key = CACHE_PREFIX +  session.getId();
		
		if (Cache.get(key) != null)
			Cache.delete(key);
		
		session.current().clear();
	}
	
	public static UsuarioSessao getUsuarioSessao(Session session) {
		
		return Cache.get(CACHE_PREFIX +  session.current().getId(), UsuarioSessao.class);
	}
	
	public static UsuarioSessao getUsuarioSessao() {
		
		return getUsuarioSessao(Session.current());
	}
	
	public static void setUsuarioSessao(UsuarioSessao usuario, Session session) {
		
		Cache.set(CACHE_PREFIX +  session.getId(), usuario);
	}
}
