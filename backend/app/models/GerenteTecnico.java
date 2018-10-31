package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import exceptions.PermissaoNegadaException;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.UsuarioLicenciamento;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Mensagem;

@Entity
@Table(schema="analise", name="gerente_tecnico")
public class GerenteTecnico extends GenericModel {
	
	public static final String SEQ = "analise.gerente_tecnico_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_analise_tecnica")
	public AnaliseTecnica analiseTecnica;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_usuario")
	public UsuarioLicenciamento usuario;
	
	@Required
	@Column(name="data_vinculacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataVinculacao;
	
	public GerenteTecnico() {
		
	}
	
	public GerenteTecnico(AnaliseTecnica analiseTecnica, UsuarioLicenciamento usuario) {
		
		super();
		this.analiseTecnica = analiseTecnica;
		this.usuario = usuario;
		this.dataVinculacao = new Date();
		
	}	
	
	public static void vincularAnalise(UsuarioLicenciamento usuario, UsuarioLicenciamento usuarioExecutor, AnaliseTecnica analiseTecnica) {
		
		if (!usuario.hasPerfil(Perfil.GERENTE_TECNICO))
			throw new PermissaoNegadaException(Mensagem.GERENTE_DIFERENTE_DE_GERENTE_TECNICO);		
		
		GerenteTecnico gerenteTecnico = new GerenteTecnico(analiseTecnica, usuario);
		gerenteTecnico.save();
		
		analiseTecnica.usuarioValidacao = usuarioExecutor;
		analiseTecnica._save();			
	}
	
	public GerenteTecnico gerarCopia() {
		
		GerenteTecnico copia = new GerenteTecnico();
		
		copia.usuario = this.usuario;
		copia.dataVinculacao = this.dataVinculacao;
		
		return copia;
	}
}
