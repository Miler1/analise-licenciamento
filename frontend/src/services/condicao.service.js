var CondicaoService = function(request, config) {

	this.getCondicoes = function() {

		return request.getWithCache(config.BASE_URL() + "condicoes");
	};

	this.findManejo = function() {

		return request.getWithCache(config.BASE_URL() + "condicoes/manejo");
	};

};

exports.services.CondicaoService = CondicaoService;