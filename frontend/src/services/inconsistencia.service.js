var InconsistenciaService = function(request,$window,config) {

	this.salvarInconsistencia = function(params){
		return request
			.post(config.BASE_URL() + 'inconsistencia/salvar', params);
	};

	this.findInconsistencia = function(params){
		return request
			.post(config.BASE_URL() + 'inconsistencia/findInconsistencia', params);
	};

	this.findInconsistenciaById = function(id){
		return request
			.get(config.BASE_URL() + 'inconsistencia/findInconsistenciaById/' + id);
	};

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};

	this.excluirInconsistencia = function(id){
		return request
			.get(config.BASE_URL() + 'inconsistencia/excluirInconsistencia/' +  id);
	};
};

exports.services.InconsistenciaService = InconsistenciaService;
