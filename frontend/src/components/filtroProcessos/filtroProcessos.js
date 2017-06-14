var FiltroProcessos = {

	bindings: {
        paginacao: '=',
		disabledFields: '<',
        atualizarLista: '=',
        atualizarPaginacao: '=',
        condicaoTramitacao: '<',
        pesquisarAoInicializar: '<'
	},

	controller: function(mensagem, processoService, municipioService, tipologiaService, 
        atividadeService, $scope, condicaoService) {

		var ctrl = this;

        ctrl.disabledFilterFields = app.DISABLED_FILTER_FIELDS;

        ctrl.openedAccordion = false;
        ctrl.municipios = [];
        ctrl.tipologias = [];
        ctrl.atividades = [];
        ctrl.condicoes = [];

		this.pesquisar = function(pagina){

            if (ctrl.filtro.periodoInicial && ctrl.filtro.periodoInicial) {

                if(moment(ctrl.filtro.periodoInicial, 'DD/MM/YYYY').isAfter(moment())) {

                    mensagem.warning("Data de início do período não pode ser posterior a data atual.");
                    return;                    
                }

                var diff = moment(ctrl.filtro.periodoFinal, 'DD/MM/yyyy')
                    .diff(moment(ctrl.filtro.periodoInicial, 'DD/MM/yyyy'), 'days');

                if (diff < 0) {
                    mensagem.warning("O período inicial não pode ser maior que o período final.");
                    return;
                }                
            }

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
                     ctrl.atualizarPaginacao(response.data, ctrl.filtro.paginaAtual);
                })
                .catch(function(){
                    mensagem.error("Ocorreu um erro ao buscar a quantidade de processos.");
                });                
		};

        this.isDisabledFields = function(field) {

            return ctrl.disabledFields && ctrl.disabledFields.indexOf(field) !== -1;
        };

        function setFiltrosPadrao(){

            ctrl.filtro = {};

            if (ctrl.condicaoTramitacao) {

                ctrl.filtro.filtrarPorUsuario = true;
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

            if (!ctrl.isDisabledFields(ctrl.disabledFilterFields.SITUACAO)) {

                condicaoService.getCondicoes().then(
                    function(response){
                        
                        ctrl.condicoes = response.data; 
                    })
                    .catch(function(){
                        mensagem.warning('Não foi possível obter a lista de situações.');
                    });
            }

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