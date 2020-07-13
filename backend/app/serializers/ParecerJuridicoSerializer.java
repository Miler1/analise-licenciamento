package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class ParecerJuridicoSerializer {

    public static JSONSerializer findParecerJuridico = SerializerUtil.create(

            "id",
            "analiseGeo.analise.processo.numero",
            "analiseGeo.analise.processo.empreendimento.cpfCnpj",
            "analiseGeo.analise.processo.empreendimento.empreendimentoEU.denominacao",
            "analiseGeo.analise.processo.empreendimento.municipio.nome",
            "analiseGeo.analise.processo.empreendimento.municipio.estado.codigo",
            "analiseGeo.analise.processo.empreendimento.empreendimentoEU.empreendedor.pessoa.nome",
            "analiseGeo.analise.processo.empreendimento.empreendimentoEU.empreendedor.pessoa.razaoSocial",
            "analiseTecnica.id",
            "parecerAnalistaGeo.tipoResultadoAnalise.id",
            "parecerAnalistaGeo.documentoParecer.id",
            "parecerAnalistaGeo.documentoParecer.nomeDoArquivo",
            "parecerAnalistaGeo.documentoParecer.tipo.nome",
            "parecerAnalistaGeo.documentoParecer.responsavel",
            "parecerAnalistaGeo.cartaImagem.id",
            "parecerAnalistaGeo.cartaImagem.nomeDoArquivo",
            "parecerAnalistaGeo.cartaImagem.tipo.nome",
            "parecerAnalistaGeo.cartaImagem.responsavel",
            "documentoFundiario.id",
            "documentoFundiario.tipo.nome",
            "ativo",
            "tipoResultadoAnalise.id",
            "tipoResultadoAnalise.nome",
            "resolvido",
            "parecer",
            "dataVencimento",
            "anexos",
            "anexos.id",
            "anexos.nomeDoArquivo");
}