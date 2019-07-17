var licenciamento = angular.module("licenciamento", [
	"ngRoute",
	"ui.bootstrap",
	"caixasEntrada",
	"aguardandoValidacao",
	"aguardandoAssinatura",
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
	"analiseManejo",
	"ui.bootstrap",
	"textAngular",
	"analiseTecnica",
	"as.sortable",
	"analiseGeo"
]);

licenciamento.config(["$routeProvider", function($routeProvider) {

	$routeProvider
		.when("/", {
			redirectTo: function(){

				if (LICENCIAMENTO_CONFIG.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.APROVADOR &&
					LICENCIAMENTO_CONFIG.usuarioSessao.autenticadoViaToken){

					return "/aguardando-assinatura";

				} else if (LICENCIAMENTO_CONFIG.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.APROVADOR ||
					LICENCIAMENTO_CONFIG.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ADMINISTRATIVO_JURIDICO) {

					return "/consultar-processo";

				} else if (LICENCIAMENTO_CONFIG.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_SIMLAM ||
					LICENCIAMENTO_CONFIG.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.APROVADOR) {

					return '/listagem-processo-manejo';
				}

				return "/caixa-entrada";
			}

		})
		.when("/consultar-processo", {
			templateUrl: "features/consultarProcesso/consultar-processo.html",
			controller: controllers.ConsultarProcessoController,
			controllerAs: 'consultarProcesso'
		})
		.when("/aguardando-assinatura", {
			templateUrl: "features/aguardandoAssinatura/listagem/aguardando-assinatura-listagem.html",
			controller: controllers.AguardandoAssinaturaAprovadorListController,
			controllerAs: 'listagem'
		})
		.when("/consultar-licencas-emitidas", {
			templateUrl: "features/consultarLicencasEmitidas/consultar-licencas-emitidas.html",
			controller: controllers.ConsultarLicencasEmitidasController,
			controllerAs: 'consultarLicencas'
		})
		.when("/listagem-processo-manejo", {
			templateUrl: "features/analiseManejo/listagem/listagem-processo-manejo.html",
			controller: controllers.ListagemProcessoManejoController,
			controllerAs: 'listagemProcessoManejo'
		})
		.otherwise({
			redirectTo: "/"
		});

}]).config(['growlProvider', function(growlProvider) {

	growlProvider.globalDisableCountDown(false)
		.globalTimeToLive(5000);

}]).config(function($provide){

	$provide.decorator('taOptions',['taRegisterTool','$delegate', function(taRegisterTool, taOptions){

		taOptions.toolbar = [
			['h1','h2','h3','h4','h5','h6','p',
			'bold','italics','underline','strikeThrough','ul','ol','redo','undo','clear',
			'justifyLeft', 'justifyCenter', 'justifyRight', 'justifyFull','indent', 'outdent','insertLink']
		];

		return taOptions;
	}]);

	$provide.decorator('taTranslations', ['$delegate', function(taTranslations) {

		taTranslations.justifyLeft.tooltip = 'Alinhar a esquerda';
		taTranslations.justifyCenter.tooltip = 'Centralizar';
		taTranslations.justifyRight.tooltip = 'Alinhar a direita';
		taTranslations.justifyFull.tooltip = 'Justificar';
		taTranslations.heading.tooltip = 'Cabeçalho ';
		taTranslations.bold.tooltip = 'Negrito';
		taTranslations.italic.tooltip = 'Itálico';
		taTranslations.underline.tooltip = 'Sublinhado';
		taTranslations.insertLink.tooltip = 'Inserir link';
		taTranslations.insertLink.dialogPrompt = 'Digite a URL';
		taTranslations.editLink.targetToggle.buttontext = 'Abrir em nova aba';
		taTranslations.editLink.reLinkButton.tooltip = 'Refazer link';
		taTranslations.editLink.unLinkButton.tooltip = 'Remover link';
		taTranslations.clear.tooltip = 'Limpar formatação';

		return taTranslations;

	}]);



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
		$rootScope.perfis = app.utils.Perfis;

		if (!$rootScope.usuarioSessao) {
			window.location = $rootScope.config.baseUrl;
		}

		$rootScope.itensMenuPrincipal = [{

			titulo: 'Caixa de entrada',
			icone: 'glyphicon glyphicon-inbox',
			url: function() {
				return '/';
			},
			countItens: true,
			estaSelecionado: function () {

				return $location.path() === '/caixa-entrada';
			},
			visivel: function(){

				return [
					app.utils.Perfis.GERENTE,
					app.utils.Perfis.ANALISTA_GEO,
					app.utils.Perfis.ANALISTA_CAR,
						app.utils.Perfis.ANALISTA_TECNICO
					].indexOf($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) > -1;
			},
			condicaoTramitacao: function() {

				if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.COORDENADOR_JURIDICO)
					return app.utils.CondicaoTramitacao.AGUARDANDO_VINCULACAO_JURIDICA;
				else if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.CONSULTOR_JURIDICO)
					return app.utils.CondicaoTramitacao.AGUARDANDO_ANALISE_JURIDICA;
				else if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.COORDENADOR_TECNICO)
					return app.utils.CondicaoTramitacao.AGUARDANDO_VINCULACAO_TECNICA_PELO_COORDENADOR;
				else if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.GERENTE)
					return app.utils.CondicaoTramitacao.AGUARDANDO_VINCULACAO_TECNICA_PELO_GERENTE;
				else if ($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_TECNICO)
					return app.utils.CondicaoTramitacao.AGUARDANDO_ANALISE_TECNICA;
				else if ($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_GEO)
					return app.utils.CondicaoTramitacao.AGUARDANDO_ANALISE_GEO;
			},
			deveFiltrarPorUsuario: true,
			codigoPerfilSelecionado: function(){

				return $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo;
			}
		},
		{

			titulo: 'Em análise',
			icone: 'glyphicon glyphicon-ok',
			url: function() {

				if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_GEO) {

					return '/analise-geo';

				} else {

					 return '/analise-tecnica';
				}
			},
			countItens: true,
			estaSelecionado: function() {

				return $location.path().indexOf('/analise-geo') > -1 ||
					$location.path().indexOf('/analise-tecnica') > -1;
			},
			visivel: function() {

				return [app.utils.Perfis.ANALISTA_GEO,
				app.utils.Perfis.ANALISTA_TECNICO].indexOf($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) > -1;
			},
			condicaoTramitacao: function () {

				if ($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_GEO)
					return app.utils.CondicaoTramitacao.EM_ANALISE_GEO;
				else if ($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_TECNICO)
					return app.utils.CondicaoTramitacao.EM_ANALISE_TECNICA;
			},
			deveFiltrarPorUsuario: true,
			codigoPerfilSelecionado: function(){

				return $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo;
			}
		},
		{

			titulo: 'Aguardando validação',
			icone: 'glyphicon glyphicon-check',
			url: function() {

				return '/aguardando-validacao';
			},
			countItens: true,
			estaSelecionado: function () {

				return $location.path().indexOf('/aguardando-validacao') > -1;
			},
			visivel: function() {

				return [app.utils.Perfis.GERENTE].indexOf($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) > -1;
			},
			condicaoTramitacao: function () {

				if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.GERENTE)
					return app.utils.CondicaoTramitacao.AGUARDANDO_VALIDACAO_TECNICA_PELO_GERENTE;
			},
			deveFiltrarPorUsuario: true,
			codigoPerfilSelecionado: function(){

				return $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo;
			}
		},
		{

			titulo: 'Aguardando assinatura',
			icone: 'glyphicon glyphicon-pencil',
			url: function() {

				return '/aguardando-assinatura';
			},
			countItens: true,
			estaSelecionado: function () {

				return $location.path().indexOf('/aguardando-assinatura') > -1;
			},
			visivel: function(){

				return $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.APROVADOR && LICENCIAMENTO_CONFIG.usuarioSessao.autenticadoViaToken;
			},

			condicaoTramitacao: function(){

				return app.utils.CondicaoTramitacao.AGUARDANDO_ASSINATURA_APROVADOR;
			},
            deveFiltrarPorUsuario: true
		},
		{
			titulo: 'Consultar processo',
			icone: 'glyphicon glyphicon-search',
			url: function() {

				return '/consultar-processo';
			},
			estaSelecionado: function () {

				return $location.path() === '/consultar-processo';
			},
			visivel: function(){

				return [
					app.utils.Perfis.COORDENADOR_JURIDICO,
					app.utils.Perfis.ADMINISTRATIVO_JURIDICO,
					app.utils.Perfis.CONSULTOR_JURIDICO,
					app.utils.Perfis.COORDENADOR_TECNICO,
					app.utils.Perfis.GERENTE_TECNICO,
					app.utils.Perfis.ANALISTA_TECNICO,
					app.utils.Perfis.APROVADOR
				].indexOf($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) !== -1;
			}
		},
		{
			titulo: 'Consultar títulos emitidos',
			icone: 'glyphicon glyphicon-list-alt',
			url: function() {

				return '/consultar-licencas-emitidas';
			},
			estaSelecionado: function () {

				return $location.path() === '/consultar-licencas-emitidas';
			},
			visivel: function(){

				return [
					app.utils.Perfis.COORDENADOR_JURIDICO,
					app.utils.Perfis.ADMINISTRATIVO_JURIDICO,
					app.utils.Perfis.CONSULTOR_JURIDICO,
					app.utils.Perfis.COORDENADOR_TECNICO,
					app.utils.Perfis.GERENTE_TECNICO,
					app.utils.Perfis.ANALISTA_TECNICO,
					app.utils.Perfis.APROVADOR
				].indexOf($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) !== -1;
			}
		},
		{
			titulo: 'Manejo Digital',
			icone: 'glyphicon glyphicon-list-alt',
			url: function() {

				return '/listagem-processo-manejo';
			},
			estaSelecionado: function () {

				return $location.path() === '/listagem-processo-manejo';
			},
			visivel: function(){

				return $scope.verificarPermissao([
					app.utils.Permissoes.VISUALIZAR_PROCESSO_MANEJO,
					app.utils.Permissoes.ANALISAR_PROCESSO_MANEJO,
					app.utils.Permissoes.LISTAR_PROCESSO_MANEJO
				]);
			}
		}];

		/*  Limpando o breadcrumb ao acessar a tela inicial */
		$scope.$on('$routeChangeSuccess', function (event, rotaAtual, rotaAnterior) {

			if (rotaAtual.$$route.originalPath === '/') {

				breadcrumb.set([]);

			}

		});

		$scope.$on("$routeChangeError", function(event, rotaAtual, rotaAnterior, error) {

			if (error.data && error.data.texto) {

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


		$scope.verificarPermissao = function (permissoesSolicitadas) {

			var result = false;

			_.forEach(LICENCIAMENTO_CONFIG.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.permissoes, function(permissaoUsuario) {

				if (!result) {

					_.forEach(permissoesSolicitadas, function (permissaoSolicitada) {

						if (permissaoSolicitada === permissaoUsuario.codigo) {

							result = true;
						}
					});
				}

			});

			return result;
		};

	}]);

licenciamento.constant('config', {
	BASE_URL: function() {
		if (LICENCIAMENTO_CONFIG.configuracoes.baseURL === "/")
			return LICENCIAMENTO_CONFIG.configuracoes.baseURL;
		else
			return LICENCIAMENTO_CONFIG.configuracoes.baseURL + "/";
	},
	QTDE_ITENS_POR_PAGINA: 10
}).constant('tamanhoMaximoArquivoAnaliseMB', 10)
	.constant('TiposAnalise', {

		JURIDICA: 'JURIDICA',
		TECNICA: 'TECNICA',
		GEO: 'GEO'
	})
	.constant('TiposSetores', {
		SECRETARIA: 'SECRETARIA',
		DIRETORIA: 'DIRETORIA',
		COORDENADORIA: 'COORDENADORIA',
		GERENCIA:'GERENCIA'
	});

var services = app.services,
	utils = app.utils,
	filters = app.filters,
	controllers = app.controllers,
	directives = app.directives;


utils.services(licenciamento)
	.add('inconsistenciaService', services.InconsistenciaService)
	.add('applicationService', services.ApplicationService)
	.add('analiseTecnicaService', services.AnaliseTecnicaService)
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
	.add('documentoAnaliseService', services.DocumentoAnaliseService)
	.add('analiseJuridicaService', services.AnaliseJuridicaService)
	.add('uploadService', services.UploadService)
	.add('imovelService', services.ImovelService)
	.add('analistaService', services.AnalistaService)
	.add('analiseTecnicaService', services.AnaliseTecnicaService)
	.add('setorService', services.SetorService)
	.add('gerenteService', services.GerenteService)
	.add('aprovadorService', services.AprovadorService)
	.add('analiseService', services.AnaliseService)
	.add('coordenadorService', services.CoordenadorService)
	.add('licencaEmitidaService', services.LicencaEmitidaService)
	.add('tipoLicencaService', services.TipoLicencaService)
	.add('licencaService', services.LicencaService)
	.add('dispensaLicencaService', services.DispensaLicencaService)
	.add('notificacaoService', services.NotificacaoService)
	.add('processoManejoService', services.ProcessoManejoService)
	.add('analiseManejoService', services.AnaliseManejoService)
	.add('observacaoService', services.ObservacaoService)
	.add('tipologiaManejoService', services.TipologiaManejoService)
	.add('atividadeManejoService', services.AtividadeManejoService)
	.add('tipoLicencaManejoService', services.TipoLicencaManejoService)
	.add('documentoShapeService', services.DocumentoShapeService)
	.add('analiseGeoService', services.AnaliseGeoService);


utils.filters(licenciamento)
	.add('textoTruncado', filters.TextoTruncado)
	.add('capitalize', filters.Capitalize);

utils.directives(licenciamento)
	.add('enter', directives.Enter, {link: directives.Enter.link, require: 'ngModel'})
	.add('mascara', directives.Mascara, {link: directives.Mascara.link, require: 'ngModel'});

licenciamento
	.controller('breadcrumbController', controllers.BreadcrumbController)
	.controller('modalSimplesController', controllers.ModalSimplesController)
	.controller('visualizacaoProcessoController', controllers.VisualizacaoProcessoController)
	.controller('analiseGeoController', controllers.AnaliseGeoController)
	.controller('legislacaoController', controllers.LegislacaoController)
	.controller('inconsistenciaController',controllers.InconsistenciaController);


licenciamento
	.component('menuPrincipal', directives.MenuPrincipal)
	.component('avaliarDocumento', directives.AvaliarDocumento)
	.component('emitirLicenca', directives.EmitirLicenca)
	.component('modalParecerDocumento', directives.ModalParecerDocumento)
	.component('resumoEmpreendimento', directives.ResumoEmpreendimento)
	.component('fichaImovel', directives.FichaImovel)
	.component('parecer', directives.Parecer)
	.component('modalInformacoesAnaliseJuridica', directives.ModalInformacoesAnaliseJuridica)
	.component('modalInformacoesLicenca', directives.ModalInformacoesLicenca)
	.component('solicitarAjusteAprovador', directives.SoliciarAjusteAprovador)
	.component('visualizarAnaliseJuridica', directives.VisualizarAnaliseJuridica)
	.component('modalFichaImovel', directives.ModalFichaImovel)
	.component('filtroLicencasEmitidas', directives.FiltroLicencasEmitidas)
	.component('tabelaLicencas', directives.TabelaLicencas)
	.component('modalVisualizarLicenca', directives.ModalVisualizarLicenca);
