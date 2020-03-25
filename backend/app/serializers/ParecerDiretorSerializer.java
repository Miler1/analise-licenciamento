package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ParecerDiretorSerializer {

    public static JSONSerializer findByAnalise = SerializerUtil.create(
            "tipoResultadoAnalise.id",
            "tipoResultadoAnalise.nome",
            "parecer",
            "dataParecer"
    );
}
