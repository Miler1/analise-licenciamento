package jobs;

import models.AnaliseGeo;
import models.Notificacao;
import models.ParecerAnalistaGeo;
import play.Logger;
import play.jobs.On;
import play.jobs.OnApplicationStart;
import utils.Helper;
import utils.Mensagem;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//@On("cron.verificarPrazoAtendimentoNotificacao")
@OnApplicationStart
public class VerificarPrazoAtendimentoNotificacao extends GenericJob {

    @Override
    public void executar() throws Exception {

        Logger.info("[INICIO-JOB] ::VerificarPrazoAtendimentoNotificacao:: [INICIO-JOB]");

        List<Notificacao> notificacoes = Notificacao.find("resolvido", false).fetch();

        notificacoes.forEach(notificacao -> {

            try {

                verificarPrazoAtendimento(notificacao);
                commitTransaction();

            }catch (Exception e) {

                Logger.error(Mensagem.ERRO_ENVIAR_SEGUNDO_EMAIL_NOTIFICACAO.getTexto(notificacao.analiseGeo.analise.processo.numero));
                Logger.error(e.getMessage());
                rollbackTransaction();

            }

        });

        Logger.info("[FIM-JOB] ::VerificarPrazoAtendimentoNotificacao:: [FIM-JOB]");
    }

    public void verificarPrazoAtendimento(Notificacao notificacao) throws Exception {

        int diasCorridos = new Long((new Date().getTime() - notificacao.dataNotificacao.getTime()) / (1000*60*60*24)).intValue();

        if(diasCorridos >= notificacao.prazoNotificacao && !notificacao.segundoEmailEnviado) {

            List<ParecerAnalistaGeo> pareceresAnalistaGeo = ParecerAnalistaGeo.find("id_analise_geo", notificacao.analiseGeo.id).fetch();

            ParecerAnalistaGeo ultimoParecer = pareceresAnalistaGeo.stream().sorted(Comparator.comparing(ParecerAnalistaGeo::getDataParecer).reversed()).collect(Collectors.toList()).get(0);

            notificacao.prazoNotificacao += (notificacao.prazoNotificacao / 2);
            notificacao.segundoEmailEnviado = true;
            notificacao.dataFinalNotificacao = Helper.somarDias(notificacao.dataNotificacao, notificacao.prazoNotificacao);
            notificacao.analiseGeo.enviarEmailNotificacao(notificacao, ultimoParecer, notificacao.analiseGeo.documentos);



        }

    }
}
