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
            "sobreposicaoCaracterizacaoEmpreendimento.id",
            "sobreposicaoCaracterizacaoEmpreendimento",
            "caracterizacao.id",
            "geometriaAtividade.id",
            "caracterizacao",
            "geometriaAtividade",
            "analiseGeo.id");
}
