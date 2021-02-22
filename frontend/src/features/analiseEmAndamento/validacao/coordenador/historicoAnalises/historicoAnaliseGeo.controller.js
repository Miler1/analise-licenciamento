var HistoricoAnaliseGeoCtrl = function( processo,
                                        parecer, 
                                        $uibModalInstance,
                                        documentoService,
                                        notificacaoService,
                                        analiseGeo ) {


    var historicoAnaliseGeoCtrl = this;

    historicoAnaliseGeoCtrl.processo = processo;
    historicoAnaliseGeoCtrl.analiseGeo = analiseGeo;
    historicoAnaliseGeoCtrl.parecer = parecer;
    historicoAnaliseGeoCtrl.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    historicoAnaliseGeoCtrl.documentos = [];
    historicoAnaliseGeoCtrl.notificacoes = [];

    historicoAnaliseGeoCtrl.setDocumentos = function() {

        notificacaoService.findByIdParecer(historicoAnaliseGeoCtrl.parecer.id).then(function(response){

            historicoAnaliseGeoCtrl.notificacoes = response.data;

            if (historicoAnaliseGeoCtrl.parecer !== null && historicoAnaliseGeoCtrl.parecer !== undefined) {

                if (historicoAnaliseGeoCtrl.parecer.documentoParecer !== null) {
    
                    historicoAnaliseGeoCtrl.documentos.push(historicoAnaliseGeoCtrl.parecer.documentoParecer);
                }
    
                if (historicoAnaliseGeoCtrl.parecer.cartaImagem !== null) {
    
                    historicoAnaliseGeoCtrl.documentos.push(historicoAnaliseGeoCtrl.parecer.cartaImagem);
    
                }
    
            }
            
            if(historicoAnaliseGeoCtrl.notificacoes[0]){
                
                _.forEach(historicoAnaliseGeoCtrl.notificacoes[0].documentosNotificacaoTecnica, function(documento){
                
                    historicoAnaliseGeoCtrl.documentos.push(documento);
                    
                });
            }
        });

    };

    historicoAnaliseGeoCtrl.downloadDocumentos = function (id) {

		documentoService.downloadById(id);

	};
    
    historicoAnaliseGeoCtrl.setDocumentos();

    historicoAnaliseGeoCtrl.fechar =function() {
      
        $uibModalInstance.dismiss('cancel');
    };

};

exports.controllers.HistoricoAnaliseGeoCtrl = HistoricoAnaliseGeoCtrl;
