package controllers;

import java.util.List;

import models.AnaliseDocumento;
import models.AnaliseJuridica;
import serializers.AnaliseDocumentoSerializer;

public class AnalisesJuridicas extends InternalController {
	
	public static void findDocumentosAnalisados(Long idAnaliseJuridica) {
		
		List<AnaliseDocumento> documentos = AnaliseJuridica.findDocumentos(idAnaliseJuridica);
		
		renderJSON(documentos, AnaliseDocumentoSerializer.analiseJuridica);
	}

}
