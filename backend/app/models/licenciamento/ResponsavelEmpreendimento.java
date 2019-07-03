package models.licenciamento;

import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.GenericModel;
import utils.PessoaUtils;
import utils.PessoaUtils.IPessoaFisica;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "responsavel_empreendimento")
public class ResponsavelEmpreendimento extends GenericModel implements PessoaUtils.IPessoa {

	private static final String SEQ = "licenciamento.responsavel_empreendimento_id_seq";
	
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
	
	@Column(name="tipo_responsavel")
	@Enumerated(EnumType.ORDINAL)
	public TipoResponsavelEmpreendimento tipo;
	
	@ManyToOne
	@JoinColumn(name="id_orgao_classe", referencedColumnName="id")
	public OrgaoClasse orgaoClasse;
	
	@Valid
	@Required
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_contato", referencedColumnName = "id")
	public Contato contato;
	
	@Valid
	@Required
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_endereco_correspondencia", referencedColumnName = "id")
	public Endereco enderecoCorrespondencia;
	
	@Column(name = "numero_registro")
	public String numeroRegistro;
	
	@Column(name = "numero_credenciamento")
	public String numeroCredenciamento;


	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "documento_responsavel_empreendimento",
			joinColumns = @JoinColumn(name = "id_responsavel_empreendimento"),
			inverseJoinColumns = @JoinColumn(name = "id_documento"))
	public List<DocumentoLicenciamento> documentosRepresentacao;


	@Override
	public Pessoa getPessoa() {
		return this.pessoa;
	}
}
