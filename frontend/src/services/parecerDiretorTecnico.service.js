var ParecerDiretorTecnicoService = function(request, config) {

	this.findParecerByAnalise = function(id) {

		return request
			.get(config.BASE_URL() + 'parecer/diretor/findParecerByAnalise/' + id);

    };

    this.concluirParecerDiretorTecnico = function(params){

		return request
			.post(config.BASE_URL() + 'parecer/diretor/concluirParecerDiretorTecnico', params);
	};
    


};

exports.services.ParecerDiretorTecnicoService = ParecerDiretorTecnicoService;
