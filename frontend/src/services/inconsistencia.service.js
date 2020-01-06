var InconsistenciaService = function(request,$window,config) {

	this.salvarInconsistenciaGeo = function(params){
		return request
			.post(config.BASE_URL() + 'inconsistencia/analiseGeo/salvarInconsistencia', params);
	};

	this.findInconsistenciaGeo = function(params){
		return request
			.post(config.BASE_URL() + 'inconsistencia/analiseGeo/findInconsistencia', params);

	};

	this.findInconsistenciaById = function(id){
		return request
			.get(config.BASE_URL() + 'inconsistencia/findInconsistenciaById/' + id);
	};

	this.download = function(idDocumento) {

        $window.open(config.BASE_URL() + 'documentos/' + idDocumento + '/download', '_blank');

	};

	this.excluirInconsistenciaGeo = function(id){
		return request
			.post(config.BASE_URL() + 'inconsistencia/analiseGeo/excluirInconsistencia/' + id);
	};

	this.salvarInconsistenciaTecnica = function(params){
		return request
			.post(config.BASE_URL() + 'inconsistencia/analiseTecnica/salvarInconsistencia', params);
	};

	this.findInconsistenciaTecnica = function(id){
		return request
			.post(config.BASE_URL() + 'inconsistencia/analiseTecnica/findInconsistencia/' + id);
	};

	this.excluirInconsistenciaTecnica = function(inconsistenciaTecnica){
		return request
			.post(config.BASE_URL() + 'inconsistencia/analiseTecnica/excluirInconsistencia', inconsistenciaTecnica);
	};

	this.findInconsistenciaByAnaliseTecnica = function(idAnalise) {
		return request
			.get(config.BASE_URL() + 'inconsistencia/analiseTecnica/findInconsistenciaByAnaliseTecnica/' + idAnalise);
	};

};

exports.services.InconsistenciaService = InconsistenciaService;
