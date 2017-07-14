package controllers;

import java.util.List;

import models.AnaliseDocumento;
import models.AnaliseJuridica;
import models.portalSeguranca.Usuario;
import security.Acao;
import security.UsuarioSessao;
import serializers.AnaliseDocumentoSerializer;
import serializers.AnaliseJuridicaSerializer;
import utils.Mensagem;

public class AnalisesJuridicas extends InternalController {
	
	public static void findDocumentosAnalisados(Long idAnaliseJuridica) {
		
		verificarPermissao(Acao.INICIAR_PARECER_JURIDICO);
		
		List<AnaliseDocumento> documentos = AnaliseJuridica.findDocumentos(idAnaliseJuridica);
		
		renderJSON(documentos, AnaliseDocumentoSerializer.analiseJuridica);
	}

	public static void findById(Long idAnaliseJuridica) {
		
		verificarPermissao(Acao.VALIDAR_PARECER_TECNICO, Acao.VALIDAR_PARECER_JURIDICO, Acao.INICIAR_PARECER_JURIDICO);
		
		AnaliseJuridica analise = AnaliseJuridica.findById(idAnaliseJuridica);
		
		renderJSON(analise, AnaliseJuridicaSerializer.findInfo);
	
	}

	public static void alterar(AnaliseJuridica analise) {
		
		verificarPermissao(Acao.INICIAR_PARECER_JURIDICO);
		
		AnaliseJuridica analiseAAlterar = AnaliseJuridica.findById(analise.id);
				
		analiseAAlterar.update(analise);
				
		renderMensagem(Mensagem.ANALISE_CADASTRADA_SUCESSO);	
	}
	
	public static void concluir(AnaliseJuridica analise) {
		
		verificarPermissao(Acao.INICIAR_PARECER_JURIDICO);
		
		AnaliseJuridica analiseAAlterar = AnaliseJuridica.findById(analise.id);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);		
		
		analiseAAlterar.finalizar(analise, usuarioExecutor);
		
		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);				
		
	}

	public static void iniciar(AnaliseJuridica analise) {
	
		verificarPermissao(Acao.INICIAR_PARECER_JURIDICO);		
		
		AnaliseJuridica analiseAAlterar = AnaliseJuridica.findById(analise.id);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);		
				
		analiseAAlterar.iniciar(usuarioExecutor);
				
		renderMensagem(Mensagem.ANALISE_JURIDICA_INICIADA_SUCESSO);	

	}

	public static void findByNumeroProcesso() {
		
		verificarPermissao(Acao.INICIAR_PARECER_JURIDICO);
		
		String numeroProcesso = getParamAsString("numeroProcesso");
		
		AnaliseJuridica analise = AnaliseJuridica.findByNumeroProcesso(numeroProcesso);
		
		renderJSON(analise, AnaliseJuridicaSerializer.parecer);
	
	}
	
	public static void validarParecer(AnaliseJuridica analise) {
		
		verificarPermissao(Acao.VALIDAR_PARECER_JURIDICO);
		
		AnaliseJuridica analiseAValidar = AnaliseJuridica.findById(analise.id);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);		
		
		analiseAValidar.validaParecer(analise, usuarioExecutor);
		
		renderMensagem(Mensagem.VALIDACAO_PARECER_JURIDICO_CONCLUIDA_SUCESSO);				
		
	}	

}
