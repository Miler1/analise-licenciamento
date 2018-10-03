var AnaliseGeoManejoController = function($rootScope, $scope, $routeParams, processoManejoService, Upload, $timeout, uploadService) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var TAMANHO_MAXIMO_ARQUIVO_MB = 10;

	var analiseGeoManejo = this;
	analiseGeoManejo.visualizarProcesso = null;
	analiseGeoManejo.formularioAnaliseGeo = null;
	analiseGeoManejo.TAMANHO_MAXIMO_ARQUIVO_MB = TAMANHO_MAXIMO_ARQUIVO_MB;
	analiseGeoManejo.processo = null;

	analiseGeoManejo.init = function() {

		processoManejoService.getProcesso($routeParams.idProcesso)
			.then(function (response) {

				analiseGeoManejo.processo = response.data;
				analiseGeoManejo.processo.analiseManejo = {pathShape: null};

			})
			.catch(function (response) {

				if (!!response.data.texto)
					mensagem.warning(response.data.texto);

				else
					mensagem.error("Ocorreu um erro obter dados do processo.");
			});
	};

	function analiseGeoValida() {

		analiseGeoManejo.formularioAnaliseGeo.$setSubmitted();
	}

	$scope.log = '';

	analiseGeoManejo.upload = function (file) {
		if (file) {

			if (!file.$error) {

				if (!analiseGeoManejo.processo.analiseManejo.pathShape) {

					uploadService.removeShape(analiseGeoManejo.processo.analiseManejo.pathShape)

						.then(function(response) {

							analiseGeoManejo.processo.analiseManejo.pathShape = null;

							analiseGeoManejo.saveShape(file);

						}, function(error){

							mensagem.error(error.data.texto);
						});

				} else {

					analiseGeoManejo.saveShape(file);
				}
			}
		}
	};


	analiseGeoManejo.getPath = function (file) {

		uploadService.saveShape(file)

			.then(function(response) {

				analiseGeoManejo.processo.analiseManejo.pathShape = response.data;

			}, function(error){

				mensagem.error(error.data.texto);
			});
	}

};

exports.controllers.AnaliseGeoManejoController = AnaliseGeoManejoController;