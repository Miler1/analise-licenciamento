var ParecerJuridicoController = function(mensagem, $scope, parecerJuridicoService, $window,$routeParams, documentoService, documentoLicenciamentoService, tamanhoMaximoArquivoAnaliseMB,uploadService,$timeout) {
	
	$scope.parecerJuridico = null;
	$scope.anexos = [];
	$scope.tiposResultadoAnalise = app.utils.TiposResultadoAnalise;
	$scope.descricaoParecer = '';
	$scope.idTipoResultadoAnalise = '';
	$scope.documentos = [];
	$scope.errors = {

		resultadoAnalise:false
	
	};

	$scope.setDocumentos = function() {
		
		if ($scope.parecerJuridico !== null && $scope.parecerJuridico !== undefined) {

			if ($scope.parecerJuridico.parecerAnalistaGeo.documentoParecer !== null) {

				$scope.documentos.push($scope.parecerJuridico.parecerAnalistaGeo.documentoParecer);
			}

			if ($scope.parecerJuridico.parecerAnalistaGeo.cartaImagem !== null) {

				$scope.documentos.push($scope.parecerJuridico.parecerAnalistaGeo.cartaImagem);

			}

		}
		
	};
		
	$timeout(function () {

		parecerJuridicoService.findParecerJuridico($routeParams.idParecerJuridico)
			.then(function(response){

			$scope.parecerJuridico = response.data;
			
			// _.forEach($scope.pareceresJuridicos, function(parecerJuridico) {

			// 	if(parecerJuridico.id.toString() === $routeParams.idParecerJuridico) {

			// 		$scope.parecerJuridico = parecerJuridico;
					
			// 	}
			// });

			$scope.setDocumentos();
			
			if (!$scope.parecerJuridico.ativo) {
				$window.location.href="https://www.sema.ap.gov.br/";
			}

		}).catch(function(response){

			mensagem.error(response.data);

		});
		
	}, 1500);

	$scope.upload = function(file, invalidFile) {

		if(file) {

			uploadService.saveExterno(file)
				.then(function(response) {

					var nomeDoArquivo = file.name;

					var quantidadeDocumentosComMesmoNome = $scope.anexos.filter(function(documento) {
						return documento.nomeDoArquivo.includes(file.name.split("\.")[0]);
					}).length;

					if(quantidadeDocumentosComMesmoNome > 0) {
						nomeDoArquivo = file.name.split("\.")[0] + " (" + quantidadeDocumentosComMesmoNome + ")." + file.name.split("\.")[1];
					}

					$scope.anexos.push({

						key: response.data,
						nomeDoArquivo: nomeDoArquivo,
						tipoDocumento: {

							id: app.utils.TiposDocumentosAnalise.DOCUMENTO_JURIDICO
						}
					});
													
			}, function(error){

				mensagem.error(error.data.texto);
			});

		} else if(invalidFile && invalidFile.$error === 'maxSize'){

			mensagem.error('Ocorreu um erro ao enviar o arquivo: ' + invalidFile.name + ' . Verifique se o arquivo tem no máximo ' + TAMANHO_MAXIMO_ARQUIVO_MB + 'MB');
		}
	};

	$scope.removerDocumento = function (indiceDocumento) {

		$scope.anexos.splice(indiceDocumento,1);
	};

	$scope.baixarDocumento = function(anexo) {
		
		documentoService.downloadAnexoExterno(anexo.key, anexo.nomeDoArquivo);
	};

	$scope.downloadDocumentos = function (id) {

		documentoService.downloadDocumentoAnalise(id);

	};

	$scope.downloadDocumentoFundiario = function (id) {

		documentoLicenciamentoService.downloadLicenciamento(id);
	};

	$scope.TAMANHO_MAXIMO_ARQUIVO_MB = tamanhoMaximoArquivoAnaliseMB;
	
	$scope.cancelar = function () {
		$window.location.href="https://www.sema.ap.gov.br/";
	};

	$scope.enviar = function () {

		if(!$scope.parecerJuridico.tipoResultadoAnalise){
			$scope.errors.resultadoAnalise = true;
			mensagem.error("Verifique os campos obrigatórios!",{referenceId: 5});
			return false;
		}else{
			$scope.errors.resultadoAnalise = false;
		}

		if (!$scope.parecerJuridico.parecer || $scope.parecerJuridico.parecer === ''){
			$scope.errors.consideracoes = true;
			mensagem.error("Verifique os campos obrigatórios!",{referenceId: 5});
			return false;

		}else{

			var params = { 
				id: $routeParams.idParecerJuridico,
				tipoResultadoAnalise: {id: $scope.parecerJuridico.tipoResultadoAnalise.id},
				parecer: $scope.parecerJuridico.parecer,
				anexos: $scope.anexos
			};

			parecerJuridicoService.enviar(params)
				.then(function (response) {

					$window.location.href="https://www.sema.ap.gov.br/";
					
			}, function(error){
				mensagem.error(error.data.texto);
			});
		}
	};

};

exports.controllers.ParecerJuridicoController = ParecerJuridicoController;
