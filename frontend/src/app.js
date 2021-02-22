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
	"analiseGeo",
	"ui.bootstrap",
	"textAngular",
	"analiseTecnica",
	"as.sortable",
	"analiseGeo",
	"summernote",
	"ngBootbox"

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
		.when("/parecer-orgao/:idComunicado", {
			templateUrl: "features/parecerOrgao/parecerOrgao.html",
			controller: controllers.ParecerOrgaoController,
			controllerAs: 'parecerOrgao'
		})
		.when("/parecer-juridico/:idParecerJuridico", {
			templateUrl: "features/parecerJuridico/parecerJuridico.html",
			controller: controllers.ParecerJuridicoController,
			controllerAs: 'parecerJuridico'
		})
		.otherwise({
			redirectTo: "/"
		});

}]).config(['growlProvider', function(growlProvider) {

	growlProvider.globalDisableCountDown(false)
		.globalTimeToLive(10000);

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



}).run(function(amMoment, $rootScope, growlMessages) {
	amMoment.changeLocale('pt-br');
	$rootScope.$on('$routeChangeSuccess', function(){
		growlMessages.destroyAllMessages();
	});
});

licenciamento.controller("AppController", ["$injector", "$scope", "$rootScope", "applicationService", "$location","$routeParams","$route", "breadcrumb", "mensagem", "$timeout", "$window",
	function($injector, $scope, $rootScope, applicationService, $location, breadcrumb, mensagem, $timeout,$routeParams,$route, $window) {

		$rootScope.location = $location;
		$rootScope.confirmacao = {};
		$rootScope.mensagens = app.utils.Mensagens;

		$rootScope.usuarioSessao = LICENCIAMENTO_CONFIG.usuarioSessao;
		$rootScope.config = LICENCIAMENTO_CONFIG.configuracoes;
		$rootScope.perfis = app.utils.Perfis;
		var appController = this;

		if (!$rootScope.usuarioSessao && !$rootScope.location.$$url.includes('/parecer-orgao') && !$rootScope.location.$$url.includes('/parecer-juridico')) {
			window.location = $rootScope.config.baseURL;
		}

		$rootScope.isRoute = function(path) {

			return $location.path().includes(path);
		};

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
					app.utils.Perfis.COORDENADOR,
					app.utils.Perfis.ANALISTA_GEO,
					app.utils.Perfis.ANALISTA_CAR,
					app.utils.Perfis.DIRETOR,
					app.utils.Perfis.ANALISTA_TECNICO
					].indexOf($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) > -1;
			},
			condicaoTramitacao: function() {

				if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.COORDENADOR_JURIDICO)
					return app.utils.CondicaoTramitacao.AGUARDANDO_VINCULACAO_JURIDICA;
				else if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.CONSULTOR_JURIDICO)
					return app.utils.CondicaoTramitacao.AGUARDANDO_ANALISE_JURIDICA;
				else if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.COORDENADOR)
					return app.utils.CondicaoTramitacao.AGUARDANDO_VINCULACAO_TECNICA_PELO_COORDENADOR;
				else if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.COORDENADOR)
					return app.utils.CondicaoTramitacao.CAIXA_ENTRADA_COORDENADOR;
				else if ($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_TECNICO)
					return app.utils.CondicaoTramitacao.AGUARDANDO_ANALISE_TECNICA;
				else if ($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_GEO)
					return app.utils.CondicaoTramitacao.CAIXA_ENTRADA_ANALISTA_GEO;
				else if ($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.DIRETOR)
					return app.utils.CondicaoTramitacao.AGUARDANDO_VALIDACAO_DIRETORIA;

			},
			deveFiltrarPorUsuario: true,
			codigoPerfilSelecionado: function(){

				return $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo;
			}
		},
		{

			titulo: 'Aguardando validação',
			icone: 'glyphicon glyphicon-pencil',
			url: function() {
				return '/';
			},
			countItens: true,
			estaSelecionado: function () {

				return $location.path() === '/caixa-entrada';
			},
			visivel: function(){

				return [
					app.utils.Perfis.SECRETARIO,
					].indexOf($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) > -1;
			},
			condicaoTramitacao: function() {

				if ($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.SECRETARIO)
					return app.utils.CondicaoTramitacao.AGUARDANDO_ASSINATURA_SECRETARIO;
			},
			deveFiltrarPorUsuario: true,
			codigoPerfilSelecionado: function(){

				return $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo;
			}
		},
		{

			titulo: 'Em análise',
			icone: 'glyphicon glyphicon-edit',
			url: function() {

				if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_GEO) {

					return '/analise-geo';

				} else if ($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_TECNICO){

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

			titulo: 'Validação da análise',
			icone: 'glyphicon glyphicon-edit',
			url: function() {

				if ($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.COORDENADOR){

					return '/analise-coordenadorTecnico';

				} else if ($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.DIRETOR){

					return '/analise-diretor';

				} else if ($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.SECRETARIO){

					return '/analise-secretario';

				}
			},
			countItens: true,
			estaSelecionado: function() {

				return $location.path().indexOf('/analise-coordenadorTecnico') > -1 ||
					$location.path().indexOf('/analise-diretor') > -1 ||
					$location.path().indexOf('/analise-secretario') > -1 ||
					$location.path().indexOf('/analise-tecnica-coordenadorTecnico') > -1;

			},
			visivel: function() {

				return [app.utils.Perfis.COORDENADOR,
					app.utils.Perfis.DIRETOR,
					app.utils.Perfis.SECRETARIO].indexOf($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) > -1;
			},
			condicaoTramitacao: function () {

				if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.COORDENADOR)
					return app.utils.CondicaoTramitacao.MENU_EM_ANALISE_COORDENADOR;
				else if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.DIRETOR)
					return app.utils.CondicaoTramitacao.EM_ANALISE_DIRETOR;
				else if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.SECRETARIO)
					return app.utils.CondicaoTramitacao.EM_ANALISE_SECRETARIO;
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

				return [app.utils.Perfis.COORDENADOR].indexOf($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) > -1;
			},
			condicaoTramitacao: function () {

				if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.COORDENADOR)
					return app.utils.CondicaoTramitacao.AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR;
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

				return $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.SECRETARIO && LICENCIAMENTO_CONFIG.usuarioSessao.autenticadoViaToken;
			},

			condicaoTramitacao: function(){

				return app.utils.CondicaoTramitacao.AGUARDANDO_ASSINATURA_SECRETARIO;
			},
            deveFiltrarPorUsuario: true
		},
		{
			titulo: 'Consultar processo/protocolo',
			icone: 'glyphicon glyphicon-search',
			url: function() {

				return '/consultar-processo';
			},
			estaSelecionado: function () {

				return $location.path() === '/consultar-processo';
			},
			visivel: function(){

				return [
					app.utils.Perfis.ANALISTA_GEO,
					app.utils.Perfis.ANALISTA_CAR,
					app.utils.Perfis.ANALISTA_TECNICO,
					app.utils.Perfis.DIRETOR,
					app.utils.Perfis.COORDENADOR,
					app.utils.Perfis.SECRETARIO
				].indexOf($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo) !== -1;
			},
			deveFiltrarPorUsuario: true
		},
		{
			titulo: 'Consultar licenças emitidas',
			icone: 'glyphicon glyphicon-search',
			url: function() {

				return '/consultar-licencas-emitidas';
			},
			estaSelecionado: function () {

				return $location.path() === '/consultar-licencas-emitidas';
			},
			visivel: function(){

				return [
					app.utils.Perfis.COORDENADOR,
					app.utils.Perfis.SECRETARIO,
					app.utils.Perfis.DIRETOR
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
	QTDE_ITENS_POR_PAGINA: 10,
	BASE_URL_GEOSERVER: LICENCIAMENTO_CONFIG.configuracoes.baseUrlGeoServer

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
	.add('documentoService', services.DocumentoService)
	.add('analiseGeoService', services.AnaliseGeoService)
	.add('validacaoShapeService', services.ValidacaoShapeService)
	.add('empreendimentoService', services.EmpreendimentoService)
	.add('desvinculoService', services.DesvinculoService)
	.add('wmsTileService', services.WMSTileService)
	.add('tiposSobreposicaoService', services.TiposSobreposicaoService)
	.add('validacaoAnaliseCoordenadorService', services.ValidacaoAnaliseCoordenadorService)
	.add('parecerOrgaoService', services.ParecerOrgaoService)
	.add('parecerJuridicoService', services.ParecerJuridicoService)
	.add('parecerAnalistaGeoService', services.ParecerAnalistaGeoService)
	.add('parecerCoordenadorService', services.ParecerCoordenadorService)
	.add('questionarioService', services.QuestionarioService)
	.add('parecerAnalistaTecnicoService', services.ParecerAnalistaTecnicoService)
	.add('parecerDiretorTecnicoService', services.ParecerDiretorTecnicoService)
	.add('parecerSecretarioService', services.ParecerSecretarioService);

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
	.controller('visualizacaoNotificacaoController', controllers.VisualizacaoNotificacaoController)
	.controller('analiseGeoController', controllers.AnaliseGeoController)
	.controller('legislacaoController', controllers.LegislacaoController)
	.controller('legislacaoController', controllers.LegislacaoController)
	.controller('validacaoShapeController', controllers.ValidacaoShapeController)
	.controller('painelMapaController', controllers.PainelMapaController)
	.controller('uploadShapesController', controllers.UploadShapesController)
	.controller('inconsistenciaController',controllers.InconsistenciaController)
	.controller('desvinculoController', controllers.DesvinculoController)
	.controller('modalParecerJuridicoController', controllers.ModalParecerJuridicoController)
	.controller('desvinculoAnaliseTecnicaController', controllers.DesvinculoAnaliseTecnicaController)
	.controller('parecerOrgaoController', controllers.ParecerOrgaoController)
	.controller('parecerJuridicoController', controllers.ParecerJuridicoController)
	.controller('desvinculoCoordenadorController', controllers.DesvinculoCoordenadorController)
	.controller('ListagemProcessoManejoController', controllers.ListagemProcessoManejoController)
	.controller('visualizarJustificativasController',controllers.VisualizarJustificativasController)
	.controller('visualizarAjustesController',controllers.VisualizarAjustesController)
	.controller('inconsistenciaTecnicaController',controllers.InconsistenciaTecnicaController)
	.controller('modalInconsistenciaVistoriaController', controllers.ModalInconsistenciaVistoriaController)
	.controller('modalCondicionanteController', controllers.ModalCondicionanteController)
	.controller('historicoAnaliseGeoCtrl', controllers.HistoricoAnaliseGeoCtrl)
	.controller('historicoAnaliseTecnicaCtrl', controllers.HistoricoAnaliseTecnicaCtrl)
	.controller('modalRestricaoController', controllers.ModalRestricaoController);

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
	.component('modalVisualizarLicenca', directives.ModalVisualizarLicenca)
	.component('modalOficioRestricao', directives.ModalOficioRestricao)
	.component('modalVisualizarSolicitacaoLicenca', directives.ModalVisualizarSolicitacaoLicenca)
	.component('modalNotificacaoRestricao', directives.ModalNotificacaoRestricao)
	.component('modalVisualizarQuestionario',directives.ModalVisualizarQuestionario)
	.component('modalVisualizarInconsistenciaTecnica',directives.ModalVisualizarInconsistenciaTecnica);
