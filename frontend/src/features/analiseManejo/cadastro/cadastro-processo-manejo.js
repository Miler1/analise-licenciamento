var CadastroProcessoManejoController = function($scope, config, $rootScope, processoManejoService, mensagem, $location, imovelService) {

	$rootScope.tituloPagina = 'CADASTRAR PROCESSO MANEJO DIGITAL';

	var cadastroProcessoManejoController = this;

	cadastroProcessoManejoController.processo = null;
	cadastroProcessoManejoController.tipologia = [];
	cadastroProcessoManejoController.atividade = [];
	cadastroProcessoManejoController.municipio = [];
	cadastroProcessoManejoController.licenca = [];
	cadastroProcessoManejoController.car = {};
	cadastroProcessoManejoController.car.numero = null;
	cadastroProcessoManejoController.car.proprietario = {};
	cadastroProcessoManejoController.car.proprietario.cpfCnpj = null;
	cadastroProcessoManejoController.car.proprietario.nome = null;
	cadastroProcessoManejoController.car.proprietario.endereco = null;
	cadastroProcessoManejoController.car.proprietario.municipio = null;

	cadastroProcessoManejoController.buscarImovel = function() {

		imovelService.getImovelByCodigo(cadastroProcessoManejoController.car.numero)
			.then(function(response) {

				console.log(response);

			}, function(error){

				mensagem.error(error.data.texto);
			});
	};
};

exports.controllers.CadastroProcessoManejoController = CadastroProcessoManejoController;