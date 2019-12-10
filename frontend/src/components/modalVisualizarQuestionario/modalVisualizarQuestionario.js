var ModalVisualizarQuestionario = {

	bindings: {

		resolve: '<',
		close: '&',
		dismiss: '&'
	},

	controller: function(mensagem, analiseJuridicaService, $scope, documentoAnaliseService) {

        var ctrl = this;
        
        ctrl.$onInit =  function() {

            ctrl.questionario = ctrl.resolve.questionario;
        };

		ctrl.fechar = function() {

			ctrl.dismiss({$value: 'cancel'});
		};

	},
	templateUrl: 'components/modalVisualizarQuestionario/modalVisualizarQuestionario.html'
};

exports.directives.ModalVisualizarQuestionario = ModalVisualizarQuestionario;
