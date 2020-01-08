var ModalVisualizarInconsistenciaTecnica = {
	bindings: {

		resolve: '<',
		close: '&',
		dismiss: '&'
	},

	controller: function(documentoService, inconsistenciaService) {

		var ctrl = this;
		
		ctrl.$onInit =  function() {

			ctrl.inconsistenciaTecnica = ctrl.resolve.inconsistenciaTecnica;

		};
		
		ctrl.fechar = function() {

			ctrl.dismiss({$value: 'cancel'});
		};

		ctrl.baixarDocumentoInconsistencia = function (anexo){

			if(!anexo.id){
				documentoService.download(anexo.key, anexo.nomeDoArquivo);
			}else{
				inconsistenciaService.download(anexo.id);
			}

		};

	},
	templateUrl: 'components/modalVisualizarInconsistenciaTecnica/modalVisualizarInconsistenciaTecnica.html'
};

exports.directives.ModalVisualizarInconsistenciaTecnica = ModalVisualizarInconsistenciaTecnica;