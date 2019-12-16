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
}
