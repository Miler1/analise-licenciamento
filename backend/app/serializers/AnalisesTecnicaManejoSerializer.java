package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class AnalisesTecnicaManejoSerializer {

	public static JSONSerializer findById = SerializerUtil.createWithDoubleTransformer(
			"id",
			"dataAnalise",
			"pathAnexo",
			"areaManejoFlorestalSolicitada",
			"areaPreservacaoPermanente",
			"areaServidao",
			"areaAntropizadaNaoConsolidada",
			"areaUsoRestrito",
			"areaSemPotencial",
			"areaCorposAgua",
			"areaEmbargadaIbama",
			"areaEmbargadaLdi",
			"areaSeletivaNdfi",
			"areaEfetivoNdfi",
			"areaExploracaoNdfiBaixo",
			"areaExploracaoNdfiMedio",
			"areaSemPreviaExploracao",
			"areaConsolidada",
			"processoManejo.numeroProcesso",
			"processoManejo.empreendimento.cpfCnpj",
			"processoManejo.empreendimento.denominacao",
			"processoManejo.empreendimento.municipio.nome",
			"analisesNdfi.id",
			"analisesNdfi.dataAnalise",
			"analisesNdfi.orbita",
			"analisesNdfi.ponto",
			"analisesNdfi.satelite",
			"analisesNdfi.nivelExploracao",
			"analisesNdfi.valor",
			"analisesNdfi.area",
			"analisesNdfi.exibirPDF",
			"analisesVetorial.id",
			"analisesVetorial.tipo",
			"analisesVetorial.nome",
			"analisesVetorial.distanciaPropriedade",
			"analisesVetorial.sobreposicaoPropriedade",
			"analisesVetorial.distanciaAmf",
			"analisesVetorial.sobreposicaoAmf",
			"analisesVetorial.observacao",
			"analisesVetorial.exibirPDF",
			"basesVetorial.id",
			"basesVetorial.nome",
			"basesVetorial.fonte",
			"basesVetorial.ultimaAtualizacao",
			"basesVetorial.observacao",
			"basesVetorial.exibirPDF",
			"observacoesDadosImovel.id",
			"observacoesDadosImovel.texto",
			"observacoesDadosImovel.passoAnalise.id",
			"observacoesDadosImovel.data",
			"observacoesBaseVetorial.id",
			"observacoesBaseVetorial.texto",
			"observacoesBaseVetorial.passoAnalise.id",
			"observacoesBaseVetorial.data",
			"observacoesAnaliseVetorial",
			"observacoesAnaliseVetorial.id",
			"observacoesAnaliseVetorial.texto",
			"observacoesAnaliseVetorial.passoAnalise.id",
			"observacoesAnaliseVetorial.data",
			"observacoesAnaliseTemporal.id",
			"observacoesAnaliseTemporal.texto",
			"observacoesAnaliseTemporal.passoAnalise.id",
			"observacoesAnaliseTemporal.data",
			"observacoesInsumosUtilizados.id",
			"observacoesInsumosUtilizados.texto",
			"observacoesInsumosUtilizados.passoAnalise.id",
			"observacoesInsumosUtilizados.data",
			"observacoesCalculoNDFI.id",
			"observacoesCalculoNDFI.texto",
			"observacoesCalculoNDFI.passoAnalise.id",
			"observacoesCalculoNDFI.data",
			"observacoesCalculoAreaEfetiva.id",
			"observacoesCalculoAreaEfetiva.texto",
			"observacoesCalculoAreaEfetiva.passoAnalise.id",
			"observacoesCalculoAreaEfetiva.data",
			"observacoesDetalhamentoAreaEfetiva.id",
			"observacoesDetalhamentoAreaEfetiva.texto",
			"observacoesDetalhamentoAreaEfetiva.passoAnalise.id",
			"observacoesDetalhamentoAreaEfetiva.data",
			"observacoesConsideracoes.id",
			"observacoesConsideracoes.texto",
			"observacoesConsideracoes.passoAnalise.id",
			"observacoesConsideracoes.data",
			"observacoesConclusao.id",
			"observacoesConclusao.texto",
			"observacoesConclusao.passoAnalise.id",
			"observacoesConclusao.data",
			"observacoesDocumentosComplementares.id",
			"observacoesDocumentosComplementares.texto",
			"observacoesDocumentosComplementares.passoAnalise.id",
			"observacoesDocumentosComplementares.data",
			"observacoesEmbasamentoLegal.id",
			"observacoesEmbasamentoLegal.texto",
			"observacoesEmbasamentoLegal.passoAnalise.id",
			"observacoesEmbasamentoLegal.data",
			"processoManejo.empreendimento.imovel.id",
			"processoManejo.empreendimento.imovel.registroCar",
			"processoManejo.empreendimento.imovel.areaTotalImovelDocumentado",
			"processoManejo.empreendimento.imovel.areaLiquidaImovel",
			"processoManejo.empreendimento.imovel.areaReservaLegal",
			"processoManejo.empreendimento.imovel.areaPreservacaoPermanente",
			"processoManejo.empreendimento.imovel.areaRemanescenteVegetacaoNativa",
			"processoManejo.empreendimento.imovel.areaCorposAgua",
			"processoManejo.empreendimento.imovel.areaUsoConsolidado",
			"documentosImovel.id",
			"documentosImovel.nome",
			"documentosImovel.tipo.id",
			"documentosImovel.tipo.nome",
			"documentosComplementares.id",
			"documentosComplementares.nome",
			"vinculosInsumosOrdenados.id",
			"vinculosInsumosOrdenados.insumo.id",
			"vinculosInsumosOrdenados.insumo.data",
			"vinculosInsumosOrdenados.insumo.ano",
			"vinculosInsumosOrdenados.insumo.satelite",
			"vinculosInsumosOrdenados.insumo.orbPonto",
			"vinculosInsumosOrdenados.exibirPDF",
			"vinculosConsideracoesOrdenados.id",
			"vinculosConsideracoesOrdenados.consideracao.id",
			"vinculosConsideracoesOrdenados.consideracao.texto",
			"vinculosConsideracoesOrdenados.exibirPDF",
			"vinculosEmbasamentosOrdenados.id",
			"vinculosEmbasamentosOrdenados.embasamentoLegal.id",
			"vinculosEmbasamentosOrdenados.embasamentoLegal.texto",
			"vinculosEmbasamentosOrdenados.exibirPDF"
	);
}
