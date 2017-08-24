var VisualizarAnaliseJuridica = {

	bindings: {

		analiseJuridica: '<'
	},

	controller: function(documentoAnaliseService) {

    console.log(this);

		var ctrl = this;

		ctrl.downloadDocumentoAnalise = function(idDocumento) {

			documentoAnaliseService.download(idDocumento);
		};
	},
	templateUrl: 'components/visualizarAnaliseJuridica/visualizarAnaliseJuridica.html'
};

exports.directives.VisualizarAnaliseJuridica = VisualizarAnaliseJuridica;