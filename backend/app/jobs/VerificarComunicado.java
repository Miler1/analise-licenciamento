package jobs;

import br.ufla.lemaf.beans.pessoa.Contato;
import models.*;

import models.licenciamento.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import org.joda.time.LocalDate;
import play.Logger;
import play.jobs.On;
import security.cadastrounificado.CadastroUnificadoWS;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
	
	public void verificarStatusComunicado() throws Exception {

		List<Comunicado> comunicadosNaoResolvidos = Comunicado.findAll();

		List <AnaliseGeo> analisesGeoComunicado = comunicadosNaoResolvidos.stream().filter(comunicado -> comunicado.ativo && comunicado.aguardandoResposta).map(comunicado -> comunicado.analiseGeo).filter(distinctByKey(analiseGeo -> analiseGeo.id)).collect(Collectors.toList());

		for (AnaliseGeo analiseGeo:analisesGeoComunicado) {

			boolean podeTramitar = true;
			boolean notificarInteressado = false;

			List<Comunicado> comunicadosAnalise = Comunicado.findByAnaliseGeo(analiseGeo.id);

			for (Comunicado comunicado: comunicadosAnalise) {

				if(comunicado.ativo) {

					if (comunicado.verificaTipoSobreposicaoComunicado(comunicado.sobreposicaoCaracterizacaoEmpreendimento) ||
							comunicado.verificaTipoSobreposicaoComunicado(comunicado.sobreposicaoCaracterizacaoAtividade) ||
							comunicado.verificaTipoSobreposicaoComunicado(comunicado.sobreposicaoCaracterizacaoComplexo)) {

						notificarInteressado = true;

					}

					List<ParecerAnalistaGeo> pareceresAnalistaGeo = ParecerAnalistaGeo.find("id_analise_geo", analiseGeo.id).fetch();

					ParecerAnalistaGeo ultimoParecer = pareceresAnalistaGeo.stream().sorted(Comparator.comparing(ParecerAnalistaGeo::getDataParecer).reversed()).collect(Collectors.toList()).get(0);

					if (vencimentoPrazo(comunicado.dataVencimento, new Date()) && !comunicado.resolvido) {

						podeTramitar = false;

					} else if (!vencimentoPrazo(comunicado.dataVencimento, new Date()) && !comunicado.resolvido && !notificarInteressado) {

						comunicado.ativo = false;
						comunicado.segundoEmailEnviado = true;
						comunicado.aguardandoResposta = false;
						comunicado.validateAndSave();

						List<String> destinatarios = new ArrayList<String>();
						destinatarios.add(comunicado.orgao.email);

						analiseGeo.reenviarEmailComunicado(ultimoParecer, comunicado, destinatarios);

					} else if(!vencimentoPrazo(comunicado.dataVencimento, new Date()) && !comunicado.resolvido && notificarInteressado) {

						podeTramitar = false;

						if (!comunicado.interessadoNotificado) {

							Empreendimento empreendimento = Empreendimento.findById(analiseGeo.analise.processo.empreendimento.id);

							Contato emailCadastrante =  CadastroUnificadoWS.ws.getPessoa(empreendimento.cpfCnpjCadastrante).contatos.stream()
									.filter(contato -> contato.principal == true && contato.tipo.descricao.equals("Email")).findFirst().orElseThrow(null);

							List<String> interessados = new ArrayList<>(Collections.singleton(emailCadastrante.valor));

							if (comunicado.sobreposicaoCaracterizacaoEmpreendimento != null) {

								analiseGeo.enviarNotificacaoInteressado(ultimoParecer, interessados, comunicado.caracterizacao, comunicado);

							} else if (comunicado.sobreposicaoCaracterizacaoAtividade != null) {

								analiseGeo.enviarNotificacaoInteressado(ultimoParecer, interessados, comunicado.caracterizacao, comunicado);

							} else if (comunicado.sobreposicaoCaracterizacaoComplexo != null) {

								analiseGeo.enviarNotificacaoInteressado(ultimoParecer, interessados, comunicado.caracterizacao, comunicado);

							}

						}

						comunicado.dataVencimento = analiseGeo.analise.dataVencimentoPrazo;
						comunicado.interessadoNotificado = true;
						comunicado.validateAndSave();
					}
					else {

						comunicado.ativo = false;
						comunicado.aguardandoResposta = false;
						comunicado.validateAndSave();
					}

				}

			}

			if(podeTramitar) {

				AnalistaGeo analistaGeo = AnalistaGeo.findByAnaliseGeo(analiseGeo.id);

				CoordenadorGeo coordenador = CoordenadorGeo.distribuicaoAutomaticaCoordenador(analiseGeo.analise.processo.caracterizacao.atividadesCaracterizacao.get(0).atividade.siglaSetor, analiseGeo);

				analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.RESOLVER_COMUNICADO, analistaGeo.usuario, coordenador.usuario);
				HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), analiseGeo);

				coordenador.save();

			}
				
		}

	}

	public Boolean vencimentoPrazo(Date dataInicio, Date dataFim) {

		LocalDate dataVencimento = new LocalDate(dataInicio.getTime());
		LocalDate dataAtual = new LocalDate(dataFim.getTime());

		if(dataVencimento.isBefore(dataAtual)) {

			return false;

		}

		return true;

	}

}
