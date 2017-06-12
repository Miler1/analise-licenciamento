package controllers;

import java.util.List;

import models.AnaliseDocumento;
import models.AnaliseJuridica;
import serializers.AnaliseDocumentoSerializer;
import serializers.AnaliseJuridicaSerializer;

public class AnalisesJuridicas extends InternalController {
	
	public static void findDocumentosAnalisados(Long idAnaliseJuridica) {
		
		List<AnaliseDocumento> documentos = AnaliseJuridica.findDocumentos(idAnaliseJuridica);
		
		renderJSON(documentos, AnaliseDocumentoSerializer.analiseJuridica);
	}
	
	public static void findAnaliseJuridicaById(Long id) {
		
		AnaliseJuridica analise = AnaliseJuridica.findById(id);
		
		renderJSON(analise, AnaliseJuridicaSerializer.findInfo);		
	}

}
