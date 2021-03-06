package models.manejoDigital;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.List;

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
	public TipologiaManejo tipologiaManejo;

	public static List<AtividadeManejo> findByTipologia(Long idTipologia) {

		return find("tipologiaManejo.id", idTipologia).fetch();
	}
}
