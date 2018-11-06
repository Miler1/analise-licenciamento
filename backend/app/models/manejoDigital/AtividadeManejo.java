package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = "analise", name = "atividade_manejo")
public class AtividadeManejo extends GenericModel {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="analise.atividade_manejo_id_seq")
	@SequenceGenerator(name="analise.atividade_manejo_id_seq", sequenceName="analise.atividade_manejo_id_seq", allocationSize=1)
	public Long id;

	@Required
	@Column
	public String nome;

	@Required
	@Column
	public String codigo;

	@Required
	@ManyToOne
	@JoinColumn(name = "id_tipologia")
	public AtividadeManejo atividadeManejo;
}
