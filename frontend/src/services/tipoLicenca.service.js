var TipoLicencaService = function(request, config) {

	this.getTiposLicencas = function() {

		return request.getWithCache(config.BASE_URL() + 'tiposLicencas');
	};

};

exports.services.TipoLicencaService = TipoLicencaService;