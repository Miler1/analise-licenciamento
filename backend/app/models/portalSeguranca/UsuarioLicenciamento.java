package models.portalSeguranca;

import main.java.br.ufla.lemaf.beans.pessoa.Perfil;
import models.EntradaUnica.Usuario;
import play.Play;
import play.data.validation.MaxSize;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import services.ExternalUsuarioService;

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

		return  ExternalUsuarioService.findUsuariosByPerfil(codigoPerfil);
	}
	
	public static List<UsuarioLicenciamento> getUsuariosByPerfilSetor(String codigoPerfil, String siglaSetor) {


		return ExternalUsuarioService.findUsuariosByPerfilAndSetor(codigoPerfil, siglaSetor);
	}
	
	public static List<UsuarioLicenciamento> getUsuariosByPerfilSetores(String codigoPerfil, List<String> siglasSetores) {

		return ExternalUsuarioService.findUsuariosByPerfilAndSetores(codigoPerfil, siglasSetores);
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
