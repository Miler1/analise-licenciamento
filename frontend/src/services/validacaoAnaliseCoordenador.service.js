var ValidacaoAnaliseCoordenadorService = function(request,config) {

	this.concluir = function(params){

		return request
			.post(config.BASE_URL() + 'parecer/coordenador/concluirParecerCoordenador', params);
	};

	this.concluirParecerTecnico = function(params){

		return request
			.post(config.BASE_URL() + 'parecer/coordenador/concluirParecerTecnicoCoordenador', params);
	};

};

exports.services.ValidacaoAnaliseCoordenadorService = ValidacaoAnaliseCoordenadorService;