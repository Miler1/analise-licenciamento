var CxEntCoordenadorJuridicoController = function($scope, config, consultorService, mensagem, $uibModal, $rootScope) {

	$rootScope.tituloPagina = 'AGUARDANDO ANÁLISE JURÍDICA';

	var cxEntCoordenadorJuridico = this;

	cxEntCoordenadorJuridico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntCoordenadorJuridico.atualizarPaginacao = atualizarPaginacao;
	cxEntCoordenadorJuridico.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntCoordenadorJuridico.vincularConsultor = vincularConsultor;
	cxEntCoordenadorJuridico.onPaginaAlterada = onPaginaAlterada;

	cxEntCoordenadorJuridico.processos = [];
	cxEntCoordenadorJuridico.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_VINCULACAO_JURIDICA;
	cxEntCoordenadorJuridico.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntCoordenadorJuridico.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;

	function atualizarListaProcessos(processos) {

		cxEntCoordenadorJuridico.processos = processos;
	}

	function atualizarPaginacao(totalItens) {

		cxEntCoordenadorJuridico.paginacao.update(totalItens, cxEntCoordenadorJuridico.paginacao.paginaAtual);
	}

	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessos');
	}

	function selecionarTodosProcessos() {

		_.each(cxEntCoordenadorJuridico.processos, function(processo){

			processo.selecionado = cxEntCoordenadorJuridico.todosProcessosSelecionados;
		});
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

			mensagem.warning('É necessário selecionar ao menos um processo para vinculá-lo ao consultor');
			return;
		}

		var modalInstance = abrirModal(processosSelecionados);

		modalInstance.result
			.then(function (result) {

				consultorService.vincularAnaliseConsultorJuridico(result.idConsultorSelecionado, result.idsProcessosSelecionados)
					.then(function(response){

						$scope.$broadcast('pesquisarProcessos');
						mensagem.success(response.data);						
					})
					.catch(function(response){
						mensagem.error(response.data.texto, {ttl: 15000});
					});				
			})
			.catch(function(){ });
	}

	function abrirModal(processos){
		
		var modalInstance = $uibModal.open({
			controller: 'modalVincularConsutorJuridicoController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			keyboard  : false,
			templateUrl: './features/caixaEntrada/coordenadorJuridico/modal-vincular-consultor.html',
			size: "lg",
			resolve: {
				processos: function () {
					return processos;
				},
				consultores: getConsultores
			}
		});

		return modalInstance;
	}

	function getConsultores(consultorService) {

		return consultorService.getConsultoresJuridicos();
	}
};

exports.controllers.CxEntCoordenadorJuridicoController = CxEntCoordenadorJuridicoController;