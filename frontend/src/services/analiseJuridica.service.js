var AnaliseJuridicaService = function(request, config) {

	this.getAnaliseJuridica =function(idAnaliseJuridica) {

		return request
                .get(config.BASE_URL() + 'analisesJuridicas/' + idAnaliseJuridica);
	};
	
	this.getDocumentosAnalisados = function(idAnaliseJuridica) {

		return request
                .get(config.BASE_URL() + 'analisesJuridicas/' + idAnaliseJuridica + '/documentosAnalisados');
	};

	this.salvar = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesJuridicas', analise);
	};

	this.concluir = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesJuridicas/concluir', analise);
	};
};

exports.services.AnaliseJuridicaService = AnaliseJuridicaService;