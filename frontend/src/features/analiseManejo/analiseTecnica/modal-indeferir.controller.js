var ModalIndeferirController = function ($uibModalInstance) {

	var modalCtrl = this;
	modalCtrl.texto = "";

	modalCtrl.cancelar = function() {

		$uibModalInstance.close();
	};

	modalCtrl.adicionar = function () {

		if (!validarFormulario()) {

			return false;
		}

		var dados = {
			justificativaIndeferimento: modalCtrl.texto
		};

		$uibModalInstance.close(dados);
	};

	function validarFormulario() {

		modalCtrl.formJustificativa.$setSubmitted();

		return modalCtrl.formJustificativa.$valid;
	}
};

exports.controllers.ModalIndeferirController = ModalIndeferirController;
