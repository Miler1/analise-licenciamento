var AnaliseGeoController = function($injector, $rootScope, $scope, $timeout, $uibModal, analiseGeo, $anchorScroll,$location, analiseGeoService, restricoes,documentoService ,idAnaliseGeo,inconsistenciaService,processoService, empreendimentoService, uploadService,mensagem, documentoAnaliseService, tiposSobreposicaoService, parecerAnalistaGeoService) {

	var idMapa = 'mapa-restricoes',
	mapa,
	layersRestricoes = $scope.layersRestricoes = {},
	meusDados,
	imovel,
	colors = ['#ef5350', '#EC407A', '#AB47BC', '#7E57C2', '#5C6BC0', '#42A5F5', '#29B6F6', '#26C6DA', '#26A69A',
		'#66BB6A', '#9CCC65', '#D4E157', '#FFEE58', '#FFCA28', '#FFA726', '#FF7043'];

	var ctrl = this;
	ctrl.restricoes = restricoes;
	ctrl.idAnaliseGeo= idAnaliseGeo;
	ctrl.analiseGeo = angular.copy(analiseGeo);
	ctrl.tipoResultadoAnalise = {id:null};
	ctrl.categoria = app.utils.Inconsistencia;
	ctrl.orgaos = app.utils.Orgao;
	ctrl.camadas = [];
	ctrl.estiloMapa = app.utils.EstiloMapa;
	ctrl.camadasSobreposicao = app.utils.CamadaSobreposicao;
	ctrl.controleVisualizacao = null;
	ctrl.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;
	ctrl.TiposSobreposicao = [];
	ctrl.numPoints = 0;
	ctrl.LegendasTipoSobreposicao = app.utils.LegendasTipoSobreposicao;
	ctrl.dadosRestricoesProjeto = [];
	ctrl.listaInconsistencias = [];
	ctrl.titulo = 'PARECER GEO';
	ctrl.notificacao = {};
	ctrl.notificacao.documentacao = null;
	ctrl.notificacao.retificacaoEmpreendimento = null;
	ctrl.notificacao.retificacaoSolicitacao = null;
	ctrl.notificacao.retificacaoSolicitacaoComGeo = null;
	ctrl.notificacao.prazoNotificacao = null;
	ctrl.errors = {
		isPdf: false
	};
	ctrl.tiposUpload = app.utils.TiposUpload;
	ctrl.labelDadosProjeto = '';
	ctrl.openedAccordionEmpreendimento = false;
	ctrl.openedAccordionDadosComplexo = false;
	ctrl.parecer = {
		situacaoFundiaria: null,
		analiseTemporal: null,
		conclusao: null,
		parecer: null,
		documentos: []
	};
	ctrl.despacho = null;
	ctrl.errors = {
		conclusao: false,
		parecer: false,
		resultadoAnalise: false,
		prazoNotificacao: false,
		docAnaliseTemporal:false,
		analiseTemporal: false,
		atendimento: false
	};
	ctrl.sobreposicoesEmpreendimento = [];

	ctrl.indexAtividade = null;
	ctrl.indexAtividadeParametro = null;

	ctrl.parametroAreaUtil = {
		atividade: []
};

	var getLayer = function(descricao){

		var layerControle;
		if(['Imóvel Análisado','Empreendimento'].indexOf(descricao) === -1){

			if(!layersRestricoes[descricao]){
				layersRestricoes[descricao] = L.layerGroup();
				layersRestricoes[descricao].addTo(mapa);
				layersRestricoes[descricao].color = colors.pop();
			}

			layerControle = layersRestricoes[descricao];

		}else {

			layerControle = meusDados;

		}



		return layerControle;

	};

	$injector.invoke(exports.controllers.PainelMapaController, this,
		{
			$scope: $scope,
			$timeout: $timeout
		}
	);

	ctrl.init('mapa-localizacao-empreendimento', true, true);

	ctrl.controleVisualizacao = "ETAPA_LOCALIZACAO_GEOGRAFICA";
	function piscarFeature(layer, color) {

		setTimeout(function(){

			var pisca = true;

			var interval = setInterval(function () {

				if (layer.setOpacity) {

					layer.setOpacity(pisca ? 0 : 1);

				} else {

					if (pisca) {
						layer.setStyle({
							color: 'red'
						});
					} else {
						layer.setStyle({
							color: color
						});
					}

				}

				pisca = !pisca;

			}, 200);

			setTimeout(function () {

				clearInterval(interval);

			}, 2000);
			mapa.off('moveend', funcaoAnterior);

			}, 350);

	}

	var funcaoAnterior;
	$scope.visualizarLayer = function(layer, color){

		if(!color && layer.options){

			color = layer.options.color;
		}

		funcaoAnterior = piscarFeature.bind(this, layer, color);

		mapa.on('moveend', funcaoAnterior);

		$('html, body').animate({

			scrollTop: $('#' + idMapa).offset().top - 50

		}, 600, function(){

			if(layer.getBounds){

				mapa.flyToBounds(layer.getBounds());

				layer.bringToFront();


			} else {

				mapa.flyTo(layer.getLatLng());

			}


		});

	};

	$scope.mostrarLayer = function(layer){
		layer.escondida = false;

		if(layer.setStyle){

			layer.setStyle({
				opacity: 1,
				fillOpacity: 0.1
			});

		}else{

			layer.setOpacity(1);

		}

	};
	$scope.esconderLayer = function(layer){

		layer.escondida = true;

		if(layer.setStyle){

			layer.setStyle({
				opacity: 0,
				fillOpacity: 0
			});

		}else{

			layer.setOpacity(0);

		}

	};

	$scope.tratarRestricao = function(restricaoProperties) {

		if(restricaoProperties.descricao === "Imóvel Sobreposto") {

			var n =  Math.log(restricaoProperties.restricao) / Math.LN10;
			var numCasas = 2-n;

			if(numCasas < 0)
				numCasas = 0;

			return restricaoProperties.restricao.toFixed(Math.ceil(numCasas)) + " %";

		} else {

			if(restricaoProperties.restricao > 1000) {
				var km = restricaoProperties.restricao / 1000;
				return km.toFixed(1) + " km";
			} else {
				return restricaoProperties.restricao.toFixed(1) + " m";
			}

		}

	};

	$scope.adicionarRestricao = function(restricao){

		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: './features/analiseGeo/analiseGeo/modal-parecer-restricao.html',
			controller: 'modalParecerRestricaoController',
			controllerAs: 'modalCtrl',
			size: 'lg',
			resolve: {
				analiseGeo: function () {
					return $scope.analiseGeo;
				},
				restricao: function () {
					return restricao;
				},
				restricaoGeo: function() {
					return angular.copy(restricao);
				},
				empreendimentoGeo: function () {
					var layerSelecionada;
					_.forEach($scope.meusDados.getLayers(), function(layer) {
						if(layer.feature.id === "meu-empreendimento")
							layerSelecionada = angular.copy(layer);
					});
					return layerSelecionada;
				},
				imovel: function () {
					var layerSelecionada;
					_.forEach($scope.meusDados.getLayers(), function(layer) {
						if(layer.feature.id === "meu-imovel")
							layerSelecionada = angular.copy(layer);
					});
					return layerSelecionada;
				}
			}
		});

		modalInstance.result.then(function(){},function(){});

	};

	var estiloPoligono = {
		color: colors.pop(),
		opacity: 1,
		weight: 3,
		fillOpacity: 0.1
	};

	var inserirGeometriaMunicipio = function(geo, mapa) {

		if(geo && mapa) {

			var objectGeo = JSON.parse(geo);

			var layer = L.geoJson({
				'type': 'Feature',
				'id': 'municipio-empreendimento',
				'geometry': objectGeo
			}, estiloPoligono);

			$scope.municipioGeormetria = layer.getLayers()[0];

		}

	};

	this.visualizarProcesso = function (processo) {

		processo.idProcesso = processo.id;
		processo.numeroProcesso = processo.numero;
		processo.denominacaoEmpreendimento = processo.empreendimento.denominacao;
		processo.cpfEmpreendimento = processo.empreendimento.cpfCnpj;

		processoService.visualizarProcesso(processo);

	};

	var esconderCamada = function(camada) {

		var tipoGeometria = JSON.parse(camada.geometria).type.toLowerCase();

		if(tipoGeometria === 'point') {

			$scope.$emit('mapa:remover-geometria-base-cluster', camada);

		} else {

			$scope.$emit('mapa:remover-geometria-base', camada);

		}

	};

	var mostrarCamada = function(camada) {

		var tipoGeometria = JSON.parse(camada.geometria).type.toLowerCase();

		if(tipoGeometria === 'point') {

			adicionarPointNoCluster(camada);

		} else {

			adicionarGeometriaNoMapa(camada, true);

		}

	};

	this.controlaExibicaoCamadas = function(camada) {

		if (camada.visivel) {

			esconderCamada(camada);

		} else {

			mostrarCamada(camada);

		}

	};

	this.controlaExibicaoLayer = function(tipoSobreposicao) {

		$scope.$emit('mapa:controla-exibicao-wmslayer', tipoSobreposicao);

	};

	this.getCamadasSobreposicoes = function() {

		ctrl.TiposSobreposicao.forEach(function (tipo) {
			tipo.legenda = ctrl.LegendasTipoSobreposicao[tipo.codigo];
			adicionarWmsLayerNoMapa(tipo);
		});
	};

	this.controlaCentralizacaoCamadas = function (camada) {

		$scope.$emit('mapa:centralizar-camada', camada.geometria);
	};

	function adicionarGeometriaNoMapa (camada, disable) {

		camada.visivel = true;
		camada.color = ctrl.estiloMapa[camada.tipo] != undefined ? ctrl.estiloMapa[camada.tipo].color : camada.estilo.color;

		$scope.$emit('mapa:adicionar-geometria-base', {
			geometria: JSON.parse(camada.geometria),
			tipo: camada.tipo,
			item: camada.item,
			estilo: {
				style: ctrl.estiloMapa[camada.tipo] || camada.estilo
			},
			popupText: camada.item,
			area: camada.area,
			cpfCnpjAreaSobreposicao: camada.cpfCnpjAreaSobreposicao,
			dataAreaSobreposicao: camada.dataAreaSobreposicao,
			nomeAreaSobreposicao: camada.nomeAreaSobreposicao,
			disableCentralizarGeometrias:disable,
			numPoints: ctrl.numPoints
		});
	}

	function adicionarPointNoCluster (camada) {

		camada.visivel = true;
		$scope.$emit('mapa:adicionar-geometria-base-cluster', camada);

	}

	function adicionarWmsLayerNoMapa (tipoSobreposicao) {

		tipoSobreposicao.visivel = false;
		tipoSobreposicao.color = ctrl.estiloMapa.SOBREPOSICAO.color;
		tipoSobreposicao.nomeLayer = ctrl.camadasSobreposicao[tipoSobreposicao.codigo];
		tipoSobreposicao.tipo = tipoSobreposicao.codigo;
		tipoSobreposicao.estilo = {style: ctrl.estiloMapa.SOBREPOSICAO};
		tipoSobreposicao.popupText = tipoSobreposicao.nome;
		tipoSobreposicao.disableCentralizarGeometrias=false;

		$scope.$emit('mapa:adicionar-wmslayer-mapa', tipoSobreposicao, true);

	}

	function contaQuantidadeCamadasPoint (camadas) {

		return camadas.filter(function(camada) {

			return JSON.parse(camada.geometria).type.toLowerCase() === 'point';

		}).length;

	}

	function getRandomColor() {
		var letters = '0123456789ABCDEF';
		var color = '#';
		for (var i = 0; i < 6; i++) {
			color += letters[Math.floor(Math.random() * 16)];
		}
		return color;
	}

	function cpfCnpjMask(campoTexto) {
		if (campoTexto.length <= 11) {
			return mascaraCpf(campoTexto);
		} else {
			return mascaraCnpj(campoTexto);
		}
	}

	function mascaraCpf(valor) {
		return valor.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/g,"\$1.\$2.\$3\-\$4");
	}

	function mascaraCnpj(valor) {
		return valor.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/g,"\$1.\$2.\$3\/\$4\-\$5");
	}

	this.init = function() {

		$scope.analiseGeo = analiseGeo;

		_.forEach(restricoes.features, function(restricao) {
			var parecerTecnicoRestricao = _.find($scope.analiseGeo.pareceresTecnicosRestricoes, function(parecerRestricao) {
				return parecerRestricao.codigoCamada === restricao.id;
			});

			if(parecerTecnicoRestricao !== undefined)
				restricao.properties.parecer = parecerTecnicoRestricao.parecer;
		});

		analiseGeoService.getDadosRestricoesProjeto(analiseGeo.analise.processo.id)
		.then(function(response) {

			ctrl.dadosRestricoesProjeto = response.data;

		});

		parecerAnalistaGeoService.findParecerByIdProcesso(analiseGeo.analise.processo.id)
		.then(function(response) {

			ctrl.parecer = response.data;

		});

		ctrl.listaInconsistencias = analiseGeo.inconsistencias.filter(function(inconsistencia) {

			return inconsistencia.categoria !== 'PROPRIEDADE';

		});

		$timeout(function() {

			var empreendimento = $scope.analiseGeo.analise.processo.empreendimento;

			$scope.$emit('mapa:adicionar-geometria-base', {
				geometria: JSON.parse(empreendimento.municipio.limite),
				tipo: 'EMP-CIDADE',
				estilo: {
					style: {
						fillColor: 'transparent',
						color: '#FFF'
					}
				},

				popupText: empreendimento.municipio.nome + ' - AM'
			});

			var cpfCnpjEmpreendimento = empreendimento.cpfCnpj;

			empreendimentoService.getDadosGeoEmpreendimento(cpfCnpjEmpreendimento)
				.then(function(response) {

					ctrl.camadasDadosEmpreendimento = response.data;

					ctrl.camadasDadosEmpreendimento.forEach(function (camada) {

						camada.geometrias.forEach(function(e) {

							adicionarGeometriaNoMapa(e);

						});

					});

					analiseGeoService.getDadosRestricoesEmpreendimento(cpfCnpjEmpreendimento).then(function (response) {

						ctrl.sobreposicoesEmpreendimento = response.data.sobreposicoes;

						ctrl.sobreposicoesEmpreendimento.forEach(function (camada) {

							var c = camada;

							c.item = cpfCnpjMask(camada.cpfCnpj);
							c.tipo = 'SOBREPOSICAO_EMPREENDIMENTO_';
							c.estilo = ctrl.estiloMapa.SOBREPOSICAO_EMPREENDIMENTO;
							c.estilo.color = getRandomColor();
							c.color = c.estilo.color;
							c.cpfCnpjAreaSobreposicao =  c.item;
							c.nomeAreaSobreposicao = c.denominacao;
							c.area = 0.0;
							c.numPoints = ctrl.numPoints;

							adicionarGeometriaNoMapa(c);

						});

					});

					analiseGeoService.getDadosProjeto($scope.analiseGeo.analise.processo.id).then(function (response) {

						ctrl.dadosProjeto = response.data;

						if(ctrl.dadosProjeto.categoria === ctrl.categoria.COMPLEXO || ctrl.dadosProjeto.complexo) {

							ctrl.labelDadosProjeto = 'Dados da área do complexo';

						} else if(ctrl.dadosProjeto.categoria === ctrl.categoria.PROPRIEDADE) {

							ctrl.labelDadosProjeto = 'Dados da área do empreendimento';

						} else {

							ctrl.labelDadosProjeto = 'Dados da(s) área(s) da(s) atividade(s)';

						}

						ctrl.dadosProjeto.atividades.forEach(function(atividade) {

							ctrl.numPoints += contaQuantidadeCamadasPoint(atividade.geometrias);

						});

						ctrl.numPoints += contaQuantidadeCamadasPoint(ctrl.dadosProjeto.restricoes);

						ctrl.dadosProjeto.atividades.forEach(function (atividade) {

							atividade.openedAccordion = false;

							atividade.geometrias.forEach(function(a) {

								a.estilo = ctrl.estiloMapa.ATIVIDADE;
								adicionarGeometriaNoMapa(a);

							});

						});

						ctrl.dadosProjeto.restricoes.forEach(function (restricao) {

							restricao.estilo = ctrl.estiloMapa.SOBREPOSICAO;
							adicionarGeometriaNoMapa(restricao);

						});

						if(ctrl.dadosProjeto.categoria === ctrl.categoria.COMPLEXO || ctrl.dadosProjeto.complexo) {

							ctrl.dadosProjeto.complexo.geometrias.forEach(function(geometria) {

								adicionarGeometriaNoMapa(geometria);

							});

						}

						tiposSobreposicaoService.getTiposSobreposicao().then(function (response) {

							ctrl.TiposSobreposicao = response.data;

						});

						var bounds = new L.latLngBounds();

						if(ctrl.dadosProjeto.categoria === ctrl.categoria.PROPRIEDADE) {

							ctrl.camadasDadosEmpreendimento.forEach(function(camada) {

								camada.geometrias.forEach(function(geometriaEmpreendimento) {

									bounds.extend(L.geoJSON(JSON.parse(geometriaEmpreendimento.geometria)).getBounds());

								});

							});

						} else if(ctrl.dadosProjeto.categoria === ctrl.categoria.COMPLEXO || ctrl.dadosProjeto.complexo) {

							ctrl.dadosProjeto.complexo.geometrias.forEach(function(geometriaComplexo) {

								bounds.extend(L.geoJSON(JSON.parse(geometriaComplexo.geometria)).getBounds());

							});

						} else {

							ctrl.dadosProjeto.atividades.forEach(function(atividade) {

								atividade.geometrias.forEach(function(geometriaAtividade) {

									bounds.extend(L.geoJSON(JSON.parse(geometriaAtividade.geometria)).getBounds());

								});

							});

						}

						$scope.$emit('mapa:centralizar-geometrias', bounds);

					});

			});

			$rootScope.$broadcast('atualizarContagemProcessos');

		});

	};

	$scope.verificaInconsistenciaEmpreendimento = function () {

		if (ctrl.analiseGeo.inconsistencias === undefined  || ctrl.analiseGeo.inconsistencias.length === 0) {
			return false;
		}

		var inconsitenciaEncontrada = _.find(ctrl.analiseGeo.inconsistencias, function (inconsistencia) {
			return inconsistencia.categoria.toUpperCase() === ctrl.categoria.PROPRIEDADE ;
		});

		return inconsitenciaEncontrada !== undefined;
	};

	$scope.verificaInconsistenciaAtividade = function ( categoriaInconsistencia, idCaracterizacao, idGeometriaAtividade) {

		if (ctrl.analiseGeo.inconsistencias === undefined  || ctrl.analiseGeo.inconsistencias.length === 0) {
			return false;
		}
		var verificaCategoria = categoriaInconsistencia;
		var inconsitenciaEncontrada = _.find(ctrl.analiseGeo.inconsistencias, function (inconsistencia) {
			return inconsistencia.categoria.toUpperCase() === verificaCategoria.toUpperCase() &&
						 inconsistencia.caracterizacao.id === idCaracterizacao &&
						 inconsistencia.geometriaAtividade.id === idGeometriaAtividade;
		});

		return inconsitenciaEncontrada !== undefined;

	};

	$scope.verificaInconsistenciaRestricao = function (categoriaInconsistencia, idCaracterizacao, idSobreposicao) {

		if (ctrl.analiseGeo.inconsistencias === undefined  || ctrl.analiseGeo.inconsistencias.length === 0) {
			return false;
		}

		var verificaCategoria = categoriaInconsistencia;

		var inconsitenciaEncontrada = _.find(ctrl.analiseGeo.inconsistencias, function (inconsistencia) {
			return inconsistencia.categoria.toUpperCase() === verificaCategoria.toUpperCase() &&
						 inconsistencia.caracterizacao.id === idCaracterizacao &&
						 (inconsistencia.sobreposicaoCaracterizacaoAtividade && inconsistencia.sobreposicaoCaracterizacaoAtividade.id === idSobreposicao) ||
						 (inconsistencia.sobreposicaoCaracterizacaoEmpreendimento && inconsistencia.sobreposicaoCaracterizacaoEmpreendimento.id === idSobreposicao) ||
						 (inconsistencia.sobreposicaoCaracterizacaoComplexo && inconsistencia.sobreposicaoCaracterizacaoComplexo.id === idSobreposicao);
		});

		return inconsitenciaEncontrada !== undefined;

	};

	ctrl.validacaoAbaAvancar = function() {

			ctrl.controleVisualizacao = "ETAPA_CONCLUSAO";
			scrollTop();

	};

	ctrl.validacaoAbaVoltar = function() {

		ctrl.controleVisualizacao = "ETAPA_LOCALIZACAO_GEOGRAFICA";
		scrollTop();
	};

	function openModal(analiseGeoModal, categoriaInconsistenciaModal, inconsistenciaModal, idCaracterizacaoModal, idGeometriaAtividadeModal, idSobreposicaoModal, dadosProjeto, listaInconsistencias) {

		var modalInstance = $uibModal.open({
			controller: 'inconsistenciaController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			templateUrl: 'features/analiseGeo/modalInconsistencia.html',
			size: 'lg',
			resolve: {
				analiseGeo: function () {
					return analiseGeoModal;
				},
				categoriaInconsistencia: function(){
					return categoriaInconsistenciaModal;
				},
				inconsistencia: function(){
					return inconsistenciaModal;
				},
				idCaracterizacao: function(){
					return idCaracterizacaoModal;
				},
				idGeometriaAtividade: function(){
					return idGeometriaAtividadeModal;
				},
				idSobreposicao: function(){
					return idSobreposicaoModal;
				},
				dadosProjeto: function(){
					return dadosProjeto;
				},
				listaInconsistencias: function() {
					return listaInconsistencias;
				}
			}
		});

		modalInstance.result.then(function(data){

			if (data.isExclusao) {
				ctrl.analiseGeo.inconsistencias.splice(ctrl.analiseGeo.inconsistencias.findIndex(function(i){
					return i.id === data.inconsistencia.id;
				}), 1);
			} else if (data.isEdicao) {

				ctrl.analiseGeo.inconsistencias.splice(ctrl.analiseGeo.inconsistencias.findIndex(function(i){
					return i.id === data.inconsistencia.id;
				}), 1);
				ctrl.analiseGeo.inconsistencias.push(data.inconsistencia);
			} else {
				ctrl.analiseGeo.inconsistencias.push(data.inconsistencia);
			}

		});

	}

	$scope.addInconsistenciaRestricao = function (categoriaInconsistencia, inconsistencia, isEdicao) {

		var idCaracterizacao = inconsistencia.caracterizacao.id;
		var idSobreposicao = inconsistencia.sobreposicaoCaracterizacaoAtividade ? inconsistencia.sobreposicaoCaracterizacaoAtividade.id : inconsistencia.sobreposicaoCaracterizacaoEmpreendimento ? inconsistencia.sobreposicaoCaracterizacaoEmpreendimento.id : inconsistencia.sobreposicaoCaracterizacaoComplexo.id;

		params = {
			categoria:categoriaInconsistencia,
			analiseGeo: {id: analiseGeo.id},
			caracterizacao: {id: idCaracterizacao},
			geometriaAtividade: {id: null},
			sobreposicaoCaracterizacaoAtividade: inconsistencia.sobreposicaoCaracterizacaoAtividade,
			sobreposicaoCaracterizacaoEmpreendimento: inconsistencia.sobreposicaoCaracterizacaoEmpreendimento,
			sobreposicaoCaracterizacaoComplexo: inconsistencia.sobreposicaoCaracterizacaoComplexo
		};

		inconsistenciaService.findInconsistenciaGeo(params)
		.then(function(response){

			var inconsistencia = response.data;
			inconsistencia.isEdicao = isEdicao;

			openModal(ctrl.analiseGeo, categoriaInconsistencia, inconsistencia, idCaracterizacao, null, idSobreposicao, ctrl.dadosProjeto, null);

		});

	};

	this.setLabelRestricao = function() {

		if(ctrl.dadosProjeto) {

			if(ctrl.dadosProjeto.categoria === ctrl.categoria.COMPLEXO || ctrl.dadosProjeto.complexo) {

				return 'Restrições do complexo';

			} else if(ctrl.dadosProjeto.categoria === ctrl.categoria.PROPRIEDADE) {

				return 'Restrições do empreendimento';

			} else {

				return 'Restrições da(s) atividade(s)';

			}

		}

	};

	$scope.addInconsistenciaGeral = function(inconsistencia){

		inconsistenciaService.findInconsistenciaById(inconsistencia.id)
		.then(function(response){

			var inconsistencia = response.data;

			openModal(ctrl.analiseGeo, inconsistencia.categoria, inconsistencia, null, null, null, ctrl.dadosProjeto, null);

		});

	};

	$scope.addInconsistenciaPropriedade = function(categoriaInconsistencia){

		params = {
			categoria: categoriaInconsistencia,
			analiseGeo: {id: analiseGeo.id}
		};

		inconsistenciaService.findInconsistenciaById(inconsistencia.id)
		.then(function(response){

			var inconsistencia = response.data;

			openModal(ctrl.analiseGeo, inconsistencia.categoria, inconsistencia, null, null, null, ctrl.dadosProjeto, null);

		});

	};

	$scope.addInconsistencia = function() {

		openModal(ctrl.analiseGeo, null, null, null, null, null, ctrl.dadosProjeto, ctrl.listaInconsistencias);

	};

	$scope.excluirInconsistenciaGeo = function(idInconsistencia) {

		inconsistenciaService.excluirInconsistenciaGeo(idInconsistencia)
			.then(function (response) {
				mensagem.success(response.data);

				ctrl.analiseGeo.inconsistencias = _.remove(ctrl.analiseGeo.inconsistencias, function(inconsistencia) {
					return(inconsistencia.id !== idInconsistencia);
				});

			}).catch(function (response) {
			mensagem.error(response.data.texto);

		});

	};

	$scope.getItemRestricao = function(inconsistencia) {

		if(inconsistencia.categoria.toUpperCase() === ctrl.categoria.RESTRICAO && ctrl.dadosRestricoesProjeto.length > 0) {

			var sobreposicaoInconsistencia = inconsistencia.sobreposicaoCaracterizacaoAtividade ? inconsistencia.sobreposicaoCaracterizacaoAtividade : inconsistencia.sobreposicaoCaracterizacaoEmpreendimento ? inconsistencia.sobreposicaoCaracterizacaoEmpreendimento : inconsistencia.sobreposicaoCaracterizacaoComplexo;

			restricao = ctrl.dadosRestricoesProjeto.find(function(restricao) {

				var sobreposicaoRestricao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

				return sobreposicaoInconsistencia.id === sobreposicaoRestricao.id;

			});

			return restricao && restricao.item ? restricao.item : '';

		} else if(inconsistencia.categoria.toUpperCase() === ctrl.categoria.ATIVIDADE && ctrl.dadosProjeto) {

			var atividade = ctrl.dadosProjeto.atividades.find(function(atividade) {

				return atividade.atividadeCaracterizacao.id === inconsistencia.atividadeCaracterizacao.id;

			});

			return atividade.atividadeCaracterizacao.atividade.nome;

	 	} else if(inconsistencia.categoria.toUpperCase() === ctrl.categoria.COMPLEXO) {

			return 'Complexo';

		} else {

			return 'Empreendimento/Atividade';

		}

	};

	$scope.getDescricaoRestricao = function(inconsistencia) {

		if(inconsistencia.categoria.toUpperCase() === ctrl.categoria.RESTRICAO && ctrl.dadosRestricoesProjeto.length > 0) {

			var sobreposicaoInconsistencia = inconsistencia.sobreposicaoCaracterizacaoAtividade ? inconsistencia.sobreposicaoCaracterizacaoAtividade : inconsistencia.sobreposicaoCaracterizacaoEmpreendimento ? inconsistencia.sobreposicaoCaracterizacaoEmpreendimento : inconsistencia.sobreposicaoCaracterizacaoComplexo;

			restricao = ctrl.dadosRestricoesProjeto.find(function(restricao) {

				var sobreposicaoRestricao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

				return sobreposicaoInconsistencia.id === sobreposicaoRestricao.id;

			});

			return restricao && restricao.descricao ? restricao.descricao : '';

		} else if(inconsistencia.categoria.toUpperCase() === ctrl.categoria.ATIVIDADE) {

			return inconsistencia.descricaoInconsistencia;

		}

		return '-';

	};

	ctrl.clonarParecerGeo = function() {

		if(ctrl.numeroProcessoClone) {

			parecerAnalistaGeoService.getParecerByNumeroProcesso(ctrl.numeroProcessoClone)
				.then(function(response){

						if(response.data.parecer === undefined) {

							ctrl.parecer.parecer = null;
							mensagem.warning(response.data.texto);

							return;

						} else{

							ctrl.parecer = response.data;

						}

				}, function(error){
					mensagem.error(error.data.texto);
				});

		}

	};

	ctrl.upload = function(file, invalidFile, tipoUpload) {

		if(invalidFile){
			ctrl.errors.isPdf = true;
		}

		if(file) {
			  ctrl.errors.isPdf = false;
				uploadService.save(file)
						.then(function(response) {

							var nomeDoArquivo = file.name;

							if(ctrl.parecer.documentos === null || ctrl.parecer.documentos === undefined) {
								ctrl.parecer.documentos = [];
							}

							var quantidadeDocumentosComMesmoNome = ctrl.parecer.documentos.filter(function(documento) {
								return documento.nomeDoArquivo.includes(file.name.split("\.")[0]);
							}).length;

							if(quantidadeDocumentosComMesmoNome > 0) {
								nomeDoArquivo = file.name.split("\.")[0] + " (" + quantidadeDocumentosComMesmoNome + ")." + file.name.split("\.")[1];
							}

							if(tipoUpload === app.utils.TiposUpload.PARECER_ANALISE_GEO) {

								ctrl.parecer.documentos.push({

									key: response.data,
									nomeDoArquivo: nomeDoArquivo,
									tipo: {

											id: app.utils.TiposDocumentosAnalise.PARECER_ANALISE_GEO
									}
								});

							} else if(tipoUpload === app.utils.TiposUpload.NOTIFICACAO){

								ctrl.parecer.documentos.push({

									key: response.data,
									nomeDoArquivo: nomeDoArquivo,
									tipo: {

											id: app.utils.TiposDocumentosAnalise.NOTIFICACAO
									}
								});
							} else if(tipoUpload === app.utils.TiposUpload.DOCUMENTO_ANALISE_TEMPORAL){

								ctrl.parecer.documentos.push({

									key: response.data,
									nomeDoArquivo: nomeDoArquivo,
									tipo: {

											id: app.utils.TiposDocumentosAnalise.DOCUMENTO_ANALISE_TEMPORAL
									}
								});
							}

						}, function(error){

								mensagem.error(error.data.texto);
						});

		} else if(invalidFile && invalidFile.$error === 'maxSize'){

				mensagem.error('Ocorreu um erro ao enviar o arquivo: ' + invalidFile.name + ' . Verifique se o arquivo tem no máximo ' + TAMANHO_MAXIMO_ARQUIVO_MB + 'MB');
		}
	};

	ctrl.removerDocumento = function (documento) {

		var indexDocumento = ctrl.parecer.documentos.indexOf(documento);

		if(ctrl.parecer.documentos[indexDocumento].key) {
			documentoService.delete(ctrl.parecer.documentos[indexDocumento].key);
		}

		ctrl.parecer.documentos.splice(indexDocumento, 1);

	};

	ctrl.baixarDocumento= function(documento) {

		if(!documento.id){
			documentoService.download(documento.key, documento.nomeDoArquivo);
		}else{
			analiseGeoService.download(documento.id);
		}
	};


	ctrl.avancarProximaEtapa = function() {
		$timeout(function() {

				$('.nav-tabs > .active').next('li').find('a').trigger('click');
				ctrl.controleVisualizacao = "ETAPA_CONCLUSAO";
				scrollTop();

    }, 0);
	};

	$scope.passoValido = function() {

		var restricoes = _.filter(ctrl.dadosRestricoesProjeto, function(restricao) {

			var sobreposicao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

			return sobreposicao.tipoSobreposicao.orgaosResponsaveis.every(function(orgao) {

				return orgao.sigla.toUpperCase() === ctrl.orgaos.IPHAN || orgao.sigla.toUpperCase() === ctrl.orgaos.IBAMA;

			});

		});

		var inconsistenciasRestricao = _.filter(ctrl.analiseGeo.inconsistencias, function(inconsistencia) {
			return inconsistencia.categoria === 'RESTRICAO';
		});

		return inconsistenciasRestricao.length === restricoes.length;

	};

	ctrl.cancelar= function() {
		$location.path('/analise-geo');
		ctrl.controleVisualizacao = "ETAPA_LOCALIZACAO_GEOGRAFICA";
	};

	function scrollTop() {
		$anchorScroll();
	}

	ctrl.voltarEtapaAnterior = function(){
		$timeout(function() {

			$('.nav-tabs > .active').prev('li').find('a').trigger('click');
			scrollTop();
			ctrl.controleVisualizacao = "ETAPA_LOCALIZACAO_GEOGRAFICA";

		}, 0);
	};

	function analiseValida() {

		var hasError = false;

		if(ctrl.parecer.tipoResultadoAnalise === undefined) {

			ctrl.errors.resultadoAnalise = true;
			return false;

		} else{

			ctrl.errors.resultadoAnalise = false;

		}

		if(ctrl.parecer.analiseTemporal !== '' && ctrl.parecer.analiseTemporal !== null) {

			var verificaDocAnaliseTemp = false;

			_.forEach(ctrl.parecer.documentos, function(documentoAnaliseTemporal){

				if(documentoAnaliseTemporal.tipo.id === app.utils.TiposDocumentosAnalise.DOCUMENTO_ANALISE_TEMPORAL){
					verificaDocAnaliseTemp = true;
				}

			});

			if (verificaDocAnaliseTemp === false){

				ctrl.errors.docAnaliseTemporal = true;
				hasError = true;

			} else {

				ctrl.errors.docAnaliseTemporal = false;

			}

		}

		if (ctrl.parecer.analiseTemporal === '' || ctrl.parecer.analiseTemporal === null) {

			if (ctrl.getDocumentosAnaliseTemporal().length === 1) {

				ctrl.errors.analiseTemporal = true;
				hasError = true;

			} else {

				ctrl.errors.analiseTemporal = true;

			}

		}


		if (!ctrl.parecer.conclusao && ctrl.parecer.tipoResultadoAnalise.id !== ctrl.TiposResultadoAnalise.EMITIR_NOTIFICACAO.toString()) {

			ctrl.errors.conclusao = true;
			hasError = true;

		} else{

			ctrl.errors.conclusao = false;

		}

		if (!ctrl.notificacao.prazoNotificacao && ctrl.parecer.tipoResultadoAnalise.id === ctrl.TiposResultadoAnalise.EMITIR_NOTIFICACAO.toString() || ctrl.notificacao.prazoNotificacao === null && ctrl.parecer.tipoResultadoAnalise.id === ctrl.TiposResultadoAnalise.EMITIR_NOTIFICACAO.toString()){

			ctrl.errors.prazoNotificacao = true;
			hasError = true;

		} else{

			ctrl.errors.prazoNotificacao = false;

		}

		if(!ctrl.parecer.parecer || ctrl.parecer.parecer === undefined){

			ctrl.errors.parecer = true;
			hasError = true;

		}else{

			ctrl.errors.parecer = false;

		}

		if(ctrl.parecer.tipoResultadoAnalise.id === ctrl.TiposResultadoAnalise.EMITIR_NOTIFICACAO.toString()) {

			if(ctrl.notificacao.retificacaoSolicitacao && !ctrl.notificacao.retificacaoSolicitacaoComGeo) {

				hasError = true;

			}

			if(!(ctrl.notificacao.documentacao || ctrl.notificacao.retificacaoEmpreendimento || (ctrl.notificacao.retificacaoSolicitacao && ctrl.notificacao.retificacaoSolicitacaoComGeo))) {

				ctrl.errors.atendimento = true;
				hasError = true;

			} else{

				ctrl.errors.atendimento = false;

			}

			if (hasError) {

				return false;
			}

			return true;

		}

		if (hasError) {

			return false;
		}

		return ((ctrl.parecer.tipoResultadoAnalise.id === ctrl.TiposResultadoAnalise.DEFERIDO.toString() ||
			ctrl.parecer.tipoResultadoAnalise.id === ctrl.TiposResultadoAnalise.INDEFERIDO.toString()) && ctrl.parecer.parecer);

	}

	ctrl.downloadPDFParecer = function() {

		var params = {
			id: $scope.analiseGeo.id,
			parecer: $scope.analiseGeo.parecer
		};

		documentoAnaliseService.generatePDFParecerGeo(params)
			.then(function(data, status, headers){

				var a = document.createElement('a');
				a.href = URL.createObjectURL(data.data.response.blob);
				a.download = data.data.response.fileName ? data.data.response.fileName : 'parecer_analise_geo.pdf';
				a.click();

			},function(error){
				mensagem.error(error.data.texto);
			});

	};

	ctrl.concluir = function(){

		if(!analiseValida()) {

			mensagem.error('Não foi possível concluir a análise. Verifique os campos obrigatórios!', { ttl: 10000 });
			return;

		}

		ctrl.analiseGeo.analise.processo.empreendimento = null;

		tratarDadosNotificacao();

		ctrl.parecer.analiseGeo = ctrl.analiseGeo;

		if(ctrl.parecer.documentos === null) {
			ctrl.parecer.documentos = [];
		}

		analiseGeoService.concluir(ctrl.parecer)
			.then(function(response) {

				var params = {
					id: $scope.analiseGeo.id
				};

				documentoAnaliseService.generatePDFCartaImagemGeo(params)
					.then(function(data, status, headers){

						var a = document.createElement('a');
						a.href = URL.createObjectURL(data.data.response.blob);
						a.download = data.data.response.fileName ? data.data.response.fileName : 'carta_imagem.pd.pdf';
						a.click();


						if(ctrl.analiseGeo.inconsistencias && ctrl.analiseGeo.inconsistencias.length === 0){

							documentoAnaliseService.generatePDFParecerGeo(params)
								.then(function(data, status, headers){

									var a = document.createElement('a');
									a.href = URL.createObjectURL(data.data.response.blob);
									a.download = data.data.response.fileName ? data.data.response.fileName : 'parecer_analise_geo.pdf';
									a.click();

							},function(error){
									mensagem.error(error.data.texto);
							});
						}

						$location.path('/analise-geo');

					},function(error){
						mensagem.error(error.data.texto);
					});

					$location.path('/analise-geo');
					mensagem.setMensagemProximaTela('success', response.data.texto);

			}, function(error){

				mensagem.error(error.data.texto, {referenceId: 5});
			});

			$rootScope.$broadcast('atualizarContagemProcessos');

	};

	function tratarDadosNotificacao() {

		ctrl.notificacao.documentacao = ctrl.notificacao.documentacao === null ? false : true;
		ctrl.notificacao.retificacaoEmpreendimento = ctrl.notificacao.retificacaoEmpreendimento === null ? false : true;
		ctrl.notificacao.retificacaoSolicitacao = ctrl.notificacao.retificacaoSolicitacao === null ? false : true;
		ctrl.notificacao.retificacaoSolicitacaoComGeo = (ctrl.notificacao.retificacaoSolicitacaoComGeo === 'true' ? true : ctrl.notificacao.retificacaoSolicitacaoComGeo === 'false' ? false : null);
		ctrl.notificacao.segundoEmailEnviado = false;
		ctrl.analiseGeo.notificacoes.push(ctrl.notificacao);

	}

	$scope.optionsText = {
		toolbar: [
			['edit',['undo','redo']],
			['style', ['bold', 'italic', 'underline', 'superscript', 'subscript', 'strikethrough', 'clear']],
			['textsize', ['fontsize']],
			['alignment', ['ul', 'ol', 'paragraph', 'lineheight']],
			['height', ['height']],
			['table', ['table']],
			['insert', ['picture',]]

		]
	};

	$scope.snPaste = function(e, model) {
		var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
		e.preventDefault();
		setTimeout( function(){
		  document.execCommand( 'insertText', false, bufferText );
		}, 10 );
	};

	ctrl.getRestricoes = function() {
		var restricoes = [];
		var orgaoEnable = false;
		var restricaoEnable = true;

		_.forEach(ctrl.dadosProjeto, function(dadoProjeto){
			_.forEach(dadoProjeto.restricoes, function(restricao){

				var sobreposicaoRestricao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

				_.forEach(sobreposicaoRestricao.tipoSobreposicao.orgaosResponsaveis, function(orgao){
					//verifica se o orgão da restrição é IPHAN ou IBAMA
					if(orgao.sigla.toUpperCase() === ctrl.orgaos.IPHAN || orgao.sigla.toUpperCase() === ctrl.orgaos.IBAMA){
						orgaoEnable = true;
					}
				});
				if(orgaoEnable){
					_.forEach(ctrl.analiseGeo.inconsistencias, function(i){
						//verifica se uma restrição já possui inconsistência

						var sobreposicao = i.sobreposicaoCaracterizacaoAtividade ? i.sobreposicaoCaracterizacaoAtividade : i.sobreposicaoCaracterizacaoEmpreendimento ? i.sobreposicaoCaracterizacaoEmpreendimento : i.sobreposicaoCaracterizacaoComplexo;

						if(sobreposicao && (sobreposicao.id === sobreposicaoRestricao.id)){
							restricaoEnable = false;
						}
					});
					if(restricaoEnable){
						restricoes.push(restricao);
					}
				}
				orgaoEnable = false;
				restricaoEnable = true;
			});
		});
		return restricoes;
	};

	ctrl.getDocumentosParecer = function() {

		var documentosParecer = [];

		documentosParecer = _.filter(ctrl.parecer.documentos, function(documento) {
			return documento.tipo.id === app.utils.TiposDocumentosAnalise.PARECER_ANALISE_GEO;
		});

		return documentosParecer;
	};

	ctrl.getDocumentosAnaliseTemporal = function() {

		var documentosAnaliseTemporal = [];

		documentosAnaliseTemporal = _.filter(ctrl.parecer.documentos, function(documento) {
			return documento.tipo.id === app.utils.TiposDocumentosAnalise.DOCUMENTO_ANALISE_TEMPORAL;
		});

		return documentosAnaliseTemporal;
	};

	ctrl.getDocumentosNotificacao = function() {

		var documentosNotificacao = [];

		documentosNotificacao = _.filter(ctrl.parecer.documentos, function(documento) {
			return documento.tipo.id === app.utils.TiposDocumentosAnalise.NOTIFICACAO;
		});

		return documentosNotificacao;
	};

	ctrl.checkedDocumentacao = function() {
		if (!ctrl.notificacao.documentacao) {
			ctrl.notificacao.documentacao = null;
		}
	};

	ctrl.checkedRetificacaoSolicitacao = function() {
		if (!ctrl.notificacao.retificacaoSolicitacao) {
			ctrl.notificacao.retificacaoSolicitacao = null;
		}
		ctrl.notificacao.retificacaoSolicitacaoComGeo = null;
	};

	ctrl.checkedRetificacaoEmpreendimento = function() {

		if (!ctrl.notificacao.retificacaoEmpreendimento) {
			ctrl.notificacao.retificacaoEmpreendimento = null;
			ctrl.notificacao.retificacaoSolicitacao = null;
			ctrl.notificacao.retificacaoSolicitacaoComGeo = null;
		} else {
			ctrl.notificacao.retificacaoSolicitacao = true;
			ctrl.notificacao.retificacaoSolicitacaoComGeo = 'true';
		}

	};

};

exports.controllers.AnaliseGeoController = AnaliseGeoController;
