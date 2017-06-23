package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sun.corba.se.spi.ior.Identifiable;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Identificavel;

@Entity
@Table(schema="analise", name="condicionante")
public class Condicionante extends GenericModel implements Identificavel {
	
	public static final String SEQ = "analise.condicionante_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_licenca_analise")
	public LicencaAnalise licencaAnalise;
	
	@Required
	public String texto;
	
	@Required
	public Integer prazo;
	
	@Required
	public Integer ordem;

	@Override
	public Long getId() {
		
		return this.id;
	}

	public void update(Condicionante novaCondicionante) {
		
		this.texto = novaCondicionante.texto;
		this.prazo = novaCondicionante.prazo;
		this.ordem = novaCondicionante.ordem;
	}
}
