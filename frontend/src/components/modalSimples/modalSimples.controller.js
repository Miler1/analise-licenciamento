var ModalSimplesController = function ($uibModalInstance, titulo, conteudo, conteudoDestaque,
										labelBotaoConfirmar, labelBotaoCancelar, exibirFooter) {

	var modalCtrl = this;

	modalCtrl.titulo = titulo;
	modalCtrl.conteudo = conteudo;
	modalCtrl.conteudoDestaque = conteudoDestaque;
	modalCtrl.labelBotaoConfirmar = labelBotaoConfirmar;
	modalCtrl.labelBotaoCancelar = labelBotaoCancelar;
	modalCtrl.exibirFooter = exibirFooter;

	modalCtrl.ok = function () {
		$uibModalInstance.close();
	};

	modalCtrl.cancelar = function () {
		$uibModalInstance.dismiss('cancel');
	};

};

exports.controllers.ModalSimplesController = ModalSimplesController;
