var CondicaoService = function(request, config) {

	this.getCondicoes = function() {

		return request.getWithCache(config.BASE_URL() + "condicoes");
	};

};

exports.services.CondicaoService = CondicaoService;