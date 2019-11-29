var DesvinculoService = function(request, config) {

	this.solicitarDesvinculoAnaliseGeo = function(params){
		return request
			.post(config.BASE_URL() + "analisesGeo/desvinculo/solicitarDesvinculo",params);
	};

	this.buscarDesvinculoPeloProcessoGeo = function(idProcesso) {
		return request
			.get(config.BASE_URL() + "analisesGeo/desvinculo/buscarDesvinculoPeloProcesso/" + idProcesso);
	};


	this.responderSolicitacaoDesvinculoAnaliseGeo = function(params) {
		return request
			.post(config.BASE_URL() + "analisesGeo/desvinculo/responderSolicitacaoDesvinculo",params);
	};
	
	this.solicitarDesvinculoAnaliseTecnica = function(params){
		return request
			.post(config.BASE_URL() + "analisesTecnica/desvinculo/solicitarDesvinculo",params);
	};

	this.buscarDesvinculoPeloProcessoTecnico = function(idProcesso) {
		return request
			.get(config.BASE_URL() + "analisesTecnica/desvinculo/buscarDesvinculoPeloProcesso/" + idProcesso);
	};

	this.responderSolicitacaoDesvinculoAnaliseTecnica = function(params) {
		return request
			.post(config.BASE_URL() + "analisesTecnica/desvinculo/responderSolicitacaoDesvinculo",params);
	};
};

exports.services.DesvinculoService = DesvinculoService;
