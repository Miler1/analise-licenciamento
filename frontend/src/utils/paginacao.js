var Paginacao = function(itensPorPagina){

	this.itensPorPagina = itensPorPagina;

	this.totalItens = 0;

	this.paginaAtual = 1;

	this.numPaginas = 1;

	this.inicioIntervalo = 0;

	this.fimIntervalo = itensPorPagina;

	this.update = function(totalItens, paginaAtual){

		this.numPaginas = Math.ceil(totalItens / this.itensPorPagina);

		this.totalItens = totalItens;

		this.paginaAtual = paginaAtual;

		this.inicioIntervalo = ((paginaAtual - 1) * this.itensPorPagina) + 1;

		this.fimIntervalo = Math.min(this.inicioIntervalo + itensPorPagina - 1, totalItens);

	};

};

exports.utils.Paginacao = Paginacao;
