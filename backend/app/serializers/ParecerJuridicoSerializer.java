package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ParecerJuridicoSerializer {

    public static JSONSerializer findParecerJuridico = SerializerUtil.create(

            "id",
            "analiseGeo.analise.processo.numero",
            "analiseGeo.analise.processo.empreendimento.pessoa.cpf",
            "analiseGeo.analise.processo.empreendimento.pessoa.cnpj",
            "analiseGeo.analise.processo.empreendimento.denominacao",
            "analiseGeo.analise.processo.empreendimento.municipio.nome",
            "analiseGeo.analise.processo.empreendimento.municipio.estado.codigo",
            "parecerAnalistaGeo.tipoResultadoAnalise.id",
            "parecerAnalistaGeo.documentoParecer.id",
            "parecerAnalistaGeo.documentoParecer.nomeDoArquivo",
            "parecerAnalistaGeo.documentoParecer.tipo.nome",
            "parecerAnalistaGeo.documentoParecer.responsavel",
            "parecerAnalistaGeo.cartaImagem.id",
            "parecerAnalistaGeo.cartaImagem.nomeDoArquivo",
            "parecerAnalistaGeo.cartaImagem.tipo.nome",
            "parecerAnalistaGeo.cartaImagem.responsavel",
            "ativo",
            "resolvido",
            "parecer",
            "dataVencimento",
            "anexos",
            "anexos.id",
            "anexos.nomeDoArquivo");
}