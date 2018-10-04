var ObservacaoService = function(request, config) {

	this.save = function(observacao) {

		return request
			.post(config.BASE_URL() + "observacao", observacao);
	};

	this.delete = function(id) {

		return request
		.delete(config.BASE_URL() + "observacao/" + id);
	};
};

exports.services.ObservacaoService = ObservacaoService;