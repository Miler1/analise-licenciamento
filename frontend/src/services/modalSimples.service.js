var ModalSimples = function($uibModal) {

    this.abrirModal = abrirModal;

    function abrirModal(config) {

        var modalInstance = $uibModal.open({
            controller: 'modalSimplesController',
            controllerAs: 'modalCtrl',
            backdrop: 'static',
            templateUrl: 'components/modalSimples/modalSimples.html',
            resolve: {
                titulo: function() { return config.titulo;},
                conteudo: function() { return config.conteudo;},
                conteudoDestaque: function() { return config.conteudoDestaque || '';},
                labelBotaoConfirmar: function() { return config.labelBotaoConfirmar || 'Sim';},
                labelBotaoCancelar: function() { return config.labelBotaoCancelar || 'Não';}
            }
        });

        return modalInstance;

    }

};

exports.services.ModalSimples = ModalSimples;