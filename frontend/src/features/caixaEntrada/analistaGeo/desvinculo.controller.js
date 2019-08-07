var DesvinculoController = function ($uibModalInstance, processo, mensagem, desvinculoService) {

	var desvinculoController = this;
	

	desvinculoController.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

	
	desvinculoController.cancelar= function() {
		$location.path('/caixa-entrada');
    };
    
	desvinculoController.concluir = function() {
        var justificativa = desvinculoController.justificativa;
        if(typeof justificativa === "undefined"|| desvinculoController.justificativa === ""){

            mensagem.error("Verifique os campos obrigatórios!");
            $uibModalInstance.close();

        }else{
            desvinculoService.solicitarDesvinculo(processo, justificativa)
                .then(function(response){

                    mensagem.success(response.data);
                    $uibModalInstance.close();	
                    $location.path('/caixa-entrada');
            });
        }
		
	};

	
};

exports.controllers.DesvinculoController = DesvinculoController;