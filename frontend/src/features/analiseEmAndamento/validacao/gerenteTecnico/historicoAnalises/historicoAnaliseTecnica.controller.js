var HistoricoAnaliseTecnicaCtrl = function( processo,
                                            parecer, 
                                            $uibModalInstance, 
                                            analiseTecnica, 
                                            documentoService ) {


    var historicoAnaliseTecnicaCtrl = this;

    historicoAnaliseTecnicaCtrl.processo = processo;
    historicoAnaliseTecnicaCtrl.analiseTecnica = analiseTecnica;
    historicoAnaliseTecnicaCtrl.parecer = parecer;
    historicoAnaliseTecnicaCtrl.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

    historicoAnaliseTecnicaCtrl.baixarDocumento = function (analiseTecnica) {
            
        documentoService.downloadParecerByIdAnaliseTecnica(analiseTecnica.id);
        
    };

    historicoAnaliseTecnicaCtrl.fechar =function() {

        $uibModalInstance.dismiss('cancel');
    };

};

exports.controllers.HistoricoAnaliseTecnicaCtrl = HistoricoAnaliseTecnicaCtrl;
