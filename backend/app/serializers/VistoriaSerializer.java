package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class VistoriaSerializer {

	public static JSONSerializer find = SerializerUtil.create(
			"id",
			"conclusao",
			"realizada",
			"documentoRit.id",
			"documentoRit.nomeDoArquivo",
			"inconsistenciaVistoria.descricaoInconsistencia",
			"inconsistenciaVistoria.tipoInconsistencia",
			"inconsistenciaVistoria.anexos.id",
			"inconsistenciaVistoria.anexos.nomeDoArquivo",
			"anexos.id",
			"anexos.nomeDoArquivo",
			"data",
			"hora",
			"descricao",
			"cursosDagua",
			"tipologiaVegetal",
			"app",
			"ocorrencia",
			"residuosLiquidos",
			"outrasInformacoes",
			"equipe.usuario.id",
			"equipe.usuario.pessoa.nome");

}
