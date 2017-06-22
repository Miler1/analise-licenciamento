package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class AnaliseTecnicaSerializer {
	
	public static JSONSerializer findInfo = SerializerUtil.create(
			
			"id",
			"analise.id",
			"analise.dataVencimentoPrazo",
			"analise.processo.id",
			"analise.processo.numero",
			"analise.processo.empreendimento.pessoa.cpf",
			"analise.processo.empreendimento.pessoa.cnpj",
			"analise.processo.empreendimento.denominacao",
			"analise.processo.empreendimento.municipio.nome",
			"analise.processo.empreendimento.municipio.estado.codigo",
			"analise.processo.caracterizacoes.id",
			"analise.processo.caracterizacoes.tipoLicenca.id",
			"analise.processo.caracterizacoes.tipoLicenca.nome",
			"analise.processo.caracterizacoes.documentosEnviados.id",
			"analise.processo.caracterizacoes.documentosEnviados.nome",
			"analise.processo.caracterizacoes.documentosEnviados.tipo.nome",
			"analise.processo.numero",
			"analise.processo.empreendimento.id",
			"analise.processo.empreendimento.denominacao",
			"analise.processo.empreendimento.pessoa.id",
			"analise.processo.empreendimento.pessoa.nome",
			"analise.processo.empreendimento.pessoa.cpf",
			"analise.processo.empreendimento.municipio.nome",
			"analise.processo.empreendimento.municipio.estado.codigo",				
			"analisesDocumentos.id",
			"analisesDocumentos.validado",
			"analisesDocumentos.parecer",
			"analisesDocumentos.documento.tipo.nome",
			"analisesDocumentos.documento.id",
			"parecer",
			"dataVencimentoPrazo",
			"revisaoSolicitada",
			"ativo",
			"dataInicio",
			"dataFim",
			"tipoResultadoAnalise.id",
			"tipoResultadoAnalise.nome"	,
			"documentos.id",
			"documentos.nome",
			"documentos.tipo.id",
			"documentos.tipo.nome",
			"consultoresJuridicos.id",
			"consultoresJuridicos.usuario.id",
			"consultoresJuridicos.usuario.pessoa.cpf",
			"consultoresJuridicos.usuario.pessoa.id",
			"consultoresJuridicos.usuario.pessoa.nome"
		);

		public static JSONSerializer parecer = SerializerUtil.create(
		
			"parecer"
		);	

}
