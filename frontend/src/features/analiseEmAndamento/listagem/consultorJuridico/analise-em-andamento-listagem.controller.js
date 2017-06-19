var AnaliseEmAndamentoJuridicoListController = function($scope, config, $location, $rootScope, processoService) {

	$rootScope.tituloPagina = 'EM ANÁLISE JURÍDICA';

	var listagem = this;

	listagem.atualizarListaProcessos = atualizarListaProcessos;
	listagem.atualizarPaginacao = atualizarPaginacao;
	listagem.selecionarTodosProcessos = selecionarTodosProcessos;
	listagem.onPaginaAlterada = onPaginaAlterada;
	listagem.continuarAnalise = continuarAnalise;

	listagem.processos = [];
	listagem.condicaoTramitacao = app.utils.CondicaoTramitacao.EM_ANALISE_JURIDICA;
	listagem.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagem.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	listagem.dateUtil = app.utils.DateUtil;
	listagem.exibirDadosProcesso = exibirDadosProcesso;

	function atualizarListaProcessos(processos) {

		listagem.processos = processos;
	}

	function atualizarPaginacao(totalItens) {

		listagem.paginacao.update(totalItens, listagem.paginacao.paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(listagem.processos, function(processo){

			processo.selecionado = listagem.todosProcessosSelecionados;
		});
	}

	function continuarAnalise(idAnaliseJuridica) {

		$location.path('/analise-juridica/' + idAnaliseJuridica.toString());
	}	

	function exibirDadosProcesso(idProcesso) {

        processoService.visualizarProcesso({ idProcesso:idProcesso});
    }	
};

exports.controllers.AnaliseEmAndamentoJuridicoListController = AnaliseEmAndamentoJuridicoListController;