package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ParecerGerenteSerializer {

	public static JSONSerializer findByIdHistoricoTramitacao = SerializerUtil.create(
			"tipoResultadoAnalise.id",
			"tipoResultadoAnalise.nome",
			"parecer",
			"documentos.tipo.id",
			"documentos.tipo.nome",
			"documentos.id",
			"documentos.nomeDoArquivo",
			"dataParecer",
			"usuario.id",
			"usuario.pessoa.nome",
			"usuario.pessoa.id"
	);

}
