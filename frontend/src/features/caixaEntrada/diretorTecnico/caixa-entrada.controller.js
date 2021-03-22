var CxEntDiretorController = function($scope, config, mensagem,$rootScope, processoService, analiseService, $location) {

	$rootScope.tituloPagina = 'AGUARDANDO VALIDAÇÃO DO DIRETOR TÉCNICO';

	var cxEntDiretor = this;

	cxEntDiretor.atualizarListaProcessos = atualizarListaProcessos;
	cxEntDiretor.atualizarPaginacao = atualizarPaginacao;
	cxEntDiretor.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntDiretor.onPaginaAlterada = onPaginaAlterada;
	cxEntDiretor.visualizarProcesso = visualizarProcesso;
	cxEntDiretor.processos = [];
    cxEntDiretor.legendas = app.utils.CondicaoTramitacao;
	cxEntDiretor.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_VALIDACAO_DIRETORIA;
    cxEntDiretor.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntDiretor.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntDiretor.PrazoAnalise = app.utils.PrazoAnalise;
	cxEntDiretor.dateUtil = app.utils.DateUtil;
	cxEntDiretor.disabledFields = _.concat($scope.caixaEntrada.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA);
	cxEntDiretor.iniciarAnaliseDiretor = iniciarAnaliseDiretor;

    cxEntDiretor.hasAtLeastOneProcessoSelected = hasAtLeastOneProcessoSelected;
    cxEntDiretor.verificarTodosProcessosMarcados = verificarTodosProcessosMarcados;

	function atualizarListaProcessos(processos) {

		cxEntDiretor.processos = processos;

	}

	function iniciarAnaliseDiretor(idAnalise) {

		analiseService.iniciar(idAnalise)
			.then(function(response){

				$rootScope.$broadcast('atualizarContagemProcessos');
				$rootScope.tituloPagina = 'EM VALIDAÇÃO PELO DIRETOR TÉCNICO';
				$location.path('/analise-diretor/' + idAnalise.toString());

			}, function(error){

				mensagem.error(error.data.texto);
			});
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		cxEntDiretor.paginacao.update(totalItens, paginaAtual);
    }

    function verificarTodosProcessosMarcados() {

		cxEntDiretor.todosProcessosSelecionados =

			_.reduce(cxEntDiretor.processos, function(resultado, p){

				return resultado && p;

			}, true);
	}

    function hasAtLeastOneProcessoSelected() {

		return _.some(cxEntDiretor.processos, {selecionado: true});
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(cxEntDiretor.processos, function(processo){

			processo.selecionado = cxEntDiretor.todosProcessosSelecionados;
		});
	}

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}

	cxEntDiretor.prazoAnaliseTecnica = function(processo) {

		return processo.dataConclusaoAnaliseTecnica ? 'Concluída' : (processo.diasAnaliseTecnica !== null && processo.diasAnaliseTecnica !== undefined ? processo.diasAnaliseTecnica : '-');

	};

};

exports.controllers.CxEntDiretorController = CxEntDiretorController;
