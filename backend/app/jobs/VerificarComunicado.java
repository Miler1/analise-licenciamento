package jobs;

import models.*;

import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import play.Logger;
import play.jobs.On;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@On("cron.verificarComunicado")
public class VerificarComunicado extends GenericJob {

	@Override
	public void executar() throws Exception {
		
		Logger.info("[INICIO-JOB] ::VerificarComunicado:: [INICIO-JOB]");
		verificarStatusComunicado();
		Logger.info("[FIM-JOB] ::VerificarComunicado:: [FIM-JOB]");
		
	}

	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;

	}
	
	public void verificarStatusComunicado() {

		List<Comunicado> comunicadosNaoResolvidos = Comunicado.findAll();

		List <AnaliseGeo> analisesGeoComunicado = comunicadosNaoResolvidos.stream().filter(comunicado -> comunicado.ativo).map(comunicado -> comunicado.analiseGeo).filter(distinctByKey(analiseGeo -> analiseGeo.id)).collect(Collectors.toList());

		for (AnaliseGeo analiseGeo:analisesGeoComunicado) {

			Boolean podeTramitar = true;

			List<Comunicado> comunicadosAnalise = Comunicado.findByAnaliseGeo(analiseGeo.id);

			for (Comunicado comunicado: comunicadosAnalise) {

				if(comunicado.ativo) {

					if (VencimentoPrazo(comunicado.dataVencimento, new Date()) && !comunicado.resolvido) {

						podeTramitar = false;

					} else {

						comunicado.ativo = false;
						comunicado.validateAndSave();

					}

				}

			}

			if(podeTramitar) {

				AnalistaGeo analistaGeo = AnalistaGeo.findByAnaliseGeo(analiseGeo.id);

				Gerente gerente = Gerente.distribuicaoAutomaticaGerente(analiseGeo.analise.processo.caracterizacao.atividadesCaracterizacao.get(0).atividade.siglaSetor, analiseGeo);

				analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.RESOLVER_COMUNICADO, analistaGeo.usuario, gerente.usuario);
				HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), analiseGeo);

				gerente.save();

			}
				
		}

	}

	public Boolean VencimentoPrazo(Date dataInicio, Date dataFim) {

		LocalDate dataVencimento = new LocalDate(dataInicio.getTime());
		LocalDate dataAtual = new LocalDate(dataFim.getTime());

		// Prazo de 30 dias para resposta do orgão venceu
		if(dataVencimento.isBefore(dataAtual)) {
			return false;
		}

		// Prazo de 30 dias para resposta do orgão ainda não venceu
		return true;

	}

}
