var TipoLicencaManejoService = function(request, config) {

	this.findAll = function() {

		return request.get(config.BASE_URL() + "tiposLicencaManejo");
	};

};

exports.services.TipoLicencaManejoService = TipoLicencaManejoService;