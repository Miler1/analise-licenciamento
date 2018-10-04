var ObservacaoService = function(request, config, $uibModal) {

	this.save = function(observacao) {

		return request
			.post(config.BASE_URL() + "observacao", observacao);
	};
};

exports.services.ObservacaoService = ObservacaoService;