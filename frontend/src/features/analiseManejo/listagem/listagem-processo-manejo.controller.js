var ListagemProcessoManejoController = function($scope, config, $rootScope, processoManejoService, mensagem, $location) {

	$rootScope.tituloPagina = 'CONSULTAR PROCESSO MANEJO DIGITAL';

	var listagemProcessoManejo = this;

	listagemProcessoManejo.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagemProcessoManejo.processosManejo = [];
	listagemProcessoManejo.atualizarPaginacao = atualizarPaginacao;
	listagemProcessoManejo.atualizarListaProcessos = atualizarListaProcessos;
	listagemProcessoManejo.onPaginaAlterada = onPaginaAlterada;
	listagemProcessoManejo.cadastrarProcessoManejo = cadastrarProcessoManejo;

	listagemProcessoManejo.permissaoCadastrar = LICENCIAMENTO_CONFIG.usuarioSessao.perfilSelecionado.listaPermissoes.indexOf('CADASTRAR_PROCESSO_MANEJO') !== -1;
	listagemProcessoManejo.permissaoAnalisar = LICENCIAMENTO_CONFIG.usuarioSessao.perfilSelecionado.listaPermissoes.indexOf('ANALISAR_PROCESSO_MANEJO') !== -1;
	listagemProcessoManejo.permissaoVisualizar = LICENCIAMENTO_CONFIG.usuarioSessao.perfilSelecionado.listaPermissoes.indexOf('VISUALIZAR_PROCESSO_MANEJO') !== -1;

	listagemProcessoManejo.iniciarAnaliseShape = function (processo) {

		$location.path('/analise-manejo/' + processo.id + '/analise-geo');
	};

	listagemProcessoManejo.visualizarProcesso = function (processo) {

		return processoManejoService.visualizarProcessoManejo(processo);
	};

	listagemProcessoManejo.status = {
		'17': 'Aguardando análise técnica',
		'18': 'Em análise técnica',
		'19': 'Deferido',
		'20': 'Indeferido',
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

	listagemProcessoManejo.init = function(processoManejo) {

		processoManejoService.getProcesso(processoManejo.id)
			.then(function (response) {

				analiseGeoManejo.processo = response.data;
				analiseGeoManejo.geometria = false;

				if (analiseGeoManejo.processo.nomeCondicao == 'Manejo digital em análise técnica' ) {

					$location.path('/analise-manejo/' + analiseGeoManejo.processo.analiseManejo.id + '/analise-tecnica');
					return;

				// Como ainda não existe a integração com o SIMLAM, esse bloco é necessário para manter a integridade do sistema
				} else if (analiseGeoManejo.processo.nomeCondicao == 'Manejo digital deferido' || analiseGeoManejo.processo.nomeCondicao == 'Manejo digital indeferido') {

					mensagem.warning("Processo já análisado.");
					$location.path('/analise-manejo');
					return;
				}
			})
			.catch(function (response) {

				if (!!response.data.texto)
					mensagem.warning(response.data.texto);

				else
					mensagem.error("Ocorreu um erro obter dados do processo.");
			});
	};
};

exports.controllers.ListagemProcessoManejoController = ListagemProcessoManejoController;
