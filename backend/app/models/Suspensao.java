package models;

import exceptions.AppException;
import exceptions.ValidacaoException;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Licenca;
import models.licenciamento.StatusCaracterizacao;
import models.licenciamento.StatusCaracterizacaoEnum;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.Logger;
import play.db.jpa.GenericModel;
import security.cadastrounificado.CadastroUnificadoWS;
import utils.DateUtil;
import utils.ListUtil;
import utils.Mensagem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema="analise", name="licenca_suspensa")
public class Suspensao extends GenericModel {

	public static final String SEQ = "analise.suspensao_id_seq";

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@OneToOne
	@JoinColumn(name="id_licenca")
	public Licenca licenca;

	@ManyToOne
	@JoinColumn(name="id_usuario_executor")
	public UsuarioAnalise usuario;

	@Column(name="quantidade_dias_suspensao")
	public Integer qtdeDiasSuspensao;

	@Column(name="data_suspensao")
	public Date dataSuspensao;

	public String justificativa;

	public Boolean ativo;

	public Suspensao() {
	}

	public static List<Suspensao> findAtivas() {
		return Suspensao.find("byAtivo", true).fetch();
	}

	public void suspenderLicenca(UsuarioAnalise usuarioExecutor) {

		Calendar c = Calendar.getInstance();
		Date dataAtual = c.getTime();

		Licenca licencaSuspensa = Licenca.findById(this.licenca.id);
		this.licenca = licencaSuspensa;
		this.ativo = true;

		Date validadeLicenca = this.licenca.dataValidade;

		Integer maxPrazo = DateUtil.getDiferencaEmDias(dataAtual, validadeLicenca);

		if(this.qtdeDiasSuspensao > maxPrazo)
			throw new ValidacaoException(Mensagem.PRAZO_MAXIMO_SUSPENSAO_EXCEDIDO, maxPrazo.toString());

		this.dataSuspensao = dataAtual;
		this.usuario = usuarioExecutor;

		try {

			licencaSuspensa.ativo = false;
			licencaSuspensa.save();
			this.save();

			Caracterizacao.setStatusCaracterizacao(ListUtil.createList(this.licenca.caracterizacao.id), StatusCaracterizacao.SUSPENSO);

			Processo processo = this.licenca.caracterizacao.processo;
			processo.tramitacao.tramitar(processo, AcaoTramitacao.SUSPENDER_PROTOCOLO, usuarioExecutor);
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(processo.objetoTramitavel.id), usuarioExecutor);

			this.licenca.caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacaoEnum.SUSPENSO.id);

			ParecerSecretario parecerSecretario = ParecerSecretario.getUltimoParecerSecretario(this.licenca.caracterizacao.processo.analise.pareceresSecretario);
			parecerSecretario.enviarEmailStatusAnalise(this.licenca.caracterizacao.processo.analise);

		} catch (Exception e) {

			Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.ERRO_ENVIAR_EMAIL, e.getMessage());

		}

	}

	private void enviarNotificacaoSuspensaoPorEmail() {

		List<String> destinatarios = new ArrayList<String>();

		destinatarios = CadastroUnificadoWS.ws.getEmailProprietarioResponsaveis(this.licenca.caracterizacao.empreendimento.empreendimentoEU.proprietarios,
																				this.licenca.caracterizacao.empreendimento.empreendimentoEU.responsaveisLegais,
																				this.licenca.caracterizacao.empreendimento.empreendimentoEU.responsaveisTecnicos, destinatarios);


		EmailNotificacaoSuspensaoLicenca emailNotificacao = new EmailNotificacaoSuspensaoLicenca(this, destinatarios);
		emailNotificacao.enviar();

	}

	private Boolean deveSuspenderProcesso(Licenca licenca) {

		if(licenca.licencaAnalise == null)
			return false;

		Processo processo = licenca.licencaAnalise.analiseTecnica.analise.processo;

		int numLicencasSuspensas = 0;

		for(Caracterizacao caracterizacao : processo.empreendimento.caracterizacoes) {
			if(caracterizacao.isSuspensa())
				numLicencasSuspensas++;
		}

		if(processo.getCaracterizacoesNaoArquivadas().size()-1 == numLicencasSuspensas)
			return true;

		return false;
	}
}
