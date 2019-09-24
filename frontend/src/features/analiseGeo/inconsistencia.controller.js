var InconsistenciaController = function ($scope,
		$uibModalInstance,
		analiseGeo,
		categoriaInconsistencia,
		idGeometriaAtividade,
		idSobreposicao,
		idCaracterizacao,
		documentoService,
		inconsistencia,
		tamanhoMaximoArquivoAnaliseMB,
		uploadService,
		inconsistenciaService,
		mensagem,
		dadosProjeto,
		listaInconsistencias) {

	var inconsistenciaController = this;
	inconsistenciaController.anexos = [];
	inconsistenciaController.idCaracterizacao = null;

	inconsistenciaController.orgaos = app.utils.Orgao;
	
	inconsistenciaController.categoriaInconsistencia = categoriaInconsistencia;
	
	inconsistenciaController.habilitaExcluir = inconsistencia !== null;
	inconsistenciaController.isEdicao = inconsistencia !== null && inconsistencia.isEdicao !== undefined ? inconsistencia.isEdicao : false;

	if(inconsistencia){
		inconsistenciaController.descricaoInconsistencia = inconsistencia.descricaoInconsistencia;
		inconsistenciaController.tipoInconsistencia = inconsistencia.tipoInconsistencia;
		inconsistenciaController.anexos = inconsistencia.anexos;
		inconsistenciaController.id = inconsistencia.id;
		inconsistenciaController.idSobreposicao = idSobreposicao;
		
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

	inconsistenciaController.buscarIdCaracterizacaoPorRestricao = function() {
		var id;

		_.forEach(dadosProjeto.restricoes, function(restricao){

			var sobreposicao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

			if(inconsistenciaController.idSobreposicao === sobreposicao.id){
				id = dadosProjeto.caracterizacao.id;
			}
		});

		return id;
	};

	inconsistenciaController.concluir = function() {

		var params;

		inconsistenciaController.idCaracterizacao = idCaracterizacao ? idCaracterizacao : inconsistenciaController.buscarIdCaracterizacaoPorRestricao();
		inconsistenciaController.idSobreposicao = idSobreposicao ? idSobreposicao : inconsistenciaController.idSobreposicao;

		var restricao = _.find(dadosProjeto.restricoes, function(restricao) {

			var sobreposicao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;
			
			return sobreposicao.id === inconsistenciaController.idSobreposicao;

		});

		restricao = restricao || {};

		var paramsInconsistencia = {
			categoria: categoriaInconsistencia ? categoriaInconsistencia : inconsistenciaController.categoriaInconsistencia,
			caracterizacao: {id: inconsistenciaController.idCaracterizacao},
			analiseGeo: {id: analiseGeo.id},
			sobreposicaoCaracterizacaoAtividade: restricao.sobreposicaoCaracterizacaoAtividade || null,
			sobreposicaoCaracterizacaoEmpreendimento: restricao.sobreposicaoCaracterizacaoEmpreendimento || null,
			sobreposicaoCaracterizacaoComplexo: restricao.sobreposicaoCaracterizacaoComplexo || null

		};

		inconsistenciaService.findInconsistencia(paramsInconsistencia)
		.then(function(inconsistenciaResponse){

			inconsistencia = inconsistenciaResponse.data;

			if(inconsistencia){

				params = {
					id : inconsistencia.id,
					analiseGeo: {id: analiseGeo.id},
					tipoInconsistencia: inconsistenciaController.tipoInconsistencia,
					descricaoInconsistencia: inconsistenciaController.descricaoInconsistencia,
					categoria: categoriaInconsistencia ? categoriaInconsistencia : inconsistenciaController.categoriaInconsistencia,
					anexos: inconsistenciaController.anexos,
					caracterizacao: {id: inconsistenciaController.idCaracterizacao},
					geometriaAtividade: {id: idGeometriaAtividade},
					sobreposicaoCaracterizacaoAtividade: restricao.sobreposicaoCaracterizacaoAtividade || null,
					sobreposicaoCaracterizacaoEmpreendimento: restricao.sobreposicaoCaracterizacaoEmpreendimento || null,
					sobreposicaoCaracterizacaoComplexo: restricao.sobreposicaoCaracterizacaoComplexo || null
				};

			}else{

				params = {
					analiseGeo: {id: analiseGeo.id},
					tipoInconsistencia: inconsistenciaController.tipoInconsistencia,
					descricaoInconsistencia: inconsistenciaController.descricaoInconsistencia,
					categoria: categoriaInconsistencia ? categoriaInconsistencia : inconsistenciaController.categoriaInconsistencia,
					anexos: inconsistenciaController.anexos,
					caracterizacao: {id: inconsistenciaController.idCaracterizacao},
					geometriaAtividade: {id: idGeometriaAtividade},
					sobreposicaoCaracterizacaoAtividade: restricao.sobreposicaoCaracterizacaoAtividade || null,
					sobreposicaoCaracterizacaoEmpreendimento: restricao.sobreposicaoCaracterizacaoEmpreendimento || null,
					sobreposicaoCaracterizacaoComplexo: restricao.sobreposicaoCaracterizacaoComplexo || null
				};

			}

			inconsistenciaService.salvarInconsistencia(params)
				.then(function(response){
					mensagem.success("Inconsistência salva com sucesso!");

					var retorno = {
						inconsistencia: response.data,
						isEdicao: params.id !== undefined && params.id !== null
					};

					listaInconsistencias.push(retorno.inconsistencia);

					$uibModalInstance.close(
						retorno);

				}).catch(function(response){
					mensagem.error(response.data.texto, {referenceId: 5});
					
				});
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

	inconsistenciaController.getRestricoesComInconsistencia = function() {
		var orgaoEnable;
		var restricoes = [];

		_.forEach(dadosProjeto.restricoes, function(restricao){

			var sobreposicao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

			_.forEach(sobreposicao.tipoSobreposicao.orgaosResponsaveis, function(orgao){
				//verifica se o orgão da restrição é IPHAN ou IBAMA
				if(orgao.sigla.toUpperCase() === inconsistenciaController.orgaos.IPHAN || orgao.sigla.toUpperCase() === inconsistenciaController.orgaos.IBAMA){
					orgaoEnable = true;
				}
			});
			if(orgaoEnable){
				restricoes.push(restricao);
			}
			orgaoEnable = false;
		});

		return restricoes;
	};

	inconsistenciaController.hasRestricoes = function() {

		var inconsistenciasRestricoes = dadosProjeto.restricoes.filter(function(restricao) {

			var sobreposicao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

			return sobreposicao.tipoSobreposicao.orgaosResponsaveis.filter(
				function(orgao) {

					return orgao.sigla === 'IPHAN' || orgao.sigla === 'IBAMA';

				}
			);

		});

		var inconsistencias = analiseGeo.inconsistencias.filter(function(inconsistencia) {

			return inconsistencia.categoria !== 'PROPRIEDADE';

		});

		return inconsistencias.length < inconsistenciasRestricoes.length;

	};

	inconsistenciaController.getRestricoesSemInconsistencia = function() {
		var restricoes = [];
		var restricaoEnable = true;
		_.forEach(inconsistenciaController.getRestricoesComInconsistencia(), function(restricao) {

			var sobreposicaoRestricao = {};

			_.forEach(analiseGeo.inconsistencias, function(i){

				//verifica se uma restrição já possui inconsistência
				var sobreposicaoInconsistencia = i.sobreposicaoCaracterizacaoAtividade ? i.sobreposicaoCaracterizacaoAtividade : i.sobreposicaoCaracterizacaoEmpreendimento ? i.sobreposicaoCaracterizacaoEmpreendimento : i.sobreposicaoCaracterizacaoComplexo;
				sobreposicaoRestricao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

				if(sobreposicaoInconsistencia && (sobreposicaoInconsistencia.id === sobreposicaoRestricao.id)){
					restricaoEnable = false;
				}
			});

			if(restricaoEnable){
				restricao.sobreposicao = sobreposicaoRestricao;
				restricoes.push(restricao);
			}

			restricaoEnable = true;

		});

		return restricoes;
		
	};

	inconsistenciaController.getItemRestricao = function(idSobreposicao) {
		var itemRestricao = {};

		_.forEach(inconsistenciaController.getRestricoesComInconsistencia(), function(restricao) {
			
			var sobreposicaoRestricao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

			if (idSobreposicao === sobreposicaoRestricao.id) {
				itemRestricao = restricao.item;
			}
		});
		return itemRestricao;
	};

};

exports.controllers.InconsistenciaController = InconsistenciaController;