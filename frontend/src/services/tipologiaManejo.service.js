var TipologiaManejoService = function(request, config) {

	this.findAll = function() {

		return request.get(config.BASE_URL() + "tipologiasManejo");
	};

};

exports.services.TipologiaManejoService = TipologiaManejoService;