var InconsistenciaController = function ($uibModalInstance,analiseGeo,categoriaInconsistencia,tamanhoMaximoArquivoAnaliseMB,uploadService, inconsistenciaService, mensagem) {

	var inconsistenciaController = this;

	inconsistenciaController.descricaoInconsistencia = null;
	inconsistenciaController.tipoInconsistencia = null;
	inconsistenciaController.anexos = [];
	inconsistenciaController.TAMANHO_MAXIMO_ARQUIVO_MB = tamanhoMaximoArquivoAnaliseMB;

	inconsistenciaController.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

	inconsistenciaController.upload = function(file, invalidFile) {

		if(file) {

				uploadService.save(file)
						.then(function(response) {

							inconsistenciaController.anexos.push({

										key: response.data,
										nome: file.name,
										tipoDocumento: {

												id: app.utils.TiposDocumentosAnalise.INCONSISTENCIA
										}
								});
															
						}, function(error){

								mensagem.error(error.data.texto);
						});

		} else if(invalidFile && invalidFile.$error === 'maxSize'){

				mensagem.error('Ocorreu um erro ao enviar o arquivo: ' + invalidFile.name + ' . Verifique se o arquivo tem no máximo ' + TAMANHO_MAXIMO_ARQUIVO_MB + 'MB');
		}
};

	 inconsistenciaController.removerDocumento = function (indiceDocumento) {

		inconsistenciaController.anexos.splice(indiceDocumento,1);
	};

	inconsistenciaController.concluir = function() {
		var params;

		params = {
			analiseGeo: {id: analiseGeo.id},
			tipoInconsistencia: inconsistenciaController.tipoInconsistencia,
			descricaoInconsistencia: inconsistenciaController.descricaoInconsistencia,
			categoria: categoriaInconsistencia,
			anexos: inconsistenciaController.anexos
		};

		inconsistenciaService.salvarInconsistencia(params)
			.then(function(response){
				mensagem.success(response.data.texto);
				$uibModalInstance.dismiss('cancel');
				
			}).catch(function(response){
				mensagem.error(response.data.texto);
				$uibModalInstance.dismiss('cancel');
			});
		
	};

	inconsistenciaController.baixarDocumentoInconsistencia= function(idDocumento) {

		inconsistenciaService.download(idDocumento);
	};

};

exports.controllers.InconsistenciaController = InconsistenciaController;