package serializers;

import flexjson.JSONSerializer;

public class ProcessamentoShapeSerializer {

    public static JSONSerializer listResultado;

    static {

        listResultado = new JSONSerializer()
                .include(
                        "data.status",
                        "data.mensagens",
                        "data.dados.keyTemp",
                        "data.dados.atributos.tipo",
                        "data.dados.atributos.nome",
                        "data.dados.registros.valor",
                        "message"
                )
                .exclude("*");
    }
}
