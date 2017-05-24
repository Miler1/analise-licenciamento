var CxEntCoordenadorJuridicoController = function($scope, config) {

	var cxEntCoordenadorJuridico = this;

	cxEntCoordenadorJuridico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntCoordenadorJuridico.atualizarPaginacao = atualizarPaginacao;
	cxEntCoordenadorJuridico.calcularDiasRestantes = calcularDiasRestantes;

	cxEntCoordenadorJuridico.processos = [];

	cxEntCoordenadorJuridico.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);

	function atualizarListaProcessos(processos) {

		cxEntCoordenadorJuridico.processos = processos;
	}

	function atualizarPaginacao(totalItens) {

		cxEntCoordenadorJuridico.paginacao.update(totalItens, cxEntCoordenadorJuridico.paginacao.paginaAtual);
	}

	function calcularDiasRestantes(stringDate){

		return moment(stringDate, 'DD/MM/yyyy').startOf('day')
			.diff(moment(Date.now()).startOf('day'), 'days');		
	}	
};

exports.controllers.CxEntCoordenadorJuridicoController = CxEntCoordenadorJuridicoController;