var CxEntSecretarioController = function($scope, 
										config, 
										mensagem,
										$rootScope, 
										processoService, 
										analiseService, 
										$location) {

	$rootScope.tituloPagina = 'AGUARDANDO ASSINATURA DO SECRETÁRIO';

	var cxEntSecretario = this;

	cxEntSecretario.atualizarListaProcessos = atualizarListaProcessos;
	cxEntSecretario.atualizarPaginacao = atualizarPaginacao;
	cxEntSecretario.onPaginaAlterada = onPaginaAlterada;
	cxEntSecretario.visualizarProcesso = visualizarProcesso;
	cxEntSecretario.processos = [];
    cxEntSecretario.legendas = app.utils.CondicaoTramitacao;
	cxEntSecretario.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_ASSINATURA_SECRETARIO;
    cxEntSecretario.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntSecretario.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntSecretario.PrazoAnalise = app.utils.PrazoAnalise;
	cxEntSecretario.dateUtil = app.utils.DateUtil;
	cxEntSecretario.disabledFields = _.concat($scope.caixaEntrada.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA);
	cxEntSecretario.iniciarAnaliseSecretario = iniciarAnaliseSecretario;

	function atualizarListaProcessos(processos) {

		cxEntSecretario.processos = processos;
		
	}

	function iniciarAnaliseSecretario(idAnalise) {

		analiseService.iniciarAnaliseSecretario(idAnalise)
			.then(function(response){

				$rootScope.$broadcast('atualizarContagemProcessos');
				$rootScope.tituloPagina = 'EM VALIDAÇÃO PELO SECRETARIO';
				$location.path('/analise-secretario/' + idAnalise.toString());
							
			}, function(error){

				mensagem.error(error.data.texto);
			});
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		cxEntSecretario.paginacao.update(totalItens, paginaAtual);
    }  

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}

	cxEntSecretario.prazoAnaliseTecnica = function(processo) {

		return processo.dataConclusaoAnaliseTecnica ? 'Concluída' : (processo.diasAnaliseTecnica !== null && processo.diasAnaliseTecnica !== undefined ? processo.diasAnaliseTecnica : '-');

	};

};

exports.controllers.CxEntSecretarioController = CxEntSecretarioController;