var VisualizacaoProcessoController = function ($location, $anchorScroll, $timeout, $uibModalInstance, processo, $scope, processoService, mensagem, municipioService, documentoLicenciamentoService, imovelService, notificacaoService) {

	var modalCtrl = this;

	modalCtrl.processo = processo;

	modalCtrl.baixarDocumento = baixarDocumento;
	modalCtrl.dateUtil = app.utils.DateUtil;
	modalCtrl.PrazoAnalise = app.utils.PrazoAnalise;
	modalCtrl.getMaiorPotencialPoluidor = getMaiorPotencialPoluidor;

	modalCtrl.abreDocumentacao = true;

	modalCtrl.exibirDocumentacao = !modalCtrl.abreDocumentacao;

	var estiloPoligono = {
		color: 'tomato',
		opacity: 1,
		weight: 3,
		fillOpacity: 0.2
	};

	processoService.getInfoProcesso(processo.idProcesso)
		.then(function(response){

			modalCtrl.dadosProcesso = response.data;
			modalCtrl.limite = modalCtrl.dadosProcesso.empreendimento.imovel ? modalCtrl.dadosProcesso.empreendimento.imovel.limite : modalCtrl.dadosProcesso.empreendimento.municipio.limite;
			modalCtrl.inicializarMapa();

		})
		.catch(function(){
			mensagem.error("Ocorreu um erro ao buscar dados do processo.");
		});

	modalCtrl.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};


	function baixarDocumento(idDocumento) {

		documentoLicenciamentoService.download(idDocumento);
	}


	// Métodos referentes ao Mapa da caracterização
	this.inicializarMapa = function() {

		modalCtrl.map = new L.Map('mapa-processo', {
			zoomControl: false,
			minZoom: 5,
			maxZoom: 16,
			scrollWheelZoom: false
		}).setView([-3, -52.497545], 6);

		/* Termos de uso: http://downloads2.esri.com/ArcGISOnline/docs/tou_summary.pdf */
		L.tileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
			attribution: 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community'
		}).addTo(modalCtrl.map);

		modalCtrl.map.on('moveend click', function() {

			if (!modalCtrl.map.scrollWheelZoom.enabled()) {

				modalCtrl.map.scrollWheelZoom.enable();

			}

		});

		window.onscroll = function () {

			if (modalCtrl.map.scrollWheelZoom.enabled()) {

				modalCtrl.map.scrollWheelZoom.disable();

			}

		};

		modalCtrl.atualizarMapa();
	};

	$(document).on( 'shown.bs.tab', 'a[data-toggle="tab"]', function (e) {

		var target = $(e.target).attr("data-target");

		if(target === '#tabCaracterizacao') {

			modalCtrl.resize();
		}
	});

	this.resize = function(){

		if(modalCtrl.map) {

			modalCtrl.map._onResize();

			modalCtrl.centralizarGeometria();

		}

	};


	this.atualizarMapa = function() {

		modalCtrl.inserirGeometria(modalCtrl.limite);

		modalCtrl.inserirEmpreendimento(modalCtrl.dadosProcesso.empreendimento.coordenadas);

		modalCtrl.map._onResize();
	};

	this.inserirGeometria = function(geo) {

		if(geo && modalCtrl.map) {

			var objectGeo = JSON.parse(geo);

			modalCtrl.areaGeometria = L.geoJson({
				'type': 'Feature',
				'geometry': objectGeo
			}, estiloPoligono);


			modalCtrl.map.addLayer(modalCtrl.areaGeometria);

		}

	};

	this.centralizarGeometria = function() {

		if(modalCtrl.areaGeometria){
			$timeout(function(){
				modalCtrl.map.fitBounds(modalCtrl.areaGeometria.getBounds());
			}, 200);
		}

	};

	this.inserirEmpreendimento = function(localizacao) {

		if(localizacao && modalCtrl.map) {

			var objectGeo = JSON.parse(localizacao);

			modalCtrl.localizacao = L.geoJson({
				'type': 'Feature',
				'geometry': objectGeo
			});

			modalCtrl.map.addLayer(modalCtrl.localizacao);

		}

	};

	this.visualizarFichaImovel = function() {

		modalCtrl.abreDocumentacao = false;
		modalCtrl.exibirDocumentacao = !modalCtrl.abreDocumentacao;

		$location.hash('ficha');

		$anchorScroll();

	};

	this.downloadNotificacao = function(idTramitacao) {

		notificacaoService.downloadNotificacao(idTramitacao);
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

	this.getDiasAnaliseTecnica = function() {

		if (this.dadosProcesso) {

			return this.dadosProcesso.analise.analiseTecnica ? this.dateUtil.calcularDias(this.dadosProcesso.analise.analiseTecnica.dataCadastro, this.dadosProcesso.analise.analiseTecnica.dataFim) : '-';
		}
	};

	this.getAnaliseTotal = function() {

		if (this.dadosProcesso) {

			var diasAnaliseTecnica = this.getDiasAnaliseTecnica(this.dadosProcesso.analise.analiseTecnica);
			diasAnaliseTecnica = diasAnaliseTecnica === '-' ? 0 : diasAnaliseTecnica;

			return this.getDiasAnaliseJuridica() + diasAnaliseTecnica;
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
