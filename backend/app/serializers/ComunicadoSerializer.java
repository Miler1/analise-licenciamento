package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ComunicadoSerializer {

    public static JSONSerializer findComunicado = SerializerUtil.create(

            "id",
            "analiseGeo.analise.processo.numero",
            "analiseGeo.analise.processo.empreendimento.pessoa.cpf",
            "analiseGeo.analise.processo.empreendimento.pessoa.cnpj",
            "analiseGeo.analise.processo.empreendimento.denominacao",
            "analiseGeo.analise.processo.empreendimento.municipio.nome",
            "analiseGeo.analise.processo.empreendimento.municipio.estado.codigo",
            "atividadeCaracterizacao.id",
            "tipoSobreposicao.nome",
            "ativo",
            "resolvido",
            "dataVencimento",
            "valido");
}
