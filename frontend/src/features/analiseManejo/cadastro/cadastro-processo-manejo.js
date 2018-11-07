var CadastroProcessoManejoController = function($scope, config, $rootScope, processoManejoService, mensagem, $location, imovelService) {

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
};

exports.controllers.CadastroProcessoManejoController = CadastroProcessoManejoController;