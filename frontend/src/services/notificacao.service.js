var NotificacaoService = function(request, config, $window) {

	this.downloadNotificacao = function(idTramitacao) {

		$window.open(config.BASE_URL() + 'notificacoes/' + idTramitacao, '_blank');

	};

	this.findByIdProcesso = function(idProcesso) {

		return request
			.get(config.BASE_URL() + 'notificacoes/findByIdProcesso/' + idProcesso);

	};

	this.findByIdParecer = function(id) {

		return request
			.get(config.BASE_URL() + 'notificacoes/findByIdParecer/' + id);

	};

	this.findByIdParecerTecnico = function(id) {

		return request
			.get(config.BASE_URL() + 'notificacoes/findByIdParecerTecnico/' + id);

	};

};

exports.services.NotificacaoService = NotificacaoService;
