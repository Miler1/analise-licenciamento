package controllers;

import models.portalSeguranca.UsuarioLicenciamento;
import play.Logger;
import play.cache.Cache;
import play.mvc.Http;
import play.mvc.Http.Request;
import security.Auth;
import utils.Configuracoes;

public class Login extends GenericController {

	public static void login() {

		if (Auth.autenticar(Http.Request.current(), session.current()))
			redirect(Configuracoes.HTTP_PATH);
		else if(Configuracoes.EXTERNAL_LOGIN)
			redirect(Configuracoes.LOGIN_URL);

	}


	public static void logout() {

		Auth.logout(session.current());
		redirect(Configuracoes.LOGIN_URL);
	}

	public static UsuarioLicenciamento getAuthenticatedUser() {

		Logger.debug("ID da Sessão: %s", new Object[]{session.getId()});

		return Cache.get(session.getId(), UsuarioLicenciamento.class);

	}

}