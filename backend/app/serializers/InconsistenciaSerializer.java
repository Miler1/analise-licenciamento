package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class InconsistenciaSerializer {

    public static JSONSerializer findInconsistencia = SerializerUtil.create(

            "id",
            "tipoInconsistencia",
            "descricaoInconsistencia",
            "anexos.id",
            "anexos.nomeDoArquivo",
            "anexos.caminho",
            "anexos.tipo",
            "anexos.tipo.nome",
            "anexos.tipo.id",
            "categoria",
            "caracterizacao.id",
            "atividadeCaracterizacao.id",
            "atividadeCaracterizacao.atividade.nome",
            "sobreposicaoCaracterizacaoAtividade",
            "sobreposicaoCaracterizacaoAtividade.id",
            "sobreposicaoCaracterizacaoAtividade.tipoSobreposicao",
            "sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.orgaosResponsaveis",
            "sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.orgaosResponsaveis.sigla",
            "sobreposicaoCaracterizacaoEmpreendimento",
            "sobreposicaoCaracterizacaoEmpreendimento.id",
            "sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao",
            "sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.orgaosResponsaveis",
            "sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.orgaosResponsaveis.sigla",
            "sobreposicaoCaracterizacaoComplexo",
            "sobreposicaoCaracterizacaoComplexo.id",
            "sobreposicaoCaracterizacaoComplexo.tipoSobreposicao",
            "sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.orgaosResponsaveis",
            "sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.orgaosResponsaveis.sigla",
            "geometriaAtividade.id",
            "caracterizacao",
            "geometriaAtividade",
            "analiseGeo.id",
            "tipoDeInconsistenciaTecnica");

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
            "inconsistenciaTecnicaTipoLicenca.id",
            "inconsistenciaTecnicaTipoLicenca.tipoLicenca.id",
            "inconsistenciaTecnicaTipoLicenca.tipoLicenca.nome",
            "inconsistenciaTecnicaAtividade.id",
            "inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id",
            "inconsistenciaTecnicaAtividade.atividadeCaracterizacao.atividade.id",
            "inconsistenciaTecnicaAtividade.atividadeCaracterizacao.atividade.nome",
            "inconsistenciaTecnicaParametro.id",
            "inconsistenciaTecnicaParametro.parametroAtividade.nome",
            "inconsistenciaTecnicaParametro.parametroAtividade.codigo",
            "inconsistenciaTecnicaQuestionario.id",
            "inconsistenciaTecnicaQuestionario.questionario.id",
            "inconsistenciaTecnicaDocumentoAdministrativo.id",
            "inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo.id",
            "inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo.tipoDocumento.nome",
            "inconsistenciaTecnicaDocumentoTecnicoAmbiental.id",
            "inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.id",
            "inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.tipoDocumento.nome",
            "tipoDeInconsistenciaTecnica");

}
