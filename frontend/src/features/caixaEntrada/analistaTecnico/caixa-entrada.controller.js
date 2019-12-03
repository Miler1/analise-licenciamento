var CxEntAnalistaTecnicoController = function($scope, config, $location, analiseTecnicaService, mensagem, $rootScope, processoService) {

	$rootScope.tituloPagina = 'ANÁLISE TÉCNICA';

	var cxEntAnalistaTecnico = this;

	cxEntAnalistaTecnico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntAnalistaTecnico.atualizarPaginacao = atualizarPaginacao;
	cxEntAnalistaTecnico.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntAnalistaTecnico.onPaginaAlterada = onPaginaAlterada;
	cxEntAnalistaTecnico.iniciarAnalise = iniciarAnalise;
	cxEntAnalistaTecnico.visualizarProcesso = visualizarProcesso;

	cxEntAnalistaTecnico.processos = [];
	cxEntAnalistaTecnico.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_ANALISE_TECNICA;
	cxEntAnalistaTecnico.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntAnalistaTecnico.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntAnalistaTecnico.PrazoAnalise = app.utils.PrazoAnalise;
	cxEntAnalistaTecnico.dateUtil = app.utils.DateUtil;
	cxEntAnalistaTecnico.disabledFields = _.concat($scope.caixaEntrada.disabledFields, app.DISABLED_FILTER_FIELDS.ANALISTA_GEO, app.DISABLED_FILTER_FIELDS.GERENCIA, app.DISABLED_FILTER_FIELDS.ANALISTA_TECNICO);

	function atualizarListaProcessos(processos) {

		cxEntAnalistaTecnico.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		cxEntAnalistaTecnico.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(cxEntAnalistaTecnico.processos, function(processo){

			processo.selecionado = cxEntAnalistaTecnico.todosProcessosSelecionados;
		});
	}

	function iniciarAnalise(idAnaliseTecnica) {

		analiseTecnicaService.iniciar({ id : idAnaliseTecnica })
			.then(function(response){

				$rootScope.$broadcast('atualizarContagemProcessos');
				$location.path('/analise-tecnica/' + idAnaliseTecnica.toString());
			
			}, function(error){

				mensagem.error(error.data.texto);
			});
	}

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}

	cxEntAnalistaTecnico.getPrazoAnaliseTecnica = function(dataParecerGerente, prazo) {

		if(dataParecerGerente) {

			var dataVencimento = cxEntAnalistaTecnico.dateUtil.somaPrazoEmDias(dataParecerGerente, prazo);

			return cxEntAnalistaTecnico.dateUtil.getDiasRestantes(dataVencimento);

		}

		return '-';

	};

};

exports.controllers.CxEntAnalistaTecnicoController = CxEntAnalistaTecnicoController;
