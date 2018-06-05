var NotificacaoService = function(request, config, $window) {

	this.downloadNotificacao = function(idTramitacao) {

        $window.open(config.BASE_URL() + 'notificacoes/' + idTramitacao + '/download', '_blank');

	};

};

exports.services.NotificacaoService = NotificacaoService;