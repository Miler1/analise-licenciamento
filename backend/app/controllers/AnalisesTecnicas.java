package controllers;

import models.*;
import models.geocalculo.Geoserver;
import org.apache.commons.io.FileUtils;
import security.Acao;
import serializers.AnaliseTecnicaSerializer;
import utils.Mensagem;

import javax.validation.ValidationException;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;

public class AnalisesTecnicas extends InternalController {
		
	public static void iniciar(AnaliseTecnica analise) {
		
		verificarPermissao(Acao.INICIAR_PARECER_TECNICO);
		
		AnaliseTecnica analiseAAlterar = AnaliseTecnica.findById(analise.id);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();
				
		analiseAAlterar.iniciar(usuarioExecutor);
				
		renderMensagem(Mensagem.ANALISE_TECNICA_INICIADA_SUCESSO);	

	}

	public static void iniciarAnaliseTecnicaGerente(AnaliseTecnica analise) {

		AnaliseTecnica analiseAlterar = AnaliseTecnica.findById(analise.id);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();

		analiseAlterar.iniciarAnaliseTecnicaGerente(usuarioExecutor);

		renderMensagem(Mensagem.GERENTE_INICIOU_ANALISE_SUCESSO);

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
	
		verificarPermissao(Acao.VALIDAR_PARECER_TECNICO, Acao.INICIAR_PARECER_TECNICO, Acao.VALIDAR_PARECERES);
		
		AnaliseTecnica analise = AnaliseTecnica.findById(idAnaliseTecnica);
		
		renderJSON(analise, AnaliseTecnicaSerializer.findInfo);
		
	}
	
	public static void getRestricoesGeo(Long idAnaliseTecnica) throws Exception {

		verificarPermissao(Acao.INICIAR_PARECER_TECNICO, Acao.VALIDAR_PARECERES);

		AnaliseTecnica analiseTecnica = AnaliseTecnica.findById(idAnaliseTecnica);

		File file = Geoserver.verificarRestricoes(
				analiseTecnica.analise.processo.empreendimento.coordenadas,
				analiseTecnica.analise.processo.empreendimento.imovel,
				"analise-geo-id-" + idAnaliseTecnica
		);

		renderJSON(FileUtils.readFileToString(file, Charset.defaultCharset()));
	}
	
	public static void validarParecer(AnaliseTecnica analise) {
		
		verificarPermissao(Acao.VALIDAR_PARECER_TECNICO);
		
		AnaliseTecnica analiseAValidar = AnaliseTecnica.findById(analise.id);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();
		
		analiseAValidar.validaParecer(analise, usuarioExecutor);
		
		renderMensagem(Mensagem.VALIDACAO_PARECER_TECNICO_CONCLUIDA_SUCESSO);
	}
	
	public static void validarParecerGerente(AnaliseTecnica analise) {
		
		verificarPermissao(Acao.VALIDAR_PARECER_TECNICO);
		
		AnaliseTecnica analiseAValidar = AnaliseTecnica.findById(analise.id);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();
		
		analiseAValidar.validaParecerGerente(analise, usuarioExecutor);
		
		renderMensagem(Mensagem.VALIDACAO_PARECER_TECNICO_CONCLUIDA_SUCESSO);
	}
	
	public static void validarParecerAprovador(AnaliseTecnica analise) {
		
		verificarPermissao(Acao.VALIDAR_PARECERES);
		
		AnaliseTecnica analiseAValidar = AnaliseTecnica.findById(analise.id);

		UsuarioAnalise usuarioExecutor = getUsuarioSessao();
		
		analiseAValidar.validarParecerValidacaoAprovador(analise, usuarioExecutor);
		
		renderMensagem(Mensagem.VALIDACAO_PARECER_APROVADOR_CONCLUIDA_SUCESSO);
	}

	public static void downloadPDFParecer(AnaliseTecnica analiseTecnica) throws Exception {

		verificarPermissao(Acao.BAIXAR_DOCUMENTO);

		String novoParecer = analiseTecnica.parecerAnalista;

		AnaliseTecnica analiseTecnicaSalva = AnaliseTecnica.findById(analiseTecnica.id);
		ParecerAnalistaTecnico ultimoParecer = analiseTecnicaSalva.pareceresAnalistaTecnico.stream().max(Comparator.comparing(ParecerAnalistaTecnico::getDataParecer)).orElseThrow(ValidationException::new);

		analiseTecnicaSalva.parecerAnalista = novoParecer;

		ultimoParecer.documentoParecer = analiseTecnicaSalva.gerarPDFParecer(ultimoParecer);

		String nome = ultimoParecer.documentoParecer.tipo.nome +  "_" + analiseTecnicaSalva.id + ".pdf";
		nome = nome.replace(' ', '_');
		response.setHeader("Content-Disposition", "attachment; filename=" + nome);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "application/pdf");

		ultimoParecer._save();

		renderBinary(ultimoParecer.documentoParecer.getFile(), nome);

	}

	public static void downloadPDFNotificacao(AnaliseTecnica analiseTecnica) throws Exception {

		verificarPermissao(Acao.INICIAR_PARECER_TECNICO);

		analiseTecnica.analise = Analise.findById(analiseTecnica.analise.id);

		List<Notificacao> notificacaos = Notificacao.gerarNotificacoesTemporarias(analiseTecnica);

		Documento pdfNotificacao = Notificacao.gerarPDF(notificacaos, analiseTecnica);

		String nome = pdfNotificacao.tipo.nome +  "_" + analiseTecnica.id + ".pdf";
		nome = nome.replace(' ', '_');
		response.setHeader("Content-Disposition", "attachment; filename=" + nome);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "application/pdf");

		renderBinary(pdfNotificacao.arquivo, nome);

	}

	public static void buscaAnaliseTecnicaByAnalise(Long idAnalise) {

		AnaliseTecnica analiseTecnica = AnaliseTecnica.find("id_analise = :id_analise")
				.setParameter("id_analise", idAnalise).first();

		renderJSON(analiseTecnica, AnaliseTecnicaSerializer.findInfo);

	}

}