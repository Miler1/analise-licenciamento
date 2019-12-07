var InconsistenciaService = function(request,$window,config) {

	this.salvarInconsistenciaGeo = function(params){
		return request
			.post(config.BASE_URL() + 'inconsistencia/analiseGeo/salvarInconsistencia', params);
	};

	this.findInconsistenciaGeo = function(params){
		return request
			.post(config.BASE_URL() + 'inconsistencia/analisesGeo/findInconsistencia', params);
	};

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};

	this.excluirInconsistenciaGeo = function(id){
		return request
			.post(config.BASE_URL() + 'inconsistencia/analisesGeo/excluirInconsistencia/' +  id);
	};

	this.salvarInconsistenciaTecnica = function(params){
		return request
			.post(config.BASE_URL() + 'inconsistencia/analiseTecnica/salvarInconsistencia', params);
	};

	this.findInconsistenciaTecnica = function(params){
		return request
			.post(config.BASE_URL() + 'inconsistencia/analiseTecnica/findInconsistencia', params);
	};

	this.excluirInconsistenciaTecnica = function(inconsistenciaTecnica){
		return request
			.post(config.BASE_URL() + 'inconsistencia/analiseTecnica/excluirInconsistencia', inconsistenciaTecnica);
	};

};

exports.services.InconsistenciaService = InconsistenciaService;
