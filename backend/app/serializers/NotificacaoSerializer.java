package serializers;

import com.vividsolutions.jts.geom.Geometry;
import flexjson.JSONSerializer;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

public class NotificacaoSerializer {

    public static JSONSerializer findAll = SerializerUtil.create(
            "id",
            "codigoSequencia",
            "codigoAno",
            "justificativa",
            "dataNotificacao",
            "dataFinalNotificacao",
            "documentosParecer.id",
            "documentosParecer.nomeDoArquivo",
            "documentacao",
            "retificacaoEmpreendimento",
            "retificacaoSolicitacao",
            "retificacaoSolicitacaoComGeo",
            "prazoNotificacao",
            "documentos.id",
            "documentos.nomeDoArquivo")
            .transform(GeometrySerializer.getTransformer(), Geometry.class)
            .exclude("*");

}
