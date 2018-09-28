var ListagemProcessoManejoController = function($scope, config, $rootScope) {

	$rootScope.tituloPagina = 'CONSULTAR PROCESSO MANEJO FLORESTAL';

	var listagemProcessoManejo = this;

	listagemProcessoManejo.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	listagemProcessoManejo.atualizarPaginacao = atualizarPaginacao;
	listagemProcessoManejo.atualizarListaProcessos = atualizarListaProcessos;
	listagemProcessoManejo.processosManejo = [];

	function atualizarPaginacao(totalItens, paginaAtual) {

		listagemProcessoManejo.paginacao.update(totalItens, paginaAtual);
	}

	function atualizarListaProcessos(processos) {

		listagemProcessoManejo.processosManejo = processos;
	}

};

exports.controllers.ListagemProcessoManejoController = ListagemProcessoManejoController;