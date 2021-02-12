package jobs;


import models.*;
import models.licenciamento.Caracterizacao;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.jobs.On;
import utils.Configuracoes;

import java.util.*;
import java.util.stream.Collectors;

@On("cron.reenviarEmail")
public class ReenvioEmailJob extends GenericJob {

	@Override
	public void executar() throws Exception {
		
		Logger.info("[INICIO-JOB] ::ReenvioEmail:: [INICIO-JOB]");

		List<ReenvioEmail> reenviosEmail = ReenvioEmail.findAll();

		for(ReenvioEmail reenvioEmail : reenviosEmail) {

			sendMail(reenvioEmail);

		}

		Logger.info("[FIM-JOB] ::ReenvioEmail:: [FIM-JOB]");
	}
	
	private void sendMail(ReenvioEmail reenvioEmail) {

		try {
			
			List<String> emailsDestinatarios = Arrays.asList(StringUtils.split(reenvioEmail.emailsDestinatario, ";"));

			switch(reenvioEmail.tipoEmail) {
				
				case COMUNICAR_JURIDICO:

					List<String> destinatariosJuridico = new ArrayList<>();
					destinatariosJuridico.add(Configuracoes.DESTINATARIO_JURIDICO);
					destinatariosJuridico.add(Configuracoes.DESTINATARIO_JURIDICO2);

					ParecerJuridico parecerJuridico = ParecerJuridico.findById(reenvioEmail.idItensEmail);

					parecerJuridico.linkParecerJuridico = Configuracoes.APP_URL + "app/index.html#!/parecer-juridico/" + parecerJuridico.id;

					EmailParecerJuridico emailParecerJuridico = new EmailParecerJuridico(parecerJuridico.analiseGeo, parecerJuridico.parecerAnalistaGeo, destinatariosJuridico, parecerJuridico);
					emailParecerJuridico.enviar();
					
					break;

				case COMUNICAR_ORGAO:

					Comunicado comunicado = Comunicado.findById(reenvioEmail.idItensEmail);
					List<String> destinatariosOrgao = new ArrayList<>();
					destinatariosOrgao.add(comunicado.orgao.email);

					List<ParecerAnalistaGeo> pareceresAnalistaGeo = ParecerAnalistaGeo.find("id_analise_geo", comunicado.analiseGeo.id).fetch();

					ParecerAnalistaGeo ultimoParecer = pareceresAnalistaGeo.stream().sorted(Comparator.comparing(ParecerAnalistaGeo::getDataParecer).reversed()).collect(Collectors.toList()).get(0);

					comunicado.analiseGeo.reenviarEmailComunicado(ultimoParecer, comunicado, destinatariosOrgao);
					
					break;
					
				case NOTIFICACAO_ANALISE_TECNICA:

					ParecerAnalistaTecnico parecerAnalistaTecnico = ParecerAnalistaTecnico.findById(reenvioEmail.idItensEmail);

					parecerAnalistaTecnico.analiseTecnica.linkNotificacao = Configuracoes.URL_LICENCIAMENTO;

					parecerAnalistaTecnico.analiseTecnica.vistoria = parecerAnalistaTecnico.vistoria;

					EmailNotificacaoAnaliseTecnica emailNotificacaoAnaliseTecnica = new EmailNotificacaoAnaliseTecnica(parecerAnalistaTecnico.analiseTecnica, parecerAnalistaTecnico, emailsDestinatarios);

					emailNotificacaoAnaliseTecnica.enviar();
					
					break;

				case NOTIFICACAO_ANALISE_GEO:

					ParecerAnalistaGeo parecerAnalistaGeo = ParecerAnalistaGeo.findById(reenvioEmail.idItensEmail);

					parecerAnalistaGeo.analiseGeo.linkNotificacao = Configuracoes.URL_LICENCIAMENTO;

					EmailNotificacaoAnaliseGeo emailNotificacaoAnaliseGeo = new EmailNotificacaoAnaliseGeo(parecerAnalistaGeo.analiseGeo, parecerAnalistaGeo, emailsDestinatarios);
					emailNotificacaoAnaliseGeo.enviar();

					break;

				case NOTIFICACAO_SECRETARIO:

					ParecerSecretario parecerSecretario = ParecerSecretario.findById(reenvioEmail.idItensEmail);

					EmailNotificacaoStatusAnalise emailNotificacaoStatusAnalise = new EmailNotificacaoStatusAnalise(parecerSecretario.analise, parecerSecretario, emailsDestinatarios);
					emailNotificacaoStatusAnalise.enviar();

					break;

				case NOTIFICACAO_SECRETARIO_DISPENSA:

					Caracterizacao caracterizacao = Caracterizacao.findById(reenvioEmail.idItensEmail);

					EmailNotificacaoStatusDispensa emailNotificacaoStatusDispensa = new EmailNotificacaoStatusDispensa(caracterizacao, emailsDestinatarios);
					emailNotificacaoStatusDispensa.enviar();

					break;

			}

			reenvioEmail.delete();

		} catch(Exception e) {

			reenvioEmail.log = e.getMessage();

			reenvioEmail.save();
		}
	}
}
