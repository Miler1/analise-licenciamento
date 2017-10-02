var ConsultarLicencasEmitidasController = function($scope, config, $rootScope, processoService, licencaEmitidaService, licencaService, $uibModal, mensagem) {

	$rootScope.tituloPagina = 'CONSULTAR LICENCAS EMITIDAS';

	var consultarLicencas = this;

	consultarLicencas.atualizarListaLicencas = atualizarListaLicencas;
	consultarLicencas.atualizarPaginacao = atualizarPaginacao;
	consultarLicencas.onPaginaAlterada = onPaginaAlterada;
	consultarLicencas.visualizarProcesso = visualizarProcesso;
	consultarLicencas.downloadLicenca = downloadLicenca;
	consultarLicencas.suspenderLicenca = suspenderLicenca;

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

	function suspenderLicenca(licenca) {

		var licencaAnalise = null;

		licencaService.findLicencaAnalise(licenca.idLicenca)
			.then(function(response) {
			
				licencaAnalise = response.data;

			}, function(error){

				mensagem.error(error.data.texto);
			});

		return licencaService.suspenderLicenca(licencaAnalise);
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