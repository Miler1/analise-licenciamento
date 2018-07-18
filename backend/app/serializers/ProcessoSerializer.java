package serializers;

import java.util.Date;

import com.vividsolutions.jts.geom.Geometry;

import flexjson.JSONSerializer;
import utils.SerializerUtil;
import utils.flexjson.GeometryTransformer;

public class ProcessoSerializer {
	
	public static JSONSerializer list = SerializerUtil.create(
			"id",
			"numero",
			"empreendimento.municipio.nome",
			"empreendimento.municipio.estado.codigo",
			"empreendimento.pessoa.cpf",
			"empreendimento.pessoa.cnpj",
			"empreendimento.denominacao");

	public static JSONSerializer getInfo = SerializerUtil.create(
			"id",
			"numero",
			"tiposLicenca",
			"analise.id",
			"analise.dataCadastro",
			"analise.dataVencimentoPrazo",
			"analise.analiseJuridica.dataVencimentoPrazo",
			"analise.analiseJuridica.dataInicio",
			"analise.analiseJuridica.dataFim",
			"analise.analiseTecnica.dataCadastro",
			"analise.analiseTecnica.justificativaCoordenador",
			"analise.analiseTecnica.dataFim",
			"analise.analiseTecnica.dataVencimentoPrazo",
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
			"empreendimento.responsaveis.pessoa.cpf",
			"empreendimento.responsaveis.pessoa.nome",
			"empreendimento.responsaveis.contato.email",
			"empreendimento.responsaveis.contato.celular",
			"empreendimento.pessoa.type",
			"empreendimento.pessoa.cpf",
			"empreendimento.pessoa.nome",
			"empreendimento.pessoa.nomeMae",
			"empreendimento.pessoa.rg",
			"empreendimento.pessoa.municipioNascimento.nome",
			"empreendimento.pessoa.municipioNascimento.estado.codigo",
			"empreendimento.pessoa.dataNascimento",
			"empreendimento.pessoa.sexo",
			"empreendimento.pessoa.estadoCivil.nome",
			"empreendimento.pessoa.tituloEleitor",
			"empreendimento.pessoa.cnpj",
			"empreendimento.pessoa.razaoSocial",
			"empreendimento.pessoa.nomeFantasia",
			"empreendimento.pessoa.municipio.nome",
			"empreendimento.pessoa.municipio.estado.codigo",
			"empreendimento.pessoa.isento",
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
			"empreendimento.empreendedor.pessoa.municipioNascimento.nome",
			"empreendimento.empreendedor.pessoa.municipioNascimento.estado.codigo",
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
			"empreendimento.empreendedor.pessoa.isento",
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
			"caracterizacao.documentosEnviados.id",
			"caracterizacao.documentosEnviados.nome",
			"caracterizacao.documentosEnviados.tipo.id",
			"caracterizacao.documentosEnviados.tipo.nome",
			"caracterizacao.tipo.nome",
			"caracterizacao.atividadesCaracterizacao.atividade",
			"caracterizacao.atividadesCaracterizacao.atividade.nome",
			"caracterizacao.atividadesCaracterizacao.atividade.tipologia.nome",
			"caracterizacao.atividadesCaracterizacao.atividadesCnae",
			"caracterizacao.atividadesCaracterizacao.atividadesCnae.nome",
			"caracterizacao.atividadesCaracterizacao.atividadesCnae.codigo",
			"caracterizacao.atividadesCaracterizacao.valorParametro",
			"caracterizacao.atividadesCaracterizacao.porteEmpreendimento.codigo",			
			"caracterizacao.atividadesCaracterizacao.atividade.parametro.nome",
			"caracterizacao.atividadesCaracterizacao.atividade.parametro.unidade",
			"caracterizacao.atividadesCaracterizacao.atividade.parametro.codigo",
			"caracterizacao.atividadesCaracterizacao.atividade.potencialPoluidor.codigo")
			.transform(new GeometryTransformer(), Geometry.class);

}
