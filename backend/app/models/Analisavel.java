package models;

import br.ufla.lemaf.beans.pessoa.Pessoa;
import models.licenciamento.TipoAnalise;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@MappedSuperclass
public abstract class Analisavel extends GenericModel {

	@ManyToOne
	@JoinColumn(name="id_analise")
	public Analise analise;

	@Required
	@Column(name="data_vencimento_prazo")
	public Date dataVencimentoPrazo;

	@Required
	@Column(name = "revisao_solicitada")
	public Boolean revisaoSolicitada;

	@Required
	@Column(name = "notificacao_atendida")
	public Boolean notificacaoAtendida;

	@Column(name = "ativo")
	public Boolean ativo;

	@Column(name = "data_inicio")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataInicio;

	@Column(name = "data_fim")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataFim;

	@Column(name = "parecer_validacao")
	public String parecerValidacao;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_usuario_validacao", referencedColumnName = "id")
	public UsuarioAnalise usuarioValidacao;

	public abstract TipoResultadoAnalise getTipoResultadoValidacao();

	public abstract TipoAnalise getTipoAnalise();

	public abstract List<Notificacao> getNotificacoes();

	public void inativar(){
		this.ativo = false;
		this._save();
	}
}
