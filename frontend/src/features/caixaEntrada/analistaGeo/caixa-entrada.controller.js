var CxEntAnalistaGeoController = function($scope, config, $location, analiseGeoService, mensagem, $rootScope, $uibModal ,processoService) {

	$rootScope.tituloPagina = 'AGUARDANDO ANÁLISE GEO';

	var cxEntAnalistaGeo = this;

	cxEntAnalistaGeo.atualizarListaProcessos = atualizarListaProcessos;
	cxEntAnalistaGeo.atualizarPaginacao = atualizarPaginacao;
	cxEntAnalistaGeo.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntAnalistaGeo.onPaginaAlterada = onPaginaAlterada;
	cxEntAnalistaGeo.iniciarAnalise = iniciarAnalise;
	cxEntAnalistaGeo.iniciarUploadShapes = iniciarUploadShapes;
	cxEntAnalistaGeo.visualizarProcesso = visualizarProcesso;
	cxEntAnalistaGeo.processos = [];
	cxEntAnalistaGeo.condicaoTramitacao = app.utils.CondicaoTramitacao;
	cxEntAnalistaGeo.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntAnalistaGeo.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntAnalistaGeo.PrazoAnalise = app.utils.PrazoAnalise;
	cxEntAnalistaGeo.dateUtil = app.utils.DateUtil;
	cxEntAnalistaGeo.origemNotificacao = app.utils.OrigemNotificacao;
	cxEntAnalistaGeo.disabledFields = _.concat($scope.caixaEntrada.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA, app.DISABLED_FILTER_FIELDS.ANALISTA_TECNICO, app.DISABLED_FILTER_FIELDS.ANALISTA_GEO);
	cxEntAnalistaGeo.notificacaoAtendida = notificacaoAtendida;
	cxEntAnalistaGeo.visualizarNotificacao = visualizarNotificacao;

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

	function iniciarAnalise(processo) {

		analiseGeoService.iniciar({ id : processo.idAnaliseGeo })
			.then(function(response){

				cxEntAnalistaGeo.iniciarUploadShapes(processo);

			}, function(error){
				mensagem.error(error.data.texto);
			});
	}

	function iniciarUploadShapes(processo){
		$rootScope.processo = processo;

		$location.path('/shape-upload/' + processo.idProcesso.toString());
	}

	function visualizarProcesso(processo) {
		return processoService.visualizarProcesso(processo);
	}


	cxEntAnalistaGeo.solicitarDesvinculoAnaliseGeo =  function(processo){

				$uibModal.open({
					controller: 'desvinculoController',
					controllerAs: 'desvinculoCtrl',
					backdrop: 'static',
					templateUrl: 'features/caixaEntrada/analistaGeo/modalDesvinculo.html',
					size: 'lg',
					resolve: {

						idAnaliseGeo: function(){
							return processo.idAnaliseGeo;
						},
						idProcesso: function(){
							return processo.idProcesso;
						}
					}

				});
	};


	function visualizarNotificacao(processo) {
		return processoService.visualizarNotificacao(processo);
	}

	function notificacaoAtendida(processo) {

		if (processo.idAnalistaGeoAnterior !== undefined) {

			return processo && processo.retificacao && processo.idAnalistaGeoAnterior === processo.idAnalistaGeo;

		} else {

			return processo && processo.retificacao;
		}
		
	}
};

exports.controllers.CxEntAnalistaGeoController = CxEntAnalistaGeoController;
