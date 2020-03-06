package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

import java.util.Date;

public class ParecerAnalistaTecnicoSerializer {

	public static JSONSerializer findByIdHistoricoTramitacao = SerializerUtil.create(
			"tipoResultadoAnalise.id",
			"parecer"
	);

	public static JSONSerializer findByIdProcesso = SerializerUtil.create(
			"id",
			"tipoResultadoAnalise.id",
			"analiseTecnica.id",
			"doProcesso",
			"daAnaliseTecnica",
			"daConclusao",
			"parecer",
			"validadePermitida",
			"condicionantes.id",
			"condicionantes.texto",
			"condicionantes.prazo",
			"restricoes.id",
			"restricoes.texto",
			"finalidadeAtividade",
			"vistoria.id",
			"vistoria.conclusao",
			"vistoria.realizada",
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
			"vistoria.equipe.usuario.login",
			"vistoria.equipe.usuario.nome",
			"vistoria.equipe.usuario.pessoa.id",
			"vistoria.equipe.usuario.pessoa.nome",
			"vistoria.documentoRit.id",
			"vistoria.documentoRit.nomeDoArquivo",
			"vistoria.documentoRit.caminho",
			"vistoria.documentoRit.tipo.id",
			"vistoria.documentoRit.tipo.nome",
			"vistoria.inconsistenciaVistoria.id",
			"vistoria.inconsistenciaVistoria.descricaoInconsistencia",
			"vistoria.inconsistenciaVistoria.tipoInconsistencia",
			"vistoria.inconsistenciaVistoria.anexos.id",
			"vistoria.inconsistenciaVistoria.anexos.nomeDoArquivo",
			"vistoria.inconsistenciaVistoria.anexos.caminho",
			"vistoria.inconsistenciaVistoria.anexos.tipo.id",
			"vistoria.inconsistenciaVistoria.anexos.tipo.nome",
			"vistoria.anexos.id",
			"vistoria.anexos.nomeDoArquivo",
			"vistoria.anexos.caminho",
			"vistoria.anexos.tipo.id",
			"vistoria.anexos.tipo.nome",
			"vistoria.documentoRelatorioTecnicoVistoria.id",
			"vistoria.documentoRelatorioTecnicoVistoria.nomeDoArquivo",
			"vistoria.documentoRelatorioTecnicoVistoria.caminho",
			"vistoria.documentoRelatorioTecnicoVistoria.tipo.id",
			"vistoria.documentoRelatorioTecnicoVistoria.tipo.nome",
			"documentos.id",
			"documentos.nomeDoArquivo",
			"documentos.caminho",
			"documentos.tipo.id",
			"documentos.tipo.nome",
			"anexos.id",
			"anexos.nomeDoArquivo",
			"anexos.caminho",
			"anexos.tipo.id",
			"anexos.tipo.nome",
			"documentoMinuta.id",
			"documentoMinuta.nomeDoArquivo",
			"documentoMinuta.caminho",
			"documentoMinuta.tipo.id",
			"documentoMinuta.tipo.nome",
			"idHistoricoTramitacao")
			.transform(DateSerializer.getTransformerWithDateTime(), Date.class)
			.exclude("*");

}
