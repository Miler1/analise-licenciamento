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
            "documentosNotificacaoTecnica.id",
            "documentosNotificacaoTecnica.caminho",
            "documentosNotificacaoTecnica.nomeDoArquivo",
            "documentos.id",
            "documentos.nomeDoArquivo",
            "justificativaDocumentacao",
            "justificativaRetificacaoEmpreendimento",
            "justificativaRetificacaoSolicitacao",
            "diasConclusao")
            .transform(GeometrySerializer.getTransformer(), Geometry.class)
            .exclude("*");

}
