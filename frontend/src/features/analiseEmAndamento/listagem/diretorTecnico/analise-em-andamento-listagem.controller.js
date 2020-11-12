var AnaliseEmAndamentoDiretorListController = function($scope, config, $location, $rootScope, processoService) {

	$rootScope.tituloPagina = 'EM VALIDAÇÃO PELO DIRETOR TÉCNICO';

	var listagem = this;

	listagem.atualizarListaProcessos = atualizarListaProcessos;
	listagem.atualizarPaginacao = atualizarPaginacao;
	listagem.selecionarTodosProcessos = selecionarTodosProcessos;
	listagem.onPaginaAlterada = onPaginaAlterada;
	listagem.continuarAnaliseGeo = continuarAnaliseGeo;
	listagem.continuarAnaliseTecnica = continuarAnaliseTecnica;
	listagem.usuarioLogadoCodigoPerfil = $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo;
	listagem.processos = [];
	listagem.condicoesEmAnalise = app.utils.CondicaoTramitacao;
	listagem.condicaoTramitacao = app.utils.CondicaoTramitacao.EM_ANALISE_DIRETOR;
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

	function continuarAnaliseTecnica(idAnalise) {

		$rootScope.$broadcast('atualizarContagemProcessos');

		$location.path('/analise-diretor/' + idAnalise.toString());
	}

	function continuarAnaliseGeo(idAnalise) {

		$rootScope.$broadcast('atualizarContagemProcessos');

		$location.path('/analise-diretor/' + idAnalise.toString());
	}
	
	function exibirDadosProcesso(processo) {

        processoService.visualizarProcesso(processo);
	}
	
	listagem.prazoAnaliseTecnica = function(processo) {

		return processo.dataConclusaoAnaliseTecnica ? 'Concluída' : (processo.diasAnaliseTecnica !== null && processo.diasAnaliseTecnica !== undefined ? processo.diasAnaliseTecnica : '-');

	};

};

exports.controllers.AnaliseEmAndamentoDiretorListController = AnaliseEmAndamentoDiretorListController;