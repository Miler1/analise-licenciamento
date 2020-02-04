package jobs;

import models.*;
import models.licenciamento.Caracterizacao;
import models.licenciamento.LicenciamentoWebService;
import models.licenciamento.StatusCaracterizacao;
import models.licenciamento.TipoAnalise;
import models.tramitacao.AcaoTramitacao;
import models.validacaoParecer.Analisavel;
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
		}

		Processo processo = criarNovoProcesso(caracterizacao, processoAnterior, renovacao, retificacao);
		Analise analise = criarNovaAnalise(processo);

		criarNovoDiasAnalise(analise);

		if(caracterizacao.status.id.equals(StatusCaracterizacao.NOTIFICACAO_ATENDIDA)){

			Analise analiseAntiga = Analise.findByProcessoAtivo(processo.processoAnterior);
			Analisavel analisavel = analiseAntiga.buscarAnalisavelAtual();

			if(analisavel.getTipoAnalise().equals(TipoAnalise.GEO)){

				AnaliseGeo analiseGeoAnterior = AnaliseGeo.findById(analise.processo.processoAnterior.analise.analiseGeo.id);
				criaAnaliseGeo(analise, analiseGeoAnterior.getAnalistaGeo());

			} else if(analisavel.getTipoAnalise().equals(TipoAnalise.TECNICA)){

				Boolean retificacaoComGeo = analiseAntiga.getAnaliseTecnica().notificacoes.get(0).retificacaoSolicitacaoComGeo;

				if(retificacao && retificacaoComGeo != null && retificacaoComGeo){

					AnaliseGeo analiseGeoAnterior = AnaliseGeo.findUltimaByAnalise(analise);
					criaAnaliseGeo(analise, analiseGeoAnterior.getAnalistaGeo());

				} else {

					AnaliseTecnica analiseTecnicaAnterior = AnaliseTecnica.findById(analise.processo.processoAnterior.analise.analiseTecnica.id);
					criaAnaliseTecnica(analise, analiseTecnicaAnterior.analistaTecnico);
				}
			}

		} else {
			criaAnaliseGeo(analise);
		}

		processo.save();

		// Arquiva o processo anterior
		if (retificacao) {
			Analise analiseAntiga = Analise.findByProcessoAtivo(processoAnterior);
			analiseAntiga.processo.tramitacao.tramitar(processoAnterior, AcaoTramitacao.ARQUIVAR_PROTOCOLO);
		}

		if(caracterizacao.status.id.equals(StatusCaracterizacao.NOTIFICACAO_ATENDIDA)){

			Analise analiseAntiga = Analise.findByProcessoAtivo(processo.processoAnterior);
			Analisavel analisavel = analiseAntiga.buscarAnalisavelAtual();

			if(analisavel.getTipoAnalise().equals(TipoAnalise.TECNICA)){

				Boolean retificacaoComGeo = analiseAntiga.getAnaliseTecnica().notificacoes.get(0).retificacaoSolicitacaoComGeo;

				if(retificacao && (retificacaoComGeo == null || !retificacaoComGeo)){

					AnaliseTecnica analiseTecnicaAnterior = AnaliseTecnica.findById(analise.processo.processoAnterior.analise.analiseTecnica.id);
					processo.tramitacao.tramitar(processo, AcaoTramitacao.INICIAR_ANALISE_TECNICA_POR_VOLTA_DE_NOTIFICACAO, null, analiseTecnicaAnterior.analistaTecnico.usuario);

				}
			}

			inativaAnaliseAnterior(analiseAntiga);
		}

		caracterizacoesProcessadas.add(caracterizacao);

		commitTransaction();

	}
	
	private Processo criarNovoProcesso(Caracterizacao caracterizacao, Processo processoAnterior, Boolean renovacao, Boolean retificacao) {
		
		Processo processo = new Processo();
		processo.numero = caracterizacao.numero;
		processo.empreendimento = caracterizacao.empreendimento;
		processo.dataCadastro = new Date();
		processo.caracterizacao = caracterizacao;
		processo.processoAnterior = processoAnterior;
		processo.renovacao = renovacao;
		processo.retificacao = retificacao;
		processo.ativo = true;

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

	private void criaAnaliseGeo(Analise analise) {
		criaAnaliseGeo(analise,null);
	}

	private void criaAnaliseGeo(Analise analise, AnalistaGeo analistaGeo) {

		AnaliseGeo analiseGeo = new AnaliseGeo();
		analiseGeo.analise = analise;
		AnalistaGeo analista = new AnalistaGeo();

		if(analistaGeo == null){
			String siglaSetor = analise.processo.caracterizacao.atividadesCaracterizacao.get(0).atividade.siglaSetor;
			analista = AnalistaGeo.distribuicaoProcesso(siglaSetor, analiseGeo);
		} else {
			analista.usuario = analistaGeo.usuario;
			analista.analiseGeo = analiseGeo;
			analista.dataVinculacao = new Date();
		}

		analiseGeo.analistasGeo = Collections.singletonList(analista);
		analiseGeo.save();
	}

	private void criaAnaliseTecnica(Analise analise) {
		criaAnaliseTecnica(analise,null);
	}

	private void criaAnaliseTecnica(Analise analise, AnalistaTecnico analistaTecnico) {

		AnaliseTecnica analiseTecnica = new AnaliseTecnica();
		analiseTecnica.analise = analise;
		AnalistaTecnico analista = new AnalistaTecnico();

		if(analistaTecnico == null){
			String siglaSetor = analise.processo.caracterizacao.atividadesCaracterizacao.get(0).atividade.siglaSetor;
            analista = AnalistaTecnico.distribuicaoAutomaticaAnalistaTecnico(siglaSetor, analise);
		} else {
			analista.usuario = analistaTecnico.usuario;
			analista.analiseTecnica = analiseTecnica;
			analista.dataVinculacao = new Date();
		}

		analiseTecnica.analistaTecnico = analista;
		analiseTecnica.save();
	}
	
	private DiasAnalise criarNovoDiasAnalise(Analise analise) {
		
		DiasAnalise diasAnalise = new DiasAnalise(analise);
		analise.diasAnalise = diasAnalise;

		diasAnalise.save();
		
		return diasAnalise;
	}

	private void inativaAnaliseAnterior(Analise analiseAnterior){
		analiseAnterior.ativo = false;
		Processo processoAnterior = analiseAnterior.processo;
		processoAnterior.ativo = false;
		analiseAnterior._save();
		processoAnterior._save();
	}

}
