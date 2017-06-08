var CxEntConsultorJuridicoController = function($scope, config, $rootScope, $location) {

	$rootScope.tituloPagina = 'AGUARDANDO ANÁLISE JURÍDICA';

	var cxEntConsultorJuridico = this;

	cxEntConsultorJuridico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntConsultorJuridico.atualizarPaginacao = atualizarPaginacao;
	cxEntConsultorJuridico.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntConsultorJuridico.onPaginaAlterada = onPaginaAlterada;
	cxEntConsultorJuridico.iniciarAnalise = iniciarAnalise;

	cxEntConsultorJuridico.processos = [];
	cxEntConsultorJuridico.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_ANALISE_JURIDICA;
	cxEntConsultorJuridico.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntConsultorJuridico.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntConsultorJuridico.dateUtil = app.utils.DateUtil;

	function atualizarListaProcessos(processos) {

		cxEntConsultorJuridico.processos = processos;
	}

	function atualizarPaginacao(totalItens) {

		cxEntConsultorJuridico.paginacao.update(totalItens, cxEntConsultorJuridico.paginacao.paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(cxEntConsultorJuridico.processos, function(processo){

			processo.selecionado = cxEntConsultorJuridico.todosProcessosSelecionados;
		});
	}

	function iniciarAnalise(idProcesso) {

		$location.path('/analise-juridica/' + idProcesso.toString());
	}	
};

exports.controllers.CxEntConsultorJuridicoController = CxEntConsultorJuridicoController;