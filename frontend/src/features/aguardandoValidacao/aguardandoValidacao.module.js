var aguardandoValidacao = angular.module('aguardandoValidacao',['ngRoute']);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

aguardandoValidacao.config(['$routeProvider', function($routeProvider){

	$routeProvider
		.when('/aguardando-validacao', {
			templateUrl: 'features/aguardandoValidacao/listagem/aguardando-validacao-listagem.html',
			controller: controllers.AguardandoValidacaoListagemController,
			controllerAs: 'aguardandoValidacaoListagem'
		})		
		.otherwise({
			redirectTo: '/'
		});
}]);

aguardandoValidacao
	.controller('aguardandoValidacaoJuridicoListController', controllers.AguardandoValidacaoJuridicoListController)
	.controller('validacaoAnaliseJuridicaController', controllers.ValidacaoAnaliseJuridicaController)
	.controller('aguardandoValidacaoTecnicaListController', controllers.AguardandoValidacaoTecnicaListController)
	.controller('validacaoAnaliseTecnicaController', controllers.ValidacaoAnaliseTecnicaController)
	.controller('validacaoAnaliseTecnicaCoordenadorController', controllers.ValidacaoAnaliseTecnicaCoordenadorController)
	.controller('aguardandoValidacaoCoordenadorTecnicoListController', controllers.AguardandoValidacaoCListController)
	.controller('aguardandoValidacaoGeoListController', controllers.AguardandoValidacaoGeoListController);
