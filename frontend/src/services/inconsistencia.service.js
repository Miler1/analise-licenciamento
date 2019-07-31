var InconsistenciaService = function(request,$window,config) {

	this.salvarInconsistencia = function(params){
		return request
			.post(config.BASE_URL() + 'inconsistencia/salvarInconsistencia', params);
	};

	this.findInconsistencia = function(params){
		return request
			.post(config.BASE_URL() + 'inconsistencia/findInconsistencia', params);
	};

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');
	};
};

exports.services.InconsistenciaService = InconsistenciaService;
