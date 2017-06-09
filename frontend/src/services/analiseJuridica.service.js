var AnaliseJuridicaService = function(request, config) {

	this.getDocumentosAnalisados = function(idAnaliseJuridica) {

		return request
                .get(config.BASE_URL() + 'analisesJuridicas/' + idAnaliseJuridica + '/documentosAnalisados');
	};

	this.salvar = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesJuridicas', analise);
	};

	this.finalizar = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesJuridicas/finalizar', analise);
	};
};

exports.services.AnaliseJuridicaService = AnaliseJuridicaService;