var analiseJuridica = angular.module('analiseJuridica', ['ngRoute']);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

analiseJuridica.config(['$routeProvider', function($routeProvider) {
	
	$routeProvider
		.when('/analise-juridica/:idProcesso', {
			templateUrl: 'features/analiseJuridica/analise-juridica.html',
			controller: controllers.AnaliseJuridicaController,
			controllerAs: 'analiseJuridica'
		})
		.otherwise({
			redirectTo: '/'
		});
}]);    



