var ParecerOrgaoService = function(request,config) {

	this.enviar = function(params){

		return request
			.post(config.BASE_URL() + 'analisesGeo/comunicado/salvarParecerOrgao', params);
	};
	this.findComunicado = function(idComunicado){

		return request
			.get(config.BASE_URL() + 'analisesGeo/comunicado/findComunicado/'+ idComunicado);
	};


};

exports.services.ParecerOrgaoService = ParecerOrgaoService;
