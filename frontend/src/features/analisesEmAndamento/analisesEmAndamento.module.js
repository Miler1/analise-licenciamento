var analisesEmAndamento = angular.module('analisesEmAndamento',['ngRoute']);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

analisesEmAndamento.config(['$routeProvider', function($routeProvider){

	$routeProvider
		.when('/analise-juridica', {
			templateUrl: 'features/analisesEmAndamento/analises-em-andamento.html',
			controller: controllers.AnalisesEmAndamentoController,
			controllerAs: 'analisesEmAndamento'
		})
		.otherwise({
			redirectTo: '/'
		});    

}]);

