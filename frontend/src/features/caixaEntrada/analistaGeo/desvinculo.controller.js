var DesvinculoController = function ($uibModalInstance,idAnaliseGeo, idProcesso, $location ,$window,$rootScope, mensagem, desvinculoService) {

    var desvinculoController = this;
    desvinculoController.respondido =null;
    

        desvinculoService.buscarDesvinculoPeloProcesso(idProcesso)
        .then(function(response){

            desvinculoController.justificativa = response.data.respostaGerente;
            desvinculoController.respondido = true;
            $('#justificativa').prop('disabled', true);
            
        }).catch(function(response){

            desvinculoController.respondido = false;

        });
	

	desvinculoController.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

	
	desvinculoController.cancelar= function() {
		$location.path('/caixa-entrada');
    };

    desvinculoController.concluir = function() {
        var params={
            justificativa: desvinculoController.justificativa,
            analiseGeo: {id: idAnaliseGeo}
        };

        desvinculoService.solicitarDesvinculo(params)
            .then(function(response){

                $rootScope.$broadcast('atualizarContagemProcessos');
                mensagem.success(response.data);
                $location.path('/caixa-entrada');
                $uibModalInstance.close();
        }).catch(function(response){
            mensagem.error(response.data.texto, {referenceId: 5});
        });

	};

	
};

exports.controllers.DesvinculoController = DesvinculoController;