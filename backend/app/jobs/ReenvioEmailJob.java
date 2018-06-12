package jobs;


import java.util.Arrays;
import java.util.List;

import models.EmailNotificacaoArquivamentoProcesso;
import models.Notificacao;
import models.Processo;
import models.tramitacao.HistoricoTramitacao;
import org.apache.commons.lang.StringUtils;

import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.DlaCancelada;
import models.EmailNotificacaoAnaliseJuridica;
import models.EmailNotificacaoAnaliseTecnica;
import models.EmailNotificacaoCancelamentoDla;
import models.EmailNotificacaoCancelamentoLicenca;
import models.EmailNotificacaoSuspensaoLicenca;
import models.LicencaCancelada;
import models.ReenvioEmail;
import models.Suspensao;
import play.Logger;
import play.jobs.On;

@On("cron.reenviarEmail")
public class ReenvioEmailJob extends GenericJob {

	@Override
	public void executar() throws Exception {
		
		Logger.info("[INICIO-JOB] ::ReenvioEmail:: [INICIO-JOB]");
		
		List<ReenvioEmail> reenviosEmail = ReenvioEmail.findAll();
		
		for(ReenvioEmail reenvioEmail : reenviosEmail) {

			sendMail(reenvioEmail);

		}
		
		Logger.info("[FIM-JOB] ::ReenvioEmail:: [FIM-JOB]");
		
	}
	private void sendMail(ReenvioEmail reenvioEmail) {

		try {
			
			List<String> emailsDestinatarios = Arrays.asList(StringUtils.split(reenvioEmail.emailsDestinatario, ";"));

			switch(reenvioEmail.tipoEmail) {
				
				case NOTIFICACAO_ANALISE_JURIDICA:

					AnaliseJuridica analiseJuridica = AnaliseJuridica.findById(reenvioEmail.idItensEmail);
					new EmailNotificacaoAnaliseJuridica(analiseJuridica, emailsDestinatarios).enviar();
					
					break;

				case NOTIFICACAO_ANALISE_TECNICA:

					AnaliseTecnica analiseTecnica = AnaliseTecnica.findById(reenvioEmail.idItensEmail);
					new EmailNotificacaoAnaliseTecnica(analiseTecnica, emailsDestinatarios).enviar();
					
					break;

				case CANCELAMENTO_DLA:

					DlaCancelada dlaCancelada = DlaCancelada.findById(reenvioEmail.idItensEmail);
					new EmailNotificacaoCancelamentoDla(dlaCancelada, emailsDestinatarios).enviar();
					
					break;

				case CANCELAMENTO_LICENCA:

					LicencaCancelada licencaCancelada = LicencaCancelada.findById(reenvioEmail.idItensEmail);
					new EmailNotificacaoCancelamentoLicenca(licencaCancelada, emailsDestinatarios).enviar();
					
					break;
					
				case SUSPENSAO_LICENCA:

					Suspensao suspensao = Suspensao.findById(reenvioEmail.idItensEmail);
					new EmailNotificacaoSuspensaoLicenca(suspensao, emailsDestinatarios).enviar();
					
					break;

				case ARQUIVAMENTO_PROCESSO:

					Processo processo = Processo.findById(reenvioEmail.idItensEmail);

					HistoricoTramitacao arquivamento = HistoricoTramitacao.getUltimaTramitacao(processo.idObjetoTramitavel);
					HistoricoTramitacao historicoAnalise = HistoricoTramitacao.getPenultimaTramitacao(processo.idObjetoTramitavel);

					List<Notificacao> notificacoes = Notificacao.find("id_historico_tramitacao", historicoAnalise.idHistorico).fetch();

					new EmailNotificacaoArquivamentoProcesso(processo, emailsDestinatarios, arquivamento.dataInicial, notificacoes).enviar();

					break;

			}

			reenvioEmail.delete();

		} catch(Exception e) {

			reenvioEmail.log = e.getMessage();

			reenvioEmail.save();

		}

	}
	
}
