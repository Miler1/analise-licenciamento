var ParecerOrgaoService = function(request,config) {

	this.enviar = function(params){
		return request
			.post(config.BASE_URL() + 'analisesGeo/comunicado/salvarParecerOrgao', params);
	};


};

exports.services.ParecerOrgaoService = ParecerOrgaoService;
