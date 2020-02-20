var ParecerPresidenteService = function(request, config) {

    this.concluirParecerPresidente = function(params){

		return request
			.post(config.BASE_URL() + 'parecer/presidente/concluirParecerPresidente', params);
	};

};

exports.services.ParecerPresidenteService = ParecerPresidenteService;
