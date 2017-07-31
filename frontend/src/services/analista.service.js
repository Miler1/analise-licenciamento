var AnalistaService = function(request, config) {

	this.getAnalistasTecnicos = function(idProcesso) {

		return request
			.get(config.BASE_URL() + "analistas/" + idProcesso);
	};

    this.vincularAnaliseAnalistaTecnico = function(idAnalista, idsProcessos) {

        return request
			.postAsUrlEncoded(config.BASE_URL() + "analistas/vincular",{
                idUsuario: idAnalista,
                idsProcesso: idsProcessos
            });
    };
};

exports.services.AnalistaService = AnalistaService;