var ModalOficioRestricao = {

    bindings: {

        resolve: '<',
        close: '&',
        dismiss: '&'
    },

    controller: function(documentoService,analiseGeoService, documentoAnaliseService, mensagem) {

        var ctrl = this;
        ctrl.justificativaOrgao=null;
        ctrl.anexos = [];

        ctrl.$onInit =  function() {

            analiseGeoService.listaComunicados(ctrl.resolve.idAnaliseGeo)
                .then(function(response){
                    var comunicados = response.data;
                    _.forEach(comunicados, function(comunicado) {

                        ctrl.justificativaOrgao = comunicado.parecerOrgao;

                    });
            });
            ctrl.restricao = ctrl.resolve.restricao;
            ctrl.idAnaliseGeo = ctrl.resolve.idAnaliseGeo;
            
        };

        ctrl.baixarDocumento = function (documento) {
            documentoService.download(documento.key, documento.nomeDoArquivo);
        };


        ctrl.downloadPDFOficioOrgao = function () {
             
            analiseGeoService.listaComunicados(ctrl.idAnaliseGeo)
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
    templateUrl: 'components/modalOficioRestricao/modalOficioRestricao.html'
};

exports.directives.ModalOficioRestricao = ModalOficioRestricao;