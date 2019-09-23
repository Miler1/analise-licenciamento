var ValidacaoAnaliseGeoGerenteController = function($rootScope, analiseGeoService ,analiseTecnicaService, $route, $scope, 
        mensagem, $location, documentoAnaliseService, processoService, $uibModal, analistaService) {

    var validacaoAnaliseGeoGerente = this;

    validacaoAnaliseGeoGerente.analiseTecnicaValidacao = {};

    validacaoAnaliseGeoGerente.init = init;
    validacaoAnaliseGeoGerente.exibirDadosProcesso = exibirDadosProcesso;
    validacaoAnaliseGeoGerente.concluir = concluir;

    validacaoAnaliseGeoGerente.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

    function init() {

        analiseGeoService.getAnliseGeoByAnalise($route.current.params.idAnalise)
            .then(function(response){
                validacaoAnaliseGeoGerente.analiseGeo = response.data;

                validacaoAnaliseGeoGerente.analiseGeoValidacao.idAnalistaTecnico =
                    validacaoAnaliseGeoGerente.analiseGeo.analistasTecnicos[0].usuario.id;
                
                if (validacaoAnaliseGeoGerente.analiseGeo.tipoResultadoValidacaoGerente) {

                    validacaoAnaliseGeoGerente.analiseGeoValidacao.idTipoResultadoValidacaoGerente =
                        validacaoAnaliseGeoGerente.analiseGeo.tipoResultadoValidacaoGerente.id;
                }
                
                validacaoAnaliseGeoGerente.analiseGeoValidacao.parecerValidacaoGerente =
                    validacaoAnaliseGeoGerente.analiseGeo.parecerValidacaoGerente;

                analistaService.getAnalistasTecnicosByProcesso(validacaoAnaliseGeoGerente.analiseGeo.analise.processo.id)
                    .then(function(response){
                        validacaoAnaliseGeoGerente.analistas = response.data;
                });            
            

            });
        
        $('#situacao-fundiaria').summernote('disable');
        
        $rootScope.$broadcast('atualizarContagemProcessos');
    }

    function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseGeoGerente.analiseGeo.analise.processo.id,
            numero: validacaoAnaliseGeoGerente.analiseGeo.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseGeoGerente.analiseGeo.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseGeoGerente.analiseGeo.analise.processo.empreendimento.pessoa.cnpj) {

            processo.cnpjEmpreendimento = validacaoAnaliseGeoGerente.analiseGeo.analise.processo.empreendimento.pessoa.cnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseGeoGerente.analiseGeo.analise.processo.empreendimento.pessoa.cpf;
        }		

        processoService.visualizarProcesso(processo);
    }

    validacaoAnaliseGeoGerente.downloadPDFparecer = function (analiseGeo) {

		var params = {
			id: analiseGeo.id
		};

		documentoAnaliseService.generatePDFParecerGeo(params)
			.then(function(data, status, headers){

				var a = document.createElement('a');
				a.href = URL.createObjectURL(data.data.response.blob);
				a.download = data.data.response.fileName ? data.data.response.fileName : 'parecer_analise_geo.pdf';
				a.click();

			},function(error){
				mensagem.error(error.data.texto);
			});
	};

	validacaoAnaliseGeoGerente.downloadPDFCartaImagem = function (analiseGeo) {

		var params = {
			id: analiseGeo.id
		};

		documentoAnaliseService.generatePDFCartaImagemGeo(params)
			.then(function(data, status, headers){

				var a = document.createElement('a');
				a.href = URL.createObjectURL(data.data.response.blob);
				a.download = data.data.response.fileName ? data.data.response.fileName : 'carta_imagem.pdf';
				a.click();

			},function(error){
				mensagem.error(error.data.texto);
			});
	};

    function concluir() {

        $scope.formularioValidacao.$setSubmitted();

        if (!$scope.formularioValidacao.$valid){

            mensagem.error('Preencha os campos destacados em vermelho para prosseguir com a validação.');
            return;
        }

        var analiseTecnica = montarAnaliseTecnica(validacaoAnaliseGeoGerente.analiseTecnicaValidacao);

        analiseTecnicaService.validarParecerGerente(analiseTecnica)
            .then(function(response) {

                mensagem.success(response.data.texto);
                $location.path('aguardando-validacao');

            }, function(error){

                mensagem.error(error.data.texto);
            });
    }
};

exports.controllers.ValidacaoAnaliseGeoGerenteController = ValidacaoAnaliseGeoGerenteController;
