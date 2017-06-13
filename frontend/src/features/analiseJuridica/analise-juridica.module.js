var analiseJuridica = angular.module('analiseJuridica', ['ngRoute']);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

analiseJuridica.config(['$routeProvider', function($routeProvider) {
	
	$routeProvider
		.when('/analise-juridica/:idAnaliseJuridica', {
			templateUrl: 'features/analiseJuridica/analise-juridica.html',
			controller: controllers.AnaliseJuridicaController,
			controllerAs: 'ctrl',

			resolve: {

				// processo: function(processoService, $route, $q) {

				// 	var deferred = $q.defer();					
				// 	processoService.consultar($route.current.params.idProcesso)
				// 		.then(function(response){
				// 			deferred.resolve(response.data);
				// 		});
				// 	return deferred.promise;
				// },

				analiseJuridica: function(analiseJuridicaService, $route, $q) {

					var deferred = $q.defer();					
					analiseJuridicaService.getAnaliseJuridica($route.current.params.idAnaliseJuridica)
						.then(function(response){
							deferred.resolve(response.data);
						});
					return deferred.promise;					
				}				
			}
		})
		.otherwise({
			redirectTo: '/'
		});
}]);    



