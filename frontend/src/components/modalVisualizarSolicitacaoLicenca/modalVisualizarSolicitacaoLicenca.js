var ModalVisualizarSolicitacaoLicenca = {

    bindings: {

        resolve: '<',
        close: '&',
        dismiss: '&'
    },

    controller: function() {
        
        var ctrl = this;
        ctrl.titulo = "PROTOCOLO";
        ctrl.possuiValidade = null;
        ctrl.tipologias = app.utils.Tipologia;

        ctrl.$onInit =  function() {

            ctrl.parecerTecnico = ctrl.resolve.parecerTecnico;
            ctrl.dadosProcesso = ctrl.resolve.dadosProcesso;
            ctrl.analiseTecnica = ctrl.resolve.analiseTecnica;
            ctrl.possuiValidade = (ctrl.analiseTecnica.analise.processo.caracterizacao.atividadesCaracterizacao[0].atividade.tipologia.codigo === ctrl.tipologias.ID_AQUICULTURA) ? false : true;
            console.log("possuiValidade",ctrl.possuiValidade);
            console.log("tipologiaAtividade",ctrl.analiseTecnica.analise.processo.caracterizacao.atividadesCaracterizacao[0].atividade.tipologia.codigo );
            console.log("utilsTipologia",ctrl.tipologias.ID_AQUICULTURA);
            ctrl.tiposResultadoAnalise = app.utils.TiposResultadoAnalise;
        };

        ctrl.fechar = function() {
			ctrl.dismiss({$value: 'cancel'});
		};

    },
    templateUrl: 'components/modalVisualizarSolicitacaoLicenca/modalVisualizarSolicitacaoLicenca.html'
};

exports.directives.ModalVisualizarSolicitacaoLicenca = ModalVisualizarSolicitacaoLicenca;