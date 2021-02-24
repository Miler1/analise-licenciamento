var DesvinculoCoordenadorController = function ($uibModalInstance, processo, $location ,$scope,$rootScope, mensagem, desvinculoService,analistaService) {

	var desvinculoCoordenadorController = this;

	desvinculoCoordenadorController.processo = processo;
	desvinculoCoordenadorController.condicaoTramitacao = app.utils.CondicaoTramitacao;
	desvinculoCoordenadorController.pessoa = null;
	desvinculoCoordenadorController.desvinculo = null;
	desvinculoCoordenadorController.analistas = null;
	desvinculoCoordenadorController.analistaDestino = {};
	desvinculoCoordenadorController.tipoDesvinculo = null;

	function onInit() {

		if(desvinculoCoordenadorController.processo.idCondicaoTramitacao === desvinculoCoordenadorController.condicaoTramitacao.SOLICITACAO_DESVINCULO_PENDENTE_ANALISE_TECNICA){

			desvinculoCoordenadorController.buscarDesvinculoPeloProcessoTecnico();
			desvinculoCoordenadorController.buscarAnalistasTecnicoByIdProcesso();
			desvinculoCoordenadorController.tipoDesvinculo = false;
			
		}else {

			desvinculoCoordenadorController.buscarDesvinculoPeloProcessoGeo();
			desvinculoCoordenadorController.buscarAnalistasGeoByIdProcesso();
			desvinculoCoordenadorController.tipoDesvinculo = true;

		}
	}

	desvinculoCoordenadorController.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

	desvinculoCoordenadorController.cancelar= function() {
		$location.path('/caixa-entrada');
	};

	desvinculoCoordenadorController.concluir = function() {

		var params = desvinculoCoordenadorController.desvinculo;

		params.aprovada = desvinculoCoordenadorController.desvinculoAceito;
		params.respostaCoordenador = desvinculoCoordenadorController.respostaCoordenador;

		if (desvinculoCoordenadorController.tipoDesvinculo === true){

			if(desvinculoCoordenadorController.desvinculoAceito) {
				params.analistaGeoDestino = {};
				params.analistaGeoDestino.id = desvinculoCoordenadorController.analistaDestino.id;
			}

			desvinculoService.responderSolicitacaoDesvinculoAnaliseGeo(params)
			.then(function(response){

				$rootScope.$broadcast('rootPesquisarProcessos');
				$rootScope.$broadcast('atualizarContagemProcessos');
				mensagem.success(response.data);
				$uibModalInstance.close();

			}).catch(function(response){
				mensagem.error(response.data.texto, {referenceId: 5});
			});

		}else if (desvinculoCoordenadorController.tipoDesvinculo === false){

			if(desvinculoCoordenadorController.desvinculoAceito) {
				params.analistaTecnicoDestino = {};
				params.analistaTecnicoDestino.id = desvinculoCoordenadorController.analistaDestino.id;
			}

			desvinculoService.responderSolicitacaoDesvinculoAnaliseTecnica(params)
			.then(function(response){

				$rootScope.$broadcast('rootPesquisarProcessos');
				$rootScope.$broadcast('atualizarContagemProcessos');
				mensagem.success(response.data);
				$location.path('/caixa-entrada');
				$uibModalInstance.close();

			}).catch(function(response){
				mensagem.error(response.data.texto, {referenceId: 5});
			});
		}

	};

	desvinculoCoordenadorController.buscarDesvinculoPeloProcessoGeo = function() {
		desvinculoService.buscarDesvinculoPeloProcessoGeo(processo.idProcesso)
			.then(function(response) {
				desvinculoCoordenadorController.desvinculo = response.data;
			});
	};

	desvinculoCoordenadorController.buscarAnalistasGeoByIdProcesso = function() {
		analistaService.buscarAnalistasGeoByIdProcesso(processo.idProcesso)
			.then(function(response) {
				desvinculoCoordenadorController.analistas = response.data;
			});
	};

	desvinculoCoordenadorController.buscarDesvinculoPeloProcessoTecnico = function() {
		desvinculoService.buscarDesvinculoPeloProcessoTecnico(processo.idProcesso)
			.then(function(response) {
				desvinculoCoordenadorController.desvinculo = response.data;
			});
	};

	desvinculoCoordenadorController.buscarAnalistasTecnicoByIdProcesso = function() {
		analistaService.buscarAnalistasTecnicoByIdProcesso(processo.idProcesso)
			.then(function(response) {
				desvinculoCoordenadorController.analistas = response.data;
			});
	};

	desvinculoCoordenadorController.validarModalParaConcluir = function() {
		var valido = false;

		if(desvinculoCoordenadorController.desvinculoAceito){
			valido = desvinculoCoordenadorController.analistaDestino.id &&
					 desvinculoCoordenadorController.analistaDestino.id != "" &&
					 desvinculoCoordenadorController.respostaCoordenador;
		} else {
			valido = desvinculoCoordenadorController.desvinculoAceito != undefined &&
					desvinculoCoordenadorController.respostaCoordenador;
			
			desvinculoCoordenadorController.analistaDestino.id = undefined;
		}

		return valido;
	};

	onInit();
};

exports.controllers.DesvinculoCoordenadorController = DesvinculoCoordenadorController;