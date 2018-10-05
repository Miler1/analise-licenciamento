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
		.when("/analise-manejo/:idAnaliseManejo/analise-tecnica", {
			templateUrl: "features/analiseManejo/analiseTecnica/analise-tecnica-manejo.html",
			controller: controllers.AnaliseTecnicaManejoController,
			controllerAs: 'analiseTecnicaManejo'
		})
		.otherwise({
			redirectTo: "/"
		});
}]);

analiseManejo
	.controller('listagemProcessoManejoController', controllers.ListagemProcessoManejoController)
	.controller('modalObservacaoController', controllers.ModalObservacaoController);

analiseManejo
	.component('filtroProcessosManejo', directives.FiltroProcessosManejo);
