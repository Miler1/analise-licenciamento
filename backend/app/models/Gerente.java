package models;

import exceptions.PermissaoNegadaException;
import models.EntradaUnica.CodigoPerfil;
import models.licenciamento.TipoAnalise;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Mensagem;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema="analise", name="gerente")
public class Gerente extends GenericModel {
	
	public static final String SEQ = "analise.gerente_id_seq";

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@ManyToOne
	@JoinColumn(name="id_analise_tecnica")
	public AnaliseTecnica analiseTecnica;

	@ManyToOne
	@JoinColumn(name="id_analise_geo")
	public AnaliseGeo analiseGeo;

	@Required
	@ManyToOne
	@JoinColumn(name="id_usuario")
	public UsuarioAnalise usuario;
	
	@Required
	@Column(name="data_vinculacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataVinculacao;
	
	public Gerente() {
		
	}
	
	public Gerente(AnaliseTecnica analiseTecnica, UsuarioAnalise usuario) {

		super();
		this.analiseTecnica = analiseTecnica;
		this.usuario = usuario;
		this.dataVinculacao = new Date();

	}

	public Gerente(AnaliseGeo analiseGeo, UsuarioAnalise usuario) {

		super();
		this.analiseGeo = analiseGeo;
		this.usuario = usuario;
		this.dataVinculacao = new Date();

	}

	public static void vincularAnalise(UsuarioAnalise usuario, UsuarioAnalise usuarioExecutor, AnaliseTecnica analiseTecnica) {
		
		if (!usuario.hasPerfil(CodigoPerfil.GERENTE))
			throw new PermissaoNegadaException(Mensagem.GERENTE_DIFERENTE_DE_GERENTE_TECNICO);		
		
		Gerente gerente = new Gerente(analiseTecnica, usuario);
		gerente.save();

		analiseTecnica.usuarioValidacao = usuarioExecutor;
		analiseTecnica._save();
	}

	public static void vincularAnaliseGeo(UsuarioAnalise usuario, UsuarioAnalise usuarioExecutor, AnaliseGeo analiseGeo) {

		if (!usuario.hasPerfil(CodigoPerfil.GERENTE))
			throw new PermissaoNegadaException(Mensagem.GERENTE_DIFERENTE_DE_GERENTE_TECNICO);

		Gerente gerente = new Gerente(analiseGeo, usuario);
		gerente.save();

		analiseGeo.usuarioValidacao = usuarioExecutor;
		analiseGeo._save();
	}
	
	public Gerente gerarCopia() {
		
		Gerente copia = new Gerente();
		
		copia.usuario = this.usuario;
		copia.dataVinculacao = this.dataVinculacao;
		
		return copia;
	}
}
