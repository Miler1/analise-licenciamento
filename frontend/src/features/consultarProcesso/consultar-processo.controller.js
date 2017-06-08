var ConsultarProcessoController = function($scope, config, $rootScope) {

	$rootScope.tituloPagina = 'CONSULTAR PROCESSO';

	var consultarProcesso = this;

	consultarProcesso.atualizarListaProcessos = atualizarListaProcessos;
	consultarProcesso.atualizarPaginacao = atualizarPaginacao;
	consultarProcesso.selecionarTodosProcessos = selecionarTodosProcessos;
	consultarProcesso.onPaginaAlterada = onPaginaAlterada;

	consultarProcesso.processos = [];
	consultarProcesso.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	consultarProcesso.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	consultarProcesso.dateUtil = app.utils.DateUtil;

	function atualizarListaProcessos(processos) {

		consultarProcesso.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		consultarProcesso.paginacao.update(totalItens, paginalAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(consultarProcesso.processos, function(processo){

			processo.selecionado = consultarProcesso.todosProcessosSelecionados;
		});
	}
};

exports.controllers.ConsultarProcessoController = ConsultarProcessoController;