var ModalVincularConsultorController = function ($uibModalInstance, mensagem, processos, consultores, $scope, tipo, justificationEnabled) {

	var modalCtrl = this;

	modalCtrl.processos = processos;
	modalCtrl.tipo = tipo;
	modalCtrl.justificationEnabled = justificationEnabled;

	if (consultores){

		modalCtrl.consultores = consultores.data;
	}

	modalCtrl.vincular = function () {

		if (!$scope.formVincularConsultor.$valid){
			return;
		}

		var idsProcessosSelecionados = _.map(modalCtrl.processos, function(processo){
			return processo.idProcesso;
		});

		var response = {
			idConsultorSelecionado: modalCtrl.idConsultorSelecionado, 
			idsProcessosSelecionados: idsProcessosSelecionados
		};

		if (modalCtrl.justificationEnabled) {

			response.justificativa = modalCtrl.justificativa;
		}

		$uibModalInstance.close(response);
	};

	modalCtrl.cancelar = function () {
		$uibModalInstance.dismiss();
	};

};

exports.controllers.ModalVincularConsultorController = ModalVincularConsultorController;
