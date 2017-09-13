var LicencaService = function(request, config) {

	this.getLicencasPesquisaRapida = function(filtro) {

		return request
			.post(config.BASE_URL() + "licencas/pesquisaRapida", filtro);
	};

	this.getLicencasPesquisaRapidaCount = function(filtro) {

		return request
			.post(config.BASE_URL() + "licencas/pesquisaRapida/count", filtro);
	};

	this.getLicencasPesquisaAvancada = function(filtro) {
		
		return request
			.post(config.BASE_URL() + "licencas/pesquisaAvancada", filtro);
	};

	this.getLicencasPesquisaAvancadaCount = function(filtro) {

		return request
			.post(config.BASE_URL() + "licencas/pesquisaAvancada/count", filtro);
	};
};

exports.services.LicencaService = LicencaService;