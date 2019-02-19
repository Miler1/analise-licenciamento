var ListagemProcessoManejoController = function($scope, config, $rootScope, processoManejoService, mensagem, $location, $uibModal) {

	$rootScope.tituloPagina = 'CONSULTAR PROCESSO MANEJO DIGITAL';

	var listagemProcessoManejo = this;

	listagemProcessoManejo.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagemProcessoManejo.processosManejo = [];
	listagemProcessoManejo.atualizarPaginacao = atualizarPaginacao;
	listagemProcessoManejo.atualizarListaProcessos = atualizarListaProcessos;
	listagemProcessoManejo.onPaginaAlterada = onPaginaAlterada;
	listagemProcessoManejo.cadastrarProcessoManejo = cadastrarProcessoManejo;

	listagemProcessoManejo.permissaoCadastrar = LICENCIAMENTO_CONFIG.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.listaPermissoes.indexOf('CADASTRAR_PROCESSO_MANEJO') !== -1;
	listagemProcessoManejo.permissaoAnalisar = LICENCIAMENTO_CONFIG.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.listaPermissoes.indexOf('ANALISAR_PROCESSO_MANEJO') !== -1;
	listagemProcessoManejo.permissaoVisualizar = LICENCIAMENTO_CONFIG.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.listaPermissoes.indexOf('VISUALIZAR_PROCESSO_MANEJO') !== -1;

	listagemProcessoManejo.iniciarAnaliseShape = function (processo) {

		$location.path('/analise-manejo/' + processo.id + '/analise-geo');
	};

	listagemProcessoManejo.visualizarProcesso = function (processo) {

		return processoManejoService.visualizarProcessoManejo(processo);
	};

	listagemProcessoManejo.status = {
		'17': 'Aguardando análise técnica',
		'18': 'Em análise técnica',
		'19': 'Apto',
		'20': 'Inapto',
		'22': 'Aguardando análise de shape',
		'23': 'Em análise de shape'
	};

	listagemProcessoManejo.downloadPdfAnaliseTecnica = function (processo) {

		processoManejoService.downloadPdfAnaliseTecnica(processo)
			.then(
				function(data, status, headers){

					var a = document.createElement('a');
					a.href = URL.createObjectURL(data.data.response.blob);
					a.download = data.data.response.fileName ? data.data.response.fileName : 'previa_notificacao_analise_juridica.pdf';
					a.click();
				},
				function(error){

					mensagem.error(error.data.texto);
				}
			);
	};

	listagemProcessoManejo.continuarAnalise = function(processo) {

		processoManejoService.getProcesso(processo.id)
			.then(function (response) {

				$location.path('/analise-manejo/' + response.data.analiseTecnica.id + '/analise-tecnica');
			})
			.catch(function (response) {

				if (!!response.data.texto)
					mensagem.warning(response.data.texto);

				else
					mensagem.error("Ocorreu um erro obter dados do processo.");
			});
	};


	listagemProcessoManejo.indeferir = function(processo) {

		var modalInstance = $uibModal.open({
			controller: 'modalIndeferirController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			keyboard: false,
			templateUrl: './features/analiseManejo/analiseTecnica/modal-indeferir.html'
		});

		modalInstance.result.then(function (dados) {

			if (dados && dados.justificativaIndeferimento) {

				processo.justificativaIndeferimento = dados.justificativaIndeferimento;

				processoManejoService.indeferir(processo).then(function (response) {

					mensagem.success(response.data.texto);
					onPaginaAlterada();
				})
				.catch(function (response) {

					if (response.data.texto)
						mensagem.warning(response.data.texto);

					else
						mensagem.error("Ocorreu um erro ao salvar a observacao.");
				});
			}

		});
	};


	function onPaginaAlterada(){

		$scope.$broadcast('pesquisarProcessosManejo');
	}

	function atualizarPaginacao(totalItens, paginaAtual) {

		listagemProcessoManejo.paginacao.update(totalItens, paginaAtual);
	}

	function atualizarListaProcessos(processos) {

		listagemProcessoManejo.processosManejo = processos;
	}

	function cadastrarProcessoManejo() {

		$location.path('/analise-manejo/cadastro');

	}

	listagemProcessoManejo.iniciarAnaliseTecnica = function(processoManejo) {

		processoManejoService.iniciarAnalise(processoManejo)
			.then(function (response) {

				$location.path('/analise-manejo/' + response.data.analiseTecnica.id + '/analise-tecnica');
			})
			.catch(function (response) {

				if (!!response.data.texto)
					mensagem.warning(response.data.texto);

				else
					mensagem.error("Ocorreu um erro ao iniciar a análise técnica do processo.");
			});
	};
};

exports.controllers.ListagemProcessoManejoController = ListagemProcessoManejoController;