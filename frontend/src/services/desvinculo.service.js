var DesvinculoService = function(request, config) {

	this.solicitarDesvinculo = function(processo, justificativa){
		return request
			.post(config.BASE_URL() + "analisesGeo/desvinculo/"+processo+"/solicitarDesvinculo/"+justificativa);
	};	
};

exports.services.DesvinculoService = DesvinculoService;
