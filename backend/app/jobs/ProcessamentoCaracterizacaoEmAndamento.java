package jobs;

import models.*;
import models.licenciamento.Caracterizacao;
import models.licenciamento.LicenciamentoWebService;
import models.tramitacao.AcaoTramitacao;
import play.Logger;
import play.jobs.On;
import play.jobs.OnApplicationStart;
import utils.ListUtil;

import java.util.*;

@On("cron.processamentoCaracterizacoesEmAndamento")
@OnApplicationStart
public class ProcessamentoCaracterizacaoEmAndamento extends GenericJob {

	@Override
	public void executar() {

		Logger.info("[INICIO-JOB] ::ProcessamentoCaracterizacaoEmAndamento:: [INICIO-JOB]");

		LicenciamentoWebService licenciamentoWS = new LicenciamentoWebService();

		// Licenças com status EM_ANALISE
		List<Caracterizacao> caracterizacoes = licenciamentoWS.getCaracterizacoesEmAndamento();

		caracterizacoes.forEach(this::processarCaracterizacao);

		licenciamentoWS.adicionarCaracterizacoesEmAnalise(ListUtil.getIdsAsArray(caracterizacoes));

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
					.setParameter("num",anterior.numero).first();

			// Arquiva o processo anterior
			if (retificacao) {
				Analise analiseAntiga = Analise.findByProcesso(processoAnterior);
				analiseAntiga.processo.tramitacao.tramitar(processoAnterior, AcaoTramitacao.ARQUIVAR_PROTOCOLO);
			}

		}

		Processo processo = criarNovoProcesso(caracterizacao);
		Analise analise = criarNovaAnalise(processo);
		processo.processoAnterior = processoAnterior;
		processo.renovacao = renovacao;

		criarNovoDiasAnalise(analise);
		AnaliseGeo analiseGeo = criarNovaAnaliseGeo(analise);

		if(analiseGeo == null) {

			rollbackTransaction();

		}

		Boolean deveTramitar = analiseGeo != null;
		
		if (deveTramitar) {

			processo.save();

			// TODO: VERIFICAR CÓDIGO COMENTADO ABAIXO

//			if (caracterizacao.renovacao) {
//
//				if (processo.isProrrogacao()) {
//
//					if (processoAntigo.tramitacao.isAcaoDisponivel(AcaoTramitacao.PRORROGAR_LICENCA, processoAntigo)
//							&& processoAntigo.isArquivavel()) {
//
//						processoAntigo.tramitacao.tramitar(processoAntigo, AcaoTramitacao.PRORROGAR_LICENCA);
//
//					}
//
//					Licenca.prorrogar(caracterizacao.getLicenca().id);
//
//				} else {
//
//					if (processoAntigo.tramitacao.isAcaoDisponivel(AcaoTramitacao.ARQUIVAR_POR_RENOVACAO, processoAntigo)
//							&& processoAntigo.isArquivavel()) {
//
//						processoAntigo.tramitacao.tramitar(processoAntigo, AcaoTramitacao.ARQUIVAR_POR_RENOVACAO);
//
//					}
//
//				}
//
//			}

		}

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

	private AnaliseGeo clonarAnaliseGeo(Analise analise, Processo processoAntigo) {

		AnaliseGeo analiseGeoAntiga = processoAntigo.analise.getAnaliseGeo();
		AnaliseGeo analiseGeo = new AnaliseGeo();

		List<AnaliseDocumento> analisesDocumentos = new ArrayList<>();
		List<Gerente> gerentes = new ArrayList<>();

		analiseGeo.id = null;
		analiseGeo.analise = analise;
		analiseGeo.dataVencimentoPrazo = new Date();
		analiseGeo.revisaoSolicitada = false;
		analiseGeo.notificacaoAtendida = false;
		analiseGeo.ativo = true;
		analiseGeo.analiseGeoRevisada = null;
		analiseGeo.dataInicio = new Date();
		analiseGeo.dataFim = new Date();
		analiseGeo.dataCadastro = new Date();
		analiseGeo.tipoResultadoValidacao = analiseGeoAntiga.tipoResultadoValidacao;
		analiseGeo.parecerValidacao = analiseGeoAntiga.parecerValidacao;
		analiseGeo.usuarioValidacao = analiseGeoAntiga.usuarioValidacao;
		analiseGeo.tipoResultadoValidacaoAprovador = analiseGeoAntiga.tipoResultadoValidacaoAprovador;
		analiseGeo.parecerValidacaoAprovador = analiseGeoAntiga.parecerValidacaoAprovador;
		analiseGeo.usuarioValidacaoAprovador = analiseGeoAntiga.usuarioValidacaoAprovador;

		for (AnaliseDocumento analiseDocumentoAntigo : analiseGeoAntiga.analisesDocumentos) {

			AnaliseDocumento analiseDocumento = new AnaliseDocumento();
			analiseDocumento.id = null;
			analiseDocumento.analiseGeo = analiseGeo;
			analiseDocumento.validado = analiseDocumentoAntigo.validado;
			analiseDocumento.parecer = analiseDocumentoAntigo.parecer;
			analiseDocumento.analiseTecnica = null;
			analiseDocumento.documento = analiseDocumentoAntigo.documento;
			analiseDocumento.analiseDocumentoAnterior = analiseDocumentoAntigo.analiseDocumentoAnterior;
			analisesDocumentos.add(analiseDocumento);
		}

		for (Gerente gerenteAntigo : analiseGeoAntiga.gerentes) {

			Gerente gerente = new Gerente();
			gerente.id = null;
			gerente.analiseGeo = analiseGeo;
			gerente.usuario = gerenteAntigo.usuario;
			gerente.dataVinculacao = gerenteAntigo.dataVinculacao;
			gerentes.add(gerente);
		}

		analiseGeo.analisesDocumentos = analisesDocumentos;
		analiseGeo.gerentes = gerentes;
		analiseGeo._save();

		return analiseGeo;
	}

	private AnaliseGeo criarNovaAnaliseGeo(Analise analise) {

		AnaliseGeo analiseGeo = new AnaliseGeo();
		analiseGeo.analise = analise;

		String siglaSetor = analise.processo.caracterizacao.atividadesCaracterizacao.get(0).atividade.siglaSetor;

		AnalistaGeo analistaGeo = AnalistaGeo.distribuicaoProcesso(siglaSetor, analiseGeo);

		if(analistaGeo == null) {
			return null;
		}

		analiseGeo.analistasGeo = new ArrayList<>();

		analiseGeo.analistasGeo.add(analistaGeo);

		analiseGeo.save();
		
		return analiseGeo;
	}
	
	private DiasAnalise criarNovoDiasAnalise(Analise analise) {
		
		DiasAnalise diasAnalise = new DiasAnalise(analise);
		analise.diasAnalise = diasAnalise;
		
		diasAnalise.save();
		
		return diasAnalise;
	}

}
