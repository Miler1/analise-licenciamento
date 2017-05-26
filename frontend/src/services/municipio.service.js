var MunicipioService = function(request, config) {

	this.getMunicipiosByUf = function(uf) {

		return request.getWithCache(config.BASE_URL() + "estados/" + uf + "/municipios", null, null, false);
	};
};

exports.services.MunicipioService = MunicipioService;