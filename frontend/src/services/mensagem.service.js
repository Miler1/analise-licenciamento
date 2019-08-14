var Mensagem = function(growl, $anchorScroll, $rootScope) {

    this.success = criarFuncaoGrowl('success');
    this.warning = criarFuncaoGrowl('warning');
    this.info = criarFuncaoGrowl('info');
    this.error = criarFuncaoGrowl('error');

    function criarFuncaoGrowl(tipo) {

        return function(mensagem, config){
            exibirMensagem(tipo, mensagem, config || {});
        };

    }

    function exibirMensagem(tipo, mensagem, config){

        /* Faz o scroll para o topo da tela, a menos que se informe o contrário */
        if(!config.dontScroll){
            scrollTop();
        }

        return growl[tipo](mensagem, config);

    }

    this.setMensagemProximaTela = function(tipo, mensagem){

        $rootScope.msg = {
            texto: mensagem,
            tipo: tipo || 'warning'
        };

    };

    this.verificaMensagemGlobal = function(){

        if ($rootScope.msg !== undefined){
            growl[$rootScope.msg.tipo]($rootScope.msg.texto, {});
            $rootScope.msg = undefined;
        }

    };

    function scrollTop() {
        $anchorScroll();
    }

};

exports.services.Mensagem = Mensagem;
