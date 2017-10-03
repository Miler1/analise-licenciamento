package serializers;

import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

import com.vividsolutions.jts.geom.Geometry;

import flexjson.JSONSerializer;

public class LicencaAnaliseSerializer {

	public static JSONSerializer list = SerializerUtil.create(
			
			"id",
			"validade",
			"observacao",
			"emitir",
			"caracterizacao.tipoLicenca.nome",			
			"caracterizacao.atividadeCaracterizacao.porteEmpreendimento.codigo",
			"caracterizacao.atividadeCaracterizacao.atividade.potencialPoluidor.codigo",
			"condicionantes.id",
			"condicionantes.texto",
			"condicionantes.prazo",
			"condicionantes.ordem",
			"recomendacoes.id",
			"recomendacoes.texto",
			"recomendacoes.ordem");
	
	
}
