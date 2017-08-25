var AnaliseService = function(request, config) {

	this.getAnalise = function(idAnalise) {

		return request
                .get(config.BASE_URL() + 'analises/' + idAnalise);
	};

};

exports.services.AnaliseService = AnaliseService;