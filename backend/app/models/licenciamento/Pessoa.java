package models.licenciamento;

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
}
