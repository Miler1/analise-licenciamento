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

import exceptions.AppException;
import exceptions.PermissaoNegadaException;
import exceptions.ValidacaoException;
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
	
	public AnalistaTecnico() {
		
	}
	
	public AnalistaTecnico(AnaliseTecnica analiseTecnica, Usuario usuario) {
		
		super();
		this.analiseTecnica = analiseTecnica;
		this.usuario = usuario;
		this.dataVinculacao = new Date();
		
	}	
	
	public static void vincularAnalise(Usuario usuario, AnaliseTecnica analiseTecnica, Usuario usuarioExecutor, String justificativaCoordenador) {
		
		if (!usuario.hasPerfil(Perfil.ANALISTA_TECNICO))
			throw new ValidacaoException(Mensagem.ANALISTA_DIFERENTE_DE_ANALISTA_TECNICO);
		
		/**
		 * A justificativa é somente obrigatória para o coordenador que vincula uma analista técnico
		 */
		if (usuarioExecutor.perfilSelecionado.id.equals(Perfil.COORDENADOR_TECNICO)) {
			
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
		if (usuarioExecutor.perfilSelecionado.id.equals(Perfil.GERENTE_TECNICO)){
			
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
}
