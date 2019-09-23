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
            "sobreposicaoCaracterizacaoAtividade",
            "sobreposicaoCaracterizacaoAtividade.id",
            "sobreposicaoCaracterizacaoEmpreendimento",
            "sobreposicaoCaracterizacaoEmpreendimento.id",
            "sobreposicaoCaracterizacaoComplexo",
            "sobreposicaoCaracterizacaoComplexo.id",
            "geometriaAtividade.id",
            "caracterizacao",
            "geometriaAtividade",
            "analiseGeo.id");
}
