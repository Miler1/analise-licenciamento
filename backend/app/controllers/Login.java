package controllers;

import play.mvc.Http.Request;
import security.Auth;
import utils.Configuracoes;

public class Login extends GenericController {

	public static void login() {
		
		if (Auth.autenticar(Request.current(), session.current()))
			redirect("/");
		else if(Configuracoes.EXTERNAL_LOGIN)
			redirect(Configuracoes.LOGIN_URL);
		
	}
	
	
	public static void logout() {
		
		Auth.logout(session.current());
		redirect(Configuracoes.LOGIN_URL);
	}
	
}
