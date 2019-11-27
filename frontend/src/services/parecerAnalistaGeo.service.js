var ParecerAnalistaGeo = function(request, config) {

	this.findParecerByIdHistoricoTramitacao = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/analistaGeo/' + id);

    };

};

exports.services.ParecerAnalistaGeo = ParecerAnalistaGeo;
