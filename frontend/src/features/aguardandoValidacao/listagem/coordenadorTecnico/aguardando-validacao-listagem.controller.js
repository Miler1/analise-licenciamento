var AguardandoValidacaoTecnicaListController = function($scope, config, $location, $rootScope, processoService) {

	$rootScope.tituloPagina = 'AGUARDANDO VALIDACÃO TÉCNICA';

	var listagem = this;

	listagem.atualizarListaProcessos = atualizarListaProcessos;
	listagem.atualizarPaginacao = atualizarPaginacao;
	listagem.selecionarTodosProcessos = selecionarTodosProcessos;
	listagem.onPaginaAlterada = onPaginaAlterada;
	listagem.validarAnalise = validarAnalise;

	listagem.processos = [];
	listagem.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR;
	listagem.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagem.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	listagem.PrazoAnalise = app.utils.PrazoAnalise;
	listagem.dateUtil = app.utils.DateUtil;
	listagem.exibirDadosProcesso = exibirDadosProcesso;

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

	function validarAnalise(idAnaliseTecnica) {

		$location.path('/aguardando-validacao/' + idAnaliseTecnica.toString());
	}

	function exibirDadosProcesso(processo) {

        processoService.visualizarProcesso(processo);
    }
};

exports.controllers.AguardandoValidacaoTecnicaListController = AguardandoValidacaoTecnicaListController;