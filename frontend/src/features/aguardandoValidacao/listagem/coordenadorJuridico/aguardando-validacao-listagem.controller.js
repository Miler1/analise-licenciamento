var AguardandoValidacaoJuridicoListController = function($scope, config, $location, $rootScope) {

	$rootScope.tituloPagina = 'AGUARDANDO VALIDACÃO JURÍDICA';

	var listagem = this;

	listagem.atualizarListaProcessos = atualizarListaProcessos;
	listagem.atualizarPaginacao = atualizarPaginacao;
	listagem.selecionarTodosProcessos = selecionarTodosProcessos;
	listagem.onPaginaAlterada = onPaginaAlterada;
	listagem.validarAnalise = validarAnalise;

	listagem.processos = [];
	listagem.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_VALIDACAO_JURIDICA;
	listagem.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagem.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	listagem.dateUtil = app.utils.DateUtil;

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

	function validarAnalise(idAnaliseJuridica) {

		$location.path('/aguardando-validacao/' + idAnaliseJuridica.toString());
	}	
};

exports.controllers.AguardandoValidacaoJuridicoListController = AguardandoValidacaoJuridicoListController;