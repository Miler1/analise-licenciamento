var ParecerJuridicoService = function(request,config) {

	this.enviar = function(params){

		return request
			.post(config.BASE_URL() + 'analisesGeo/parecerJuridico/salvarParecerJuridico' , params);
	};

	this.findParecerJuridico = function(idParecerJuridico){

		return request
			.get(config.BASE_URL() + 'analisesGeo/parecerJuridico/findParecerJuridico/'+ idParecerJuridico);
	};

	this.findPareceres = function(){

		return request
			.get(config.BASE_URL() + 'analisesGeo/parecerJuridico/findPareceres/');
	};

	this.getParecerJuridicoByAnaliseTecnica = function(idAnaliseTecnica){

		return request
			.get(config.BASE_URL() + 'analisesGeo/parecerJuridico/getParecerJuridicoByAnaliseTecnica/'+ idAnaliseTecnica);
	};


};

exports.services.ParecerJuridicoService = ParecerJuridicoService;
