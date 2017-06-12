package controllers;

import java.util.List;

import models.AnaliseDocumento;
import models.AnaliseJuridica;
import serializers.AnaliseDocumentoSerializer;
import serializers.AnaliseJuridicaSerializer;
import utils.Mensagem;

public class AnalisesJuridicas extends InternalController {
	
	public static void findDocumentosAnalisados(Long idAnaliseJuridica) {
		
		List<AnaliseDocumento> documentos = AnaliseJuridica.findDocumentos(idAnaliseJuridica);
		
		renderJSON(documentos, AnaliseDocumentoSerializer.analiseJuridica);
	}

	public static void findById(Long idAnaliseJuridica) {
		
		AnaliseJuridica analise = AnaliseJuridica.findById(idAnaliseJuridica);
		
		renderJSON(analise, AnaliseJuridicaSerializer.findInfo);
	
	}

	public static void alterar(AnaliseJuridica analise) {
		
		AnaliseJuridica analiseAAlterar = AnaliseJuridica.findById(analise.id);
				
		analiseAAlterar.update(analise);
				
		renderMensagem(Mensagem.ANALISE_JURIDICA_CADASTRADA_SUCESSO);	
	}
	
	public static void concluir(AnaliseJuridica analise) {
		
		analise.finalizar();
		
		renderMensagem(Mensagem.ANALISE_JURIDICA_CONCLUIDA_SUCESSO);				
		
	}

}
