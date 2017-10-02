var LicencaService = function(request, config, $uibModal) {

	this.emitirLicencas = function(licencasAnalise) {

		return request
			.post(config.BASE_URL() + "licencasAnalise/emitir", licencasAnalise);
	};

	this.suspenderLicenca = function(licenca) {

		var modalInstance = $uibModal.open({
			templateUrl: 'components/modalResumoLicenca/modalResumoLicenca.html',
			size: 'lg',
		});

	};
};

exports.services.LicencaService = LicencaService;