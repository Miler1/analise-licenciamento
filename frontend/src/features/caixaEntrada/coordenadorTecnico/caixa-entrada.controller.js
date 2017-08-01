var CxEntCoordenadorTecnicoController = function($scope, config, analistaService, mensagem, $uibModal, $rootScope, processoService) {

	$rootScope.tituloPagina = 'AGUARDANDO VINCULAÇÃO TÉCNICA';

	var cxEntCoordenadorTecnico = this;

	cxEntCoordenadorTecnico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntCoordenadorTecnico.atualizarPaginacao = atualizarPaginacao;
	cxEntCoordenadorTecnico.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntCoordenadorTecnico.vincularAnalista = vincularAnalista;
	cxEntCoordenadorTecnico.onPaginaAlterada = onPaginaAlterada;
	cxEntCoordenadorTecnico.hasAtLeastOneProcessoSelected = hasAtLeastOneProcessoSelected;
	cxEntCoordenadorTecnico.visualizarProcesso = visualizarProcesso;

	cxEntCoordenadorTecnico.processos = [];
	cxEntCoordenadorTecnico.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_VINCULACAO_TECNICA;
	cxEntCoordenadorTecnico.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntCoordenadorTecnico.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntCoordenadorTecnico.dateUtil = app.utils.DateUtil;
	cxEntCoordenadorTecnico.verificarTodosProcessosMarcados = verificarTodosProcessosMarcados;

	function atualizarListaProcessos(processos) {

		cxEntCoordenadorTecnico.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		cxEntCoordenadorTecnico.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(cxEntCoordenadorTecnico.processos, function(processo){

			processo.selecionado = cxEntCoordenadorTecnico.todosProcessosSelecionados;
		});
	}

	function hasAtLeastOneProcessoSelected() {

		return _.some(cxEntCoordenadorTecnico.processos, {selecionado: true});		
	}

	function vincularAnalista(processoSelecionado) {
		
		var processosSelecionados = [];

		if (processoSelecionado) {

			processosSelecionados.push(processoSelecionado);

		} else {

		 	_.each(cxEntCoordenadorTecnico.processos, function(processo){

				 if (processo.selecionado) {

					 processosSelecionados.push(processo);
				 } 
			});  
		}

		if (processosSelecionados.length === 0) {

			mensagem.warning('É necessário selecionar ao menos um processo para vinculá-lo ao analista.');
			return;
		}

		var modalInstance = abrirModal(processosSelecionados);

		modalInstance.result
			.then(function (result) {

				analistaService.vincularAnaliseAnalistaTecnico(result.idConsultorSelecionado, result.idsProcessosSelecionados)
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
				}
			}
		});

		return modalInstance;
	}

	function getAnalistas(idProcesso) {

		return analistaService.getAnalistasTecnicosModal(idProcesso);
	}

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}

	function verificarTodosProcessosMarcados() {

		cxEntCoordenadorTecnico.todosProcessosSelecionados = 
			
			_.reduce(cxEntCoordenadorTecnico.processos, function(resultado, p){
			
				return resultado && p;

			}, true);
	}
};

exports.controllers.CxEntCoordenadorTecnicoController = CxEntCoordenadorTecnicoController;