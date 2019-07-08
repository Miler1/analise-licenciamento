/**
 * Controller para a validação de shapes
 **/
var ValidacaoShapeController = function (validacaoShapeService, mensagem) {

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
		return validacaoShape.descricao &&
			validacaoShape.necessitaValidacao &&
			validacaoShape.resultadoEnvio;
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
		validacaoShape.resultadoEnvio = null;
	}

    /**
     * Solução para que o arquivo não seja apagado ao se clicar novamente no botão de upload e cancelar a ação
     **/
	function arquivoShapeEscolhido(files, file) {

		if (files.length === 0) {

			validacaoShape.arquivoSelecionado = null;

			mensagem.error('O arquivo enviado não possui uma extensão de arquivo válida. Envie arquivos cuja extensão seja \".zip\" ou \".rar\"');

			return;
		}

		var tamanhoMaximoEmBytes = 104857600; // 100MB

		var fileValidate = files.pop();

		if (fileValidate && fileValidate.size > tamanhoMaximoEmBytes) {

			validacaoShape.arquivoSelecionado = null;

			mensagem.error('O arquivo escolhido ultrapassa o limite de tamanho aceito pelo sistema (100MB). Envie um arquivo que não ultrapasse esse tamanho');

			return;
		}

		var arquivoEnviar = validacaoShape.arquivoSelecionado.arquivo = fileValidate;

		validacaoShapeService.uploadShapeFile(arquivoEnviar)
			.then(function (response) {

				var resultado = response.data;

				if (resultado) {

					if (resultado.data.status === 'SUCESSO') {

						var dados = resultado.data.dados;
						validacaoShape.arquivoSelecionado.arquivo.key = dados.keyTemp;
						validacaoShape.arquivoSelecionado.arquivo.nome = arquivoEnviar.name;

						// Publica resultado
						validacaoShape.resultadoEnvio = dados;

						if (dados.atributos.length < 1) {
							restartUploadOnError('O arquivo enviado é inválido. A quantidade de atributos do shapefile é menor que a quantidade de colunas obrigatórias.');
							return;
						}

						for (var i = 0; i < validacaoShape.resultadoEnvio.atributos.length; i++) {

							// validacaoShape.resultadoEnvio.atributos[i].colunasBanco = [];

							// for (var j = 0; j < validacaoShape.colunasBanco.length; j++) {
							// 	if (validacaoShape.resultadoEnvio.atributos[i].tipo === validacaoShape.colunasBanco[j].tipo && validacaoShape.colunasBanco[j].obrigatorio) {
							// 		validacaoShape.resultadoEnvio.atributos[i].colunasBanco.push(validacaoShape.colunasBanco[j]);
							// 	}
							// }

							if (validacaoShape.resultadoEnvio.atributos[i].tipo === 'Geometry') {

								validacaoShape.resultadoEnvio.atributos[i].nomeBanco = 'the_geom';
								validacaoShape.resultadoEnvio.atributos[i].tipoGeometria = true;

								// Linha para converter de texto para JSON de geometria
								// Fazer isso antes de colocar no mapa - E NÃO SALVAR NO BANCO ANTES
								// L.geoJSON(JSON.parse(ctrl.localizacaoEmpreendimento)).addTo(map);
								validacaoShape.resultadoEnvio.registros[i][i].valor = JSON.parse(validacaoShape.resultadoEnvio.registros[i][i].valor);
							}

							if (validacaoShape.resultadoEnvio.atributos[i].tipo === 'Date') {

								for (var v = 0; v < validacaoShape.resultadoEnvio.registros.length; v++) {

									if (validacaoShape.resultadoEnvio.registros[v][i].valor) {
										validacaoShape.resultadoEnvio.registros[v][i].valor = new Date(validacaoShape.resultadoEnvio.registros[v][i].valor).asString();
									} else {
										validacaoShape.resultadoEnvio.registros[v][i].valor = '-';
									}

								}
							}
						}
					}

					else {
						restartUploadOnError(response.data);
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
		validacaoShape.resultadoEnvio = null;

		mensagem.warning(message);

		return [];
	}

	function abrirModal() {

		$('#modalEspecificacoesArquivo').modal('show');
	}

};

exports.controllers.ValidacaoShapeController = ValidacaoShapeController;
