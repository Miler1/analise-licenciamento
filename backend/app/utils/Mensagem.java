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
	DOCUMENTO_NAO_ENCONTRADO,
	ANALISE_CADASTRADA_SUCESSO,
	ANALISE_CONCLUIDA_SUCESSO,
	ANALISE_PARECER_NAO_PREENCHIDO,
	ANALISE_SEM_RESULTADO,
	ANALISE_DOCUMENTO_NAO_AVALIADO,
	TODOS_OS_DOCUMENTOS_VALIDOS,
	ANALISE_JURIDICA_CONCLUIDA,
	ANALISE_JURIDICA_INICIADA_SUCESSO,
	ANALISE_SEM_RESULTADO_VALIDACAO,
	ANALISE_JURIDICA_CONSULTOR_NAO_INFORMADO,
	VALIDACAO_PARECER_JURIDICO_CONCLUIDA_SUCESSO,
	ANALISE_SEM_PARECER_VALIDACAO,
	
	ANALISE_TECNICA_INICIADA_SUCESSO,
	ANALISE_TECNICA_VALIDADE_LICENCA_MAIOR_PERMITIDO,
	ANALISE_TECNICA_CONDICIONANTE_PRAZO_MAIOR_PERMITIDO,
	ANALISE_TECNICA_LICENCA_SEM_VALIDACAO,
	VALIDACAO_PARECER_TECNICO_CONCLUIDA_SUCESSO,
	ANALISE_TECNICA_USUARIO_SEM_SETOR,
	ANALISE_TECNICA_GERENTE_ANALISTA_NAO_INFORMADO,
	
	ANALISTA_DIFERENTE_DE_ANALISTA_TECNICO,
	ANALISTA_VINCULADO_SUCESSO,
	ANALISTA_JUSTIFICATIVA_COORDENADOR_OBRIGATORIA,
	
	GERENTE_DIFERENTE_DE_GERENTE_TECNICO,
	GERENTE_VINCULADO_SUCESSO,
	
	SETOR_OBRIGATORIO_ANALISE,
	SETOR_NAO_ENCONTRADO;
	
	public String getTexto(Object ... args) {
		
		return Messages.get(name(), args);
	}

}
