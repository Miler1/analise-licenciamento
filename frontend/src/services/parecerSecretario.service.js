var ParecerSecretarioService = function(request, config) {

    this.concluirParecerSecretario = function(params){

		return request
			.post(config.BASE_URL() + 'parecer/secretario/concluirParecerSecretario', params);
	};

	this.findParecerByIdHistoricoTramitacao = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/secretario/findParecerByIdHistoricoTramitacao/' + id);

	};   

};

exports.services.ParecerSecretarioService = ParecerSecretarioService;
