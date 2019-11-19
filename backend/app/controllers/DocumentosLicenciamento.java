package controllers;

import models.licenciamento.DocumentoLicenciamento;
import security.Acao;
import utils.Mensagem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DocumentosLicenciamento extends InternalController {

	public static void download(Long idDocumento) throws FileNotFoundException {
		
		verificarPermissao(Acao.LISTAR_PROCESSO, Acao.CONSULTAR_PROCESSO, Acao.INICIAR_PARECER_JURIDICO,
				Acao.INICIAR_PARECER_TECNICO, Acao.VALIDAR_PARECER_JURIDICO, Acao.VALIDAR_PARECER_TECNICO,
				Acao.VINCULAR_PROCESSO_JURIDICO, Acao.VINCULAR_PROCESSO_TECNICO);
		
		returnIfNull(idDocumento, "Long");
		
		DocumentoLicenciamento documento = DocumentoLicenciamento.findById(idDocumento);
			
		if(documento != null) {
			File file = documento.getFile();
			renderBinary(new FileInputStream(file), file.getName(), true);
		}
		
		renderMensagem(Mensagem.DOCUMENTO_NAO_ENCONTRADO);

	}

}
