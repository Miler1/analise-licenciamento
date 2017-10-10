var DispensaLicencaService = function(request, config) {

	this.findInfoDLA = function(idDLA) {
		
		return request
			.get(config.BASE_URL() + "dla/" + idDLA);
	};

};

exports.services.DispensaLicencaService = DispensaLicencaService;