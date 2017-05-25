var TipologiaService = function(request, config) {

	this.getTipologias = function(params) {

		return request.getWithCache(config.BASE_URL() + "tipologias", params);
	};

};

exports.services.TipologiaService = TipologiaService;