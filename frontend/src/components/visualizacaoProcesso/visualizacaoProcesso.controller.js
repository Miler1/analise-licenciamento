var VisualizacaoProcessoController = function ($location, $injector, desvinculoService,
											   $uibModal, $scope, $rootScope, $timeout,
											   $uibModalInstance, processo, mensagem,
											   $anchorScroll,processoService, documentoService,
											   empreendimentoService, notificacaoService,
											   documentoLicenciamentoService, analiseGeoService, parecerDiretorTecnicoService,
											   parecerAnalistaGeoService, parecerGerenteService,parecerSecretarioService,
											   tiposSobreposicaoService,parecerAnalistaTecnicoService) {

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
	modalCtrl.dadosProjeto = null;
	modalCtrl.TiposSobreposicao = [];
	modalCtrl.openedAccordionGeo = false;
	modalCtrl.openedAccordionGerente = false;
	modalCtrl.labelAnalistaGeo = '';
	modalCtrl.labelGerente = '';
	modalCtrl.parecer = {};
	modalCtrl.pareceres = {};
	modalCtrl.pareceresDiretor = {};
	modalCtrl.pareceresSecretario ={};
	modalCtrl.pareceresTecnicos = {};
	modalCtrl.documentos = [];
	modalCtrl.processosAnteriores = [];
	modalCtrl.tramitacoes = [];
	modalCtrl.tipoContato = app.TIPO_CONTATO;
	modalCtrl.tipoEndereco = app.TIPO_ENDERECO;

	modalCtrl.contatoPrincipal = {
		email: null,
		telefone:null,
		celular: null
	};

	modalCtrl.enderecosEU = {
		principal: {},
		correspondencia: {}
	};

	$injector.invoke(exports.controllers.PainelMapaController, this,
		{
			$scope: $scope,
			$timeout: $timeout
		}
	);

	modalCtrl.setPareceres = function() {

		if(modalCtrl.usuarioLogadoCodigoPerfil === modalCtrl.perfis.ANALISTA_GEO) {

			modalCtrl.pareceres = modalCtrl.dadosProcesso.analise.analiseGeo.pareceresAnalistaGeo;

			modalCtrl.dadosProcesso.analise.analiseGeo.pareceresGerenteAnaliseGeo.forEach(function(parecerGerente) {

				if(parecerGerente.tipoResultadoAnalise.id === modalCtrl.tiposResultadoAnaliseUtils.SOLICITAR_AJUSTES) {

					modalCtrl.pareceres = modalCtrl.pareceres.concat(parecerGerente);
				}

			});
			
		} else if(modalCtrl.usuarioLogadoCodigoPerfil === modalCtrl.perfis.GERENTE || 
				modalCtrl.usuarioLogadoCodigoPerfil === modalCtrl.perfis.DIRETOR ||
				modalCtrl.usuarioLogadoCodigoPerfil === modalCtrl.perfis.SECRETARIO) {

			modalCtrl.pareceres = modalCtrl.dadosProcesso.analise.analiseGeo.pareceresAnalistaGeo;

			modalCtrl.pareceres = modalCtrl.pareceres.concat(modalCtrl.dadosProcesso.analise.analiseGeo.pareceresGerenteAnaliseGeo);

			if (modalCtrl.dadosProcesso.analise.analiseTecnica !== null) {

				modalCtrl.pareceresTecnicos = modalCtrl.dadosProcesso.analise.analiseTecnica.pareceresAnalistaTecnico;
				modalCtrl.pareceresTecnicos = modalCtrl.pareceresTecnicos.concat(modalCtrl.dadosProcesso.analise.analiseTecnica.pareceresGerenteAnaliseTecnica);

			}
			
			modalCtrl.pareceresDiretor = modalCtrl.dadosProcesso.analise.pareceresDiretorTecnico;
			modalCtrl.pareceresSecretario = modalCtrl.dadosProcesso.analise.pareceresSecretario;

		} else if(modalCtrl.usuarioLogadoCodigoPerfil === modalCtrl.perfis.ANALISTA_TECNICO) {

			modalCtrl.pareceresTecnicos = modalCtrl.dadosProcesso.analise.analiseTecnica.pareceresAnalistaTecnico;
			modalCtrl.dadosProcesso.analise.analiseTecnica.pareceresGerenteAnaliseTecnica.forEach(function(parecerGerente) {

				if(parecerGerente.tipoResultadoAnalise.id === modalCtrl.tiposResultadoAnaliseUtils.SOLICITAR_AJUSTES) {

					modalCtrl.pareceresTecnicos = modalCtrl.pareceresTecnicos.concat(parecerGerente);
				}

			});

		}

		if (!_.isEmpty(modalCtrl.pareceres)) {

			modalCtrl.pareceres = modalCtrl.pareceres.sort(function(processo1, processo2){

				if(modalCtrl.dateUtil.isBefore(processo1.dataParecer, processo2.dataParecer)) {

					return 1;

				} else if(modalCtrl.dateUtil.isAfter(processo1.dataParecer, processo2.dataParecer)) {

					return -1;

				} else {

					return 0;

				}

			});
		}

		if (!_.isEmpty(modalCtrl.pareceresTecnicos)) {

			modalCtrl.pareceresTecnicos = modalCtrl.pareceresTecnicos.sort(function(processo1, processo2){

				if(modalCtrl.dateUtil.isBefore(processo1.dataParecer, processo2.dataParecer)) {

					return 1;

				} else if(modalCtrl.dateUtil.isAfter(processo1.dataParecer, processo2.dataParecer)) {

					return -1;

				} else {

					return 0;

				}

			});
		}


	};

	modalCtrl.setDocumentos = function() {

		var ultimoParecer = modalCtrl.dadosProcesso.analise.analiseGeo.pareceresAnalistaGeo[0];

		if (ultimoParecer !== null && ultimoParecer !== undefined) {

			if (ultimoParecer.documentoParecer !== null) {

				modalCtrl.documentos.push(ultimoParecer.documentoParecer);
			}

			if (ultimoParecer.cartaImagem !== null) {

				modalCtrl.documentos.push(ultimoParecer.cartaImagem);

			}

		}

		ultimoParecer = modalCtrl.dadosProcesso.analise.analiseTecnica;

		if (ultimoParecer !== null && ultimoParecer !== undefined) {

			if(!_.isEmpty(ultimoParecer.pareceresAnalistaTecnico)) {

				if (ultimoParecer.pareceresAnalistaTecnico[0].documentoParecer) {

					modalCtrl.documentos.push(ultimoParecer.pareceresAnalistaTecnico[0].documentoParecer);

				}

				if (ultimoParecer.pareceresAnalistaTecnico[0].documentoMinuta) {

					modalCtrl.documentos.push(ultimoParecer.pareceresAnalistaTecnico[0].documentoMinuta);

				}

				if (ultimoParecer.pareceresAnalistaTecnico[0].vistoria && ultimoParecer.pareceresAnalistaTecnico[0].vistoria.documentoRelatorioTecnicoVistoria) {

					modalCtrl.documentos.push(ultimoParecer.pareceresAnalistaTecnico[0].vistoria.documentoRelatorioTecnicoVistoria);

				}

			}

		}

	};

	modalCtrl.setPareceresAntigos = function(processo) {

		if (processo.analise.analiseTecnica !== null) {
			
			modalCtrl.dadosProcesso.analise.analiseTecnica.pareceresAnalistaTecnico = modalCtrl.dadosProcesso.analise.analiseTecnica.pareceresAnalistaTecnico.concat(processo.analise.analiseTecnica.pareceresAnalistaTecnico);
			processo.analise.analiseTecnica.pareceresGerenteAnaliseTecnica.forEach(function(parecerGerente) {

				modalCtrl.dadosProcesso.analise.analiseTecnica.pareceresGerenteAnaliseTecnica = modalCtrl.dadosProcesso.analise.analiseTecnica.pareceresGerenteAnaliseTecnica.concat(parecerGerente);

			}); 

		}

		if (processo.analise.analiseGeo !== null && processo.analise.analiseTecnica === null) {

			modalCtrl.dadosProcesso.analise.analiseGeo.pareceresAnalistaGeo = modalCtrl.dadosProcesso.analise.analiseGeo.pareceresAnalistaGeo.concat(processo.analise.analiseGeo.pareceresAnalistaGeo);
			processo.analise.analiseGeo.pareceresGerenteAnaliseGeo.forEach(function(parecerGerente) {

				modalCtrl.dadosProcesso.analise.analiseGeo.pareceresGerenteAnaliseGeo = modalCtrl.dadosProcesso.analise.analiseGeo.pareceresGerenteAnaliseGeo.concat(parecerGerente);

			}); 
		
		}
		
	};

	modalCtrl.getDocumentosSolicitacao = function(){

        var documentosSolicitacaoGrupo = [];

        _.forEach(modalCtrl.dadosProcesso.caracterizacao.documentosSolicitacaoGrupo, function(documentoTecnicoAmbiental){
            
            if (documentoTecnicoAmbiental.documento != null) {

				documentosSolicitacaoGrupo = documentosSolicitacaoGrupo.concat(documentoTecnicoAmbiental);
            }
            
        });

        modalCtrl.dadosProcesso.caracterizacao.documentosSolicitacaoGrupo = documentosSolicitacaoGrupo;
    };

	if (processo.idProcesso) {

		processoService.getInfoProcesso(processo.idProcesso)
			.then(function(response){

				modalCtrl.dadosProcesso = response.data;
				modalCtrl.limite = modalCtrl.dadosProcesso.empreendimento.imovel ? modalCtrl.dadosProcesso.empreendimento.imovel.limite : modalCtrl.dadosProcesso.empreendimento.municipio.limite;
				modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.empreendedor.pessoa.contatos = getContatoPessoa(modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.empreendedor.pessoa.contatos, modalCtrl.contatoPrincipal);
				modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.contatos = getContatoPessoa(modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.contatos, modalCtrl.contatoPrincipal);
				modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.enderecos = getEndereco(modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.enderecos,modalCtrl.enderecosEU);
				modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.empreendedor.pessoa.enderecos = getEndereco(modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.empreendedor.pessoa.enderecos,modalCtrl.enderecosEU);
				setResponsaveisLegaisTecnicos();

				if (modalCtrl.dadosProcesso.processoAnterior != null) {

					processoService.getProcessosAnteriores(modalCtrl.dadosProcesso.processoAnterior.id)
						.then(function(response){
					
						modalCtrl.processosAnteriores = response.data;

						_.forEach(modalCtrl.processosAnteriores, function(processo){

							modalCtrl.setPareceresAntigos(processo);

							_.forEach(processo.historicoTramitacao, function(tramitacao){

								if (tramitacao.idCondicaoFinal !== modalCtrl.comparaStatus.ARQUIVADO) {

									modalCtrl.tramitacoes = modalCtrl.tramitacoes.concat(tramitacao);
									
								}

							});

						});

						modalCtrl.setPareceres();
						modalCtrl.setDocumentos();
						modalCtrl.getDocumentosSolicitacao();
					});

				} else {

					modalCtrl.setPareceres();
					modalCtrl.setDocumentos();
					modalCtrl.getDocumentosSolicitacao();

				}
			})
			.catch(function(){
				mensagem.error("Ocorreu um erro ao buscar dados do protocolo.");
			});

	} else {

		processoService.getInfoProcessoByNumero(processo.numeroProcesso)
		.then(function(response){

			modalCtrl.dadosProcesso = response.data;
			modalCtrl.limite = modalCtrl.dadosProcesso.empreendimento.imovel ? modalCtrl.dadosProcesso.empreendimento.imovel.limite : modalCtrl.dadosProcesso.empreendimento.municipio.limite;
			modalCtrl.setPareceres();
			modalCtrl.setDocumentos();
		})
		.catch(function(){
			mensagem.error("Ocorreu um erro ao buscar dados do protocolo.");
		});

	}

	modalCtrl.setLabelAnalistas = function(tipoResultadoAnalistaGeo) {

		if(tipoResultadoAnalistaGeo.id === modalCtrl.tiposResultadoAnaliseUtils.DEFERIDO) {

			return 'Despacho';

		} else if(tipoResultadoAnalistaGeo.id === modalCtrl.tiposResultadoAnaliseUtils.INDEFERIDO) {

			return 'Justificativa';

		} else if(tipoResultadoAnalistaGeo.id === modalCtrl.tiposResultadoAnaliseUtils.EMITIR_NOTIFICACAO) {

			return 'Descrição da solicitação';

		}

	};

	modalCtrl.condicoesAnalistas = function(parecer) {

		if (parecer.tipoResultadoAnalise.id !== modalCtrl.tiposResultadoAnaliseUtils.SOLICITAR_AJUSTES &&
			parecer.tipoResultadoAnalise.id !== modalCtrl.tiposResultadoAnaliseUtils.PARECER_NAO_VALIDADO &&
			parecer.tipoResultadoAnalise.id !== modalCtrl.tiposResultadoAnaliseUtils.PARECER_VALIDADO) {

			return true;

		}

		return false;

	};

	modalCtrl.condicoesGerentes = function(parecer) {

		if (parecer.tipoResultadoAnalise.id === modalCtrl.tiposResultadoAnaliseUtils.SOLICITAR_AJUSTES ||
			parecer.tipoResultadoAnalise.id === modalCtrl.tiposResultadoAnaliseUtils.PARECER_NAO_VALIDADO ||
			parecer.tipoResultadoAnalise.id === modalCtrl.tiposResultadoAnaliseUtils.PARECER_VALIDADO) {

			return true;

		}

		return false;

	};

	modalCtrl.verificaLoginAnaliseGeo = function() {

		if (modalCtrl.perfis.ANALISTA_GEO === modalCtrl.usuarioLogadoCodigoPerfil ||
			modalCtrl.perfis.GERENTE === modalCtrl.usuarioLogadoCodigoPerfil ||
			modalCtrl.perfis.DIRETOR === modalCtrl.usuarioLogadoCodigoPerfil ||
			modalCtrl.perfis.SECRETARIO === modalCtrl.usuarioLogadoCodigoPerfil) {

			return true;

		}

		return false;

	};

	modalCtrl.verificaLoginAnaliseTecnica = function() {

		if (modalCtrl.perfis.ANALISTA_TECNICO === modalCtrl.usuarioLogadoCodigoPerfil ||
			modalCtrl.perfis.GERENTE === modalCtrl.usuarioLogadoCodigoPerfil ||
			modalCtrl.perfis.DIRETOR === modalCtrl.usuarioLogadoCodigoPerfil ||
			modalCtrl.perfis.SECRETARIO === modalCtrl.usuarioLogadoCodigoPerfil) {

			return true;

		}

		return false;

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

	this.setLabelRestricao = function() {

		if(modalCtrl.dadosProjeto !== null) {

			if(modalCtrl.dadosProjeto.categoria === modalCtrl.categoria.COMPLEXO || modalCtrl.dadosProjeto.complexo) {

				return 'Restrições do complexo';

			} else if(modalCtrl.dadosProjeto.categoria === modalCtrl.categoria.PROPRIEDADE) {

				return 'Restrições do empreendimento';

			} else {

				return 'Restrições da(s) atividade(s)';

			}
		}

	};

	// Métodos referentes ao Mapa da caracterização
	this.iniciarMapa = function() {

		if(modalCtrl.map === null) {

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

				var cpfCnpjEmpreendimento = empreendimento.cpfCnpj;

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

						centralizarMapa();

					});

				});

			});

		}

	};

	function centralizarMapa() {

		if(modalCtrl.dadosProjeto) {

			var bounds = new L.latLngBounds();

			if(modalCtrl.dadosProjeto.categoria === modalCtrl.categoria.PROPRIEDADE) {

				modalCtrl.camadasDadosEmpreendimento.forEach(function(camada) {

					camada.geometrias.forEach(function(geometriaEmpreendimento) {

						bounds.extend(L.geoJSON(JSON.parse(geometriaEmpreendimento.geometria)).getBounds());

					});

				});

			} else if(modalCtrl.dadosProjeto.categoria === modalCtrl.categoria.COMPLEXO || modalCtrl.dadosProjeto.complexo) {

				modalCtrl.dadosProjeto.complexo.geometrias.forEach(function(geometriaComplexo) {

					bounds.extend(L.geoJSON(JSON.parse(geometriaComplexo.geometria)).getBounds());

				});

			} else {

				modalCtrl.dadosProjeto.atividades.forEach(function(atividade) {

					atividade.geometrias.forEach(function(geometriaAtividade) {

						bounds.extend(L.geoJSON(JSON.parse(geometriaAtividade.geometria)).getBounds());

					});

				});

			}

			$scope.$emit('mapa:centralizar-geometrias', bounds);

		}

	}

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
			numPoints: modalCtrl.numPoints,
			cpfCnpjAreaSobreposicao: camada.cpfCnpjAreaSobreposicao,
			dataAreaSobreposicao: camada.dataAreaSobreposicao,
			nomeAreaSobreposicao: camada.nomeAreaSobreposicao
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
			centralizarMapa();

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

	this.downloadDocumentos = function (id) {

		documentoService.downloadById(id);

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

	this.visualizarJustificativas = function(idProcesso, historico){

		if(historico.idAcao === modalCtrl.acaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_GEO){

			desvinculoService.buscarDesvinculoPeloProcessoGeo(idProcesso)
            	.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		} else if(historico.idAcao === modalCtrl.acaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_TECNICA){

			desvinculoService.buscarDesvinculoPeloProcessoTecnico(idProcesso)
            	.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		}else if(historico.idAcao === modalCtrl.acaoTramitacao.VALIDAR_PARECER_GEO_GERENTE ||
				  historico.idAcao === modalCtrl.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_GERENTE ||
				  historico.idAcao === modalCtrl.acaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO) {

				parecerGerenteService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
					.then(function(response){
						abrirModal(response.data, idProcesso);
					});

		}else if(historico.idAcao === modalCtrl.acaoTramitacao.DEFERIR_ANALISE_TECNICA_VIA_GERENTE ||
				historico.idAcao === modalCtrl.acaoTramitacao.INDEFERIR_ANALISE_TECNICA_VIA_GERENTE ||
				historico.idAcao === modalCtrl.acaoTramitacao.NOTIFICAR_PELO_ANALISTA_TECNICO){

			parecerAnalistaTecnicoService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		} else if(historico.idAcao === modalCtrl.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_GERENTE ||
				historico.idAcao === modalCtrl.acaoTramitacao.VALIDAR_PARECER_TECNICO_GERENTE || 
				historico.idAcao === modalCtrl.acaoTramitacao.INVALIDAR_PARECER_TECNICO_ENCAMINHANDO_TECNICO){
			
			parecerGerenteService.findParecerTecnicoByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		} else if(historico.idAcao === modalCtrl.acaoTramitacao.VALIDAR_ANALISE_PELO_DIRETOR ||
			historico.idAcao === modalCtrl.acaoTramitacao.INVALIDAR_ANALISE_PELO_DIRETOR ){
		
			parecerDiretorTecnicoService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		}else if(historico.idAcao === modalCtrl.acaoTramitacao.APROVAR_SOLICITACAO_LICENCA ||
			historico.idAcao === modalCtrl.acaoTramitacao.NEGAR_SOLICITACAO_LICENCA ){
		
			parecerSecretarioService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
				});
		}else {

			parecerAnalistaGeoService.findParecerByIdHistoricoTramitacao(historico.idHistorico)
				.then(function(response){
					abrirModal(response.data, idProcesso);
				});

		}

	};

	this.validaJustificativas = function (tramitacao){

		return tramitacao.idAcao === modalCtrl.acaoTramitacao.DEFERIR_ANALISE_GEO ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.INDEFERIR_ANALISE_GEO ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.NOTIFICAR_PELO_ANALISTA_GEO ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.NOTIFICAR_PELO_ANALISTA_TECNICO ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_GEO ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_TECNICA ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.VALIDAR_PARECER_GEO_GERENTE ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_GERENTE ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_GERENTE ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.AGUARDAR_RESPOSTA_COMUNICADO||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.INDEFERIR_ANALISE_TECNICA_VIA_GERENTE ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.DEFERIR_ANALISE_TECNICA_VIA_GERENTE ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.VALIDAR_PARECER_TECNICO_GERENTE ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.VALIDAR_ANALISE_PELO_DIRETOR ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.INVALIDAR_ANALISE_PELO_DIRETOR ||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.APROVAR_SOLICITACAO_LICENCA || 
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.INVALIDAR_PARECER_TECNICO_ENCAMINHANDO_TECNICO||
		   tramitacao.idAcao === modalCtrl.acaoTramitacao.NEGAR_SOLICITACAO_LICENCA;
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

	function getContatoPessoa(listaContatos, contatoPrincipal){

		_.forEach(listaContatos, function (contato){
					
			if(contato.principal === true && contato.tipo.id === modalCtrl.tipoContato.EMAIL)
				contatoPrincipal.email = contato.valor;
			else if (contato.tipo.id === modalCtrl.tipoContato.TELEFONE_RESIDENCIAL)
				contatoPrincipal.telefone = contato.valor;
			else if (contato.tipo.id === modalCtrl.tipoContato.TELEFONE_CELULAR)
				contatoPrincipal.celular = contato.valor;
			 
		});	
		return contatoPrincipal;
	}

	function getEndereco(listaEnderecos, enderecos) {

		_.forEach(listaEnderecos, function(endereco){

			if(endereco.tipo.id === modalCtrl.tipoEndereco.PRINCIPAL)
				enderecos.principal = endereco; 
			else
				enderecos.correspondencia = endereco;
		});
		return enderecos;
	}
	
	function setResponsaveisLegaisTecnicos() {

		modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.responsaveis = [];

		_.forEach(modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.responsaveisLegais, function(responsavelLegal){
			modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.responsaveis.push(responsavelLegal);
		});

		_.forEach(modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.responsaveisTecnicos, function(responsavelTecnico){
			modalCtrl.dadosProcesso.empreendimento.empreendimentoEU.responsaveis.push(responsavelTecnico);
		});

	}


};

exports.controllers.VisualizacaoProcessoController = VisualizacaoProcessoController;
