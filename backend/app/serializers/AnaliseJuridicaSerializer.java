package serializers;

import utils.SerializerUtil;
import flexjson.JSONSerializer;

public class AnaliseJuridicaSerializer {
	
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
				"analise.processo.empreendimento.imovel.id",
				"analise.processo.caracterizacoes.id",
				"analise.processo.caracterizacoes.tipoLicenca.id",
				"analise.processo.caracterizacoes.tipoLicenca.nome",
				"analise.processo.caracterizacoes.documentosEnviados.id",
				"analise.processo.caracterizacoes.documentosEnviados.nome",
				"analise.processo.caracterizacoes.documentosEnviados.tipo.nome",
				"analise.processo.caracterizacoes.documentosEnviados.tipo.tipoAnalise",
				"analise.processo.numero",
				"analise.processo.empreendimento.id",
				"analise.processo.empreendimento.denominacao",
				"analise.processo.empreendimento.pessoa.id",
				"analise.processo.empreendimento.pessoa.nome",
				"analise.processo.empreendimento.pessoa.cpf",
				"analise.processo.empreendimento.municipio.nome",
				"analise.processo.empreendimento.municipio.estado.codigo",			
				"analiseJuridicaRevisada.id",
				"analiseJuridicaRevisada.parecerValidacao",
				"analiseJuridicaRevisada.parecerValidacaoAprovador",
				"analiseJuridicaRevisada.usuarioValidacaoAprovador.id",
				"analiseJuridicaRevisada.usuarioValidacaoAprovador.pessoa.nome",
				"analisesDocumentos.id",
				"analisesDocumentos.validado",
				"analisesDocumentos.parecer",
				"analisesDocumentos.documento.tipo.nome",
				"analisesDocumentos.documento.id",
				"analisesDocumentos.documento.tipo.tipoAnalise",
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
				"consultoresJuridicos.usuario.pessoa.nome",
				"parecerValidacao",
				"usuarioValidacao.pessoa.nome"
			);
	
	public static JSONSerializer parecer = SerializerUtil.create(
			
				"parecer"
			);

}
