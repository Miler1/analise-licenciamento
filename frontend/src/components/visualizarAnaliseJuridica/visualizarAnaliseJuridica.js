var VisualizarAnaliseJuridica = {

	bindings: {

		analiseJuridica: '<'
	},

	controller: function(documentoAnaliseService) {

		var ctrl = this;

		ctrl.downloadDocumentoAnalise = function(idDocumento) {

			documentoAnaliseService.download(idDocumento);
		};
	},
	templateUrl: 'components/visualizarAnaliseJuridica/visualizarAnaliseJuridica.html'
};

exports.directives.VisualizarAnaliseJuridica = VisualizarAnaliseJuridica;