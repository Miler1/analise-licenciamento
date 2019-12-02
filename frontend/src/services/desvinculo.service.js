var DesvinculoService = function(request, config) {

	this.solicitarDesvinculoAnaliseGeo = function(params){
		return request
			.post(config.BASE_URL() + "desvinculo/analistaGeo/solicitarDesvinculo",params);
	};

	this.buscarDesvinculoPeloProcessoGeo = function(idProcesso) {
		return request
			.get(config.BASE_URL() + "desvinculo/analistaGeo/buscarDesvinculoPeloProcesso/" + idProcesso);
	};


	this.responderSolicitacaoDesvinculoAnaliseGeo = function(params) {
		return request
			.post(config.BASE_URL() + "desvinculo/analistaGeo/responderSolicitacaoDesvinculo",params);
	};
	
	this.solicitarDesvinculoAnaliseTecnica = function(params){
		return request
			.post(config.BASE_URL() + "desvinculo/analistaTecnico/solicitarDesvinculo",params);
	};

	this.buscarDesvinculoPeloProcessoTecnico = function(idProcesso) {
		return request
			.get(config.BASE_URL() + "desvinculo/analistaTecnico/buscarDesvinculoPeloProcesso/" + idProcesso);
	};

	this.responderSolicitacaoDesvinculoAnaliseTecnica = function(params) {
		return request
			.post(config.BASE_URL() + "desvinculo/analistaTecnico/responderSolicitacaoDesvinculo",params);
	};
};

exports.services.DesvinculoService = DesvinculoService;
