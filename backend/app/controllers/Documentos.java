package controllers;

import models.Documento;
import security.Acao;
import utils.Mensagem;

import java.io.File;

public class Documentos extends InternalController {

   public static void download(Long id) {
	   
	   verificarPermissao(Acao.VALIDAR_PARECER_JURIDICO, Acao.VALIDAR_PARECER_TECNICO, Acao.INICIAR_PARECER_JURIDICO, Acao.INICIAR_PARECER_TECNICO);
	   
	   returnIfNull(id, "Long");
	   
	   Documento documento = Documento.findById(id);
	   
	   if(documento != null) {
		   File documentoBinary = documento.getFile();
		   renderBinary(documentoBinary, documentoBinary.getName());
	   }
		   
	   renderMensagem(Mensagem.DOCUMENTO_NAO_ENCONTRADO);
	   
   }

}
