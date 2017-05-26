var AtividadeService = function(request, config) {

	this.getAtividades = function(params) {

		return request.getWithCache(config.BASE_URL + "atividades", params, null, false);
	};
};

exports.services.AtividadeService = AtividadeService;