var ParecerAnalistaTecnicoService = function(request, config) {

	this.findParecerByIdHistoricoTramitacao = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/analistaTecnico/findParecerByIdHistoricoTramitacao/' + id);

	};

	this.concluir = function(parecer) {

		return request
			.post(config.BASE_URL() + 'parecer/analistaTecnico/concluir', parecer);

	};

	this.getParecerByNumeroProcesso = function(numeroProcesso) {

		return request
		.get(config.BASE_URL() + 'parecer/analistaTecnico?numeroProcesso=' + numeroProcesso);
	};
	
	this.findParecerByIdProcesso = function(id) {

		return request
				.get(config.BASE_URL() + 'parecer/analistaTecnico/findParecerByIdProcesso/' + id);

	};

	this.getUltimoParecerAnaliseTecnica = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/analistaTecnico/getUltimoParecerAnaliseTecnica/' + id);

	};

};

exports.services.ParecerAnalistaTecnicoService = ParecerAnalistaTecnicoService;
