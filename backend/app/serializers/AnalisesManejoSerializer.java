package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class AnalisesManejoSerializer {

	public static JSONSerializer findById = SerializerUtil.create(
			"id",
			"dataAnalise",
			"pathAnexo",
			"analiseTemporal",
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
			"consideracoes",
			"conclusao",
			"processoManejo.numeroProcesso",
			"processoManejo.cpfCnpj",
			"processoManejo.denominacaoEmpreendimentoSimlam",
			"processoManejo.nomeMunicipioSimlam",
			"analisesNdfi.id",
			"analisesNdfi.dataAnalise",
			"analisesNdfi.orbita",
			"analisesNdfi.ponto",
			"analisesNdfi.satelite",
			"analisesNdfi.nivelExploracao",
			"analisesNdfi.valor",
			"analisesNdfi.area",
			"analisesVetorial.id",
			"analisesVetorial.tipo",
			"analisesVetorial.nome",
			"analisesVetorial.distanciaPropriedade",
			"analisesVetorial.sobreposicaoPropriedade",
			"analisesVetorial.distanciaAmf",
			"analisesVetorial.sobreposicaoAmf",
			"analisesVetorial.observacao",
			"basesVetorial.id",
			"basesVetorial.nome",
			"basesVetorial.fonte",
			"basesVetorial.ultimaAtualizacao",
			"basesVetorial.escala",
			"basesVetorial.observacao",
			"observacoesDadosImovel.id",
			"observacoesDadosImovel.texto",
			"observacoesDadosImovel.passoAnalise.id",
			"observacoesBaseVetorial.id",
			"observacoesBaseVetorial.texto",
			"observacoesBaseVetorial.passoAnalise.id",
			"observacoesAnaliseVetorial",
			"observacoesAnaliseVetorial.id",
			"observacoesAnaliseVetorial.texto",
			"observacoesAnaliseTemporal.passoAnalise.id",
			"observacoesInsumosUtilizados.id",
			"observacoesInsumosUtilizados.texto",
			"observacoesInsumosUtilizados.passoAnalise.id",
			"observacoesCalculoNDFI.id",
			"observacoesCalculoNDFI.texto",
			"observacoesCalculoNDFI.passoAnalise.id",
			"observacoesCalculoAreaEfetiva.id",
			"observacoesCalculoAreaEfetiva.texto",
			"observacoesCalculoAreaEfetiva.passoAnalise.id",
			"observacoesDetalhamentoAreaEfetiva.id",
			"observacoesDetalhamentoAreaEfetiva.texto",
			"observacoesDetalhamentoAreaEfetiva.passoAnalise.id",
			"observacoesConsideracoes.id",
			"observacoesConsideracoes.texto",
			"observacoesConsideracoes.passoAnalise.id",
			"observacoesConclusao.id",
			"observacoesConclusao.texto",
			"observacoesConclusao.passoAnalise.id",
			"processoManejo.imovelManejo.id",
			"processoManejo.imovelManejo.registroCar",
			"processoManejo.imovelManejo.areaTotalImovelDocumentado",
			"processoManejo.imovelManejo.areaLiquidaImovel",
			"processoManejo.imovelManejo.areaReservaLegal",
			"processoManejo.imovelManejo.areaPreservacaoPermanente",
			"processoManejo.imovelManejo.areaRemanescenteVegetacaoNativa",
			"processoManejo.imovelManejo.areaCorposAgua",
			"processoManejo.imovelManejo.areaUsoConsolidado"
	);
}
