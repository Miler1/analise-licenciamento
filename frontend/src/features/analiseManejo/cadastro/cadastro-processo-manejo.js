var CadastroProcessoManejoController = function($scope, config, $rootScope, processoManejoService, mensagem, $location, imovelService) {

	$rootScope.tituloPagina = 'CADASTRAR PROCESSO MANEJO DIGITAL';

	var cadastroProcessoManejoController = this;

	cadastroProcessoManejoController.processo = criarProcesso();
	cadastroProcessoManejoController.tipologia = [];
	cadastroProcessoManejoController.atividade = [];
	cadastroProcessoManejoController.municipio = [];
	cadastroProcessoManejoController.licenca = [];

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

	cadastroProcessoManejoController.excluirProcesso = function() {

		cadastroProcessoManejoController.processo = criarProcesso();
	};
};

exports.controllers.CadastroProcessoManejoController = CadastroProcessoManejoController;