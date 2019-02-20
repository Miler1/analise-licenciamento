package models.portalSeguranca;

import main.java.br.ufla.lemaf.beans.pessoa.Perfil;
import models.EntradaUnica.Usuario;
import play.Play;
import play.data.validation.MaxSize;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Entity
@Table(schema = "portal_seguranca", name = "usuario")
public class UsuarioLicenciamento extends GenericModel  {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_usuario")
	@SequenceGenerator(name = "sq_usuario", sequenceName = "portal_seguranca.sq_usuario", allocationSize = 1)
	public Long id;

	@MaxSize(value = 20)
	public String login;

	public static transient ExecutorService executorService = new ScheduledThreadPoolExecutor(Integer.valueOf(Play.configuration.getProperty("usuario.threads", "3")));

	@Transient
	private List<Integer> permittedActionsIds;

	@Transient
	public Usuario usuarioEntradaUnica;

	public static List<UsuarioLicenciamento> getUsuariosByPerfil(String codigoPerfil) {

		// TODO REFACTOR
		//return UsuarioLicenciamento.find("SELECT DISTINCT(u) FROM Usuario u JOIN u.perfisUsuario pu JOIN pu.perfil p WHERE p.id = ?", idPerfil).fetch();
		return null;
	}
	
	public static List<UsuarioLicenciamento> getUsuariosByPerfilSetor(String codigoPerfil, String siglaSetor) {

		//TODO REFACTOR
		//return UsuarioLicenciamento.find("SELECT DISTINCT(u) FROM Usuario u JOIN u.perfisUsuario pu JOIN pu.perfil p JOIN pu.setor s where p.id = ? and s.id = ? ",idPerfil, siglaSetor).fetch();
		return null;
	}
	
	public static List<UsuarioLicenciamento> getUsuariosByPerfilSetores(String codigoPerfil, List<Integer> idsSetores) {

		// TODO REFACTOR
//		if(idsSetores.size() == 0) {
//			return null;
//		}
//
//		Query query = JPA.em().createQuery("SELECT DISTINCT(u) FROM Usuario u JOIN u.perfisUsuario pu JOIN pu.perfil p JOIN pu.setor s where p.id = :idPerfil and s.id IN (:idsSetores)");
//
//		query.setParameter("idPerfil", idPerfil);
//		query.setParameter("idsSetores", idsSetores);
//
//		return query.getResultList();
		return new ArrayList<>();
	}
	
	public boolean hasPerfil(String codigoPerfil){

		for (Perfil perfil : this.usuarioEntradaUnica.perfis) {

			if (perfil.codigo.equals(codigoPerfil)) {

				return true;
			}
		}

		return false;
	}
}
