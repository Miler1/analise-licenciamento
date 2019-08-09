var InconsistenciaService = function(request,$window,config) {

	this.salvarInconsistencia = function(params){
		return request
			.post(config.BASE_URL() + 'analisesGeo/inconsistencia/salvarInconsistencia', params);
	};

	this.findInconsistencia = function(params){
		return request
			.post(config.BASE_URL() + 'analisesGeo/inconsistencia/findInconsistencia', params);
	};

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};

	this.excluirInconsistencia = function(id){
		return request
			.post(config.BASE_URL() + 'analisesGeo/inconsistencia/excluirInconsistencia/' +  id);
	};
};

exports.services.InconsistenciaService = InconsistenciaService;
