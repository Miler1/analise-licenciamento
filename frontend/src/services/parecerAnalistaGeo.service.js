var ParecerAnalistaGeoService = function(request, config) {

	this.findParecerByIdHistoricoTramitacao = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/analistaGeo/findParecerByIdHistoricoTramitacao/' + id);

	};

	this.findParecerByIdAnaliseGeo = function(idAnaliseGeo) {

		return request
			.get(config.BASE_URL() + 'parecer/analistaGeo/findParecerByIdAnaliseGeo/' + idAnaliseGeo);

	};
	
	this.getParecerByNumeroProcesso = function(numeroProcesso) {

		return request
				.get(config.BASE_URL() + 'parecer/analistaGeo?numeroProcesso=' + numeroProcesso);

	};

	this.findParecerByIdProcesso = function(id) {

		return request
				.get(config.BASE_URL() + 'parecer/analistaGeo/findParecerByIdProcesso/' + id);

	};

};

exports.services.ParecerAnalistaGeoService = ParecerAnalistaGeoService;
