package controllers;

import security.Auth;
import security.UsuarioSessao;
import serializers.ApplicationSerializer;
import utils.Configuracoes;

public class Application extends GenericController {

    public static void index() {
        
		UsuarioSessao usuarioSessao = Auth.getUsuarioSessao(session.current());
		
		if (usuarioSessao != null)
			redirect(Configuracoes.INDEX_URL);
		else
			redirect(Configuracoes.LOGIN_URL);
    	
    }
    
	public static void findInfo() {
		
		DadosApp dados = new DadosApp();
		dados.usuarioSessao = Auth.getUsuarioSessao(session.current());
		
		renderJSON(dados, ApplicationSerializer.findInfo);
	}
	
	public static void versao() {
		
		render();
	}
	
	public static class DadosApp {
		
		public UsuarioSessao usuarioSessao;
	}

}