var ModalInconsistenciaVistoriaController = function (
    $uibModalInstance,
    $rootScope,
    documentoService,
    uploadService,
    inconsistenciaVistoria,
    mensagem) {

    var modalCtrl = this;

    modalCtrl.inconsistenciaVistoria = inconsistenciaVistoria;
    modalCtrl.TAMANHO_MAXIMO_ARQUIVO_MB = 25;
    modalCtrl.labelModal = inconsistenciaVistoria.descricaoInconsistencia ? 'Editar' : 'Adicionar';

    modalCtrl.errors = {

        descricaoInconsistencia: false,
        tipoInconsistencia: false

    };

    modalCtrl.limparErrosInconsistenciaVistoria = function() {

        modalCtrl.errors = {

            descricaoInconsistencia: false,
            tipoInconsistencia: false
    
        };

    };

    modalCtrl.fechar = function () {

        modalCtrl.limparErrosInconsistenciaVistoria();
        $uibModalInstance.dismiss('cancel');
        
    };

    modalCtrl.init = function() {};

    modalCtrl.upload = function(file, invalidFile) {

        if(file) {

            uploadService.save(file)
                .then(function(response) {

                    modalCtrl.inconsistenciaVistoria.anexos.push(
                        {
                            key: response.data,
                            nomeDoArquivo: file.name,
                            tipo: {
                                id: app.utils.TiposDocumentosAnalise.INCONSISTENCIA_VISTORIA
                            }
                        });
                    }, function(error){
                        mensagem.error(error.data.texto, {referenceId: 5});
                    });

        } else if(invalidFile && invalidFile.$error === 'maxSize'){
            mensagem.error('Ocorreu um erro ao enviar o arquivo: ' + invalidFile.name + ' . Verifique se o arquivo tem no máximo ' + modalCtrl.TAMANHO_MAXIMO_ARQUIVO_MB + 'MB', {referenceId: 5});
        }

    };

    modalCtrl.removerDocumento = function (indiceDocumento) {

        modalCtrl.inconsistenciaVistoria.anexos.splice(indiceDocumento, 1);

    };

    modalCtrl.baixarDocumento = function(anexo) {

        if(!anexo.id){

            documentoService.download(anexo.key, anexo.nomeDoArquivo);

        } else{

            documentoService.downloadById(anexo.id);

        }

    };

    function inconsistenciaValida() {

        if(!modalCtrl.inconsistenciaVistoria.descricaoInconsistencia || modalCtrl.inconsistenciaVistoria.descricaoInconsistencia === ''){
            
            modalCtrl.errors.descricaoInconsistencia = true;

        }

        if(!modalCtrl.inconsistenciaVistoria.tipoInconsistencia || modalCtrl.inconsistenciaVistoria.tipoInconsistencia === ''){
            
            modalCtrl.errors.tipoInconsistencia = true;

        }

        return !Object.keys(modalCtrl.errors).some(function(key) {
            return modalCtrl.errors[key];
        });

    }

    modalCtrl.concluir = function() {

        if(inconsistenciaValida()){

            $rootScope.$broadcast('adicionarInconsistenciaVistoria', modalCtrl.inconsistenciaVistoria);
            modalCtrl.limparErrosInconsistenciaVistoria();
            modalCtrl.fechar();

        }

    };

};

exports.controllers.ModalInconsistenciaVistoriaController = ModalInconsistenciaVistoriaController;
