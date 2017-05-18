package models.tramitacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

// View que possui os dasdos das condicao
@Entity
@Table(schema = "tramitacao", name = "VW_CONDICAO")
public class Condicao extends GenericModel {
	
	@Id
	@Column(name = "ID_CONDICAO")
	public Long idCondicao;

	@Column(name = "ID_FLUXO")
	public Long idFluxo;

	@Column(name = "ID_ETAPA")
	public Long idEtapa;

	@Column(name = "TX_ETAPA")
	public String nomeEtapa;

	@Column(name = "NM_CONDICAO")
	public String nomeCondicao;


}
