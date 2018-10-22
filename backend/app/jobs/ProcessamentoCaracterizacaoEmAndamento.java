package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.*;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Licenca;
import models.licenciamento.LicenciamentoWebService;
import models.tramitacao.AcaoTramitacao;
import play.Logger;
import play.jobs.On;
import utils.ListUtil;

@On("cron.processamentoCaracterizacoesEmAndamento")
public class ProcessamentoCaracterizacaoEmAndamento extends GenericJob {

	@Override
	public void executar() {

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
		Processo processoAntigo = null;
		Analise analise = null;

		boolean deveTramitar = false;


		if (processo == null) {
			
			processo = criarNovoProcesso(caracterizacao);

			analise = criarNovaAnalise(processo);


			if (caracterizacao.renovacao) {

				processoAntigo = Processo.find("numero", caracterizacao.numeroProcessoAntigo).first();
				processo.processoAnterior = processoAntigo;
				processo.renovacao = true;

				if (!caracterizacao.empreendimento.houveAlteracoes) {

					clonarAnaliseJuridica(analise, processoAntigo);

				} else {

					criarNovaAnaliseJuridica(analise);
				}

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
		
		if (deveTramitar) {

			processo.save();

			if (caracterizacao.renovacao) {

				if (processo.isProrrogacao()) {

					if (processoAntigo.tramitacao.isAcaoDisponivel(AcaoTramitacao.PRORROGAR_LICENCA, processoAntigo)
							&& processoAntigo.isArquivavel()) {

						processoAntigo.tramitacao.tramitar(processoAntigo, AcaoTramitacao.PRORROGAR_LICENCA);
					}

					Licenca.prorrogar(caracterizacao.getLicenca().id);

				} else {

					if (processoAntigo.tramitacao.isAcaoDisponivel(AcaoTramitacao.ARQUIVAR_POR_RENOVACAO, processoAntigo)
							&& processoAntigo.isArquivavel()) {

						processoAntigo.tramitacao.tramitar(processoAntigo, AcaoTramitacao.ARQUIVAR_POR_RENOVACAO);
					}
				}

				if (!caracterizacao.empreendimento.houveAlteracoes) {

					processo.tramitacao.tramitar(processo, AcaoTramitacao.RENOVAR_SEM_ALTERACAO);

					AnaliseTecnica analiseTecnica = new AnaliseTecnica();
					analiseTecnica.analise = analise;
					analiseTecnica.save();
				}
			}
		}

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

	private AnaliseJuridica clonarAnaliseJuridica(Analise analise, Processo processoAntigo) {

		AnaliseJuridica analiseJuridicaAntiga = processoAntigo.getAnalise().getAnaliseJuridica();
		AnaliseJuridica analiseJuridica = new AnaliseJuridica();

		List<AnaliseDocumento> analisesDocumentos = new ArrayList<>();
		List<ConsultorJuridico> consultoresJuridicos = new ArrayList<>();

		analiseJuridica.id = null;
		analiseJuridica.analise = analise;
		analiseJuridica.parecer = analiseJuridicaAntiga.parecer;
		analiseJuridica.dataVencimentoPrazo = new Date();
		analiseJuridica.revisaoSolicitada = false;
		analiseJuridica.notificacaoAtendida = false;
		analiseJuridica.ativo = true;
		analiseJuridica.analiseJuridicaRevisada = null;
		analiseJuridica.dataInicio = new Date();
		analiseJuridica.dataFim = new Date();
		analiseJuridica.tipoResultadoAnalise = analiseJuridicaAntiga.tipoResultadoAnalise;
		analiseJuridica.tipoResultadoValidacao = analiseJuridicaAntiga.tipoResultadoValidacao;
		analiseJuridica.parecerValidacao = analiseJuridicaAntiga.parecerValidacao;
		analiseJuridica.usuarioValidacao = analiseJuridicaAntiga.usuarioValidacao;
		analiseJuridica.tipoResultadoValidacaoAprovador = analiseJuridicaAntiga.tipoResultadoValidacaoAprovador;
		analiseJuridica.parecerValidacaoAprovador = analiseJuridicaAntiga.parecerValidacaoAprovador;
		analiseJuridica.usuarioValidacaoAprovador = analiseJuridicaAntiga.usuarioValidacaoAprovador;

		for (AnaliseDocumento analiseDocumentoAntigo : analiseJuridicaAntiga.analisesDocumentos) {

			AnaliseDocumento analiseDocumento = new AnaliseDocumento();
			analiseDocumento.id = null;
			analiseDocumento.analiseJuridica = analiseJuridica;
			analiseDocumento.validado = analiseDocumentoAntigo.validado;
			analiseDocumento.parecer = analiseDocumentoAntigo.parecer;
			analiseDocumento.analiseTecnica = null;
			analiseDocumento.documento = analiseDocumentoAntigo.documento;
			analiseDocumento.analiseDocumentoAnterior = analiseDocumentoAntigo.analiseDocumentoAnterior;
			analisesDocumentos.add(analiseDocumento);
		}

		for (ConsultorJuridico consultorJuridicoAntigo : analiseJuridicaAntiga.consultoresJuridicos) {

			ConsultorJuridico consultorJuridico = new ConsultorJuridico();
			consultorJuridico.id = null;
			consultorJuridico.analiseJuridica = analiseJuridica;
			consultorJuridico.usuario = consultorJuridicoAntigo.usuario;
			consultorJuridico.dataVinculacao = consultorJuridicoAntigo.dataVinculacao;
			consultoresJuridicos.add(consultorJuridico);
		}

		analiseJuridica.analisesDocumentos = analisesDocumentos;
		analiseJuridica.consultoresJuridicos = consultoresJuridicos;
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
