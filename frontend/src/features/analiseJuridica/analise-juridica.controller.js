var AnaliseJuridicaController = function($rootScope, $scope, $routeParams, processo, 
        analiseJuridica, documentoLicenciamentoService, uploadService, mensagem, $uibModal) {

    var TAMANHO_MAXIMO_ARQUIVO_MB = 10;
    var ctrl = this;

    $rootScope.tituloPagina = 'PARECER JURÍDICO';

    ctrl.TAMANHO_MAXIMO_ARQUIVO_MB = TAMANHO_MAXIMO_ARQUIVO_MB;

    ctrl.processo = processo;
    ctrl.analiseJuridica = angular.copy(analiseJuridica);
    
    ctrl.documentosProcesso = angular.copy(ctrl.analiseJuridica
                                                .analise
                                                .processo
                                                .caracterizacoes[0]
                                                .documentosEnviados);

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

    ctrl.editarMotivoInvalidacao = function(indiceDocumento) {

        var documento = ctrl.documentosProcesso[indiceDocumento];

        var modalInstance = $uibModal.open({

            component: 'modalParecerDocumento',
            size: 'lg',
            resolve: {

                nomeDocumento: function() {

                    return documento.tipo.nome;
                },

                parecer: function() {

                    return documento.parecer;
                }
            }
        });

        modalInstance.result.then(function(response){

            console.log(response);
        
        }, function(){

        });
    };
};

exports.controllers.AnaliseJuridicaController = AnaliseJuridicaController;