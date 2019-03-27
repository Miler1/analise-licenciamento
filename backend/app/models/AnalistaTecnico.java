package models;

import exceptions.ValidacaoException;
import models.EntradaUnica.CodigoPerfil;
import models.EntradaUnica.Setor;
import models.portalSeguranca.UsuarioLicenciamento;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Mensagem;

import javax.persistence.*;
import java.util.Date;

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
	public UsuarioLicenciamento usuario;
	
	@Required
	@Column(name="data_vinculacao")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataVinculacao;
	
	public AnalistaTecnico() {
		
	}
	
	public AnalistaTecnico(AnaliseTecnica analiseTecnica, UsuarioLicenciamento usuario) {
		
		super();
		this.analiseTecnica = analiseTecnica;
		this.usuario = usuario;
		this.dataVinculacao = new Date();
		
	}	
	
	public static void vincularAnalise(UsuarioLicenciamento usuario, AnaliseTecnica analiseTecnica, UsuarioLicenciamento usuarioExecutor, String justificativaCoordenador) {

		if (!usuario.hasPerfil(CodigoPerfil.ANALISTA_TECNICO))
			throw new ValidacaoException(Mensagem.ANALISTA_DIFERENTE_DE_ANALISTA_TECNICO);
		
		/**
		 * A justificativa é somente obrigatória para o coordenador que vincula uma analista técnico
		 */
		if (usuarioExecutor.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.COORDENADOR_TECNICO)) {
			
			if (justificativaCoordenador == null || justificativaCoordenador.isEmpty()){
				throw new ValidacaoException(Mensagem.ANALISTA_JUSTIFICATIVA_COORDENADOR_OBRIGATORIA);
			}
			
			analiseTecnica.justificativaCoordenador = justificativaCoordenador;
		}
		
		AnalistaTecnico analistaTecnico = new AnalistaTecnico(analiseTecnica, usuario);
		analistaTecnico.save();
		
		/**
		 * Se for o gerente o executor da vinculação, então atribui o usuário executor para o campo do gerente,
		 * caso contrário atribui o usuário executor para o campo do coordenador. 
		 */
		if (usuarioExecutor.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.GERENTE_TECNICO)){
			
			analiseTecnica.usuarioValidacaoGerente = usuarioExecutor;
			
		} else {
			
			analiseTecnica.usuarioValidacao = usuarioExecutor;
		}
		
		analiseTecnica._save();
	}
	
	public AnalistaTecnico gerarCopia() {
		
		AnalistaTecnico copia = new AnalistaTecnico();
		
		copia.usuario = this.usuario;
		copia.dataVinculacao = this.dataVinculacao;
		
		return copia;
	}

	public Setor getSetor() {
//TODO REFACTOR

//		PerfilUsuario perfil = PerfilUsuario.find("usuario.id = :x AND perfil.nome = :y")
//				.setParameter("x", this.usuario.id)
//				.setParameter("y", "Analista TÉCNICO")
//				.first();
//
//		return perfil.setor;

		return null;
	}
}
