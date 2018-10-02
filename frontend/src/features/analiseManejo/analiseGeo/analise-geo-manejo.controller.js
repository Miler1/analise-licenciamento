var AnaliseGeoManejoController = function($rootScope, $scope, $routeParams, processoManejoService, Upload, $timeout, uploadService) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var analiseGeoManejo = this;

	analiseGeoManejo.visualizarProcesso = null;
	analiseGeoManejo.processo = null;
	analiseGeoManejo.formularioAnaliseGeo = null;
	analiseGeoManejo.files = [];
	analiseGeoManejo.nomeArquivoShape = undefined;

	analiseGeoManejo.init = function() {

		processoManejoService.getProcesso($routeParams.idProcesso)
			.then(function (response) {

				analiseGeoManejo.processo = response.data;

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

	$scope.upload = function (file) {
		if (file) {
			if (!file.$error) {

				uploadService.shape(file)
				.then(function (resp) {
					$timeout(function() {
						$scope.log = 'file: ' +
						resp.config.data.file.name +
						', Response: ' + JSON.stringify(resp.data) +
						'\n' + $scope.log;
					});
				}, null, function (evt) {
					var progressPercentage = parseInt(100.0 *
							evt.loaded / evt.total);
					$scope.log = 'progress: ' + progressPercentage +
						'% ' + evt.config.data.file.name + '\n' +
					  $scope.log;
				});
			}
		}
	};

};

exports.controllers.AnaliseGeoManejoController = AnaliseGeoManejoController;