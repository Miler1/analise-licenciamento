var analiseGeo = angular.module('analiseGeo', ['ngRoute']);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

analiseGeo.config(['$routeProvider', function($routeProvider) {
	
	$routeProvider
		.when('/analise-geo/:idAnaliseTecnica', {
			templateUrl: 'features/analiseGeo/analise-geo.html',
			controller: controllers.AnaliseGeoController,
			controllerAs: 'ctrl',

			resolve: {

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



