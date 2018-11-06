var AtividadeManejoService = function(request, config) {

	this.findAll = function() {

		return request.get(config.BASE_URL() + "atividadesManejo");
	};

};

exports.services.AtividadeManejoService = AtividadeManejoService;