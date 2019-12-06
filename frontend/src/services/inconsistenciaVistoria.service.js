var InconsistenciaVistoriaService = function(request, config) {

	this.salvar = function(inconsistenciaVistoria) {

		return request.post(config.BASE_URL() + "inconsistenciaVistoria/salvar", inconsistenciaVistoria);

	};

	this.deletar = function(idInconsistenciaVistoria) {

		return request.get(config.BASE_URL() + "inconsistenciaVistoria/deletar/" + idInconsistenciaVistoria);

	};

};

exports.services.InconsistenciaVistoriaService = InconsistenciaVistoriaService;
