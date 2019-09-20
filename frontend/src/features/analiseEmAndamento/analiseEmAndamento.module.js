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
		.when('/analise-tecnica', {
			templateUrl: 'features/analiseEmAndamento/listagem/analise-em-andamento-listagem.html',
			controller: controllers.AnaliseEmAndamentoListagemController,
			controllerAs: 'analiseEmAndamentoListagem'
		})
		.when('/analise-geo', {
			templateUrl: 'features/analiseEmAndamento/listagem/analise-em-andamento-listagem.html',
			controller: controllers.AnaliseEmAndamentoListagemController,
			controllerAs: 'analiseEmAndamentoListagem'
		})
		.when('/analise-gerente', {
			templateUrl: 'features/analiseEmAndamento/listagem/analise-em-andamento-listagem.html',
			controller: controllers.AnaliseEmAndamentoListagemController,
			controllerAs: 'analiseEmAndamentoListagem'
		})
		.when('/analise-gerente/:idAnalise', {
			templateUrl: 'features/analiseEmAndamento/validacao/validacao-analise.html',
			controller: controllers.ValidacaoAnaliseController,
			controllerAs: 'validacaoAnalise'
		})
		.otherwise({
			redirectTo: '/'
		});    

}]);

analiseEmAndamento
	.controller('analiseEmAndamentoJuridicoListController', controllers.AnaliseEmAndamentoJuridicoListController)
	.controller('analiseEmAndamentoTecnicaListController', controllers.AnaliseEmAndamentoTecnicaListController)
	.controller('analiseEmAndamentoGeoListController', controllers.AnaliseEmAndamentoGeoListController)
	.controller('analiseEmAndamentoGerenteListController', controllers.AnaliseEmAndamentoGerenteListController)
	.controller('validacaoAnaliseGeoGerenteController', controllers.ValidacaoAnaliseGeoGerenteController);
