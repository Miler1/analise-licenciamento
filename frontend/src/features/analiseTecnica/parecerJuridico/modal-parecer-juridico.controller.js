var ModalParecerJuridicoController = function ($uibModalInstance, parecerJuridico, documentos, documentoService, documentoLicenciamentoService, $location,$rootScope, mensagem, parecerJuridicoService) {

    var modalCtrl = this;

    modalCtrl.parecerJuridico = parecerJuridico;
    modalCtrl.documentos = documentos;
    modalCtrl.tiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    
    modalCtrl.baixarDocumento = function(anexo) {
		
		if(!anexo.id){

            documentoService.download(anexo.key, anexo.nomeDoArquivo);

        } else {

            documentoService.downloadById(anexo.id);

        }
	};

	modalCtrl.downloadDocumentos = function (id) {

		documentoService.downloadById(id);

    };
    
    modalCtrl.downloadDocumentoFundiario = function (id) {

        documentoLicenciamentoService.download(id);
        
	};

	modalCtrl.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};
	
};

exports.controllers.ModalParecerJuridicoController = ModalParecerJuridicoController;