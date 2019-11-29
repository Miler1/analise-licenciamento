package models;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema="analise", name="desvinculo_analise_tecnica")
public class DesvinculoAnaliseTecnica extends GenericModel {

	public static final String SEQ = "analise.desvinculo_analise_tecnica_id_seq";

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;


	@ManyToOne
	@JoinColumn(name="id_analise_tecnica", nullable=true)
	public AnaliseTecnica analiseTecnica;

	@Required
	@Column(name="justificativa")
	public String justificativa;


	@Column(name="resposta_gerente")
	public String respostaGerente;


	@Column(name="aprovada")
	public Boolean aprovada;

	@OneToOne
	@JoinColumn(name="id_gerente", referencedColumnName = "id")
	public UsuarioAnalise gerente;

	@Required
	@Column(name="data_solicitacao")
	public Date dataSolicitacao;


	@Column(name="data_resposta")
	public Date dataResposta;

	@ManyToOne
	@JoinColumn(name="id_usuario")
	public UsuarioAnalise analistaTecnico;

	@ManyToOne
	@JoinColumn(name="id_usuario_destino")
	public UsuarioAnalise analistaTecnicoDestino;

	public void update(DesvinculoAnaliseTecnica novoDesvinculo) {

		this.respostaGerente = novoDesvinculo.respostaGerente;
		this.aprovada = novoDesvinculo.aprovada;
		this.dataResposta = novoDesvinculo.dataResposta;
		this.analistaTecnicoDestino = novoDesvinculo.analistaTecnicoDestino;

		this._save();
	}

}
