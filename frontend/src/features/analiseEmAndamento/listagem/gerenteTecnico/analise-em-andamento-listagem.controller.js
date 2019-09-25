var AnaliseEmAndamentoGerenteListController = function($scope, config, $location, $rootScope, processoService) {

	$rootScope.tituloPagina = 'EM ANÁLISE GERENTE';

	var listagem = this;

	listagem.atualizarListaProcessos = atualizarListaProcessos;
	listagem.atualizarPaginacao = atualizarPaginacao;
	listagem.selecionarTodosProcessos = selecionarTodosProcessos;
	listagem.onPaginaAlterada = onPaginaAlterada;
	listagem.continuarAnalise = continuarAnalise;

	listagem.usuarioLogadoCodigoPerfil = $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo;
	listagem.processos = [];
	listagem.condicaoTramitacao = app.utils.CondicaoTramitacao.EM_ANALISE_GERENTE;
	listagem.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagem.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	listagem.PrazoAnalise = app.utils.PrazoAnalise;
	listagem.dateUtil = app.utils.DateUtil;
	listagem.exibirDadosProcesso = exibirDadosProcesso;
	listagem.disabledFields = _.concat($scope.analiseEmAndamentoListagem.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA);

	function atualizarListaProcessos(processos) {

		listagem.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		listagem.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(listagem.processos, function(processo){

			processo.selecionado = listagem.todosProcessosSelecionados;
		});
	}

	function continuarAnalise(idAnalise) {

		$location.path('/analise-gerente/' + idAnalise.toString());
	}	

	function exibirDadosProcesso(processo) {

        processoService.visualizarProcesso(processo);
    }
};

exports.controllers.AnaliseEmAndamentoGerenteListController = AnaliseEmAndamentoGerenteListController;