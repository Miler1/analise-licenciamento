package jobs;

import models.*;
import models.licenciamento.Caracterizacao;
import models.licenciamento.LicenciamentoWebService;
import play.Logger;
import play.jobs.On;
import utils.ListUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@On("cron.processamentoCaracterizacoesEmAndamento")
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
	
	private void processarCaracterizacao(Caracterizacao caracterizacao) {

		Logger.info("ProcessamentoCaracterizacaoEmAndamento:: Processando " + caracterizacao.numero);

		Processo processo = Processo.find("id_caracterizacao", caracterizacao.id).first();
		Processo processoAntigo;
		Analise analise;
		AnaliseGeo analiseGeo;

		boolean deveTramitar = false;

		if (processo == null) {
			
			processo = criarNovoProcesso(caracterizacao);

			analise = criarNovaAnalise(processo);

			if (caracterizacao.renovacao || caracterizacao.retificacao) {

				Caracterizacao caracterizacaoAnterior = Caracterizacao.findById(caracterizacao.idCaracterizacaoOrigem);
				processoAntigo = Processo.find("numero ORDER BY id DESC", caracterizacaoAnterior.numero).first();
				processo.processoAnterior = processoAntigo;
				processo.renovacao = caracterizacao.renovacao;

			}

			criarNovoDiasAnalise(analise);
			analiseGeo = criarNovaAnaliseGeo(analise);

			if(analiseGeo == null) {

				rollbackTransaction();

			}

			deveTramitar = analiseGeo != null;

		} else if(processo.caracterizacao.id.equals(caracterizacao.id)) {
			
			return;
			
		} else {
			
			processo.caracterizacao = caracterizacao;

			processo._save();
		}
		
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
