package models.licenciamento;

import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "empreendimento")
@FilterDefs(value = {
		@FilterDef( name = "empreendimentoAtivo", parameters = @ParamDef(name = "ativo", type = "boolean"), defaultCondition = "ativo = FALSE" )
})
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
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(schema = "licenciamento", name = "endereco_empreendimento",
			joinColumns = @JoinColumn(name = "id_empreendimento"),
			inverseJoinColumns = @JoinColumn(name = "id_endereco"))
	public List<Endereco> enderecos;
	
	@Column(name="tipo_localizacao")
	@Enumerated(EnumType.ORDINAL)
	public TipoLocalizacao localizacao;
	
	@Column(name = "the_geom", columnDefinition = "Geometry")
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

	@Column(name = "houve_alteracoes")
	public boolean houveAlteracoes;
	
	@Transient
	public boolean possuiCaracterizacoes;

	@Column(name = "possui_anexo")
	public boolean possui_anexo;
	
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

	public static Empreendimento buscaEmpreendimentoByCpfCnpj(String cpfCnpj) {
		String select = "";
		select =
				" SELECT emp FROM " + Empreendimento.class.getCanonicalName() + " emp " +
						" INNER JOIN emp.pessoa p ";

		select += cpfCnpj.length() > 11 ?
				" INNER JOIN PessoaJuridica pj ON p.id = pj.id WHERE pj.cnpj = :cpfCnpj" :
				" INNER JOIN PessoaFisica pf ON p.id = pf.id WHERE pf.cpf = :cpfCnpj" ;

		return Empreendimento.find(select)
				.setParameter("cpfCnpj", cpfCnpj)
				.first();
	}

}
