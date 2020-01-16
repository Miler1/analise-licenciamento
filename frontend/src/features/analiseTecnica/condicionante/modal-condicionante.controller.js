var ModalCondicionanteController = function (
    $uibModalInstance,
    $rootScope) {

    var modalCtrl = this;

    modalCtrl.condicionante = { 
        texto: '',
        prazo: null
    };

    modalCtrl.errors = {

        texto: false,
        prazo: false

    };

    modalCtrl.fechar = function () {

        $uibModalInstance.dismiss('cancel');

    };

    modalCtrl.init = function(){};

    var condicionanteValida = function() {

        return !Object.keys(modalCtrl.errors).some(function(campo) {
            return modalCtrl.errors[campo];
        });

    };

    modalCtrl.concluir = function() {

        if(!condicionanteValida()){

            return;

        } else{

            $rootScope.$broadcast('adicionarCondicionante', modalCtrl.condicionante);
            modalCtrl.fechar();

        }

    };

};

exports.controllers.ModalCondicionanteController = ModalCondicionanteController;
