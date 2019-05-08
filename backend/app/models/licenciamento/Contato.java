package models.licenciamento;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "contato")
public class Contato extends GenericModel {

	private static final String SEQ = "licenciamento.contato_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@Required
	public String email;
	
	public String telefone;
	
	public String celular;
}
