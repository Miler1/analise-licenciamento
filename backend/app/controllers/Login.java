package controllers;

import play.mvc.Http;
import security.Auth;
import utils.Configuracoes;

public class Login extends GenericController {

	public static void autenticar() {

		if (Auth.autenticarPortalSeguranca(Http.Request.current(), session.current()))
			redirect(Configuracoes.HTTP_PATH);
		else if(Configuracoes.EXTERNAL_LOGIN)
			redirect(Configuracoes.LOGIN_URL);
	}

	public static void logout() {

		Auth.logout(session.current());
		redirect(Configuracoes.LOGIN_URL);
	}

	public static void login(String sessionKeyEntradaUnica) {

		if (Auth.autenticarEntradaUnica(sessionKeyEntradaUnica, session.current()))
			redirect(Configuracoes.HTTP_PATH);
		else if(Configuracoes.EXTERNAL_LOGIN)
			redirect(Configuracoes.LOGIN_URL);
	}

}