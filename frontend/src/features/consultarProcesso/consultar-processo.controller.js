var ConsultarProcessoController = function($scope, config, $rootScope, processoService, $uibModal) {

	$rootScope.tituloPagina = 'CONSULTAR PROCESSO';

	var consultarProcesso = this;

	consultarProcesso.atualizarListaProcessos = atualizarListaProcessos;
	consultarProcesso.atualizarPaginacao = atualizarPaginacao;
	consultarProcesso.selecionarTodosProcessos = selecionarTodosProcessos;
	consultarProcesso.onPaginaAlterada = onPaginaAlterada;
	consultarProcesso.visualizarProcesso = visualizarProcesso;

	consultarProcesso.processos = [];
	consultarProcesso.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	consultarProcesso.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	consultarProcesso.dateUtil = app.utils.DateUtil;
	consultarProcesso.getDiasRestantes = getDiasRestantes;
	consultarProcesso.isPrazoMinimoAvisoAnalise = isPrazoMinimoAvisoAnalise;

	function atualizarListaProcessos(processos) {

		consultarProcesso.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		consultarProcesso.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(consultarProcesso.processos, function(processo){

			processo.selecionado = consultarProcesso.todosProcessosSelecionados;
		});
	}

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}

	function getDiasRestantes(processo) {

		if(processo.dataConclusaoAnaliseJuridica) {
			return 'Concluída em ' + processo.dataConclusaoAnaliseJuridica.split(' ')[0];
		}
		return consultarProcesso.dateUtil.getDiasRestantes(processo.dataVencimentoPrazoAnaliseJuridica);

	}

	function isPrazoMinimoAvisoAnalise(processo) {

		if(processo.dataConclusaoAnaliseJuridica) {
			return false;		
		}
		return consultarProcesso.dateUtil.isPrazoMinimoAvisoAnalise(processo.dataVencimentoPrazoAnaliseJuridica, 
					consultarProcesso.PrazoMinimoAvisoAnalise.ANALISE_JURIDICA);
	}

};

exports.controllers.ConsultarProcessoController = ConsultarProcessoController;