var AnaliseTecnicaController = function ($rootScope, uploadService, $route, $scope, $location, 
                                        analistaService, analiseTecnica, mensagem, $uibModal, 
                                        analiseTecnicaService, tamanhoMaximoArquivoAnaliseMB,
                                        documentoAnaliseService, restricoes, TiposAnalise,inconsistenciaService, 
                                        documentoLicenciamentoService, processoService, documentoService,
                                        parecerAnalistaTecnicoService) {

    $rootScope.tituloPagina = 'PARECER TÉCNICO';

    var ctrl = this;

    ctrl.tiposResultadoAnalise = app.utils.TiposResultadoAnalise;
    ctrl.TAMANHO_MAXIMO_ARQUIVO_MB = tamanhoMaximoArquivoAnaliseMB;
    ctrl.processo = angular.copy(analiseTecnica.analise.processo);
    ctrl.imovel = angular.copy(analiseTecnica.analise.processo.empreendimento.imovel);
    ctrl.restricoes = restricoes;
    ctrl.idAnaliseTecnica = $route.current.params.idAnaliseTecnica;
    ctrl.formularios = {};
    ctrl.tabAtiva = 0;
    ctrl.tiposAnalise = TiposAnalise;
    ctrl.tipoDeInconsistenciaTecnica = app.utils.InconsistenciaTecnica;
    ctrl.dateUtil = app.utils.DateUtil;
    ctrl.analistaTecnico = $rootScope.usuarioSessao.usuarioEntradaUnica.nome;
    ctrl.analiseTecnica = null;
    ctrl.pergunta = null;
    ctrl.anexos = [];
    ctrl.tiposDocumentosAnalise = app.utils.TiposDocumentosAnalise;
    ctrl.semInconsistenciaVistoria = null;
    ctrl.analistasTecnico = [];
    ctrl.tiposUpload = app.utils.TiposUpload;
    ctrl.dataAtual = new Date();
    $scope.analistaSelecionado = null;
    ctrl.tipoLicenca =  {};

    ctrl.parecer = {
        doProcesso: null,
        daAnaliseTecnica: null,
        daConclusao: null,
        documentos: [],
        parecer: null,
        analiseTecnica: null,
        tipoResultadoAnalise: null,
        vistoria: {
            realizada: null,
            documentoRit: null,
            inconsistenciaVistoria: null,
            anexos: [],
            equipe: [],
            conclusao: null,
            data: null,
            hora: null,
            descricao: null,
            cursosDagua: null,
            tipologiaVegetal: null,
            app: null,
            ocorrencia: null,
            residuosLiquidos: null,
            outrasInformacoes: null
        }
    };
   
    ctrl.itemValidoLicenca = {
        tipoLicenca: null,
        atividade: [],
        questionario: null,
        documentoAdministrativo: [],
        documentoTecnicoAmbiental: []
    };

    ctrl.errors = { 

        isPdf: false,
        autoInfracao: false,
        pergunta: false,
        parecer: false,
        doProcesso: false,
        daAnaliseTecnica: false,
        daConclusao: false,

        vistoria: {
            realizada: false,
            conclusao: false,
            data: false,
            hora: false,
            descricao: false,
            documentoRit: false,
            inconsistenciaVistoria: false
        },

        deferido: {
            finalidade: false,
            despacho: false
        }

    };
    
    ctrl.init = function () {

        $rootScope.$broadcast('atualizarContagemProcessos');

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id)
            .then(function(response){

                ctrl.analiseTecnica = response.data;
                ctrl.tipoLicenca = ctrl.analiseTecnica.analise.processo.caracterizacao.tipoLicenca;
                ctrl.porteEmpreendimento = ctrl.analiseTecnica.analise.processo.caracterizacao.atividadesCaracterizacao[0].porteEmpreendimento;

                ctrl.parecer.analiseTecnica = {
                    id: ctrl.analiseTecnica.id
                };

                ctrl.parecer.tipoResultadoAnalise = {
                    id: null
                };

                analistaService.getAnalistasTecnicoBySetor()
                .then(function(response) {
        
                    response.data.forEach(function(analista){
                        ctrl.analistasTecnico.push({ usuario: analista });
                    });
        
                });

                ctrl.validarItensLicenca(app.utils.InconsistenciaTecnica.TIPO_LICENCA, ctrl.analiseTecnica);

                _.forEach(ctrl.analiseTecnica.analise.processo.caracterizacao.atividadesCaracterizacao, function(atividade, index){

                    ctrl.itemValidoLicenca.atividade[index] = {
                        atividadeValida: null,
                        parametros: []
                    };

                    ctrl.validarInconsistenciaAtividade(app.utils.InconsistenciaTecnica.ATIVIDADE, index, atividade);
                });
               
                ctrl.validarItensLicenca(app.utils.InconsistenciaTecnica.QUESTIONARIO, ctrl.analiseTecnica);

                _.forEach(ctrl.analiseTecnica.analise.processo.caracterizacao.atividadesCaracterizacao, function(atividade, index){
                    _.forEach(atividade.atividade.parametros, function(parametro, indexParametro) {

                        ctrl.validarInconsistenciaParametro(app.utils.InconsistenciaTecnica.PARAMETRO, parametro, index, indexParametro, atividade);

                    });
                });

                _.forEach(ctrl.analiseTecnica.analise.processo.caracterizacao.documentosEnviados, function(documentoAdministrativo, index){
                    ctrl.validarInconsistenciaDocumentoAdministrativo(app.utils.InconsistenciaTecnica.DOCUMENTO_ADMINISTRATIVO, documentoAdministrativo, index);
                });

                _.forEach(ctrl.analiseTecnica.analise.processo.caracterizacao.documentosSolicitacaoGrupo, function(documentoTecnicoAmbiental, index){
                    ctrl.validarInconsistenciaDocumentoTecnicoAmbiental(app.utils.InconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL, documentoTecnicoAmbiental, index);
                });
            }); 
    };

    ctrl.validarAbas = function(abaDestino) {

        if(abaDestino === 1 && !ctrl.validarCampos()) {

            ctrl.tabAtiva = 0;

        } else if(abaDestino === 2) {

            if(!ctrl.validarCampos()) {

                ctrl.tabAtiva = 0;

            } else if(!vistoriaValida()) {

                ctrl.tabAtiva = 1;

            }            

        }

    };

    ctrl.verificarData = function() {

        if(ctrl.parecer.vistoria.data instanceof Date && 
            ctrl.dateUtil.isAfter(ctrl.parecer.vistoria.data, new Date())) {

            ctrl.parecer.vistoria.data = new Date();

        }

    };

    ctrl.addRestricao = function() {

        $uibModal.open({
            animation: true,
            templateUrl: './features/analiseTecnica/restricao/modal-restricao.html',
            controller: 'modalRestricaoController',
            controllerAs: 'modalCtrl',
            size: 'lg'
        });

    };

    ctrl.excluirRestricao = function(restricaoAdd) {

        _.remove(ctrl.parecer.restricoes, function(restricao) {
           return restricao === restricaoAdd; 
        });

    };

    ctrl.addCondicionante = function() {

        $uibModal.open({
            animation: true,
            templateUrl: './features/analiseTecnica/condicionante/modal-condicionante.html',
            controller: 'modalCondicionanteController',
            controllerAs: 'modalCtrl',
            size: 'lg'
        });

    };

    ctrl.excluirCondicionante = function(condicionanteAdd) {

        _.remove(ctrl.parecer.condicionantes, function(condicionante) {
           return condicionante === condicionanteAdd; 
        });

    };

    ctrl.hasInconsistencias = function() {

        return ctrl.analiseTecnica && ctrl.analiseTecnica.inconsistenciasTecnica.length > 0 &&
                ctrl.parecer.vistoria && ctrl.parecer.vistoria.inconsistenciaVistoria !== null;

    };
    
    ctrl.exibirDadosProcesso = function () {

        var processo = {

            idProcesso: ctrl.processo.id,
            numero: ctrl.processo.numero,
            denominacaoEmpreendimento: ctrl.processo.empreendimento.denominacao
        };

        if (ctrl.processo.empreendimento.pessoa.cnpj) {

            processo.cnpjEmpreendimento = ctrl.processo.empreendimento.pessoa.cnpj;

        } else {

            processo.cpfEmpreendimento = ctrl.processo.empreendimento.pessoa.cpf;
        }

        processoService.visualizarProcesso(processo);

    };

    ctrl.validarInconsistenciaAtividade = function(tipoDeInconsistenciaTecnica, index, atividade) {

        if (tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.ATIVIDADE){

            inconsistenciaTecnica = _.find( ctrl.analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
                return inconsistenciaTecnica.inconsistenciaTecnicaAtividade !== null &&
                    inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id === atividade.id;
            });

            if( inconsistenciaTecnica !== undefined && inconsistenciaTecnica.inconsistenciaTecnicaAtividade !== null){           
                ctrl.itemValidoLicenca.atividade[index].atividadeValida = true;
                return false;
            }else{
                ctrl.itemValidoLicenca.atividade[index].atividadeValida = false;
                return true;
            }
        }       
    };

    ctrl.validarInconsistenciaParametro = function(tipoDeInconsistenciaTecnica, parametro, indexAtividade, indexParametro, atividade) {

        if (tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.PARAMETRO){

            inconsistenciaTecnica = _.find( ctrl.analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
                return inconsistenciaTecnica.inconsistenciaTecnicaParametro !== null &&
                    inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade.id === parametro.id &&
                    inconsistenciaTecnica.inconsistenciaTecnicaParametro.atividadeCaracterizacao.id === atividade.id;
            });

            if( inconsistenciaTecnica !== undefined && inconsistenciaTecnica.inconsistenciaTecnicaParametro !== null ){
               
                ctrl.itemValidoLicenca.atividade[indexAtividade].parametros[indexParametro] = true;
                return false;
            }else{
                ctrl.itemValidoLicenca.atividade[indexAtividade].parametros[indexParametro] = false;
                return true;
            }
        }       
    };

    ctrl.validarInconsistenciaDocumentoAdministrativo = function(tipoDeInconsistenciaTecnica, documento, index) {

        if (tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.DOCUMENTO_ADMINISTRATIVO){

            inconsistenciaTecnica = _.filter( ctrl.analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
                if(inconsistenciaTecnica !== null){
                    return inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo;
                }
            });
        }
        
        if( inconsistenciaTecnica !== undefined && inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo !== null){

            if(_.isEmpty(inconsistenciaTecnica)) {

                ctrl.itemValidoLicenca.documentoAdministrativo[index] = false;
                return true;

            }

            _.forEach(inconsistenciaTecnica, function(inconsistencia){
                
                if(documento.id === inconsistencia.inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo.id ){           
                    ctrl.itemValidoLicenca.documentoAdministrativo[index] = true;
                    return false;

                }else{
                    ctrl.itemValidoLicenca.documentoAdministrativo[index] = false;
                    return true;

                }
            });

        }       
    };

    ctrl.validarInconsistenciaDocumentoTecnicoAmbiental = function(tipoDeInconsistenciaTecnica, documento, index) {

        if (tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL){

            inconsistenciaTecnica = _.filter( ctrl.analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
                if(inconsistenciaTecnica !== null){
                    return inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental;
                }
            });
        }

        if( inconsistenciaTecnica !== undefined && inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental !== null){
            
            if(_.isEmpty(inconsistenciaTecnica)) {

                ctrl.itemValidoLicenca.documentoTecnicoAmbiental[index] = false;
                return true;

            }

            _.forEach(inconsistenciaTecnica, function(inconsistencia){

                if(documento.documento.id === inconsistencia.inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.documento.id ){           
                    ctrl.itemValidoLicenca.documentoTecnicoAmbiental[index] = true;
                    return false;
                }else{
                    ctrl.itemValidoLicenca.documentoTecnicoAmbiental[index] = false;
                    return true;
                }
            });    
        }       
    };
    
    ctrl.deletarInconsistenciaVistoria = function() {

        if(ctrl.parecer.vistoria && ctrl.parecer.vistoria.inconsistenciaVistoria) {
            
            ctrl.parecer.vistoria.inconsistenciaVistoria = null;

        }

    };

    ctrl.marcarSemInconsistencia = function() {

        ctrl.deletarInconsistenciaVistoria();
        ctrl.semInconsistenciaVistoria = true;

    };

    ctrl.limparErrosVistoria = function() {

        ctrl.errors.vistoria = {
            realizada: false,
            conclusao: false,
            data: false,
            hora: false,
            descricao: false,
            documentoRit: false,
            inconsistenciaVistoria: false
        };

    };

    ctrl.limparVistoriaRealizada = function() {

        ctrl.limparErrosVistoria();

        if(ctrl.parecer.vistoria.realizada === 'true') {

            ctrl.parecer.vistoria = {
                realizada: 'false',
                documentoRit: null,
                inconsistenciaVistoria: null,
                anexos: [],
                equipe: [],
                conclusao: null,
                data: null,
                hora: null,
                descricao: null,
                cursosDagua: null,
                tipologiaVegetal: null,
                app: null,
                ocorrencia: null,
                residuosLiquidos: null,
                outrasInformacoes: null
            };
            ctrl.semInconsistenciaVistoria = null;

        }
        
    };

    ctrl.limparVistoriaNaoRealizada = function() {

        ctrl.limparErrosVistoria();

        if(ctrl.parecer.vistoria.realizada === 'false') {

            ctrl.parecer.vistoria.conclusao = null;

        }
        
    };

    $rootScope.$on('adicionarInconsistenciaVistoria', function(event, inconsistenciaVistoria) {

        ctrl.parecer.vistoria.inconsistenciaVistoria = inconsistenciaVistoria;
        ctrl.semInconsistenciaVistoria = null;
        
    });

    $rootScope.$on('adicionarCondicionante', function(event, condicionante) {

        if(ctrl.parecer.condicionantes === null || ctrl.parecer.condicionantes === undefined) {

            ctrl.parecer.condicionantes = [];

        }

        ctrl.parecer.condicionantes.push(condicionante);

    });

    $rootScope.$on('adicionarRestricao', function(event, restricao) {

        if(ctrl.parecer.restricoes === null || ctrl.parecer.restricoes === undefined) {

            ctrl.parecer.restricoes = [];

        }

        ctrl.parecer.restricoes.push(restricao);

    });

    ctrl.adicionarAnalistaEquipe = function(analistaSelecionado) {

        var isAdded = _.some(ctrl.parecer.vistoria.equipe, function(analista) {
            return analistaSelecionado && analista.usuario.id === analistaSelecionado.id;
        });

        if(!isAdded && analistaSelecionado) {

            ctrl.parecer.vistoria.equipe.push(analistaSelecionado);

            _.remove(ctrl.analistasTecnico, function(analista) {
                return analista.usuario.id === analistaSelecionado.usuario.id;
            });

        }

    };

    ctrl.removerAnalistaEquipe = function(analistaSelecionado) {

        if(analistaSelecionado === undefined) {

            ctrl.analistasTecnico = ctrl.parecer.vistoria.equipe;
            ctrl.parecer.vistoria.equipe = [];

        } else {

            ctrl.analistasTecnico.push(analistaSelecionado);

            _.remove(ctrl.parecer.vistoria.equipe, function(analista) {
                return analista.usuario.id === analistaSelecionado.usuario.id;
            });

        }

    };

    ctrl.checkTipoResultadoAnalise = function(tipoResultadoAnalise) {

        return ctrl.parecer.tipoResultadoAnalise && parseInt(ctrl.parecer.tipoResultadoAnalise.id) === tipoResultadoAnalise;

    };

    var camposConclusaoValidos = function() {

        var valido = true;

        if(ctrl.parecer.doProcesso === null || ctrl.parecer.doProcesso === '' || ctrl.parecer.doProcesso === undefined) {

            ctrl.errors.doProcesso = true;
            valido = false;

        }

        if(ctrl.parecer.daAnaliseTecnica === null || ctrl.parecer.daAnaliseTecnica === '' || ctrl.parecer.daAnaliseTecnica === undefined) {

            ctrl.errors.daAnaliseTecnica = true;
            valido = false;

        }

        if(ctrl.parecer.daConclusao === null || ctrl.parecer.daConclusao === '' || ctrl.parecer.daConclusao === undefined) {

            ctrl.errors.daConclusao = true;
            valido = false;

        }

        return valido;

    }; 

    var parecerDeferidoValido = function() {

        if(ctrl.parecer.finalidadeAtividade === null || ctrl.parecer.finalidadeAtividade === '' || ctrl.parecer.finalidadeAtividade === undefined) {

            ctrl.errors.deferido.finalidade = true;

        }

        if(ctrl.parecer.parecer === null || ctrl.parecer.parecer === '' || ctrl.parecer.parecer === undefined) {

            ctrl.errors.deferido.despacho = true;

        }

        return !Object.keys(ctrl.errors.deferido).forEach(function(campo) {
            return ctrl.errors.deferido[campo];
        });

    };

    var parecerIndeferidoValido = function() {

        if(ctrl.parecer.parecer === null || ctrl.parecer.parecer === '' || ctrl.parecer.parecer === undefined) {

            ctrl.errors.parecer = true;

        }

        return !ctrl.errors.parecer;

    };

    var vistoriaValida = function() {

        if(ctrl.parecer.vistoria.realizada === null || ctrl.parecer.vistoria.realizada === undefined) {

            ctrl.errors.vistoria.realizada = true;

        }

        if(ctrl.parecer.vistoria.realizada !== null && ctrl.parecer.vistoria.realizada === 'true') {

            if(ctrl.parecer.vistoria.conclusao === null || ctrl.parecer.vistoria.conclusao === '') {

                ctrl.errors.vistoria.conclusao = true;

            }

            if(ctrl.parecer.vistoria.documentoRit === null) {

                ctrl.errors.vistoria.documentoRit = true;

            }

            if(ctrl.semInconsistenciaVistoria === null && ctrl.parecer.vistoria.inconsistenciaVistoria === null) {

                ctrl.errors.vistoria.inconsistenciaVistoria = true;

            }

            if(ctrl.parecer.vistoria.data === null) {
    
                ctrl.errors.vistoria.data = true;
    
            }
            
            if(ctrl.parecer.vistoria.hora === null) {
    
                ctrl.errors.vistoria.hora = true;
    
            }

            if(ctrl.parecer.vistoria.descricao === null) {
    
                ctrl.errors.vistoria.descricao = true;
    
            }

        } else if(ctrl.parecer.vistoria.realizada !== null) {

            if(ctrl.parecer.vistoria.conclusao === null || ctrl.parecer.vistoria.conclusao === '') {

                ctrl.errors.vistoria.conclusao = true;
    
            }

        }        

        return !Object.keys(ctrl.errors.vistoria).some(function(key) {
            return ctrl.errors.vistoria[key];
        });

    };

    ctrl.openModalInconsistencia = function() {

        ctrl.limparErrosVistoria();

        var inconsistenciaVistoria = {
            descricaoInconsistencia: null,
            tipoInconsistencia: null,
            anexos: []
        };

        $uibModal.open({
			controller: 'modalInconsistenciaVistoriaController',
			controllerAs: 'modalCtrl',
			backdrop: 'static',
			templateUrl: 'features/analiseTecnica/inconsistenciaVistoria/modal-inconsistencia-vistoria.html',
			size: 'lg',
			resolve: {
				inconsistenciaVistoria: function(){
					return inconsistenciaVistoria;
				}
            }
		});

    };

    ctrl.avancar = function() {

        if(ctrl.tabAtiva === 0 && ctrl.validarCampos()) {

            ctrl.tabAtiva = ctrl.tabAtiva + 1;

        } else if(ctrl.tabAtiva + 1 === 2 && vistoriaValida()) {

            ctrl.tabAtiva = ctrl.tabAtiva + 1;
            window.scrollTo(0, 0);

            if(ctrl.parecer.vistoria.realizada === 'true') {

                ctrl.parecer.vistoria.realizada = true;

            } else {

                ctrl.parecer.vistoria.realizada = false;

            }

        }

    };

    $scope.snPaste = function(e, model) {
		var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
		e.preventDefault();
		setTimeout( function(){
		  document.execCommand('insertText', false, bufferText );
		}, 10 );
    };

    ctrl.baixarDocumento = function(anexo) {

        if(!anexo.id){

            documentoService.download(anexo.key, anexo.nomeDoArquivo);

        } else {

            documentoService.downloadById(anexo.id);

        }

    };
    
    ctrl.removerDocumentoRit = function() {

        documentoService.delete(ctrl.parecer.vistoria.documentoRit.key);
        ctrl.parecer.vistoria.documentoRit = null;

    };

    ctrl.removerDocumentosVistoria = function(index) {

        var anexo = ctrl.parecer.vistoria.anexos[index];
        ctrl.parecer.vistoria.anexos.splice(index, 1);

        documentoService.delete(anexo.key);

    };

    ctrl.upload = function(file, invalidFile, tipoDocumento) {

        if(invalidFile){
            ctrl.errors.isPdf = true;
        }

        if(file) {

            ctrl.errors.isPdf = false;
            ctrl.limparErrosVistoria();

            uploadService.save(file)
                .then(function(response) {

                    var nomeDoArquivo = file.name;

                    if(ctrl.parecer.documentos === null || ctrl.parecer.documentos === undefined) {
                        ctrl.parecer.documentos = [];
                    }

                    var quantidadeDocumentosComMesmoNome = ctrl.parecer.documentos.filter(function(documento) { 
                        return documento.nomeDoArquivo.includes(file.name.split("\.")[0]);
                    }).length;

                    if(quantidadeDocumentosComMesmoNome > 0) {
                        nomeDoArquivo = file.name.split("\.")[0] + " (" + quantidadeDocumentosComMesmoNome + ")." + file.name.split("\.")[1];
                    }

                    if(tipoDocumento === app.utils.TiposUpload.PARECER_ANALISE_TECNICA) {

                        ctrl.parecer.documentos.push({

                            key: response.data,
                            nomeDoArquivo: nomeDoArquivo,
                            tipo: {

                                id: app.utils.TiposDocumentosAnalise.PARECER_ANALISE_TECNICA
                            }
                            
                        });

                    } else if(tipoDocumento === app.utils.TiposUpload.NOTIFICACAO){

                        ctrl.parecer.documentos.push({

                            key: response.data,
                            nomeDoArquivo: nomeDoArquivo,
                            tipo: {

                                id: app.utils.TiposDocumentosAnalise.NOTIFICACAO
                            }
                        });

                    } else if(tipoDocumento === ctrl.tiposDocumentosAnalise.DOCUMENTO_RIT) {

                        ctrl.parecer.vistoria.documentoRit = {
                            key: response.data,
                            nomeDoArquivo: file.name,
                            tipo: {
                                id: ctrl.tiposDocumentosAnalise.DOCUMENTO_RIT
                            }
                        };

                        ctrl.errors.vistoria.documentoRit = false;

                    } else if(tipoDocumento === ctrl.tiposDocumentosAnalise.DOCUMENTO_VISTORIA) {

                        ctrl.parecer.vistoria.anexos.push({
                            key: response.data,
                            nomeDoArquivo: file.name,
                            tipo: {
                                id: ctrl.tiposDocumentosAnalise.DOCUMENTO_VISTORIA
                            }
                        });

                    }else if (tipoDocumento === ctrl.tiposDocumentosAnalise.AUTO_INFRACAO){

                        ctrl.anexos.push({
    
                            key: response.data,
                            nomeDoArquivo: file.name,
                            tipo: {
                                id: app.utils.TiposDocumentosAnalise.AUTO_INFRACAO
                            }
                        });

                    }

                }, function(error){
                    mensagem.error(error.data.texto);
                });

        } else if(invalidFile && invalidFile.$error === 'maxSize'){
            mensagem.error('Ocorreu um erro ao enviar o arquivo: ' + invalidFile.name);
        }

    };

    ctrl.downloadPDFNotificacao = function() {

        var analise = JSON.parse(JSON.stringify(this.analiseTecnica));

        analise.analise.processo.empreendimento = null;

        documentoAnaliseService.generatePDFNotificacaoParecerTecnico(analise)
            .then(
                function(data, status, headers){

                    var a = document.createElement('a');
                    a.href = URL.createObjectURL(data.data.response.blob);
                    a.download = data.data.response.fileName ? data.data.response.fileName : 'previa_notificacao_analise_tecnica.pdf';
                    a.click();
                },

                function(error){

                    mensagem.error(error.data.texto);
                }
            );
    };

    ctrl.clonarParecerTecnico = function() {

        if(ctrl.numeroProcessoClone) {

            // TODO: tem que fazer isso funcionar quando tiver parecer 

            // parecerAnalistaTecnicoService.getParecerByNumeroProcesso(ctrl.numeroProcessoClone)
            // 	.then(function(response){

            // 			if(response.data.parecer === undefined) {

            // 				ctrl.parecer.parecer = null;
            // 				mensagem.error(response.data.texto);

            // 				return;

            // 			} else{

            // 				ctrl.parecer = response.data;

            // 			}

            // 	}, function(error){
            // 		mensagem.error(error.data.texto);
            // 	});

        }

    };

    $scope.optionsText = {
        toolbar: [
            ['edit',['undo','redo']],
            ['style', ['bold', 'italic', 'underline', 'superscript', 'subscript', 'strikethrough', 'clear']],
            ['textsize', ['fontsize']],
            ['alignment', ['ul', 'ol', 'paragraph', 'lineheight']],
            ['height', ['height']],
            ['table', ['table']],
            ['insert', ['picture',]]
            
        ]
    };

    ctrl.getDocumentosParecer = function() {
        
        var documentosParecer = [];

        documentosParecer = _.filter(ctrl.parecer.documentos, function(documento) {
            return documento.tipo.id === app.utils.TiposDocumentosAnalise.PARECER_ANALISE_TECNICA;
        });

        return documentosParecer;
    };

    ctrl.removerDocumento = function (documento) {

        var indexDocumento = ctrl.parecer.documentos.indexOf(documento);

        ctrl.parecer.documentos.splice(indexDocumento, 1);

    };

    ctrl.adicionarInconsistenciaTecnicaTipoLicenca = function (analiseTecnica, tipoDeInconsistenciaTecnica) {

        var inconsistenciaTecnica = _.find( analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
    
                return inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca;
            });
       
        openModal(analiseTecnica, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, null, null, null, null, null, null, null);
   
    };

    ctrl.adicionarInconsistenciaTecnicaAtividade = function (analiseTecnica, tipoDeInconsistenciaTecnica, atividadeCaracterizacao, index) {

        var inconsistenciaTecnica = _.find( ctrl.analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
            return inconsistenciaTecnica.inconsistenciaTecnicaAtividade !== null &&
                inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id === atividadeCaracterizacao.id;
        });
   
        openModal(analiseTecnica, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, atividadeCaracterizacao, null, null, null, null, index, null);

    };

    ctrl.adicionarInconsistenciaTecnicaParametro = function (analiseTecnica, tipoDeInconsistenciaTecnica, parametroAtividade, index, indexParametro, atividade) {

        var inconsistenciaTecnica = _.find( ctrl.analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
            return inconsistenciaTecnica.inconsistenciaTecnicaParametro !== null &&
                inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade.id === parametroAtividade.id && 
                inconsistenciaTecnica.inconsistenciaTecnicaParametro.atividadeCaracterizacao.id === atividade.id;
        });
   
        openModal(analiseTecnica, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, atividade, parametroAtividade, null, null, null, index, indexParametro);

    };

    ctrl.adicionarInconsistenciaTecnicaQuestionario = function (analiseTecnica,tipoDeInconsistenciaTecnica) {

        var inconsistenciaTecnica = _.find( analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
    
            return inconsistenciaTecnica.inconsistenciaTecnicaQuestionario;
        });
   
        openModal(analiseTecnica, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, null, null, analiseTecnica.analise.processo.caracterizacao.questionario3, null, null, null, null);

    };

    ctrl.adicionarInconsistenciaTecnicaDocumentoAdministrativo = function (analiseTecnica, tipoDeInconsistenciaTecnica, documentoAdministrativo, index) {

        var inconsistenciaTecnica = _.find( analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){

            if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo !== null){
                if (documentoAdministrativo.id === inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo.id){
                    return inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo;
                }
            }
        });
   
        openModal(analiseTecnica, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, null, null, null, documentoAdministrativo, null, index, null);

    };

    ctrl.adicionarInconsistenciaTecnicaDocumentoTecnicoAmbiental = function (analiseTecnica,tipoDeInconsistenciaTecnica, documentoTecnicoAmbiental, index) {

        var inconsistenciaTecnica = _.find( analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){

            if(inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental !== null){
                if (documentoTecnicoAmbiental.id === inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.id){
                    return inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental;
                }
            }
    
        });
   
        openModal(analiseTecnica, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, null, null, null, null, documentoTecnicoAmbiental, index, null);

    };

    function openModal(analiseTecnicaModal, tipoDeInconsistenciaTecnicaModal, inconsistenciaTecnicaModal, 
                       atividadeCaracterizacaoModal, parametroAtividadeModal, questionarioModal, 
                       documentoAdministrativoModal, documentoTecnicoAmbientalModal, indexModal, indexParametroModal) {
            
        $uibModal.open({
            animation: true,
            templateUrl: './features/analiseTecnica/modalInconsistenciaTecnica.html',
            controller: 'inconsistenciaTecnicaController',
            controllerAs: 'modalCtrl',
            size: 'lg',
            resolve: {
                analiseTecnica: function () {
                    return analiseTecnicaModal;
                },
                tipoDeInconsistenciaTecnica: function(){
                    return tipoDeInconsistenciaTecnicaModal;
                },
                inconsistenciaTecnica: function(){
                    return inconsistenciaTecnicaModal;
                },
                atividadeCaracterizacao: function(){
                    return atividadeCaracterizacaoModal;
                },
                parametroAtividade: function(){
                    return parametroAtividadeModal;
                },
                questionario: function(){
                    return questionarioModal;
                },
                documentoAdministrativo: function(){
                    return documentoAdministrativoModal;
                },
                documentoTecnicoAmbiental: function(){
                    return documentoTecnicoAmbientalModal;
                },
                index: function(){
                    return indexModal;
                },
                indexParametro: function(){
                    return indexParametroModal;
                }
            }
        });
    }

    ctrl.validarItensLicenca = function (tipoDeInconsistenciaTecnica, analiseTecnica){

        var verificaAnaliseTecnica = null;

        analiseTecnicaService.getAnaliseTecnica(analiseTecnica.id).then(function(response){
            verificaAnaliseTecnica = response.data;
        
            if (tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.TIPO_LICENCA){

                inconsistenciaTecnica = _.some( verificaAnaliseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
                   return inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca;
                });

                if (!inconsistenciaTecnica){
                    ctrl.itemValidoLicenca.tipoLicenca = false;
                }else{
                    ctrl.itemValidoLicenca.tipoLicenca = true;
                }

            }else if (tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.QUESTIONARIO){

                inconsistenciaTecnica = _.some( verificaAnaliseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){
                    return inconsistenciaTecnica.inconsistenciaTecnicaQuestionario;
                });

                if(!inconsistenciaTecnica){            
                    ctrl.itemValidoLicenca.questionario = false;
                }else{
                    ctrl.itemValidoLicenca.questionario = true;
                }       
            }
        });
    };
    
    ctrl.excluirInconsistencia = function (analiseTecnica, tipoDeInconsistenciaTecnica, parametro, documento, atividade, index, indexParametro){

        inconsistenciaTecnica = _.find( analiseTecnica.inconsistenciasTecnica, function(inconsistenciaTecnica){

            if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.TIPO_LICENCA){
                ctrl.itemValidoLicenca.tipoLicenca = false;
                return inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca;

            }else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.ATIVIDADE){
                
                if(inconsistenciaTecnica.inconsistenciaTecnicaAtividade !== null && atividade.id === inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id){
                    ctrl.itemValidoLicenca.atividade[index].atividadeValida = false;
                    return inconsistenciaTecnica.inconsistenciaTecnicaAtividade;
                }

            }else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.PARAMETRO){
                
                if (inconsistenciaTecnica.inconsistenciaTecnicaParametro !== null && 
                    parametro.id === inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade.id && 
                    atividade.id === inconsistenciaTecnica.inconsistenciaTecnicaParametro.atividadeCaracterizacao.id){

                    ctrl.itemValidoLicenca.atividade[index].parametros[indexParametro] = false;
                    return inconsistenciaTecnica.inconsistenciaTecnicaParametro;
                }             

            }else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.QUESTIONARIO){
                ctrl.itemValidoLicenca.questionario = false;
                return inconsistenciaTecnica.inconsistenciaTecnicaQuestionario;

            }else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.DOCUMENTO_ADMINISTRATIVO){

                if (inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo !== null && documento.id === inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo.id){
                    ctrl.itemValidoLicenca.documentoAdministrativo[index] = false;
                    return inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo;
                }  

            }else if (tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL){

                if (inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental !== null && documento.id === inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos.id){
                    ctrl.itemValidoLicenca.documentoTecnicoAmbiental[index] = false;
                    return inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental;
                }  
            }
        });
        inconsistenciaTecnica.tipoDeInconsistenciaTecnica = tipoDeInconsistenciaTecnica;

        inconsistenciaService.excluirInconsistenciaTecnica(inconsistenciaTecnica)
            .then(function (response) {

                mensagem.success(response.data);
                
                if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.PARAMETRO){
                    if (parametro.id === inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade.id){
                        ctrl.validarInconsistenciaParametro(tipoDeInconsistenciaTecnica, parametro, index, indexParametro, atividade);
                    }
                }else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.ATIVIDADE){
                    if(atividadeCaracterizacao.id === inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id){
                        ctrl.validarInconsistenciaAtividade(tipoDeInconsistenciaTecnica, index, atividade);
                    }
                }else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.DOCUMENTO_ADMINISTRATIVO){
                    if(atividadeCaracterizacao.id === inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id){
                        ctrl.validarInconsistenciaDocumentoAdministrativo(tipoDeInconsistenciaTecnica, documentoAdministrativo, index);
                    }
                }else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL){
                    if(atividadeCaracterizacao.id === inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao.id){
                        ctrl.validarInconsistenciaDocumentoTecnicoAmbiental(tipoDeInconsistenciaTecnica, documentoTecnicoAmbiental, index); 
                    }
                }

            }).catch(function (response) {
            mensagem.error(response.data.texto);

        });       
        
    };

    ctrl.visualizarQuestionario = function() {
        $uibModal.open({

			component: 'modalVisualizarQuestionario',
			size: 'lg',
			resolve: {
                idProcesso: function(){
                    return ctrl.analiseTecnica.analise.processo.id;
                }
			}
		});
    };

    $rootScope.$on('atualizarMarcacaoInconsistencia', function(event, tipoDeInconsistenciaTecnica, inconsistenciaTecnica, index, indexParametro) {

        ctrl.analiseTecnica.inconsistenciasTecnica.push(inconsistenciaTecnica);

        if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.PARAMETRO) {
        
            ctrl.validarInconsistenciaParametro(tipoDeInconsistenciaTecnica, inconsistenciaTecnica.inconsistenciaTecnicaParametro.parametroAtividade, index, indexParametro, inconsistenciaTecnica.inconsistenciaTecnicaParametro.atividadeCaracterizacao);
        
        } else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.ATIVIDADE) {

            ctrl.validarInconsistenciaAtividade(tipoDeInconsistenciaTecnica, index, inconsistenciaTecnica.inconsistenciaTecnicaAtividade.atividadeCaracterizacao);

        } else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.DOCUMENTO_ADMINISTRATIVO) {

            ctrl.validarInconsistenciaDocumentoAdministrativo(tipoDeInconsistenciaTecnica, inconsistenciaTecnica.inconsistenciaTecnicaDocumentoAdministrativo.documentoAdministrativo, index);
        
        } else if(tipoDeInconsistenciaTecnica === ctrl.tipoDeInconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL) {
           
            ctrl.validarInconsistenciaDocumentoTecnicoAmbiental(tipoDeInconsistenciaTecnica, inconsistenciaTecnica.inconsistenciaTecnicaDocumentoTecnicoAmbiental.documentosTecnicos, index);
        
        } else {
            ctrl.validarItensLicenca(tipoDeInconsistenciaTecnica, ctrl.analiseTecnica, index);
        }

    });
    
    ctrl.removerDocumentoAnaliseTecnica = function (indiceDocumento) {
    
        ctrl.anexos.splice(indiceDocumento,1);

    };

    ctrl.getDocumentosAutoInfracao = function() {
		
		var documentoAutoInfracao = [];

		documentoAutoInfracao = _.filter(ctrl.anexos, function(documento) {
			return documento.tipo.id === app.utils.TiposDocumentosAnalise.AUTO_INFRACAO;
		});

		return documentoAutoInfracao;
    };
    
    ctrl.validarCampos = function () {

        if(ctrl.pergunta === null) {
            ctrl.errors.pergunta = true;
            return false;

        } else if(ctrl.pergunta === "true") {

            if (_.isEmpty(ctrl.anexos) || ctrl.anexos === null) {
                ctrl.errors.autoInfracao = true;
                return false;

            } else{

                _.forEach(ctrl.anexos , function(documentoAutoInfracao) {

                    if(documentoAutoInfracao.tipo.id === app.utils.TiposDocumentosAnalise.AUTO_INFRACAO){
                        ctrl.errors.autoInfracao = false;
                    }

                });

                return true;

            }

        } else if (ctrl.pergunta === "false"){

            return true;

        }

    };

    ctrl.visualizarDocumento = function (documento){

        documentoLicenciamentoService.download(documento.id);
            
    };

    ctrl.concluir = function () {

		if(!camposConclusaoValidos()) {

			mensagem.error('Não foi possível concluir a análise. Verifique os campos obrigatórios!', { ttl: 10000 });
            return;
            
        }
        
        if(ctrl.anexos.length > 0) {

            ctrl.parecer.documentos = _.concat(ctrl.parecer.documentos, ctrl.anexos);

        }

        var parecerValido = false;

        if(parseInt(ctrl.parecer.tipoResultadoAnalise.id) === ctrl.tiposResultadoAnalise.DEFERIDO) {

            ctrl.parecer.validadePermitida = ctrl.tipoLicenca.validadeEmAnos;
            parecerValido = parecerDeferidoValido();

        } else if(parseInt(ctrl.parecer.tipoResultadoAnalise.id) === ctrl.tiposResultadoAnalise.INDEFERIDO) {

            parecerValido = parecerIndeferidoValido();

        }

        if(parecerValido) {

            parecerAnalistaTecnicoService.concluir(ctrl.parecer)
            .then(function(response) {

                $location.path('/analise-tecnica');
                mensagem.setMensagemProximaTela('success', response.data.texto);

            });

        }

        $rootScope.$broadcast('atualizarContagemProcessos');

    };
        
    ctrl.visualizarDocumentoAnalise = function (anexo){

        documentoService.download(anexo.key);
            
    };

};

exports.controllers.AnaliseTecnicaController = AnaliseTecnicaController;
