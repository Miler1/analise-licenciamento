var FiltroProcessos = {

	bindings: {
        paginacao: '=',
		disabledFields: '<',
        atualizarLista: '=',
        atualizarPaginacao: '=',
        condicaoTramitacao: '<',
        pesquisarAoInicializar: '<'
	},

	controller: function(mensagem, processoService, municipioService, tipologiaService, atividadeService) {

		var ctrl = this;

        ctrl.openedAccordion = false;
        ctrl.filtro = {};
        ctrl.municipios = [];
        ctrl.tipologias = [];
        ctrl.atividades = [];


        function setFiltroControlePagina(){
            ctrl.filtro.paginaAtual = ctrl.paginacao.paginaAtual;
            ctrl.filtro.itensPorPagina = ctrl.paginacao.itensPorPagina;
        }

		this.pesquisar = function(){

            setFiltroControlePagina();

            processoService.getProcessos(ctrl.filtro)
                .then(function(response){
                     ctrl.atualizarLista(response.data);
                })
                .catch(function(){
                    mensagem.error("Ocorreu um erro ao buscar a lista de processos.");
                });

            processoService.getProcessosCount(ctrl.filtro)
                .then(function(response){
                     ctrl.atualizarPaginacao(response.data);
                })
                .catch(function(){
                    mensagem.error("Ocorreu um erro ao buscar a quantidade de processos.");
                });                
		};

        this.$postLink = function(){

            if (ctrl.condicaoTramitacao) {

                ctrl.filtro.idCondicaoTramitacao = ctrl.condicaoTramitacao;
            }

			municipioService.getMunicipiosByUf('PA').then(
				function(response){
					
					ctrl.municipios = response.data; 
				})
				.catch(function(){
					mensagem.warning('Não foi possível obter a lista de municípios.');
				});

            var params = {licenciamentoSimplificado: true};

            tipologiaService.getTipologias(params).then(
				function(response){
					
					ctrl.tipologias = response.data; 
				})
				.catch(function(){
					mensagem.warning('Não foi possível obter a lista de tipologia.');
				});              

            atividadeService.getAtividades(params).then(
				function(response){
					
					ctrl.atividades = response.data; 
				})
				.catch(function(){
					mensagem.warning('Não foi possível obter a lista de atividades.');
				});

            if (ctrl.pesquisarAoInicializar){

                ctrl.pesquisar()
            }
        };
	},

	templateUrl: 'components/filtroProcessos/filtroProcessos.html'

};

exports.directives.FiltroProcessos = FiltroProcessos;