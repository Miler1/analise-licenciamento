var ParecerGerenteService = function(request, config) {

	this.findParecerByIdHistoricoTramitacao = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/gerente/findParecerByIdHistoricoTramitacao/' + id);

	};

	this.findParecerTecnicoByIdHistoricoTramitacao = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/gerente/findParecerTecnicoByIdHistoricoTramitacao/' + id);

	};

	this.getUltimoParecerGerenteAnaliseTecnica = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/gerente/getUltimoParecerGerenteAnaliseTecnica/' + id);

	};
	
	this.getUltimoParecerGerenteAnaliseGeo = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/gerente/getUltimoParecerGerenteAnaliseGeo/' + id);

	};
	
	this.findJustificativaParecerByIdAnaliseGeo = function(idAnaliseGeo) {

		return request
			.get(config.BASE_URL() + 'parecer/gerente/justificativa/findByIdAnaliseGeo/' + idAnaliseGeo);

	};
	
	this.findJustificativaParecerByIdAnaliseTecnica = function(idAnaliseTecnica) {

		return request
			.get(config.BASE_URL() + 'parecer/gerente/justificativa/findByIdAnaliseTecnica/' + idAnaliseTecnica);

    };

};

exports.services.ParecerGerenteService = ParecerGerenteService;
