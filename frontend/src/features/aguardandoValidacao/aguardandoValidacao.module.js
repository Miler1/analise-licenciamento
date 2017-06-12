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
		.when('/aguardando-validacao/:idAnalise', {
			templateUrl: 'features/aguardandoValidacao/validacaoAnalise/validacao-analise.html',
			controller: controllers.ValidacaoAnaliseController,
			controllerAs: 'validacaoAnalise',
			// resolve: {
			// 	analiseJuridica: getAnaliseJuridica
			// }
		})		
		.otherwise({
			redirectTo: '/'
		});    

}]);

aguardandoValidacao
	.controller('aguardandoValidacaoJuridicoListController', controllers.AguardandoValidacaoJuridicoListController)
	.controller('validacaoAnaliseJuridicaController', controllers.ValidacaoAnaliseJuridicaController);

// function getAnaliseJuridica(analiseJuridicaService, $q, $route) {

// 	var deferred = $q.defer();

// 	analiseJuridicaService.getAnaliseJuridicaById($route.current.params.idAnalise)
// 		.then(function(response){
// 			deferred.resolve(response.data);
// 		});		
		
// 	return deferred.promise;	
// }
