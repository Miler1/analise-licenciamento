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
	inconsistenciaController.idSobreposicao = null;
	inconsistenciaController.idAtividadeCaracterizacao = null;
	inconsistenciaController.dadosProjeto = dadosProjeto;
	inconsistenciaController.orgaos = app.utils.Orgao;
	inconsistenciaController.errors = {
		categoria:false,
		descricao:false,
		tipo:false,
		item:false,
		atividade: false
	};
	inconsistenciaController.inconsistenciaEdicao = inconsistencia;
	inconsistenciaController.tiposInconsistencia = app.utils.Inconsistencia;
	inconsistenciaController.categoriaInconsistencia = categoriaInconsistencia;
	inconsistenciaController.habilitaExcluir = inconsistencia !== null && inconsistencia.id !== null;
	inconsistenciaController.isEdicao = inconsistencia !== null && inconsistencia.id !== null;

	if(dadosProjeto.categoria === inconsistenciaController.tiposInconsistencia.COMPLEXO) {

		inconsistenciaController.categoriaProjeto = 'Complexo';

	} else if(dadosProjeto.categoria === inconsistenciaController.tiposInconsistencia.PROPRIEDADE) {

		inconsistenciaController.categoriaProjeto = 'Empreendimento/Atividade';

	} else {

		inconsistenciaController.categoriaProjeto = 'Atividade';

	}

	if(inconsistencia) {
		inconsistenciaController.descricaoInconsistencia = inconsistencia.descricaoInconsistencia;
		inconsistenciaController.tipoInconsistencia = inconsistencia.tipoInconsistencia;
		inconsistenciaController.anexos = inconsistencia.anexos;
		inconsistenciaController.id = inconsistencia.id;
		inconsistenciaController.idSobreposicao = idSobreposicao;
		inconsistenciaController.idAtividadeCaracterizacao = inconsistencia.atividadeCaracterizacao ? inconsistencia.atividadeCaracterizacao.id : null;
	}

	inconsistenciaController.TAMANHO_MAXIMO_ARQUIVO_MB = tamanhoMaximoArquivoAnaliseMB;

	inconsistenciaController.fechar = function () {
		$uibModalInstance.dismiss('cancel');
	};

	inconsistenciaController.upload = function(file, invalidFile) {

		if(file) {

				uploadService.save(file)
						.then(function(response) {

							var nomeDoArquivo = file.name;

							var quantidadeDocumentosComMesmoNome = inconsistenciaController.anexos.filter(function(documento) {
								return documento.nomeDoArquivo.includes(file.name.split("\.")[0]);
							}).length;
		
							if(quantidadeDocumentosComMesmoNome > 0) {
								nomeDoArquivo = file.name.split("\.")[0] + " (" + quantidadeDocumentosComMesmoNome + ")." + file.name.split("\.")[1];
							}

							inconsistenciaController.anexos.push({

										key: response.data,
										nomeDoArquivo: nomeDoArquivo,
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

	function hasErrors() {

		var hasError = false;

		var restricao = _.find(dadosProjeto.restricoes, function(restricao) {

			var sobreposicao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;
			
			return sobreposicao.id === inconsistenciaController.idSobreposicao;

		});

		restricao = restricao || {};

		if(!inconsistenciaController.categoriaInconsistencia || inconsistenciaController.categoriaInconsistencia === ''){
			
			inconsistenciaController.errors.categoria = true;
			hasError = true;

		} else {

			inconsistenciaController.errors.categoria = false;

		}

		if(inconsistenciaController.categoriaInconsistencia === inconsistenciaController.tiposInconsistencia.ATIVIDADE && (!inconsistenciaController.idAtividadeCaracterizacao || inconsistenciaController.idAtividadeCaracterizacao === null)){
			
			inconsistenciaController.errors.atividade = true;
			hasError = true;

		} else {

			inconsistenciaController.errors.atividade = false;

		}

		if(inconsistenciaController.categoriaInconsistencia === inconsistenciaController.tiposInconsistencia.RESTRICAO && (!restricao.item || restricao.item === null)){
			
			inconsistenciaController.errors.item = true;
			hasError = true;

		} else {

			inconsistenciaController.errors.item = false;

		}

		if(!inconsistenciaController.descricaoInconsistencia || inconsistenciaController.descricaoInconsistencia === ''){
			
			inconsistenciaController.errors.descricao = true;
			hasError = true;

		} else {

			inconsistenciaController.errors.descricao = false;

		}

		if(!inconsistenciaController.tipoInconsistencia || inconsistenciaController.tipoInconsistencia === ''){
			
			inconsistenciaController.errors.tipo = true;
			hasError = true;

		} else {

			inconsistenciaController.errors.tipo = false;

		}

		if(hasError) {
			return true;
		}

	}

	inconsistenciaController.concluir = function() {

		inconsistenciaController.idCaracterizacao = idCaracterizacao ? idCaracterizacao : inconsistenciaController.buscarIdCaracterizacaoPorRestricao();
		inconsistenciaController.idSobreposicao = idSobreposicao ? idSobreposicao : inconsistenciaController.idSobreposicao;

		hasErrors();

		var restricao = _.find(dadosProjeto.restricoes, function(restricao) {

			var sobreposicao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;
			
			return sobreposicao.id === inconsistenciaController.idSobreposicao;

		});

		restricao = restricao || {};

		var inconsistencia = {
			id: inconsistenciaController.inconsistenciaEdicao ? inconsistenciaController.inconsistenciaEdicao.id : null,
			analiseGeo: {id: analiseGeo.id},
			tipoInconsistencia: inconsistenciaController.tipoInconsistencia,
			descricaoInconsistencia: inconsistenciaController.descricaoInconsistencia,
			categoria: categoriaInconsistencia ? categoriaInconsistencia : inconsistenciaController.categoriaInconsistencia,
			anexos: inconsistenciaController.anexos,
			caracterizacao: {id: inconsistenciaController.idCaracterizacao},
			atividadeCaracterizacao: { id: inconsistenciaController.idAtividadeCaracterizacao },
			geometriaAtividade: {id: idGeometriaAtividade},
			sobreposicaoCaracterizacaoAtividade: restricao.sobreposicaoCaracterizacaoAtividade || null,
			sobreposicaoCaracterizacaoEmpreendimento: restricao.sobreposicaoCaracterizacaoEmpreendimento || null,
			sobreposicaoCaracterizacaoComplexo: restricao.sobreposicaoCaracterizacaoComplexo || null
		};

		inconsistenciaService.salvarInconsistenciaGeo(inconsistencia)
			.then(function(response){

				mensagem.success("Inconsistência salva com sucesso!");

				var retorno = {
					inconsistencia: response.data,
					isEdicao: inconsistencia.id !== undefined && inconsistencia.id !== null
				};

				var sobreposicao = retorno.inconsistencia.sobreposicaoCaracterizacaoAtividade ? retorno.inconsistencia.sobreposicaoCaracterizacaoAtividade : retorno.inconsistencia.sobreposicaoCaracterizacaoEmpreendimento ? retorno.inconsistencia.sobreposicaoCaracterizacaoEmpreendimento : retorno.inconsistencia.sobreposicaoCaracterizacaoComplexo;
				var inconsistenciaValida = false;

				if(sobreposicao) {

					inconsistenciaValida = sobreposicao.tipoSobreposicao.orgaosResponsaveis.every(function(orgao) {

						return orgao.sigla.toUpperCase() === inconsistenciaController.orgaos.IPHAN || orgao.sigla.toUpperCase() === inconsistenciaController.orgaos.IBAMA;

					});
					
				}

				if(listaInconsistencias && inconsistenciaValida) {						
					listaInconsistencias.push(retorno.inconsistencia);
				}

				$uibModalInstance.close(
					retorno);

			}).catch(function(response){
				mensagem.error(response.data.texto, { referenceId: 5 });
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

				inconsistenciaService.excluirInconsistenciaGeo(inconsistencia.id)
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

			return sobreposicao.tipoSobreposicao.orgaosResponsaveis.every(
				function(orgao) {
					return orgao.sigla === inconsistenciaController.orgaos.IPHAN || orgao.sigla === inconsistenciaController.orgaos.IBAMA;
				}
			);

		});

		var inconsistencias = analiseGeo.inconsistencias.filter(function(inconsistencia) {

			return inconsistencia.categoria !== inconsistenciaController.tiposInconsistencia.PROPRIEDADE && 
					inconsistencia.categoria !== inconsistenciaController.tiposInconsistencia.ATIVIDADE && 
					inconsistencia.categoria !== inconsistenciaController.tiposInconsistencia.COMPLEXO;

		});

		return inconsistencias.length < inconsistenciasRestricoes.length;

	};

	inconsistenciaController.verificaInconsistenciaCategoria = function(categoria) {

		if(categoria === inconsistenciaController.tiposInconsistencia.ATIVIDADE && !inconsistenciaController.isEdicao) {

			return dadosProjeto.atividades.length > analiseGeo.inconsistencias.filter(function(inconsistencia) { 
				return inconsistencia.categoria === inconsistenciaController.tiposInconsistencia.ATIVIDADE;
			}).length;

		}

		return true;

	};

	inconsistenciaController.getAtividadesSemInconsistencia = function() {

		return _.filter(inconsistenciaController.dadosProjeto.atividades, function(atividade) {

			return analiseGeo.inconsistencias.length === 0 || _.some(analiseGeo.inconsistencias, function(inconsistencia) {
				return inconsistencia.atividadeCaracterizacao && inconsistencia.atividadeCaracterizacao.id !== atividade.atividadeCaracterizacao.id;
			});

		});

	};

	inconsistenciaController.getRestricoesSemInconsistencia = function() {
		
		var restricoes = [];
		var restricaoEnable = true;

		_.forEach(inconsistenciaController.getRestricoesComInconsistencia(), function(restricao) {

			var sobreposicaoRestricao = restricao.sobreposicaoCaracterizacaoAtividade ? restricao.sobreposicaoCaracterizacaoAtividade : restricao.sobreposicaoCaracterizacaoEmpreendimento ? restricao.sobreposicaoCaracterizacaoEmpreendimento : restricao.sobreposicaoCaracterizacaoComplexo;

			_.forEach(analiseGeo.inconsistencias, function(i){

				//verifica se uma restrição já possui inconsistência
				var sobreposicaoInconsistencia = i.sobreposicaoCaracterizacaoAtividade ? i.sobreposicaoCaracterizacaoAtividade : i.sobreposicaoCaracterizacaoEmpreendimento ? i.sobreposicaoCaracterizacaoEmpreendimento : i.sobreposicaoCaracterizacaoComplexo;

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

	inconsistenciaController.getItemAtividade = function(idAtividadeCaracterizacao) {

		var atividade = _.find(analiseGeo.inconsistencias, function(inconsistencia) {
			return inconsistencia.atividadeCaracterizacao && inconsistencia.atividadeCaracterizacao.id === idAtividadeCaracterizacao;
		});

		return atividade.atividadeCaracterizacao.atividade.nome;

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