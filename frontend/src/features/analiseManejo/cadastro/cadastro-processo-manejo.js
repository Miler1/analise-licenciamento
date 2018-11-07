var CadastroProcessoManejoController = function($scope, config, $rootScope, processoManejoService, tipoLicencaService, atividadeService, mensagem, $location, imovelService) {

	$rootScope.tituloPagina = 'CADASTRAR PROCESSO MANEJO DIGITAL';

	var cadastroProcessoManejoController = this;

	cadastroProcessoManejoController.processo = {
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
			}
		},
		tipoLicenca: {
			id: undefined
		},
		atividadeManejo: {
			id: undefined
		}
	};
	cadastroProcessoManejoController.tipologia = [];
	cadastroProcessoManejoController.atividade = [];
	cadastroProcessoManejoController.municipio = [];
	cadastroProcessoManejoController.licenca = [];

	cadastroProcessoManejoController.buscarImovel = function() {

		imovelService.getImovelByCodigo(cadastroProcessoManejoController.processo.empreendimento.imovel.registroCar)
			.then(function(response) {

				console.log(response);

			}, function(error){

				mensagem.error(error.data.texto);
			});
	};

	this.$postLink = function(){

		cadastroProcessoManejoService.getMunicipiosByUf('PA').then(
			function(response){

				cadastroProcessoManejoController.municipio = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de municípios.');
			});

		cadastroProcessoManejoService.findTipologias().then(
			function(response){

				cadastroProcessoManejoController.tipologia = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de tipologias.');
			});

		atividadeService.getAtividades().then(
			function(response){

				cadastroProcessoManejoController.atividade = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de atividades.');
			});

		tipoLicencaService.getTiposLicencas().then(
			function(response){

				cadastroProcessoManejoController.licenca = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de licencas.');
			});
	};

};

exports.controllers.CadastroProcessoManejoController = CadastroProcessoManejoController;