var analiseManejo = angular.module("analiseManejo", ["ngRoute", 'ngFileUpload']);

var utils = app.utils,
	controllers = app.controllers,
	directives = app.directives;

analiseManejo.config(["$routeProvider", function($routeProvider) {

	$routeProvider
		.when("/analise-manejo", {
			templateUrl: "features/analiseManejo/listagem/listagem-processo-manejo.html",
			controller: controllers.ListagemProcessoManejoController,
			controllerAs: 'listagemProcessoManejo'
		})
		.when("/analise-manejo/:idProcesso/analise-geo", {
			templateUrl: "features/analiseManejo/analiseGeo/analise-geo-manejo.html",
			controller: controllers.AnaliseGeoManejoController,
			controllerAs: 'analiseGeoManejo'
		})
		.otherwise({
			redirectTo: "/"
		});
}]);

analiseManejo
	.controller('listagemProcessoManejoController', controllers.ListagemProcessoManejoController);

analiseManejo
	.component('filtroProcessosManejo', directives.FiltroProcessosManejo);
