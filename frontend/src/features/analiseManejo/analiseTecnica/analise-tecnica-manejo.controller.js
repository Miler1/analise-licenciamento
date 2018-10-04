var AnaliseTecnicaManejoController = function($rootScope, $scope, $routeParams, processoManejoService, uploadService, $location, mensagem) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var analiseTecnicaManejo = this;
	analiseTecnicaManejo.analiseTecnica = null;
	analiseTecnicaManejo.passos = {
		DADOS_IMOVEL: 0,
		BASE_VETORIAL:1,
		ANALISE_VETORIAL: 2,
		ANALISE_TEMPORAL: 3,
		INSUMOS_UTILIZADOS: 4,
		CALCULO_NDFI: 5,
		CALCULO_AREA_EFETIVA: 6,
		DETALHAMENTO_AREA_EFETIVA: 7,
		CONSIDERACOES: 8,
		CONCLUSAO: 9
	};

	analiseTecnicaManejo.passoAtual = analiseTecnicaManejo.passos.DADOS_IMOVEL;

	analiseTecnicaManejo.init = function() {

		processoManejoService.getAnalise($routeParams.idAnaliseManejo)
			.then(function (response) {

				analiseTecnicaManejo.analiseTecnica = response.data;

			})
			.catch(function (response) {

				if (!!response.data.texto)
					mensagem.warning(response.data.texto);

				else
					mensagem.error("Ocorreu um erro obter dados do processo.");
			});

	};

};

exports.controllers.AnaliseTecnicaManejoController = AnaliseTecnicaManejoController;