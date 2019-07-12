package models.licenciamento;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.PessoaUtils.IPessoa;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "proprietario")
public class Proprietario extends GenericModel implements IPessoa {

	private static final String SEQ = "licenciamento.proprietario_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@Required
	@OneToOne
	@JoinColumn(name = "id_pessoa", referencedColumnName = "id")
	public Pessoa pessoa;
	
	@ManyToOne
	@JoinColumn(name = "id_empreendimento", referencedColumnName = "id", nullable = false)
	public Empreendimento empreendimento;	
	
	@Override
	public Pessoa getPessoa() {

		return this.pessoa;
	}
	
	
	
}
