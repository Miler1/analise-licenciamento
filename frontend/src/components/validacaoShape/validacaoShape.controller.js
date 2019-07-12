/**
 * Controller para a validação de shapes
 **/
var ValidacaoShapeController = function (validacaoShapeService, mensagem, $scope) {

	var validacaoShape = this;

	/** Atribuição das funções **/
	validacaoShape.init = init;
	validacaoShape.alterarArquivo = alterarArquivo;
	validacaoShape.atualizaBarraProgresso = atualizaBarraProgresso;
	validacaoShape.cancelaUpload = cancelaUpload;
	validacaoShape.abrirModal = abrirModal;
	validacaoShape.arquivoShapeEscolhido = arquivoShapeEscolhido;
	
	/** Atribuição das variáveis **/
	validacaoShape.progressBarStatus = 0;
	validacaoShape.resultadoProcessamento = null;

	/** Declaração das funções **/
	function init(tipo,cor,popupText){
		validacaoShape.tipo = tipo;
		validacaoShape.cor = cor;
		validacaoShape.popupText = popupText;
	}

	function atualizaBarraProgresso(evt) {
		var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
		validacaoShape.progressBarStatus = progressPercentage + '%';
	}

	function cancelaUpload() {
		validacaoShapeService.abortUploadShapeFile();

		validacaoShape.progressBarStatus = 0;
		validacaoShape.arquivoSelecionado = null;
		validacaoShape.resultadoProcessamento = null;
		validacaoShape.resultadoEnvio = null;

		$scope.$emit('shapefile:eraseUpload', {geometria: null, tipo: validacaoShape.tipo});
	}

	function alterarArquivo() {
		validacaoShape.progressBarStatus = 0;
		validacaoShape.arquivoSelecionado = null;
		validacaoShape.resultadoProcessamento = null;
		validacaoShape.resultadoEnvio = null;

		$scope.$emit('shapefile:eraseUpload', {geometria: null, tipo: validacaoShape.tipo});
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


							if (validacaoShape.resultadoEnvio.atributos[i].tipo === 'Geometry') {

								validacaoShape.resultadoEnvio.atributos[i].nomeBanco = 'the_geom';
								validacaoShape.resultadoEnvio.atributos[i].tipoGeometria = true;

								// Linha para converter de texto para JSON de geometria
								validacaoShape.resultadoEnvio.registros[i][i].valor = JSON.parse(validacaoShape.resultadoEnvio.registros[i][i].valor);
								validacaoShape.shapeEnviado = validacaoShape.resultadoEnvio.registros[i][i].valor;
								
								$scope.$emit('shapefile:uploaded', {
									geometria: validacaoShape.shapeEnviado, 
									tipo: validacaoShape.tipo, 
									estilo: {
										style: {
											fillColor: validacaoShape.cor,
											color: validacaoShape.cor,
											fillOpacity: 0.2
										}
									},
									popupText: validacaoShape.popupText
								});
							}
						}
					}

					else {
						if (response.data.data.mensagens[0] === "error.shapefile.geometry.invalid.notPolygon") {
							restartUploadOnError('O arquivo enviado é inválido. Não possui um polígono na sua geometria');
						} else if (response.data.data.mensagens[0] === "error.shapefile.structure") {
							restartUploadOnError('O arquivo enviado é inválido. Não possui uma estrutura válida de shapes');
						} else if( response.data.data.mensagens[0] === "error.shapefile.not.sirgas2000") {
							restartUploadOnError('O arquivo enviado é inválido. Não está no formato SIRGAS 2000');
						}
						else { 
							restartUploadOnError(response.data.data.mensagens[0]);
						}
					}

					validacaoShape.resultadoProcessamento = resultado;
				}

			}, null, validacaoShape.atualizaBarraProgresso)
			.catch(function (response) {
				restartUploadOnError(response.data.data.mensagens[0]);
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
