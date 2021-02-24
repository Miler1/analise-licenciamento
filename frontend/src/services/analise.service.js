var AnaliseService = function(request, config) {

	this.getAnalise = function(idAnalise) {

		return request
                .get(config.BASE_URL() + 'analises/' + idAnalise);
	};

	this.iniciar = function(idAnalise) {

		return request
                	.post(config.BASE_URL() + 'analises/iniciar/'+ idAnalise);
	};

	this.iniciarAnaliseSecretario = function(idAnalise) {

		return request
                	.post(config.BASE_URL() + 'analises/iniciarAnaliseSecretario/'+ idAnalise);
	};

};

exports.services.AnaliseService = AnaliseService;