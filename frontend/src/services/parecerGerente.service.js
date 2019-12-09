var ParecerGerenteService = function(request, config) {

	this.findParecerByIdHistoricoTramitacao = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/gerente/' + id);

	};
	
	this.findJustificativaParecerByIdAnaliseGeo = function(idAnaliseGeo) {

		return request
			.get(config.BASE_URL() + 'parecer/justificativa/gerente/findByIdAnaliseGeo/' + idAnaliseGeo);

    };

};

exports.services.ParecerGerenteService = ParecerGerenteService;
