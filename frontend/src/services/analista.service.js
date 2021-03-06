var AnalistaService = function(request, config) {


    this.getAnalistasTecnicos = function() {

		return request
			.get(config.BASE_URL() + "analistas");
	};

	this.getAnalistasTecnicoBySetor = function() {

		return request
			.get(config.BASE_URL() + "analistasTecnico/findAllBySetor");

	};

	this.buscarAnalistasGeoByIdProcesso = function(idProcesso) {
		return request
			.get(config.BASE_URL() + "analistas/buscarAnalistasGeoByIdProcesso/" + idProcesso);
	};

	this.buscarAnalistasTecnicoByIdProcesso = function(idProcesso) {
		return request
			.get(config.BASE_URL() + "analistas/buscarAnalistasTecnicoByIdProcesso/" + idProcesso);
	};

	this.getAnalistasGeo = function() {

		return request
			.get(config.BASE_URL() + "analistasGeo");
	};

    this.getAnalistasTecnicosByProcesso = function(idProcesso) {

		return request
			.get(config.BASE_URL() + "analistas/" + idProcesso);
	};

    this.getAnalistasTecnicosByPerfil = function(isGerenteLogado) {

		return request
			.get(config.BASE_URL() + "analistas/perfil?isGerente=" + isGerenteLogado);
	};

	this.getAnalistasGeoByPerfil = function(isGerenteLogado) {

		return request
			.get(config.BASE_URL() + "analistasGeo/perfil?isGerente=" + isGerenteLogado);
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