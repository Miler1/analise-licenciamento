var FiltroProcessos = {

	bindings: {
		paginacao: '=',
		disabledFields: '<',
		atualizarLista: '=',
		atualizarPaginacao: '=',
		condicaoTramitacao: '<',
		pesquisarAoInicializar: '<',
		isAnaliseJuridica: '<',
		isAnaliseTecnica: '<',
		isAnaliseGeo: '<',
		isAnaliseTecnicaOpcional: '<',
		isGerente: '<',
		onAfterUpdate: '=',
		isGerenteLogado: '<',
		pesquisarTodasGerencias: '<',
		tipoSetor: '<',
		filtrarPorUsuario: '<',
		consultarProcessos: '<'
	},

	controller: function(mensagem, processoService, municipioService, tipologiaService, 
		atividadeService, $scope, condicaoService, $rootScope, analistaService, setorService,
		TiposSetores, consultorService) {

		var ctrl = this;
		var caixaEntrada = false;
		var emAnalise = false;

		ctrl.disabledFilterFields = app.DISABLED_FILTER_FIELDS;
		ctrl.usuarioLogadoCodigoPerfil = $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo;
		ctrl.perfis = app.utils.Perfis;

		ctrl.openedAccordion = false;
		ctrl.municipios = [];
		ctrl.tipologias = [];
		ctrl.atividades = [];
		ctrl.analistasTecnicos = [];
		ctrl.analistasGeo = [];
		ctrl.condicoes = [];
		ctrl.Gerencias = [];
		ctrl.Coordenadorias = [];
		ctrl.Consultores = [];

		ctrl.maxDataInicio = new Date();

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

			if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_GEO ||
				$rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.GERENTE) {
				
				if(caixaEntrada && $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.GERENTE) {

					ctrl.filtro.listaIdCondicaoTramitacao = app.utils.CondicaoTramitacao.CAIXA_ENTRADA_GERENTE;
					ctrl.filtro.idCondicaoTramitacao = null;

				} else if (emAnalise && $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.GERENTE ){

					ctrl.filtro.listaIdCondicaoTramitacao = app.utils.CondicaoTramitacao.MENU_EM_ANALISE_GERENTE;
					ctrl.filtro.idCondicaoTramitacao = null;

				} else if(ctrl.filtro.idCondicaoTramitacao === 'ANALISE_GEO_FINALIZADA') {
					
					ctrl.filtro.listaIdCondicaoTramitacao = getCondicoesAnaliseGeoFinalizada();
					ctrl.filtro.idCondicaoTramitacao = null;
				
				} else if(caixaEntrada && $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_GEO) {

					ctrl.filtro.idCondicaoTramitacao = null;

				} else if(!caixaEntrada) {
					
					ctrl.filtro.listaIdCondicaoTramitacao = null;
				} 

			}

			ctrl.filtro.paginaAtual = pagina || ctrl.paginacao.paginaAtual;
			ctrl.filtro.itensPorPagina = ctrl.paginacao.itensPorPagina;

			var filtro = angular.copy(ctrl.filtro);
			
			processoService.getProcessos(filtro)
				.then(function(response){

					ctrl.atualizarLista(response.data);

					if (_.isFunction(ctrl.onAfterUpdate))
						ctrl.onAfterUpdate(filtro);

				})
				.catch(function(response){
					if(!!response.data.texto)
						mensagem.warning(response.data.texto);
					else
						mensagem.error("Ocorreu um erro ao buscar a lista de protocolos.");
				});

			processoService.getProcessosCount(filtro)
				.then(function(response){
					 ctrl.atualizarPaginacao(response.data, filtro.paginaAtual);
				})
				.catch(function(response){
					if(!!response.data.texto)
						mensagem.warning(response.data.texto);
					else
						mensagem.error("Ocorreu um erro ao buscar a quantidade de protocolos.");
				});

			if (!_.isEmpty(ctrl.filtro.listaIdCondicaoTramitacao)) {

				ctrl.filtro.idCondicaoTramitacao = 'ANALISE_GEO_FINALIZADA';
			}

			$rootScope.$broadcast('atualizarContagemProcessos');
		};

		this.isDisabledFields = function(field) {

			return ctrl.disabledFields && ctrl.disabledFields.indexOf(field) !== -1;
		};

		function setFiltrosPadrao(){

			ctrl.filtro = {};

			if (ctrl.filtrarPorUsuario) {
				ctrl.filtro.idUsuarioLogado = $rootScope.usuarioSessao.id;
			}

			if (_.isArray(ctrl.condicaoTramitacao)) {

				ctrl.filtro.filtrarPorUsuario = true;
				ctrl.filtro.idUsuarioLogado = $rootScope.usuarioSessao.id;
				ctrl.filtro.listaIdCondicaoTramitacao = ctrl.condicaoTramitacao;

				if(ctrl.condicaoTramitacao.includes(app.utils.CondicaoTramitacao.AGUARDANDO_VALIDACAO_GEO_PELO_GERENTE)){
					caixaEntrada = true;
				}else{
					emAnalise = true;
				}
				
			

			} else if (ctrl.condicaoTramitacao) {

				ctrl.filtro.filtrarPorUsuario = true;
				ctrl.filtro.idUsuarioLogado = $rootScope.usuarioSessao.id;
				ctrl.filtro.idCondicaoTramitacao = ctrl.condicaoTramitacao;
				caixaEntrada = false;
				emAnalise = false;
			}

			ctrl.filtro.isAnaliseJuridica = !!ctrl.isAnaliseJuridica;
			ctrl.filtro.isAnaliseTecnica = !!ctrl.isAnaliseTecnica;
			ctrl.filtro.isAnaliseTecnicaOpcional = !!ctrl.isAnaliseTecnicaOpcional;
			ctrl.filtro.isAnaliseGeo = !!ctrl.isAnaliseGeo;
			ctrl.filtro.isAnaliseGeoOpcional = !!ctrl.isAnaliseGeoOpcional;
			ctrl.filtro.isGerente = !!ctrl.isGerente;
			ctrl.filtro.isConsultarProcessos = !!ctrl.consultarProcessos;
		}

		this.limparFiltros = function(){

			setFiltrosPadrao();

			$('#cpfCnpjEmpreendimento').val('');

			this.pesquisar(1);
		};

		this.$postLink = function(){

			setFiltrosPadrao();

			municipioService.getMunicipiosByUf('AM').then(
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

			if(ctrl.usuarioLogadoCodigoPerfil !== ctrl.perfis.ANALISTA_GEO && ctrl.usuarioLogadoCodigoPerfil !== ctrl.perfis.ANALISTA_TECNICO){
				
				if (!ctrl.isDisabledFields(ctrl.disabledFilterFields.ANALISTA_TECNICO)){
					
					if(ctrl.isAnaliseTecnicaOpcional){
						
						analistaService.getAnalistasTecnicos()
							.then(function(response){

								ctrl.analistasTecnicos = response.data;
							})
							.catch(function(){
								mensagem.warning('Não foi possível obter a lista de analistas técnicos.');
							});
					}
					else{
						analistaService.getAnalistasTecnicosByPerfil(ctrl.isGerenteLogado)
							.then(function(response){

								ctrl.analistasTecnicos = response.data;
							})
							.catch(function(){
								mensagem.warning('Não foi possível obter a lista de analistas técnicos.');
							});
					}
				}

				if (!ctrl.isDisabledFields(ctrl.disabledFilterFields.ANALISTA_GERENTE) && !ctrl.isDisabledFields(ctrl.disabledFilterFields.ANALISTA_GEO)){
					if(ctrl.isAnaliseTecnicaOpcional){
						analistaService.getAnalistasGeo()
							.then(function(response){

								ctrl.analistasGeo = response.data;
							})
							.catch(function(){
								mensagem.warning('Não foi possível obter a lista de analistas GEO.');
							});
					}
					else{
						analistaService.getAnalistasGeoByPerfil(ctrl.isGerenteLogado)
							.then(function(response){

								ctrl.analistasGeo = response.data;
							})
							.catch(function(){
								mensagem.warning('Não foi possível obter a lista de analistas GEO.');
							});

					}
				}

			}

			if (!ctrl.isDisabledFields(ctrl.disabledFilterFields.SITUACAO)) {

				condicaoService.getCondicoes().then(
					function(response){
						
						ctrl.condicoes = response.data;

						if($rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.ANALISTA_GEO || 
							$rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === app.utils.Perfis.GERENTE ) {

							ctrl.condicoes.push({
								idCondicao: 'ANALISE_GEO_FINALIZADA',
								nomeCondicao: 'Analise GEO finalizada'
							});
						}
					})
					.catch(function(){
						mensagem.warning('Não foi possível obter a lista de situações.');
					});
			}

			if (!ctrl.isDisabledFields(ctrl.disabledFilterFields.GERENCIA)){

				if(!ctrl.pesquisarTodasGerencias) {
					/**
					 * Nível 1 corresponde aos filhos e nível 2 aos netos na hieraquia. 
					 * Neste caso, colocamos esta verificação, pois se for o aprovador
					 * as gerências pertencentes a ele estão dois níveis abaixo. Já se
					 * for o coordenador estará um nível abaixo.
					 */
					var nivel = $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo === $rootScope.perfis.APROVADOR ? 2 : 1;

					setorService.getSetoresByNivel(nivel)
						.then(function(response){

							ctrl.Gerencias = response.data;
						})
						.catch(function(response){

							if(response.data && response.data.texto) {

								mensagem.warning(response.data.texto);

							} else {

								mensagem.warning('Não foi possível obter a lista de setores.');
							}
						});
				
				} else {

					setorService.getSetoresPorTipo(ctrl.tipoSetor)
						.then(function(response){

							ctrl.Gerencias = response.data;
						})
						.catch(function(response){

							if(response.data && response.data.texto) {

								mensagem.warning(response.data.texto);

							} else {

								mensagem.warning('Não foi possível obter a lista de setores.');
							}
						});
				}
			}

			if (!ctrl.isDisabledFields(ctrl.disabledFilterFields.COORDENADORIA)){
				/**
				 * Nível 1 corresponde aos filhos na hieraquia. Ex. Se o usuário logado for Diretor,
				 * então os setores que virão serão as coordenadorias
				 */
				setorService.getSetoresByNivel(1)
					.then(function(response){

						ctrl.Coordenadorias = response.data;
					})
					.catch(function(response){

						if(response.data && response.data.texto) {

							mensagem.warning(response.data.texto);

						} else {

							mensagem.warning('Não foi possível obter a lista de setores.');
						}
					});
			}

			if (!ctrl.isDisabledFields(ctrl.disabledFilterFields.CONSULTOR_JURIDICO)){

				consultorService.getConsultoresJuridicos()
					.then(function(response){

						ctrl.consultoresJuridicos = response.data;
					})
					.catch(function(){
						mensagem.warning('Não foi possível obter a lista de consultores jurídicos.');
					});
			}

			if (ctrl.filtrarPorUsuario) {
				ctrl.filtro.idUsuarioLogado = $rootScope.usuarioSessao.id;
			}

			if (ctrl.pesquisarAoInicializar){

				ctrl.pesquisar(1);
			}
		};

		function getCondicoesAnaliseGeoFinalizada() {
			return [
				app.utils.CondicaoTramitacao.AGUARDANDO_VALIDACAO_GEO_PELO_GERENTE,
				app.utils.CondicaoTramitacao.AGUARDANDO_VALIDACAO_GERENTE,
				app.utils.CondicaoTramitacao.EM_ANALISE_GERENTE,
				app.utils.CondicaoTramitacao.AGUARDANDO_RESPOSTA_COMUNICADO,
				app.utils.CondicaoTramitacao.AGUARDANDO_VALIDACAO_TECNICA_PELO_GERENTE,
				app.utils.CondicaoTramitacao.AGUARDANDO_ANALISE_TECNICA,
				app.utils.CondicaoTramitacao.EM_ANALISE_TECNICA,
				app.utils.CondicaoTramitacao.AGUARDANDO_RESPOSTA_JURIDICO
			];
		}

		$scope.$on('pesquisarProcessos', function(event){

			ctrl.pesquisar();
		});

		$rootScope.$on('rootPesquisarProcessos', function(event){

			ctrl.pesquisar();
		});

	},

	templateUrl: 'components/filtroProcessos/filtroProcessos.html'

};

exports.directives.FiltroProcessos = FiltroProcessos;