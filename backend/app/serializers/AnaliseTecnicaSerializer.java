package serializers;

import com.vividsolutions.jts.geom.Geometry;

import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;
import flexjson.JSONSerializer;

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
			"analise.processo.empreendimento.municipio.limite",
			"analise.processo.empreendimento.imovel.codigo",
			"analise.processo.empreendimento.imovel.idCar",
			"analise.processo.empreendimento.imovel.nome",
			"analise.processo.empreendimento.imovel.municipio.nome",
			"analise.processo.empreendimento.imovel.municipio.estado.codigo",
			"analise.processo.caracterizacoes.id",
			"analise.processo.caracterizacoes.tipoLicenca.id",
			"analise.processo.caracterizacoes.tipoLicenca.nome",
			"analise.processo.caracterizacoes.documentosEnviados.id",
			"analise.processo.caracterizacoes.documentosEnviados.nome",
			"analise.processo.caracterizacoes.documentosEnviados.tipo.id",
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
			"analiseTecnicaRevisada.id",
			"analiseTecnicaRevisada.parecerValidacao",
			"analiseTecnicaRevisada.parecerValidacaoGerente",
			"analiseTecnicaRevisada.parecerValidacaoAprovador",
			"analiseTecnicaRevisada.usuarioValidacaoAprovador.id",
			"analiseTecnicaRevisada.usuarioValidacaoAprovador.pessoa.nome",
			"analisesDocumentos.id",
			"analisesDocumentos.validado",
			"analisesDocumentos.parecer",
			"analisesDocumentos.documento.id",
			"analisesDocumentos.documento.tipo.id",
			"analisesDocumentos.documento.tipo.nome",
			"analisesDocumentos.documento.tipo.tipoAnalise",
			"pareceresTecnicosRestricoes.id",
			"pareceresTecnicosRestricoes.codigoCamada",
			"pareceresTecnicosRestricoes.parecer",
			"parecer",
			"parecerValidacaoGerente",
			"usuarioValidacaoGerente",
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
			"licencasAnalise.id",
			"licencasAnalise.validade",
			"licencasAnalise.validadeMaxima",
			"licencasAnalise.observacao",
			"licencasAnalise.emitir",
			"licencasAnalise.caracterizacao.tipoLicenca.nome",			
			"licencasAnalise.caracterizacao.atividadeCaracterizacao.porteEmpreendimento.codigo",
			"licencasAnalise.caracterizacao.atividadeCaracterizacao.atividade.potencialPoluidor.codigo",
			"licencasAnalise.condicionantes.id",
			"licencasAnalise.condicionantes.texto",
			"licencasAnalise.condicionantes.prazo",
			"licencasAnalise.condicionantes.ordem",
			"licencasAnalise.recomendacoes.id",
			"licencasAnalise.recomendacoes.texto",
			"licencasAnalise.recomendacoes.ordem",
			"analistasTecnicos.id",
			"analistasTecnicos.usuario.id",
			"analistasTecnicos.usuario.pessoa.cpf",
			"analistasTecnicos.usuario.pessoa.id",
			"analistasTecnicos.usuario.pessoa.nome",
			"gerentesTecnicos.id",
			"gerentesTecnicos.usuario.id",
			"gerentesTecnicos.usuario.pessoa.cpf",
			"gerentesTecnicos.usuario.pessoa.id",
			"gerentesTecnicos.usuario.pessoa.nome",
			"tipoResultadoValidacaoGerente.id",
			"parecerValidacaoGerente",
			"analiseTecnicaRevisada",
			"usuarioValidacao.pessoa.nome"
			
		).transform(new GeometryTransformer(), Geometry.class);

		public static JSONSerializer parecer = SerializerUtil.create(
		
			"parecer"
		);	

}
