var AnaliseTecnicaManejoController = function($rootScope, $scope, $routeParams, processoManejoService, uploadService, $location, mensagem) {

	$rootScope.tituloPagina = 'PARECER TÉCNICO';

	var analiseTecnicaManejo = this;
	analiseTecnicaManejo.processo = null;

	analiseTecnicaManejo.init = function() {


	};

};

exports.controllers.AnaliseTecnicaManejoController = AnaliseTecnicaManejoController;