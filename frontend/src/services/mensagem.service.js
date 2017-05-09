var Mensagem = function(growl, $anchorScroll) {

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

    function scrollTop() {
        $anchorScroll();
    }

};

exports.services.Mensagem = Mensagem;
