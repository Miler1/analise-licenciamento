var AnaliseJuridicaController = function($rootScope, $scope, $routeParams, processo, 
        analiseJuridica, documentoLicenciamentoService, uploadService, mensagem, $uibModal, analiseJuridicaService) {

    var TAMANHO_MAXIMO_ARQUIVO_MB = 10;
    var ctrl = this;

    $rootScope.tituloPagina = 'PARECER JURÍDICO';

    ctrl.TAMANHO_MAXIMO_ARQUIVO_MB = TAMANHO_MAXIMO_ARQUIVO_MB;

    ctrl.processo = processo;
    ctrl.analiseJuridica = angular.copy(analiseJuridica);
    
    ctrl.documentosAnalisados = angular.copy();

    ctrl.documentosParecer = angular.copy(ctrl.analiseJuridica.documentos || []);

    ctrl.upload = function(file) {

        uploadService.save(file)
            .then(function(response) {

                ctrl.documentosParecer.push({

                    key: response,
                    nome: file.name,
                    tipoDocumento: {

                        id: app.utils.TiposDocumentosAnalise.ANALISE_JURIDICA
                    }
                });
            }, function(error){

                mensagem.error('Ocorreu um erro ao enviar o arquivo. Verifique se o arquivo tem no máximo ' + TAMANHO_MAXIMO_ARQUIVO_MB + 'MB');
            });
    };

    ctrl.downloadDocumentoLicenciamento = function(idDocumento) {

        documentoLicenciamentoService.download(idDocumento);
    };

    ctrl.editarMotivoInvalidacao = editarMotivoInvalidacao;

    ctrl.init = function() {

        getDocumentosAnalisados();
    };

    function editarMotivoInvalidacao(indiceDocumento) {

        var analiseDocumento = ctrl.analisesDocumentos[indiceDocumento];

        var modalInstance = $uibModal.open({

            component: 'modalParecerDocumento',
            size: 'lg',
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
};

exports.controllers.AnaliseJuridicaController = AnaliseJuridicaController;

