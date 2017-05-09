var licenciamento = angular.module("licenciamento", [
	"ngRoute",
	"ui.bootstrap",
	"angular-growl",
	"ngMessages",
	"idf.br-filters",
	"ui.utils.masks",
	"angularMoment",
	"ngFileUpload",
	'ui.select',
	"ngSanitize"
]);

licenciamento.config(["$routeProvider",	function($routeProvider) {

	$routeProvider
		.when("/", {
			redirectTo: "/inicial"
		})
		.when("/inicial", {
			templateUrl: "layout/inicial.html"
		})
		.otherwise({
			redirectTo: "/"
		});

}]).config(['growlProvider', function(growlProvider) {

	growlProvider.globalDisableCountDown(false)
				.globalTimeToLive(5000);

}]).run(function(amMoment) {
	amMoment.changeLocale('pt-br');
});

licenciamento.controller("AppController", ["$scope", "$rootScope", "applicationService", "$location", "breadcrumb", "mensagem", "$timeout", "$window",
	function($scope, $rootScope, applicationService, $location, breadcrumb, mensagem, $timeout, $window) {

	$rootScope.location = $location;
	$rootScope.confirmacao = {};
	$rootScope.mensagens = app.utils.Mensagens;

	applicationService.findInfo(function(dados) {

		$rootScope.usuarioSessao = dados.data.usuarioSessao;
		$rootScope.config = dados.data.configuracoes;

		if(!$rootScope.usuarioSessao){
			window.location = "/";
		}

		configurarPermissoes($rootScope.usuarioSessao, $rootScope);
	});

	/*  Limpando o breadcrumb ao acessar a tela inicial */
	$scope.$on('$routeChangeSuccess', function(event, rotaAtual, rotaAnterior){

		if(rotaAtual.$$route.originalPath === '/'){

			breadcrumb.set([]);

		}

	});

	$scope.$on("$routeChangeError", function (event, rotaAtual, rotaAnterior, error) {

		if (error.data.texto) {

			mensagem.error(error.data.texto);
		}

		if (rotaAnterior) {

            $timeout(function() {
				
            	$window.history.back();
            }, 0);
        }
        else {

            $location.path("/");
        }
	});

}]);

licenciamento.constant('config', {
	BASE_URL: "/",
	QTDE_ITENS_POR_PAGINA: 10
});


function configurarPermissoes (usuarioSessao, $rootScope) {

	var permissoes = {};

	for (var i = 0; i < usuarioSessao.permissoes.length; i++)
		permissoes[usuarioSessao.permissoes[i]] = true;

	$rootScope.permissoes = permissoes;

}


var services = app.services,
	utils = app.utils,
	filters = app.filters,
	controllers = app.controllers,
	directives = app.directives;


utils.services(licenciamento)
	.add('applicationService', services.ApplicationService)
	.add('mensagem', services.Mensagem)
	.add('request', services.Request)
	.add('animacaoLoader', services.AnimacaoLoader)
	.add('breadcrumb', services.BreadcrumbService)
	.add('modalSimplesService', services.ModalSimples);

utils.filters(licenciamento)
	.add('textoTruncado', filters.TextoTruncado)
	.add('capitalize', filters.Capitalize);

utils.directives(licenciamento)
	.add('enter', directives.Enter, {link: directives.Enter.link, require: 'ngModel'});

licenciamento
	.controller('breadcrumbController', controllers.BreadcrumbController)
	.controller('modalSimplesController', controllers.ModalSimplesController);
