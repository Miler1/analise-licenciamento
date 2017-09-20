var LicencaService = function(request, config) {

	this.emitirLicencas = function(licencasAnalise) {

		return request
			.post(config.BASE_URL() + "/licencasAnalise/emitir", licencasAnalise);
	};
};

exports.services.LicencaService = LicencaService;