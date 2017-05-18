package models.licenciamento;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import exceptions.ValidacaoException;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.GenericModel;
import play.db.jpa.JPABase;
import utils.ListUtil;
import utils.Mensagem;
import utils.ModelUtil;
import utils.PessoaUtils.IPessoa;
import utils.PessoaUtils.IPessoaFisica;
import utils.validacao.Validacao;

@Entity
@Table(schema = "licenciamento", name = "responsavel_empreendimento")
public class ResponsavelEmpreendimento extends GenericModel implements IPessoaFisica {

	private static final String SEQ = "licenciamento.responsavel_empreendimento_id_seq";
	
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
	public PessoaFisica getPessoa() {
		return this.pessoa;
	}
}
