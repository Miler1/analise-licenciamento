var ModalCondicionanteController = function (
    mensagem,
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

        if(!modalCtrl.condicionante.texto || modalCtrl.condicionante.texto === '' || modalCtrl.condicionante.texto === null){
			
			modalCtrl.errors.texto = true;

		} else {

			modalCtrl.errors.texto = false;

		}

		if(!modalCtrl.condicionante.prazo || modalCtrl.condicionante.prazo === '' || modalCtrl.condicionante.prazo === null){
			
			modalCtrl.errors.prazo = true;

		} else {

			modalCtrl.errors.prazo = false;

		}

        return !Object.keys(modalCtrl.errors).some(function(campo) {
            return modalCtrl.errors[campo];
        });

    };

    modalCtrl.concluir = function() {

        if(!condicionanteValida()){

            mensagem.error("Preencha os campos obrigatórios para prosseguir.", { referenceId: 5 });
            return;

        } else{

            $rootScope.$broadcast('adicionarCondicionante', modalCtrl.condicionante);
            modalCtrl.fechar();

        }

    };

};

exports.controllers.ModalCondicionanteController = ModalCondicionanteController;
