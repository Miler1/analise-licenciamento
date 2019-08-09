var DesvinculoController = function ($uibModalInstance,idProcesso , mensagem, desvinculoService) {

	var desvinculoController = this;
	

	desvinculoController.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

	
	desvinculoController.cancelar= function() {
		$location.path('/caixa-entrada');
    };
    
    desvinculoController.concluir = function() {
        var params={
            justificativa: desvinculoController.justificativa,
            processo: {id: idProcesso}
        };
            desvinculoService.solicitarDesvinculo(params)
                .then(function(response){

                    mensagem.success(response.data);
                    $uibModalInstance.close();	
                    $location.path('/caixa-entrada');
            });

	};

	
};

exports.controllers.DesvinculoController = DesvinculoController;