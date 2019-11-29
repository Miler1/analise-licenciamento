var DesvinculoAnaliseTecnicaController = function ($uibModalInstance,idAnaliseTecnica, idProcesso, $location,$rootScope, mensagem, desvinculoService) {

    var desvinculoController = this;
    desvinculoController.respondido =null;
    desvinculoController.errorJustificativa = false;

        desvinculoService.buscarDesvinculoPeloProcessoTecnico(idProcesso)
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
            analiseTecnica: {id: idAnaliseTecnica}
        };
        if (desvinculoController.justificativa === '' || !desvinculoController.justificativa){
            mensagem.error("Verifique os campos obrigatórios!",{referenceId: 5});
            desvinculoController.errorJustificativa = true;
            return false;
        }
        

        desvinculoService.solicitarDesvinculoAnaliseTecnica(params)
            .then(function(response){

                $rootScope.$broadcast('rootPesquisarProcessos');
                $rootScope.$broadcast('atualizarContagemProcessos');
                mensagem.success(response.data);
                $location.path('/caixa-entrada');
                $uibModalInstance.close();
        }).catch(function(response){
            mensagem.error(response.data.texto, {referenceId: 5});
        });

	};

	
};

exports.controllers.DesvinculoAnaliseTecnicaController = DesvinculoAnaliseTecnicaController;