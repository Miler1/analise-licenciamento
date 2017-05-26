var TipologiaService = function(request, config) {

	this.getTipologias = function(params) {

		return request.getWithCache(config.BASE_URL + "tipologias", params, null, false);
	};

};

exports.services.TipologiaService = TipologiaService;