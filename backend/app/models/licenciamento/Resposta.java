package models.licenciamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.GenericModel;
import utils.Identificavel;

@Entity
@Table(schema = "licenciamento", name = "resposta")
public class Resposta extends GenericModel implements Identificavel {

	@Id
	public Long id;

	public String texto;

	@Column(name="permite_licenciamento")
	public Boolean permiteLicenciamento;
	
	@ManyToOne
	@JoinColumn(name="id_pergunta", referencedColumnName="id")
	public Pergunta pergunta;

	@Override
	public Long getId() {
		
		return this.id;
	}
}