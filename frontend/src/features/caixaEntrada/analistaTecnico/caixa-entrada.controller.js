var CxEntAnalistaTecnicoController = function($scope, config, consultorService, mensagem, $uibModal, $rootScope, processoService) {

	$rootScope.tituloPagina = 'AGUARDANDO ANÁLISE TÉCNICA';

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
	cxEntAnalistaTecnico.dateUtil = app.utils.DateUtil;

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

		//TODO Iniciar a análise
	}

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}	
};

exports.controllers.CxEntAnalistaTecnicoController = CxEntAnalistaTecnicoController;