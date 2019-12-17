var ModalRestricaoController = function (
    $uibModalInstance,
    $rootScope) {

    var modalCtrl = this;

    modalCtrl.restricao = { 
        texto: ''
    };

    modalCtrl.errors = {

        texto: false

    };

    modalCtrl.fechar = function () {

        $uibModalInstance.dismiss('cancel');

    };

    modalCtrl.init = function(){};

    var restricaoValida = function() {

        return !Object.keys(modalCtrl.errors).some(function(campo) {
            return modalCtrl.errors[campo];
        });

    };

    modalCtrl.concluir = function() {

        if(!restricaoValida()){

            return;

        } else{

            $rootScope.$broadcast('adicionarRestricao', modalCtrl.restricao);
            modalCtrl.fechar();

        }

    };

};

exports.controllers.ModalRestricaoController = ModalRestricaoController;
