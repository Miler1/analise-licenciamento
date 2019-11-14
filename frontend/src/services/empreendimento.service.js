var EmpreendimentoService =  function (request, config) {

	this.getDadosGeoEmpreendimento = function(cpfCnpj){

		return request
			.post(config.BASE_URL() + 'empreendimento/buscaDadosGeo/' + cpfCnpj);

	};

};

exports.services.EmpreendimentoService = EmpreendimentoService;