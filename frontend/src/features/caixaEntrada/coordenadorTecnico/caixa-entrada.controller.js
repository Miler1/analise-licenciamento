var CxEntCoordenadorTecnicoController = function($scope, config, analistaService, mensagem, $uibModal, $rootScope, processoService, gerenteService) {

	$rootScope.tituloPagina = 'AGUARDANDO VINCULAÇÃO TÉCNICA';

	var cxEntCoordenadorTecnico = this;

	cxEntCoordenadorTecnico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntCoordenadorTecnico.atualizarPaginacao = atualizarPaginacao;
	cxEntCoordenadorTecnico.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntCoordenadorTecnico.vincularAnalista = vincularAnalista;
	cxEntCoordenadorTecnico.onPaginaAlterada = onPaginaAlterada;
	cxEntCoordenadorTecnico.hasAtLeastOneProcessoSelected = hasAtLeastOneProcessoSelected;
	cxEntCoordenadorTecnico.visualizarProcesso = visualizarProcesso;
	cxEntCoordenadorTecnico.vincularGerente = vincularGerente;
	cxEntCoordenadorTecnico.afterUpdateFilters = afterUpdateFilters;
	cxEntCoordenadorTecnico.verificarTodosProcessosMarcados = verificarTodosProcessosMarcados;
	cxEntCoordenadorTecnico.getTitleSelecaoProcesso = getTitleSelecaoProcesso;

	cxEntCoordenadorTecnico.processos = [];
	cxEntCoordenadorTecnico.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_VINCULACAO_TECNICA_PELO_COORDENADOR;
	cxEntCoordenadorTecnico.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntCoordenadorTecnico.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntCoordenadorTecnico.dateUtil = app.utils.DateUtil;
	cxEntCoordenadorTecnico.selecionouUmGerente = false;

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

	function getProcessosSelecionados(processoSelecionado) {

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

		return processosSelecionados;
	}

	function vincularAnalista(processoSelecionado) {
		
		var processosSelecionados = getProcessosSelecionados(processoSelecionado);

		if (processosSelecionados.length === 0) {

			mensagem.warning('É necessário selecionar ao menos um processo para vinculá-lo ao analista.');
			return;
		}

		var modalInstance = abrirModal(processosSelecionados, 'analista técnico', getAnalistas, true);

		modalInstance.result
			.then(function (result) {

				analistaService.vincularAnaliseAnalistaTecnico(result.idConsultorSelecionado, result.justificativa, result.idsProcessosSelecionados)
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

	function vincularGerente(processoSelecionado) {
		
		var processosSelecionados = getProcessosSelecionados(processoSelecionado);

		if (processosSelecionados.length === 0) {

			mensagem.warning('É necessário selecionar ao menos um processo para vinculá-lo ao gerente.');
			return;
		}

		var modalInstance = abrirModal(processosSelecionados, 'gerente técnico', getGerentes, false);

		modalInstance.result
			.then(function (result) {

				analistaService.vincularAnaliseGerenteTecnico(result.idConsultorSelecionado, result.idsProcessosSelecionados)
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

	function abrirModal(processos, tipo, getAnalistasGerentes, justificationEnabled){
		
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
				consultores: getAnalistasGerentes(processos[0].idProcesso),
				tipo: function() {
					return tipo;
				},
				justificationEnabled: function(){
					return justificationEnabled;
				}
			}
		});

		return modalInstance;
	}

	function afterUpdateFilters(filtro) {

		cxEntCoordenadorTecnico.selecionouUmGerente = !!filtro.idSetor;

		if (!cxEntCoordenadorTecnico.selecionouUmGerente) {

			cxEntCoordenadorTecnico.todosProcessosSelecionados = false;
			selecionarTodosProcessos();
		}
	}

	function getTitleSelecaoProcesso(){

		return cxEntCoordenadorTecnico.selecionouUmGerente ? '' : 'Para selecionar mais de um processo é necessário filtrar por uma gerência.';
	}

	function getAnalistas(idProcesso) {

		return analistaService.getAnalistasTecnicosByProcesso(idProcesso);
	}

	function getGerentes(idProcesso) {

		return gerenteService.getGerentesTecnicosByIdProcesso(idProcesso);
	}	

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}

	function verificarTodosProcessosMarcados() {

		cxEntCoordenadorTecnico.todosProcessosSelecionados = 
			
			_.reduce(cxEntCoordenadorTecnico.processos, function(resultado, p){
			
				return resultado && p.selecionado;

			}, true);
	}
};

exports.controllers.CxEntCoordenadorTecnicoController = CxEntCoordenadorTecnicoController;