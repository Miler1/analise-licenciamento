var CxEntCoordenadorJuridicoController = function($scope, config, consultorService, mensagem, $uibModal, processoService) {

	var cxEntCoordenadorJuridico = this;

	cxEntCoordenadorJuridico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntCoordenadorJuridico.atualizarPaginacao = atualizarPaginacao;
	cxEntCoordenadorJuridico.calcularDiasRestantes = calcularDiasRestantes;
	cxEntCoordenadorJuridico.getDiasRestantes = getDiasRestantes;
	cxEntCoordenadorJuridico.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntCoordenadorJuridico.isPrazoMinimoAvisoAnalise = isPrazoMinimoAvisoAnalise;
	cxEntCoordenadorJuridico.vincularConsultor = vincularConsultor;
	cxEntCoordenadorJuridico.onPaginaAlterada = onPaginaAlterada;
	cxEntCoordenadorJuridico.visualizarProcesso = visualizarProcesso;

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

	function calcularDiasRestantes(stringDate){

		return moment(stringDate, 'DD/MM/yyyy').startOf('day')
			.diff(moment(Date.now()).startOf('day'), 'days');
	}

	function getDiasRestantes(dataVencimento){

		var diasRestantes = calcularDiasRestantes(dataVencimento);

		return diasRestantes >=0 ? diasRestantes : Math.abs(diasRestantes) + ' dia(s) atraso';
	}

	function selecionarTodosProcessos() {

		_.each(cxEntCoordenadorJuridico.processos, function(processo){

			processo.selecionado = cxEntCoordenadorJuridico.todosProcessosSelecionados;
		});
	}

	function isPrazoMinimoAvisoAnalise(dataVencimento, prazoMinimo) {

		return calcularDiasRestantes(dataVencimento) <= prazoMinimo;
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

	function visualizarProcesso(processo) {

		return processoService.visualizarProcesso(processo);
	}
};

exports.controllers.CxEntCoordenadorJuridicoController = CxEntCoordenadorJuridicoController;