var DesvinculoService = function(request, config) {

	this.solicitarDesvinculo = function(params){
		return request
			.post(config.BASE_URL() + "analisesGeo/desvinculo/solicitarDesvinculo",params);
	};

	this.buscarDesvinculoPeloProcesso = function(idProcesso) {
		return request
			.get(config.BASE_URL() + "analisesGeo/desvinculo/buscarDesvinculoPeloProcesso/" + idProcesso);
	};


	this.responderSolicitacaoDesvinculo = function(params) {
		return request
			.post(config.BASE_URL() + "analisesGeo/desvinculo/responderSolicitacaoDesvinculo",params);
	};
};

exports.services.DesvinculoService = DesvinculoService;
