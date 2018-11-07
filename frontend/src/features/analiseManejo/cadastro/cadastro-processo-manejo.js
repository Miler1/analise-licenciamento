var CadastroProcessoManejoController = function($scope, config, $rootScope, cadastroProcessoManejoService, cadastroManejoService, tipoLicencaService, atividadeService,  mensagem, $location) {

	$rootScope.tituloPagina = 'CADASTRAR PROCESSO MANEJO DIGITAL';

	var cadastroProcessoManejoController = this;

	cadastroProcessoManejoController.processo = null;
	cadastroProcessoManejoController.tipologias = [];
	cadastroProcessoManejoController.atividades = [];
	cadastroProcessoManejoController.municipios = [];
	cadastroProcessoManejoController.licencas = [];
	cadastroProcessoManejoController.car = null;
	cadastroProcessoManejoController.car.numero = null;
	cadastroProcessoManejoController.car.proprietario.cpfCnpj = null;
	cadastroProcessoManejoController.car.proprietario.nome = null;
	cadastroProcessoManejoController.car.proprietario.endereco = null;
	cadastroProcessoManejoController.car.proprietario.municipio = null;



	this.$postLink = function(){

		cadastroProcessoManejoService.getMunicipiosByUf('PA').then(
			function(response){

				cadastroProcessoManejoController.municipios = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de municípios.');
			});

		cadastroProcessoManejoService.findTipologias().then(
			function(response){

				cadastroProcessoManejoController.tipologias = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de tipologias.');
			});

		atividadeService.getAtividades().then(
			function(response){

				cadastroProcessoManejoController.atividades = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de atividades.');
			});

		tipoLicencaService.getTiposLicencas().then(
			function(response){

				cadastroProcessoManejoController.licencas = response.data;
			})
			.catch(function(){
				mensagem.warning('Não foi possível obter a lista de licencas.');
			});
	};

	templateUrl: 'components/features/analiseManejo/cadastro/cadastro-processo-manejo.html'

};

exports.controllers.CadastroProcessoManejoController = CadastroProcessoManejoController;