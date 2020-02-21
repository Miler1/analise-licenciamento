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
			"documentos.key",
			"documentos.arquivo",
			"documentoParecer.id",
			"documentoParecer.nomeDoArquivo",
			"documentoMinuta.id",
			"documentoMinuta.nomeDoArquivo",
			"vistoria.documentoRelatorioTecnicoVistoria.id",
			"vistoria.documentoRelatorioTecnicoVistoria.nomeDoArquivo",
			"vistoria.documentoRelatorioTecnicoVistoria.tipo.id",
			"vistoria.documentoRelatorioTecnicoVistoria.tipo.nome",
			"vistoria.id",
			"vistoria.conclusao",
			"vistoria.realizada",
			"idHistoricoTramitacao",
			"doProcesso",
			"daAnaliseTecnica",
			"daConclusao"
	);

}
