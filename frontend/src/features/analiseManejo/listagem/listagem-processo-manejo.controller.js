var ListagemProcessoManejoController = function($scope, config, $rootScope) {

	$rootScope.tituloPagina = 'CONSULTAR PROCESSO MANEJO FLORESTAL';

	var listagemProcessoManejo = this;

	listagemProcessoManejo.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagemProcessoManejo.atualizarPaginacao = atualizarPaginacao;
	listagemProcessoManejo.atualizarListaProcessos = atualizarListaProcessos;
	listagemProcessoManejo.onPaginaAlterada = onPaginaAlterada;
	listagemProcessoManejo.iniciarAnalise = null;
	listagemProcessoManejo.visualizarProcesso = null;
	listagemProcessoManejo.processosManejo = [
		{
			numProcesso: "2018/001",
			idEmpreendimento: "1",
			cpfCnpjEmpreendimento: "76579607000130",
			denominacaoEmpreendimento: "Empreendimento industrial",
			idMunicipio: "1",
			nomeMunicipio: "Bagre",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT"
		},
		{
			numProcesso: "2018/002",
			idEmpreendimento: "2",
			cpfCnpjEmpreendimento: "81836353000128",
			denominacaoEmpreendimento: "Sitio do Kustavinho amarelooo",
			idMunicipio: "2",
			nomeMunicipio: "Page",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT"
		},
		{
			numProcesso: "2018/003",
			idEmpreendimento: "3",
			cpfCnpjEmpreendimento: "50081340000162",
			denominacaoEmpreendimento: "Mariane e César Limpeza ME",
			idMunicipio: "3",
			nomeMunicipio: "Belém",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT"
		},
		{
			numProcesso: "2018/004",
			idEmpreendimento: "4",
			cpfCnpjEmpreendimento: "13113652000184",
			denominacaoEmpreendimento: "Julia e Fernando Doces & Salgados ME",
			idMunicipio: "4",
			nomeMunicipio: "Ananindeua",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT"
		},
		{
			numProcesso: "2018/005",
			idEmpreendimento: "5",
			cpfCnpjEmpreendimento: "96070128000109",
			denominacaoEmpreendimento: "Fernanda e Malu Entregas Expressas ME",
			idMunicipio: "5",
			nomeMunicipio: "Marabá",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT"
		},
		{
			numProcesso: "2018/006",
			idEmpreendimento: "6",
			cpfCnpjEmpreendimento: "87839177000164",
			denominacaoEmpreendimento: "Elias e Ana Marcenaria Ltda",
			idMunicipio: "3",
			nomeMunicipio: "Belém",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT"
		},
		{
			numProcesso: "2018/007",
			idEmpreendimento: "7",
			cpfCnpjEmpreendimento: "93133609000110",
			denominacaoEmpreendimento: "Leandro e Alexandre Padaria ME",
			idMunicipio: "3",
			nomeMunicipio: "Belém",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT"
		},
		{
			numProcesso: "2018/008",
			idEmpreendimento: "8",
			cpfCnpjEmpreendimento: "46847458000181",
			denominacaoEmpreendimento: "Rafael e Larissa Marketing ME",
			idMunicipio: "8",
			nomeMunicipio: "Santarém",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT"
		},
		{
			numProcesso: "2018/009",
			idEmpreendimento: "9",
			cpfCnpjEmpreendimento: "07101336000190",
			denominacaoEmpreendimento: "Allana e Nicolas Lavanderia ME",
			idMunicipio: "4",
			nomeMunicipio: "Ananindeua",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT"
		},
		{
			numProcesso: "2018/010",
			idEmpreendimento: "10",
			cpfCnpjEmpreendimento: "75993724000182",
			denominacaoEmpreendimento: "Luan e Ana Padaria Ltda",
			idMunicipio: "3",
			nomeMunicipio: "Belém",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT"
		},
	];

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessosManejo');
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		listagemProcessoManejo.paginacao.update(totalItens, paginaAtual);
	}

	function atualizarListaProcessos(processos) {

		listagemProcessoManejo.processosManejo = processos;
	}

};

exports.controllers.ListagemProcessoManejoController = ListagemProcessoManejoController;