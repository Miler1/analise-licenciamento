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
import models.portalSeguranca.Usuario;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Mensagem;

@Entity
@Table(schema="analise", name="analista_tecnico")
public class AnalistaTecnico extends GenericModel {
	
	public static final String SEQ = "analise.analista_tecnico_id_seq";
	
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
	public Usuario usuario;
	
	@Required
	@Column(name="data_vinculacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataVinculacao;
	
	public AnalistaTecnico(AnaliseTecnica analiseTecnica, Usuario usuario) {
		
		super();
		this.analiseTecnica = analiseTecnica;
		this.usuario = usuario;
		this.dataVinculacao = new Date();
		
	}	
	
	public static void vincularAnalise(Usuario usuario, AnaliseTecnica analiseTecnica) {
		
		if (!usuario.hasPerfil(Perfil.ANALISTA_TECNICO))
			throw new PermissaoNegadaException(Mensagem.ANALISTA_DIFERENTE_DE_ANALISTA_TECNICO);		
		
		AnalistaTecnico analistaTecnico = new AnalistaTecnico(analiseTecnica, usuario);
		analistaTecnico.save();
		
	}
}
