package controllers;

import java.util.HashMap;
import java.util.Map;

import play.mvc.Http.Request;
import security.Auth;
import utils.Configuracoes;

public class Login extends GenericController {

	private static final String TEMPLATE = "public/app/features/login/login.html";
	
	public static void showLogin() {
		
		renderLoginTemplate(null);
	}
	
	public static void login() {
		
		if (Auth.autenticar(Request.current(), session.current()))
			redirect("/");
		else if(Configuracoes.EXTERNAL_LOGIN)
			redirect(Configuracoes.LOGIN_URL);
		else
			renderLoginTemplate("Login e/ou senha incorreto(s).");
	}
	
	
	public static void logout() {
		
		Auth.logout(session.current());
		redirect(Configuracoes.LOGIN_URL);
	}


	private static void renderLoginTemplate(String errorMsg) {
		
		Map<String, Object> params = null;
		
		if (errorMsg != null) {
			
			params = new HashMap<String, Object>();
			params.put("errorMsg", errorMsg);
			
			renderTemplate(TEMPLATE, params);
			
		} else {
			
			renderTemplate(TEMPLATE);
		}
	}
}
