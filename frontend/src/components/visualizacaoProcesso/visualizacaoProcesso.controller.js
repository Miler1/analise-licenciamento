var VisualizacaoProcessoController = function ($uibModalInstance, processo, $scope) {

	var modalCtrl = this;

	//$scope.selecionado = 'dados';

	modalCtrl.processo = processo;

	console.log('Entrou' + modalCtrl.processo);

	modalCtrl.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

};

exports.controllers.VisualizacaoProcessoController = VisualizacaoProcessoController;
