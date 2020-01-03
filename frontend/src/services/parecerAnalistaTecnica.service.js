var ParecerAnalistaTecnicoService = function(request, config) {

	this.concluir = function(parecer) {

		return request
			.post(config.BASE_URL() + 'parecer/analistaTecnico/concluir', parecer);

	};

};

exports.services.ParecerAnalistaTecnicoService = ParecerAnalistaTecnicoService;
