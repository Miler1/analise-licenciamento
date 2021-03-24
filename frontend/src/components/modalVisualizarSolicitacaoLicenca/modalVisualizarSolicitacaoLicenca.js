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
            ctrl.processo = ctrl.resolve.processo;
            ctrl.possuiValidade = (ctrl.analiseTecnica.analise.processo.caracterizacao.atividadesCaracterizacao[0].atividade.tipologia.codigo === ctrl.tipologias.ID_AQUICULTURA) ? false : true;
            ctrl.tiposResultadoAnalise = app.utils.TiposResultadoAnalise;
            ctrl.cpfCnpj = (ctrl.processo.empreendimento.cpfCnpj === null || ctrl.processo.empreendimento.cpfCnpj === undefined) ? ctrl.processo.empreendimento.cpfCnpj : ctrl.processo.empreendimento.cpfCnpj;

        };

        ctrl.fechar = function() {
			ctrl.dismiss({$value: 'cancel'});
		};

    },
    templateUrl: 'components/modalVisualizarSolicitacaoLicenca/modalVisualizarSolicitacaoLicenca.html'
};

exports.directives.ModalVisualizarSolicitacaoLicenca = ModalVisualizarSolicitacaoLicenca;