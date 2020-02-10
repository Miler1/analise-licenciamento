var VisualizacaoNotificacaoController = function ($rootScope,$uibModalInstance, processo, mensagem, documentoService, notificacaoService) {

	var modalCtrl = this;

	modalCtrl.notificacoes = [];
	modalCtrl.baixarDocumento = baixarDocumento;

	modalCtrl.usuarioLogadoCodigoPerfil = $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo;

	if (processo.idProcesso) {
		if (modalCtrl.usuarioLogadoCodigoPerfil === app.utils.Perfis.ANALISTA_GEO) {

			notificacaoService.findByIdProcesso(processo.idProcesso)
				.then(function(response){

					modalCtrl.notificacoes = response.data;
					prepararDadosParaExibicao();

				})
				.catch(function(){
					mensagem.error("Ocorreu um erro ao buscar dados das notificações.");
				});

		} else if (modalCtrl.usuarioLogadoCodigoPerfil === app.utils.Perfis.ANALISTA_TECNICO) {

			notificacaoService.findByIdProcessoTecnico(processo.idProcesso)
				.then(function(response){

					modalCtrl.notificacoes = response.data;
					prepararDadosParaExibicao();

				})
				.catch(function(){
					mensagem.error("Ocorreu um erro ao buscar dados das notificações.");
				});
		}
	}

	modalCtrl.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

	function prepararDadosParaExibicao() {
		_.forEach(modalCtrl.notificacoes, function(notificacao) {
			notificacao.retificacaoSolicitacaoComGeo = !!notificacao.retificacaoSolicitacaoComGeo;
		});
	}

	function baixarDocumento(idAnexo) {

		documentoService.downloadById(idAnexo);
	}

};

exports.controllers.VisualizacaoNotificacaoController = VisualizacaoNotificacaoController;
