var CxEntAnalistaGeoController = function($scope, config, $location, analiseGeoService, mensagem, $rootScope, processoService) {

	$rootScope.tituloPagina = 'AGUARDANDO ANÁLISE GEO';

	var cxEntAnalistaGeo = this;

	cxEntAnalistaGeo.atualizarListaProcessos = atualizarListaProcessos;
	cxEntAnalistaGeo.atualizarPaginacao = atualizarPaginacao;
	cxEntAnalistaGeo.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntAnalistaGeo.onPaginaAlterada = onPaginaAlterada;
	cxEntAnalistaGeo.iniciarAnalise = iniciarAnalise;
	cxEntAnalistaGeo.visualizarProcesso = visualizarProcesso;

	cxEntAnalistaGeo.processos = [];
	cxEntAnalistaGeo.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_ANALISE_GEO;
	cxEntAnalistaGeo.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntAnalistaGeo.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntAnalistaGeo.PrazoAnalise = app.utils.PrazoAnalise;
	cxEntAnalistaGeo.dateUtil = app.utils.DateUtil;
	cxEntAnalistaGeo.disabledFields = _.concat($scope.caixaEntrada.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA);

	function atualizarListaProcessos(processos) {

		cxEntAnalistaGeo.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		cxEntAnalistaGeo.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(cxEntAnalistaGeo.processos, function(processo){

			processo.selecionado = cxEntAnalistaGeo.todosProcessosSelecionados;
		});
	}
	
	function iniciarAnalise(idAnaliseGeo) {

		// analiseGeoService.iniciar({ id : idAnaliseGeo })
		// 	.then(function(response){

		// 		// $rootScope.$broadcast('atualizarContagemProcessos');
		// 		// URL necessária para entrar na análise
		// 		// $location.path('/analise-geo/' + idAnaliseGeo.toString());
		// 		// $location.path('/analise-geo/shape-upload');
			
		// 	}, function(error){

		// 		mensagem.error(error.data.texto);
		// 	});
		$location.path('/analise-geo/shape-upload');
	}

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}	
};

exports.controllers.CxEntAnalistaGeoController = CxEntAnalistaGeoController;