var CxEntCoordenadorJuridicoController = function($scope, config) {

	var cxEntCoordenadorJuridico = this;

	cxEntCoordenadorJuridico.atualizarListaProcessos = atualizarListaProcessos;
	cxEntCoordenadorJuridico.atualizarPaginacao = atualizarPaginacao;
	cxEntCoordenadorJuridico.calcularDiasRestantes = calcularDiasRestantes;	
	cxEntCoordenadorJuridico.getDiasRestantes = getDiasRestantes;
	cxEntCoordenadorJuridico.selecionarTodosProcessos = selecionarTodosProcessos;
	cxEntCoordenadorJuridico.isPrazoMinimoAvisoAnalise = isPrazoMinimoAvisoAnalise;

	cxEntCoordenadorJuridico.processos = [];
	cxEntCoordenadorJuridico.condicaoTramitacao = app.utils.CondicaoTramitacao.AGUARDANDO_VINCULACAO_JURIDICA;
	cxEntCoordenadorJuridico.paginacao = new app.utils.Paginacao(config.QTDE_ITENS_POR_PAGINA);
	cxEntCoordenadorJuridico.PrazoMinimoAvisoAnalise = app.utils.PrazoMinimoAvisoAnalise;

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

	function getDiasRestantes(dataVencimento){

		var diasRestantes = calcularDiasRestantes(dataVencimento);

		return diasRestantes >=0 ? diasRestantes : Math.abs(diasRestantes) + ' dia(s) atraso';
	}

	function selecionarTodosProcessos() {

		_.each(cxEntCoordenadorJuridico.processos, function(processo){

			processo.selecionado = cxEntCoordenadorJuridico.todosProcessosSelecionados;
		});
	}

	function isPrazoMinimoAvisoAnalise(dataVencimento, prazoMinimo) {

		return calcularDiasRestantes(dataVencimento) <= prazoMinimo; 
	}	
};

exports.controllers.CxEntCoordenadorJuridicoController = CxEntCoordenadorJuridicoController;