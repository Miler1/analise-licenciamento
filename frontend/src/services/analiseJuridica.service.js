var AnaliseJuridicaService = function(request, config) {

	this.getDocumentosAnalisados = function(idAnaliseJuridica) {

		return request
                .get(config.BASE_URL() + 'analisesJuridicas/' + idAnaliseJuridica + '/documentosAnalisados');
	};
};

exports.services.AnaliseJuridicaService = AnaliseJuridicaService;