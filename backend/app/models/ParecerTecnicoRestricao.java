package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
@Table(schema="analise", name="parecer_tecnico_restricao")
public class ParecerTecnicoRestricao extends GenericModel {
	
	public static final String SEQ = "analise.parecer_tecnico_restricao_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_analise_tecnica")
	public AnaliseTecnica analiseTecnica;
	
	@Required
	@Column(name="codigo_camada")
	public String codigoCamada;
	
	@Required
	public String parecer;
	
	public List<ParecerTecnicoRestricao> getByIdAnaliseTecnica(Long idAnaliseTecnica) {
		
		return ParecerTecnicoRestricao.find("byAnaliseTecnica", idAnaliseTecnica).fetch();
		
	}
}
