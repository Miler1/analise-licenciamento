var HistoricoAnaliseTecnicaCtrl = function( processo,
                                            parecer, 
                                            $uibModalInstance, 
                                            notificacaoService,
                                            analiseTecnica, 
                                            documentoService ) {


    var historicoAnaliseTecnicaCtrl = this;

    historicoAnaliseTecnicaCtrl.processo = processo;
    historicoAnaliseTecnicaCtrl.analiseTecnica = analiseTecnica;
    historicoAnaliseTecnicaCtrl.parecer = parecer;
    historicoAnaliseTecnicaCtrl.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    historicoAnaliseTecnicaCtrl.tipoDocumento = app.utils.TiposDocumentosAnalise;

    historicoAnaliseTecnicaCtrl.baixarDocumento = function (analiseTecnica, tipoDocumento) {

        if ( tipoDocumento === historicoAnaliseTecnicaCtrl.tipoDocumento.PARECER_ANALISE_TECNICA ) {

            documentoService.downloadParecerByIdAnaliseTecnica(analiseTecnica.id);

        }else if ( tipoDocumento === historicoAnaliseTecnicaCtrl.tipoDocumento.DOCUMENTO_RELATORIO_TECNICO_VISTORIA ) {
            
            documentoService.downloadRTVByIdAnaliseTecnica(analiseTecnica.id);

        }else if ( tipoDocumento === historicoAnaliseTecnicaCtrl.tipoDocumento.DOCUMENTO_MINUTA ) {

            documentoService.downloadMinutaByIdAnaliseTecnica(analiseTecnica.id);

        }
                    
    };

    notificacaoService.findByIdParecerTecnico(historicoAnaliseTecnicaCtrl.parecer.id).then(function(response){

        historicoAnaliseTecnicaCtrl.notificacoes = response.data;


    });

    historicoAnaliseTecnicaCtrl.downloadDocumentos = function (id) {

		documentoService.downloadById(id);

	};

    historicoAnaliseTecnicaCtrl.fechar =function() {

        $uibModalInstance.dismiss('cancel');
    };

};

exports.controllers.HistoricoAnaliseTecnicaCtrl = HistoricoAnaliseTecnicaCtrl;
