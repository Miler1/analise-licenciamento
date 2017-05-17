package models.licenciamento;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import utils.PessoaUtils.IPessoaFisica;

@Entity
@Table(schema = "licenciamento", name = "representante_legal")
public class RepresentanteLegal extends GenericModel implements IPessoaFisica {

	private static final String SEQ = "licenciamento.representante_legal_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@Column(name = "data_vinculacao")
	public Date dataVinculacao = new Date();
	
	@OneToOne
	@JoinColumn(name = "id_pessoa")
	public PessoaFisica pessoa;

	@Override
	public PessoaFisica getPessoa() {
		
		return this.pessoa;
	}
	
}
