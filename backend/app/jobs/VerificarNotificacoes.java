package jobs;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Analise;
import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.DiasAnalise;
import models.EmailNotificacaoArquivamentoProcesso;
import models.LicencaAnalise;
import models.Notificacao;
import models.licenciamento.SolicitacaoDocumentoCaracterizacao;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.Logger;
import play.jobs.On;

@On("cron.vetificarNotificacoes")
public class VerificarNotificacoes extends GenericJob {

	@Override
	public void executar() throws Exception {
		
		Logger.info("[INICIO-JOB] ::VerificarNotificacoes:: [INICIO-JOB]");
		verificarStatusNotificacoes();
		Logger.info("[FIM-JOB] ::VerificarNotificacoes:: [FIM-JOB]");
		
	}
	
	public void verificarStatusNotificacoes() {
		
		List<Analise> analises = Analise.findComNotificacao();
		
		if(!analises.isEmpty()) {
			
			for(Analise analise : analises) {
				
				DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();

				if(analise.temNotificacaoAberta && (verificaDiasAnalise.qtdeDiasNotificacao != null
						&& verificaDiasAnalise.qtdeDiasNotificacao > 10)) {
					
					analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.ARQUIVAR_PROCESSO);
					analise.temNotificacaoAberta = false;
					analise._save();

					enviarEmailArquivamento(analise);

				} else if(!analise.hasNotificacaoNaoResolvida()) {

					List<Notificacao> notificacoes = Notificacao.getByAnalise(analise);
					analise.temNotificacaoAberta = false;
					analise.save();
					
					for(Notificacao notificacao : notificacoes) {
						
						SolicitacaoDocumentoCaracterizacao solicitacaoDocumentoCaracterizacao = 
								SolicitacaoDocumentoCaracterizacao.findByTipoAndCaracterizacao(notificacao.tipoDocumento, analise.processo.getCaracterizacao());

						
						if(notificacao.documentoCorrigido != null) {
							
							solicitacaoDocumentoCaracterizacao.documento = notificacao.documentoCorrigido;
							solicitacaoDocumentoCaracterizacao.save();
							
							notificacao.analiseDocumento.documento = notificacao.documentoCorrigido;
							notificacao.analiseDocumento.save();
							
						}
						
					}
					
					if(notificacoes.get(0).analiseJuridica != null) {
						
						analise.analiseJuridica.ativo = false;
						analise.analiseJuridica._save();
						
						AnaliseJuridica novaAnaliseJuridica = analise.analiseJuridica.gerarCopia();
						novaAnaliseJuridica._save();
						
						analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.RESOLVER_NOTIFICACAO_JURIDICA);
						
					} else if(notificacoes.get(0).analiseTecnica != null) {
						
						analise.analiseTecnica.ativo = false;
						analise.analiseTecnica._save();
						
						AnaliseTecnica novaAnaliseTecnica = analise.analiseTecnica.gerarCopia();
						novaAnaliseTecnica._save();
						
						/**
						 * Workaround para persistir as licenças e os pareceres técnicos restrições
						 */		
						for(LicencaAnalise licencaAnalise: novaAnaliseTecnica.licencasAnalise) {
							
							licencaAnalise._save();
							
							licencaAnalise.saveCondicionantes();
							licencaAnalise.saveRecomendacoes();
						}
						
						analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.RESOLVER_NOTIFICACAO_TECNICA);
						
					}
					
				}
				
			}
			
		}
		
		
	}

	private void enviarEmailArquivamento(Analise analise) {

		List<String> destinatarios = new ArrayList<>();
		destinatarios.addAll(analise.processo.empreendimento.emailsProprietarios());
		destinatarios.addAll(analise.processo.empreendimento.emailsResponsaveis());

		HistoricoTramitacao arquivamento = HistoricoTramitacao.getUltimaTramitacao(analise.processo.idObjetoTramitavel);
		HistoricoTramitacao historicoAnalise = HistoricoTramitacao.getPenultimaTramitacao(analise.processo.idObjetoTramitavel);

		List<Notificacao> notificacoes = Notificacao.find("id_historico_tramitacao", historicoAnalise.idHistorico).fetch();

		new EmailNotificacaoArquivamentoProcesso(analise.processo, destinatarios, arquivamento.dataInicial, notificacoes).enviar();
	}
	
}
