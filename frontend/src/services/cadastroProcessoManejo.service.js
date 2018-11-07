var CadastroProcessoManejoService = function (request, config) {

	this.findTipologias = function (numeroProcesso) {

        return request
            .get(config.BASE_URL() + 'tipologiasManejo');
	};

	this.getMunicipios = function (uf) {

        return request
            .get(config.BASE_URL() + '/estados/'+ uf +'/municipios');
    };
};

exports.services.CadastroProcessoManejoService = CadastroProcessoManejoService;