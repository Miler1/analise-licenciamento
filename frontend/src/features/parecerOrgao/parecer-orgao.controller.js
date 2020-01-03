var ParecerOrgaoController = function(mensagem, $scope, parecerOrgaoService, $window,$routeParams, documentoService, tamanhoMaximoArquivoAnaliseMB,uploadService,$timeout) {

	$scope.comunicado = null;
	$scope.anexos = [];
		
	$timeout(function () {
		
		parecerOrgaoService.findComunicado($routeParams.idComunicado)
		.then(function(response){

			$scope.comunicado = response.data;
			
			if (!comunicado.valido) {
				$window.location.href="http://www.ipaam.am.gov.br/";
			}

		}).catch(function(response){

			mensagem.error(response.data);

		});
		
	}, 150);



	$scope.upload = function(file, invalidFile) {

		if(file) {

			uploadService.saveExterno(file)
				.then(function(response) {

					$scope.anexos.push({

						key: response.data,
						nomeDoArquivo: file.name,
						tipoDocumento: {

							id: app.utils.TiposDocumentosAnalise.PARECER_ORGAO
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

	$scope.baixarDocumento= function(anexo) {
		
		documentoService.download(anexo.key, anexo.nomeDoArquivo);
	};

	$scope.TAMANHO_MAXIMO_ARQUIVO_MB = tamanhoMaximoArquivoAnaliseMB;
	
	$scope.cancelar = function () {
		$window.location.href="http://www.ipaam.am.gov.br/";
	};

	$scope.enviar = function () {

		var parecerOrgao = document.getElementById('descricaoParecer').value;

		if (!parecerOrgao || parecerOrgao ===''){

			$scope.comunicado.parecerOrgao = true;
			mensagem.error("Verifique os campos obrigatórios!",{referenceId: 5});
			return false;

		}else{

			var params = { 
				id: $routeParams.idComunicado,
				parecerOrgao: parecerOrgao,
				anexos: $scope.anexos
			};

			parecerOrgaoService.enviar(params)
				.then(function (response) {

					if(response.data ==true){

						$window.location.href="http://www.ipaam.am.gov.br/";

					}else{

						mensagem.error("Verifique os campos obrigatórios!",{referenceId: 5});

					}
			});
		}
	};

};

exports.controllers.ParecerOrgaoController = ParecerOrgaoController;
