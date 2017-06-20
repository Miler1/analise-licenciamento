var ModalVincularConsultorController = function ($uibModalInstance, mensagem, processos, consultores, $scope, tipo) {

	var modalCtrl = this;

	modalCtrl.processos = processos;
	modalCtrl.tipo = tipo;

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

		$uibModalInstance.close({
			idConsultorSelecionado: modalCtrl.idConsultorSelecionado, 
			idsProcessosSelecionados: idsProcessosSelecionados
		});
	};

	modalCtrl.cancelar = function () {
		$uibModalInstance.dismiss();
	};

};

exports.controllers.ModalVincularConsultorController = ModalVincularConsultorController;
