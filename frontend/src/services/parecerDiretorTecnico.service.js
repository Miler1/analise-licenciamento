var ParecerDiretorTecnicoService = function(request, config) {

	this.findParecerByAnalise = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/diretor/findParecerByAnalise/' + id);

	};

};

exports.services.ParecerDiretorTecnicoService = ParecerDiretorTecnicoService;
