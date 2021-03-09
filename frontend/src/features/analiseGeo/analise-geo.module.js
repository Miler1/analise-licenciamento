var analiseGeo = angular.module('analiseGeo', ['ngRoute']);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

analiseGeo.config(['$routeProvider', function($routeProvider) {

	$routeProvider
		.when('/analise-geo/:idAnaliseGeo', {
			templateUrl: 'features/analiseGeo/analise-geo.html',
			controller: controllers.AnaliseGeoController,
			controllerAs: 'ctrl',

			resolve: {

				analiseGeo: function(analiseGeoService, $route, $q) {

					var deferred = $q.defer();
					analiseGeoService.getAnaliseGeo($route.current.params.idAnaliseGeo)
						.then(function(response){
							deferred.resolve(response.data);
						});
					return deferred.promise;
				},
				restricoes: function(analiseGeoService, $route, $q) {

					// var deferred = $q.defer();
					// analiseGeoService.getRestricoesGeo($route.current.params.idAnaliseGeo)
					// 	.then(function(response){
					// 		deferred.resolve(response.data);
					// 	});
					// return deferred.promise;

				},
				idAnaliseGeo: function($route) {

					return $route.current.params.idAnaliseGeo;

				}
			}
		})
		.otherwise({
			redirectTo: '/'
		});
}]);

analiseGeo
	//.controller('analiseGeoController', controllers.AnaliseGeoController)
	.controller('modalParecerRestricaoController', controllers.ModalParecerRestricaoController);
