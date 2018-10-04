var AnaliseTecnicaManejoController = function($rootScope, $scope, $routeParams, processoManejoService, uploadService, $location, mensagem) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var analiseTecnicaManejo = this;
	analiseTecnicaManejo.analiseTecnica = null;

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