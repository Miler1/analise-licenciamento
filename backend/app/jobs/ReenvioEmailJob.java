package jobs;


import models.*;
import models.licenciamento.Licenca;
import models.tramitacao.HistoricoTramitacao;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.jobs.On;

import java.util.Arrays;
import java.util.List;

@On("cron.reenviarEmail")
public class ReenvioEmailJob extends GenericJob {

	@Override
	public void executar() throws Exception {
		
//		Logger.info("[INICIO-JOB] ::ReenvioEmail:: [INICIO-JOB]");
//
//		List<ReenvioEmail> reenviosEmail = ReenvioEmail.findAll();
//
//		for(ReenvioEmail reenvioEmail : reenviosEmail) {
//
//			sendMail(reenvioEmail);
//
//		}
//
//		Logger.info("[FIM-JOB] ::ReenvioEmail:: [FIM-JOB]");
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
//					new EmailNotificacaoAnaliseTecnica(analiseTecnica, emailsDestinatarios).enviar();
					
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

					new EmailNotificacaoArquivamentoProcesso(processo, emailsDestinatarios, arquivamento.dataInicial, notificacoes,
							historicoAnalise.relHistoricoTramitacaoSetor.siglaSetor).enviar();

					break;

				case PRORROGACAO_LICENCA:

				Licenca licenca = Suspensao.findById(reenvioEmail.idItensEmail);
				new EmailNotificacaoProrrogacaoLicenca(licenca, emailsDestinatarios).enviar();

				break;
			}

			reenvioEmail.delete();

		} catch(Exception e) {

			reenvioEmail.log = e.getMessage();

			reenvioEmail.save();
		}
	}
}
