var InconsistenciaTecnicaController = function (
    $uibModalInstance,
    documentoService,
    uploadService,
    analiseTecnica,
    inconsistenciaTecnica,
    atividadeCaracterizacao, 
    parametroAtividade,
    questionario,
    documentoAdministrativo,
    documentoTecnicoAmbiental,
    index,
    indexParametro,
    isCoordenador,
    $rootScope,
    inconsistenciaService,
    tipoDeInconsistenciaTecnica,
    mensagem) {

var modalCtrl = this;

modalCtrl.TAMANHO_MAXIMO_ARQUIVO_MB = 25;
modalCtrl.anexos = [];
modalCtrl.tipoDeInconsistenciaTecnica = tipoDeInconsistenciaTecnica;
modalCtrl.questionario = questionario;
modalCtrl.tipoInconsistencia = null;
modalCtrl.descricaoInconsistencia = null;
modalCtrl.isCoordenador = isCoordenador;

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
            inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao = atividadeCaracterizacao;

        }else if(inconsistenciaTecnica.inconsistenciaTecnicaParametro != null){
            inconsistenciaTecnica.tipoDeInconsistenciaTecnica = app.utils.InconsistenciaTecnica.PARAMETRO;
            inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade = parametroAtividade;
            inconsistenciaTecnica.inconsistenciaTecnicaParametro.atividadeCaracterizacao = atividadeCaracterizacao;

        }else if(inconsistenciaTecnica.inconsistenciaTecnicaQuestionario != null){
            inconsistenciaTecnica.tipoDeInconsistenciaTecnica = app.utils.InconsistenciaTecnica.QUESTIONARIO;
            inconsistenciaTecnica.inconsistenciaTecnicaQuestionario.questionario = questionario;
            
        }else if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo != null){
            inconsistenciaTecnica.tipoDeInconsistenciaTecnica = app.utils.InconsistenciaTecnica.DOCUMENTO_ADMINISTRATIVO;
            inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo = documentoAdministrativo;

        }else if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental){
            inconsistenciaTecnica.tipoDeInconsistenciaTecnica = app.utils.InconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL;
            inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentoTecnicoAmbiental = documentoTecnicoAmbiental;
        }

        inconsistenciaService.findInconsistenciaTecnica(inconsistenciaTecnica.id)
        .then(function(response){

            if(response.data != null){

                inconsistenciaTecnica = response.data;
                modalCtrl.descricaoInconsistencia = inconsistenciaTecnica.descricaoInconsistencia;
                modalCtrl.tipoInconsistencia = inconsistenciaTecnica.tipoInconsistencia;
                modalCtrl.anexos = inconsistenciaTecnica.anexos;
                modalCtrl.id = inconsistenciaTecnica.id;
            }
        });

    }
};

modalCtrl.upload = function(file, invalidFile) {

    if(file) {

            uploadService.save(file)
                    .then(function(response) {

                        var nomeDoArquivo = file.name;

                        var quantidadeDocumentosComMesmoNome = modalCtrl.anexos.filter(function(documento) {
                            return documento.nomeDoArquivo.includes(file.name.split("\.")[0]);
                        }).length;
    
                        if(quantidadeDocumentosComMesmoNome > 0) {
                            nomeDoArquivo = file.name.split("\.")[0] + " (" + quantidadeDocumentosComMesmoNome + ")." + file.name.split("\.")[1];
                        }

                        modalCtrl.anexos.push({

                            key: response.data,
                            nomeDoArquivo: nomeDoArquivo,
                            tipoDocumento: {

                                    id: app.utils.TiposDocumentosAnalise.INCONSISTENCIA_TECNICA
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
        modalCtrl.errors.analiseTecnica.descricao = true;
    }else {
        modalCtrl.errors.analiseTecnica.descricao = false;
    }

    if(!modalCtrl.tipoInconsistencia || modalCtrl.tipoInconsistencia === ''){
        modalCtrl.errors.analiseTecnica.tipo = true;
    }else {
        modalCtrl.errors.analiseTecnica.tipo = false;
    }

    if (modalCtrl.errors.analiseTecnica.descricao === true || modalCtrl.errors.analiseTecnica.tipo === true){
        return false;
    }else{
        return true;
    }

}

modalCtrl.concluir = function() {
    var params;

    if(!verificaCampos()){
        mensagem.error('Não foi possível concluir a análise. Verifique os campos obrigatórios!', {referenceId: 5});
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
                parametroAtividade: parametroAtividade,
                atividadeCaracterizacao:atividadeCaracterizacao
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
            params.inconsistenciaTecnicaDocumentoTecnicoAmbiental = {
                documentosTecnicos: documentoTecnicoAmbiental
            };
        }

        inconsistenciaService.salvarInconsistenciaTecnica(params)
            .then(function(response){
                
                mensagem.success("Inconsistência salva com sucesso!");
                $rootScope.$broadcast('atualizarMarcacaoInconsistencia', tipoDeInconsistenciaTecnica, response.data, index, indexParametro);
                modalCtrl.fechar();

            }).catch(function(response){
                mensagem.error(response.data.texto, {referenceId: 5});
                
            });
      }

};

};

exports.controllers.InconsistenciaTecnicaController = InconsistenciaTecnicaController;