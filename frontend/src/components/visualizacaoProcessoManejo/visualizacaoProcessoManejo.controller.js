var VisualizacaoProcessoManejoController = function ($location, $anchorScroll, $timeout, $uibModalInstance, processo, $scope, processoManejoService) {

	var modalCtrl = this;

	modalCtrl.processo = processo;

	if (processo.idProcesso) {

		processoManejoService.getProcesso(processo.idProcesso)
			.then(function(response){

				modalCtrl.dadosProcesso = response.data;
			})
			.catch(function(){
				mensagem.error("Ocorreu um erro ao buscar dados do processo do manejo.");
			});

	}

	modalCtrl.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

};

exports.controllers.VisualizacaoProcessoManejoController = VisualizacaoProcessoManejoController;