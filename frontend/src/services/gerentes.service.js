var GerenteService = function(request, config) {

    this.getGerentesTecnicos = function() {

		return request
			.get(config.BASE_URL() + "gerentes");
	};

    this.getGerentesTecnicosByIdProcesso = function(idProcesso) {

		return request
			.get(config.BASE_URL() + "gerentes/processo/" + idProcesso);
	};

    this.vincularAnaliseGerenteTecnico = function(idGerente, idsProcessos) {

        return request
			.postAsUrlEncoded(config.BASE_URL() + "gerentes/vincular",{
                idUsuario: idGerente,
                idsProcesso: idsProcessos
            });
    };
};

exports.services.GerenteService = GerenteService;