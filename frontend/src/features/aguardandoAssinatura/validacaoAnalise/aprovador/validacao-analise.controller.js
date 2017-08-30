var ValidacaoAnaliseAprovadorController = function ($rootScope, $route, $routeParams, $scope,
    mensagem, $location, processoService, $uibModal, analiseService, analiseJuridicaService, 
    analiseTecnicaService, documentoAnaliseService, documentoLicenciamentoService) {

    var validacaoAnaliseAprovador = this;

    validacaoAnaliseAprovador.formularios = {};
    validacaoAnaliseAprovador.tabAtiva = 0;
    validacaoAnaliseAprovador.init = init;
    validacaoAnaliseAprovador.exibirDadosProcesso = exibirDadosProcesso;
    validacaoAnaliseAprovador.carregarDadosAnaliseJuridica = carregarDadosAnaliseJuridica;
    validacaoAnaliseAprovador.carregarDadosAnaliseGeo = carregarDadosAnaliseGeo;
    validacaoAnaliseAprovador.carregarDadosAnaliseTecnica = carregarDadosAnaliseTecnica;
    validacaoAnaliseAprovador.downloadDocumentoAnalise = downloadDocumentoAnalise;
    validacaoAnaliseAprovador.downloadDocumentoValidado = downloadDocumentoValidado;
    validacaoAnaliseAprovador.visualizarLicenca = visualizarLicenca;

    function init() {

        analiseService.getAnalise($routeParams.idAnalise)
            .then(function (response) {

                validacaoAnaliseAprovador.analise = response.data;
                validacaoAnaliseAprovador.analise.processo.empreendimento.municipio =
                    validacaoAnaliseAprovador.analise.processo.empreendimento.endereco.municipio;
                carregarDadosAnaliseJuridica();
                carregarDadosAnaliseGeo();
                carregarDadosAnaliseTecnica();
                
            });
    }

    function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseAprovador.analise.processo.id,
            numero: validacaoAnaliseAprovador.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseAprovador.analise.processo.empreendimento.denominacao
        };

        if (validacaoAnaliseAprovador.analise.processo.empreendimento.pessoa.cnpj) {

            processo.cnpjEmpreendimento = validacaoAnaliseAprovador.analise.processo.empreendimento.pessoa.cnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseAprovador.analise.processo.empreendimento.pessoa.cpf;
        }

        processoService.visualizarProcesso(processo);
    }

    function carregarDadosAnaliseJuridica() {

        if (validacaoAnaliseAprovador.analise) {
            analiseJuridicaService.getAnaliseJuridica(validacaoAnaliseAprovador.analise.analiseJuridica.id)
                .then(function (response) {
                    validacaoAnaliseAprovador.analiseJuridica = response.data;
                });
        }
    }

    function carregarDadosAnaliseGeo() {

        if (validacaoAnaliseAprovador.analise) {

            analiseTecnicaService.getRestricoesGeo(validacaoAnaliseAprovador.analise.analiseTecnica.id)
                .then(function (response) {
                    validacaoAnaliseAprovador.restricoes = response.data;
                });
        }
    }

    function carregarDadosAnaliseTecnica() {

        analiseTecnicaService.getAnaliseTecnica(validacaoAnaliseAprovador.analise.analiseTecnica.id)
            .then(function (response) {
                validacaoAnaliseAprovador.analiseTecnica = response.data;
            });
    }

    function downloadDocumentoAnalise(idDocumento) {

        documentoAnaliseService.download(idDocumento);
    }

    function downloadDocumentoValidado(idDocumento) {
        documentoLicenciamentoService.download(idDocumento);
    }

    function visualizarLicenca(indice) {

        var analiseLicenca = validacaoAnaliseAprovador.analiseTecnica.licencasAnalise[indice];

        var modalInstance = $uibModal.open({

            component: 'modalVisualizarLicenca',
            size: 'lg',
            resolve: {

                dadosLicenca: function () {

                    return angular.copy(analiseLicenca);
                }
            }
        });

    }

};

exports.controllers.ValidacaoAnaliseAprovadorController = ValidacaoAnaliseAprovadorController;
