var ConsultarProcessoController = function($scope, config, $rootScope, processoService, TiposSetores, documentoAnaliseService, mensagem, documentoService) {

	$rootScope.tituloPagina = 'CONSULTAR PROTOCOLO';

	var consultarProcesso = this;

	consultarProcesso.usuarioLogadoCodigoPerfil = $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo;
	consultarProcesso.perfis = app.utils.Perfis;
	consultarProcesso.atualizarListaProcessos = atualizarListaProcessos;
	consultarProcesso.atualizarPaginacao = atualizarPaginacao;
	consultarProcesso.selecionarTodosProcessos = selecionarTodosProcessos;
	consultarProcesso.onPaginaAlterada = onPaginaAlterada;
	consultarProcesso.visualizarProcesso = visualizarProcesso;
	consultarProcesso.visualizarNotificacao = visualizarNotificacao;

	consultarProcesso.legendaDesvinculo = app.utils.CondicaoTramitacao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_GEO;

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
		app.DISABLED_FILTER_FIELDS.GERENCIA];

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

	function visualizarNotificacao(processo) {

		return processoService.visualizarNotificacao(processo);
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

	consultarProcesso.getPrazoAnaliseGeo = function(processo) {

		if(processo.idCondicaoTramitacao === consultarProcesso.condicaoTramitacao.EM_ANALISE_GERENTE ||
			processo.idCondicaoTramitacao === consultarProcesso.condicaoTramitacao.AGUARDANDO_VALIDACAO_GEO_PELO_GERENTE || 
			processo.dataConclusaoAnaliseGeo) {

			return 'Concluída';

		} else if(processo.idCondicaoInicialHistoricoTramitacao === consultarProcesso.condicaoTramitacao.EM_ANALISE_GEO &&
				processo.idCondicaoFinalHistoricoTramitacao === consultarProcesso.condicaoTramitacao.AGUARDANDO_ANALISE_GEO) {

			return parseInt(consultarProcesso.dateUtil.getContaDiasRestantesData(processo.dataVencimentoPrazoAnaliseGeo)) + processo.diasCongelamento;

		}

		return consultarProcesso.dateUtil.getContaDiasRestantesData(processo.dataVencimentoPrazoAnaliseGeo);

	};

	consultarProcesso.downloadPDFparecer = function (processo) {

		var params = {
			id: processo.idAnaliseGeo
		};

		documentoAnaliseService.generatePDFParecerGeo(params)
			.then(function(data, status, headers){

				var url = URL.createObjectURL(data.data.response.blob);
                window.open(url, '_blank');

			},function(error){
				mensagem.error(error.data.texto);
			});
	};

	consultarProcesso.downloadPDFparecerTecnico = function (processo) {

		documentoService.downloadParecerByIdAnaliseTecnica(processo.idAnaliseTecnica);

	};

	consultarProcesso.downloadPDFminuta = function (processo) {

		documentoService.downloadMinutaByIdAnaliseTecnica(processo.idAnaliseTecnica);
	};

	consultarProcesso.downloadPDFvistoria = function (processo) {
		documentoService.downloadRTVByIdAnaliseTecnica(processo.idAnaliseTecnica);
	};

	consultarProcesso.downloadPDFCartaImagem = function (processo) {

		var params = {
			id: processo.idAnaliseGeo
		};

		documentoAnaliseService.generatePDFCartaImagemGeo(params)
			.then(function(data, status, headers){

				var url = URL.createObjectURL(data.data.response.blob);
                window.open(url, '_blank');

			},function(error){
				mensagem.error(error.data.texto);
			});
	};

	consultarProcesso.verificaStatusAnalise = function(idCondicaoTramitacao) {

		var CONSULTAR_PROTOCOLO_ANALISTA_GEO = [25, 26, 30, 4];	
		var CONSULTAR_PROTOCOLO_ANALISTA_TECNICO = [7, 8, 9, 33, 35];
		var status = true;
		
		// Verificar permissões de status para cada perfil
		if (consultarProcesso.usuarioLogadoCodigoPerfil === consultarProcesso.perfis.ANALISTA_GEO || consultarProcesso.usuarioLogadoCodigoPerfil === consultarProcesso.perfis.GERENTE) {

			CONSULTAR_PROTOCOLO_ANALISTA_GEO.forEach(function(condicao){

				if(idCondicaoTramitacao === condicao) {

					status = false;

				}

			});

		} else if (consultarProcesso.usuarioLogadoCodigoPerfil === consultarProcesso.perfis.ANALISTA_TECNICO) {
			CONSULTAR_PROTOCOLO_ANALISTA_TECNICO.forEach(function(condicao){

				if(idCondicaoTramitacao === condicao) {

					status = false;

				}

			});
		}

		return status;

	};

	consultarProcesso.prazoAnaliseTecnica = function(processo) {

		if(processo.dataConclusaoAnaliseTecnica) {

			return 'Concluída';

		} else if(processo.diasAnaliseTecnica !== null && processo.diasAnaliseTecnica !== undefined) {

			return consultarProcesso.PrazoMinimoAvisoAnalise.ANALISE_TECNICA - processo.diasAnaliseTecnica;

		}

		return '-';

	};

};

exports.controllers.ConsultarProcessoController = ConsultarProcessoController;