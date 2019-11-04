package controllers;

import exceptions.AppException;
import models.EntradaUnica.Usuario;
import models.PerfilUsuarioAnalise;
import models.Pessoa;
import models.SetorUsuarioAnalise;
import models.UsuarioAnalise;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.mvc.Before;
import security.Auth;
import security.AuthService;
import security.AuthServiceFactory;
import security.ExternalServiceSecurity;
import services.IntegracaoEntradaUnicaService;
import utils.Configuracoes;
import utils.Helper;
import utils.Mensagem;

import java.util.ArrayList;
import java.util.List;

public class Login extends GenericController {

	private static AuthService authenticationService = new AuthServiceFactory().getInstance();
	@Before(unless={"login", "logout", "showLogin","getAuthenticatedUser", "showLoginEntradaUnica"})
	protected static void isAutenticado() {

		if(isExternalResource()) {

			ExternalServiceSecurity.validateAdress(request);

		} else if(Auth.getUsuarioSessao() == null) {

			showLogin();
			unauthorized();
		}

	}

	public static void showLogin() {

		redirect(Configuracoes.ENTRADA_UNICA_URL_PORTAL_SEGURANCA);
	}

	public static void showLoginEntradaUnica(String sessionKeyEntradaUnica) throws Exception {

		if(sessionKeyEntradaUnica != null) {

			if(!doAuthentication(sessionKeyEntradaUnica)) {
				showLogin();
			}

			UsuarioAnalise usuarioLogado =  Auth.getUsuarioSessao();

			if (usuarioLogado == null) {
				unauthorized();
			}

			String codigoPerfil = usuarioLogado.usuarioEntradaUnica.perfilSelecionado.codigo;

			if (!usuarioLogado.usuarioEntradaUnica.possuiPerfil(codigoPerfil)) {

				clearUsuarioSessao();

				throw new AppException(Mensagem.PERFIL_NAO_VINCULADO_AO_USUARIO);
			}

			redirect(Configuracoes.HTTP_PATH);
		}

	}

	private static void clearUsuarioSessao() {

		request.current().cookies.remove("PLAY_SESSION");
		Cache.delete(session.getId());
		session.clear();
	}

	private static List<PerfilUsuarioAnalise> preenchePerfis(UsuarioAnalise usuarioAnalise, Usuario usuario) {

		List<PerfilUsuarioAnalise> perfis = new ArrayList<>();

		usuario.salvarPerfis(usuarioAnalise);

		return perfis;

	}

	private static List<SetorUsuarioAnalise> preencheSetores(UsuarioAnalise usuarioAnalise, Usuario usuario) {

		List<SetorUsuarioAnalise> setores = new ArrayList<>();

		usuario.salvarSetores(usuarioAnalise);

		return setores;

	}


	/**
	 * Executa serviço de autenticação configurado no sistema via application.conf se o usuário não estiver autenticado
	 * @return
	 */
	private static boolean doAuthentication(String sessionKeyEntradaUnica) throws Exception {

		Usuario usuario = authenticationService.usuarioLogadoBySessionKey(sessionKeyEntradaUnica);

		if (usuario == null)
			return false;

		boolean autenticado = (usuario.perfilSelecionado != null);

		UsuarioAnalise usuarioAnalise = UsuarioAnalise.find("login", usuario.login).first();

		if (usuarioAnalise == null) {
			usuarioAnalise = new UsuarioAnalise();
			usuarioAnalise.login = Helper.desformatarCpfCnpj(usuario.login);
			Pessoa pessoa = new Pessoa();
			pessoa.nome = usuario.nome;
			pessoa.save();
			usuarioAnalise.pessoa = pessoa;
			usuarioAnalise = usuarioAnalise.save();

			usuarioAnalise.perfis = preenchePerfis(usuarioAnalise, usuario);
			usuarioAnalise.setores = preencheSetores(usuarioAnalise,usuario);
			usuarioAnalise.pessoa = pessoa;
			usuarioAnalise.save();
		}

		if (usuarioAnalise.perfis == null || usuarioAnalise.perfis.isEmpty()) {

			usuarioAnalise.perfis = preenchePerfis(usuarioAnalise,usuario);

		}

		if(usuarioAnalise.setores == null || usuarioAnalise.setores.isEmpty()){

			usuarioAnalise.setores = preencheSetores(usuarioAnalise,usuario);

		}

		usuarioAnalise.usuarioEntradaUnica = usuario;

		if (autenticado) {
			Logger.info("[AUTH] Usuário autenticado [ Login: " + usuario.login + " ] - Authenticated - SessionID: " + session.getId() + " - GA SessionID: " + usuario.sessionKeyEntradaUnica);
			Auth.setUsuarioSessao(usuarioAnalise, session);
		} else {

			String login = request.params.get("login");

			Logger.error("[LOGIN] - Usuário não autenticado [ Login: " + (login != null ? login : "?") + " ] ");
		}

		return autenticado;

	}


	public static void logout() {

		Auth.logout(session.current());
		redirect(Configuracoes.LOGIN_URL);
	}

	public static UsuarioAnalise getAuthenticatedUser() {

		Logger.debug("ID da Sessão: %s", new Object[]{session.getId()});

		return Cache.get(session.getId(), UsuarioAnalise.class);

	}

	private static boolean isExternalResource() {

		return request.path.indexOf(Play.configuration.getProperty("authentication.url.external")) != -1;

	}

}