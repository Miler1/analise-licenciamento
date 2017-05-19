var ProcessoService = function(request, config) {

	this.getProcessoslist = function(filtro, paginaAtual, itensPorPagina) {

		return request
			.get(config.BASE_URL() + "processos", {
				filtro: filtro,
				numeroPagina: paginaAtual,
				qtdItensPorPagina: itensPorPagina
			});
	};
};

exports.services.ProcessoService = ProcessoService;