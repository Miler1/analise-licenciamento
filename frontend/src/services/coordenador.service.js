var CoordenadorService = function(request, config) {

    this.getCoordenador = function() {

		return request
			.get(config.BASE_URL() + "coordenadorTecnico");
	};

    this.getCoordenadorByIdProcesso = function(idProcesso) {

		return request
			.get(config.BASE_URL() + "coordenadorTecnico/processo/" + idProcesso);
	};

    this.vincularAnaliseCoordenador = function(idCoordenador, idsProcessos) {

        return request
			.postAsUrlEncoded(config.BASE_URL() + "coordenadorTecnico/vincular",{
                idUsuario: idCoordenador,
                idsProcesso: idsProcessos
            });
    };

    this.getCoordenador = function (idPerfil, idProcesso) {

        return request
            .get(config.BASE_URL() + "coordenadoresAprovacao?idPerfil=" + idPerfil + "&idProcesso=" + idProcesso);
    };

};

exports.services.CoordenadorService = CoordenadorService;