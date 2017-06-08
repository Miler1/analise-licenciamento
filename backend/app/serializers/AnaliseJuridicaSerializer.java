package serializers;

import utils.SerializerUtil;
import flexjson.JSONSerializer;

public class AnaliseJuridicaSerializer {
	
	public static JSONSerializer findInfo = SerializerUtil.create(
			
				"id",
				"analise.id",
				"analise.dataVencimentoPrazo",
				"analise.processo.caracterizacoes.id",
				"analise.processo.caracterizacoes.tipoLicenca.id",
				"analise.processo.caracterizacoes.tipoLicenca.nome",
				"analise.processo.caracterizacoes.documentosEnviados.id",
				"analise.processo.caracterizacoes.documentosEnviados.caminho",
				"analise.processo.caracterizacoes.documentosEnviados.tipo.nome",
				"parecer",
				"dataVencimentoPrazo",
				"revisaoSolicitada",
				"ativo",
				"dataInicio",
				"dataFim",
				"tipoResultadoAnalise.id",
				"tipoResultadoAnalise.nome"	,
				"ativo",
				"documentos.id",
				"documentos.caminho",
				"documentos.tipo.id",
				"documentos.tipo.nome",
				"consultoresJuridicos.id",
				"consultoresJuridicos.usuario.id",
				"consultoresJuridicos.usuario.pessoa.cpf",
				"consultoresJuridicos.usuario.pessoa.id",
				"consultoresJuridicos.usuario.pessoa.nome"
			);

}
