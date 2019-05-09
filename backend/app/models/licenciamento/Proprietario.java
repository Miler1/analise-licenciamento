package models.licenciamento;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.PessoaUtils.IPessoaFisica;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "proprietario")
public class Proprietario extends GenericModel implements IPessoaFisica {

	private static final String SEQ = "licenciamento.proprietario_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@Required
	@OneToOne
	@JoinColumn(name = "id_pessoa_fisica", referencedColumnName = "id_pessoa")
	public PessoaFisica pessoa;
	
	@ManyToOne
	@JoinColumn(name = "id_empreendimento", referencedColumnName = "id", nullable = false)
	public Empreendimento empreendimento;	
	
	@Override
	public PessoaFisica getPessoa() {
		
		return this.pessoa;
	}
	
	
	
}
