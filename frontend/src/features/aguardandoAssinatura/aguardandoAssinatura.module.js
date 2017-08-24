var aguardandoAssinatura = angular.module('aguardandoAssinatura',['ngRoute']);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

aguardandoAssinatura.config(['$routeProvider', function($routeProvider){

	$routeProvider
		.when('/aguardando-assinatura/:idAnalise', {
			templateUrl: 'features/aguardandoAssinatura/validacaoAnalise/validacao-analise.html',
			controller: controllers.ValidacaoAnaliseController,
			controllerAs: 'validacaoAnalise'
		})		
		.otherwise({
			redirectTo: '/'
		});
}]);

aguardandoAssinatura
	.controller('validacaoAnaliseAprovadorController', controllers.ValidacaoAnaliseAprovadorController);
