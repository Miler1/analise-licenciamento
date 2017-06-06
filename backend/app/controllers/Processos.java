package controllers;

import java.util.List;

import builders.ProcessoBuilder.FiltroProcesso;
import models.Processo;
import models.licenciamento.Caracterizacao;
import models.tramitacao.HistoricoTramitacao;
import security.Acao;
import security.Auth;

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
	
	public class ProcessoVO {
		
		private Processo processo;
		private List<HistoricoTramitacao> historicoTramitacao;
		
	}
	
	public void getInfoProcesso(Long id) {
		
		Processo processo = Processo.findById(id);
		List<HistoricoTramitacao> historicoTramitacao = HistoricoTramitacao.getByObjetoTramitavel(processo.idObjetoTramitavel);
		
		ProcessoVO processoVO = new ProcessoVO();
		processoVO.processo = processo;
		processoVO.historicoTramitacao = historicoTramitacao;
		
		renderJSON(processoVO);
		
	}
}
