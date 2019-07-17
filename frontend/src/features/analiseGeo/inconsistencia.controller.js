var InconsistenciaController = function ($uibModalInstance,analiseGeo,inconsistenciaService, mensagem) {

	var inconsistenciaController = this;

	inconsistenciaController.descricaoInconsistencia = null;
	inconsistenciaController.tipoInconsistencia = null;
	inconsistenciaController.anexos = [];

	inconsistenciaController.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

	inconsistenciaController.concluir = function() {
		var params;
		params = {
			analiseGeo: {id: analiseGeo.id},
			tipoInconsistencia: inconsistenciaController.tipoInconsistencia,
			descricaoInconsistencia: inconsistenciaController.descricaoInconsistencia
		};
		inconsistenciaService.salvarInconsistencia(params).then(function(response){
			if (response.data.texto === 'A inconsistência foi salva com sucesso'){
				mensagem.success(response.data.texto);
				$uibModalInstance.dismiss('cancel');
			}else{
				mensagem.error(response.data.texto);
				$uibModalInstance.dismiss('cancel');
			}
		});
		
	};

};

exports.controllers.InconsistenciaController = InconsistenciaController;