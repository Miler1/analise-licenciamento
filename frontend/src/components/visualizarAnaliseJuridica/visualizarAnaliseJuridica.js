var VisualizarAnaliseJuridica = {

	bindings: {

		analiseJuridica: '<',
		exibirDocumentosAnalisados: '<'
	},

	controller: function(documentoAnaliseService, documentoLicenciamentoService) {

		var ctrl = this;

		ctrl.downloadAnexo = function(idDocumento) {
			documentoAnaliseService.download(idDocumento);
		};

		ctrl.downloadDocumentoAnalisado = function(idDocumento) {
 			documentoLicenciamentoService.download(idDocumento);
		};
	},
	templateUrl: 'components/visualizarAnaliseJuridica/visualizarAnaliseJuridica.html'
};

exports.directives.VisualizarAnaliseJuridica = VisualizarAnaliseJuridica;