var VisualizacaoProcessoController = function ($location, $injector, desvinculoService,
											   $uibModal, $scope, $rootScope, $timeout, 
											   $uibModalInstance, processo, mensagem, 
											   $anchorScroll,processoService, 
											   empreendimentoService, notificacaoService,
											   documentoLicenciamentoService, analiseGeoService, 
											   parecerAnalistaGeoService, parecerGerenteService,
											   tiposSobreposicaoService) {

	var modalCtrl = this;

	modalCtrl.processo = processo;
	modalCtrl.baixarDocumento = baixarDocumento;
	modalCtrl.getMaiorPotencialPoluidor = getMaiorPotencialPoluidor;
	modalCtrl.usuarioLogadoCodigoPerfil = $rootScope.usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo;
	modalCtrl.abreDocumentacao = true;
	modalCtrl.abreTramitacaoProcessoAtual = true;
	modalCtrl.abreTramitacaoProcessoAnterior = false;
	modalCtrl.perfis = app.utils.Perfis;
	modalCtrl.comparaStatus = app.utils.CondicaoTramitacao;
	modalCtrl.estiloMapa = app.utils.EstiloMapa;
	modalCtrl.dateUtil = app.utils.DateUtil;
	modalCtrl.PrazoAnalise = app.utils.PrazoAnalise;
	modalCtrl.LegendasTipoSobreposicao = app.utils.LegendasTipoSobreposicao;
	modalCtrl.camadasSobreposicao = app.utils.CamadaSobreposicao;
	modalCtrl.tiposResultadoAnaliseUtils = app.utils.TiposResultadoAnalise;
	modalCtrl.acaoTramitacao = app.utils.AcaoTramitacao;
	modalCtrl.categoria = app.utils.Inconsistencia;
	modalCtrl.exibirDocumentacao = !modalCtrl.abreDocumentacao;
	modalCtrl.numPoints = 0;
	modalCtrl.dadosProjeto = {};
	modalCtrl.TiposSobreposicao = [];
	modalCtrl.openedAccordionGeo = false;
	modalCtrl.openedAccordionGerente = false;
	modalCtrl.labelAnalistaGeo = '';
	modalCtrl.labelGerente = '';
	modalCtrl.parecer = {};

	$injector.invoke(exports.controllers.PainelMapaController, this,
		{
			$scope: $scope,
			$timeout: $timeout
		}
	);

	if (processo.idProcesso) {

		processoService.getInfoProcesso(processo.idProcesso)
			.then(function(response){

				modalCtrl.dadosProcesso = response.data;
				modalCtrl.limite = modalCtrl.dadosProcesso.empreendimento.imovel ? modalCtrl.dadosProcesso.empreendimento.imovel.limite : modalCtrl.dadosProcesso.empreendimento.municipio.limite;

			})
			.catch(function(){
				mensagem.error("Ocorreu um erro ao buscar dados do protocolo.");
			});

	} else {

		processoService.getInfoProcessoByNumero(processo.numeroProcesso)
		.then(function(response){

			modalCtrl.dadosProcesso = response.data;
			modalCtrl.limite = modalCtrl.dadosProcesso.empreendimento.imovel ? modalCtrl.dadosProcesso.empreendimento.imovel.limite : modalCtrl.dadosProcesso.empreendimento.municipio.limite;
	
		})
		.catch(function(){
			mensagem.error("Ocorreu um erro ao buscar dados do protocolo.");
		});

	}

	modalCtrl.setLabelAnalistaGeo = function(tipoResultadoAnalistaGeo) {

		if(tipoResultadoAnalistaGeo.id === modalCtrl.tiposResultadoAnaliseUtils.DEFERIDO) {

			return 'Despacho';

		} else if(tipoResultadoAnalistaGeo.id === modalCtrl.tiposResultadoAnaliseUtils.INDEFERIDO) {

			return 'Justificativa';

		} else if(tipoResultadoAnalistaGeo.id === modalCtrl.tiposResultadoAnaliseUtils.EMITIR_NOTIFICACAO) {

			return 'Descrição da solicitação';

		}

	};

	modalCtrl.setLabelGerente = function(tipoResultadoAnaliseGerente) {

		if(tipoResultadoAnaliseGerente.id === modalCtrl.tiposResultadoAnaliseUtils.PARECER_VALIDADO) {

			return 'Despacho';

		} else if(tipoResultadoAnaliseGerente.id === modalCtrl.tiposResultadoAnaliseUtils.SOLICITAR_AJUSTES) {

			return 'Observações';

		} else if(tipoResultadoAnaliseGerente.id === modalCtrl.tiposResultadoAnaliseUtils.PARECER_NAO_VALIDADO) {

			return 'Justificativa';

		}

	};

	// Métodos referentes ao Mapa da caracterização
	this.iniciarMapa = function() {

		modalCtrl.init('mapa-visualizacao-protocolo', true, true);

		$timeout(function() {

			var empreendimento = modalCtrl.dadosProcesso.empreendimento;

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

			empreendimentoService.getDadosGeoEmpreendimento(cpfCnpjEmpreendimento).then(function(response) {

				modalCtrl.camadasDadosEmpreendimento = response.data;

				modalCtrl.camadasDadosEmpreendimento.forEach(function (camada) {
					
					camada.geometrias.forEach(function(e) {
						
						adicionarGeometriaNoMapa(e);

					});

				});

				analiseGeoService.getDadosProjeto(modalCtrl.processo.idProcesso).then(function (response) {

					modalCtrl.dadosProjeto = response.data;

					modalCtrl.dadosProjeto.atividades.forEach(function(atividade) {

						modalCtrl.numPoints += contaQuantidadeCamadasPoint(atividade.geometrias);

					});

					modalCtrl.numPoints += contaQuantidadeCamadasPoint(modalCtrl.dadosProjeto.restricoes);

					modalCtrl.dadosProjeto.atividades.forEach(function (atividade) {

						atividade.openedAccordion = false;
						
						atividade.geometrias.forEach(function(a) {
							
							a.estilo = modalCtrl.estiloMapa.ATIVIDADE;
							adicionarGeometriaNoMapa(a);

						});

					});

					modalCtrl.dadosProjeto.restricoes.forEach(function (restricao) {

						restricao.estilo = modalCtrl.estiloMapa.SOBREPOSICAO;
						adicionarGeometriaNoMapa(restricao);

					});

					if(modalCtrl.dadosProjeto.categoria === modalCtrl.categoria.COMPLEXO || modalCtrl.dadosProjeto.complexo) {

						modalCtrl.dadosProjeto.complexo.geometrias.forEach(function(geometria) {

							adicionarGeometriaNoMapa(geometria);

						});

					}

					tiposSobreposicaoService.getTiposSobreposicao().then(function (response) {

						modalCtrl.TiposSobreposicao = response.data;

					});

					$scope.$emit('mapa:centralizar-mapa');

				});

			});

		});

	};

	function contaQuantidadeCamadasPoint (camadas) {

		return camadas.filter(function(camada) {

			return JSON.parse(camada.geometria).type.toLowerCase() === 'point';

		}).length;

	}

	function adicionarWmsLayerNoMapa (tipoSobreposicao) {

		tipoSobreposicao.visivel = false;
		tipoSobreposicao.color = modalCtrl.estiloMapa.SOBREPOSICAO.color;
		tipoSobreposicao.nomeLayer = modalCtrl.camadasSobreposicao[tipoSobreposicao.codigo];
		tipoSobreposicao.tipo = tipoSobreposicao.codigo;
		tipoSobreposicao.estilo = {style: modalCtrl.estiloMapa.SOBREPOSICAO};
		tipoSobreposicao.popupText = tipoSobreposicao.nome;
		tipoSobreposicao.disableCentralizarGeometrias=false;

		$scope.$emit('mapa:adicionar-wmslayer-mapa', tipoSobreposicao, true);

	}

	function adicionarGeometriaNoMapa (camada, disable) {

		camada.visivel = true;
		camada.color = modalCtrl.estiloMapa[camada.tipo] != undefined ? modalCtrl.estiloMapa[camada.tipo].color : camada.estilo.color;

		$scope.$emit('mapa:adicionar-geometria-base', {
			geometria: JSON.parse(camada.geometria),
			tipo: camada.tipo,
			item: camada.item,
			estilo: {
				style: modalCtrl.estiloMapa[camada.tipo] || camada.estilo
			},
			popupText: camada.item,
			area: camada.area,
			disableCentralizarGeometrias:disable,
			numPoints: modalCtrl.numPoints
		});

	}

	function adicionarPointNoCluster (camada) {

		camada.visivel = true;
		$scope.$emit('mapa:adicionar-geometria-base-cluster', camada);

	}

	this.getCamadasSobreposicoes = function() {

		modalCtrl.TiposSobreposicao.forEach(function (tipo) {
			tipo.legenda = modalCtrl.LegendasTipoSobreposicao[tipo.codigo];
			adicionarWmsLayerNoMapa(tipo);
		});

	};

	this.controlaCentralizacaoCamadas = function (camada) {

		$scope.$emit('mapa:centralizar-camada', camada.geometria);

	};

	this.controlaExibicaoLayer = function(tipoSobreposicao) {

		$scope.$emit('mapa:controla-exibicao-wmslayer', tipoSobreposicao);

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

	this.resize = function(){

		if(modalCtrl.map) {

			modalCtrl.map._onResize();
			$scope.$emit('mapa:centralizar-mapa');

		}

	};

	modalCtrl.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

	modalCtrl.downloadDocumentoLicenciamento = function (idDocumento) {

		documentoLicenciamentoService.download(idDocumento);

	};

	function baixarDocumento(idDocumento) {

		documentoLicenciamentoService.download(idDocumento);
	}

	$(document).on( 'shown.bs.tab', 'a[data-toggle="tab"]', function (e) {

		var target = $(e.target).attr("data-target");

		if(target === '#tabCaracterizacao') {

			modalCtrl.resize();

		}
	});


	this.visualizarFichaImovel = function() {

		modalCtrl.abreDocumentacao = false;
		modalCtrl.exibirDocumentacao = !modalCtrl.abreDocumentacao;

		$location.hash('ficha');

		$anchorScroll();

	};

	this.downloadNotificacao = function(idTramitacao) {

		notificacaoService.downloadNotificacao(idTramitacao);
	};

	var abrirModal = function(parecer, idProcesso) {

		$uibModal.open({
			controller: 'visualizarJustificativasController',
			controllerAs: 'visualizarJustificativasCtlr',
			templateUrl: 'components/visualizacaoProcesso/modalVisualizarObservacao.html',
			size: 'lg',
			resolve: {

				parecer: function() {
					return parecer;
				},
				
				idProcesso: function() {
					return idProcesso;
				}

			}
		});

	};

	this.visualizarJustificativas =  function(idProcesso, historico){

		if(historico.idAcao === modalCtrl.acaoTramitacao.SOLICITAR_DESVINCULO){

			desvinculoService.buscarDesvinculoPeloProcessoGeo(idProcesso)
            	.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		} else if(historico.idAcao === modalCtrl.acaoTramitacao.VALIDAR_PARECER_GEO_GERENTE ||
				  historico.idAcao === modalCtrl.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_GERENTE ||
				  historico.idAcao === modalCtrl.acaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO) {

				parecerGerenteService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
					.then(function(response){
						abrirModal(response.data, idProcesso);
					});

		} else {

			parecerAnalistaGeoService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		}

	};

	this.validaJustificativas = function (tramitacao){
		
		return tramitacao.idAcao === modalCtrl.acaoTramitacao.DEFERIR_ANALISE_GEO || 
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.INDEFERIR_ANALISE_GEO ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.EMITIR_NOTIFICACAO ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.SOLICITAR_DESVINCULO ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.VALIDAR_PARECER_GEO_GERENTE ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_GERENTE ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO;

	};

	function getDataFimAnalise(dataFimAnalise) {

		if (dataFimAnalise) {

			return moment(dataFimAnalise, 'DD/MM/yyyy').startOf('day');
		} else {

			return moment(new Date()).startOf('day');
		}
	}

	this.isPrazoAnaliseVencido = function(dataFimAnalise, dataVencimentoAnalise){

		var momentDataFimAnalise = getDataFimAnalise(dataFimAnalise);

		return momentDataFimAnalise.isAfter(moment(dataVencimentoAnalise, 'DD/MM/yyyy').startOf('day'));
	};

	this.getDiasAnaliseJuridica = function() {

		if (this.dadosProcesso) {

			return this.dateUtil.calcularDias(this.dadosProcesso.analise.dataCadastro, this.dadosProcesso.analise.analiseJuridica.dataFim);
		}
	};

	this.getDiasAnaliseGeo = function() {

		if (this.dadosProcesso) {

			return this.dateUtil.calcularDias(this.dadosProcesso.analise.dataCadastro, this.dadosProcesso.analise.analiseGeo.dataFim);
		}
	};

	this.getDiasAnaliseTecnica = function() {

		if (this.dadosProcesso) {

			return this.dadosProcesso.analise.analiseTecnica ? this.dateUtil.calcularDias(this.dadosProcesso.analise.analiseTecnica.dataCadastro, this.dadosProcesso.analise.analiseTecnica.dataFim) : '-';
		}
	};

	this.getAnaliseTotal = function() {

		if (this.dadosProcesso) {

			var diasAnaliseTecnica = this.getDiasAnaliseTecnica(this.dadosProcesso.analise.analiseTecnica);
			diasAnaliseTecnica = diasAnaliseTecnica === '-' ? 0 : diasAnaliseTecnica;

			return this.getDiasAnaliseGeo() + diasAnaliseTecnica;
		}
	};

	function romanToInt(romano) {

		if(romano == null) return -1;

		var num = charToInt(romano.charAt(0));
		var pre, curr;

		for(var i = 1; i < romano.length; i++) {

			curr = charToInt(romano.charAt(i));
			pre = charToInt(romano.charAt(i-1));

			if(curr <= pre) {

				num += curr;

			} else {

				num = num - pre*2 + curr;

			}

		}

		return num;

	}

	function charToInt(c) {

		switch (c) {

			case 'I': return 1;
			case 'V': return 5;
			case 'X': return 10;
			case 'L': return 50;
			case 'C': return 100;
			case 'D': return 500;
			case 'M': return 1000;
			default: return -1;

		}

	}

	function getMaiorPotencialPoluidor(atividadesCaracterizacao) {

		if(!atividadesCaracterizacao)
			return;

		var maiorPotencialPoluidor = atividadesCaracterizacao[0].atividade.potencialPoluidor;

		_.forEach(atividadesCaracterizacao, function(atividadeCaracterizacao) {

			potencialPoluidor = atividadeCaracterizacao.atividade.potencialPoluidor;

			if(romanToInt(potencialPoluidor.codigo) > romanToInt(maiorPotencialPoluidor.codigo)) {
				maiorPotencialPoluidor = potencialPoluidor;
			}
		});

		return maiorPotencialPoluidor;

	}

};

exports.controllers.VisualizacaoProcessoController = VisualizacaoProcessoController;
