var aguardandoAssinatura = angular.module('aguardandoAssinatura',['ngRoute']);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

aguardandoAssinatura.config(['$routeProvider', function($routeProvider){

	$routeProvider
		.when('/aguardando-assinatura-aprovador/:idAnalise', {
			templateUrl: 'features/aguardandoAssinatura/validacaoAnalise/aprovador/validacao-analise.html',
			controller: controllers.ValidacaoAnaliseAprovadorController,
			controllerAs: 'ctrl'
		})		
		.otherwise({
			redirectTo: '/'
		});
}]);
