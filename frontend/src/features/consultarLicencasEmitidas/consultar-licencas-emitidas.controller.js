var ConsultarLicencasEmitidasController = function($scope, config, $rootScope, processoService, licencaEmitidaService, licencaService, $uibModal, mensagem) {

	$rootScope.tituloPagina = 'CONSULTAR LICENCAS EMITIDAS';

	var consultarLicencas = this;

	consultarLicencas.atualizarListaLicencas = atualizarListaLicencas;
	consultarLicencas.atualizarPaginacao = atualizarPaginacao;
	consultarLicencas.onPaginaAlterada = onPaginaAlterada;
	consultarLicencas.visualizarProcesso = visualizarProcesso;
	consultarLicencas.downloadLicenca = downloadLicenca;
	consultarLicencas.recuperarInfoLicenca = recuperarInfoLicenca;

	consultarLicencas.licencas = [];
	consultarLicencas.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	consultarLicencas.TIPOS_CARACTERIZACOES = app.TIPOS_CARACTERIZACOES;

	function atualizarListaLicencas(licencas) {

		consultarLicencas.licencas = licencas;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		consultarLicencas.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarLicencas');
	}

	function visualizarProcesso(licenca) {

		return processoService.visualizarProcesso(licenca);
	}

	function recuperarInfoLicenca(licenca) {

		var licencaRecuperada = null;

		licencaService.findInfoLicenca(licenca.idLicenca)
			.then(function(response) {

				if(response.data.licencaAnalise == null){
					licencaRecuperada = response.data;
					return licencaEmitidaService.modalInfoLicencaRecuperada(licencaRecuperada);
				}
				licencaRecuperada = response.data.licencaAnalise;
				licencaRecuperada.numeroProcesso = response.data.licencaAnalise.caracterizacao.numeroProcesso;				
				licencaRecuperada.caracterizacao = response.data.caracterizacao;
				licencaRecuperada.dataCadastro = response.data.dataCadastro;
				licencaRecuperada.dataValidade = response.data.dataValidade;
				licencaRecuperada.id = response.data.id;

				return licencaEmitidaService.modalInfoLicencaRecuperada(licencaRecuperada);


			}, function(error) {

				mensagem.error(error.data.texto);
			});

	}

	function downloadLicenca(licenca) {

		if (licenca.origemLicenca === app.ORIGEM_LICENCA.DISPENSA) {

			licencaEmitidaService.downloadDla(licenca.idLicenca);
		} else {

			licencaEmitidaService.downloadLicenca(licenca.idLicenca);
		}
	}
};

exports.controllers.ConsultarLicencasEmitidasController = ConsultarLicencasEmitidasController;