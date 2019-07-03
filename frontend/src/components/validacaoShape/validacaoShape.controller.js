/**
 * Controller para a validação de shapes
 **/
var ValidacaoShapeController = function (validacaoShapeService) {

	var validacaoShape = this;

	/** Atribuição das funções **/
	validacaoShape.alterarArquivo = alterarArquivo;
	validacaoShape.atualizaBarraProgresso = atualizaBarraProgresso;
	validacaoShape.cancelaUpload = cancelaUpload;
	validacaoShape.abrirModal = abrirModal;
	validacaoShape.arquivoShapeEscolhido = arquivoShapeEscolhido;
	validacaoShape.passoValido = passoValido;

	/** Atribuição das variáveis **/
	validacaoShape.progressBarStatus = 0;
	validacaoShape.resultadoProcessamento = null;

	/** Declaração das funções **/
	function passoValido() {
		return $scope.cadastro.la.descricao &&
			$scope.cadastro.la.necessitaValidacao &&
			$scope.cadastro.la.resultadoEnvio;
	}

	function atualizaBarraProgresso(evt) {
		var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);

		validacaoShape.progressBarStatus = progressPercentage + '%';
	}

	function cancelaUpload() {

		validacaoShapeService.abortUploadShapeFile();
	}

	function alterarArquivo() {
		validacaoShape.progressBarStatus = 0;
		validacaoShape.arquivoSelecionado = null;
		validacaoShape.resultadoProcessamento = null;
		$scope.cadastro.la.resultadoEnvio = null;
	}

    /**
     * Solução para que o arquivo não seja apagado ao se clicar novamente no botão de upload e cancelar a ação
     **/
	function arquivoShapeEscolhido(files, file) {

		if (files.length === 0) {

			validacaoShape.arquivoSelecionado = null;

			mensagemService.error('O arquivo enviado não possui uma extensão de arquivo válida. Envie arquivos cuja extensão seja \".zip\" ou \".rar\"');

			return;
		}

		var tamanhoMaximoEmBytes = 104857600; // 100MB

		var fileValidate = files.pop();

		if (fileValidate && fileValidate.size > tamanhoMaximoEmBytes) {

			validacaoShape.arquivoSelecionado = null;

			mensagemService.error('O arquivo escolhido ultrapassa o limite de tamanho aceito pelo sistema (100MB). Envie um arquivo que não ultrapasse esse tamanho');

			return;
		}

		var arquivoEnviar = validacaoShape.arquivoSelecionado.arquivo = fileValidate;

		validacaoShapeService.uploadShapeFile(arquivoEnviar)
			.then(function (response) {

				var resultado = response.data;

				if (resultado) {

					if (resultado.status === 'SUCESSO') {

						var dados = resultado.dados;
						validacaoShape.arquivoSelecionado.arquivo.key = dados.keyTemp;
						validacaoShape.arquivoSelecionado.arquivo.nome = arquivoEnviar.name;

						// Publica resultado
						$scope.cadastro.la.resultadoEnvio = dados;

						if (dados.atributos.length < $scope.cadastro.quantidadeColunasObrigatorias) {
							restartUploadOnError('O arquivo enviado é inválido. A quantidade de atributos do shapefile é menor que a quantidade de colunas obrigatórias.');
							return;
						}

						for (var i = 0; i < $scope.cadastro.la.resultadoEnvio.atributos.length; i++) {

							$scope.cadastro.la.resultadoEnvio.atributos[i].colunasBanco = [];

							for (var j = 0; j < $scope.cadastro.colunasBanco.length; j++) {
								if ($scope.cadastro.la.resultadoEnvio.atributos[i].tipo === $scope.cadastro.colunasBanco[j].tipo && $scope.cadastro.colunasBanco[j].obrigatorio) {
									$scope.cadastro.la.resultadoEnvio.atributos[i].colunasBanco.push($scope.cadastro.colunasBanco[j]);
								}
							}

							if ($scope.cadastro.la.resultadoEnvio.atributos[i].tipo === 'Geometry') {

								$scope.cadastro.la.resultadoEnvio.atributos[i].nomeBanco = 'the_geom';
								$scope.cadastro.la.resultadoEnvio.atributos[i].tipoGeometria = true;
							}

							if ($scope.cadastro.la.resultadoEnvio.atributos[i].tipo === 'Date') {

								for (var v = 0; v < $scope.cadastro.la.resultadoEnvio.registros.length; v++) {

									if ($scope.cadastro.la.resultadoEnvio.registros[v][i].valor) {
										$scope.cadastro.la.resultadoEnvio.registros[v][i].valor = new Date($scope.cadastro.la.resultadoEnvio.registros[v][i].valor).asString();
									} else {
										$scope.cadastro.la.resultadoEnvio.registros[v][i].valor = '-';
									}

								}
							}
						}
					}

					validacaoShape.resultadoProcessamento = resultado;
				}

			}, null, validacaoShape.atualizaBarraProgresso)
			.catch(function (response) {

				restartUploadOnError(response.data);
			});
	}

	function restartUploadOnError(message) {

		validacaoShape.arquivoSelecionado = null;
		validacaoShape.resultadoProcessamento = null;
		$scope.cadastro.la.resultadoEnvio = null;

		mensagemService.warning(message);

		return [];
	}

	function abrirModal() {

		$('#modalEspecificacoesArquivo').modal('show');
	}

};

exports.controllers.ValidacaoShapeController = ValidacaoShapeController;
