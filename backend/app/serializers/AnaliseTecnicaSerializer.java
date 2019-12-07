package serializers;

import com.vividsolutions.jts.geom.Geometry;
import flexjson.JSONSerializer;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

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
			"analise.processo.caracterizacao.id",
			"analise.processo.caracterizacao.tipoLicenca.id",
			"analise.processo.caracterizacao.tipoLicenca.nome",
			"analise.processo.caracterizacao.documentosEnviados.id",
			"analise.processo.caracterizacao.documentosEnviados.nome",
			"analise.processo.caracterizacao.documentosEnviados.tipo.id",
			"analise.processo.caracterizacao.documentosEnviados.tipo.nome",
			"analise.processo.caracterizacao.documentosEnviados.tipo.tipoAnalise",
			"analise.processo.caracterizacao.atividadesCaracterizacao.id",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividade.nome",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividadesCnae.nome",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividade.parametros.id",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividade.parametros.nome",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividade.parametros.codigo",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividadeCaracterizacaoParametros.valorParametro",
			"inconsistenciasTecnica.id",
			"inconsistenciasTecnica.tipoDeInconsistenciaTecnica",
			"inconsistenciasTecnica.tipoInconsistencia",
			"inconsistenciasTecnica.descricaoInconsistencia",
			"inconsistenciasTecnica.anexos.id",
			"inconsistenciasTecnica.anexos.nomeDoArquivo",
			"inconsistenciasTecnica.anexos.caminho",
			"inconsistenciasTecnica.anexos.tipo",
			"inconsistenciasTecnica.anexos.tipo.nome",
			"inconsistenciasTecnica.anexos.tipo.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaTipoLicenca.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaTipoLicenca.tipoLicenca",
			"inconsistenciasTecnica.inconsistenciaTecnicaAtividade.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaParametro.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaParametro.parametroAtividade.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaParametro.parametroAtividade.nome",
			"inconsistenciasTecnica.inconsistenciaTecnicaParametro.parametroAtividade.codigo",
			"inconsistenciasTecnica.inconsistenciaTecnicaParametro.valor",
//			"inconsistenciasTecnica.inconsistenciaTecnicaQuestionario",
			"inconsistenciasTecnica.inconsistenciaTecnicaDocumento.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaDocumento.documento.id",
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
			"analisesDocumentos.notificacao.justificativa",
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
			"gerentes.id",
			"gerentes.usuario.id",
			"gerentes.usuario.pessoa.cpf",
			"gerentes.usuario.pessoa.id",
			"gerentes.usuario.pessoa.nome",
			"tipoResultadoValidacaoGerente.id",
			"parecerValidacaoGerente",
			"analiseTecnicaRevisada",
			"usuarioValidacao.pessoa.nome"
			
		).transform(new GeometryTransformer(), Geometry.class);

		public static JSONSerializer parecer = SerializerUtil.create(
		
			"parecer"
		);

}
