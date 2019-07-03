package models.licenciamento;

import play.db.jpa.GenericModel;
import utils.Identificavel;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "resposta")
public class Resposta extends GenericModel implements Identificavel {

	@Id
	public Long id;

	public String texto;

	public String codigo;

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