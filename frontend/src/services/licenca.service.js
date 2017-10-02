var LicencaService = function(request, config, $uibModal) {

	this.emitirLicencas = function(licencasAnalise) {

		return request
			.post(config.BASE_URL() + "licencasAnalise/emitir", licencasAnalise);
	};

    this.findLicencaAnalise = function(idLicenca) {
		
		return request
			.get(config.BASE_URL() + "licencasAnalise/" + idLicenca);
	};

	this.suspenderLicenca = function(licencaAnalise) {
		
		var modalInstance = $uibModal.open({

			component: 'modalVisualizarLicenca',
			size: 'lg',
			resolve: {

				dadosLicenca: function () {

					return licencaAnalise;
				}
			}
		});

	};
};

exports.services.LicencaService = LicencaService;