var analiseEmAndamento = angular.module('analiseEmAndamento',['ngRoute']);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

analiseEmAndamento.config(['$routeProvider', function($routeProvider){

	$routeProvider
		.when('/analise-juridica', {
			templateUrl: 'features/analiseEmAndamento/listagem/analise-em-andamento-listagem.html',
			controller: controllers.AnaliseEmAndamentoListagemController,
			controllerAs: 'analiseEmAndamentoListagem'
		})
		.otherwise({
			redirectTo: '/'
		});    

}]);

analiseEmAndamento
	.controller('analiseEmAndamentoJuridicoListController', controllers.AnaliseEmAndamentoJuridicoListController);
