package controllers;

import models.Analise;
import serializers.AnalisesSerializer;

public class Analises extends InternalController {
	
	public static void findById(Long id) {
		
		renderJSON(Analise.findById(id), AnalisesSerializer.findInfo);
	}

}
