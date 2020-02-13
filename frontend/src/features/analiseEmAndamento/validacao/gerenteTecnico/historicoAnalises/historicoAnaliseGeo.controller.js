var HistoricoAnaliseGeoCtrl = function( processo,
                                        parecer, 
                                        $uibModalInstance, 
                                        analiseGeo ) {


    var historicoAnaliseGeoCtrl = this;

    historicoAnaliseGeoCtrl.processo = processo;
    historicoAnaliseGeoCtrl.analiseGeo = analiseGeo;
    historicoAnaliseGeoCtrl.parecer = parecer;
    historicoAnaliseGeoCtrl.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

    console.log(historicoAnaliseGeoCtrl.parecer);
    historicoAnaliseGeoCtrl.fechar =function() {

        $uibModalInstance.dismiss('cancel');
    };

};

exports.controllers.HistoricoAnaliseGeoCtrl = HistoricoAnaliseGeoCtrl;
