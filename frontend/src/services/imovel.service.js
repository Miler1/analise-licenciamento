var ImovelService =  function (request, config, $uibModal) {

	this.getImovelDadosCadastrante = function(idImovel){

		return request
			.get(config.BASE_URL() + 'fichaImovelResumida',{

			idImovel : idImovel,
			tela: 'cadastranteFichaResumida'

		});
	};

	this.getImovelDadosImovel = function(idImovel){

		return request
			.get(config.BASE_URL() + 'fichaImovelResumida',{

			idImovel : idImovel,
			tela: 'imovelFichaResumida'

		});
	};

	this.getImovelDadosDominio = function(idImovel){

		return request
			.get(config.BASE_URL() + 'fichaImovelResumida',{

			idImovel : idImovel,
			tela: 'dominioFichaResumida'

		});

	};

	this.getImovelDemonstrativo = function(codigoImovel){

		return request
			.get(config.BASE_URL() + 'fichaImovelResumidaDemonstrativo',{
			codigoImovel : codigoImovel
		});
	};

	this.getImovelDadosGeo = function(idImovel){

		return request
			.get(config.BASE_URL() + 'fichaImovelResumida',{

			idImovel : idImovel,
			tela: 'geoFichaResumida'

		});
	};

	this.baixarDemonstrativo = function(codigoImovel){

		window.location.assign(config.BASE_URL() + 'baixarDemonstrativo?codigoImovel=' + codigoImovel);
	};

	this.baixarShapefile = function(codigoImovel){

		window.location.assign(config.BASE_URL() + 'baixarShapefile?codigoImovel=' + codigoImovel);
	};

	this.getImovelByCodigo = function(codigoImovel) {

		return request
		.get(config.BASE_URL() + 'imovel/codigo/' + codigoImovel + '/completo');
	};

};

exports.services.ImovelService = ImovelService;