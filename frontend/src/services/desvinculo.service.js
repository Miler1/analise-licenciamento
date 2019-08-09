var DesvinculoService = function(request, config) {

	this.solicitarDesvinculo = function(params){
		return request
			.post(config.BASE_URL() + "analisesGeo/desvinculo/solicitarDesvinculo",params);
	};	
};

exports.services.DesvinculoService = DesvinculoService;
