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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
@Table(schema = "portal_seguranca", name = "permissao")
public class Permissao extends GenericModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_permissao")
	@SequenceGenerator(name = "sq_permissao", sequenceName = "portal_seguranca.sq_permissao", allocationSize = 1)
	public Integer id;

	@Required(message="permissoes.validacao.nome.req")
	public String nome;

	@MaxSize(value = 255, message = "permissoes.validacao.codigo.tamanho")
	@Required(message="permissoes.validacao.codigo.req")
	public String codigo;

	@Column(name = "data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_modulo", referencedColumnName = "id")
	@Required(message = "permissoes.validacao.modulo.req")
	public Modulo modulo;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(schema = "portal_seguranca", name = "permissao_perfil", 
		joinColumns = @JoinColumn(name = "id_permissao", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "id_perfil", referencedColumnName = "id"))
	public List<Perfil> perfis;

	@Transient
	private List<Integer> permittedActionsIds;
}
