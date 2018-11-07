var AtividadeManejoService = function(request, config) {

	this.findAll = function() {

		return request.get(config.BASE_URL() + "atividadesManejo");
	};

	this.findByTipologia = function(tipologia) {

		return request.get(config.BASE_URL() + "atividadesManejo/tipologia/" + tipologia);
	}

};

exports.services.AtividadeManejoService = AtividadeManejoService;