var ParecerPresidenteService = function(request, config) {

    this.concluirParecerPresidente = function(params){

		return request
			.post(config.BASE_URL() + 'parecer/presidente/concluirParecerPresidente', params);
	};

	this.findParecerByIdHistoricoTramitacao = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/presidente/findParecerByIdHistoricoTramitacao/' + id);

	};   

};

exports.services.ParecerPresidenteService = ParecerPresidenteService;
