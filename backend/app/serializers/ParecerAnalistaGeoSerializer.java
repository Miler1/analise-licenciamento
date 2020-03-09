package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ParecerAnalistaGeoSerializer {

	public static JSONSerializer findByIdHistoricoTramitacao = SerializerUtil.create(
			"id",
			"tipoResultadoAnalise.id",
			"tipoResultadoAnalise.nome",
			"parecer",
			"dataParecer",
			"usuario.id",
			"usuario.pessoa.nome",
			"usuario.pessoa.id",
			"documentos.id",
			"documentos.nomeDoArquivo",
			"documentos.tipo.id",
			"documentos.tipo.nome",
			"documentoParecer.id",
			"documentoParecer.nomeDoArquivo",
			"cartaImagem.id",
			"cartaImagem.nomeDoArquivo",
			"situacaoFundiaria",
			"analiseTemporal",
			"conclusao"
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
