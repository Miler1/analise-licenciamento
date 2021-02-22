var CxEntCoordenadorController = function($scope, config, analistaService,analiseTecnicaService, mensagem, $uibModal,$rootScope, processoService, analiseGeoService, $location) {

	$rootScope.tituloPagina = 'AGUARDANDO VALIDAÇÃO DO COORDENADOR';

	var cxEntCoordenador = this;

	cxEntCoordenador.atualizarListaProcessos = atualizarListaProcessos;
	cxEntCoordenador.legendaDesvinculo = app.utils.CondicaoTramitacao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_GEO;
	cxEntCoordenador.atualizarPaginacao = atualizarPaginacao;
	cxEntCoordenador.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntCoordenador.vincularAnalista = vincularAnalista;
	cxEntCoordenador.onPaginaAlterada = onPaginaAlterada;
	cxEntCoordenador.hasAtLeastOneProcessoSelected = hasAtLeastOneProcessoSelected;
	cxEntCoordenador.visualizarProcesso = visualizarProcesso;
	cxEntCoordenador.processos = [];
	cxEntCoordenador.legendas = app.utils.CondicaoTramitacao;
	cxEntCoordenador.condicaoTramitacao = app.utils.CondicaoTramitacao.CAIXA_ENTRADA_COORDENADOR;
	cxEntCoordenador.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntCoordenador.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntCoordenador.PrazoAnalise = app.utils.PrazoAnalise;
	cxEntCoordenador.dateUtil = app.utils.DateUtil;
	cxEntCoordenador.verificarTodosProcessosMarcados = verificarTodosProcessosMarcados;
	cxEntCoordenador.disabledFields = _.concat($scope.caixaEntrada.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA);

	function atualizarListaProcessos(processos) {

		cxEntCoordenador.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		cxEntCoordenador.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(cxEntCoordenador.processos, function(processo){

			processo.selecionado = cxEntCoordenador.todosProcessosSelecionados;
		});
	}

	function hasAtLeastOneProcessoSelected() {

		return _.some(cxEntCoordenador.processos, {selecionado: true});
	}

	function vincularAnalista(processoSelecionado) {
		
		var processosSelecionados = [];

		if (processoSelecionado) {

			processosSelecionados.push(processoSelecionado);

		} else {

		 	_.each(cxEntCoordenador.processos, function(processo){

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
		
		$uibModal.open({
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

		cxEntCoordenador.todosProcessosSelecionados =
			
			_.reduce(cxEntCoordenador.processos, function(resultado, p){
			
				return resultado && p;

			}, true);
	}

	cxEntCoordenador.prazoAnaliseTecnica = function(processo) {

		return processo.dataConclusaoAnaliseTecnica ? 'Concluída' : (processo.diasAnaliseTecnica !== null && processo.diasAnaliseTecnica !== undefined ? processo.diasAnaliseTecnica : '-');

	};

	cxEntCoordenador.atenderSolicitacaoDesvinculo =  function(processo){

		$uibModal.open({
			controller: 'desvinculoCoordenadorController',
			controllerAs: 'desvinculoCoordenadorCtrl',
			templateUrl: 'features/caixaEntrada/coordenador/modalDesvinculo.html',
			backdrop: 'static',
			size: 'lg',
			resolve: {

				processo: function(){
					return processo;
				}
			}
			
		});
	};

	cxEntCoordenador.verificarStatusGeo = function(processo) {
		
		return processo.idCondicaoTramitacao === cxEntCoordenador.legendaDesvinculo ||
			processo.idCondicaoTramitacao === app.utils.CondicaoTramitacao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA ||
			processo.idCondicaoTramitacao !== cxEntCoordenador.legendas.AGUARDANDO_VALIDACAO_GEO_PELO_COORDENADOR;
		
	};

	cxEntCoordenador.verificarStatusTecnico = function(processo) {
		
		return processo.idCondicaoTramitacao === cxEntCoordenador.legendaDesvinculo ||
			processo.idCondicaoTramitacao === app.utils.CondicaoTramitacao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA || 
			processo.idCondicaoTramitacao !== cxEntCoordenador.legendas.AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR;
	
	};

	cxEntCoordenador.verificarSolicitacaoDesvinculo = function(processo) {
		
		return processo.idCondicaoTramitacao === cxEntCoordenador.legendaDesvinculo ||
			processo.idCondicaoTramitacao === app.utils.CondicaoTramitacao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA;
	
	};

	cxEntCoordenador.iniciarAnaliseCoordenador = function(idAnalise, idAnaliseGeo, idAnaliseTecnica) {

		if(idAnaliseTecnica === null){

			analiseGeoService.iniciarAnaliseCoordenador({ id : idAnaliseGeo })
			.then(function(response){

				$rootScope.$broadcast('atualizarContagemProcessos');
				$rootScope.tituloPagina = 'EM VALIDAÇÃO PELO COORDENADOR';
				$location.path('/analise-coordenador/' + idAnalise.toString());
			
			}, function(error){
				mensagem.error(error.data.texto);
			});

		}else if (idAnaliseGeo === null){

			analiseTecnicaService.iniciarAnaliseTecnicaCoordenador({ id : idAnaliseTecnica })
			.then(function(response){

				$rootScope.tituloPagina = 'EM VALIDAÇÃO PELO COORDENADOR';
				$location.path('/analise-tecnica-coordenador/' + idAnalise.toString());
				$rootScope.$broadcast('atualizarContagemProcessos');
			
			}, function(error){
				mensagem.error(error.data.texto);
			});
		}
		
		
	};
};

exports.controllers.CxEntCoordenadorController = CxEntCoordenadorController;