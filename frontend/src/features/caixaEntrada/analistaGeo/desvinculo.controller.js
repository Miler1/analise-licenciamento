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
                    $rootScope.$broadcast('atualizarContagemProcessos');
                    $location.path('/caixa-entrada');
                    $uibModalInstance.close();	
            });
        }
		
	};

	
};

exports.controllers.DesvinculoController = DesvinculoController;