var LicencaEmitidaService = function(request,$window, config) {

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

		$window.open(config.BASE_URL() + "licencasEmitidas/" + idLicenca + "/downloadDla", "_blank");
	};

	this.downloadLicenca = function(idLicenca) {

		$window.open(config.BASE_URL() + "licencasEmitidas/" + idLicenca + "/download", '_blank');
		
	};

	this.suspenderLicenca = function(suspensao){

		return request
			.post(config.BASE_URL() + "suspensoes/licenca", suspensao);
	};

	this.suspenderDispensa = function(suspensao){

		return request
			.post(config.BASE_URL() + "suspensoes/dispensa", suspensao);
	};

	this.cancelarLicenca = function(cancelamento){

		return request
			.post(config.BASE_URL() + "cancelamentos/licenca", cancelamento);
	};

	this.cancelarDLA = function(cancelamento) {

		return request
			.post(config.BASE_URL() + "cancelamentos/dla", cancelamento);
	};

	this.cancelarDispensa = function(cancelamento) {

		return request
			.post(config.BASE_URL() + "cancelamentos/cancelarDispensa", cancelamento);
	};
};

exports.services.LicencaEmitidaService = LicencaEmitidaService;