package jobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.*;
import org.hibernate.Session;

import models.licenciamento.Caracterizacao;
import models.licenciamento.LicenciamentoWebService;
import play.Logger;
import play.db.jpa.JPA;
import play.jobs.On;
import utils.Configuracoes;
import utils.ListUtil;

@On("cron.processamentoCaracterizacoesEmAndamento")
public class ProcessamentoCaracterizacaoEmAndamento extends GenericJob {

	@Override
	public void executar() throws Exception {

		Logger.info("[INICIO-JOB] ::ProcessamentoCaracterizacaoEmAndamento:: [INICIO-JOB]");
		
		LicenciamentoWebService licenciamentoWS = new LicenciamentoWebService();

		// Licenças com status EM_ANALISE
		List<Caracterizacao> caracterizacoes = licenciamentoWS.getCaracterizacoesEmAndamento();
		
		for(Caracterizacao caracterizacao : caracterizacoes) {
			
			processarCaracterizacao(caracterizacao);
			
		}
		
		Long[] ids = new ListUtil().getIdsAsArray(caracterizacoes);
		licenciamentoWS.adicionarCaracterizacoesEmAnalise(ids);

		Logger.info("[FIM-JOB] ::ProcessamentoCaracterizacaoEmAndamento:: [FIM-JOB]");
	}
	
	private void processarCaracterizacao(Caracterizacao caracterizacao) {

		Logger.info("ProcessamentoCaracterizacaoEmAndamento:: Processando " + caracterizacao.numeroProcesso);
		
		Processo processo = Processo.find("byNumero", caracterizacao.numeroProcesso).first();
		
		boolean deveTramitar = false;


		if(processo == null) {
			
			processo = criarNovoProcesso(caracterizacao);

			Analise analise = criarNovaAnalise(processo);

			if (caracterizacao.renovacao) {

				clonarAnaliseJuridica(analise, caracterizacao);

			} else {

				criarNovaAnaliseJuridica(analise);
			}

			criarNovoDiasAnalise(analise);
			
			deveTramitar = true;

		} else if(processo.caracterizacoes.contains(caracterizacao)) {
			
			return;
			
		} else {
			
			if(processo.caracterizacoes == null)
				processo.caracterizacoes = new ArrayList<>();
			
			processo.caracterizacoes.add(caracterizacao);

			processo._save();
		}
		
		if(deveTramitar)
			processo.save();
		
		commitTransaction();
		
	}
	
	private Processo criarNovoProcesso(Caracterizacao caracterizacao) {
		
		Processo processo = new Processo();
		processo.numero = caracterizacao.numeroProcesso;
		processo.empreendimento = caracterizacao.empreendimento;
		processo.dataCadastro = new Date();
		
		processo.caracterizacoes = new ArrayList<>();
		processo.caracterizacoes.add(caracterizacao);
		
		processo._save();

		return processo;
		
	}
	
	private Analise criarNovaAnalise(Processo processo) {
		
		Analise analise = new Analise();
		analise.processo = processo;
		analise.dataCadastro = new Date();
		analise.ativo = true;
		analise.temNotificacaoAberta = false;

		analise.save();
		
		return analise;
		
	}

	private AnaliseJuridica clonarAnaliseJuridica(Analise analise, Caracterizacao caracterizacao) {

		Processo processoAntigo = Processo.find("byIdCaracterizacao", caracterizacao.id).first();

		AnaliseJuridica AnaliseJuridicaAntiga = processoAntigo.getAnalise().getAnaliseJuridica();

		AnaliseJuridica analiseJuridica = new AnaliseJuridica();

		try {

			analiseJuridica = AnaliseJuridicaAntiga.clone();

		} catch (CloneNotSupportedException e) {

			e.printStackTrace();
		}

		analiseJuridica.analise = analise;
		analiseJuridica.dataVencimentoPrazo = new Date();
		analiseJuridica.revisaoSolicitada = false;
		analiseJuridica.notificacaoAtendida = false;
		analiseJuridica.ativo = true;
		analiseJuridica.analiseJuridicaRevisada = null;
		analiseJuridica.dataInicio = new Date();
		analiseJuridica.dataFim = new Date();

		analiseJuridica._save();

		return analiseJuridica;
	}

	private AnaliseJuridica criarNovaAnaliseJuridica(Analise analise) {

		AnaliseJuridica analiseJuridica = new AnaliseJuridica();
		analiseJuridica.analise = analise;
		
		analiseJuridica.save();
		
		return analiseJuridica;
	}
	
	private DiasAnalise criarNovoDiasAnalise(Analise analise) {
		
		DiasAnalise diasAnalise = new DiasAnalise(analise);
		analise.diasAnalise = diasAnalise;
		
		diasAnalise.save();
		
		return diasAnalise;
	}

}
