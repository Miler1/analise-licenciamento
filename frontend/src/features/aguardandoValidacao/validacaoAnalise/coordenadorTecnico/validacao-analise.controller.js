var ValidacaoAnaliseTecnicaController = function($rootScope, analiseTecnicaService, $route, $scope, 
		mensagem, $location, documentoAnaliseService, processoService, $uibModal, analistaService, coordenadorService) {

    var validacaoAnaliseTecnica = this;

    validacaoAnaliseTecnica.analiseTecnicaValidacao = {};
    validacaoAnaliseTecnica.analistaCoordenador = {
        COORDENADOR: 'coordenadoresTecnicos',
        ANALISTA: 'analistasTecnicos'
    };

    validacaoAnaliseTecnica.init = init;
    validacaoAnaliseTecnica.exibirDadosProcesso = exibirDadosProcesso; 
    validacaoAnaliseTecnica.exibirAnaliseJuridica = exibirAnaliseJuridica;
    validacaoAnaliseTecnica.downloadDocumentoAnalise = downloadDocumentoAnalise;
	validacaoAnaliseTecnica.isParecerNaoValidado = isParecerNaoValidado;
	validacaoAnaliseTecnica.isObrigatorio = isObrigatorio;
	validacaoAnaliseTecnica.cancelar = cancelar;
	validacaoAnaliseTecnica.concluir = concluir;
    validacaoAnaliseTecnica.getAnalistasTecnicos = getAnalistasTecnicos;
    validacaoAnaliseTecnica.getCoordenadorByProcesso = getCoordenadorByProcesso;
    validacaoAnaliseTecnica.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

    function init() {

		analiseTecnicaService.getAnaliseTecnica($route.current.params.idAnalise)
			.then(function(response){
				validacaoAnaliseTecnica.analiseTecnica = response.data;               
			});

		analistaService.getAnalistasTecnicos()
			.then(function(response){
				validacaoAnaliseTecnica.analistas = response.data;
			});            
		
		$rootScope.$broadcast('atualizarContagemProcessos');        
    }

    function getAnalistasTecnicos() {

        analistaService.getAnalistasTecnicosByProcesso(validacaoAnaliseTecnica.analiseTecnica.analise.processo.id)
            .then(function(response){
                validacaoAnaliseTecnica.analistaCoordenador = response.data;
            });
    }

    function getCoordenadorByProcesso() {

        coordenadorService.getCoordenadorByIdProcesso(validacaoAnaliseTecnica.analiseTecnica.analise.processo.id)
            .then(function(response){
                validacaoAnaliseTecnica.analistaCoordenador = response.data;
            });
    }

	function exibirDadosProcesso() {

        var processo = {

            idProcesso: validacaoAnaliseTecnica.analiseTecnica.analise.processo.id,
            numero: validacaoAnaliseTecnica.analiseTecnica.analise.processo.numero,
            denominacaoEmpreendimento: validacaoAnaliseTecnica.analiseTecnica.analise.processo.empreendimento.denominacao
        };

        if(validacaoAnaliseTecnica.analiseTecnica.analise.processo.empreendimento.pessoa.cnpj) {

            processo.cnpjEmpreendimento = validacaoAnaliseTecnica.analiseTecnica.analise.processo.empreendimento.pessoa.cnpj;

        } else {

            processo.cpfEmpreendimento = validacaoAnaliseTecnica.analiseTecnica.analise.processo.empreendimento.pessoa.cpf;
        }		

        processoService.visualizarProcesso(processo);
    }    

    function exibirAnaliseJuridica() {

        processoService.getAnaliseJuridica(validacaoAnaliseTecnica.analiseTecnica.analise.processo.id)
            .then(function(response){

                $uibModal.open({

                    component: 'modalInformacoesAnaliseJuridica',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {

                        idAnalise: function() {

                            return response.data.id;
                        }
                    }    
                });    
            });
    }

    function downloadDocumentoAnalise(idDocumento) {

        documentoAnaliseService.download(idDocumento);
    }

	function isParecerNaoValidado() {

		return validacaoAnaliseTecnica.analiseTecnicaValidacao.idTipoResultadoValidacao === 
                                validacaoAnaliseTecnica.TiposResultadoAnalise.PARECER_NAO_VALIDADO;
	}

	function isObrigatorio() {
		
        return [validacaoAnaliseTecnica.TiposResultadoAnalise.PARECER_NAO_VALIDADO,
				validacaoAnaliseTecnica.TiposResultadoAnalise.SOLICITAR_AJUSTES]
				.indexOf(validacaoAnaliseTecnica.analiseTecnicaValidacao.idTipoResultadoValidacao) !== -1;
	}

    function cancelar() {

        $location.path('aguardando-validacao');
    }

	function montarAnaliseTecnica(analiseTecnicaValidacao){

		var analiseTecnica = 
            {
                id: validacaoAnaliseTecnica.analiseTecnica.id,
                tipoResultadoValidacao: { id : analiseTecnicaValidacao.idTipoResultadoValidacao},
                parecerValidacao: analiseTecnicaValidacao.parecerValidacao
            };

        analiseTecnica[analiseTecnicaValidacao.analistaCoordenador] = [
            {
                usuario: {
                    id: analiseTecnicaValidacao.idAnalistaTecnico
                }
            }
        ];
        
        return analiseTecnica;
	}    

	function concluir() {

		$scope.formularioValidacao.$setSubmitted();

		if (!$scope.formularioValidacao.$valid){

            mensagem.error('Preencha os campos destacados em vermelho para prosseguir com a validação.');
			return;
        }

        var analiseTecnica = montarAnaliseTecnica(validacaoAnaliseTecnica.analiseTecnicaValidacao);

		analiseTecnicaService.validarParecer(analiseTecnica)
            .then(function(response) {

                mensagem.success(response.data.texto);
				$location.path('aguardando-validacao');

            }, function(error){

                mensagem.error(error.data.texto);
            });
    }    
};

exports.controllers.ValidacaoAnaliseTecnicaController = ValidacaoAnaliseTecnicaController;