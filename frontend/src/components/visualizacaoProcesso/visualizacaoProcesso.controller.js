var VisualizacaoProcessoController = function ($uibModalInstance, processo, $scope, processoService, mensagem, municipioService, documentoLicenciamentoService) {

	var modalCtrl = this;

	modalCtrl.processo = processo;

	modalCtrl.baixarDocumento = baixarDocumento;

	processoService.getInfoProcesso(processo.idProcesso)
		.then(function(response){

			modalCtrl.dadosProcesso = response.data;
			modalCtrl.limite = modalCtrl.dadosProcesso.empreendimento.imovel ? modalCtrl.dadosProcesso.empreendimento.imovel.limite : modalCtrl.dadosProcesso.empreendimento.municipio.limite;
			console.log(response.data);
		})
		.catch(function(){
			mensagem.error("Ocorreu um erro ao buscar dados do processo.");
		});

	modalCtrl.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};


	function baixarDocumento(idDocumento) {

		documentoLicenciamentoService.download(idDocumento);
	}

};

exports.controllers.VisualizacaoProcessoController = VisualizacaoProcessoController;
