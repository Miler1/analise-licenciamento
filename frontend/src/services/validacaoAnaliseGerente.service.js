var ValidacaoAnaliseGerenteService = function(request,config) {

	this.concluir = function(params){

		return request
			.post(config.BASE_URL() + 'parecer/gerente/concluirParecerGerente', params);
	};

};

exports.services.ValidacaoAnaliseGerenteService = ValidacaoAnaliseGerenteService;
