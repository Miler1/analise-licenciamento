var QuestionarioService = function(request, config) {

	this.getQuestionario = function(idProcesso) {

		return request
			.get(config.BASE_URL() + 'questionario/' + idProcesso);
	};
	
};

exports.services.QuestionarioService = QuestionarioService;
