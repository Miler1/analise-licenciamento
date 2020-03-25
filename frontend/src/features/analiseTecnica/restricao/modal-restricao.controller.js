var ModalRestricaoController = function (
    mensagem,
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

        if(!modalCtrl.restricao.texto || modalCtrl.restricao.texto === '' || modalCtrl.restricao.texto === null){
			
			modalCtrl.errors.texto = true;

		} else {

			modalCtrl.errors.texto = false;

		}

        return !Object.keys(modalCtrl.errors).some(function(campo) {
            return modalCtrl.errors[campo];
        });

    };

    modalCtrl.concluir = function() {

        if(!restricaoValida()){
            
            mensagem.error("Preencha os campos obrigatórios para prosseguir.", { referenceId: 5 });
            return;

        } else{

            $rootScope.$broadcast('adicionarRestricao', modalCtrl.restricao);
            modalCtrl.fechar();
            

        }

    };

};

exports.controllers.ModalRestricaoController = ModalRestricaoController;
