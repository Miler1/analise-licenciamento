package models;

import br.ufla.lemaf.beans.pessoa.Contato;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Empreendimento;
import models.licenciamento.StatusCaracterizacao;
import models.licenciamento.StatusCaracterizacaoEnum;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.db.jpa.GenericModel;
import security.cadastrounificado.CadastroUnificadoWS;
import utils.Configuracoes;
import utils.DateUtil;

import javax.persistence.*;
import javax.validation.ValidationException;
import java.util.*;

import static security.Auth.getUsuarioSessao;

@Entity
@Table(schema = "analise", name = "parecer_presidente")
public class ParecerPresidente extends GenericModel {

	public static final String SEQ = "analise.parecer_presidente_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	@Column(name = "id")
	public Long id;

	@OneToOne
	@JoinColumn(name = "id_analise")
	public Analise analise;

	@ManyToOne
	@JoinColumn(name = "id_tipo_resultado_analise")
	public TipoResultadoAnalise tipoResultadoAnalise;

	@Column(name = "parecer")
	public String parecer;

	@Column(name = "data_parecer")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataParecer;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_usuario_presidente", referencedColumnName = "id")
	public UsuarioAnalise usuario;

	@Column(name = "id_historico_tramitacao")
	public Long idHistoricoTramitacao;

	public Date getDataParecer() {
		return dataParecer;
	}

	public static ParecerPresidente getUltimoParecerPresidente(List<ParecerPresidente> pareceresPresidente) {

		return pareceresPresidente.stream().max(Comparator.comparing(ParecerPresidente::getDataParecer)).orElseThrow(ValidationException::new);

	}

	public void finalizar(Analise analise, UsuarioAnalise presidente) throws Exception {

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.ANALISE_APROVADA)) {

			Analise.alterarStatusLicenca(StatusCaracterizacaoEnum.ANALISE_APROVADA.codigo, analise.processo.numero);

			analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.APROVAR_SOLICITACAO_LICENCA, getUsuarioSessao());
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analise.processo.idObjetoTramitavel), getUsuarioSessao());

			analise.processo.caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacaoEnum.ANALISE_APROVADA.id);

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.ANALISE_NAO_APROVADA)) {

			Analise.alterarStatusLicenca(StatusCaracterizacaoEnum.ANALISE_REJEITADA.codigo, analise.processo.numero);

			analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.NEGAR_SOLICITACAO_LICENCA, getUsuarioSessao());
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analise.processo.idObjetoTramitavel), getUsuarioSessao());

			analise.processo.caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacaoEnum.ANALISE_REJEITADA.id);

		}

		this.usuario = presidente;
		this.dataParecer = new Date();

		HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(analise.processo.idObjetoTramitavel);
		this.idHistoricoTramitacao = historicoTramitacao.idHistorico;

		this.save();

		enviarEmailStatusAnalise(analise);

	}

	public void enviarEmailStatusAnalise(Analise analise) throws Exception {

		Empreendimento empreendimento = Empreendimento.findById(analise.processo.empreendimento.id);

		Contato emailCadastrante =  CadastroUnificadoWS.ws.getPessoa(empreendimento.cpfCnpjCadastrante).contatos.stream()
				.filter(contato -> contato.principal == true && contato.tipo.descricao.equals("Email")).findFirst().orElseThrow(null);

		List<String> interessados = new ArrayList<>(Collections.singleton(emailCadastrante.valor));

		EmailNotificacaoStatusAnalise emailNotificacaoStatusAnalise = new EmailNotificacaoStatusAnalise(analise,this, interessados);
		emailNotificacaoStatusAnalise.enviar();

	}

}
