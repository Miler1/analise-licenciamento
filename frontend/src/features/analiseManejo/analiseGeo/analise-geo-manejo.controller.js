var AnaliseGeoManejoController = function($rootScope, $routeParams, processoManejoService) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var analiseGeoManejo = this;

	analiseGeoManejo.visualizarProcesso = null;
	analiseGeoManejo.processo = null;

	//analiseGeoManejo.init = function () {

		processoManejoService.getProcesso($routeParams.idProcesso)
			.then(function(response){

				analiseGeoManejo.processo = response.data;

			})
			.catch(function(response){

				if(!!response.data.texto)
					mensagem.warning(response.data.texto);

				else
					mensagem.error("Ocorreu um erro obter dados do processo.");
			});

	//};

};

exports.controllers.AnaliseGeoManejoController = AnaliseGeoManejoController;