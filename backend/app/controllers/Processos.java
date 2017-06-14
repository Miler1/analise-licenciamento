package controllers;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import models.AnaliseJuridica;
import models.Processo;
import models.geocalculo.Geoserver;

import org.apache.commons.io.FileUtils;

import security.Acao;
import security.Auth;
import serializers.AnaliseJuridicaSerializer;
import serializers.ProcessoSerializer;
import builders.ProcessoBuilder.FiltroProcesso;

import com.vividsolutions.jts.geom.Geometry;

public class Processos extends InternalController {

	public void getAnaliseGeo(Long id) throws Exception {

		Processo processo = Processo.findById(id);

		//Geometry geometryEmpreendimento = new WKTReader2().read("POINT(-54.5526123046875 -4.088418298945898)");
		//Geometry geometryImovel = new WKTReader2().read("POLYGON((-55.2227783203125 -4.209464815163465,-55.3271484375 -4.286157851321014,-55.1953125 -4.3299789981207795,-55.01953125 -4.373797607393285,-54.569091796875 -4.3299789981207795,-54.217529296875 -4.291635632570905,-54.019775390625 -4.132764222542468,-54.371337890625 -3.9957805129630253,-54.635009765625 -3.8861770336993486,-54.6844482421875 -4.198508047098707,-55.030517578125 -4.258768357307995,-55.2227783203125 -4.209464815163465))");

		Geometry geometryEmpreendimento = processo.empreendimento.coordenadas;

		Geometry geometryImovel = null;

		if(processo.empreendimento.imovel != null){
			geometryImovel = processo.empreendimento.imovel.limite;
		}

		File file = Geoserver.verificarRestricoes(geometryEmpreendimento, geometryImovel, processo.numero);

		renderJSON(FileUtils.readFileToString(file, Charset.defaultCharset()));
	}

	public void listWithFilter(FiltroProcesso filtro){
		
		verificarPermissao(Acao.LISTAR_PROCESSO_JURIDICO);
		
		List processosList = Processo.listWithFilter(filtro, Auth.getUsuarioSessao());
		
		renderJSON(processosList);
	}
	
	public void countWithFilter(FiltroProcesso filtro){
		
		verificarPermissao(Acao.LISTAR_PROCESSO_JURIDICO);
		
		renderJSON(Processo.countWithFilter(filtro, Auth.getUsuarioSessao()));
	}
	
	public void findById(Long idProcesso) {
			
		renderJSON(Processo.findById(idProcesso), ProcessoSerializer.list);
	}

	public void getInfoProcesso(Long id) {
				
		Processo processo = Processo.findById(id);
		
		renderJSON(processo, ProcessoSerializer.getInfo);
		
	}

	public static void findAnaliseJuridica(Long idProcesso) {
		
		Processo processo = Processo.findById(idProcesso);
		
		AnaliseJuridica analise = AnaliseJuridica.findByProcesso(processo);
		
		renderJSON(analise, AnaliseJuridicaSerializer.findInfo);
	
	}
}
