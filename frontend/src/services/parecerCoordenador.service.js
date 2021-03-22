var ParecerCoordenadorService = function(request, config) {

	this.findParecerByIdHistoricoTramitacao = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/coordenador/findParecerByIdHistoricoTramitacao/' + id);

	};

	this.findParecerTecnicoByIdHistoricoTramitacao = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/coordenador/findParecerTecnicoByIdHistoricoTramitacao/' + id);

	};

	this.getUltimoParecerCoordenadorAnaliseTecnica = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/coordenador/getUltimoParecerCoordenadorAnaliseTecnica/' + id);

	};
	
	this.getUltimoParecerCoordenadorAnaliseGeo = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/coordenador/getUltimoParecerCoordenadorAnaliseGeo/' + id);

	};
	
	this.findJustificativaParecerByIdAnaliseGeo = function(idAnaliseGeo) {

		return request
			.get(config.BASE_URL() + 'parecer/coordenador/justificativa/findByIdAnaliseGeo/' + idAnaliseGeo);

	};
	
	this.findJustificativaParecerByIdAnaliseTecnica = function(idAnaliseTecnica) {

		return request
			.get(config.BASE_URL() + 'parecer/coordenador/justificativa/findByIdAnaliseTecnica/' + idAnaliseTecnica);

    };

};

exports.services.ParecerCoordenadorService = ParecerCoordenadorService;
