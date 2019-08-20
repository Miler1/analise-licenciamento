var ConsultarProcessoController = function($scope, config, $rootScope, processoService, $uibModal, TiposSetores, documentoAnaliseService, mensagem) {

	$rootScope.tituloPagina = 'CONSULTAR PROCESSO';

	var consultarProcesso = this;

	consultarProcesso.atualizarListaProcessos = atualizarListaProcessos;
	consultarProcesso.atualizarPaginacao = atualizarPaginacao;
	consultarProcesso.selecionarTodosProcessos = selecionarTodosProcessos;
	consultarProcesso.onPaginaAlterada = onPaginaAlterada;
	consultarProcesso.visualizarProcesso = visualizarProcesso;

	consultarProcesso.legendaDesvinculo = app.utils.CondicaoTramitacao.SOLICITACAO_DESVINCULO_PENDENTE;
	consultarProcesso.condicaoTramitacao = app.utils.CondicaoTramitacao;
	consultarProcesso.processos = [];
	consultarProcesso.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	consultarProcesso.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	consultarProcesso.dateUtil = app.utils.DateUtil;
	consultarProcesso.getDiasRestantes = getDiasRestantes;
	consultarProcesso.isPrazoMinimoAvisoAnalise = isPrazoMinimoAvisoAnalise;
	consultarProcesso.PrazoAnalise = app.utils.PrazoAnalise;
	consultarProcesso.GERENCIA = TiposSetores.GERENCIA;
	consultarProcesso.disabledFields = [app.DISABLED_FILTER_FIELDS.COORDENADORIA, app.DISABLED_FILTER_FIELDS.CONSULTOR_JURIDICO,
		app.DISABLED_FILTER_FIELDS.PERIODO_PROCESSO, app.DISABLED_FILTER_FIELDS.GERENCIA, app.DISABLED_FILTER_FIELDS.ANALISTA_TECNICO];

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

	function getDiasRestantes(processo, dataVencimento, dataConclusao) {

		if(processo[dataConclusao]) {
			
			return 'Concluída em ' + processo[dataConclusao].split(' ')[0];
		
		} else if(processo[dataVencimento]) {

			return consultarProcesso.dateUtil.getDiasRestantes(processo[dataVencimento]);
		
		} else {

			return '-';
		}
	}

	function isPrazoMinimoAvisoAnalise(processo, dataVencimento, dataConclusao, tipoAnalise) {

		if(processo[dataConclusao]) {
			return false;		
		}
		return consultarProcesso.dateUtil.isPrazoMinimoAvisoAnalise(processo[dataVencimento], 
					consultarProcesso.PrazoMinimoAvisoAnalise[tipoAnalise]);
	}

	consultarProcesso.downloadPDFparecer = function (processo) {

		var params = {
			id: processo.idAnaliseGeo
		};

		documentoAnaliseService.generatePDFParecerGeo(params)
			.then(function(data, status, headers){

				var a = document.createElement('a');
				a.href = URL.createObjectURL(data.data.response.blob);
				a.download = data.data.response.fileName ? data.data.response.fileName : 'parecer_analise_geo.pdf';
				a.click();

			},function(error){
				mensagem.error(error.data.texto);
			});
	};

	consultarProcesso.downloadPDFCartaImagem = function (processo) {

		var params = {
			id: processo.idAnaliseGeo
		};

		documentoAnaliseService.generatePDFCartaImagemGeo(params)
			.then(function(data, status, headers){

				var a = document.createElement('a');
				a.href = URL.createObjectURL(data.data.response.blob);
				a.download = data.data.response.fileName ? data.data.response.fileName : 'carta_imagem.pdf';
				a.click();

			},function(error){
				mensagem.error(error.data.texto);
			});
	};

};

exports.controllers.ConsultarProcessoController = ConsultarProcessoController;