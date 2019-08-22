var AnaliseGeoController = function($injector, $rootScope, $scope, $timeout, $uibModal, analiseGeo, $anchorScroll,$location, analiseGeoService, restricoes,documentoService ,idAnaliseGeo,inconsistenciaService,processoService, empreendimentoService, uploadService,mensagem, documentoAnaliseService, tiposSobreposicaoService) {
	
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
	ctrl.analiseGeo.tipoResultadoAnalise = {id:undefined};
	ctrl.categoria = app.utils.Inconsistencia;
	ctrl.orgaos = app.utils.Orgao;
	ctrl.camadas = [];
	ctrl.estiloMapa = app.utils.EstiloMapa;
	ctrl.camadasSobreposicao = app.utils.CamadaSobreposicao;
	ctrl.controleVisualizacao = null;
	ctrl.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;
	ctrl.TiposSobreposicao = [];

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

		processoService.visualizarProcesso(processo);
    };

	this.controlaExibicaoCamadas = function(camada) {

		if (camada.visivel) {
			$scope.$emit('mapa:remover-geometria-base', camada);
		} else {
			adicionarGeometriaNoMapa(camada, true);
		}
	};

	this.controlaExibicaoLayer = function(tipoSobreposicao) {

		$scope.$emit('mapa:controla-exibicao-wmslayer', tipoSobreposicao);
	};

	this.getCamadasSobreposicoes = function() {

		ctrl.TiposSobreposicao.forEach(function (tipo) {
			adicionarWmsLayerNoMapa(tipo);
		});
	};

	this.controlaCentralizacaoCamadas = function (camada) {

		$scope.$emit('mapa:centralizar-camada', camada);
	};

	function adicionarGeometriaNoMapa (camada, disable) {

		camada.visivel = true;
		camada.color = ctrl.estiloMapa[camada.tipo] != undefined ? ctrl.estiloMapa[camada.tipo].color : camada.estilo.color;

		$scope.$emit('mapa:adicionar-geometria-base', {
			geometria: JSON.parse(camada.geometria),
			tipo: camada.tipo,
			estilo: {
				style: ctrl.estiloMapa[camada.tipo] || camada.estilo
			},
			popupText: camada.item,
			disableCentralizarGeometrias:disable
		});
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

	this.init = function() {

		$scope.analiseGeo = analiseGeo;

		_.forEach(restricoes.features, function(restricao) {
			var parecerTecnicoRestricao = _.find($scope.analiseGeo.pareceresTecnicosRestricoes, function(parecerRestricao) {
				return parecerRestricao.codigoCamada === restricao.id;
			});

			if(parecerTecnicoRestricao !== undefined)
				restricao.properties.parecer = parecerTecnicoRestricao.parecer;
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

			var cpfCnpjEmpreendimento = empreendimento.pessoa.cpf || empreendimento.pessoa.cnpj;

			empreendimentoService.getDadosGeoEmpreendimento(cpfCnpjEmpreendimento)
				.then(function(response) {

					ctrl.camadasDadosEmpreendimento = response.data;

					ctrl.camadasDadosEmpreendimento.forEach(function (camada) {
						adicionarGeometriaNoMapa(camada);
					});

					analiseGeoService.getDadosAreaProjeto($scope.analiseGeo.analise.processo.id)
						.then(function (response) {

							ctrl.camadasDadosAtividade = response.data;
							ctrl.camadasDadosAtividade.forEach(function (camadaAtividade) {
								camadaAtividade.camadasGeo.forEach(function (camadaGeo) {
									camadaGeo.estilo = ctrl.estiloMapa.ATIVIDADE;
									adicionarGeometriaNoMapa(camadaGeo);
								});

								if (camadaAtividade.restricoes !== null && camadaAtividade.restricoes.length > 0) {

									camadaAtividade.restricoes.forEach(function (restricao) {

										restricao.estilo = ctrl.estiloMapa.SOBREPOSICAO;

										adicionarGeometriaNoMapa(restricao);
									});
								}

								tiposSobreposicaoService.getTiposSobreposicao()
									.then(function (response) {
										ctrl.TiposSobreposicao = response.data;
									});
							});
						});
				});

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

	$scope.verificaInconsistenciaAtividade = function ( categoriaInconsistencia, idAtividadeCaracterizacao, idGeometriaAtividade) {

		if (ctrl.analiseGeo.inconsistencias === undefined  || ctrl.analiseGeo.inconsistencias.length === 0) {
			return false;
		}
		var verificaCategoria = categoriaInconsistencia;
		var inconsitenciaEncontrada = _.find(ctrl.analiseGeo.inconsistencias, function (inconsistencia) {
			return inconsistencia.categoria.toUpperCase() === verificaCategoria.toUpperCase() && 
						 inconsistencia.atividadeCaracterizacao.id === idAtividadeCaracterizacao && 
						 inconsistencia.geometriaAtividade.id === idGeometriaAtividade;
		});


		return inconsitenciaEncontrada !== undefined;
	};

	$scope.addInconsistenciaAtividade = function (categoriaInconsistencia, idAtividadeCaracterizacao , idGeometriaAtividade) {

		params = {
			categoria:categoriaInconsistencia,
			analiseGeo: {id: analiseGeo.id},
			atividadeCaracterizacao: {id: idAtividadeCaracterizacao},
			geometriaAtividade: {id: idGeometriaAtividade}			
		};

		inconsistenciaService.findInconsistencia(params)
		.then(function(response){

			var modalInstance = $uibModal.open({
				controller: 'inconsistenciaController',
				controllerAs: 'modalCtrl',
				templateUrl: 'features/analiseGeo/modalInconsistencia.html',
				size: 'lg',
				resolve: {
					analiseGeo: function () {
						return ctrl.analiseGeo;
					},
					categoriaInconsistencia: function(){
						return categoriaInconsistencia;
					},
					inconsistencia: function(){
						return response.data;
					},
					idAtividadeCaracterizacao: function(){
						return idAtividadeCaracterizacao;
					},
					idGeometriaAtividade: function(){
						return idGeometriaAtividade;
					}
				}
			});

			modalInstance.result.then(function(data){

				if (data.isExclusao) {
					ctrl.analiseGeo.inconsistencias.splice(ctrl.analiseGeo.inconsistencias.indexOf(data.inconsistencia), 1);
				} else if (data.isEdicao) {

					ctrl.analiseGeo.inconsistencias.splice(ctrl.analiseGeo.inconsistencias.findIndex(function(i){
						return i.id === data.inconsistencia.id;
					}), 1);
					ctrl.analiseGeo.inconsistencias.push(data.inconsistencia);
				} else {
					ctrl.analiseGeo.inconsistencias.push(data.inconsistencia);
				}

			});
			
		});		

	};

	$scope.addInconsistencia = function(categoriaInconsistencia){
			
			params = {
				categoria: categoriaInconsistencia,
				analiseGeo: {id: analiseGeo.id}
			};
	
			inconsistenciaService.findInconsistencia(params)
			.then(function(response){

				var modalInstance = $uibModal.open({
					controller: 'inconsistenciaController',
					controllerAs: 'modalCtrl',
					templateUrl: 'features/analiseGeo/modalInconsistencia.html',
					size: 'lg',
					resolve: {
						analiseGeo: function () {
							return ctrl.analiseGeo;
						},
						categoriaInconsistencia: function(){
							return categoriaInconsistencia;
						},
						inconsistencia: function(){
							return response.data;
						},
						idAtividadeCaracterizacao: function(){
							return null;
						},
						idGeometriaAtividade: function(){
							return null;
						}
					}
				});

				modalInstance.result.then(function(data){

					if (data.isExclusao) {
						ctrl.analiseGeo.inconsistencias.splice(ctrl.analiseGeo.inconsistencias.indexOf(data.inconsistencia), 1);
					} else if (data.isEdicao) {

						ctrl.analiseGeo.inconsistencias.splice(ctrl.analiseGeo.inconsistencias.findIndex(function(i){
							return i.id === data.inconsistencia.id;
						}), 1);
						ctrl.analiseGeo.inconsistencias.push(data.inconsistencia);
					} else {
						ctrl.analiseGeo.inconsistencias.push(data.inconsistencia);
					}

				});
				
			});
	};

	function controleCamposParecerEmpreedimento() {

		if(ctrl.analiseGeo.inconsistencias.length > 0){
			$('#situacaoFundiaria').summernote('disable');
			$('#analiseTemporal').summernote('disable');
			ctrl.analiseGeo.situacaoFundiaria = undefined;
            ctrl.analiseGeo.analiseTemporal = undefined;

		} else {
			$('#situacaoFundiaria').summernote('enable');
			$('#analiseTemporal').summernote('enable');
		}
	}


	ctrl.clonarParecerGeo = function() {

		analiseGeoService.getParecerByNumeroProcesso(ctrl.numeroProcessoClone)
			.then(function(response){

					if(response.data === null) {

							ctrl.analiseGeo.parecer = null;
							mensagem.error('Não foi encontrado um parecer para esse número de processo.');
							return;
					}
					ctrl.analiseGeo.parecer = response.data.parecer;
					ctrl.analiseGeo.situacaoFundiaria = response.data.situacaoFundiaria;
					ctrl.analiseGeo.analiseTemporal = response.data.analiseTemporal;

			}, function(error){

					mensagem.error(error.data.texto);
			});
	};

	ctrl.upload = function(file, invalidFile) {

		if(file) {

				uploadService.save(file)
						.then(function(response) {

							ctrl.analiseGeo.documentos.push({

										key: response.data,
										nomeDoArquivo: file.name,
										tipoDocumento: {

												id: app.utils.TiposDocumentosAnalise.ANALISE_GEO
										}
								});

						}, function(error){

								mensagem.error(error.data.texto);
						});

		} else if(invalidFile && invalidFile.$error === 'maxSize'){

				mensagem.error('Ocorreu um erro ao enviar o arquivo: ' + invalidFile.name + ' . Verifique se o arquivo tem no máximo ' + TAMANHO_MAXIMO_ARQUIVO_MB + 'MB');
		}
	};

	 ctrl.removerDocumento = function (indiceDocumento) {

		ctrl.analiseGeo.documentos.splice(indiceDocumento,1);

	};

	ctrl.baixarDocumento= function(documento) {

		if(!documento.id){
			documentoService.download(documento.key, documento.nomeDoArquivo);
		}else{
			analiseGeoService.download(documento.id);
		}
	};


	ctrl.avancarProximaEtapa= function() {
		ctrl.controleVisualizacao = "ETAPA_CONCLUSAO";
		$('.nav-tabs > .active').next('li').find('a').trigger('click');
		controleCamposParecerEmpreedimento();
		scrollTop();
	};


	ctrl.cancelar= function() {
		$location.path('/analise-geo');
		ctrl.controleVisualizacao = "ETAPA_LOCALIZACAO_GEOGRAFICA";
	};

	function scrollTop() {
		$anchorScroll();
	}

	ctrl.voltarEtapaAnterior = function(){
			$('.nav-tabs > .active').prev('li').find('a').trigger('click');
			scrollTop();
			ctrl.controleVisualizacao = "ETAPA_LOCALIZACAO_GEOGRAFICA";
	};

	function analiseValida() {

		if ((!ctrl.analiseGeo.inconsistencias || ctrl.analiseGeo.inconsistencias.length === 0) && (!ctrl.analiseGeo.analiseTemporal || !ctrl.analiseGeo.situacaoFundiaria)) {
			return false;
		}

		if (!ctrl.analiseGeo.parecer) {
			return false;
		}

		if(ctrl.analiseGeo.tipoResultadoAnalise.id === undefined) {
			return false;
		}

		return ((ctrl.analiseGeo.tipoResultadoAnalise.id === ctrl.TiposResultadoAnalise.DEFERIDO.toString() ||
			ctrl.analiseGeo.tipoResultadoAnalise.id === ctrl.TiposResultadoAnalise.INDEFERIDO.toString()) && ctrl.analiseGeo.despacho);
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

			mensagem.error('Não foi possível concluir a análise. Verifique se as seguintes condições foram satisfeitas: ' +
				'<ul>' +
				'<li>Para concluir é necessário descrever o parecer na conclusão.</li>' +
				'<li>Selecione um parecer para o processo (Deferido, Indeferido, Emitir notificação).</li>' +
				'</ul>', { ttl: 10000 });
			return;
		}

		ctrl.analiseGeo.analise.processo.empreendimento = null;

		analiseGeoService.concluir(ctrl.analiseGeo)
			.then(function(response) {

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

						documentoAnaliseService.generatePDFCartaImagemGeo(params)
							.then(function(data, status, headers){

								var a = document.createElement('a');
								a.href = URL.createObjectURL(data.data.response.blob);
								a.download = data.data.response.fileName ? data.data.response.fileName : 'carta_imagem.pd.pdf';
								a.click();

								$location.path('/analise-geo');
								mensagem.setMensagemProximaTela('success', response.data.texto);

							},function(error){
								mensagem.error(error.data.texto);
							});

					},function(error){
							mensagem.error(error.data.texto);
					});

			}, function(error){

				mensagem.error(error.data.texto);
			});
	};

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

};

exports.controllers.AnaliseGeoController = AnaliseGeoController;