var ModalInconsistenciaVistoriaController = function (
    $uibModalInstance,
    $rootScope,
    documentoService,
    tamanhoMaximoArquivoAnaliseMB,
    uploadService,
    inconsistenciaVistoria,
    inconsistenciaVistoriaService,
    mensagem) {

    var modalCtrl = this;

    modalCtrl.inconsistenciaVistoria = inconsistenciaVistoria;
    modalCtrl.TAMANHO_MAXIMO_ARQUIVO_MB = tamanhoMaximoArquivoAnaliseMB;
    modalCtrl.labelModal = inconsistenciaVistoria.id ? 'Editar' : 'Adicionar';

    modalCtrl.errors = {

        descricaoInconsistencia: false

    };

    modalCtrl.fechar = function () {
        $uibModalInstance.dismiss('cancel');
    };

    modalCtrl.init = function(){};

    modalCtrl.upload = function(file, invalidFile) {

        if(file) {

            uploadService.save(file)
                .then(function(response) {

                    modalCtrl.inconsistenciaVistoria.anexos.push({
                            key: response.data,
                            nomeDoArquivo: file.name,
                            tipo: {
                                id: app.utils.TiposDocumentosAnalise.INCONSISTENCIA_VISTORIA
                            }
                        });
                                       
                    }, function(error){
                        mensagem.error(error.data.texto);
                    });

        } else if(invalidFile && invalidFile.$error === 'maxSize'){
            mensagem.error('Ocorreu um erro ao enviar o arquivo: ' + invalidFile.name + ' . Verifique se o arquivo tem no máximo ' + TAMANHO_MAXIMO_ARQUIVO_MB + 'MB');
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

        }else {

            modalCtrl.errors.descricaoInconsistencia = false;

        }

        return !Object.keys(modalCtrl.errors).some(function(key) {
            return modalCtrl.errors[key];
        });

    }

    modalCtrl.concluir = function() {

        if(!inconsistenciaValida()){

            return;

        }else{

            inconsistenciaVistoriaService.salvar(modalCtrl.inconsistenciaVistoria)
                .then(function(response){

                    mensagem.success("Inconsistência salva com sucesso!");
                    $rootScope.$broadcast('buscarInconsistenciaVistoria', response.data);

                    modalCtrl.fechar();

                }).catch(function(response){
                    mensagem.error(response.data.texto, {referenceId: 5});
                });

        }

    };

};

exports.controllers.ModalInconsistenciaVistoriaController = ModalInconsistenciaVistoriaController;
