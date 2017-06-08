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
		.otherwise({
			redirectTo: "/"
		});
}]);

caixasEntrada
	.controller('cxEntCoordenadorJuridicoController', controllers.CxEntCoordenadorJuridicoController)
	.controller('modalVincularConsutorJuridicoController', controllers.ModalVincularConsutorJuridicoController)
	.controller('cxEntConsultorJuridicoController', controllers.CxEntConsultorJuridicoController);

caixasEntrada
	.component('filtroProcessos', directives.FiltroProcessos);
