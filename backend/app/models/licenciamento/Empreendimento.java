package models.licenciamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.GenericModel;

@Entity
@Table(schema = "licenciamento", name = "empreendimento")
@Filter(name="empreendimentoAtivo")
public class Empreendimento extends GenericModel {

	private static final String SEQ = "licenciamento.empreendimento_id_seq";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;
	
	@Required
	public String denominacao;
	
	@Column(name = "id_redesim")
	public Long idRedeSim;
	
	@Required
	@OneToOne
	@JoinColumn(name = "id_pessoa", referencedColumnName = "id")
	public Pessoa pessoa;
	
	@Required
	@OneToOne
	@JoinColumn(name = "id_empreendedor", referencedColumnName = "id")
	public Empreendedor empreendedor;
	
	@Valid
	@Required
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_contato", referencedColumnName = "id")
	public Contato contato;
	
	@Valid
	@Required
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_endereco", referencedColumnName = "id")
	public Endereco endereco;
	
	@Column(name="tipo_localizacao")
	@Enumerated(EnumType.ORDINAL)
	public TipoLocalizacao localizacao;
	
	@Column(name = "the_geom")
	@Type(type = "org.hibernate.spatial.GeometryType")
	public Geometry coordenadas;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "empreendimento", orphanRemoval = true)
	public ImovelEmpreendimento imovel;
	
	@Column(name = "data_cadastro")
	public Date dataCadastro;
	
	@OneToMany(mappedBy = "empreendimento", targetEntity = ResponsavelEmpreendimento.class, orphanRemoval = true)
	public List<ResponsavelEmpreendimento> responsaveis;
	
	public boolean ativo;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_municipio", referencedColumnName="id_municipio")
	public Municipio municipio;
	
	@Required
	@Column(name="tipo_esfera")
	@Enumerated(EnumType.ORDINAL)
	public Esfera jurisdicao;
	
	@OneToMany(mappedBy = "empreendimento", targetEntity = Proprietario.class, orphanRemoval=true)
	public List<Proprietario> proprietarios;
	
	@Transient
	public boolean possuiCaracterizacoes;
	
	public List<String> emailsProprietarios() {
		
		List<String> emails = new ArrayList<String>();
		for(Proprietario proprietario : this.proprietarios) {
			
			if(proprietario.pessoa.contato != null) {				
				emails.add(proprietario.pessoa.contato.email);
			}
		}
		return emails;	
	}
	
	public List<String> emailsResponsaveis() {
		
		List<String> emails = new ArrayList<String>();
		for(ResponsavelEmpreendimento responsavel : this.responsaveis) {
			emails.add(responsavel.pessoa.contato.email);
		}
		return emails;		
	}
}
