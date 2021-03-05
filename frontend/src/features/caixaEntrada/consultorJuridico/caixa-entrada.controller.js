var CxEntConsultorJuridicoController = function($scope, config, $rootScope, $location, analiseJuridicaService, mensagem, processoService) {

	$rootScope.tituloPagina = 'AGUARDANDO ANÁLISE JURÍDICA';

	var cxEntConsultorJuridico = this;

	cxEntConsultorJuridico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntConsultorJuridico.atualizarPaginacao = atualizarPaginacao;
	cxEntConsultorJuridico.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntConsultorJuridico.onPaginaAlterada = onPaginaAlterada;
	cxEntConsultorJuridico.iniciarAnalise = iniciarAnalise;
	cxEntConsultorJuridico.visualizarProcesso = visualizarProcesso;

	cxEntConsultorJuridico.processos = [];
	cxEntConsultorJuridico.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_ANALISE_JURIDICA;
	cxEntConsultorJuridico.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntConsultorJuridico.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntConsultorJuridico.PrazoAnalise = app.utils.PrazoAnalise;
	cxEntConsultorJuridico.dateUtil = app.utils.DateUtil;
	cxEntConsultorJuridico.disabledFields = _.concat($scope.caixaEntrada.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA, app.DISABLED_FILTER_FIELDS.ANALISTA_GEO);

	function atualizarListaProcessos(processos) {

		cxEntConsultorJuridico.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		cxEntConsultorJuridico.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(cxEntConsultorJuridico.processos, function(processo){

			processo.selecionado = cxEntConsultorJuridico.todosProcessosSelecionados;
		});
	}

	function iniciarAnalise(idAnaliseJuridica) {

		analiseJuridicaService.iniciar({ id : idAnaliseJuridica })
			.then(function(response){

				$rootScope.$broadcast('atualizarContagemProcessos');
				$location.path('/analise-juridica/' + idAnaliseJuridica.toString());

			}, function(error){

				mensagem.error(error.data.texto);
			});
	}

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}
};

exports.controllers.CxEntConsultorJuridicoController = CxEntConsultorJuridicoController;