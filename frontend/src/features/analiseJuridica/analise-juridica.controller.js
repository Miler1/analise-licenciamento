var AnaliseJuridicaController = function($rootScope, $scope, $routeParams, $location, processo, 
        analiseJuridica, documentoLicenciamentoService, uploadService, mensagem, $uibModal, analiseJuridicaService, documentoAnaliseService) {

    $rootScope.tituloPagina = 'PARECER JURÍDICO';
    var TAMANHO_MAXIMO_ARQUIVO_MB = 10;
    var ctrl = this;

    ctrl.DEFERIDO = app.utils.TiposResultadoAnalise.DEFERIDO;
    ctrl.INDEFERIDO = app.utils.TiposResultadoAnalise.INDEFERIDO;
    ctrl.EMITIR_NOTIFICACAO = app.utils.TiposResultadoAnalise.EMITIR_NOTIFICACAO;
    ctrl.TAMANHO_MAXIMO_ARQUIVO_MB = TAMANHO_MAXIMO_ARQUIVO_MB;
    ctrl.processo = processo;    
    ctrl.analiseJuridica = angular.copy(analiseJuridica);    
    ctrl.analiseJuridica.tipoResultadoAnalise = ctrl.analiseJuridica.tipoResultadoAnalise || {};    
    ctrl.documentosAnalisados = angular.copy();
    ctrl.documentosParecer = angular.copy(ctrl.analiseJuridica.documentos || []);
    ctrl.editarMotivoInvalidacao = editarMotivoInvalidacao;
    ctrl.upload = function(file) {

        uploadService.save(file)
            .then(function(response) {

                ctrl.documentosParecer.push({

                    key: response.data,
                    nome: file.name,
                    tipoDocumento: {

                        id: app.utils.TiposDocumentosAnalise.ANALISE_JURIDICA
                    }
                });
            }, function(error){

                mensagem.error('Ocorreu um erro ao enviar o arquivo. Verifique se o arquivo tem no máximo ' + TAMANHO_MAXIMO_ARQUIVO_MB + 'MB');
            });
    };
    
    ctrl.cancelar = function() {

        $location.path('caixa-entrada');
    };

    ctrl.salvar = function() {

        montarAnaliseJuridica();
        analiseJuridicaService.salvar(ctrl.analiseJuridica)
            .then(function(response) {

                mensagem.success(response.data.texto);
            
            }, function(error){

                mensagem.error(error.data.texto);
            });        
    };

    ctrl.concluir = function(){

        if(!analiseValida()) {

            mensagem.error('Não foi possível concluir a análise porque existem campos inválidos ou documentos que não foram avaliados.');
        }

        montarAnaliseJuridica();
        analiseJuridicaService.concluir(ctrl.analiseJuridica)
            .then(function(response) {

            });
    };

    ctrl.downloadDocumentoLicenciamento = function(idDocumento) {

        documentoLicenciamentoService.download(idDocumento);
    };

    ctrl.downloadDocumentoAnalise = function(idDocumento) {

        documentoAnaliseService.download(idDocumento);
    };

    ctrl.removerDocumento = function(indiceDocumento) {

        ctrl.documentosParecer.splice(indiceDocumento,1);
    };

    ctrl.init = function() {

        getDocumentosAnalisados();
    };

    function montarAnaliseJuridica() {

        ctrl.analiseJuridica.documentos = ctrl.documentosParecer;
        ctrl.analiseJuridica.analisesDocumentos = ctrl.analisesDocumentos;
    }

    function editarMotivoInvalidacao(indiceDocumento) {

        var analiseDocumento = ctrl.analisesDocumentos[indiceDocumento];

        var modalInstance = $uibModal.open({

            component: 'modalParecerDocumento',
            size: 'lg',
            backdrop: 'static',
            resolve: {

                nomeDocumento: function() {

                    return analiseDocumento.documento.tipo.nome;
                },

                parecer: function() {

                    return analiseDocumento.parecer;
                }
            }
        });

        modalInstance.result.then(function(response){

            ctrl.analisesDocumentos[indiceDocumento].parecer = response;
        
        }, function(){ });
    }

    function getDocumentosAnalisados() {

        analiseJuridicaService.getDocumentosAnalisados(ctrl.analiseJuridica.id)
            .then(function(response){
                            
                ctrl.analisesDocumentos = response.data;

                if(ctrl.analisesDocumentos.length === 0) {

                    var documentosProcesso = angular.copy(ctrl.analiseJuridica
                                                .analise
                                                .processo
                                                .caracterizacoes[0]
                                                .documentosEnviados);

                    ctrl.analisesDocumentos = documentosProcesso.map(function(documento){

                        return {

                            "validado": undefined,
                            "parecer": undefined,
                            "documento": documento
                        };
                    });
                }
            });
    }

    function analiseValida() {

        $scope.formularioParecer.$setSubmitted();
        $scope.formularioResultado.$setSubmitted();

        var parecerPreenchido = $scope.formularioParecer.$valid;
        var resultadoPreenchido = $scope.formularioResultado.$valid;
        var documentosNaoValidados = ctrl.analisesDocumentos.filter(function(analise){

            return analise.validado === undefined || (analise.validado === false && !analise.parecer);
        });

        return parecerPreenchido && documentosNaoValidados.length === 0 && resultadoPreenchido;
    }
};

exports.controllers.AnaliseJuridicaController = AnaliseJuridicaController;

