var InconsistenciaService = function(request,$window,config) {

	this.salvarInconsistencia = function(params){
		return request
			.post(config.BASE_URL() + '/analisesGeo/salvarInconsistencia', params);
	};

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};
};

exports.services.InconsistenciaService = InconsistenciaService;
