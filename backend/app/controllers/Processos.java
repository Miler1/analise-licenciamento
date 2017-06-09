package controllers;

import java.util.List;

import builders.ProcessoBuilder.FiltroProcesso;
import models.AnaliseJuridica;
import models.Processo;
import models.licenciamento.Caracterizacao;
import models.tramitacao.HistoricoTramitacao;
import security.Acao;
import security.Auth;
import serializers.AnaliseJuridicaSerializer;
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

	public void getInfoProcesso(Long id) {
		
		Processo processo = Processo.findById(id);
		
		renderJSON(processo, ProcessoSerializer.getInfo);
		
	}

	public static void findAnaliseJuridica(Long idProcesso) {
		
		Processo processo = Processo.findById(idProcesso);
		
		AnaliseJuridica analise = AnaliseJuridica.findByProcesso(processo);
		
		renderJSON(analise, AnaliseJuridicaSerializer.findInfo);
	
	}
}
