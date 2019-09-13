var DesvinculoGerenteController = function ($uibModalInstance, processo, $location ,$window,$rootScope, mensagem, desvinculoService) {

	var desvinculoGerenteController = this;

	desvinculoGerenteController.processo = processo;
	desvinculoGerenteController.pessoa = null;
	desvinculoGerenteController.desvinculo = null;
	desvinculoGerenteController.analistasGeo = null;

	function onInit() {
		// desvinculoGerenteController.buscarAgenteSolicitante();
		desvinculoGerenteController.buscarDesvinculoPeloProcesso();
		desvinculoGerenteController.buscarAnalistasGeo();
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
			params.analistaGeo.id = desvinculoGerenteController.analistaEscolhido;
		}
		params.aprovada = desvinculoGerenteController.desvinculoAceito;
		params.respostaGerente = desvinculoGerenteController.respostaGerente;

		desvinculoService.respondersolicitacaoDesvinculo(params)
			.then(function(response){

				mensagem.success(response.data);
				$rootScope.$broadcast('atualizarContagemProcessos');
				$location.path('/caixa-entrada');
				$uibModalInstance.close();	
				$window.location.reload();
		}).catch(function(response){
			mensagem.error(response.data.texto, {referenceId: 5});
		});

	};

	// desvinculoGerenteController.buscarAgenteSolicitante = function() {
	// 	desvinculoService.buscarAgenteSolicitante(processo.idProcesso)
	// 		.then(function(response) {
	// 			desvinculoGerenteController.pessoa = response.data;
	// 		});
	// };

	desvinculoGerenteController.buscarDesvinculoPeloProcesso = function() {
		desvinculoService.buscarDesvinculoPeloProcesso(processo.idProcesso)
			.then(function(response) {
				desvinculoGerenteController.desvinculo = response.data;
			});
	};

	desvinculoGerenteController.buscarAnalistasGeo = function() {
		desvinculoService.buscarAnalistasGeo(processo.idProcesso)
			.then(function(response) {
				desvinculoGerenteController.analistasGeo = response.data;
			});
	};

	desvinculoGerenteController.validarModalParaConcluir = function() {
		var valido = false;

		if(desvinculoGerenteController.desvinculoAceito){
			valido = desvinculoGerenteController.analistaEscolhido &&
					 desvinculoGerenteController.analistaEscolhido != "" &&
					 desvinculoGerenteController.respostaGerente;
		} else {
			valido = desvinculoGerenteController.desvinculoAceito != undefined &&
					desvinculoGerenteController.respostaGerente;
			
			desvinculoGerenteController.analistaEscolhido = undefined;
		}

		return valido;
	};

	onInit();
};

exports.controllers.DesvinculoGerenteController = DesvinculoGerenteController;