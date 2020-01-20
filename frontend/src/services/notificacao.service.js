var NotificacaoService = function(request, config, $window) {

	this.downloadNotificacao = function(idTramitacao) {

		$window.open(config.BASE_URL() + 'notificacoes/' + idTramitacao, '_blank');

	};

	this.findByIdProcesso = function(idProcesso) {

		return request
			.get(config.BASE_URL() + 'notificacoes/findByIdProcesso/' + idProcesso);

	};

	this.findByIdProcessoTecnico = function(idProcesso) {

		return request
			.get(config.BASE_URL() + 'notificacoes/findByIdProcessoTecnico/' + idProcesso);

	};

};

exports.services.NotificacaoService = NotificacaoService;