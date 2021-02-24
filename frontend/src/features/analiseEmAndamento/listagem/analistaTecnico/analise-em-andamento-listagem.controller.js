var AnaliseEmAndamentoTecnicaListController = function($scope, config, $location, $uibModal,
														parecerCoordenadorService, $rootScope,
														analiseTecnicaService, processoService) {

	$rootScope.tituloPagina = 'EM ANÁLISE TÉCNICA';

	var listagem = this;

	listagem.atualizarListaProcessos = atualizarListaProcessos;
	listagem.visualizarSolicitacaoAjustes = visualizarSolicitacaoAjustes;
	listagem.verificaSolicitacaoAjustes = verificaSolicitacaoAjustes;
	listagem.tipoResultadoAnalise = app.utils.TiposResultadoAnalise;
	listagem.atualizarPaginacao = atualizarPaginacao;
	listagem.selecionarTodosProcessos = selecionarTodosProcessos;
	listagem.onPaginaAlterada = onPaginaAlterada;
	listagem.continuarAnalise = continuarAnalise;
	listagem.processos = [];
	listagem.condicaoTramitacao = app.utils.CondicaoTramitacao.EM_ANALISE_TECNICA;
	listagem.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagem.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	listagem.PrazoAnalise = app.utils.PrazoAnalise;
	listagem.dateUtil = app.utils.DateUtil;
	listagem.origemNotificacao = app.utils.OrigemNotificacao;
	listagem.exibirDadosProcesso = exibirDadosProcesso;
	listagem.disabledFields = _.concat($scope.analiseEmAndamentoListagem.disabledFields, app.DISABLED_FILTER_FIELDS.ANALISTA_GEO, app.DISABLED_FILTER_FIELDS.GERENCIA, app.DISABLED_FILTER_FIELDS.ANALISTA_TECNICO);
	listagem.notificacaoAtendida = notificacaoAtendida;
	listagem.visualizarNotificacao = visualizarNotificacao;

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

	function continuarAnalise(idAnaliseTecnica) {

		$location.path('/analise-tecnica/' + idAnaliseTecnica.toString());
	}

	function exibirDadosProcesso(processo) {

        processoService.visualizarProcesso(processo);
	}

	function visualizarSolicitacaoAjustes(processo) {

		parecerCoordenadorService.findJustificativaParecerByIdAnaliseTecnica(processo.idAnaliseTecnica)
			.then(function(response){

				$uibModal.open({
					controller: 'visualizarAjustesController',
					controllerAs: 'visualizarAjustesController',
					backdrop: 'static',
					templateUrl: 'components/modalVisualizarSolicitacaoAjustes/modalVisualizarSolicitacaoAjustes.html',
					size: 'lg',
					resolve: {
						justificativa: function () {
							return response.data;
						}
						
					}
				});
		});
	}

	function verificaSolicitacaoAjustes(processo) {

		analiseTecnicaService.getAnaliseTecnica(processo.idAnaliseTecnica)
			.then(function(response){

				if(_.isEmpty(response.data.pareceresCoordenadorAnaliseTecnica)){

					processo.verificaAnalise = false;
					
				}else{

					_.find(response.data.pareceresCoordenadorAnaliseTecnica, function(parecerCoordenador) {

						if(parecerCoordenador.parecer === null || parecerCoordenador.tipoResultadoAnalise.id !== listagem.tipoResultadoAnalise.SOLICITAR_AJUSTES){

							processo.verificaAnalise = false;
							
						}else{
							
							processo.verificaAnalise=true;
							
						}
					});
				}
			});
	}

	function notificacaoAtendida(processo) {
		return processo && processo.retificacao && processo.idOrigemNotificacao === listagem.origemNotificacao.ANALISE_TECNICA && processo.idAnalistaTecnicoAnterior === processo.idAnalistaTecnico;
	}

	function visualizarNotificacao(processo) {

		return processoService.visualizarNotificacao(processo);
	}

};

exports.controllers.AnaliseEmAndamentoTecnicaListController = AnaliseEmAndamentoTecnicaListController;
