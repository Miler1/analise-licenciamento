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
	
		AnaliseTecnica analise = AnaliseTecnica.findById(idAnaliseTecnica);
		
		renderJSON(analise, AnaliseTecnicaSerializer.findInfo);
		
	}
	
	public void getRestricoesGeo(Long idAnaliseTecnica) throws Exception {
		
		Geometry geometryEmpreendimento = new WKTReader2().read("POINT(-54.5526123046875 -4.088418298945898)");
		Geometry geometryImovel = new WKTReader2().read("POLYGON((-54.45648193359375 -4.165636821607836,-54.6844482421875 -4.198508047098707,-54.56634521484375 -4.332717735806342,-54.3658447265625 -4.280680030820496,-54.35211181640625 -4.198508047098707,-54.45648193359375 -4.165636821607836))");

		ImovelEmpreendimento imovel = new ImovelEmpreendimento();

		imovel.codigo = "PA-1505650-A37C2EB9275541D1BAB5D01CD8783D81";
		imovel.limite = geometryImovel;

		File file = Geoserver.verificarRestricoes(geometryEmpreendimento, imovel, "analise-geo-id-" + idAnaliseTecnica);

		renderJSON(FileUtils.readFileToString(file, Charset.defaultCharset()));
	}
}
