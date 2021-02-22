var AnaliseEmAndamentoGeoListController = function($scope, config, $location,
												   $rootScope, processoService,
												   analiseGeoService, parecerCoordenadorService,
												   mensagem, $uibModal) {

	$rootScope.tituloPagina = 'EM ANÁLISE GEO';

	var listagem = this;

	listagem.atualizarListaProcessos = atualizarListaProcessos;
	listagem.atualizarPaginacao = atualizarPaginacao;
	listagem.selecionarTodosProcessos = selecionarTodosProcessos;
	listagem.onPaginaAlterada = onPaginaAlterada;
	listagem.continuarAnalise = continuarAnalise;
	listagem.visualizarSolicitacaoAjustes = visualizarSolicitacaoAjustes;
	listagem.verificaSolicitacaoAjustes = verificaSolicitacaoAjustes;
	listagem.tipoResultadoAnalise = app.utils.TiposResultadoAnalise;

	listagem.processos = [];
	listagem.condicaoTramitacao = app.utils.CondicaoTramitacao.EM_ANALISE_GEO;
	listagem.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagem.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	listagem.PrazoAnalise = app.utils.PrazoAnalise;
	listagem.dateUtil = app.utils.DateUtil;
	listagem.exibirDadosProcesso = exibirDadosProcesso;
	listagem.disabledFields = _.concat($scope.analiseEmAndamentoListagem.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA, app.DISABLED_FILTER_FIELDS.ANALISTA_GEO);
	listagem.visualizarNotificacao = visualizarNotificacao;
	listagem.notificacaoAtendida = notificacaoAtendida;

	mensagem.verificaMensagemGlobal();

	function atualizarListaProcessos(processos) {

		listagem.processos = processos;
		
	}

	function verificaSolicitacaoAjustes(processo) {

		analiseGeoService.getAnaliseGeo(processo.idAnaliseGeo)
			.then(function(response){

				if(_.isEmpty(response.data.pareceresCoordenadorAnaliseGeo)){
					processo.verificaAnalise = false;

				}else{
					_.find(response.data.pareceresCoordenadorAnaliseGeo, function(parecerCoordenador) {
						if(parecerCoordenador.parecer === null || parecerCoordenador.tipoResultadoAnalise.id !== listagem.tipoResultadoAnalise.SOLICITAR_AJUSTES){
							processo.verificaAnalise = false;

						}else{
							processo.verificaAnalise=true;

						}
					});
				}
			});
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

	function iniciarUploadShapes(processo){

		$location.path('/shape-upload/' + processo.idProcesso.toString());
	}

	function continuarAnalise(processo) {

		iniciarUploadShapes(processo);

	}

	function primeiroAcesso(processo) {
		var cpfCnpjEmpreendimento = processo.cpfEmpreendimento ? processo.cpfEmpreendimento : processo.cnpjEmpreendimento;

		analiseGeoService.getPossuiAnexo(cpfCnpjEmpreendimento)
		.then(function(response){

			if(response.data === null){

				iniciarUploadShapes(processo);

			}else {

				$rootScope.$broadcast('atualizarContagemProcessos');
				$location.path('/analise-geo/' + processo.idAnaliseGeo.toString());

			}
		}, function(error){

			mensagem.error(error.data.texto);

		});
	}

	function exibirDadosProcesso(processo) {

        processoService.visualizarProcesso(processo);
	}

	function visualizarSolicitacaoAjustes(processo) {

		parecerCoordenadorService.findJustificativaParecerByIdAnaliseGeo(processo.idAnaliseGeo)
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

	function visualizarNotificacao(processo) {

		return processoService.visualizarNotificacao(processo);
	}

	function notificacaoAtendida(processo) {
		
		if (processo.idAnalistaGeoAnterior !== undefined) {

			return processo && processo.retificacao && processo.idAnalistaGeoAnterior === processo.idAnalistaGeo;

		} else {

			return processo && processo.retificacao;
		}
	}
};

exports.controllers.AnaliseEmAndamentoGeoListController = AnaliseEmAndamentoGeoListController;
