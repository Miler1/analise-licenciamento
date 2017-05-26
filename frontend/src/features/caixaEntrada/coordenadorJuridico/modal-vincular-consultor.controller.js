var ModalVincularConsutorJuridicoController = function ($uibModalInstance, mensagem, processos, consultores, $scope) {

	var modalCtrl = this;

	modalCtrl.processos = processos;

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

exports.controllers.ModalVincularConsutorJuridicoController = ModalVincularConsutorJuridicoController;
