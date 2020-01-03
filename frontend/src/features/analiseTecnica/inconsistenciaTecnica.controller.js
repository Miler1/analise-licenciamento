var InconsistenciaTecnicaController = function (
    $uibModalInstance,
    documentoService,
    tamanhoMaximoArquivoAnaliseMB,
    uploadService,
    analiseTecnica,
    inconsistenciaTecnica,
    atividadeCaracterizacao, 
    parametroAtividade,
    questionario,
    documentoAdministrativo,
    documentoTecnicoAmbiental,
    $rootScope,
    inconsistenciaService,
    tipoDeInconsistenciaTecnica,
    mensagem) {

var modalCtrl = this;

modalCtrl.TAMANHO_MAXIMO_ARQUIVO_MB = tamanhoMaximoArquivoAnaliseMB;
modalCtrl.anexos = [];
modalCtrl.tipoDeInconsistenciaTecnica = tipoDeInconsistenciaTecnica;
modalCtrl.questionario = questionario;

modalCtrl.errors = {

    analiseTecnica: {
        descricao:false,
        tipo:false
    }

};

modalCtrl.fechar = function () {
    $uibModalInstance.dismiss('cancel');
};


modalCtrl.init = function(){
    if(inconsistenciaTecnica){

        if(inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca != null){
            inconsistenciaTecnica.tipoDeInconsistenciaTecnica = app.utils.InconsistenciaTecnica.TIPO_LICENCA;

        }else if(inconsistenciaTecnica.inconsistenciaTecnicaAtividade != null){
            inconsistenciaTecnica.tipoDeInconsistenciaTecnica = app.utils.InconsistenciaTecnica.ATIVIDADE;

        }else if(inconsistenciaTecnica.inconsistenciaTecnicaParametro != null){
            inconsistenciaTecnica.tipoDeInconsistenciaTecnica = app.utils.InconsistenciaTecnica.PARAMETRO;
            inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade = parametroAtividade;

        }else if(inconsistenciaTecnica.inconsistenciaTecnicaQuestionario != null){
            inconsistenciaTecnica.tipoDeInconsistenciaTecnica = app.utils.InconsistenciaTecnica.QUESTIONARIO;
            inconsistenciaTecnica.inconsistenciaTecnicaQuestionario.questionario = questionario;
            
        }else if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo != null){
            inconsistenciaTecnica.tipoDeInconsistenciaTecnica = app.utils.InconsistenciaTecnica.DOCUMENTO_ADMINISTRATIVO;
            inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo = documentoAdministrativo;

        }else if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAdministrativo){
            inconsistenciaTecnica.tipoDeInconsistenciaTecnica = app.utils.InconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL;
            inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAdministrativo.documentoTecnicoAmbiental = documentoTecnicoAmbiental;
        }

        inconsistenciaService.findInconsistenciaTecnica(inconsistenciaTecnica.id)
        .then(function(response){

            inconsistenciaTecnica = response.data;
            modalCtrl.descricaoInconsistencia = inconsistenciaTecnica.descricaoInconsistencia;
            modalCtrl.tipoInconsistencia = inconsistenciaTecnica.tipoInconsistencia;
            modalCtrl.anexos = inconsistenciaTecnica.anexos;
            modalCtrl.id = inconsistenciaTecnica.id;
        });

    }
};

modalCtrl.upload = function(file, invalidFile) {

    if(file) {

            uploadService.save(file)
                    .then(function(response) {

                        modalCtrl.anexos.push({

                                    key: response.data,
                                    nomeDoArquivo: file.name,
                                    tipoDocumento: {

                                            id: app.utils.TiposDocumentosAnalise.INCONSISTENCIA_TECNICA
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

    modalCtrl.anexos.splice(indiceDocumento,1);
};


modalCtrl.baixarDocumentoInconsistencia= function(anexo) {

    if(!anexo.id){
        documentoService.download(anexo.key, anexo.nomeDoArquivo);
    }else{
        inconsistenciaService.download(anexo.id);
    }

};

function verificaCampos() {

    if(!modalCtrl.descricaoInconsistencia || modalCtrl.descricaoInconsistencia === ''){
        modalCtrl.errors.descricao = true;
    }else {
        modalCtrl.errors.descricao = false;
    }

    if(!modalCtrl.tipoInconsistencia || modalCtrl.tipoInconsistencia === ''){
        modalCtrl.errors.tipo = true;
    }else {
        modalCtrl.errors.tipo = false;
    }

    if (modalCtrl.errors.descricao === true || modalCtrl.errors.tipo === true){
        return false;
    }

}

modalCtrl.concluir = function() {
    var params;

    if(verificaCampos()){
        return;
    }else{
               
        params = {
            id: modalCtrl.id,
            analiseTecnica: {id: analiseTecnica.id},
            tipoInconsistencia: modalCtrl.tipoInconsistencia,
            descricaoInconsistencia: modalCtrl.descricaoInconsistencia,
            anexos: modalCtrl.anexos,
            tipoDeInconsistenciaTecnica: modalCtrl.tipoDeInconsistenciaTecnica,
            
        };
        if(modalCtrl.tipoDeInconsistenciaTecnica === app.utils.InconsistenciaTecnica.TIPO_LICENCA){

            params.inconsistenciaTecnicaTipoLicenca = {
                tipoLicenca: analiseTecnica.analise.processo.caracterizacao.tipoLicenca
            };

        }else if (modalCtrl.tipoDeInconsistenciaTecnica === app.utils.InconsistenciaTecnica.ATIVIDADE){
            params.inconsistenciaTecnicaAtividade = {
                atividadeCaracterizacao: atividadeCaracterizacao
            };

        }else if (modalCtrl.tipoDeInconsistenciaTecnica === app.utils.InconsistenciaTecnica.PARAMETRO){
            params.inconsistenciaTecnicaParametro = { 
                parametroAtividade: parametroAtividade
            };

        }else if (modalCtrl.tipoDeInconsistenciaTecnica === app.utils.InconsistenciaTecnica.QUESTIONARIO){
            params.inconsistenciaTecnicaQuestionario = {
                questionario: questionario
            };

        }else if (modalCtrl.tipoDeInconsistenciaTecnica === app.utils.InconsistenciaTecnica.DOCUMENTO_ADMINISTRATIVO){
            params.inconsistenciaTecnicaDocumentoAdministrativo = {
                documentoAdministrativo: documentoAdministrativo
            };
            
        }else if (modalCtrl.tipoDeInconsistenciaTecnica === app.utils.InconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL){
            params.inconsistenciaTecnicaDocumentoTecnicoAdministrativo = {
                documentosTecnicos: documentoTecnicoAmbiental
            };
        }

        inconsistenciaService.salvarInconsistenciaTecnica(params)
            .then(function(response){
                mensagem.success("Inconsistência salva com sucesso!");
                $rootScope.$broadcast('atualizarMarcacaoInconsistencia', tipoDeInconsistenciaTecnica, parametro, documentoAdministrativo, documentoTecnicoAmbiental);
                modalCtrl.fechar();

            }).catch(function(response){
                mensagem.error(response.data.texto, {referenceId: 5});
                
            });
      }

};

};

exports.controllers.InconsistenciaTecnicaController = InconsistenciaTecnicaController;