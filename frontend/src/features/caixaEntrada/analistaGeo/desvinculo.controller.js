var DesvinculoController = function ($uibModalInstance,idProcesso, $location ,$window,$rootScope, mensagem, desvinculoService) {

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
                    $rootScope.$broadcast('atualizarContagemProcessos');
                    $location.path('/caixa-entrada');
                    $uibModalInstance.close();	
                    $window.location.reload();
            }).catch(function(response){
				mensagem.error(response.data.texto, {referenceId: 5});
            });
            
	};

	
};

exports.controllers.DesvinculoController = DesvinculoController;