var ModalNotificacaoRestricao = {

    bindings: {

        resolve: '<',
        close: '&',
        dismiss: '&'
    },

    controller: function(documentoService,analiseGeoService,inconsistenciaService, documentoAnaliseService, mensagem) {

        var ctrl = this;

        ctrl.anexos = [];

        ctrl.$onInit =  function() {

            ctrl.inconsistencia = ctrl.resolve.inconsistencia;
            ctrl.restricao = ctrl.resolve.restricao;
            ctrl.idAnaliseGeo = ctrl.resolve.idAnaliseGeo;
            
        };

        ctrl.baixarDocumentoInconsistencia= function(anexo) {

            if(!anexo.id){
                documentoService.download(anexo.key, anexo.nomeDoArquivo);
            }else{
                inconsistenciaService.download(anexo.id);
            }
    
        };

		ctrl.fechar = function() {
			ctrl.dismiss({$value: 'cancel'});
		};


        ctrl.downloadPDFOficioOrgao = function () {
             
            analiseGeoService.listaComunicadosByIdAnaliseGeo(ctrl.idAnaliseGeo)
                .then(function(response){
    
                    var comunicados = response.data;
    
                    _.forEach(comunicados, function(comunicado) {
                        
                        if(comunicado.orgao.sigla.toUpperCase() === app.utils.Orgao.IPHAN || comunicado.orgao.sigla.toUpperCase() === app.utils.Orgao.IBAMA){
                            return;
                        }else{
                            documentoAnaliseService.generatePDFOficioOrgao(comunicado.id)
                            .then(function(data, status, headers){
                
                                var a = document.createElement('a');
                                a.href = URL.createObjectURL(data.data.response.blob);
                                a.download = data.data.response.fileName ? data.data.response.fileName : 'oficio_orgao.pdf';
                                a.click();
                
                            },function(error){
                                mensagem.error(error.data.texto);
                            });		
                    }			
                    });
                });
        };
    

    },
    templateUrl: 'components/modalNotificacaoRestricao/modalNotificacaoRestricao.html'
};

exports.directives.ModalNotificacaoRestricao = ModalNotificacaoRestricao;