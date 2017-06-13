var CxEntConsultorJuridicoController = function($scope, config, $rootScope, $location, analiseJuridicaService, mensagem) {

	$rootScope.tituloPagina = 'AGUARDANDO ANÁLISE JURÍDICA';

	var cxEntConsultorJuridico = this;

	cxEntConsultorJuridico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntConsultorJuridico.atualizarPaginacao = atualizarPaginacao;
	cxEntConsultorJuridico.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntConsultorJuridico.onPaginaAlterada = onPaginaAlterada;
	cxEntConsultorJuridico.iniciarAnalise = iniciarAnalise;

	cxEntConsultorJuridico.processos = [];
	cxEntConsultorJuridico.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_ANALISE_JURIDICA;
	cxEntConsultorJuridico.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntConsultorJuridico.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntConsultorJuridico.dateUtil = app.utils.DateUtil;

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

				$location.path('/analise-juridica/' + idAnaliseJuridica.toString());
			
			}, function(error){

				mensagem.error(error.data.texto);
			});
	}	
};

exports.controllers.CxEntConsultorJuridicoController = CxEntConsultorJuridicoController;