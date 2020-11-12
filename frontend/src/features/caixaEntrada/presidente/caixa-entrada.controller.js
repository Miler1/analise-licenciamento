var CxEntPresidenteController = function($scope, 
										config, 
										mensagem,
										$rootScope, 
										processoService, 
										analiseService, 
										$location) {

	$rootScope.tituloPagina = 'AGUARDANDO ASSINATURA DO DIRETOR PRESIDENTE';

	var cxEntPresidente = this;

	cxEntPresidente.atualizarListaProcessos = atualizarListaProcessos;
	cxEntPresidente.atualizarPaginacao = atualizarPaginacao;
	cxEntPresidente.onPaginaAlterada = onPaginaAlterada;
	cxEntPresidente.visualizarProcesso = visualizarProcesso;
	cxEntPresidente.processos = [];
    cxEntPresidente.legendas = app.utils.CondicaoTramitacao;
	cxEntPresidente.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_ASSINATURA_PRESIDENTE;
    cxEntPresidente.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntPresidente.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntPresidente.PrazoAnalise = app.utils.PrazoAnalise;
	cxEntPresidente.dateUtil = app.utils.DateUtil;
	cxEntPresidente.disabledFields = _.concat($scope.caixaEntrada.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA);
	cxEntPresidente.iniciarAnalisePresidente = iniciarAnalisePresidente;

	function atualizarListaProcessos(processos) {

		cxEntPresidente.processos = processos;
		
	}

	function iniciarAnalisePresidente(idAnalise) {

		analiseService.iniciarAnalisePresidente(idAnalise)
			.then(function(response){

				$rootScope.$broadcast('atualizarContagemProcessos');
				$rootScope.tituloPagina = 'EM VALIDAÇÃO PELO DIRETOR PRESIDENTE';
				$location.path('/analise-presidente/' + idAnalise.toString());
							
			}, function(error){

				mensagem.error(error.data.texto);
			});
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		cxEntPresidente.paginacao.update(totalItens, paginaAtual);
    }  

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}

	cxEntPresidente.prazoAnaliseTecnica = function(processo) {

		return processo.dataConclusaoAnaliseTecnica ? 'Concluída' : (processo.diasAnaliseTecnica !== null && processo.diasAnaliseTecnica !== undefined ? processo.diasAnaliseTecnica : '-');

	};

};

exports.controllers.CxEntPresidenteController = CxEntPresidenteController;