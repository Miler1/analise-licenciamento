package jobs;


import models.*;
import models.licenciamento.Caracterizacao;
import models.licenciamento.SolicitacaoDocumentoCaracterizacao;
import models.licenciamento.StatusCaracterizacao;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import play.Logger;
import play.jobs.On;
import security.cadastrounificado.CadastroUnificadoWS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@On("cron.vetificarNotificacoes")
public class VerificarNotificacoes extends GenericJob {

	@Override
	public void executar() throws Exception {
		
//		Logger.info("[INICIO-JOB] ::VerificarNotificacoes:: [INICIO-JOB]");
//		verificarStatusNotificacoes();
//		Logger.info("[FIM-JOB] ::VerificarNotificacoes:: [FIM-JOB]");
		
	}
	
	public void verificarStatusNotificacoes() {
		
		List<Analise> analises = Analise.findComNotificacao();
		
		if(!analises.isEmpty()) {
			
			for(Analise analise : analises) {
				
				DiasAnalise verificaDiasAnalise = DiasAnalise.find("analise.id", analise.id).first();

				Notificacao notificacaoArquivamento = null;

				if (verificaDiasAnalise.qtdeDiasNotificacao != null && verificaDiasAnalise.qtdeDiasNotificacao > 10) {

					if (analise.analiseTecnica != null) {

						notificacaoArquivamento = Notificacao.find("analiseTecnica.id", analise.analiseTecnica.id).first();

					} else if (analise.analiseJuridica != null) {

						notificacaoArquivamento = Notificacao.find("analiseJuridica.id", analise.analiseJuridica.id).first();
					}
				}

				if (verificaDiasAnalise.qtdeDiasNotificacao > 20 || (notificacaoArquivamento != null && notificacaoArquivamento.dataFinalNotificacao !=null
						&& CalculaDiferencaDias(notificacaoArquivamento.dataFinalNotificacao, new Date()) > 10)) {

					analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.ARQUIVAR_PROTOCOLO);
					analise.temNotificacaoAberta = false;
					analise._save();

					Caracterizacao caracterizacao = Caracterizacao.findById(analise.processo.caracterizacao.id);
					caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacao.ARQUIVADO);
					caracterizacao._save();

					enviarEmailArquivamento(analise);

				} else if(!analise.hasNotificacaoNaoResolvida()) {

					List<Notificacao> notificacoes = Notificacao.getByAnalise(analise);
					analise.temNotificacaoAberta = false;
					analise.save();
					
					for(Notificacao notificacao : notificacoes) {
						
						SolicitacaoDocumentoCaracterizacao solicitacaoDocumentoCaracterizacao = 
								SolicitacaoDocumentoCaracterizacao.findByTipoAndCaracterizacao(notificacao.tipoDocumento, analise.processo.caracterizacao);
						
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
						
						AnaliseJuridica novaAnaliseJuridica = analise.analiseJuridica.gerarCopia(true);
						novaAnaliseJuridica._save();

						Caracterizacao caracterizacao = Caracterizacao.findById(analise.processo.caracterizacao.id);
						caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacao.EM_ANALISE);
						caracterizacao._save();

						analise.processo.tramitacao.tramitar(analise.processo, AcaoTramitacao.RESOLVER_NOTIFICACAO_JURIDICA);
						
					} else if(notificacoes.get(0).analiseTecnica != null) {
						
						analise.analiseTecnica.ativo = false;
						analise.analiseTecnica._save();
						
						AnaliseTecnica novaAnaliseTecnica = analise.analiseTecnica.gerarCopia(true);
						novaAnaliseTecnica._save();

						Caracterizacao caracterizacao = Caracterizacao.findById(analise.processo.caracterizacao.id);
						caracterizacao.status = StatusCaracterizacao.findById(StatusCaracterizacao.EM_ANALISE);
						caracterizacao._save();
						
						/**
						 * Workaround para persistir as licenças e os pareceres técnicos restrições
						 */		
						for(LicencaAnalise licencaAnalise: novaAnaliseTecnica.licencasAnalise) {
							
							licencaAnalise._save();
							
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

		destinatarios = CadastroUnificadoWS.ws.getEmailProprietarioResponsaveis(analise.processo.empreendimento.empreendimentoEU.proprietarios,
																				analise.processo.empreendimento.empreendimentoEU.responsaveisLegais,
																				analise.processo.empreendimento.empreendimentoEU.responsaveisTecnicos, destinatarios);

		HistoricoTramitacao arquivamento = HistoricoTramitacao.getUltimaTramitacao(analise.processo.idObjetoTramitavel);
		HistoricoTramitacao historicoAnalise = HistoricoTramitacao.getPenultimaTramitacao(analise.processo.idObjetoTramitavel);

		List<Notificacao> notificacoes = Notificacao.find("id_historico_tramitacao", historicoAnalise.idHistorico).fetch();

		new EmailNotificacaoArquivamentoProcesso(analise.processo, destinatarios, arquivamento.dataInicial, notificacoes,
				historicoAnalise.relHistoricoTramitacaoSetor.siglaSetor).enviar();
	}


	public int CalculaDiferencaDias(Date dataInicial, Date dataFinal) {

		if (dataInicial == null || dataFinal == null) {

			return 0;
		}

		LocalDate dataInicio = new LocalDate(dataInicial.getTime());
		LocalDate dataFim = new LocalDate(dataFinal.getTime());

		return Days.daysBetween(dataInicio, dataFim).getDays();
	}
}
