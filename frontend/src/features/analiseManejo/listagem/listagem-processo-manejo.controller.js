var ListagemProcessoManejoController = function($scope, config, $rootScope, processoManejoService, mensagem, $location) {

	$rootScope.tituloPagina = 'CONSULTAR PROCESSO MANEJO DIGITAL';

	var listagemProcessoManejo = this;

	listagemProcessoManejo.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagemProcessoManejo.atualizarPaginacao = atualizarPaginacao;
	listagemProcessoManejo.atualizarListaProcessos = atualizarListaProcessos;
	listagemProcessoManejo.onPaginaAlterada = onPaginaAlterada;
	listagemProcessoManejo.cadastrarProcessoManejo = cadastrarProcessoManejo;

	listagemProcessoManejo.permissaoCadastrar = LICENCIAMENTO_CONFIG.usuarioSessao.perfilSelecionado.listaPermissoes.indexOf('CADASTRAR_PROCESSO_MANEJO') !== -1;
	listagemProcessoManejo.permissaoAnalisar = LICENCIAMENTO_CONFIG.usuarioSessao.perfilSelecionado.listaPermissoes.indexOf('ANALISAR_PROCESSO_MANEJO') !== -1;
	listagemProcessoManejo.permissaoVisualizar = LICENCIAMENTO_CONFIG.usuarioSessao.perfilSelecionado.listaPermissoes.indexOf('VISUALIZAR_PROCESSO_MANEJO') !== -1;

	listagemProcessoManejo.iniciarAnalise = function (processo) {

		$location.path('/analise-manejo/' + processo.id + '/analise-geo');

	};

	listagemProcessoManejo.visualizarProcesso = null;
	listagemProcessoManejo.processosManejo = [];

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
};

exports.controllers.ListagemProcessoManejoController = ListagemProcessoManejoController;
