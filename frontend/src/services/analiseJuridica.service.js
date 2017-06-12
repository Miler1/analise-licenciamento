var AnaliseJuridicaService = function(request, config) {

	this.getDocumentosAnalisados = function(idAnaliseJuridica) {

		return request
            .get(config.BASE_URL() + 'analisesJuridicas/' + idAnaliseJuridica + '/documentosAnalisados');
	};

	this.getAnaliseJuridicaById = function(idAnaliseJuridica) {

		return request
            .get(config.BASE_URL() + 'analisesJuridicas/' + idAnaliseJuridica);
	};	
};

exports.services.AnaliseJuridicaService = AnaliseJuridicaService;