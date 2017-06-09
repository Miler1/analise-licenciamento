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
	DOCUMENTO_NAO_ENCONTRADO;
	
	public String getTexto(Object ... args) {
		
		return Messages.get(name(), args);
	}

}
