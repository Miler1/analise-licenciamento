var ModalVisualizarQuestionario = {

	bindings: {

		resolve: '<',
		close: '&',
		dismiss: '&'
	},

	controller: function(questionarioService) {

        var ctrl = this;
        
        ctrl.$onInit =  function() {
			
			questionarioService.getQuestionario(ctrl.resolve.idProcesso)
				.then(function(response){
					tratarDadosQuestionario(response.data);
					ctrl.questionario = response.data;
			});

		};
		
		function tratarDadosQuestionario(questionario) {
			if(questionario){
				questionario.consumoAgua = questionario.consumoAgua ? "true" : "false";
				questionario.efluentes = questionario.efluentes ? "true" : "false";
				questionario.residuosSolidos = questionario.residuosSolidos ? "true" : "false";
			}
		}

		ctrl.fechar = function() {

			ctrl.dismiss({$value: 'cancel'});
		};

	},
	templateUrl: 'components/modalVisualizarQuestionario/modalVisualizarQuestionario.html'
};

exports.directives.ModalVisualizarQuestionario = ModalVisualizarQuestionario;
