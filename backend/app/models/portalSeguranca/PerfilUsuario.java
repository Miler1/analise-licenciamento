package models.portalSeguranca;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Identificavel;

@Entity
@Table(schema="portal_seguranca", name="perfil_usuario")
public class PerfilUsuario extends GenericModel {
	
	private static final String SEQ = "portal_seguranca.perfil_usuario_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Integer id;
	
	@Required(message = "perfisUsuarios.validacao.usuario.req")
	@ManyToOne
	@JoinColumn(name="id_usuario", referencedColumnName="id")
	public Usuario usuario;
	
	@Required(message = "perfisUsuarios.validacao.perfil.req")
	@ManyToOne
	@JoinColumn(name="id_perfil", referencedColumnName="id")
	public Perfil perfil;
	
	@ManyToOne
	@JoinColumn(name="id_setor", referencedColumnName="id")
	public Setor setor;
}
