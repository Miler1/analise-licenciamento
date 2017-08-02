var AnalistaService = function(request, config) {


    this.getAnalistasTecnicos = function() {

		return request
			.get(config.BASE_URL() + "analistas");
	};

    	this.getAnalistasTecnicosByProcesso = function(idProcesso) {

		return request
			.get(config.BASE_URL() + "analistas/" + idProcesso);
	};

    this.getAnalistasTecnicosByPerfil = function() {

		return request
			.get(config.BASE_URL() + "analistas/perfil");
	};

    this.vincularAnaliseAnalistaTecnico = function(idAnalista, justificativaCoordenador, idsProcessos) {

        return request
			.postAsUrlEncoded(config.BASE_URL() + "analistas/vincular",{
                idUsuario: idAnalista,
                justificativaCoordenador: justificativaCoordenador,
                idsProcesso: idsProcessos
            });
    };
};

exports.services.AnalistaService = AnalistaService;