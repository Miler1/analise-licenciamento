var CadastroProcessoManejoController = function($scope, config, $rootScope, tipoLicencaManejoService, atividadeManejoService, municipioService, tipologiaManejoService, mensagem, $location, imovelService, processoManejoService) {

	$rootScope.tituloPagina = 'CADASTRAR PROCESSO MANEJO DIGITAL';

	var cadastroProcessoManejoController = this;

	cadastroProcessoManejoController.processo = criarProcesso();
	cadastroProcessoManejoController.tipologias = [];
	cadastroProcessoManejoController.tipologia = undefined;
	cadastroProcessoManejoController.atividades = [];
	cadastroProcessoManejoController.municipios = [];
	cadastroProcessoManejoController.licencas = [];
	cadastroProcessoManejoController.necessarioBuscarEmpreendimento = false;

	function criarProcesso() {

		var processo = {
			numeroProcesso: undefined,
			empreendimento: {
				denominacao: undefined,
				cpfCnpj: undefined,
				municipio: {
					id: undefined
				},
				imovel: {
					registroCar: undefined,
					municipio: {
						id: undefined
					},
					descricaoAcesso: undefined,
					nome: undefined,
					nomeSiglaMunicipio: undefined
				}
			},
			tipoLicenca: {
				id: undefined
			},
			atividadeManejo: {
				id: undefined
			}
		};

		return processo;
	}

	function apagarEmpreendimento() {

		cadastroProcessoManejoController.processo.empreendimento = {
			denominacao: undefined,
			cpfCnpj: undefined,
			municipio: cadastroProcessoManejoController.processo.empreendimento.municipio,
			imovel: {
				registroCar: undefined,
				municipio: {
					id: undefined
				},
				descricaoAcesso: undefined,
				nome: undefined,
				nomeSiglaMunicipio: undefined
			}
		};
	}

	cadastroProcessoManejoController.buscarImovel = function() {

		cadastroProcessoManejoController.necessarioBuscarEmpreendimento = false;

		imovelService.getImovelByCodigo(cadastroProcessoManejoController.processo.empreendimento.imovel.registroCar)
			.then(function(response) {

				cadastroProcessoManejoController.processo.empreendimento.denominacao = response.data.cadastrante.denominacao ? response.data.cadastrante.denominacao : response.data.cadastrante.nome;
				cadastroProcessoManejoController.processo.empreendimento.cpfCnpj = response.data.cadastrante.cnpj ? response.data.cadastrante.cnpj : response.data.cadastrante.cpf;

				cadastroProcessoManejoController.processo.empreendimento.imovel.descricaoAcesso = response.data.imovel.descricaoAcesso;
				cadastroProcessoManejoController.processo.empreendimento.imovel.nome = response.data.imovel.nome;
				cadastroProcessoManejoController.processo.empreendimento.imovel.municipio.id = response.data.imovel.codigoMunicipio;
				cadastroProcessoManejoController.processo.empreendimento.imovel.nomeSiglaMunicipio = response.data.imovel.nomeMunicipio + '/' + response.data.imovel.siglaEstado;

				_.forEach(response.data.geo, function(geo) {

					switch(geo.tema.codigo) {

						case 'AREA_IMOVEL':
							cadastroProcessoManejoController.processo.empreendimento.imovel.areaTotalImovelDocumentado = geo.area;
							break;

						case 'AREA_IMOVEL_LIQUIDA':
							cadastroProcessoManejoController.processo.empreendimento.imovel.areaLiquidaImovel = geo.area;
							break;

						case 'ARL_TOTAL':
							cadastroProcessoManejoController.processo.empreendimento.imovel.areaReservaLegal = geo.area;
							break;

						case 'APP_TOTAL':
							cadastroProcessoManejoController.processo.empreendimento.imovel.areaPreservacaoPermanente = geo.area;
							break;

						case 'VEGETACAO_NATIVA':
							cadastroProcessoManejoController.processo.empreendimento.imovel.areaRemanescenteVegetacaoNativa = geo.area;
							break;

						case 'AREA_CONSOLIDADA':
							cadastroProcessoManejoController.processo.empreendimento.imovel.areaUsoConsolidado = geo.area;
							break;
					}

				});

			}, function(error){

				apagarEmpreendimento();
				mensagem.error(error.data.texto);
			});
	};

	function init(){

		municipioService.getMunicipiosByUf('PA').then(
			function(response){

				cadastroProcessoManejoController.municipios = response.data;
			})
			.catch(function(){
				mensagem.error('Não foi possível obter a lista de municípios.');
			});

		tipologiaManejoService.findAll().then(
			function(response){

				cadastroProcessoManejoController.tipologias = response.data;
			})
			.catch(function(){
				mensagem.error('Não foi possível obter a lista de tipologias.');
			});

		tipoLicencaManejoService.findAll().then(
			function(response){

				cadastroProcessoManejoController.licencas = response.data;
			})
			.catch(function(){
				mensagem.error('Não foi possível obter a lista de licencas.');
			});
	}

	init();

	cadastroProcessoManejoController.excluirProcesso = function() {

		apagarEmpreendimento();
	};


	cadastroProcessoManejoController.voltar = function() {

		$location.path('/analise-manejo');
	};

	cadastroProcessoManejoController.cadastrar = function() {

		if(!formValido()) {

			if (cadastroProcessoManejoController.processo.empreendimento.imovel.registroCar && !cadastroProcessoManejoController.processo.empreendimento.denominacao) {

				cadastroProcessoManejoController.necessarioBuscarEmpreendimento = true;
				mensagem.error('É necessário buscar um imóvel no CAR.', { ttl: 10000 });
				return;
			}

			mensagem.error('Verifique os campos obrigatórios.', { ttl: 10000 });
			return;
		}

		cadastroProcessoManejoController.processo.tipoLicenca.id = 1;

		processoManejoService.salvarProcesso(cadastroProcessoManejoController.processo)
			.then(function(response){

				$location.path('/analise-manejo');

			})
			.catch(function(response){

				if(!!response.data.texto)
					mensagem.warning(response.data.texto);

				else
					mensagem.error("Ocorreu um erro ao salvar o processo.");
			});
	};

	function formValido() {

		$scope.formularioCadastroProcessoManejo.$setSubmitted();
		return $scope.formularioCadastroProcessoManejo.$valid && cadastroProcessoManejoController.processo.empreendimento.denominacao;
	}

	cadastroProcessoManejoController.buscarAtividades = function () {

		if (cadastroProcessoManejoController.tipologia) {

			atividadeManejoService.findByTipologia(cadastroProcessoManejoController.tipologia).then(
				function(response){

					cadastroProcessoManejoController.atividades = response.data;
				})
				.catch(function(){
					mensagem.error('Não foi possível obter a lista de atividades.');
				});
		}
	};

	cadastroProcessoManejoController.conferirNumeroProcesso = function (numeroProcesso) {

		processoManejoService.findByNumeroProcesso(numeroProcesso).then(
			function(response){

				var existe = response.data;

				if(existe) {

					mensagem.error('Já existe um processo com este número cadastrado.');
				}
			})
			.catch(function(){
				mensagem.error('Não foi possível consultar o numero do processo.');
			});
	};
};

exports.controllers.CadastroProcessoManejoController = CadastroProcessoManejoController;