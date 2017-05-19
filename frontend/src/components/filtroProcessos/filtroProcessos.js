var FiltroProcessos = {

	bindings: {
		processos: '=',
        paginacao: '=',
		disabledFields: '<',
        atualizarLista: '='
	},

	controller: function(processoService, mensagem) {

		var ctrl = this;

        ctrl.filtro = {};

		this.pesquisar = function(){

            processoService.getProcessoslist(filtro, paginacao.paginaAtual, paginacao.itensPorPagina)
                .then(function(response){
                     atualizarLista(response.data);
                })
                .catch(function(){
                    mensagem.error("Ocorreu um erro ao buscar a lista de processos.");
                });
		};
	},

	templateUrl: 'components/filtroProcessos/filtroProcessos.html'

};

exports.directives.FiltroProcessos = FiltroProcessos;