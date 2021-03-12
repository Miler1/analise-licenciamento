var analiseTecnica = angular.module('analiseTecnica', ['ngRoute']);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

analiseTecnica.config(['$routeProvider', function($routeProvider) {

	$routeProvider
		.when('/analise-tecnica/:idAnaliseTecnica', {
			templateUrl: 'features/analiseTecnica/analise-tecnica.html',
			controller: controllers.AnaliseTecnicaController,
			controllerAs: 'ctrl',

			resolve: {

				analiseTecnica: function(analiseTecnicaService, $route, $q) {

					var deferred = $q.defer();
					analiseTecnicaService.getAnaliseTecnica($route.current.params.idAnaliseTecnica)
						.then(function(response){
							deferred.resolve(response.data);
						});
					return deferred.promise;
				},
				restricoes: function(analiseTecnicaService, $route, $q) {

					var deferred = $q.defer();
					analiseTecnicaService.getRestricoesGeo($route.current.params.idAnaliseTecnica)
						.then(function(response){
							deferred.resolve(response.data);
						});
					return deferred.promise;

				},
				idAnaliseTecnica: function($route) {

					return $route.current.params.idAnaliseTecnica;

				}
			}
		})
		.otherwise({
			redirectTo: '/'
		});
}]);

analiseTecnica
	//.controller('analiseGeoController', controllers.AnaliseGeoController)
	.controller('modalParecerRestricaoController', controllers.ModalParecerRestricaoController);