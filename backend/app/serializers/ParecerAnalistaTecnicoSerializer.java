package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ParecerAnalistaTecnicoSerializer {

	public static JSONSerializer findByIdHistoricoTramitacao = SerializerUtil.create(
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
			"documentoMinuta.id",
			"documentoMinuta.nomeDoArquivo",
			"vistoria",
			"idHistoricoTramitacao",
			"doProcesso",
			"daAnaliseTecnica",
			"daConclusao"
	);

}
