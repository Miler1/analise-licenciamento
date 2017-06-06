var FiltroProcessos = {

	bindings: {
        paginacao: '=',
		disabledFields: '<',
        atualizarLista: '=',
        atualizarPaginacao: '=',
        condicaoTramitacao: '<',
        pesquisarAoInicializar: '<'
	},

	controller: function(mensagem, processoService, municipioService, tipologiaService, atividadeService, $scope) {

		var ctrl = this;

        ctrl.openedAccordion = false;
        ctrl.municipios = [];
        ctrl.tipologias = [];
        ctrl.atividades = [];

		this.pesquisar = function(pagina){

            ctrl.filtro.paginaAtual = pagina || ctrl.paginacao.paginaAtual;
            ctrl.filtro.itensPorPagina = ctrl.paginacao.itensPorPagina;

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

        function setFiltrosPadrao(){

            ctrl.filtro = {};

            if (ctrl.condicaoTramitacao) {

                ctrl.filtro.idCondicaoTramitacao = ctrl.condicaoTramitacao;
            }
        }

        this.limparFiltros = function(){

            setFiltrosPadrao();

            $('#cpfCnpjEmpreendimento').val('');

            this.pesquisar(1);
        };

        this.$postLink = function(){

            setFiltrosPadrao();

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

                ctrl.pesquisar(1);
            }
        };

		$scope.$on('pesquisarProcessos', function(event){

			ctrl.pesquisar();
		});        
	},

	templateUrl: 'components/filtroProcessos/filtroProcessos.html'

};

exports.directives.FiltroProcessos = FiltroProcessos;