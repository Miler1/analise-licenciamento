package models.tramitacao;

import models.UsuarioAnalise;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

//  View com informações do objeto tramitavel

 @Entity
 @Table(schema="tramitacao", name = "OBJETO_TRAMITAVEL")
public class ObjetoTramitavel extends GenericModel {

 	@Id
 	@Column(name = "ID_OBJETO_TRAMITAVEL")
	public Long id;
 	
 	@ManyToOne(fetch=FetchType.LAZY)
 	@JoinColumn(name = "ID_CONDICAO", referencedColumnName = "ID_CONDICAO")
	public Condicao condicao;

 	@ManyToOne(fetch=FetchType.LAZY)
 	@JoinColumn(name = "ID_USUARIO", referencedColumnName = "id")
	public UsuarioAnalise usuarioResponsavel;
	
 	@ManyToOne
 	@JoinColumn(name = "ID_PAI", referencedColumnName = "ID_OBJETO_TRAMITAVEL")
	public ObjetoTramitavel objetoTramitavelPai;
	
 	@Column(name = "ID_FLUXO")
	public Long idFluxo;
 	
 	@Column(name = "dt_criacao_objeto_tramitavel")
 	public Date dataCriacao;
 	
 	@ManyToOne(fetch=FetchType.LAZY)
 	@JoinColumn(name = "ID_RESPONSAVEL_ANTERIOR", referencedColumnName = "id")
	public UsuarioAnalise responsavelAnterior;
	
 	@OneToMany
 	@JoinColumn(name = "ID_OBJETO_TRAMITAVEL", referencedColumnName = "ID_OBJETO_TRAMITAVEL")
	public List<AcaoDisponivelObjetoTramitavel> acoesDisponiveis;
	
	public UsuarioAnalise getResponsavel() {
		return this.usuarioResponsavel;
	}
	
 	/** Getter usados em serializadores **/
	
	public Long getIdCondicao() {
		return this.condicao.idCondicao;
	}
	
	public String getNmCondicao() {
		return this.condicao.nomeCondicao;
	}
	
	public Long getIdEtapa() {
		return this.condicao.idEtapa;
	}
	
	public String getNomeEtapa() {
		return this.condicao.nomeEtapa;
	}
	
	public Long getIdUsuarioResponsavel() {
		
		return (this.usuarioResponsavel != null) ? this.usuarioResponsavel.id : null;
	}
	
	public String getNomeUsuarioResponsavel() {
		
		return (this.usuarioResponsavel != null && this.usuarioResponsavel.usuarioEntradaUnica != null)
				? this.usuarioResponsavel.usuarioEntradaUnica.nome : null;
	}

	public Long getIdResponsavelAnterior() {
		
		return (this.responsavelAnterior != null) ? this.responsavelAnterior.id : null;
	}
	
	public String getNomeResponsavelAnterior() {
		
		return (this.responsavelAnterior != null && this.responsavelAnterior.usuarioEntradaUnica != null)
				? this.responsavelAnterior.usuarioEntradaUnica.nome : null;
	}

	/**
	 * @author alaor
	 * 
	 * Valida se um processo está em uma determinada condição para realizar a tramitação.
	 * 
	 * @param idObjeto
	 * @param condicao
	 * @return
	 */
	public static boolean validaCondicao(Long idObjeto, Long condicao){
		
		ObjetoTramitavel objeto = (ObjetoTramitavel) ObjetoTramitavel.find("id= :idObjeto AND condicao.idCondicao= :condicao ")
			.setParameter("idObjeto", idObjeto)
			.setParameter("condicao", condicao)
			.first();
		
		if(objeto != null)
			return true;
		
		return false;
	}
}
