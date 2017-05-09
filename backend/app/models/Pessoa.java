package models;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import exceptions.ValidacaoException;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.GenericModel;
import utils.ListUtil;
import utils.Mensagem;
import utils.validacao.Validacao;

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
	
	public boolean isPessoaFisica() {
		
		return (this instanceof PessoaFisica);
	}
	
	@Override
	public Pessoa save() {
		
		validar();
		
		this.ativo = true;
		this.dataCadastro = new Date();
		
		return super.save();
	}
	
	public void updateContato(Contato dados) {
		
		this.contato.update(dados);
	}
	
	public void updateEnderecos(List<Endereco> enderecosAtualizados) {
		
		if (enderecosAtualizados == null || enderecosAtualizados.isEmpty())
			return;
		
		for (Endereco enderecoAtualizado : enderecosAtualizados) {
			
			if (enderecoAtualizado.id == null) {
				
				enderecoAtualizado.save();
				this.enderecos.add(enderecoAtualizado);
				
			} else {
				
				Endereco endereco = ListUtil.getById(enderecoAtualizado.id, this.enderecos);
				
				if (endereco == null)
					throw new RuntimeException("Endereço de id " + enderecoAtualizado.id + " não encontrado.");
				
				endereco.update(enderecoAtualizado);
			}
		}
		
		validar();
		
		super.save();
	}
	
	protected void validar() {
		
		Validacao.validar(this);
		
		if (enderecos != null && enderecos.size() > 1) {
			
			int countEnderecosPrinc = Endereco.countByCorrespondencia(enderecos, false);
			int countEnderecosCorresp = Endereco.countByCorrespondencia(enderecos, true);
			
			if (countEnderecosCorresp > 1 || countEnderecosPrinc > 1)
				throw new ValidacaoException(Mensagem.ENDERECOS_EXTRAS_NAO_PERMITIDOS);
		}
	}
	
	public Endereco getEnderecoPrincipal() {
		
		return Endereco.getByCorrespondencia(enderecos, false);
	}
	
	public Endereco getEnderecoCorrespondencia() {
		
		return Endereco.getByCorrespondencia(enderecos, true);
	}

	public static Pessoa findByCpfCnpj(String cpfCnpj) {
		
		return Pessoa.find("(cpf = :cpfCnpj OR cnpj = :cpfCnpj)")
				.setParameter("cpfCnpj", cpfCnpj).first();

	}
	
	public static Pessoa findByCpf(String cpf) {
		
		return Pessoa.find("byCpf", cpf).first();		
	}
	
	public static Pessoa findByIdOuCpfCnpj(Pessoa pessoa) {
		
		if (pessoa == null)
			return null;
		
		if (pessoa.id != null)
			return Pessoa.findById(pessoa.id);
		
		if (pessoa.getCpfCnpj() != null)
			return findByCpfCnpj(pessoa.getCpfCnpj());
		
		return null;
	}
}
