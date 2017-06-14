var AnaliseJuridicaService = function(request, config) {

	this.getAnaliseJuridica = function(idAnaliseJuridica) {

		return request
                .get(config.BASE_URL() + 'analisesJuridicas/' + idAnaliseJuridica);
	};

	this.getParecerByNumeroProcesso = function(numeroProcesso) {

		// envia numero do processo 'encodado' por causa da barra
		return request
                .get(config.BASE_URL() + 'analisesJuridicas/' + encodeURIComponent(numeroProcesso) + '/parecer');
	};

	this.iniciar = function(analise) {

		return request
                .post(config.BASE_URL() + 'analisesJuridicas/iniciar', analise);
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

	this.validarParecer = function(analise) {

		return request
				.post(config.BASE_URL() + 'analisesJuridicas/validarParecer', analise);
	};	
};

exports.services.AnaliseJuridicaService = AnaliseJuridicaService;