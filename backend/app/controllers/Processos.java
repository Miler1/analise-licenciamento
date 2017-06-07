package controllers;

import java.util.List;

import builders.ProcessoBuilder.FiltroProcesso;
import models.Processo;
import security.Acao;
import security.Auth;
import serializers.ProcessoSerializer;

public class Processos extends InternalController {

	public void listWithFilter(FiltroProcesso filtro){
		
		verificarPermissao(Acao.LISTAR_PROCESSO_JURIDICO);
		
		List processosList = Processo.listWithFilter(filtro, Auth.getUsuarioSessao());
		
		renderJSON(processosList);
	}
	
	public void countWithFilter(FiltroProcesso filtro){
		
		verificarPermissao(Acao.LISTAR_PROCESSO_JURIDICO);
		
		renderJSON(Processo.countWithFilter(filtro, Auth.getUsuarioSessao()));
	}
	
	public void findById(Long idProcesso) {
		
		renderJSON(Processo.findById(idProcesso), ProcessoSerializer.list);
	}
}
