var CxEntGerenteTecnicoController = function($scope, config, analistaService,gerenteService, mensagem, $uibModal, $rootScope, processoService) {

	$rootScope.tituloPagina = 'AGUARDANDO ANÁLISE TÉCNICA';

	var cxEntGerenteTecnico = this;

	cxEntGerenteTecnico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntGerenteTecnico.atualizarPaginacao = atualizarPaginacao;
	cxEntGerenteTecnico.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntGerenteTecnico.vincularAnalista = vincularAnalista;
	cxEntGerenteTecnico.onPaginaAlterada = onPaginaAlterada;
	cxEntGerenteTecnico.hasAtLeastOneProcessoSelected = hasAtLeastOneProcessoSelected;
	cxEntGerenteTecnico.visualizarProcesso = visualizarProcesso;

	cxEntGerenteTecnico.processos = [];
	cxEntGerenteTecnico.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_VINCULACAO_TECNICA_PELO_GERENTE;
	cxEntGerenteTecnico.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntGerenteTecnico.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntGerenteTecnico.dateUtil = app.utils.DateUtil;
	cxEntGerenteTecnico.verificarTodosProcessosMarcados = verificarTodosProcessosMarcados;
	cxEntGerenteTecnico.disabledFields = _.concat($scope.caixaEntrada.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA);

	function atualizarListaProcessos(processos) {

		cxEntGerenteTecnico.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		cxEntGerenteTecnico.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(cxEntGerenteTecnico.processos, function(processo){

			processo.selecionado = cxEntGerenteTecnico.todosProcessosSelecionados;
		});
	}

	function hasAtLeastOneProcessoSelected() {

		return _.some(cxEntGerenteTecnico.processos, {selecionado: true});		
	}

	function vincularAnalista(processoSelecionado) {
		
		var processosSelecionados = [];

		if (processoSelecionado) {

			processosSelecionados.push(processoSelecionado);

		} else {

		 	_.each(cxEntGerenteTecnico.processos, function(processo){

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

		cxEntGerenteTecnico.todosProcessosSelecionados = 
			
			_.reduce(cxEntGerenteTecnico.processos, function(resultado, p){
			
				return resultado && p;

			}, true);
	}
};

exports.controllers.CxEntGerenteTecnicoController = CxEntGerenteTecnicoController;