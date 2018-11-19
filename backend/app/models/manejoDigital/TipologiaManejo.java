package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = "analise", name = "tipologia_manejo")
public class TipologiaManejo extends GenericModel {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.tipologia_manejo_id_seq")
	@SequenceGenerator(name="analise.tipologia_manejo_id_seq", sequenceName="analise.tipologia_manejo_id_seq", allocationSize=1)
	public Long id;

	@Required
	@Column
	public String nome;

	@Required
	@Column
	public String codigo;
}
