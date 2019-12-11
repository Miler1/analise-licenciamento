var ModalOficioRestricao = {

    bindings: {

        resolve: '<',
        close: '&',
        dismiss: '&'
    },

    controller: function(documentoService,analiseGeoService, documentoAnaliseService, parecerAnalistaGeoService, mensagem) {

        var ctrl = this;
        ctrl.justificativaOrgao = null;
        ctrl.tipoResultadoAnalise = null;
        ctrl.INDEFERIDO = app.utils.TiposResultadoAnalise.INDEFERIDO;
        ctrl.anexos = [];

        ctrl.$onInit =  function() {

            ctrl.restricao = ctrl.resolve.restricao;
            ctrl.idAnaliseGeo = ctrl.resolve.idAnaliseGeo;
            
            var sobreposicaoRestricao = ctrl.restricao.sobreposicaoCaracterizacaoAtividade ? ctrl.restricao.sobreposicaoCaracterizacaoAtividade : ctrl.restricao.sobreposicaoCaracterizacaoEmpreendimento ? ctrl.restricao.sobreposicaoCaracterizacaoEmpreendimento : ctrl.restricao.sobreposicaoCaracterizacaoComplexo;            
            
            parecerAnalistaGeoService.findParecerByIdAnaliseGeo(ctrl.idAnaliseGeo)
                .then(function(response){

                    var parecer = response.data;
                    ctrl.tipoResultadoAnalise = parecer.tipoResultadoAnalise.id;

            });

            if (ctrl.tipoResultadoAnalise !== ctrl.INDEFERIDO) {

                if(ctrl.restricao.sobreposicaoCaracterizacaoEmpreendimento) {

                    analiseGeoService.getComunicadoByIdAnaliseGeoEmpreendimento(ctrl.idAnaliseGeo, sobreposicaoRestricao.id)
                        .then(function(response){

                        var comunicado = response.data;
                        ctrl.justificativaOrgao = comunicado.parecerOrgao;
                        ctrl.anexos = ctrl.anexos.concat(comunicado.anexos);

                    });

                } else if(ctrl.restricao.sobreposicaoCaracterizacaoAtividade) {
                    
                    analiseGeoService.getComunicadoByIdAnaliseGeoAtividade(ctrl.idAnaliseGeo, sobreposicaoRestricao.id)
                        .then(function(response){

                        var comunicado = response.data;
                        ctrl.justificativaOrgao = comunicado.parecerOrgao;
                        ctrl.anexos = ctrl.anexos.concat(comunicado.anexos);

                    });

                } else if(ctrl.restricao.sobreposicaoCaracterizacaoComplexo) {
                    
                    analiseGeoService.getComunicadoByIdAnaliseGeoComplexo(ctrl.idAnaliseGeo, sobreposicaoRestricao.id)
                        .then(function(response){

                        var comunicado = response.data;
                        ctrl.justificativaOrgao = comunicado.parecerOrgao;
                        ctrl.anexos = ctrl.anexos.concat(comunicado.anexos);

                    });

                }
            }
            
        };

        ctrl.baixarDocumento = function (documento) {
            
            documentoService.downloadById(documento.id);
        };

        ctrl.downloadPDFOficioOrgao = function () {
            
            var sobreposicaoRestricao = ctrl.restricao.sobreposicaoCaracterizacaoAtividade ? ctrl.restricao.sobreposicaoCaracterizacaoAtividade : ctrl.restricao.sobreposicaoCaracterizacaoEmpreendimento ? ctrl.restricao.sobreposicaoCaracterizacaoEmpreendimento : ctrl.restricao.sobreposicaoCaracterizacaoComplexo;
            
            if(ctrl.restricao.sobreposicaoCaracterizacaoEmpreendimento) {
                
                analiseGeoService.getComunicadoByIdAnaliseGeoEmpreendimento(ctrl.idAnaliseGeo, sobreposicaoRestricao.id)
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

            } else if(ctrl.restricao.sobreposicaoCaracterizacaoAtividade) {
                
                analiseGeoService.getComunicadoByIdAnaliseGeoAtividade(ctrl.idAnaliseGeo, sobreposicaoRestricao.id)
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

            } else if(ctrl.restricao.sobreposicaoCaracterizacaoComplexo) {
                
                analiseGeoService.getComunicadoByIdAnaliseGeoComplexo(ctrl.idAnaliseGeo, sobreposicaoRestricao.id)
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

            }            
                    
        };

        ctrl.fechar = function() {
			ctrl.dismiss({$value: 'cancel'});
		};

    },
    templateUrl: 'components/modalOficioRestricao/modalOficioRestricao.html'
};

exports.directives.ModalOficioRestricao = ModalOficioRestricao;