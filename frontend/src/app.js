var licenciamento = angular.module("licenciamento", [
	"ngRoute",
	"ui.bootstrap",
	"caixasEntrada",
	"angular-growl",
	"ngMessages",
	"idf.br-filters",
	"ui.utils.masks",
	"angularMoment",
	"ngFileUpload",
	'ui.select',
	"ngSanitize",
	"analiseJuridica",
	"analiseEmAndamento",
	"froala"
]);

licenciamento.config(["$routeProvider", function($routeProvider) {

	$routeProvider
		.when("/", {
			redirectTo: "/caixa-entrada"
		})
		.when("/consultar-processo", {
			templateUrl: "features/consultarProcesso/consultar-processo.html",
			controller: controllers.ConsultarProcessoController,
			controllerAs: 'consultarProcesso'
		})		
		.otherwise({
			redirectTo: "/"
		});

}]).config(['growlProvider', function(growlProvider) {

	growlProvider.globalDisableCountDown(false)
		.globalTimeToLive(5000);

}]).value('froalaConfig', {

        language: 'pt_br',
        toolbarButtons : ["bold", "italic", "underline", "|", "align", 
                            "formatOL", "formatUL", "strikeThrough", "color",
                            "fontFamily", "fontSize", "undo", "redo", "indent", "outdent",
                            "paragraphFormat","insertLink", "insertLink", "subscript", "superscript"],
		placeholderText: '',
		height: 150

}).run(function(amMoment) {
	amMoment.changeLocale('pt-br');
});

licenciamento.controller("AppController", ["$scope", "$rootScope", "applicationService", "$location", "breadcrumb", "mensagem", "$timeout", "$window",
	function($scope, $rootScope, applicationService, $location, breadcrumb, mensagem, $timeout, $window) {

		$rootScope.location = $location;
		$rootScope.confirmacao = {};
		$rootScope.mensagens = app.utils.Mensagens;

		$rootScope.usuarioSessao = LICENCIAMENTO_CONFIG.usuarioSessao;
		$rootScope.config = LICENCIAMENTO_CONFIG.configuracoes;

		$rootScope.itensMenuPrincipal = [{

			titulo: 'Caixa de entrada (novos processos)',
			icone: 'glyphicon glyphicon-inbox',
			url: '/',
			estaSelecionado: function () {

				return $location.path() === '/caixa-entrada' || $location.path() === '/';
			},
			visivel: function(){

				return true;
			}
		}, {

			titulo: 'Em análise',
			icone: 'glyphicon glyphicon-ok',
			url: '/analise-juridica',
			estaSelecionado: function() {

				return $location.path().indexOf('/analise-juridica') > -1;
			},
			visivel: function() {

				return $rootScope.usuarioSessao.perfilSelecionado.id === app.utils.Perfis.CONSULTOR_JURIDICO;
			}
		},
		{

			titulo: 'Aguardando validação',
			icone: 'glyphicon glyphicon-check',
			url: '/aguardando-validacao',
			estaSelecionado: function () {

				return false;
			},
			visivel: function() {

				return $rootScope.usuarioSessao.perfilSelecionado.id === app.utils.Perfis.COORDENADOR_JURIDICO;
			}			

		}, {
			titulo: 'Consultar processo',
			icone: 'glyphicon glyphicon-search',
			url: '/consultar-processo',
			estaSelecionado: function () {

				return $location.path() === '/consultar-processo';
			},
			visivel: function(){

				return true;
			}
		}];


		if (!$rootScope.usuarioSessao) {
			window.location = $rootScope.config.baseUrl;
		}

		configurarPermissoes($rootScope.usuarioSessao, $rootScope);

		/*  Limpando o breadcrumb ao acessar a tela inicial */
		$scope.$on('$routeChangeSuccess', function (event, rotaAtual, rotaAnterior) {

			if (rotaAtual.$$route.originalPath === '/') {

				breadcrumb.set([]);

			}

		});

		$scope.$on("$routeChangeError", function(event, rotaAtual, rotaAnterior, error) {

			if (error.data.texto) {

				mensagem.error(error.data.texto);
			}

			if (rotaAnterior) {

				$timeout(function () {

					$window.history.back();
				}, 0);
			}
			else {

				$location.path("/");
			}
		});

	}]);

licenciamento.constant('config', {
	BASE_URL: function() {
		if (LICENCIAMENTO_CONFIG.configuracoes.baseURL === "/")
			return LICENCIAMENTO_CONFIG.configuracoes.baseURL;
		else
			return LICENCIAMENTO_CONFIG.configuracoes.baseURL + "/";
	},
	QTDE_ITENS_POR_PAGINA: 10
});


function configurarPermissoes(usuarioSessao, $rootScope) {

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
	.add('modalSimplesService', services.ModalSimples)
	.add('processoService', services.ProcessoService)
	.add('municipioService', services.MunicipioService)
	.add('tipologiaService', services.TipologiaService)
	.add('atividadeService', services.AtividadeService)
	.add('consultorService', services.ConsultorService)
	.add('condicaoService', services.CondicaoService)
	.add('documentoLicenciamentoService', services.DocumentoLicenciamentoService)

utils.filters(licenciamento)
	.add('textoTruncado', filters.TextoTruncado)
	.add('capitalize', filters.Capitalize);

utils.directives(licenciamento)
	.add('enter', directives.Enter, {link: directives.Enter.link, require: 'ngModel'})
	.add('mascara', directives.Mascara, {link: directives.Mascara.link, require: 'ngModel'});

licenciamento
	.controller('breadcrumbController', controllers.BreadcrumbController)
	.controller('modalSimplesController', controllers.ModalSimplesController);

licenciamento
	.component('menuPrincipal', directives.MenuPrincipal);