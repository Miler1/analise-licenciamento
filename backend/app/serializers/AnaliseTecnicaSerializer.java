package serializers;

import com.vividsolutions.jts.geom.Geometry;
import flexjson.JSONSerializer;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

public class AnaliseTecnicaSerializer {
	
	public static JSONSerializer findInfo = SerializerUtil.create(
			
			"id",
			"analistaTecnico.id",
			"analistaTecnico.usuario.id",
			"analistaTecnico.usuario.pessoa.nome",
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
			"analise.processo.caracterizacao.processoLicenciamento.id",
			"analise.processo.caracterizacao.processoLicenciamento.numero",
			"analise.processo.caracterizacao.questionario3.id",
			"analise.processo.caracterizacao.tipoLicenca.id",
			"analise.processo.caracterizacao.tipoLicenca.nome",
			"analise.processo.caracterizacao.tipoLicenca.validadeEmAnos",
			"analise.processo.caracterizacao.solicitacoesDocumento.id",
			"analise.processo.caracterizacao.solicitacoesDocumento.nome",
			"analise.processo.caracterizacao.solicitacoesDocumento.tipoDocumento.id",
			"analise.processo.caracterizacao.solicitacoesDocumento.tipoDocumento.nome",
			"analise.processo.caracterizacao.solicitacoesDocumento.tipoDocumento.tipoAnalise",
			"analise.processo.caracterizacao.solicitacoesDocumento.documento.id",
			"analise.processo.caracterizacao.documentosEnviados.id",
			"analise.processo.caracterizacao.documentosEnviados.nome",
			"analise.processo.caracterizacao.documentosEnviados.tipo.id",
			"analise.processo.caracterizacao.documentosEnviados.tipo.nome",
			"analise.processo.caracterizacao.documentosEnviados.tipo.tipoAnalise",
			"analise.processo.caracterizacao.documentosSolicitacaoGrupo.id",
			"analise.processo.caracterizacao.documentosSolicitacaoGrupo.documento.id",
			"analise.processo.caracterizacao.documentosSolicitacaoGrupo.tipoDocumento.id",
			"analise.processo.caracterizacao.documentosSolicitacaoGrupo.tipoDocumento.nome",
			"analise.processo.caracterizacao.documentosSolicitacaoGrupo.tipoDocumento.tipoAnalise",
			"analise.processo.caracterizacao.atividadesCaracterizacao.id",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividade.nome",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividadesCnae.nome",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividade.parametros.id",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividade.parametros.nome",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividade.parametros.codigo",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividade.tipologia.nome",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividade.tipologia.codigo",
			"analise.processo.caracterizacao.atividadesCaracterizacao.porteEmpreendimento.nome",
			"analise.processo.caracterizacao.atividadesCaracterizacao.atividadeCaracterizacaoParametros.valorParametro",
			"analise.processo.caracterizacao.vigenciaSolicitada",
			"inconsistenciasTecnica.id",
			"inconsistenciasTecnica.analiseTecnica.id",
			"inconsistenciasTecnica.analiseTecnica.analise",
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
			"inconsistenciasTecnica.inconsistenciaTecnicaParametro.atividadeCaracterizacao.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaParametro.atividadeCaracterizacao.atividade.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaParametro.atividadeCaracterizacao.atividade.nome",
			"inconsistenciasTecnica.inconsistenciaTecnicaParametro.valor",
			"inconsistenciasTecnica.inconsistenciaTecnicaQuestionario.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaQuestionario.questionario.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaDocumentoAdministrativo.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo.documento.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.tipoDocumento.id",
			"inconsistenciasTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.tipoDocumento.nome",
			"inconsistenciasTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.documento.id",
			"pareceresAnalistaTecnico.vistoria.documentoRelatorioTecnicoVistoria.id",
			"pareceresAnalistaTecnico.vistoria.documentoRelatorioTecnicoVistoria.nomeDoArquivo",
			"pareceresAnalistaTecnico.tipoResultadoAnalise.id",
			"pareceresAnalistaTecnico.tipoResultadoAnalise.nome",
			"pareceresAnalistaTecnico.analistaTecnico.pessoa.nome",
			"pareceresAnalistaTecnico.analiseTecnica.id",
			"pareceresAnalistaTecnico.doProcesso",
			"pareceresAnalistaTecnico.daConclusao",
			"pareceresAnalistaTecnico.daAnaliseTecnica",
			"pareceresAnalistaTecnico.parecer",
			"pareceresAnalistaTecnico.dataParecer",
			"pareceresAnalistaTecnico.vistoria.realizada",
			"pareceresAnalistaTecnico.documentos.id",
			"pareceresAnalistaTecnico.documentos.tipo.id",
			"pareceresAnalistaTecnico.documentos.nomeDoArquivo",
			"pareceresAnalistaTecnico.validadePermitida",
			"pareceresAnalistaTecnico.finalidadeAtividade",
			"pareceresAnalistaTecnico.documentos.arquivo",
			"pareceresAnalistaTecnico.documentoParecer.id",
			"pareceresAnalistaTecnico.documentoParecer.tipo.id",
			"pareceresAnalistaTecnico.documentoParecer.nomeDoArquivo",
			"pareceresAnalistaTecnico.documentoParecer.arquivo",
			"pareceresAnalistaTecnico.documentoMinuta.id",
			"pareceresAnalistaTecnico.documentoMinuta.tipo.id",
			"pareceresAnalistaTecnico.documentoMinuta.nomeDoArquivo",
			"pareceresAnalistaTecnico.documentoMinuta.arquivo",
			"pareceresAnalistaTecnico.dataParecer",
			"pareceresAnalistaTecnico.condicionantes.texto",
			"pareceresAnalistaTecnico.condicionantes.prazo",
			"pareceresAnalistaTecnico.restricoes.texto",
			"pareceresAnalistaTecnico.id",
			"pareceresGerenteAnaliseTecnica.tipoResultadoAnalise.id",
			"pareceresGerenteAnaliseTecnica.tipoResultadoAnalise.nome",
			"pareceresGerenteAnaliseTecnica.analiseTecnia.id",
			"pareceresGerenteAnaliseTecnica.dataParecer",
			"pareceresGerenteAnaliseTecnica.parecer",
			"pareceresGerenteAnaliseTecnica.usuario.id",
			"pareceresGerenteAnaliseTecnica.usuario.pessoa.cpf",
			"pareceresGerenteAnaliseTecnica.usuario.pessoa.nome",
			"pareceresGerenteAnaliseTecnica.usuario.pessoa.id",
			"analise.processo.numero",
			"analise.processo.empreendimento.id",
			"analise.processo.empreendimento.pessoa.id",
			"analise.processo.empreendimento.pessoa.nome",
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
			"documentos.arquivo",
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
			"usuarioValidacao.pessoa.nome",
			"vistoria.id",
			"vistoria.conclusao",
			"vistoria.realizada",
			"vistoria.documentoRit.id",
			"vistoria.documentoRit.nomeDoArquivo",
			"vistoria.inconsistenciaVistoria.descricaoInconsistencia",
			"vistoria.inconsistenciaVistoria.tipoInconsistencia",
			"vistoria.inconsistenciaVistoria.anexos.id",
			"vistoria.inconsistenciaVistoria.anexos.nomeDoArquivo",
			"vistoria.anexos.id",
			"vistoria.anexos.nomeDoArquivo",
			"vistoria.data",
			"vistoria.hora",
			"vistoria.descricao",
			"vistoria.cursosDagua",
			"vistoria.tipologiaVegetal",
			"vistoria.app",
			"vistoria.ocorrencia",
			"vistoria.residuosLiquidos",
			"vistoria.outrasInformacoes",
			"vistoria.equipe.usuario.id",
			"vistoria.equipe.usuario.pessoa.nome",
			"notificacoes.id",
			"notificacoes.analiseJuridica.id",
			"notificacoes.analiseTecnica.id",
			"notificacoes.analiseGeo.id",
			"notificacoes.tipoDocumento.tipoAnalise.id",
			"notificacoes.documentoCorrigido.id",
			"notificacoes.analiseDocumento.id",
			"notificacoes.codigoSequencia",
			"notificacoes.codigoAno",
			"notificacoes.justificativa",
			"notificacoes.dataNotificacao",
			"notificacoes.historicoTramitacao.idHistorico",
			"notificacoes.dataFinalNotificacao",
			"notificacoes.documentacao",
			"notificacoes.retificacaoEmpreendimento",
			"notificacoes.retificacaoSolicitacao",
			"notificacoes.retificacaoSolicitacaoComGeo",
			"notificacoes.documentos.id",
			"notificacoes.documentos.nomeDoArquivo",
			"notificacoes.prazoNotificacao",
			"notificacoes.justificativaDocumentacao",
			"notificacoes.justificativaRetificacaoEmpreendimento",
			"notificacoes.justificativaRetificacaoSolicitacao"
			
		).transform(new GeometryTransformer(), Geometry.class);

		public static JSONSerializer parecer = SerializerUtil.create(
		
			"parecer"
		);

}
