package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class InconsistenciaTecnicaSerializer {

	public static JSONSerializer findInconsistenciaTecnica = SerializerUtil.create(
			"id",
			"tipoInconsistencia",
			"descricaoInconsistencia",
			"anexos.id",
			"anexos.nomeDoArquivo",
			"anexos.caminho",
			"anexos.tipo",
			"anexos.tipo.nome",
			"anexos.tipo.id",
			"analiseTecnica.id",
			"analiseTecnica.analise",
			"inconsistenciaTecnicaTipoLicenca.id",
			"inconsistenciaTecnicaTipoLicenca.tipoLicenca.id",
			"inconsistenciaTecnicaTipoLicenca.tipoLicenca.sigla",
			"inconsistenciaTecnicaAtividade.id",
			"inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id",
			"inconsistenciaTecnicaAtividade.atividadeCaracterizacao.atividade.id",
			"inconsistenciaTecnicaAtividade.atividadeCaracterizacao.atividade.nome",
			"inconsistenciaTecnicaParametro.id",
			"inconsistenciaTecnicaParametro.parametroAtividade.id",
			"inconsistenciaTecnicaParametro.parametroAtividade.nome",
			"inconsistenciaTecnicaParametro.parametroAtividade.codigo",
			"inconsistenciaTecnicaParametro.atividadeCaracterizacao.id",
			"inconsistenciaTecnicaParametro.atividadeCaracterizacao.atividade.id",
			"inconsistenciaTecnicaParametro.atividadeCaracterizacao.atividade.nome",
			"inconsistenciaTecnicaQuestionario.id",
			"inconsistenciaTecnicaQuestionario.questionario.id",
			"inconsistenciaTecnicaDocumentoAdministrativo.id",
			"inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo.id",
			"inconsistenciaTecnicaDocumentoTecnicoAmbiental.id",
			"inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.id",
			"inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.nome",
			"inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.documento.id",
			"tipoDeInconsistenciaTecnica");
}
