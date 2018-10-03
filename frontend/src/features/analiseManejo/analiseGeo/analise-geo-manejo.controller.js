var AnaliseGeoManejoController = function($rootScope, $scope, $routeParams, processoManejoService, uploadService, $location, mensagem) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var TAMANHO_MAXIMO_ARQUIVO_MB = 10;

	var analiseGeoManejo = this;
	analiseGeoManejo.visualizarProcesso = null;
	analiseGeoManejo.formularioAnaliseGeo = null;
	analiseGeoManejo.TAMANHO_MAXIMO_ARQUIVO_MB = TAMANHO_MAXIMO_ARQUIVO_MB;
	analiseGeoManejo.processo = null;
	analiseGeoManejo.arquivoShape = null;

	analiseGeoManejo.init = function() {

		processoManejoService.getProcesso($routeParams.idProcesso)
			.then(function (response) {

				analiseGeoManejo.processo = response.data;
				analiseGeoManejo.processo.analiseManejo = {pathShape: null};

				if (analiseGeoManejo.processo.nomeCondicao == 'Manejo digital em análise técnica' ) {

					mensagem.warning('Ops... Você não deveria estar aqui, que constrangedor...');
				}

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

				if (analiseGeoManejo.processo.analiseManejo.pathShape) {

					var nameFile = analiseGeoManejo.processo.analiseManejo.pathShape.replace(/^.*[\\\/]/, '');

					uploadService.removeShape(nameFile)

						.then(function(response) {

							analiseGeoManejo.processo.analiseManejo.pathShape = null;
							analiseGeoManejo.arquivoShape = null;
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

	analiseGeoManejo.saveShape = function (file) {

		uploadService.saveShape(file)

			.then(function(response) {

				analiseGeoManejo.processo.analiseManejo.pathShape = response.data;
				analiseGeoManejo.arquivoShape = file;

			}, function(error){

				mensagem.error(error.data.texto);
			});
	};

	analiseGeoManejo.removeUpload = function () {

		var nameFile = analiseGeoManejo.processo.analiseManejo.pathShape.replace(/^.*[\\\/]/, '');

		uploadService.removeShape(nameFile)

			.then(function(response) {

				analiseGeoManejo.processo.analiseManejo.pathShape = null;
				analiseGeoManejo.arquivoShape = null;

			}, function(error){

				mensagem.error(error.data.texto);
			});
	};

	analiseGeoManejo.analisar = function() {

		if(!analiseValida()) {

			mensagem.error('O arquivo do shape não foi selecionado', { ttl: 10000 });
			return;
		}

		processoManejoService.iniciarAnalise(analiseGeoManejo.processo)
			.then(function(response) {

				mensagem.sucess(response.data.texto);

			}, function(error){

				mensagem.error(error.data.texto);
			});
	};

	analiseGeoManejo.cancelar = function() {

		$location.path('/analise-manejo');
	};

	function analiseValida() {

		return (analiseGeoManejo.processo.analiseManejo.pathShape);
	}


};

exports.controllers.AnaliseGeoManejoController = AnaliseGeoManejoController;