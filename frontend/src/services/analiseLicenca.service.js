var AnaliseLicencaService = function(request, config) {

	this.getByCaracterizacao = function(idCaracterizacao) {

		return request
			.get(config.BASE_URL() + "caracterizacoes/" + idCaracterizacao + "/analisesLicencas");
	};
};

exports.services.AnaliseLicencaService = AnaliseLicencaService;