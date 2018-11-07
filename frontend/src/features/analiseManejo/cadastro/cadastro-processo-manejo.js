var CadastroProcessoManejoController = function($scope, config, $rootScope, tipoLicencaManejoService, atividadeManejoService, municipioService, tipologiaManejoService, mensagem, $location, imovelService) {

	$rootScope.tituloPagina = 'CADASTRAR PROCESSO MANEJO DIGITAL';

	var cadastroProcessoManejoController = this;

	cadastroProcessoManejoController.processo = criarProcesso();
	cadastroProcessoManejoController.tipologias = [];
	cadastroProcessoManejoController.tipologia = undefined;
	cadastroProcessoManejoController.atividades = [];
	cadastroProcessoManejoController.municipios = [];
	cadastroProcessoManejoController.licencas = [];

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

	cadastroProcessoManejoController.buscarImovel = function() {

		imovelService.getImovelByCodigo(cadastroProcessoManejoController.processo.empreendimento.imovel.registroCar)
			.then(function(response) {

				cadastroProcessoManejoController.processo.empreendimento.denominacao = response.data.cadastrante.denominacao ? response.data.cadastrante.denominacao : response.data.cadastrante.nome;
				cadastroProcessoManejoController.processo.empreendimento.cpfCnpj = response.data.cadastrante.cnpj ? response.data.cadastrante.cnpj : response.data.cadastrante.cpf;
				cadastroProcessoManejoController.processo.empreendimento.municipio.id = response.data.cadastrante.endereco.municipio;

				cadastroProcessoManejoController.processo.empreendimento.imovel.descricaoAcesso = response.data.imovel.descricaoAcesso;
				cadastroProcessoManejoController.processo.empreendimento.imovel.nome = response.data.imovel.nome;
				cadastroProcessoManejoController.processo.empreendimento.imovel.municipio.id = response.data.imovel.codigoMunicipio;
				cadastroProcessoManejoController.processo.empreendimento.imovel.nomeSiglaMunicipio = response.data.imovel.nomeMunicipio + '/' + response.data.imovel.siglaEstado;

			}, function(error){

				cadastroProcessoManejoController.processo = criarProcesso();
				mensagem.error(error.data.texto);
			});
	};

	function init(){

		municipioService.getMunicipiosByUf('PA').then(
			function(response){

				cadastroProcessoManejoController.municipios = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de municípios.');
			});

		tipologiaManejoService.findAll().then(
			function(response){

				cadastroProcessoManejoController.tipologias = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de tipologias.');
			});

		tipoLicencaManejoService.findAll().then(
			function(response){

				cadastroProcessoManejoController.licencas = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de licencas.');
			});
	};

	init();

	cadastroProcessoManejoController.excluirProcesso = function() {

		cadastroProcessoManejoController.processo = criarProcesso();
	};


	cadastroProcessoManejoController.voltar = function() {

		$location.path('/analise-manejo');
	};

	cadastroProcessoManejoController.cadastrar = function() {

		if(!formValido()) {

			mensagem.error('Verifique os campos obrigatórios.', { ttl: 10000 });
			return;
		}
	};

	function formValido() {

		return false;
	}

	cadastroProcessoManejoController.buscarAtividades = function () {

		if (cadastroProcessoManejoController.tipologia) {

			atividadeManejoService.findByTipologia(cadastroProcessoManejoController.tipologia).then(
				function(response){

					cadastroProcessoManejoController.atividades = response.data;
				})
				.catch(function(){
					mensagem.warning('Não foi possível obter a lista de atividades.');
				});
		}
	}
};

exports.controllers.CadastroProcessoManejoController = CadastroProcessoManejoController;