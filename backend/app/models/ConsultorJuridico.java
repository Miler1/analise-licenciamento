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
@Table(schema="analise", name="consultor_juridico")
public class ConsultorJuridico extends GenericModel {
	
	public static final String SEQ = "analise.consultor_juridico_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_analise_juridica")
	public AnaliseJuridica analiseJuridica;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_usuario")
	public Usuario usuario;
	
	@Required
	@Column(name="data_vinculacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataVinculacao;
	
	public ConsultorJuridico(AnaliseJuridica analiseJuridica, Usuario usuario) {
		
		super();
		this.analiseJuridica = analiseJuridica;
		this.usuario = usuario;
		this.dataVinculacao = new Date();
		
	}
	
	public static void vincularAnalise(Usuario usuario, AnaliseJuridica analiseJuridica) {
		
		if (!usuario.hasPerfil(Perfil.CONSULTOR_JURIDICO))
			throw new PermissaoNegadaException(Mensagem.CONSULTOR_DIFERENTE_DE_CONSULTOR_JURIDICO);		
		
		ConsultorJuridico consultorJuridico = new ConsultorJuridico(analiseJuridica, usuario);
		consultorJuridico.save();
		
	}
}
