var ParecerAnalistaTecnicoService = function(request, config) {

	this.findParecerByIdHistoricoTramitacao = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/analistaTecnico/findParecerByIdHistoricoTramitacao/' + id);

	};

	this.concluir = function(parecer) {

		return request
			.post(config.BASE_URL() + 'parecer/analistaTecnico/concluir', parecer);

	};
	
	this.getUltimoParecerAnaliseTecnica = function(id) {
		return request
			.get(config.BASE_URL() + 'parecer/analistaTecnico/getUltimoParecerAnaliseTecnica/' + id);

	};
	
};

exports.services.ParecerAnalistaTecnicoService = ParecerAnalistaTecnicoService;
