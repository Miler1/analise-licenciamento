var ModalOficioRestricao = {

    bindings: {

        resolve: '<',
        close: '&',
        dismiss: '&'
    },

    controller: function(documentoService,analiseGeoService, documentoAnaliseService, mensagem) {

        var ctrl = this;
        ctrl.justificativaOrgao = null;
        ctrl.anexos = [];

        ctrl.$onInit =  function() {

            ctrl.restricao = ctrl.resolve.restricao;
            ctrl.idAnaliseGeo = ctrl.resolve.idAnaliseGeo;
                
            analiseGeoService.getComunicadoByIdAnaliseGeo(ctrl.idAnaliseGeo)
                .then(function(response){

                    var comunicado = response.data;
                    ctrl.justificativaOrgao = comunicado.parecerOrgao;
                    ctrl.anexos = ctrl.anexos.concat(comunicado.anexos);

            });

        };

        ctrl.baixarDocumento = function (documento) {
            
            documentoService.downloadById(documento.id);
        };

        ctrl.downloadPDFOficioOrgao = function () {
                             
            analiseGeoService.getComunicadoByIdAnaliseGeo(ctrl.idAnaliseGeo)
            .then(function(response){

                var comunicado = response.data;

                if(comunicado.orgao.sigla.toUpperCase() === app.utils.Orgao.IPHAN || comunicado.orgao.sigla.toUpperCase() === app.utils.Orgao.IBAMA){
                    return;
                } else {
                    documentoAnaliseService.generatePDFOficioOrgao(comunicado.id)
                    .then(function(data, status, headers){
        
                        var url = URL.createObjectURL(data.data.response.blob);
                        window.open(url, '_blank');
                        
        
                    },function(error){
                        mensagem.error(error.data.texto);
                    });
                }			
            });
            
        };


        ctrl.fechar = function() {
			ctrl.dismiss({$value: 'cancel'});
		};

    },
    templateUrl: 'components/modalOficioRestricao/modalOficioRestricao.html'
};

exports.directives.ModalOficioRestricao = ModalOficioRestricao;