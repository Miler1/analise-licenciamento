package controllers;

import java.util.List;

import builders.ProcessoBuilder.FiltroProcesso;
import models.Processo;
import security.Acao;

public class Processos extends InternalController {

	public void listWithFilter(FiltroProcesso filtro){
		
		verificarPermissao(Acao.LISTAR_PROCESSO_JURIDICO);
		
		List processosList = Processo.listWithFilter(filtro);
		
		renderJSON(processosList);
	}
	
	public void countWithFilter(FiltroProcesso filtro){
		
		verificarPermissao(Acao.LISTAR_PROCESSO_JURIDICO);
		
		renderJSON(Processo.countWithFilter(filtro));
	}
}
