package jobs;

import models.*;
import models.licenciamento.Caracterizacao;
import models.licenciamento.LicenciamentoWebService;
import models.licenciamento.StatusCaracterizacao;
import models.tramitacao.AcaoTramitacao;
import play.Logger;
import play.jobs.On;
import utils.ListUtil;

import java.util.*;

@On("cron.processamentoCaracterizacoesEmAndamento")
public class ProcessamentoCaracterizacaoEmAndamento extends GenericJob {

	private List<Caracterizacao> caracterizacoesProcessadas;

	@Override
	public void executar() {

		Logger.info("[INICIO-JOB] ::ProcessamentoCaracterizacaoEmAndamento:: [INICIO-JOB]");

		LicenciamentoWebService licenciamentoWS = new LicenciamentoWebService();

		// Licenças com status EM_ANALISE
		List<Caracterizacao> caracterizacoes = licenciamentoWS.getCaracterizacoesEmAndamento();

		UsuarioAnalise.atualizaUsuariosAnalise();
		caracterizacoesProcessadas = new ArrayList<>();

		caracterizacoes.forEach(this::processarCaracterizacao);

		if(!caracterizacoesProcessadas.isEmpty()) {
			licenciamentoWS.adicionarCaracterizacoesEmAnalise(ListUtil.getIdsAsArray(caracterizacoesProcessadas));
		}

		Logger.info("[FIM-JOB] ::ProcessamentoCaracterizacaoEmAndamento:: [FIM-JOB]");

	}

	private Long coalesce(Long ...ids){
		return Arrays.stream(ids).filter(Objects::nonNull).findFirst().orElse(null);
	}
	
	private void processarCaracterizacao(Caracterizacao caracterizacao) {

		Logger.info("ProcessamentoCaracterizacaoEmAndamento:: Processando " + caracterizacao.numero);

		Boolean renovacao = caracterizacao.isRenovacao();
		Boolean retificacao = caracterizacao.isRetificacao();
		Processo processoAnterior = null;

		if(renovacao || retificacao){

			// Carrega processo anterior
			Caracterizacao anterior = Caracterizacao.findById(coalesce(caracterizacao.idCaracterizacaoOrigem, caracterizacao.id));
			processoAnterior = Processo.find("numero = :num ORDER BY id DESC")
					.setParameter("num", anterior.numero).first();

			// Arquiva o processo anterior
			if (retificacao) {
				Analise analiseAntiga = Analise.findByProcessoAtivo(processoAnterior);
				analiseAntiga.processo.tramitacao.tramitar(processoAnterior, AcaoTramitacao.ARQUIVAR_PROTOCOLO);
			}

		}

		Processo processo = criarNovoProcesso(caracterizacao);
		Analise analise = criarNovaAnalise(processo);
		processo.processoAnterior = processoAnterior;
		processo.renovacao = renovacao;

		criarNovoDiasAnalise(analise);

		if(!criaAnaliseGeoVinculandoAnalistaGeo(analise, caracterizacao.status)) {

			rollbackTransaction();
			return;

		}

		processo.save();
		caracterizacoesProcessadas.add(caracterizacao);
		commitTransaction();

	}
	
	private Processo criarNovoProcesso(Caracterizacao caracterizacao) {
		
		Processo processo = new Processo();
		processo.numero = caracterizacao.numero;
		processo.empreendimento = caracterizacao.empreendimento;
		processo.dataCadastro = new Date();
		processo.caracterizacao = caracterizacao;

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

	private boolean criaAnaliseGeoVinculandoAnalistaGeo(Analise analise, StatusCaracterizacao status) {

		AnaliseGeo analiseGeo = new AnaliseGeo();
		analiseGeo.analise = analise;

		String siglaSetor = analise.processo.caracterizacao.atividadesCaracterizacao.get(0).atividade.siglaSetor;

		AnalistaGeo analistaGeo;

		if(status.id.equals(StatusCaracterizacao.NOTIFICACAO_ATENDIDA)) {

			AnaliseGeo analiseGeoAnterior = AnaliseGeo.findById(analise.processo.processoAnterior.analise.analiseGeo.id);
			analiseGeoAnterior.ativo = false;
			analiseGeoAnterior._save();

			analistaGeo = analiseGeoAnterior.getAnalistaGeo();
			analistaGeo = new AnalistaGeo(analiseGeo, analistaGeo.usuario);


		} else {

			analistaGeo = AnalistaGeo.distribuicaoProcesso(siglaSetor, analiseGeo);

		}

		if(analistaGeo == null) {
			return false;
		}

		analiseGeo.analistasGeo = new ArrayList<>();
		analiseGeo.analistasGeo.add(analistaGeo);
		analiseGeo.save();
		
		return true;

	}
	
	private DiasAnalise criarNovoDiasAnalise(Analise analise) {
		
		DiasAnalise diasAnalise = new DiasAnalise(analise);
		analise.diasAnalise = diasAnalise;

		diasAnalise.save();
		
		return diasAnalise;
	}

}
