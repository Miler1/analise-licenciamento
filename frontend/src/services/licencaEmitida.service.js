var LicencaEmitidaService = function(request, config) {

	this.getLicencasEmitidasPesquisaRapida = function(filtro) {

		return request
			.post(config.BASE_URL() + "licencasEmitidas/pesquisaRapida", filtro);
	};

	this.getLicencasEmitidasPesquisaRapidaCount = function(filtro) {

		return request
			.post(config.BASE_URL() + "licencasEmitidas/pesquisaRapida/count", filtro);
	};

	this.getLicencasEmitidasPesquisaAvancada = function(filtro) {
		
		return request
			.post(config.BASE_URL() + "licencasEmitidas/pesquisaAvancada", filtro);
	};

	this.getLicencasEmitidasPesquisaAvancadaCount = function(filtro) {

		return request
			.post(config.BASE_URL() + "licencasEmitidas/pesquisaAvancada/count", filtro);
	};

	this.downloadDla = function(idLicenca) {
		
		window.location.href = config.BASE_URL() + "licencasEmitidas/" + idLicenca + "/downloadDla";
	};

	this.downloadLicenca = function(idLicenca) {
		
		window.location.href = config.BASE_URL() + "licencasEmitidas/" + idLicenca + "/download";
	};
};

exports.services.LicencaEmitidaService = LicencaEmitidaService;