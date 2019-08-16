var TiposSobreposicaoService = function(request, config) {

	this.getTiposSobreposicao = function() {

		return request.getWithCache(config.BASE_URL() + "tiposSobreposicao");
	};

};

exports.services.TiposSobreposicaoService = TiposSobreposicaoService;