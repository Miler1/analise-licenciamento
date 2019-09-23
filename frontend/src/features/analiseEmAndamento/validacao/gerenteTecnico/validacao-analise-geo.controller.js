var ValidacaoAnaliseGeoGerenteController = function($rootScope, analiseGeoService ,analiseTecnicaService, $route, $scope, 
        mensagem, $location, documentoAnaliseService, processoService, $uibModal, analistaService, documentoService, empreendimentoService) {

    var validacaoAnaliseGeoGerente = this;

    validacaoAnaliseGeoGerente.analiseGeoValidacao = {};
    validacaoAnaliseGeoGerente.camadasDadosEmpreendimento = {};
    validacaoAnaliseGeoGerente.dadosProjeto = {};

    validacaoAnaliseGeoGerente.init = init;
    validacaoAnaliseGeoGerente.exibirDadosProcesso = exibirDadosProcesso;
    validacaoAnaliseGeoGerente.concluir = concluir;
    validacaoAnaliseGeoGerente.baixarDocumento = baixarDocumento;
    validacaoAnaliseGeoGerente.verificarTamanhoInconsistencias = verificarTamanhoInconsistencias;
    validacaoAnaliseGeoGerente.openModalOficio = openModalOficio;

    validacaoAnaliseGeoGerente.TiposResultadoAnalise = app.utils.TiposResultadoAnalise;

    function init() {

        analiseGeoService.getAnliseGeoByAnalise($route.current.params.idAnalise)
            .then(function(response){
                validacaoAnaliseGeoGerente.analiseGeo = response.data;

                if(validacaoAnaliseGeoGerente.analiseGeo.analistasTecnicos) {
                    validacaoAnaliseGeoGerente.analiseGeoValidacao.idAnalistaTecnico =
                        validacaoAnaliseGeoGerente.analiseGeo.analistasTecnicos[0].usuario.id;
                }

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
            
                getDadosVisualizar(validacaoAnaliseGeoGerente.analiseGeo.analise.processo);

            });
        
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

    function baixarDocumento(documento) {
        if(!documento.id){
			documentoService.download(documento.key, documento.nomeDoArquivo);
		}else{
			analiseGeoService.download(documento.id);
		}
    }

    function getDadosVisualizar(processo) {
        var pessoa = processo.empreendimento.pessoa;
        var cpfCnpjEmpreendimento = pessoa.cpf ? pessoa.cpf : pessoa.cnpj;

        var idProcesso = processo.id;

        empreendimentoService.getDadosGeoEmpreendimento(cpfCnpjEmpreendimento)
			.then(function(response) {

                validacaoAnaliseGeoGerente.camadasDadosEmpreendimento = response.data;
            });
        
        analiseGeoService.getDadosProjeto(idProcesso)
            .then(function (response) {

                validacaoAnaliseGeoGerente.dadosProjeto = response.data;
            });
    }

    function verificarTamanhoInconsistencias () {
        if(validacaoAnaliseGeoGerente.analiseGeo) {
            var inconsistencias = angular.copy(validacaoAnaliseGeoGerente.analiseGeo.inconsistencias);
            return _.remove(inconsistencias, function(i){
                return(i.categoria !== 'ATIVIDADE');
            }).length > 0;
        } 
        return false;
    }
    
    function openModalOficio(restricao) {
        var modalInstance = $uibModal.open({

            component: 'modalOficioRestricao',
            size: 'lg',
            resolve: {

                restricao: function() {

                    return restricao;
                }
            }    
        });
    }

};

exports.controllers.ValidacaoAnaliseGeoGerenteController = ValidacaoAnaliseGeoGerenteController;
