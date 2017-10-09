package controllers;

import play.Play;
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
		
		if(Play.mode == Play.Mode.DEV)
			dados.usuarioSessao.autenticadoViaToken = true;
		
		String jsonConfig = ApplicationSerializer.findInfo.serialize(dados);
		
		render(jsonConfig);
	}
	
	public static void versao() {
		
		render();
	}
	
	public static class DadosApp {
		
		public UsuarioSessao usuarioSessao;
		public ConfiguracoesApp configuracoes = new ConfiguracoesApp();
	}
	
	public static class ConfiguracoesApp {
		
		public String baseURL = Configuracoes.HTTP_PATH;
	}

}
