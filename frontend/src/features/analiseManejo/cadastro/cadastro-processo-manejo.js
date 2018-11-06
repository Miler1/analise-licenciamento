var CadastroProcessoManejoController = function($scope, config, $rootScope, processoManejoService, mensagem, $location) {

	$rootScope.tituloPagina = 'CADASTRAR PROCESSO MANEJO DIGITAL';

	var cadastroProcessoManejoController = this;

	cadastroProcessoManejoController.processo = null;
	cadastroProcessoManejoController.tipologia = null;
	cadastroProcessoManejoController.atividade = null;
	cadastroProcessoManejoController.municipio = null;
	cadastroProcessoManejoController.licenca = null;
	cadastroProcessoManejoController.car = null;
	cadastroProcessoManejoController.car.numero = null;
	cadastroProcessoManejoController.car.proprietario.cpfCnpj = null;
	cadastroProcessoManejoController.car.proprietario.nome = null;
	cadastroProcessoManejoController.car.proprietario.endereco = null;
	cadastroProcessoManejoController.car.proprietario.municipio = null;
};

exports.controllers.CadastroProcessoManejoController = CadastroProcessoManejoController;