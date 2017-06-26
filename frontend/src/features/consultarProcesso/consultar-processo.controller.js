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



	var modalInstance = $uibModal.open({

		component: 'modalInformacoesLicenca',
		size: 'lg',
		backdrop: 'static',
		resolve: {

			dadosLicenca: function() {

				return {
					condicionantes: '',
					observacoes: 'teste'
				};
			}
		}
	});
};

exports.controllers.ConsultarProcessoController = ConsultarProcessoController;