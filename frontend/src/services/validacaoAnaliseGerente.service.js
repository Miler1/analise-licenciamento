var ValidacaoAnaliseGerenteService = function(request,config) {

	this.concluir = function(params){

		return request
			.post(config.BASE_URL() + 'parecer/gerente/concluirParecerGerente', params);
	};

	this.concluirParecerTecnico = function(params){

		return request
			.post(config.BASE_URL() + 'parecer/gerente/concluirParecerTecnicoGerente', params);
	};

};

exports.services.ValidacaoAnaliseGerenteService = ValidacaoAnaliseGerenteService;
