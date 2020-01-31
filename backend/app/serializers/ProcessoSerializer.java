package serializers;

import com.vividsolutions.jts.geom.Geometry;
import flexjson.JSONSerializer;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

import java.util.Date;

public class ProcessoSerializer {
	
	public static JSONSerializer list = SerializerUtil.create(
			"id",
			"numero",
			"empreendimento.municipio.nome",
			"empreendimento.municipio.estado.codigo",
			"empreendimento.pessoa.cpf",
			"empreendimento.pessoa.cnpj",
			"objetoTramitavel",
			"empreendimento.denominacao");

	public static JSONSerializer getInfo = SerializerUtil.create(
			"id",
			"numero",
			"tiposLicenca",
			"idObjetoTramitavel",
			"renovacao",
			"analise.id",
			"analise.dataCadastro",
			"analise.dataVencimentoPrazo",
			"analise.diasAnalise.qtdeDiasGeo",
			"analise.diasAnalise.qtdeDiasTecnica",
			"analise.diasAnalise.qtdeDiasAnalise",
			"analise.analiseGeo.id",
			"analise.analiseGeo.pareceresGerenteAnaliseGeo.id",
			"analise.analiseGeo.pareceresGerenteAnaliseGeo.tipoResultadoAnalise.id",
			"analise.analiseGeo.pareceresGerenteAnaliseGeo.tipoResultadoAnalise.nome",
			"analise.analiseGeo.pareceresGerenteAnaliseGeo.parecer",
			"analise.analiseGeo.pareceresGerenteAnaliseGeo.usuario.pessoa.nome",
			"analise.analiseGeo.pareceresGerenteAnaliseGeo.dataParecer",
			"analise.analiseTecnica.id",
			"analise.analiseTecnica.pareceresGerenteAnaliseTecnica.id",
			"analise.analiseTecnica.pareceresGerenteAnaliseTecnica.tipoResultadoAnalise.id",
			"analise.analiseTecnica.pareceresGerenteAnaliseTecnica.tipoResultadoAnalise.nome",
			"analise.analiseTecnica.pareceresGerenteAnaliseTecnica.parecer",
			"analise.analiseTecnica.pareceresGerenteAnaliseTecnica.usuario.pessoa.nome",
			"analise.analiseTecnica.pareceresGerenteAnaliseTecnica.dataParecer",
			"analise.analiseTecnica.pareceresAnalistaTecnico.id",
			"analise.analiseTecnica.pareceresAnalistaTecnico.tipoResultadoAnalise.id",
			"analise.analiseTecnica.pareceresAnalistaTecnico.tipoResultadoAnalise.nome",
			"analise.analiseTecnica.pareceresAnalistaTecnico.parecer",
			"analise.analiseTecnica.pareceresAnalistaTecnico.analistaTecnico.pessoa.nome",
			"analise.analiseTecnica.pareceresAnalistaTecnico.dataParecer",
			"analise.analiseTecnica.pareceresAnalistaTecnico.documentoParecer.id",
			"analise.analiseTecnica.pareceresAnalistaTecnico.documentoParecer.nomeDoArquivo",
			"analise.analiseTecnica.pareceresAnalistaTecnico.documentoParecer.tipo.nome",
			"analise.analiseGeo.pareceresAnalistaGeo.id",
			"analise.analiseGeo.pareceresAnalistaGeo.tipoResultadoAnalise.id",
			"analise.analiseGeo.pareceresAnalistaGeo.tipoResultadoAnalise.nome",
			"analise.analiseGeo.pareceresAnalistaGeo.parecer",
			"analise.analiseGeo.pareceresAnalistaGeo.usuario.pessoa.nome",
			"analise.analiseGeo.pareceresAnalistaGeo.dataParecer",
			"analise.analiseJuridica.id",
			"analise.analisesJuridica.dataVencimentoPrazo",
			"analise.analisesJuridica.dataInicio",
			"analise.analisesJuridica.dataFim",
			"analise.analisesGeo.dataVencimentoPrazo",
			"analise.analisesGeo.dataInicio",
			"analise.analisesGeo.dataFim",
			"analise.analisesTecnicas.dataCadastro",
			"analise.analisesTecnicas.justificativaCoordenador",
			"analise.analisesTecnicas.dataFim",
			"analise.analisesTecnicas.dataVencimentoPrazo",
			"historicoTramitacao.idHistorico",
			"historicoTramitacao.dataInicial",
			"historicoTramitacao.dataFinal",
			"historicoTramitacao.nomeUsuarioDestino",
			"historicoTramitacao.nomeUsuarioExecutor",
			"historicoTramitacao.idAcao",
			"historicoTramitacao.nomeAcao",
			"historicoTramitacao.idCondicaoInicial",
			"historicoTramitacao.nomeCondicaoInicial",
			"historicoTramitacao.idCondicaoFinal",
			"historicoTramitacao.nomeCondicaoFinal",
			"historicoTramitacao.tempoPermanencia",
			"historicoTramitacao.hasNotificacoes",
			"historicoTramitacao.documentosCorrigidos.id",
			"historicoTramitacao.documentosCorrigidos.tipo.nome",
			"historicoTramitacao.setor.nome",
			"historicoTramitacao.setor.sigla",
			"historicoTramitacaoAnterior.idHistorico",
			"historicoTramitacaoAnterior.dataInicial",
			"historicoTramitacaoAnterior.dataFinal",
			"historicoTramitacaoAnterior.nomeUsuarioDestino",
			"historicoTramitacaoAnterior.nomeUsuarioExecutor",
			"historicoTramitacaoAnterior.idAcao",
			"historicoTramitacaoAnterior.nomeAcao",
			"historicoTramitacaoAnterior.idCondicaoInicial",
			"historicoTramitacaoAnterior.nomeCondicaoInicial",
			"historicoTramitacaoAnterior.idCondicaoFinal",
			"historicoTramitacaoAnterior.nomeCondicaoFinal",
			"historicoTramitacaoAnterior.tempoPermanencia",
			"historicoTramitacaoAnterior.hasNotificacoes",
			"historicoTramitacaoAnterior.documentosCorrigidos.id",
			"historicoTramitacaoAnterior.documentosCorrigidos.tipo.nome",
			"historicoTramitacaoAnterior.setor.nome",
			"historicoTramitacaoAnterior.relHistoricoTramitacaoSetor.siglaSetor",
			"empreendimento.id",
			"empreendimento.denominacao",
			"empreendimento.jurisdicao",
			"empreendimento.municipio.nome",
			"empreendimento.municipio.estado.codigo",
			"empreendimento.municipio.id",
			"empreendimento.municipio.limite",
			"empreendimento.contato.email",
			"empreendimento.contato.telefone",
			"empreendimento.contato.celular",
			"empreendimento.enderecoCorrespondencia.logradouro",
			"empreendimento.enderecoCorrespondencia.numero",
			"empreendimento.enderecoCorrespondencia.cep",
			"empreendimento.enderecoCorrespondencia.complemento",
			"empreendimento.enderecoCorrespondencia.bairro",
			"empreendimento.enderecoCorrespondencia.correspondencia",
			"empreendimento.enderecoCorrespondencia.caixaPostal",
			"empreendimento.enderecoCorrespondencia.municipio.nome",
			"empreendimento.enderecoCorrespondencia.municipio.estado.codigo",
			"empreendimento.enderecoCorrespondencia.roteiroAcesso",
			"empreendimento.enderecoCorrespondencia.tipo",
			"empreendimento.proprietarios.pessoa.cpf",
			"empreendimento.proprietarios.pessoa.nome",
			"empreendimento.proprietarios.pessoa.dataNascimento",
			"empreendimento.proprietarios.pessoa.rg",
			"empreendimento.localizacao",
			"empreendimento.imovel.codigo",
			"empreendimento.imovel.idCar",
			"empreendimento.imovel.nome",
			"empreendimento.imovel.limite",
			"empreendimento.imovel.municipio.nome",
			"empreendimento.imovel.municipio.estado.nome",
			"empreendimento.coordenadas",
			"empreendimento.area",
			"empreendimento.responsaveis.pessoa.cpf",
			"empreendimento.responsaveis.pessoa.nome",
			"empreendimento.responsaveis.contato.email",
			"empreendimento.responsaveis.contato.celular",
			"empreendimento.pessoa.type",
			"empreendimento.pessoa.cpf",
			"empreendimento.pessoa.nome",
			"empreendimento.pessoa.nomeMae",
			"empreendimento.pessoa.rg",
			"empreendimento.pessoa.dataNascimento",
			"empreendimento.pessoa.sexo",
			"empreendimento.pessoa.estadoCivil.nome",
			"empreendimento.pessoa.tituloEleitor",
			"empreendimento.pessoa.cnpj",
			"empreendimento.pessoa.razaoSocial",
			"empreendimento.pessoa.nomeFantasia",
			"empreendimento.pessoa.municipio.nome",
			"empreendimento.pessoa.municipio.estado.codigo",
			"empreendimento.pessoa.inscricaoEstadual",
			"empreendimento.pessoa.dataFundacao",
			"empreendimento.empreendedor.esfera",
			"empreendimento.empreendedor.representantesLegais.pessoa.cpf",
			"empreendimento.empreendedor.representantesLegais.pessoa.nome",
			"empreendimento.empreendedor.representantesLegais.pessoa.contato.email",
			"empreendimento.empreendedor.representantesLegais.dataVinculacao",
			"empreendimento.empreendedor.pessoa.type",
			"empreendimento.empreendedor.pessoa.cpf",
			"empreendimento.empreendedor.pessoa.nome",
			"empreendimento.empreendedor.pessoa.nomeMae",
			"empreendimento.empreendedor.pessoa.rg",
			"empreendimento.empreendedor.pessoa.dataNascimento",
			"empreendimento.empreendedor.pessoa.sexo",
			"empreendimento.empreendedor.pessoa.estadoCivil.nome",
			"empreendimento.empreendedor.pessoa.tituloEleitor",
			"empreendimento.empreendedor.pessoa.cnpj",
			"empreendimento.empreendedor.pessoa.dataFundacao",
			"empreendimento.empreendedor.pessoa.razaoSocial",
			"empreendimento.empreendedor.pessoa.nomeFantasia",
			"empreendimento.empreendedor.pessoa.municipio.nome",
			"empreendimento.empreendedor.pessoa.municipio.estado.codigo",
			"empreendimento.empreendedor.pessoa.inscricaoEstadual",
			"empreendimento.empreendedor.pessoa.contato.email",
			"empreendimento.empreendedor.pessoa.contato.telefone",
			"empreendimento.empreendedor.pessoa.contato.celular",
			"empreendimento.empreendedor.pessoa.enderecos.logradouro",
			"empreendimento.empreendedor.pessoa.enderecos.numero",
			"empreendimento.empreendedor.pessoa.enderecos.complemento",
			"empreendimento.empreendedor.pessoa.enderecos.bairro",
			"empreendimento.empreendedor.pessoa.enderecos.correspondencia",
			"empreendimento.empreendedor.pessoa.enderecos.cep",
			"empreendimento.empreendedor.pessoa.enderecos.caixaPostal",
			"empreendimento.empreendedor.pessoa.enderecos.municipio.nome",
			"empreendimento.empreendedor.pessoa.enderecos.municipio.estado.codigo",
			"empreendimento.empreendedor.pessoa.enderecos.roteiroAcesso",
			"empreendimento.empreendedor.pessoa.enderecos.tipo",
			"objetoTramitavel.condicao.idCondicao",
			"caracterizacao.documentosEnviados.id",
			"caracterizacao.documentosEnviados.nome",
			"caracterizacao.documentosEnviados.tipo.id",
			"caracterizacao.documentosEnviados.tipo.nome",
			"caracterizacao.documentosSolicitacaoGrupo.id",
			"caracterizacao.documentosSolicitacaoGrupo.documento.id",
			"caracterizacao.documentosSolicitacaoGrupo.tipoDocumento.id",
			"caracterizacao.documentosSolicitacaoGrupo.tipoDocumento.nome",
			"caracterizacao.tipo.nome",
			"caracterizacao.tipoLicenca.nome",
			"caracterizacao.licencas.dataValidade",
			"caracterizacao.atividadesCaracterizacao.atividade",
			"caracterizacao.atividadesCaracterizacao.atividade.potencialPoluidor",
			"caracterizacao.atividadesCaracterizacao.atividade.potencialPoluidor.nome",
			"caracterizacao.atividadesCaracterizacao.atividade.potencialPoluidor.codigo",
			"caracterizacao.atividadesCaracterizacao.atividade.nome",
			"caracterizacao.atividadesCaracterizacao.atividade.tipologia.nome",
			"caracterizacao.atividadesCaracterizacao.atividadesCnae",
			"caracterizacao.atividadesCaracterizacao.atividadesCnae.nome",
			"caracterizacao.atividadesCaracterizacao.atividadesCnae.codigo",
			"caracterizacao.atividadesCaracterizacao.atividadeCaracterizacaoParametros.valorParametro",
			"caracterizacao.atividadesCaracterizacao.porteEmpreendimento.codigo",
			"caracterizacao.atividadesCaracterizacao.porteEmpreendimento.nome",
			"caracterizacao.atividadesCaracterizacao.atividade.parametros.nome",
			"caracterizacao.atividadesCaracterizacao.atividade.parametros.codigo",
			"caracterizacao.atividadeCaracterizacaoMaiorPotencialPoluidorEPorte.atividade.potencialPoluidor.nome",
			"caracterizacao.atividadeCaracterizacaoMaiorPotencialPoluidorEPorte.porteEmpreendimento.nome")
	        .transform(GeometrySerializer.getTransformer(), Geometry.class)
			.transform(DateSerializer.getTransformerWithDateTime(), Date.class)
			.exclude("*");

}
