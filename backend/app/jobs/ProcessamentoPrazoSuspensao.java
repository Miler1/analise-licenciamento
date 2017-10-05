package jobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import exceptions.AppException;
import models.Processo;
import models.Suspensao;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Licenca;
import models.licenciamento.LicenciamentoWebService;
import models.licenciamento.StatusCaracterizacao;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.Condicao;
import play.Logger;
import play.db.jpa.JPA;
import play.jobs.On;
import utils.Mensagem;

@On("0 0/1 * 1/1 * ? *")
public class ProcessamentoPrazoSuspensao  extends GenericJob {
	
	@Override
	public void executar() throws Exception{

		Logger.info("[INICIO-JOB] ::ProcessamentoPrazoSuspensao:: [INICIO-JOB]");
		verificaPrazoSuspensao();
		Logger.info("[FIM-JOB] ::ProcessamentoPrazoSuspensao:: [FIM-JOB]");
		

	}
	
	public void verificaPrazoSuspensao() {
		
		List<Suspensao> suspensoes = Suspensao.findAtivas();
		
		if(!suspensoes.isEmpty()) {
			
			for(Suspensao suspensao: suspensoes){
				
				//mostra a data final da suspensao
				
				Date dataFinalSuspenso = addDays(suspensao.dataSuspensao, suspensao.qtdeDiasSuspensao);
				Date hoje =  new Date();
				
				if(hoje.after(dataFinalSuspenso)) {	
					
					Licenca dadosLicenca = Licenca.findById(suspensao.licenca.id);
					Licenca novaLicenca = new Licenca(dadosLicenca.caracterizacao);
					novaLicenca.caracterizacao = dadosLicenca.caracterizacao;					
					novaLicenca.dataCadastro = dadosLicenca.dataCadastro;
					novaLicenca.dataValidade = dadosLicenca.dataValidade;
					novaLicenca.numero = dadosLicenca.numero;
					novaLicenca.ativo = true;
					novaLicenca.licencaAnterior = dadosLicenca;
					
					if(dadosLicenca.dataValidadeProrrogada == null) {
						
						novaLicenca.dataValidadeProrrogada = addDays(dadosLicenca.dataValidade, suspensao.qtdeDiasSuspensao);
					} else {
						
						novaLicenca.dataValidadeProrrogada = addDays(dadosLicenca.dataValidadeProrrogada, suspensao.qtdeDiasSuspensao);
					}
					
					novaLicenca._save();
			
					suspensao.ativo = false;
					suspensao._save();
					
					commitTransaction();
					
					try {
						
						List<Long> idsLicencas = new ArrayList<>();
						idsLicencas.add(novaLicenca.id);
						
						LicenciamentoWebService webService = new LicenciamentoWebService();
						webService.gerarPDFLicencas(idsLicencas);
						
						List<Long> idsCaracterizacoes =  new ArrayList();
						idsCaracterizacoes.add(novaLicenca.caracterizacao.id);
						
						if(dadosLicenca.licencaAnalise != null) {
							Processo processo = dadosLicenca.licencaAnalise.analiseTecnica.analise.processo;
						
							if(processo.objetoTramitavel.condicao.equals(Condicao.SUSPENSO)) {						
								processo.tramitacao.tramitar(processo, AcaoTramitacao.EMITIR_LICENCA);
							}
							
						}
						
						Caracterizacao.setStatusCaracterizacao(idsCaracterizacoes, StatusCaracterizacao.FINALIZADO);
						
					} catch (Exception e) {
						
						rollbackTransaction();
						
						Licenca licenca = Licenca.findById(novaLicenca.id);
						
						if(licenca != null)
							licenca.delete();
						
						Suspensao reverterSuspensao = Suspensao.findById(suspensao.id);
						
						if(reverterSuspensao != null) {
							
							reverterSuspensao.ativo = true;
							reverterSuspensao._save();
						}
						
						
						commitTransaction();
						
						throw new AppException(Mensagem.ERRO_EMITIR_LICENCAS);
						
						
					}
				}
				
			}
		}
		
	}
	
	public static Date addDays(Date date, int days) {
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
				
		return cal.getTime();
	}

}
