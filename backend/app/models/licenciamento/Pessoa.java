package models.licenciamento;

import exceptions.AppException;
import play.data.validation.Valid;
import play.db.jpa.GenericModel;
import utils.Configuracoes;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa extends GenericModel {
	
	private static final String SEQ = "licenciamento.pessoa_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_contato", referencedColumnName = "id")
	public Contato contato;
	
	public Boolean ativo;
	
	@Column(name = "data_cadastro")
	public Date dataCadastro;
	
	@Valid
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(schema = "licenciamento", name = "endereco_pessoa",
			joinColumns = @JoinColumn(name = "id_pessoa"),
			inverseJoinColumns = @JoinColumn(name = "id_endereco"))
	public List<Endereco> enderecos;
	
	// Atributo utilizado para deserialização pelo gson.
	@Transient
	public final String type = this.getClass().getSimpleName();


	public String getCpfCnpj() {

		if (this.isPessoaFisica())
			return ((PessoaFisica)this).cpf;
		else
			return ((PessoaJuridica)this).cnpj;
	}

	public String getNomeRazaoSocial() {

		if (this.isPessoaFisica()) {
			return ((PessoaFisica)this).nome;
		}

		return ((PessoaJuridica)this).razaoSocial;
	}

	public void setCpfCnpj(String cpfCnpj) {

		if (this.isPessoaFisica()) {
			((PessoaFisica)this).cpf = cpfCnpj;
		} else {
			((PessoaJuridica)this).cnpj = cpfCnpj;
		}
	}

	public boolean isPessoaFisica() {

		return (this instanceof PessoaFisica);
	}

}
