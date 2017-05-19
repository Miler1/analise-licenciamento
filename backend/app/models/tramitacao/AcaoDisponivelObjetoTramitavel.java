package models.tramitacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

// View que lista quais ações estão disponiveis para determinado objeto tramitavel

 @Entity
 @Table(schema="tramitacao", name = "VW_ACAO_DISPONIVEL_OBJETO")
public class AcaoDisponivelObjetoTramitavel extends GenericModel {

 	@Id
 	@Column(name = "ID_ACAO")
	public Long idAcao;
	
 	@Column(name = "TX_DESCRICAO")
	public String nomeAcao;
	
 	@Column(name = "FL_TRAMITAVEL")
	public Boolean tramitavel;
	
 	@Id
 	@Column(name = "ID_OBJETO_TRAMITAVEL")
	public Long idObjetoTramitavel;
 	
 	public AcaoDisponivelObjetoTramitavel() {
 	}
 	
 	public AcaoDisponivelObjetoTramitavel(Long idAcao, Long idObjetoTramitavel) {
 		this.idAcao = idAcao;
 		this.idObjetoTramitavel = idObjetoTramitavel;
 	}
	
	public boolean isTramitavel() {
		return tramitavel != null && tramitavel;
	}
}
