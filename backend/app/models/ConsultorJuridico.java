package models;

import exceptions.PermissaoNegadaException;
import models.EntradaUnica.CodigoPerfil;
import models.EntradaUnica.Setor;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Mensagem;

import javax.persistence.*;
import java.util.Date;

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
	public UsuarioAnalise usuario;
	
	@Required
	@Column(name="data_vinculacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataVinculacao;
	
	public ConsultorJuridico(){
		super();
	}
	
	public ConsultorJuridico(AnaliseJuridica analiseJuridica, UsuarioAnalise usuario) {
		
		super();
		this.analiseJuridica = analiseJuridica;
		this.usuario = usuario;
		this.dataVinculacao = new Date();
		
	}
	
	public static void vincularAnalise(UsuarioAnalise usuario, AnaliseJuridica analiseJuridica, UsuarioAnalise usuarioExecutor) {
		
		if (!usuario.hasPerfil(CodigoPerfil.CONSULTOR_JURIDICO))
			throw new PermissaoNegadaException(Mensagem.CONSULTOR_DIFERENTE_DE_CONSULTOR_JURIDICO);
		
		ConsultorJuridico consultorJuridico = new ConsultorJuridico(analiseJuridica, usuario);
		consultorJuridico.save();
		
		analiseJuridica.usuarioValidacao = usuarioExecutor;
		analiseJuridica.save();
		
	}

	public ConsultorJuridico gerarCopia() {
		
		ConsultorJuridico copia = new ConsultorJuridico();
		
		copia.usuario = this.usuario;
		copia.dataVinculacao = this.dataVinculacao;
		
		return copia;
	}

	public Setor getSetor() {

		// TODO REFACTOR
//		PerfilUsuario perfil = PerfilUsuario.find("usuario.id = :x AND perfil.nome = :y")
//				.setParameter("x", this.usuario.id)
//				.setParameter("y", "Consultor JURÍDICO")
//				.first();
//
//		return perfil.setor;
		return null;
	}
}
