package controllers;

import java.io.File;

import models.Documento;
import models.licenciamento.DocumentoLicenciamento;
import play.mvc.Controller;
import utils.Configuracoes;
import utils.Mensagem;

public class Documentos extends InternalController {

   public static void download(Long id) {

	   returnIfNull(id, "Long");
	   
	   Documento documento = Documento.findById(id);
	   
	   if(documento != null) {
		   File documentoBinary = documento.getFile();
		   renderBinary(documentoBinary, documentoBinary.getName());
	   }
		   
	   renderMensagem(Mensagem.DOCUMENTO_NAO_ENCONTRADO);
	   
   }
   
   public static void downloadLicenciamento(Long id) {

	   returnIfNull(id, "Long");
	   
	   DocumentoLicenciamento documento = DocumentoLicenciamento.findById(id);

	   if(documento != null) {
		   File documentoBinary = documento.getFile();
		   renderBinary(documentoBinary, documentoBinary.getName());
	   }
	   
	   renderMensagem(Mensagem.DOCUMENTO_NAO_ENCONTRADO);
	   
   }

}
