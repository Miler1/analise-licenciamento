var FiltroProcessosManejo = {

	bindings: {
		paginacao: '=',
		atualizarLista: '=',
		atualizarPaginacao: '=',
		pesquisarAoInicializar: '<'
	},

	controller: function($scope, municipioService, atividadeManejoService, tipoLicencaManejoService, processoManejoService, condicaoService, $rootScope) {

		var ctrl = this;

		ctrl.openedAccordion = false;
		ctrl.municipios = [];
		ctrl.atividades = [];
		ctrl.manejosDigitais = [];
		ctrl.statusLicenca = [];


		municipioService.getMunicipiosByUf('AP').then(

			function(response){

				ctrl.municipios = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de municípios.');
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

				_.forEach(ctrl.manejosDigitais, function(manejoDigital) {

					if(manejoDigital.codigo === 'APAT') {

						ctrl.filtro.idManejoDigital = manejoDigital.id;
					}
				});

			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de tipos de licença do manejo.');
			});

		condicaoService.findManejo().then(

			function(response){

				ctrl.statusLicenca = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de status do protocolo do manejo.');
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
						mensagem.error("Ocorreu um erro ao buscar a lista de protocolos do manejo.");
				});

			processoManejoService.getProcessosCount(ctrl.filtro)
			.then(function(response){
					ctrl.atualizarPaginacao(response.data, ctrl.filtro.paginaAtual);
			})
			.catch(function(response){
				if(!!response.data.texto)
					mensagem.warning(response.data.texto);
				else
					mensagem.error("Ocorreu um erro ao buscar a quantidade de protocolos.");
			});

			$rootScope.$broadcast('atualizarContagemProcessosManejo');
		};


		function setFiltrosPadrao(){

			ctrl.filtro = {};
		}

		this.limparFiltros = function(){

			var idManejoDigital = ctrl.filtro.idManejoDigital;
			setFiltrosPadrao();
			ctrl.filtro.idManejoDigital = idManejoDigital;

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