var InconsistenciaService = function(request) {

	this.salvarInconsistencia = function(params){
		return request
			.post("/salvarInconsistencia", params);
	};
};

exports.services.InconsistenciaService = InconsistenciaService;
