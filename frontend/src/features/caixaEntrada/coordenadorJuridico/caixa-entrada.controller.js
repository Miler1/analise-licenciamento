var CxEntCoordenadorJuridicoController = function($scope, config, consultorService, mensagem, $uibModal, $rootScope, processoService) {

	$rootScope.tituloPagina = 'AGUARDANDO VINCULAÇÃO JURÍDICA';

	var cxEntCoordenadorJuridico = this;

	cxEntCoordenadorJuridico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntCoordenadorJuridico.atualizarPaginacao = atualizarPaginacao;
	cxEntCoordenadorJuridico.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntCoordenadorJuridico.vincularConsultor = vincularConsultor;
	cxEntCoordenadorJuridico.onPaginaAlterada = onPaginaAlterada;
	cxEntCoordenadorJuridico.hasAtLeastOneProcessoSelected = hasAtLeastOneProcessoSelected;
	cxEntCoordenadorJuridico.visualizarProcesso = visualizarProcesso;

	cxEntCoordenadorJuridico.processos = [];
	cxEntCoordenadorJuridico.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_VINCULACAO_JURIDICA;
	cxEntCoordenadorJuridico.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntCoordenadorJuridico.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;
	cxEntCoordenadorJuridico.PrazoAnalise = app.utils.PrazoAnalise;
	cxEntCoordenadorJuridico.dateUtil = app.utils.DateUtil;
	cxEntCoordenadorJuridico.verificarTodosProcessosMarcados = verificarTodosProcessosMarcados;
	cxEntCoordenadorJuridico.disabledFields = _.concat($scope.caixaEntrada.disabledFields, app.DISABLED_FILTER_FIELDS.GERENCIA, app.DISABLED_FILTER_FIELDS.ANALISTA_GEO);

	function atualizarListaProcessos(processos) {

		cxEntCoordenadorJuridico.processos = processos;
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		cxEntCoordenadorJuridico.paginacao.update(totalItens, paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(cxEntCoordenadorJuridico.processos, function(processo){

			processo.selecionado = cxEntCoordenadorJuridico.todosProcessosSelecionados;
		});
	}

	function hasAtLeastOneProcessoSelected() {

		return _.some(cxEntCoordenadorJuridico.processos, {selecionado: true});
	}

	function vincularConsultor(processoSelecionado) {

		var processosSelecionados = [];

		if (processoSelecionado) {

			processosSelecionados.push(processoSelecionado);

		} else {

		 	_.each(cxEntCoordenadorJuridico.processos, function(processo){

				 if (processo.selecionado) {

					 processosSelecionados.push(processo);
				 }
			});
		}

		if (processosSelecionados.length === 0) {

			mensagem.warning('É necessário selecionar ao menos um protocolo para vinculá-lo ao consultor.');
			return;
		}

		var modalInstance = abrirModal(processosSelecionados);

		modalInstance.result
			.then(function (result) {

				consultorService.vincularAnaliseConsultorJuridico(result.idConsultorSelecionado, result.idsProcessosSelecionados)
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
				consultores: getConsultores,
				tipo: function() {
					return 'consultor jurídico';
				},
				justificationEnabled: function() {
					return false;
				}
			}
		});

		return modalInstance;
	}

	function getConsultores() {

		return consultorService.getConsultoresJuridicos();
	}

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}

	function verificarTodosProcessosMarcados() {

		cxEntCoordenadorJuridico.todosProcessosSelecionados =

			_.reduce(cxEntCoordenadorJuridico.processos, function(resultado, p){

				return resultado && p;

			}, true);
	}
};

exports.controllers.CxEntCoordenadorJuridicoController = CxEntCoordenadorJuridicoController;