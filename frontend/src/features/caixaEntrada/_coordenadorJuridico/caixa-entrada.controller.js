var CxEntCoordenadorJuridicoController = function($scope, config) {

	var cxEntCoordenadorJuridico = this;

	cxEntCoordenadorJuridico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntCoordenadorJuridico.atualizarPaginacao = atualizarPaginacao;

	cxEntCoordenadorJuridico.processos = [];

	cxEntCoordenadorJuridico.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);

	function atualizarListaProcessos(processos) {

		cxEntCoordenadorJuridico.processos = processos;
	}

	function atualizarPaginacao(totalItens) {

		cxEntCoordenadorJuridico.paginacao.update(totalItens, cxEntCoordenadorJuridico.paginacao.paginaAtual);
	}	
};

exports.controllers.CxEntCoordenadorJuridicoController = CxEntCoordenadorJuridicoController;