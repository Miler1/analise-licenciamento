package jobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Analise;
import models.AnaliseJuridica;
import models.Processo;
import models.licenciamento.Caracterizacao;
import models.licenciamento.LicenciamentoWebService;
import play.Logger;
import play.jobs.On;
import utils.Configuracoes;

@On("cron.processamentoCaracterizacoesEmAndamento")
public class ProcessamentoCaracterizacaoEmAndamento extends GenericJob {

	@Override
	public void executar() throws Exception {

		Logger.info("ProcessamentoCaracterizacaoEmAndamento:: Iniciando job");
		
		List<Caracterizacao> caracterizacoes = new LicenciamentoWebService().getCaracterizacoesEmAndamento();
		
		for(Caracterizacao caracterizacao : caracterizacoes) {
			
			processarCaracterizacao(caracterizacao);
			
		}
		
	}
	
	private void processarCaracterizacao(Caracterizacao caracterizacao) {
		
		try {

			Logger.info("ProcessamentoCaracterizacaoEmAndamento:: Processando " + caracterizacao.numeroProcesso);
			
			Processo processo = Processo.find("byNumero", caracterizacao.numeroProcesso).first();
			
			boolean deveTramitar = false;
			
			if(processo == null) {
				
				processo = criarNovoProcesso(caracterizacao);
				
				Analise analise = criarNovaAnalise(processo);
				
				criarNovaAnaliseJuridica(analise);
				
				deveTramitar = true;
				
			} else {
				
				if(processo.caracterizacoes == null)
					processo.caracterizacoes = new ArrayList<>();
				
				processo.caracterizacoes.add(caracterizacao);
				
				processo._save();
				
			}
			
			new LicenciamentoWebService().adicionarCaracterizacaoEmAnalise(caracterizacao);
			
			if(deveTramitar)
				processo.save();
			
			commitTransaction();
			
		} catch (Exception e) {
			
			Logger.error(e, e.getMessage());
			rollbackTransaction();
		}
		
	}
	
	private Processo criarNovoProcesso(Caracterizacao caracterizacao) {
		
		Processo processo = new Processo();
		processo.numero = caracterizacao.numeroProcesso;
		processo.empreendimento = caracterizacao.empreendimento;
		
		processo.caracterizacoes = new ArrayList<>();
		processo.caracterizacoes.add(caracterizacao);
		
		processo._save();
		
		return processo;
		
	}
	
	private Analise criarNovaAnalise(Processo processo) {
		
		Analise analise = new Analise();
		analise.processo = processo;
		analise.dataCadastro = new Date();
		
		analise.save();
		
		return analise;
		
	}
	
	private AnaliseJuridica criarNovaAnaliseJuridica(Analise analise) {

		AnaliseJuridica analiseJuridica = new AnaliseJuridica();
		analiseJuridica.analise = analise;
		
		analiseJuridica.save();
		
		return analiseJuridica;
	}

}
