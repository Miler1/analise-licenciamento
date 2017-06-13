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
	ANALISE_JURIDICA_CADASTRADA_SUCESSO,
	ANALISE_JURIDICA_CONCLUIDA_SUCESSO,
	ANALISE_JURIDICA_PARECER_NAO_PREENCHIDO,
	ANALISE_JURIDICA_SEM_RESULTADO,
	ANALISE_JURIDICA_DOCUMENTO_NAO_AVALIADO,
	TODOS_OS_DOCUMENTOS_VALIDOS,
	ANALISE_JURIDICA_CONCLUIDA,
	ANALISE_JURIDICA_INICIADA_SUCESSO,
	ANALISE_JURIDICA_SEM_RESULTADO_VALIDACAO,
	ANALISE_JURIDICA_CONSULTOR_NAO_INFORMADO;
	
	public String getTexto(Object ... args) {
		
		return Messages.get(name(), args);
	}

}
