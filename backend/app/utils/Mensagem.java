package utils;

import play.i18n.Messages;

public enum Mensagem {

	ERRO_PADRAO,
	PERMISSAO_NEGADA,
	CPF_CNPJ_INVALIDO_NAO_INFORMADO,
	ERRO_VALIDACAO,
	ENDERECOS_EXTRAS_NAO_PERMITIDOS,
	PARAMETRO_NAO_INFORMADO,

	EXCEPTION_TRAMITACAO,
	VALIDACAO_PARECER_GEO_CONCLUIDA_SUCESSO,
	TRAMITACAO_ACAO_DISPONIVEL_OBRIGATORIA,
	TRAMITACAO_ACAO_OBRIGATORIA,
	TRAMITACAO_OBJETO_TRAMITAVEL_OBRIGATORIO,
	TRAMITACAO_FLUXO_OBRIGATORIO,
	TRAMITACAO_USUARIO_EXECUTOR_OBRIGATORIO,
	TRAMITACAO_OBJETO_OBRIGATORIO_TRAMITAR,
	TRAMITACAO_OBJETO_TRAMITAVEL_VAZIO,

	CONSULTOR_DIFERENTE_DE_CONSULTOR_JURIDICO,
	CONSULTOR_VINCULADO_SUCESSO,
	UPLOAD_ERRO,
	UPLOAD_EXTENSAO_NAO_SUPORTADA,
	UPLOAD_EXTENSAO_NAO_SUPORTADA_SHAPE,
	UPLOAD_EXTENSAO_NAO_SUPORTADA_IMOVEL,
	DOCUMENTO_NAO_ENCONTRADO,
	ANALISE_CADASTRADA_SUCESSO,
	ANALISE_CONCLUIDA_SUCESSO,
	ANALISE_PARECER_NAO_PREENCHIDO,
	ANALISE_SITUACAO_FUNDIARIA_NAO_PREENCHIDA,
	ANALISE_ANALISE_TEMPORAL_NAO_PREENCHIDA,
	ANALISE_DESPACHO_NAO_PREENCHIDO,
	ANALISE_JUSTIFICATIVA_NAO_PREENCHIDA,
	ANALISE_CONCLUSAO_NAO_PREENCHIDA,
	ANALISE_FINAL_PROCESSO_NAO_PREENCHIDA,
	ANALISE_SEM_RESULTADO,
	ANALISE_DOCUMENTO_NAO_AVALIADO,
	TODOS_OS_DOCUMENTOS_VALIDOS,
	ANALISE_JURIDICA_CONCLUIDA,
	ANALISE_GEO_CONCLUIDA,
	ANALISE_GEO_INICIADA_SUCESSO,
	ANALISE_JURIDICA_INICIADA_SUCESSO,
	ANALISE_SEM_RESULTADO_VALIDACAO,
	ANALISE_JURIDICA_CONSULTOR_NAO_INFORMADO,
	VALIDACAO_PARECER_JURIDICO_CONCLUIDA_SUCESSO,
	ANALISE_SEM_PARECER_VALIDACAO,

	ANALISE_TECNICA_INICIADA_SUCESSO,
	ANALISE_TECNICA_VALIDADE_LICENCA_MAIOR_PERMITIDO,
	ANALISE_TECNICA_CONDICIONANTE_PRAZO_MAIOR_PERMITIDO,
	ANALISE_TECNICA_LICENCA_SEM_VALIDACAO,
	ANALISE_GEO_GERENTE_ANALISTA_NAO_INFORMADO,
	ANALISE_GEO_LICENCA_SEM_VALIDACAO,
	VALIDACAO_PARECER_TECNICO_CONCLUIDA_SUCESSO,
	ANALISE_TECNICA_USUARIO_SEM_SETOR,
	ANALISE_GEO_USUARIO_SEM_SETOR,
	ANALISE_TECNICA_GERENTE_ANALISTA_NAO_INFORMADO,
    ERRO_CRIAR_DOCUMENTO_ANALISE_MANEJO,

	ANALISTA_DIFERENTE_DE_ANALISTA_TECNICO,
	ANALISTA_DIFERENTE_DE_ANALISTA_GEO,
	ANALISTA_VINCULADO_SUCESSO,
	ANALISTA_JUSTIFICATIVA_COORDENADOR_OBRIGATORIA,

	GERENTE_DIFERENTE_DE_GERENTE_TECNICO,
	GERENTE_DIFERENTE_DE_GERENTE_GEO,
	GERENTE_VINCULADO_SUCESSO,

	SETOR_OBRIGATORIO_ANALISE,
	SETOR_NAO_ENCONTRADO,

	VALIDACAO_PARECER_APROVADOR_CONCLUIDA_SUCESSO,

	ERRO_EMITIR_LICENCAS,
	ERRO_NENHUMA_LICENCA_EMITIDA,
	LICENCAS_EMITIDAS_SUCESSO,

	PRAZO_MAXIMO_SUSPENSAO_EXCEDIDO,
	LICENCA_SUSPENSA_SUCESSO,
	ERRO_ENVIAR_EMAIL,
	LICENCA_CANCELADA_OU_SUSPENSA,

	LICENCA_CANCELADA_SUCESSO,

	ERRO_CANCELAR_DLA,

	SHAPE_REMOVIDO_SUCESSO,

	ANEXO_REMOVIDO_SUCESSO,

	ANALISE_MANEJO_INICIADA_COM_SUCESSO,

	OBSERVACAO_EXCLUIDA_SUCESSO,

	ANEXO_SALVO_SUCESSO,

	ANALISE_FINALIZADA_SUCESSO,

	PROCESSO_ANALISE_USUARIO_DIFERENTE,

	PROCESSO_MANEJO_CADASTRADO_COM_SUCESSO,

	IMOVEL_NAO_ENCONTRADO,

	ANALISE_SHAPE_INICIADA_COM_SUCESSO,

	DOCUMENTO_DELETADO_COM_SUCESSO,

	PROCESSO_MANEJO_INDEFERIDO_COM_SUCESSO,

	DOCUMENTO_IMOVEL_MANEJO_TAMANHO_MAXIMO_LISTA_EXCEDIDO,

	DOCUMENTO_SHAPE_MANEJO_TAMANHO_MAXIMO_LISTA_EXCEDIDO,

	DOCUMENTO_SHAPE_MANEJO_DOCUMENTOS_OBRIGATORIOS,

	DOCUMENTO_INVALIDO,

	ERRO_PROCESSAR_ANEXO_PROCESSO_MANEJO,

	MUNICIPIO_INVALIDO_NAO_INFORMADO,

	ANALISE_TECNICA_CONCLUSAO_OBRIGATORIA,

	PERFIL_NAO_VINCULADO_AO_USUARIO,

	INCONSISTENCIA_SALVA_SUCESSO,

	INCONSISTENCIA_EXCLUIDA_SUCESSO,

	CAMPOS_OBRIGATORIOS,

	DESVINCULO_SOLICITADO_COM_SUCESSO,


	//Entrada Única
	ERRO_COMUNICACAO_ENTRADA_UNICA;

	public String getTexto(Object ... args) {

		return Messages.get(name(), args);
	}

}
