var ConsultarLicencasEmitidasController = function($scope, config, $rootScope, processoService, $uibModal, TiposSetores) {

	$rootScope.tituloPagina = 'CONSULTAR LICENCAS EMITIDAS';

	var consultarLicencas = this;

	consultarLicencas.atualizarListaProcessos = atualizarListaProcessos;
	consultarLicencas.atualizarPaginacao = atualizarPaginacao;
	consultarLicencas.selecionarTodosProcessos = selecionarTodosProcessos;
	consultarLicencas.onPaginaAlterada = onPaginaAlterada;
	consultarLicencas.visualizarProcesso = visualizarProcesso;

	consultarLicencas.processos = [];
	consultarLicencas.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	consultarLicencas.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	consultarLicencas.dateUtil = app.utils.DateUtil;
	consultarLicencas.getDiasRestantes = getDiasRestantes;
	consultarLicencas.isPrazoMinimoAvisoAnalise = isPrazoMinimoAvisoAnalise;
	consultarLicencas.GERENCIA = TiposSetores.GERENCIA;
	consultarLicencas.disabledFields = [app.DISABLED_FILTER_FIELDS.COORDENADORIA, app.DISABLED_FILTER_FIELDS.CONSULTOR_JURIDICO];

	function atualizarListaProcessos(processos) {

		consultarLicencas.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		consultarLicencas.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(consultarLicencas.processos, function(processo){

			processo.selecionado = consultarLicencas.todosProcessosSelecionados;
		});
	}

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}

	function getDiasRestantes(processo, dataVencimento, dataConclusao) {

		if(processo[dataConclusao]) {
			
			return 'Concluída em ' + processo[dataConclusao].split(' ')[0];
		
		} else if(processo[dataVencimento]) {

			return consultarLicencas.dateUtil.getDiasRestantes(processo[dataVencimento]);
		
		} else {

			return '-';
		}
	}

	function isPrazoMinimoAvisoAnalise(processo, dataVencimento, dataConclusao, tipoAnalise) {

		if(processo[dataConclusao]) {
			return false;		
		}
		return consultarLicencas.dateUtil.isPrazoMinimoAvisoAnalise(processo[dataVencimento], 
					consultarLicencas.PrazoMinimoAvisoAnalise[tipoAnalise]);
	}

};

exports.controllers.ConsultarLicencasEmitidasController = ConsultarLicencasEmitidasController;