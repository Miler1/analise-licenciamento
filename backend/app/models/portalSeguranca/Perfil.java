package models.portalSeguranca;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.groovy.runtime.dgmimpl.arrays.IntegerArrayGetAtMetaMethod;

import play.Play;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
@Table(schema = "portal_seguranca", name = "perfil")
public class Perfil extends GenericModel {

	public static final int NOME_TAMANHO_MAXIMO = 100;
	public static final String CAMINHO_TEMP = Play.applicationPath + "/tmp/";
	public static final String PATH_PERFIS = Play.configuration.getProperty("application.diretorioGravarImagens.perfis");
	public static final Integer CONSULTOR_JURIDICO = 5;
	private static final Integer ID_EXTERNO = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_perfil")
	@SequenceGenerator(name = "sq_perfil", sequenceName = "portal_seguranca.sq_perfil", allocationSize = 1)
	public Integer id;

	@MaxSize(value = NOME_TAMANHO_MAXIMO, message = "perfis.validacao.nome.max")
	@Required(message = "perfis.validacao.nome.req")
	public String nome;

	public String avatar;

	@Column(name = "data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(schema = "portal_seguranca", name = "permissao_perfil", 
		joinColumns = @JoinColumn(name = "id_perfil", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "id_permissao", referencedColumnName = "id"))
	public List<Permissao> permissoes;

	@Transient
	private List<Integer> permittedActionsIds;
	
}