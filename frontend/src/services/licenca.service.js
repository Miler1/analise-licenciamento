var LicencaService = function(request, config, $uibModal) {

	this.emitirLicencas = function(licencasAnalise) {

		return request
			.post(config.BASE_URL() + "licencasAnalise/emitir", licencasAnalise);
	};

    this.findInfoLicenca = function(idLicenca) {
		
		return request
			.get(config.BASE_URL() + "licencas/" + idLicenca);
	};

	this.modalInfoLicencaSuspender = function(licencaSuspender) {
		
		var modalInstance = $uibModal.open({

			component: 'modalVisualizarLicenca',
			size: 'lg',
			resolve: {

				dadosLicenca: function () {

					return licencaSuspender;
				},
				isSuspensao: true
			}
		});

	};
};

exports.services.LicencaService = LicencaService;