var DesvinculoGerenteController = function ($uibModalInstance, processo, $location ,$window,$rootScope, mensagem, desvinculoService,analistaService) {

	var desvinculoGerenteController = this;

	desvinculoGerenteController.processo = processo;
	desvinculoGerenteController.pessoa = null;
	desvinculoGerenteController.desvinculo = null;
	desvinculoGerenteController.analistasGeo = null;
	desvinculoGerenteController.analistaGeoDestino = {};

	function onInit() {
		// desvinculoGerenteController.buscarAgenteSolicitante();
		desvinculoGerenteController.buscarDesvinculoPeloProcesso();
		desvinculoGerenteController.buscarAnalistasGeoByIdProcesso();
	}

	desvinculoGerenteController.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

	desvinculoGerenteController.cancelar= function() {
		$location.path('/caixa-entrada');
	};

	desvinculoGerenteController.concluir = function() {

		var params = desvinculoGerenteController.desvinculo;
		if(desvinculoGerenteController.desvinculoAceito) {
			params.analistaGeoDestino = {};
			params.analistaGeoDestino.id = desvinculoGerenteController.analistaGeoDestino.id;
		}
		params.aprovada = desvinculoGerenteController.desvinculoAceito;
		params.respostaGerente = desvinculoGerenteController.respostaGerente;

		desvinculoService.respondersolicitacaoDesvinculo(params)
			.then(function(response){

				mensagem.success(response.data);
				$rootScope.$broadcast('atualizarContagemProcessos');
				$location.path('/caixa-entrada');
				$uibModalInstance.close();
		}).catch(function(response){
			mensagem.error(response.data.texto, {referenceId: 5});
		});

	};

	desvinculoGerenteController.buscarDesvinculoPeloProcesso = function() {
		desvinculoService.buscarDesvinculoPeloProcesso(processo.idProcesso)
			.then(function(response) {
				desvinculoGerenteController.desvinculo = response.data;
			});
	};

	desvinculoGerenteController.buscarAnalistasGeoByIdProcesso = function() {
		analistaService.buscarAnalistasGeoByIdProcesso(processo.idProcesso)
			.then(function(response) {
				desvinculoGerenteController.analistasGeo = response.data;
			});
	};

	desvinculoGerenteController.validarModalParaConcluir = function() {
		var valido = false;

		if(desvinculoGerenteController.desvinculoAceito){
			valido = desvinculoGerenteController.analistaGeoDestino.id &&
					 desvinculoGerenteController.analistaGeoDestino.id != "" &&
					 desvinculoGerenteController.respostaGerente;
		} else {
			valido = desvinculoGerenteController.desvinculoAceito != undefined &&
					desvinculoGerenteController.respostaGerente;
			
			desvinculoGerenteController.analistaGeoDestino.id = undefined;
		}

		return valido;
	};

	onInit();
};

exports.controllers.DesvinculoGerenteController = DesvinculoGerenteController;