var AguardandoAssinaturaAprovadorListController = function($scope, config, $location, $rootScope, processoService) {

	$rootScope.tituloPagina = 'AGUARDANDO ASSINATURA APROVADOR';

	var listagem = this;

	listagem.atualizarListaProcessos = atualizarListaProcessos;
	listagem.atualizarPaginacao = atualizarPaginacao;
	listagem.selecionarTodosProcessos = selecionarTodosProcessos;
	listagem.onPaginaAlterada = onPaginaAlterada;
	listagem.validarAnalise = validarAnalise;
	listagem.getPrazoAnalise = getPrazoAnalise;

	listagem.processos = [];
	listagem.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_ASSINATURA_APROVADOR;
	listagem.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagem.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	listagem.dateUtil = app.utils.DateUtil;
	listagem.exibirDadosProcesso = exibirDadosProcesso;
	listagem.dataAtual = Date.now();
	listagem.disabledFields = [app.DISABLED_FILTER_FIELDS.SITUACAO, app.DISABLED_FILTER_FIELDS.PERIODO_PROCESSO];    

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

	function validarAnalise(idAnalise) {

        //adicionar link para a validação da assinatura do aprovador
        $location.path('aguardando-assinatura/' + idAnalise);
	}	

	function exibirDadosProcesso(processo) {

        processoService.visualizarProcesso(processo);
    }

	function getPrazoAnalise(dataCadastroAnalise) {

		return Math.abs(listagem.dateUtil.calcularDiasRestantes(dataCadastroAnalise));
	}
};

exports.controllers.AguardandoAssinaturaAprovadorListController = AguardandoAssinaturaAprovadorListController;
