package jobs;


import exceptions.AppException;
import models.*;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Licenca;
import models.licenciamento.LicenciamentoWebService;
import models.licenciamento.StatusCaracterizacao;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.Condicao;
import models.tramitacao.ObjetoTramitavel;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import play.Logger;
import play.jobs.On;
import utils.DateUtil;
import utils.Mensagem;

import java.util.*;

@On("cron.processamentoPrazos")
public class ProcessamentoPrazos extends GenericJob {

	@Override
	public void executar() {

		Logger.info("[INICIO-JOB] ::ProcessamentoPrazos:: [INICIO-JOB]");
		contarDiasAnalise();
		verificaPrazoSuspensao();
		Logger.info("[FIM-JOB] ::ProcessamentoPrazos:: [FIM-JOB]");

	}

	public void contarDiasAnalise() {

		List<Analise> analises = Analise.findAtivas();

		if(!analises.isEmpty()) {

			for (Analise analise : analises) {

				if (!ObjetoTramitavel.validaCondicao(analise.processo.idObjetoTramitavel, Condicao.ARQUIVADO)) {

					DiasAnalise diasAnalise = DiasAnalise.find("analise.id", analise.id).first();
					analise.diasAnalise = diasAnalise;

					if (diasAnalise != null) {

						if (analise.getAnaliseGeo() != null) {

							if (analise.analiseGeo.dataFim == null) {

								if (analise.diasAnalise.qtdeDiasGeo == null) {
									analise.diasAnalise.qtdeDiasGeo = 0;
								}

								analise.diasAnalise.preencheDiasAnaliseGeo();

							} else {

								// TODO: VERIFICAR

								if (analise.analiseGeo.dataFimValidacaoAprovador == null) {

									if (analise.diasAnalise.qtdeDiasAprovador == null) {
										analise.diasAnalise.qtdeDiasAprovador = 0;
									}

									int verificaDiasAprovadorCorretos = CalculaDiferencaDias(analise.analiseGeo.dataFim, new Date());
									int verificaDiasCorretos = CalculaDiferencaDias(analise.dataCadastro, new Date());

									if (verificaDiasAprovadorCorretos != diasAnalise.qtdeDiasAprovador) {

										analise.diasAnalise.qtdeDiasAprovador += 1;

									}

									if (verificaDiasCorretos != diasAnalise.qtdeDiasAnalise) {

										analise.diasAnalise.qtdeDiasAnalise += 1;

									}

								}

							}

						} else if (analise.getAnaliseTecnica() != null) {

							if (analise.analiseTecnica.dataFim == null) {

								if (analise.diasAnalise.qtdeDiasTecnica == null) {
									analise.diasAnalise.qtdeDiasTecnica = 0;
								}
								int verificaDiasTecnicosCorretos = CalculaDiferencaDias(analise.analiseTecnica.dataCadastro, new Date());

								if (verificaDiasTecnicosCorretos != diasAnalise.qtdeDiasTecnica) {

									analise.diasAnalise.qtdeDiasTecnica = verificaDiasTecnicosCorretos;
								}

							} else {

								// TODO: VERIFICAR

								if (analise.analiseTecnica.dataFimValidacaoAprovador == null) {

									if (analise.diasAnalise.qtdeDiasAprovador == null) {
										analise.diasAnalise.qtdeDiasAprovador = 0;
									}

									int verificaDiasAprovadorCorretos = CalculaDiferencaDias(analise.analiseTecnica.dataFim, new Date());
									int verificaDiasCorretos = CalculaDiferencaDias(analise.dataCadastro, new Date());

									if (verificaDiasAprovadorCorretos != diasAnalise.qtdeDiasAprovador) {

										analise.diasAnalise.qtdeDiasAprovador += 1;
									}

									if (verificaDiasCorretos != diasAnalise.qtdeDiasAnalise) {

										analise.diasAnalise.qtdeDiasAnalise += 1;
									}

								}
							}


						}

					} else {

						analise.diasAnalise = new DiasAnalise(analise);

					}

					analise.diasAnalise.qtdeDiasAnalise = DateUtil.getDiferencaEmDias(analise.dataCadastro, new Date());
					analise.diasAnalise.save();

				}

			}

		}

	}

	public int CalculaDiferencaDias(Date dataInicial, Date dataFinal) {

		LocalDate dataInicio = new LocalDate(dataInicial.getTime());
		LocalDate dataFim = new LocalDate(dataFinal.getTime());

		return Days.daysBetween(dataInicio, dataFim).getDays();

	}

	public void verificaPrazoSuspensao() {

		Logger.info("[INICIO-JOB] ::ProcessamentoPrazoSuspensao:: [INICIO-JOB]");

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

						Caracterizacao.setStatusCaracterizacao(idsCaracterizacoes, StatusCaracterizacao.DEFERIDO);
						Caracterizacao.setCaracterizacaoEmAnalise(idsCaracterizacoes, false);
						Caracterizacao.setCaracterizacaoEmRenovacao(idsCaracterizacoes, false);

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
		Logger.info("[FIM-JOB] ::ProcessamentoPrazoSuspensao:: [FIM-JOB]");

	}

	private static Date addDays(Date date, int days) {

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);

		return cal.getTime();
	}

}
