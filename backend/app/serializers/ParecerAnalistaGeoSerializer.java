package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ParecerAnalistaGeoSerializer {

	public static JSONSerializer findByIdHistoricoTramitacao = SerializerUtil.create(
			"tipoResultadoAnalise.id",
			"parecer"
	);

	public static JSONSerializer findByIdNumeroProcesso = SerializerUtil.create(
			"parecer",
			"situacaoFundiaria",
			"analiseTemporal",
			"conclusao"
	);

	public static JSONSerializer findByIdAnaliseGeo = SerializerUtil.create(
			"tipoResultadoAnalise.id",
			"parecer"
	);

	public static JSONSerializer findByIdProcesso = SerializerUtil.create(
			"parecer",
			"situacaoFundiaria",
			"analiseTemporal",
			"conclusao",
			"documentos",
			"documentos.id",
			"documentos.nomeDoArquivo",
			"documentos.caminho",
			"documentos.tipo.id",
			"documentos.tipo.nome"

	);

}
