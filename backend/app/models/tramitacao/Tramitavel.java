package models.tramitacao;

import java.util.List;

// Interface que deve ser implementada pelas classes que serão tramitaveis

public interface Tramitavel {

	Long getIdObjetoTramitavel();
	
	void setIdObjetoTramitavel(Long idObjetoTramitavel);
	
	List<AcaoDisponivelObjetoTramitavel> getAcoesDisponiveisTramitacao();
	
	void salvaObjetoTramitavel(); //Salva o objeto tramitavel para ser salvo o id objeto tramitavel
}
