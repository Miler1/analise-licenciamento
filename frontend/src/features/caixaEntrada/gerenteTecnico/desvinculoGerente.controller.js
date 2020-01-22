var DesvinculoGerenteController = function ($uibModalInstance, processo, $location ,$window,$rootScope, mensagem, desvinculoService,analistaService) {

	var desvinculoGerenteController = this;

	desvinculoGerenteController.processo = processo;
	desvinculoGerenteController.condicaoTramitacao = app.utils.CondicaoTramitacao;
	desvinculoGerenteController.pessoa = null;
	desvinculoGerenteController.desvinculo = null;
	desvinculoGerenteController.analistas = null;
	desvinculoGerenteController.analistaDestino = {};
	desvinculoGerenteController.tipoDesvinculo = null;

	function onInit() {

		if(desvinculoGerenteController.processo.idCondicaoTramitacao === desvinculoGerenteController.condicaoTramitacao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA){

			desvinculoGerenteController.buscarDesvinculoPeloProcessoTecnico();
			desvinculoGerenteController.buscarAnalistasTecnicoByIdProcesso();
			desvinculoGerenteController.tipoDesvinculo = false;
			
		}else {

			desvinculoGerenteController.buscarDesvinculoPeloProcessoGeo();
			desvinculoGerenteController.buscarAnalistasGeoByIdProcesso();
			desvinculoGerenteController.tipoDesvinculo = true;

		}
	}

	desvinculoGerenteController.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

	desvinculoGerenteController.cancelar= function() {
		$location.path('/caixa-entrada');
	};

	desvinculoGerenteController.concluir = function() {

		var params = desvinculoGerenteController.desvinculo;

		params.aprovada = desvinculoGerenteController.desvinculoAceito;
		params.respostaGerente = desvinculoGerenteController.respostaGerente;

		if (desvinculoGerenteController.tipoDesvinculo === true){

			if(desvinculoGerenteController.desvinculoAceito) {
				params.analistaGeoDestino = {};
				params.analistaGeoDestino.id = desvinculoGerenteController.analistaDestino.id;
			}

			desvinculoService.responderSolicitacaoDesvinculoAnaliseGeo(params)
			.then(function(response){

				mensagem.success(response.data);
				$rootScope.$broadcast('atualizarContagemProcessos');
				$rootScope.$broadcast('rootPesquisarProcessos');
				$location.path('/caixa-entrada');
				$uibModalInstance.dismiss();

			}).catch(function(response){
				mensagem.error(response.data.texto, {referenceId: 5});
			});

		}else if (desvinculoGerenteController.tipoDesvinculo === false){

			if(desvinculoGerenteController.desvinculoAceito) {
				params.analistaTecnicoDestino = {};
				params.analistaTecnicoDestino.id = desvinculoGerenteController.analistaDestino.id;
			}

			desvinculoService.responderSolicitacaoDesvinculoAnaliseTecnica(params)
			.then(function(response){

				mensagem.success(response.data);
				$rootScope.$broadcast('atualizarContagemProcessos');
				$rootScope.$broadcast('rootPesquisarProcessos');
				$location.path('/caixa-entrada');
				$uibModalInstance.dismiss();

			}).catch(function(response){
				mensagem.error(response.data.texto, {referenceId: 5});
			});
		}
	};

	desvinculoGerenteController.buscarDesvinculoPeloProcessoGeo = function() {
		desvinculoService.buscarDesvinculoPeloProcessoGeo(processo.idProcesso)
			.then(function(response) {
				desvinculoGerenteController.desvinculo = response.data;
			});
	};

	desvinculoGerenteController.buscarAnalistasGeoByIdProcesso = function() {
		analistaService.buscarAnalistasGeoByIdProcesso(processo.idProcesso)
			.then(function(response) {
				desvinculoGerenteController.analistas = response.data;
			});
	};

	desvinculoGerenteController.buscarDesvinculoPeloProcessoTecnico = function() {
		desvinculoService.buscarDesvinculoPeloProcessoTecnico(processo.idProcesso)
			.then(function(response) {
				desvinculoGerenteController.desvinculo = response.data;
			});
	};

	desvinculoGerenteController.buscarAnalistasTecnicoByIdProcesso = function() {
		analistaService.buscarAnalistasTecnicoByIdProcesso(processo.idProcesso)
			.then(function(response) {
				desvinculoGerenteController.analistas = response.data;
			});
	};

	desvinculoGerenteController.validarModalParaConcluir = function() {
		var valido = false;

		if(desvinculoGerenteController.desvinculoAceito){
			valido = desvinculoGerenteController.analistaDestino.id &&
					 desvinculoGerenteController.analistaDestino.id != "" &&
					 desvinculoGerenteController.respostaGerente;
		} else {
			valido = desvinculoGerenteController.desvinculoAceito != undefined &&
					desvinculoGerenteController.respostaGerente;
			
			desvinculoGerenteController.analistaDestino.id = undefined;
		}

		return valido;
	};

	onInit();
};

exports.controllers.DesvinculoGerenteController = DesvinculoGerenteController;