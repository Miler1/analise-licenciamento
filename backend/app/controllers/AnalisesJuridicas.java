package controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import models.*;
import models.pdf.PDFGenerator;
import models.portalSeguranca.Usuario;
import play.libs.Crypto;
import security.Acao;
import security.UsuarioSessao;
import serializers.AnaliseDocumentoSerializer;
import serializers.AnaliseJuridicaSerializer;
import utils.Configuracoes;
import utils.Mensagem;

public class AnalisesJuridicas extends InternalController {
	
	public static void findDocumentosAnalisados(Long idAnaliseJuridica) {
		
		verificarPermissao(Acao.INICIAR_PARECER_JURIDICO);
		
		List<AnaliseDocumento> documentos = AnaliseJuridica.findDocumentos(idAnaliseJuridica);
		
		renderJSON(documentos, AnaliseDocumentoSerializer.analiseJuridica);
	}

	public static void findById(Long idAnaliseJuridica) {
		
		verificarPermissao(Acao.VALIDAR_PARECER_TECNICO, Acao.VALIDAR_PARECER_JURIDICO, Acao.INICIAR_PARECER_JURIDICO, Acao.CONSULTAR_PROCESSO);
		
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
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id, usuarioSessao.perfilSelecionado, usuarioSessao.setorSelecionado);
		analiseAAlterar.finalizar(analise, usuarioExecutor);
		
		renderMensagem(Mensagem.ANALISE_CONCLUIDA_SUCESSO);				
		
	}

	public static void iniciar(AnaliseJuridica analise) {
	
		verificarPermissao(Acao.INICIAR_PARECER_JURIDICO);		
		
		AnaliseJuridica analiseAAlterar = AnaliseJuridica.findById(analise.id);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id, usuarioSessao.perfilSelecionado, usuarioSessao.setorSelecionado);
				
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
		
		AnaliseJuridica analiseAvalidar = AnaliseJuridica.findById(analise.id);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id, usuarioSessao.perfilSelecionado, usuarioSessao.setorSelecionado);
		
		analiseAvalidar.validaParecer(analise, usuarioExecutor);
		
		renderMensagem(Mensagem.VALIDACAO_PARECER_JURIDICO_CONCLUIDA_SUCESSO);				
		
	}
	
	public static void validarParecerAprovador(AnaliseJuridica analise) {
		
		verificarPermissao(Acao.VALIDAR_PARECERES_JURIDICO_TECNICO);
		
		AnaliseJuridica analiseAvalidar = AnaliseJuridica.findById(analise.id);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id, usuarioSessao.perfilSelecionado, usuarioSessao.setorSelecionado);
		
		analiseAvalidar.validarParecerValidacaoAprovador(analise, usuarioExecutor);
		
		renderMensagem(Mensagem.VALIDACAO_PARECER_APROVADOR_CONCLUIDA_SUCESSO);				
		
	}

	public static void downloadPDFParecer(AnaliseJuridica analiseJuridica) throws Exception {

		verificarPermissao(Acao.INICIAR_PARECER_JURIDICO);

		String novoParecer = analiseJuridica.parecer;

		AnaliseJuridica analiseJuridicaSalva = AnaliseJuridica.findById(analiseJuridica.id);

		analiseJuridicaSalva.parecer = novoParecer;

		Documento pdfParecer = analiseJuridicaSalva.gerarPDFParecer();

		String nome = pdfParecer.tipo.nome +  "_" + analiseJuridicaSalva.id + ".pdf";
		nome = nome.replace(' ', '_');
		response.setHeader("Content-Disposition", "attachment; filename=" + nome);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "application/pdf");

		renderBinary(pdfParecer.arquivo, nome);

	}

	public static void downloadPDFNotificacao(AnaliseJuridica analiseJuridica) throws Exception {

		verificarPermissao(Acao.INICIAR_PARECER_JURIDICO);

		List<Notificacao> notificacaos = Notificacao.gerarNotificacoesTemporarias(analiseJuridica);

		Documento pdfNotificacao = Notificacao.gerarPDF(notificacaos, analiseJuridica);

		String nome = pdfNotificacao.tipo.nome +  "_" + analiseJuridica.id + ".pdf";
		nome = nome.replace(' ', '_');
		response.setHeader("Content-Disposition", "attachment; filename=" + nome);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "application/pdf");

		renderBinary(pdfNotificacao.arquivo, nome);

	}

}
