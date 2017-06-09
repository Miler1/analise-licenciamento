package controllers;

import java.io.File;

import models.Documento;
import models.licenciamento.DocumentoLicenciamento;

public class DocumentosLicenciamento extends InternalController {

	public static void download(Long idDocumento) {
		
		DocumentoLicenciamento documento = DocumentoLicenciamento.findById(idDocumento);

		File file = documento.getFile();
		
		renderBinary(file, file.getName());		
	
	}

}
