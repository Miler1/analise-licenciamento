var caixasEntrada = angular.module("caixasEntrada", ["ngRoute"]);

var utils = app.utils,
    controllers = app.controllers,
    directives = app.directives;

caixasEntrada.config(["$routeProvider", function($routeProvider) {
	
	$routeProvider
		.when("/caixa-entrada", {
			templateUrl: "features/caixaEntrada/caixa-entrada.html",
			controller: controllers.CaixaEntradaController,
			controllerAs: 'caixaEntrada'
		})
		.when("/shape-upload/:idProcesso", {
			templateUrl: "features/caixaEntrada/analistaGeo/shape-upload.html",
			controller: controllers.CaixaEntradaController,
			controllerAs: 'caixaEntrada',

			resolve: {
				
				idProcesso: function($route) {
					return $route.current.params.idProcesso;
				}
			}
		})
		.otherwise({
			redirectTo: "/"
		});
}]);

caixasEntrada
	.controller('cxEntCoordenadorJuridicoController', controllers.CxEntCoordenadorJuridicoController)
	.controller('cxEntCoordenadorTecnicoController', controllers.CxEntCoordenadorTecnicoController)
	.controller('modalVincularConsultorController', controllers.ModalVincularConsultorController)
	.controller('cxEntConsultorJuridicoController', controllers.CxEntConsultorJuridicoController)
	.controller('cxEntAnalistaTecnicoController', controllers.CxEntAnalistaTecnicoController)
	.controller('cxEntGerenteTecnicoController', controllers.CxEntGerenteController)
	.controller('cxEntDiretorController', controllers.CxEntDiretorController)
	.controller('cxEntSecretarioController', controllers.CxEntSecretarioController)
	.controller('cxEntAnalistaGeoController', controllers.CxEntAnalistaGeoController);

caixasEntrada
	.component('filtroProcessos', directives.FiltroProcessos);
