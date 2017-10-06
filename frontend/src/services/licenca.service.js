var LicencaService = function(request, config) {

	this.emitirLicencas = function(licencasAnalise) {

		return request
			.post(config.BASE_URL() + "licencasAnalise/emitir", licencasAnalise);
	};

    this.findInfoLicenca = function(idLicenca) {
		
		return request
			.get(config.BASE_URL() + "licencas/" + idLicenca);
	};

};

exports.services.LicencaService = LicencaService;