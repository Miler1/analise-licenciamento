package utils;

import play.i18n.Messages;

public enum Mensagem {

	ERRO_PADRAO,
	PERMISSAO_NEGADA,
	CPF_CNPJ_INVALIDO_NAO_INFORMADO,
	ERRO_VALIDACAO,
	ENDERECOS_EXTRAS_NAO_PERMITIDOS,
	PARAMETRO_NAO_INFORMADO,
	EXCEPTION_TRAMITACAO;
	
	public String getTexto(Object ... args) {
		
		return Messages.get(name(), args);
	}

}
