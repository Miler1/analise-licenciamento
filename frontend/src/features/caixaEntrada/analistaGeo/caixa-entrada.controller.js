var CxEntAnalistaGeoController = function($scope, config, $location, analiseGeoService, mensagem, $rootScope, $uibModal ,processoService) {

	$rootScope.tituloPagina = 'AGUARDANDO ANÁLISE GEO';

	var cxEntAnalistaGeo = this;

	cxEntAnalistaGeo.atualizarListaProcessos = atualizarListaProcessos;
	cxEntAnalistaGeo.atualizarPaginacao = atualizarPaginacao;
	cxEntAnalistaGeo.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntAnalistaGeo.onPaginaAlterada = onPaginaAlterada;
	cxEntAnalistaGeo.iniciarAnalise = iniciarAnalise;
	cxEntAnalistaGeo.iniciarUploadShapes = iniciarUploadShapes;
	cxEntAnalistaGeo.visualizarProcesso = visualizarProcesso;
	cxEntAnalistaGeo.primeiroAcesso = primeiroAcesso;
	cxEntAnalistaGeo.processos = [];
	cxEntAnalistaGeo.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_ANALISE_GEO;
	cxEntAnalistaGeo.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntAnalistaGeo.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntAnalistaGeo.PrazoAnalise = app.utils.PrazoAnalise;
	cxEntAnalistaGeo.dateUtil = app.utils.DateUtil;
	cxEntAnalistaGeo.disabledFields = _.concat($scope.caixaEntrada.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA, app.DISABLED_FILTER_FIELDS.ANALISTA_TECNICO, app.DISABLED_FILTER_FIELDS.ANALISTA_GEO);

	function atualizarListaProcessos(processos) {

		cxEntAnalistaGeo.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		cxEntAnalistaGeo.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(cxEntAnalistaGeo.processos, function(processo){

			processo.selecionado = cxEntAnalistaGeo.todosProcessosSelecionados;
		});
	}
	
	function iniciarAnalise(idAnaliseGeo) {
		analiseGeoService.iniciar({ id : idAnaliseGeo })
			.then(function(response){

				$rootScope.$broadcast('atualizarContagemProcessos');
				$rootScope.tituloPagina = 'EM ANÁLISE GEO';
				$location.path('/analise-geo/' + idAnaliseGeo.toString());
			
			}, function(error){
				mensagem.error(error.data.texto);
			});
	}
	
	function iniciarUploadShapes(processo){
		$rootScope.processo = processo;

		$location.path('/shape-upload/' + processo.idProcesso.toString());
	}

	function visualizarProcesso(processo) {
		return processoService.visualizarProcesso(processo);
	}


	cxEntAnalistaGeo.solicitarDesvinculoAnaliseGeo =  function(processo){

				$uibModal.open({
					controller: 'desvinculoController',
					controllerAs: 'desvinculoCtrl',
					backdrop: 'static',
					templateUrl: 'features/caixaEntrada/analistaGeo/modalDesvinculo.html',
					size: 'lg',
					resolve: {

						idAnaliseGeo: function(){
							return processo.idAnaliseGeo;
						},
						idProcesso: function(){
							return processo.idProcesso;
						}
					}
					
				});
	};

	function primeiroAcesso(processo) {
		var cpfCnpjEmpreendimento = processo.cpfEmpreendimento ? processo.cpfEmpreendimento : processo.cnpjEmpreendimento;

		analiseGeoService.getPossuiAnexo(cpfCnpjEmpreendimento)
			.then(function(response){
				// Caso possua null - nenhuma ação foi realizada no empreendimento
				if(response.data === null){
					cxEntAnalistaGeo.iniciarUploadShapes(processo);
				}
				// Caso possua true ou false - já existiu uma análise prévia do empreendimento
				else {
					cxEntAnalistaGeo.iniciarAnalise(processo.idAnaliseGeo);
				}
			}, function(error){
				mensagem.error(error.data.texto);
			});
	}
};

exports.controllers.CxEntAnalistaGeoController = CxEntAnalistaGeoController;