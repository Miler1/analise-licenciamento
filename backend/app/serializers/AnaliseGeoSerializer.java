package serializers;

import com.vividsolutions.jts.geom.Geometry;
import flexjson.JSONSerializer;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

import java.util.Date;

public class AnaliseGeoSerializer {

    public static JSONSerializer findInfo = SerializerUtil.create(
            "id",
            "analise.id",
            "analise.dataVencimentoPrazo",
            "analise.processo.id",
            "analise.processo.numero",
            "analise.processo.empreendimento.pessoa.cpf",
            "analise.processo.empreendimento.pessoa.cnpj",
            "analise.processo.empreendimento.denominacao",
            "analise.processo.empreendimento.municipio.nome",
            "analise.processo.empreendimento.municipio.estado.codigo",
            "analise.processo.empreendimento.municipio.limite",
            "analise.processo.empreendimento",
            "analise.processo.empreendimento.imovel.codigo",
            "analise.processo.empreendimento.imovel.idCar",
            "analise.processo.empreendimento.imovel.nome",
            "analise.processo.empreendimento.imovel.municipio.nome",
            "analise.processo.empreendimento.imovel.municipio.estado.codigo",
            "analise.processo.caracterizacao.id",
            "analise.processo.caracterizacao.processoLicenciamento.id",
            "analise.processo.caracterizacao.processoLicenciamento.numero",
            "analise.processo.caracterizacao.tipoLicenca.id",
            "analise.processo.caracterizacao.tipoLicenca.nome",
            "analise.processo.caracterizacao.documentosEnviados.id",
            "analise.processo.caracterizacao.documentosEnviados.nome",
            "analise.processo.caracterizacao.documentosEnviados.tipo.id",
            "analise.processo.caracterizacao.documentosEnviados.tipo.nome",
            "analise.processo.caracterizacao.documentosEnviados.tipo.tipoAnalise",
            "analise.processo.caracterizacao.atividadesCaracterizacao.atividade.nome",
            "analise.processo.caracterizacao.atividadesCaracterizacao.atividade.parametros.codigo",
            "analise.processo.caracterizacao.atividadesCaracterizacao.atividade.parametros.nome",
            "analise.processo.caracterizacao.atividadesCaracterizacao.atividadeCaracterizacaoParametros.valorParametro",
            "analise.processo.caracterizacao.atividadesCaracterizacao.sobreposicaoCaracterizacaoAtividade.",
            "analise.processo.caracterizacao.atividadesCaracterizacao.sobreposicaoCaracterizacaoAtividade.id",
            "analise.processo.numero",
            "analise.processo.empreendimento.id",
            "analise.processo.empreendimento.denominacao",
            "analise.processo.empreendimento.pessoa.id",
            "analise.processo.empreendimento.pessoa.nome",
            "analise.processo.empreendimento.pessoa.cpf",
            "analise.processo.empreendimento.municipio.nome",
            "analise.processo.empreendimento.municipio.estado.codigo",
            "analiseGeoRevisada.id",
            "analiseGeoRevisada.parecerValidacao",
            "analiseGeoRevisada.parecerValidacaoGerente",
            "analiseGeoRevisada.parecerValidacaoAprovador",
            "analiseGeoRevisada.usuarioValidacaoAprovador.id",
            "analiseGeoRevisada.usuarioValidacaoAprovador.pessoa.nome",
            "analisesDocumentos.id",
            "analisesDocumentos.validado",
            "analisesDocumentos.parecer",
            "analisesDocumentos.notificacao.justificativa",
            "analisesDocumentos.documento.id",
            "analisesDocumentos.documento.tipo.id",
            "analisesDocumentos.documento.tipo.nome",
            "analisesDocumentos.documento.tipo.tipoAnalise",
            "pareceresGeoRestricoes.id",
            "pareceresGeoRestricoes.codigoCamada",
            "pareceresGeoRestricoes.parecer",
            "parecer",
            "situacaoFundiaria",
            "analiseTemporal",
            "parecerValidacaoGerente",
            "despacho",
            "dataVencimentoPrazo",
            "revisaoSolicitada",
            "ativo",
            "dataInicio",
            "dataFim",
            "tipoResultadoAnalise",
            "tipoResultadoAnalise.id",
            "tipoResultadoAnalise.nome"	,
            "documentos.id",
            "documentos.nome",
            "documentos.tipo.id",
            "documentos.tipo.nome",
            "documentos.nomeDoArquivo",
            "licencasAnalise.id",
            "licencasAnalise.validade",
            "licencasAnalise.validadeMaxima",
            "licencasAnalise.observacao",
            "licencasAnalise.emitir",
            "licencasAnalise.caracterizacao.tipoLicenca.nome",
            "licencasAnalise.caracterizacao.atividadeCaracterizacao.porteEmpreendimento.codigo",
            "licencasAnalise.caracterizacao.atividadeCaracterizacao.atividade.potencialPoluidor.codigo",
            "licencasAnalise.condicionantes.id",
            "licencasAnalise.condicionantes.texto",
            "licencasAnalise.condicionantes.prazo",
            "licencasAnalise.condicionantes.ordem",
            "licencasAnalise.recomendacoes.id",
            "licencasAnalise.recomendacoes.texto",
            "licencasAnalise.recomendacoes.ordem",
            "analistasGeo.id",
            "analistasGeo.usuario.id",
            "analistasGeo.usuario.pessoa.cpf",
            "analistasGeo.usuario.pessoa.id",
            "analistasGeo.usuario.pessoa.nome",
            "gerentesGeo.id",
            "gerentesGeo.usuario.id",
            "gerentesGeo.usuario.pessoa.cpf",
            "gerentesGeo.usuario.pessoa.id",
            "gerentesGeo.usuario.pessoa.nome",
            "pareceresAnalistaGeo.situacaoFundiaria",
            "pareceresAnalistaGeo.analiseTemporal",
            "pareceresAnalistaGeo.conclusao",
            "pareceresAnalistaGeo.dataParecer",
            "pareceresAnalistaGeo.tipoResultadoAnalise.id",
            "pareceresAnalistaGeo.tipoResultadoAnalise.nome",
            "pareceresAnalistaGeo.parecer",
            "pareceresAnalistaGeo.usuario.pessoa.nome",
            "pareceresAnalistaGeo.documentos.id",
            "pareceresAnalistaGeo.documentos.caminho",
            "pareceresAnalistaGeo.documentos.tipo.id",
            "pareceresAnalistaGeo.documentos.tipo.nome",
            "pareceresAnalistaGeo.documentos.nomeDoArquivo",
            "pareceresAnalistaGeo.analiseGeo.analistasGeo.usuario.pessoa.nome",
            "pareceresAnalistaGeo.documentoParecer.id",
            "pareceresAnalistaGeo.documentoParecer.caminho",
            "pareceresAnalistaGeo.documentoParecer.tipo.id",
            "pareceresAnalistaGeo.documentoParecer.tipo.nome",
            "pareceresAnalistaGeo.documentoParecer.nomeDoArquivo",
            "pareceresAnalistaGeo.cartaImagem.id",
            "pareceresAnalistaGeo.cartaImagem.caminho",
            "pareceresAnalistaGeo.cartaImagem.tipo.id",
            "pareceresAnalistaGeo.cartaImagem.tipo.nome",
            "pareceresAnalistaGeo.cartaImagem.nomeDoArquivo",
            "pareceresGerenteAnaliseGeo.tipoResultadoAnalise.id",
            "pareceresGerenteAnaliseGeo.tipoResultadoAnalise.nome",
            "pareceresGerenteAnaliseGeo.parecer",
            "pareceresGerenteAnaliseGeo.usuario.id",
            "pareceresGerenteAnaliseGeo.usuario.pessoa.cpf",
            "pareceresGerenteAnaliseGeo.usuario.pessoa.id",
            "pareceresGerenteAnaliseGeo.usuario.pessoa.nome",
            "pareceresGerenteAnaliseGeo.documentos.id",
            "pareceresGerenteAnaliseGeo.documentos.caminho",
            "pareceresGerenteAnaliseGeo.documentos.tipo.id",
            "pareceresGerenteAnaliseGeo.documentos.tipo.nome",
            "pareceresGerenteAnaliseGeo.documentos.nomeDoArquivo",
            "analiseGeoRevisada",
            "usuarioValidacao.pessoa.nome",
            "inconsistencias.id",
            "inconsistencias.tipoInconsistencia",
            "inconsistencias.descricaoInconsistencia",
            "inconsistencias.anexos.id",
            "inconsistencias.anexos.nomeDoArquivo",
            "inconsistencias.anexos.caminho",
            "inconsistencias.anexos.tipo",
            "inconsistencias.anexos.tipo.nome",
            "inconsistencias.anexos.tipo.id",
            "inconsistencias.categoria",
            "inconsistencias.caracterizacao.id",
            "inconsistencias.atividadeCaracterizacao",
            "inconsistencias.atividadeCaracterizacao.id",
            "inconsistencias.atividadeCaracterizacao.atividade.nome",
            "inconsistencias.sobreposicaoCaracterizacaoEmpreendimento.id",
            "inconsistencias.sobreposicaoCaracterizacaoComplexo",
            "inconsistencias.sobreposicaoCaracterizacaoComplexo.id",
            "inconsistencias.geometriaAtividade.id",
            "inconsistencias.sobreposicaoCaracterizacaoAtividade.id",
            "notificacoes.id",
            "notificacoes.analiseJuridica.id",
            "notificacoes.analiseTecnica.id",
            "notificacoes.analiseGeo.id",
            "notificacoes.tipoDocumento.tipoAnalise.id",
            "notificacoes.documentoCorrigido.id",
            "notificacoes.analiseDocumento.id",
            "notificacoes.codigoSequencia",
            "notificacoes.codigoAno",
            "notificacoes.justificativa",
            "notificacoes.dataNotificacao",
            "notificacoes.historicoTramitacao.idHistorico",
            "notificacoes.dataFinalNotificacao",
            "notificacoes.documentacao",
            "notificacoes.retificacaoEmpreendimento",
            "notificacoes.retificacaoSolicitacao",
            "notificacoes.retificacaoSolicitacaoComGeo",
            "notificacoes.documentos.id",
            "notificacoes.documentos.nomeDoArquivo",
            "notificacoes.prazoNotificacao",
            "notificacoes.justificativaDocumentacao",
            "notificacoes.justificativaRetificacaoEmpreendimento",
            "notificacoes.justificativaRetificacaoSolicitacao",
            "desvinculos.id",
            "desvinculos.analiseGeo",
            "desvinculos.justificativa",
            "desvinculos.respostaGerente"
    ).transform(new GeometryTransformer(), Geometry.class)
     .transform(DateSerializer.getTransformerWithDateTime(), Date.class);

    public static JSONSerializer parecer = SerializerUtil.create(

            "parecer",
            "situacaoFundiaria",
            "analiseTemporal"
    );

}
