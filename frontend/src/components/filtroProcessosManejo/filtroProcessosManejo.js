var FiltroProcessosManejo = {

	bindings: {
		paginacao: '=',
		atualizarLista: '=',
		atualizarPaginacao: '=',
		pesquisarAoInicializar: '<'
	},

	controller: function($scope, municipioService, tipologiaManejoService, atividadeManejoService, tipoLicencaManejoService, processoManejoService, condicaoService, $rootScope) {

		var ctrl = this;

		ctrl.openedAccordion = false;
		ctrl.municipios = [];
		ctrl.tipologias = [];
		ctrl.atividades = [];
		ctrl.manejosDigitais = [];
		ctrl.statusLicenca = [];


		municipioService.getMunicipiosByUf('PA').then(

			function(response){

				ctrl.municipios = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de municípios.');
			});

		tipologiaManejoService.findAll().then(

			function(response){

				ctrl.tipologias = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de tipologias do manejo.');
			});

		atividadeManejoService.findAll().then(

			function(response){

				ctrl.atividades = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de atividades do manejo.');
			});

		tipoLicencaManejoService.findAll().then(

			function(response){

				ctrl.manejosDigitais = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de tipos de licença do manejo.');
			});

		condicaoService.findManejo().then(

			function(response){

				ctrl.statusLicenca = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de status do processo do manejo.');
			});

		this.pesquisar = function(pagina){

			ctrl.filtro.paginaAtual = pagina || ctrl.paginacao.paginaAtual;
			ctrl.filtro.itensPorPagina = ctrl.paginacao.itensPorPagina;

			processoManejoService.getProcessos(ctrl.filtro)
				.then(function(response){

					ctrl.atualizarLista(response.data);

					if (_.isFunction(ctrl.onAfterUpdate))
						ctrl.onAfterUpdate(ctrl.filtro);

				})
				.catch(function(response){
					if(!!response.data.texto)
						mensagem.warning(response.data.texto);
					else
						mensagem.error("Ocorreu um erro ao buscar a lista de processos do manejo.");
				});

			processoManejoService.getProcessosCount(ctrl.filtro)
			.then(function(response){
					ctrl.atualizarPaginacao(response.data, ctrl.filtro.paginaAtual);
			})
			.catch(function(response){
				if(!!response.data.texto)
					mensagem.warning(response.data.texto);
				else
					mensagem.error("Ocorreu um erro ao buscar a quantidade de processos.");
			});

			$rootScope.$broadcast('atualizarContagemProcessosManejo');
		};


		function setFiltrosPadrao(){

			ctrl.filtro = {};
		}

		this.limparFiltros = function(){

			setFiltrosPadrao();

			$('#cpfCnpjEmpreendimento').val('');

			this.pesquisar(1);
		};

		this.$postLink = function(){

			setFiltrosPadrao();

			if (ctrl.pesquisarAoInicializar){

				ctrl.pesquisar(1);
			}
		};

		$scope.$on('pesquisarProcessosManejo', function(event){

			ctrl.pesquisar();
		});
	},

	templateUrl: 'components/filtroProcessosManejo/filtroProcessosManejo.html'

};

exports.directives.FiltroProcessosManejo = FiltroProcessosManejo;