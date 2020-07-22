var VisualizacaoNotificacaoController = function ($rootScope,$uibModalInstance, processo, mensagem, documentoService, notificacaoService) {

	var modalCtrl = this;

	modalCtrl.notificacoes = [];
	modalCtrl.baixarDocumento = baixarDocumento;

	modalCtrl.usuarioLogadoCodigoPerfil = $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo;

	if (processo.idProcesso) {

		if($rootScope.tituloPagina === app.TITULOS_PAGINA.CONSULTAR_PROCESSO_PROTOCOLO){

			notificacaoService.findByIdProcesso(processo.idProcesso).then(function(response){

				modalCtrl.notificacoes = response.data;
				prepararDadosParaExibicao();

			}).catch(function(){

				mensagem.error("Ocorreu um erro ao buscar dados das notificações.");

			});

		}else{
			notificacaoService.findNotificacoesByIdProcesso(processo.idProcesso).then(function(response){

				modalCtrl.notificacoes = response.data;
				prepararDadosParaExibicao();

			}).catch(function(){

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
