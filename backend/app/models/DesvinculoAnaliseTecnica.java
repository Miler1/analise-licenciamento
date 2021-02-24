package models;

import exceptions.ValidacaoException;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.DateUtil;
import utils.Mensagem;

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


	@Column(name="resposta_coordenador")
	public String respostaCoordenador;


	@Column(name="aprovada")
	public Boolean aprovada;

	@OneToOne
	@JoinColumn(name="id_coordenador", referencedColumnName = "id")
	public UsuarioAnalise coordenador;

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

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void update(DesvinculoAnaliseTecnica novoDesvinculo) {

		this.respostaCoordenador = novoDesvinculo.respostaCoordenador;
		this.aprovada = novoDesvinculo.aprovada;
		this.dataResposta = novoDesvinculo.dataResposta;
		this.analistaTecnicoDestino = novoDesvinculo.analistaTecnicoDestino;

		this._save();

	}

	public void solicitaDesvinculoSAnaliseTecnica(UsuarioAnalise usuarioSessao){

		if(this.justificativa == null || this.justificativa.equals("")){

			throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);

		}

		this.dataSolicitacao = new Date();

		String siglaSetor = usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla;

		Coordenador coordenador = Coordenador.distribuicaoAutomaticaCoordenadorAnaliseTecnica(siglaSetor, this.analiseTecnica);

		coordenador.save();

		this.coordenador = UsuarioAnalise.findByCoordenador(coordenador);

		this.analistaTecnico =  usuarioSessao;

		this.save();

		this.analiseTecnica = AnaliseTecnica.findById(this.analiseTecnica.id);
		this.analiseTecnica.analise.processo.tramitacao.tramitar(this.analiseTecnica.analise.processo, AcaoTramitacao.SOLICITAR_DESVINCULO_ANALISE_TECNICA, usuarioSessao, this.coordenador);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analiseTecnica.analise.processo.objetoTramitavel.id), usuarioSessao);

	}


	public void respondeSolicitacaoDesvinculoAnaliseTecnica(UsuarioAnalise usuarioSessao){

		if(this.justificativa == null ||
				this.justificativa.equals("") ||
				this.respostaCoordenador == null  ||
				this.respostaCoordenador.equals("") ||
				this.aprovada == null){

			throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);

		}

		this.analiseTecnica = AnaliseTecnica.findById(this.analiseTecnica.id);

		this.dataResposta = new Date();

		DesvinculoAnaliseTecnica desvinculoAlterar = DesvinculoAnaliseTecnica.findById(this.id);

		desvinculoAlterar.update(this);

		this.analiseTecnica = AnaliseTecnica.findById(this.analiseTecnica.id);

		if(this.aprovada) {

			this.analistaTecnicoDestino = UsuarioAnalise.findById(this.analistaTecnicoDestino.id);

			AnalistaTecnico analistaTecnico = AnalistaTecnico.find("id_analise_tecnica = :id_analise_tecnica")
					.setParameter("id_analise_tecnica", this.analiseTecnica.id).first();

			analistaTecnico.usuario = this.analistaTecnicoDestino;
			analistaTecnico._save();

			this.analiseTecnica.analise.processo.tramitacao.tramitar(this.analiseTecnica.analise.processo, AcaoTramitacao.APROVAR_SOLICITACAO_DESVINCULO_TECNICO, usuarioSessao, this.analistaTecnicoDestino);
			this.analiseTecnica.dataVencimentoPrazo = DateUtil.somaDiasEmData(new Date(), Configuracoes.PRAZO_ANALISE_TECNICA);
			this.analiseTecnica.analise.diasAnalise.preencheDiasAnaliseTecnica();
			this.analiseTecnica.analise.diasAnalise._save();
			this.analiseTecnica._save();

		} else {

			this.analistaTecnicoDestino = this.analistaTecnico;
			this.analiseTecnica.analise.processo.tramitacao.tramitar(this.analiseTecnica.analise.processo, AcaoTramitacao.NEGAR_SOLICITACAO_DESVINCULO_TECNICO, usuarioSessao, this.analistaTecnico);

			this.analiseTecnica.dataVencimentoPrazo = DateUtil.somaDiasEmData(DateUtil.somaDiasEmData(this.analiseTecnica.dataCadastro, Configuracoes.PRAZO_ANALISE_TECNICA) , DiasAnalise.intervalosTramitacoesAnaliseTecnica(this.analiseTecnica.analise.processo.getHistoricoTramitacao()));
			this.analiseTecnica.analise.diasAnalise.preencheDiasAnaliseTecnica();
			this.analiseTecnica.analise.diasAnalise._save();
			this.analiseTecnica._save();

		}

		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.analiseTecnica.analise.processo.objetoTramitavel.id), usuarioSessao);

		DesvinculoAnaliseTecnica desvinculoAnaliseTecnica = DesvinculoAnaliseTecnica.findById(this.id);
		desvinculoAnaliseTecnica.update(this);

	}

}
