var CxEntGerenteController = function($scope, config, analistaService,analiseTecnicaService, mensagem, $uibModal,$rootScope, processoService, analiseGeoService, $location) {

	$rootScope.tituloPagina = 'AGUARDANDO VALIDAÇÃO DO GERENTE TÉCNICO';

	var cxEntGerente = this;

	cxEntGerente.atualizarListaProcessos = atualizarListaProcessos;
	cxEntGerente.legendaDesvinculo = app.utils.CondicaoTramitacao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_GEO;
	cxEntGerente.atualizarPaginacao = atualizarPaginacao;
	cxEntGerente.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntGerente.vincularAnalista = vincularAnalista;
	cxEntGerente.onPaginaAlterada = onPaginaAlterada;
	cxEntGerente.hasAtLeastOneProcessoSelected = hasAtLeastOneProcessoSelected;
	cxEntGerente.visualizarProcesso = visualizarProcesso;
	cxEntGerente.processos = [];
	cxEntGerente.legendas = app.utils.CondicaoTramitacao;
	cxEntGerente.condicaoTramitacao = app.utils.CondicaoTramitacao.CAIXA_ENTRADA_GERENTE;
	cxEntGerente.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntGerente.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntGerente.PrazoAnalise = app.utils.PrazoAnalise;
	cxEntGerente.dateUtil = app.utils.DateUtil;
	cxEntGerente.verificarTodosProcessosMarcados = verificarTodosProcessosMarcados;
	cxEntGerente.disabledFields = _.concat($scope.caixaEntrada.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA);

	function atualizarListaProcessos(processos) {

		cxEntGerente.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		cxEntGerente.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(cxEntGerente.processos, function(processo){

			processo.selecionado = cxEntGerente.todosProcessosSelecionados;
		});
	}

	function hasAtLeastOneProcessoSelected() {

		return _.some(cxEntGerente.processos, {selecionado: true});
	}

	function vincularAnalista(processoSelecionado) {
		
		var processosSelecionados = [];

		if (processoSelecionado) {

			processosSelecionados.push(processoSelecionado);

		} else {

		 	_.each(cxEntGerente.processos, function(processo){

				 if (processo.selecionado) {

					 processosSelecionados.push(processo);
				 } 
			});  
		}

		if (processosSelecionados.length === 0) {

			mensagem.warning('É necessário selecionar ao menos um protocolo para vinculá-lo ao analista.');
			return;
		}

		var modalInstance = abrirModal(processosSelecionados);

		modalInstance.result
			.then(function (result) {

				analistaService.vincularAnaliseAnalistaTecnico(result.idConsultorSelecionado, null, result.idsProcessosSelecionados)
					.then(function(response){

						$scope.$broadcast('pesquisarProcessos');
						mensagem.success(response.data.texto);
					})
					.catch(function(response){
						mensagem.error(response.data.texto, {ttl: 15000});
					});
			})
			.catch(function(){ });
	}

	function abrirModal(processos){
		
		var modalInstance = $uibModal.open({
			controller: 'modalVincularConsultorController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			keyboard  : false,
			templateUrl: './features/caixaEntrada/common/modal-vincular-consultor.html',
			size: "lg",
			resolve: {
				processos: function () {
					return processos;
				},
				consultores: getAnalistas(processos[0].idProcesso),
				tipo: function() {
					return 'analista técnico';
				},
				justificationEnabled: function(){
					return false;
				}
			}
		});

		return modalInstance;
	}

	function getAnalistas(idProcesso) {

		return analistaService.getAnalistasTecnicosByProcesso(idProcesso);
	}

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}

	function verificarTodosProcessosMarcados() {

		cxEntGerente.todosProcessosSelecionados = 
			
			_.reduce(cxEntGerente.processos, function(resultado, p){
			
				return resultado && p;

			}, true);
	}

	cxEntGerente.prazoAnaliseTecnica = function(processo) {

		return processo.dataConclusaoAnaliseTecnica ? 'Concluída' : (processo.diasAnaliseTecnica !== null && processo.diasAnaliseTecnica !== undefined ? processo.diasAnaliseTecnica : '-');

	};

	cxEntGerente.atenderSolicitacaoDesvinculo =  function(processo){

		var modalInstance = $uibModal.open({
			controller: 'desvinculoGerenteController',
			controllerAs: 'desvinculoGerenteCtrl',
			templateUrl: 'features/caixaEntrada/gerenteTecnico/modalDesvinculo.html',
			backdrop: 'static',
			size: 'lg',
			resolve: {

				processo: function(){
					return processo;
				}
			}
			
		});
	};

	cxEntGerente.verificarStatusGeo = function(processo) {
		
		return processo.idCondicaoTramitacao === cxEntGerente.legendaDesvinculo || 
			processo.idCondicaoTramitacao === app.utils.CondicaoTramitacao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA ||
			processo.idCondicaoTramitacao !== cxEntGerente.legendas.AGUARDANDO_VALIDACAO_GEO_PELO_GERENTE; 
		
	};

	cxEntGerente.verificarStatusTecnico = function(processo) {
		
		return processo.idCondicaoTramitacao === cxEntGerente.legendaDesvinculo || 
			processo.idCondicaoTramitacao === app.utils.CondicaoTramitacao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA || 
			processo.idCondicaoTramitacao !== cxEntGerente.legendas.AGUARDANDO_VALIDACAO_TECNICA_PELO_GERENTE; 
	
	};

	cxEntGerente.verificarSolicitacaoDesvinculo = function(processo) {
		
		return processo.idCondicaoTramitacao === cxEntGerente.legendaDesvinculo || 
			processo.idCondicaoTramitacao === app.utils.CondicaoTramitacao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA;
	
	};

	cxEntGerente.iniciarAnaliseGerente = function(idAnalise, idAnaliseGeo, idAnaliseTecnica) {

		if(idAnaliseTecnica === null){

			analiseGeoService.iniciarAnaliseGerente({ id : idAnaliseGeo })
			.then(function(response){

				$rootScope.$broadcast('atualizarContagemProcessos');
				$rootScope.tituloPagina = 'EM VALIDAÇÃO PELO GERENTE TÉCNICO';
				$location.path('/analise-gerente/' + idAnalise.toString());
			
			}, function(error){
				mensagem.error(error.data.texto);
			});

		}else if (idAnaliseGeo === null){

			analiseTecnicaService.iniciarAnaliseTecnicaGerente({ id : idAnaliseTecnica })
			.then(function(response){

				$rootScope.tituloPagina = 'EM VALIDAÇÃO PELO GERENTE TÉCNICO';
				$location.path('/analise-tecnica-gerente/' + idAnalise.toString());
				$rootScope.$broadcast('atualizarContagemProcessos');
			
			}, function(error){
				mensagem.error(error.data.texto);
			});
		}
		
		
	};
};

exports.controllers.CxEntGerenteController = CxEntGerenteController;