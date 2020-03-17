package jobs;

import models.Analise;
import models.Processo;
import models.licenciamento.StatusCaracterizacaoEnum;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.Condicao;
import models.tramitacao.HistoricoTramitacao;
import play.Logger;
import play.jobs.On;
import utils.Configuracoes;
import utils.Helper;
import utils.Mensagem;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@On("cron.verificarAnaliseVencida")
public class VerificarAnaliseVencida extends GenericJob {

    @Override
    public void executar() {

//        Logger.info("[INICIO-JOB] ::VerificarAnaliseVencida:: [INICIO-JOB]");
        verificarAnalisesVencidas();
//        Logger.info("[FIM-JOB] ::VerificarAnaliseVencida:: [FIM-JOB]");

    }

    public void verificarAnalisesVencidas() {

        List<Long> idCondicoes = Arrays.asList(Condicao.ANALISE_FINALIZADA,
                Condicao.ARQUIVADO,
                Condicao.LICENCA_EMITIDA,
                Condicao.SUSPENSO,
                Condicao.CANCELADO);

        List<Processo> processos = Processo
                .find("objetoTramitavel.condicao.id NOT IN(:idCondicoes)")
                .setParameter("idCondicoes", idCondicoes).fetch();

        processos.forEach(processo -> {

            try {

                verificaPrazoDeVencimento(processo);
                commitTransaction();

            } catch (Exception e) {

//                Logger.error(Mensagem.ERRO_ARQUIVAR_PROCESSO.getTexto(processo.numero));
//                Logger.error(e.getMessage());
                rollbackTransaction();

            }

        });

    }

    public void verificaPrazoDeVencimento(Processo processo) {

        if(Helper.getDiferencaDias(new Date(), processo.dataCadastro) > Configuracoes.PRAZO_ANALISE) {

            Analise.alterarStatusLicenca(StatusCaracterizacaoEnum.ARQUIVADO.codigo, processo.numero);

            processo.tramitacao.tramitar(processo, AcaoTramitacao.ARQUIVAR_PROTOCOLO);

            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(processo.objetoTramitavel.id), processo.caracterizacao.atividadesCaracterizacao.get(0).atividade.siglaSetor);

        }

    }

}
