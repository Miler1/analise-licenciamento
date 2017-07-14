package controllers;

import java.io.File;

import models.Documento;
import models.licenciamento.DocumentoLicenciamento;
import security.Acao;
import utils.Mensagem;

public class DocumentosLicenciamento extends InternalController {

	public static void download(Long idDocumento) {
		
		verificarPermissao(Acao.LISTAR_PROCESSO_JURIDICO, Acao.CONSULTAR_PROCESSO, Acao.INICIAR_PARECER_JURIDICO, 
				Acao.INICIAR_PARECER_TECNICO, Acao.VALIDAR_PARECER_JURIDICO, Acao.VALIDAR_PARECER_TECNICO,
				Acao.VINCULAR_PROCESSO_JURIDICO, Acao.VINCULAR_PROCESSO_TECNICO);
		
		returnIfNull(idDocumento, "Long");
		
		DocumentoLicenciamento documento = DocumentoLicenciamento.findById(idDocumento);
			
		if(documento != null) {
			File file = documento.getFile();
			renderBinary(file, file.getName());
		}
		
		renderMensagem(Mensagem.DOCUMENTO_NAO_ENCONTRADO);
	}

}
