var VisualizacaoProcessoController = function ($uibModalInstance, processo, $scope, processoService, mensagem, municipioService, documentoLicenciamentoService) {

	var modalCtrl = this;

	modalCtrl.processo = processo;

	modalCtrl.baixarDocumento = baixarDocumento;

	processoService.getInfoProcesso(processo.idProcesso)
		.then(function(response){

			modalCtrl.dadosProcesso = response.data;
			console.log(response.data);
		})
		.catch(function(){
			mensagem.error("Ocorreu um erro ao buscar dados do processo.");
		});

	modalCtrl.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};


	// function getGeometriaMunicipio(id) {

	// 	municipioService.getMunicipioGeometryById(id)
	// 		.then(function(response) {

	// 			etapaGeo.limite = response.data.limite;

	// 		})
	// 		.catch(function() {

	// 			mensagem.error("Não foi possível obter o limite do município.");

	// 		});

	// }

	// function getDadosImovel(codigoImovel) {

	// 	imovelService.getImoveisCompletoByCodigo(codigoImovel)
	// 		.then(function(response){

	// 			etapaGeo.limite = response.data.geo;

	// 		})
	// 		.catch(function(){

	// 			mensagem.error("Não foi possível obter os dados do imóvel no CAR.");

	// 		});

	// 	getTemaGeoserver(codigoImovel, imovelService.camadaLDI, 'camadaLDI', "Lista de desmatamento ilegal (LDI)");

	// 	getTemaGeoserver(codigoImovel, imovelService.camadaPRODES, 'camadaPRODES', "PRODES");

	// }

	function baixarDocumento(idDocumento) {

		documentoLicenciamentoService.download(idDocumento);
	}

};

exports.controllers.VisualizacaoProcessoController = VisualizacaoProcessoController;
