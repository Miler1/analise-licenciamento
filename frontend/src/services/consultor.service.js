var ConsultorService = function(request, config) {

	this.getConsultoresJuridicos = function(idPerfil) {

		return request
			.get(config.BASE_URL + "consultores/juridicos");
	};

    this.vincularAnaliseConsultorJuridico = function(idConsultor, idsProcessos) {

        return request
			.postAsUrlEncoded(config.BASE_URL + "consultores/juridicos/vincular",{
                idUsuario: idConsultor,
                idsProcesso: idsProcessos
            });
    };
};

exports.services.ConsultorService = ConsultorService;