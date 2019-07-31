package models;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "analise", name = "pessoa")
public class Pessoa extends GenericModel  {

	public static final String SEQ = "analise.usuario_analise_id_seq";

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	@Column(name="id_pessoa")
	public Long id;

	@Column(name="nome")
	@Required
	public String nome;

}
