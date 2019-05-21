package security;

import models.EntradaUnica.Usuario;
import models.portalSeguranca.UsuarioLicenciamento;
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

	public static UsuarioLicenciamento getAuthenticatedUser(Session session) {

		return Cache.get(CACHE_PREFIX +  session.current().getId(), UsuarioLicenciamento.class);
	}

	public static void setUsuarioSessao(UsuarioLicenciamento usuario, Session session) {

		Cache.set(CACHE_PREFIX +  session.getId(), usuario);
	}

	public static UsuarioLicenciamento getUsuarioSessao() {

		return getAuthenticatedUser(Session.current());
	}

	@Override
	public Usuario usuarioLogadoBySessionKey(String sessionKey) {

		main.java.br.ufla.lemaf.beans.pessoa.Usuario usuarioEntradaUnica;

		try {

			if(CadastroUnificadoWS.ws == null) {
				throw new RuntimeException("Não foi possível realizar a autenticação. Contate o administrador do sistema.");
			}

			// Login no portal do Cadastro Unificado
			usuarioEntradaUnica = CadastroUnificadoWS.ws.searchBySessionKey(sessionKey);

			Logger.info("[CADASTRO-UNIFICADO-AUTHENTICATION - autenticar()]");

			Usuario usuarioSessao = new Usuario(usuarioEntradaUnica);

			usuarioSessao.perfilSelecionado = usuarioEntradaUnica.perfilSelecionado;
//			usuarioSessao.perfil = new Perfil(usuarioEntradaUnica.perfilSelecionado);
//
//			usuarioSessao.acessoLiberado = true;


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