package controllers;

import java.util.List;

import builders.ProcessoBuilder.FiltroProcesso;
import models.Processo;

public class Processos extends GenericController {

	public void listWithFilter(FiltroProcesso filtro){
		
		List processosList = Processo.list(filtro);
		
		renderJSON(processosList);
	}
	
	public void countWithFilter(FiltroProcesso filtro){
		
		 renderJSON(Processo.count(filtro));
	}
}
