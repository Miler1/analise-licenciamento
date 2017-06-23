package controllers;

import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.portalSeguranca.Usuario;
import security.Acao;
import security.UsuarioSessao;
import serializers.AnaliseTecnicaSerializer;
import utils.Mensagem;

public class AnalisesTecnicas extends InternalController {
		
	public static void iniciar(AnaliseTecnica analise) {
		
		verificarPermissao(Acao.INICIAR_PARECER_TECNICO);
		
		AnaliseTecnica analiseAAlterar = AnaliseTecnica.findById(analise.id);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);		
				
		analiseAAlterar.iniciar(usuarioExecutor);
				
		renderMensagem(Mensagem.ANALISE_TECNICA_INICIADA_SUCESSO);	

	}

	public static void concluir(AnaliseTecnica analise) {
		
		verificarPermissao(Acao.INICIAR_PARECER_TECNICO);
		
		AnaliseTecnica analiseAAlterar = AnaliseTecnica.findById(analise.id);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);		
		
		analiseAAlterar.finalizar(analise, usuarioExecutor);
		
		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);				
		
	}	
	
	public static void findByNumeroProcesso() {
		
		verificarPermissao(Acao.INICIAR_PARECER_TECNICO);
		
		String numeroProcesso = getParamAsString("numeroProcesso");
		
		AnaliseTecnica analise = AnaliseTecnica.findByNumeroProcesso(numeroProcesso);
		
		renderJSON(analise, AnaliseTecnicaSerializer.parecer);
	
	}
	
	public static void alterar(AnaliseTecnica analise) {
		
		verificarPermissao(Acao.INICIAR_PARECER_TECNICO);
		
		AnaliseTecnica analiseAAlterar = AnaliseTecnica.findById(analise.id);
				
		analiseAAlterar.update(analise);
				
		renderMensagem(Mensagem.ANALISE_CADASTRADA_SUCESSO);	
	}

	public static void findById(Long idAnaliseTecnica) {
	
		AnaliseTecnica analise = AnaliseTecnica.findById(idAnaliseTecnica);
		
		renderJSON(analise, AnaliseTecnicaSerializer.findInfo);
		
	}
}
