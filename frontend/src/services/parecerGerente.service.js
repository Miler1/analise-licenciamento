var ParecerGerenteService = function(request, config) {

	this.findParecerByIdHistoricoTramitacao = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/gerente/findParecerByIdHistoricoTramitacao/' + id);

    };

};

exports.services.ParecerGerenteService = ParecerGerenteService;
