var ModalIndeferirController = function ($uibModalInstance) {

	var modalCtrl = this;
	modalCtrl.texto = "";

	// modalCtrl.cancelar = function() {
	//
	// 	$uibModalInstance.close();
	// };
	//
	// modalCtrl.adicionar = function () {
	//
	// 	if (!validarFormulario()) {
	//
	// 		return false;
	// 	}
	//
	// 	var observacao = {
	// 		texto: modalCtrl.texto
	// 	};
	//
	// 	$uibModalInstance.close(observacao);
	// };
	//
	// function validarFormulario() {
	//
	// 	modalCtrl.formObservacao.$setSubmitted();
	//
	// 	return modalCtrl.formObservacao.$valid;
	// }
};

exports.controllers.ModalIndeferirController = ModalIndeferirController;
