var InconsistenciaController = function ($scope,$uibModalInstance,analiseGeo,categoriaInconsistencia, documentoService, inconsistencia,tamanhoMaximoArquivoAnaliseMB,uploadService, inconsistenciaService, mensagem) {

	var inconsistenciaController = this;
	inconsistenciaController.anexos = [];

	inconsistenciaController.habilitaExcluir = inconsistencia !== null;

	if(inconsistencia){
		inconsistenciaController.descricaoInconsistencia = inconsistencia.descricaoInconsistencia;
		inconsistenciaController.tipoInconsistencia = inconsistencia.tipoInconsistencia;
		inconsistenciaController.anexos = inconsistencia.anexos;
		inconsistenciaController.id = inconsistencia.id;
		
	}
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
										nomeDoArquivo: file.name,
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
		if(inconsistencia){
			params ={
				id : inconsistencia.id,
				analiseGeo: {id: analiseGeo.id},
				tipoInconsistencia: inconsistenciaController.tipoInconsistencia,
				descricaoInconsistencia: inconsistenciaController.descricaoInconsistencia,
				categoria: categoriaInconsistencia,
				anexos: inconsistenciaController.anexos,
				atividadeCaracterizacao: {id:inconsistencia.idAtividadeCaracterizacao},
				geometriaAtividade: {id: inconsistencia.idGeometriaAtividade}
			};
		}else{
			params = {
				analiseGeo: {id: analiseGeo.id},
				tipoInconsistencia: inconsistenciaController.tipoInconsistencia,
				descricaoInconsistencia: inconsistenciaController.descricaoInconsistencia,
				categoria: categoriaInconsistencia,
				anexos: inconsistenciaController.anexos,
				atividadeCaracterizacao: {id: inconsistencia.idAtividadeCaracterizacao},
				geometriaAtividade: {id: inconsistencia.idGeometriaAtividade}
			};
		}
		inconsistenciaService.salvarInconsistencia(params)
			.then(function(response){
				mensagem.success("Inconsistência salva com sucesso!");

				var retorno = {
					inconsistencia: response.data,
					isEdicao: params.id !== undefined && params.id !== null
				};

				$uibModalInstance.close(
					retorno);

			}).catch(function(response){
				mensagem.error(response.data.texto, {referenceId: 5});
				
			});
		
	};

	inconsistenciaController.baixarDocumentoInconsistencia= function(anexo) {

		if(!anexo.id){
			documentoService.download(anexo.key, anexo.nomeDoArquivo);
		}else{
			inconsistenciaService.download(anexo.id);
		}

	};

	inconsistenciaController.customDialogButtons = {
		warning: {
			label: "Cancelar",
			className: "btn-default",
			callback: function() {

			}
		},
		main: {
			label: "Excluir",
			className: "btn-success",
			callback: function() {

				inconsistenciaService.excluirInconsistencia(inconsistencia.id)
					.then(function (response) {
						mensagem.success(response.data);

						var retorno = {
							inconsistencia: inconsistencia,
							isExclusao: true
						};

						$uibModalInstance.close(
							retorno);
					}).catch(function (response) {
					mensagem.error(response.data.texto);

				});
			}
		}
	};

};

exports.controllers.InconsistenciaController = InconsistenciaController;