var QuestionarioService = function(request, config) {

	this.getQuestionario = function(idProcesso) {

		return request
			.get(config.BASE_URL() + 'processos/questionario/' + idProcesso);
	};

	
};

exports.services.QuestionarioService = QuestionarioService;
