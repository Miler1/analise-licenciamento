package models.portalSeguranca;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.EntradaUnica.Usuario;
import play.Play;
import play.data.validation.MaxSize;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

@Entity
@Table(schema = "portal_seguranca", name = "usuario")
public class UsuarioLicenciamento extends GenericModel  {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_usuario")
	@SequenceGenerator(name = "sq_usuario", sequenceName = "portal_seguranca.sq_usuario", allocationSize = 1)
	public Long id;

	@MaxSize(value = 20)
	public String login;

	@OneToMany(mappedBy="usuario", orphanRemoval = true)
	public List<PerfilUsuario> perfisUsuario;

	@Transient
	public Setor setorSelecionado;

	public static transient ExecutorService executorService = new ScheduledThreadPoolExecutor(Integer.valueOf(Play.configuration.getProperty("usuario.threads", "3")));

	@Transient
	private List<Integer> permittedActionsIds;

	@Transient
	public Usuario usuarioEntradaUnica;

	@Transient
	public Perfil perfilSelecionado;
	
	public static List<UsuarioLicenciamento> getUsuariosByPerfil(Integer idPerfil) {
		
		return UsuarioLicenciamento.find("SELECT DISTINCT(u) FROM Usuario u JOIN u.perfisUsuario pu JOIN pu.perfil p WHERE p.id = ?", idPerfil).fetch();
	}
	
	public static List<UsuarioLicenciamento> getUsuariosByPerfilSetor(Integer idPerfil, Integer idSetor) {
		
		return UsuarioLicenciamento.find("SELECT DISTINCT(u) FROM Usuario u JOIN u.perfisUsuario pu JOIN pu.perfil p JOIN pu.setor s where p.id = ? and s.id = ? ",idPerfil, idSetor).fetch();
	}
	
	public static List<UsuarioLicenciamento> getUsuariosByPerfilSetores(Integer idPerfil, List<Integer> idsSetores) {
		
		if(idsSetores.size() == 0) {
			return null;
		}
		
		Query query = JPA.em().createQuery("SELECT DISTINCT(u) FROM Usuario u JOIN u.perfisUsuario pu JOIN pu.perfil p JOIN pu.setor s where p.id = :idPerfil and s.id IN (:idsSetores)");
		
		query.setParameter("idPerfil", idPerfil);
		query.setParameter("idsSetores", idsSetores);
		
		return query.getResultList();
	}
	
	public boolean hasPerfil(Integer idPerfil){
		
		for (PerfilUsuario perfilUsuario : perfisUsuario) {
			
			if (perfilUsuario.perfil.id.equals(idPerfil)) {
				return true;
			}
		}
		
		return false;
	}

	public static UsuarioLicenciamento findById(Object id, Perfil perfilSelecionado, Setor setorSelecionado) {

		UsuarioLicenciamento usuario = UsuarioLicenciamento.findById(id);
		usuario.setorSelecionado = setorSelecionado;
		usuario.perfilSelecionado = perfilSelecionado;

		return usuario;
	}
}
