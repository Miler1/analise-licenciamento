var ListagemProcessoManejoController = function($scope, config, $rootScope) {

	$rootScope.tituloPagina = 'CONSULTAR PROCESSO MANEJO FLORESTAL';

	var listagemProcessoManejo = this;

	listagemProcessoManejo.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagemProcessoManejo.atualizarPaginacao = atualizarPaginacao;
	listagemProcessoManejo.atualizarListaProcessos = atualizarListaProcessos;
	listagemProcessoManejo.iniciarAnalise = null;
	listagemProcessoManejo.visualizarProcesso = null;
	listagemProcessoManejo.processosManejo = [];

	function atualizarPaginacao(totalItens, paginaAtual) {

		listagemProcessoManejo.paginacao.update(totalItens, paginaAtual);
	}

	function atualizarListaProcessos(processos) {

		listagemProcessoManejo.processosManejo = processos;
	}

	this.$postLink = function(){

		listagemProcessoManejo.processosManejo = [

			{
				processo: "1",
				cpfCnpj: "1",
				empreendimento: "1",
				municipioEmpreendimento: "1",
				siglaEstadoEmpreendimento: "1",
				licencas: "1",
			},
			{
				processo: "2",
				cpfCnpj: "2",
				empreendimento: "2",
				municipioEmpreendimento: "2",
				siglaEstadoEmpreendimento: "2",
				licencas: "2",
			}
		];
	};

};

exports.controllers.ListagemProcessoManejoController = ListagemProcessoManejoController;