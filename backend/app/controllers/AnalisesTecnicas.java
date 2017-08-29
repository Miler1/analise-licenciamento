package controllers;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.geotools.geometry.jts.WKTReader2;

import com.vividsolutions.jts.geom.Geometry;

import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.geocalculo.Geoserver;
import models.licenciamento.ImovelEmpreendimento;
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
	
		verificarPermissao(Acao.VALIDAR_PARECER_TECNICO, Acao.INICIAR_PARECER_TECNICO, Acao.VALIDAR_PARECERES_JURIDICO_TECNICO);
		
		AnaliseTecnica analise = AnaliseTecnica.findById(idAnaliseTecnica);
		
		renderJSON(analise, AnaliseTecnicaSerializer.findInfo);
		
	}
	
	
	public void getRestricoesGeo(Long idAnaliseTecnica) throws Exception {
		
		verificarPermissao(Acao.INICIAR_PARECER_TECNICO);
		
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
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);
		
		analiseAValidar.validaParecer(analise, usuarioExecutor);
		
		renderMensagem(Mensagem.VALIDACAO_PARECER_TECNICO_CONCLUIDA_SUCESSO);
	}
	
	public static void validarParecerGerente(AnaliseTecnica analise) {
		
		verificarPermissao(Acao.VALIDAR_PARECER_TECNICO);
		
		AnaliseTecnica analiseAValidar = AnaliseTecnica.findById(analise.id);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);
		
		analiseAValidar.validaParecerGerente(analise, usuarioExecutor);
		
		renderMensagem(Mensagem.VALIDACAO_PARECER_TECNICO_CONCLUIDA_SUCESSO);
	}
	
	public static void validarParecerAprovador(AnaliseTecnica analise) {
		
		verificarPermissao(Acao.VALIDAR_PARECERES_JURIDICO_TECNICO);
		
		AnaliseTecnica analiseAValidar = AnaliseTecnica.findById(analise.id);
		
		UsuarioSessao usuarioSessao = getUsuarioSessao();
		Usuario usuarioExecutor = Usuario.findById(usuarioSessao.id);
		
		analiseAValidar.validarParecerValidacaoAprovador(analise, usuarioExecutor);
		
		renderMensagem(Mensagem.VALIDACAO_PARECER_APROVADOR_CONCLUIDA_SUCESSO);
	}	
}