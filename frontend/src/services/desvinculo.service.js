var DesvinculoService = function(request, config) {

	this.solicitarDesvinculo = function(params){
		return request
			.post(config.BASE_URL() + "analisesGeo/desvinculo/solicitarDesvinculo",params);
	};

	this.buscarDesvinculoPeloProcesso = function(idProcesso) {
		return request
			.get(config.BASE_URL() + "analisesGeo/desvinculo/buscarDesvinculoPeloProcesso/" + idProcesso);
	};

	this.buscarAnalistasGeo = function(idProcesso) {
		return request
			.get(config.BASE_URL() + "analisesGeo/desvinculo/buscarAnalistasGeo/" + idProcesso);
	};

	this.respondersolicitacaoDesvinculo = function(params) {
		return request
			.post(config.BASE_URL() + "analisesGeo/desvinculo/respondersolicitacaoDesvinculo",params);
	};
};

exports.services.DesvinculoService = DesvinculoService;
