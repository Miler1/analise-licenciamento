var analiseJuridica = angular.module('analiseJuridica', ['ngRoute']);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

analiseJuridica.config(['$routeProvider', function($routeProvider) {
	
	$routeProvider
		.when('/analise-juridica/:idProcesso', {
			templateUrl: 'features/analiseJuridica/analise-juridica.html',
			controller: controllers.AnaliseJuridicaController,
			controllerAs: 'analiseJuridica',

			resolve: {

				processo: function(processoService, $route, $q) {

					var deferred = $q.defer();					
					processoService.consultar($route.current.params.idProcesso)
						.then(function(processo){
							deferred.resolve(processo.data);
						});
					return deferred.promise;
				}
			}
		})
		.otherwise({
			redirectTo: '/'
		});
}]);    



