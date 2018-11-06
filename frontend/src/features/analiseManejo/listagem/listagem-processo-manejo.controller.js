var ListagemProcessoManejoController = function($scope, config, $rootScope, processoManejoService, mensagem, $location) {

	$rootScope.tituloPagina = 'CONSULTAR PROCESSO MANEJO DIGITAL';

	var listagemProcessoManejo = this;

	listagemProcessoManejo.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagemProcessoManejo.atualizarPaginacao = atualizarPaginacao;
	listagemProcessoManejo.atualizarListaProcessos = atualizarListaProcessos;
	listagemProcessoManejo.onPaginaAlterada = onPaginaAlterada;

	listagemProcessoManejo.iniciarAnalise = function (processo) {

		$location.path('/analise-manejo/' + processo.id + '/analise-geo');

	};

	listagemProcessoManejo.visualizarProcesso = null;
	listagemProcessoManejo.processosManejo = [
		{
			id: 1,
			numeroProcesso: "2019/001",
			idEmpreendimento: "1",
			cpfCnpj: "76579607000130",
			denominacaoEmpreendimentoSimlam: "Empreendimento industrial",
			idMunicipio: "1",
			nomeMunicipioSimlam: "Bagre",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT",
			imovelManejo: {
				registroCar: "PA-1508407-327A45061DA64D23866C811111DC44E0",
				areaTotalImovelDocumentado: 100.0,
				areaLiquidaImovel: 80.0,
				endereco: "Rua E, 189",
				bairro: "Serraria",
				cep: "57046-805"
			}
		},
		{
			numeroProcesso: "2019/002",
			idEmpreendimento: "2",
			cpfCnpj: "81836353000128",
			denominacaoEmpreendimentoSimlam: "Sitio do Kustavinho amarelooo",
			idMunicipio: "2",
			nomeMunicipioSimlam: "Page",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT",
			imovelManejo: {
				registroCar: "PA-1508407-327A45061DA64D23866C811111DC44E0",
				areaTotalImovelDocumentado: 80.0,
				areaLiquidaImovel: 60.0,
				areaReservaLegal: 20.0,
				endereco: "Rua Santa Catarina, 293",
				bairro: "Habitasa",
				cep: "69905-084"
			}
		},
		{
			numeroProcesso: "2019/003",
			idEmpreendimento: "3",
			cpfCnpj: "50081340000162",
			denominacaoEmpreendimentoSimlam: "Mariane e César Limpeza ME",
			idMunicipio: "3",
			nomeMunicipioSimlam: "Belém",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT",
			imovelManejo: {
				registroCar: "PA-1508407-327A45061DA64D23866C811111DC44E0",
				areaTotalImovelDocumentado: 60.0,
				areaLiquidaImovel: 40.0,
				areaPreservacaoPermanente: 17.0,
				endereco: "Rua da Alegria, 939",
				bairro: "Rio Grande",
				cep: "65091-773"
			}
		},
		{
			numeroProcesso: "2019/004",
			idEmpreendimento: "4",
			cpfCnpj: "13113652000184",
			denominacaoEmpreendimentoSimlam: "Julia e Fernando Doces & Salgados ME",
			idMunicipio: "4",
			nomeMunicipioSimlam: "Ananindeua",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT",
			imovelManejo: {
				registroCar: "PA-1508407-327A45061DA64D23866C811111DC44E0",
				areaTotalImovelDocumentado: 40.0,
				areaLiquidaImovel: 20.0,
				areaRemanescenteVegetacaoNativa: 5.0,
				endereco: "Avenida Vinte e Cinco de Maio, 980",
				bairro: "IAPI",
				cep: "40323-340"
			}
		},
		{
			numeroProcesso: "2019/005",
			idEmpreendimento: "5",
			cpfCnpj: "96070128000109",
			denominacaoEmpreendimentoSimlam: "Fernanda e Malu Entregas Expressas ME",
			idMunicipio: "5",
			nomeMunicipioSimlam: "Marabá",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT",
			imovelManejo: {
				registroCar: "PA-1508407-327A45061DA64D23866C811111DC44E0",
				areaTotalImovelDocumentado: 20.0,
				areaLiquidaImovel: 1.0,
				areaCorposAgua: 1.0,
				endereco: "Rua José Maciel, 552",
				bairro: "Jardim Floresta",
				cep: "69312-062"
			}
		},
		{
			numeroProcesso: "2019/006",
			idEmpreendimento: "6",
			cpfCnpj: "87839177000164",
			denominacaoEmpreendimentoSimlam: "Elias e Ana Marcenaria Ltda",
			idMunicipio: "3",
			nomeMunicipioSimlam: "Belém",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT",
			imovelManejo: {
				registroCar: "PA-1508407-327A45061DA64D23866C811111DC44E0",
				areaTotalImovelDocumentado: 20.0,
				areaLiquidaImovel: 10.0,
				areaUsoConsolidado: 10.0,
				endereco: "Rua Emília Michalski Ubá, 258",
				bairro: "Cachoeira",
				cep: "82710-450"
			}
		},
		{
			numeroProcesso: "2019/007",
			idEmpreendimento: "7",
			cpfCnpj: "93133609000110",
			denominacaoEmpreendimentoSimlam: "Leandro e Alexandre Padaria ME",
			idMunicipio: "3",
			nomeMunicipioSimlam: "Belém",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT",
			imovelManejo: {
				registroCar: "PA-1508407-327A45061DA64D23866C811111DC44E0",
				areaTotalImovelDocumentado: 20.0,
				areaLiquidaImovel: 10.0,
				areaReservaLegal: 0.0,
				areaPreservacaoPermanente: 0.0,
				areaRemanescenteVegetacaoNativa: 0.0,
				areaCorposAgua: 0.0,
				areaUsoConsolidado: 0.0,
				endereco: "Área Isolada 14, 417",
				bairro: "Núcleo Rural Vargem Bonita (Núcleo Bandeirante)",
				cep: "71750-542"
			}
		},
		{
			numeroProcesso: "2019/008",
			idEmpreendimento: "8",
			cpfCnpj: "46847458000181",
			denominacaoEmpreendimentoSimlam: "Rafael e Larissa Marketing ME",
			idMunicipio: "8",
			nomeMunicipioSimlam: "Santarém",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT",
			imovelManejo: {
				registroCar: "PA-1508407-327A45061DA64D23866C811111DC44E0",
				areaTotalImovelDocumentado: 30.0,
				areaLiquidaImovel: 15.0,
				areaReservaLegal: 1.0,
				areaPreservacaoPermanente: 0.0,
				areaRemanescenteVegetacaoNativa: 2.0,
				areaCorposAgua: 0.0,
				areaUsoConsolidado: 0.0,
				endereco: "Rua Regeneração, 493",
				bairro: "São José",
				cep: "64218-300"
			}
		},
		{
			numeroProcesso: "2019/009",
			idEmpreendimento: "9",
			cpfCnpj: "07101336000190",
			denominacaoEmpreendimentoSimlam: "Allana e Nicolas Lavanderia ME",
			idMunicipio: "4",
			nomeMunicipioSimlam: "Ananindeua",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT",
			imovelManejo: {
				registroCar: "PA-1508407-327A45061DA64D23866C811111DC44E0",
				areaTotalImovelDocumentado: 804.0,
				areaLiquidaImovel: 158.0,
				areaReservaLegal: 1.0,
				areaPreservacaoPermanente: 30.0,
				areaRemanescenteVegetacaoNativa: 20.0,
				areaCorposAgua: 1.0,
				areaUsoConsolidado: 150.0,
				endereco: "Rua Carmelita Lobo Siqueira, 764",
				bairro: "Fazendinha",
				cep: "68911-032"
			}
		},
		{
			numeroProcesso: "2019/010",
			idEmpreendimento: "10",
			cpfCnpj: "75993724000182",
			denominacaoEmpreendimentoSimlam: "Luan e Ana Padaria Ltda",
			idMunicipio: "3",
			nomeMunicipioSimlam: "Belém",
			siglaEstadoMunicipio: "PA",
			idTipoLicenca: "1",
			nomeTipoLicenca: "APAT",
			imovelManejo: {
				registroCar: "PA-1508407-327A45061DA64D23866C811111DC44E0",
				areaTotalImovelDocumentado: 1048.0,
				areaLiquidaImovel: 642.0,
				areaPreservacaoPermanente: 30.0,
				areaRemanescenteVegetacaoNativa: 20.0,
				areaUsoConsolidado: 150.0,
				endereco: "Rua Dominicana, 598",
				bairro: "Parque das Estrelas",
				cep: "65911-303"
			}
		},
	];

	listagemProcessoManejo.downloadPdfAnaliseTecnica = function (processo) {

		processoManejoService.downloadPdfAnaliseTecnica(processo)
			.then(
				function(data, status, headers){

					var a = document.createElement('a');
					a.href = URL.createObjectURL(data.data.response.blob);
					a.download = data.data.response.fileName ? data.data.response.fileName : 'previa_notificacao_analise_juridica.pdf';
					a.click();
				},
				function(error){

					mensagem.error(error.data.texto);
				}
			);
	};


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