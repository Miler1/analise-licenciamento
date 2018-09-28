var analiseManejo = angular.module("analiseManejo", ["ngRoute"]);

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
		.otherwise({
			redirectTo: "/"
		});
}]);

analiseManejo
	.controller('listagemProcessoManejoController', controllers.ListagemProcessoManejoController);

analiseManejo
	.component('filtroProcessosManejo', directives.FiltroProcessosManejo);
