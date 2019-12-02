package jobs;

import models.Analise;
import models.Processo;
import models.licenciamento.StatusCaracterizacaoEnum;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.Condicao;
import models.tramitacao.HistoricoTramitacao;
import play.Logger;
import play.jobs.On;
import play.jobs.OnApplicationStart;
import utils.Helper;
import utils.Mensagem;

import java.util.Date;
import java.util.List;

//@On("cron.verificarAnaliseVencida")
@OnApplicationStart
public class VerificarAnaliseVencida extends GenericJob {

    @Override
    public void executar() {
        Logger.info("[INICIO-JOB] ::VerificarAnaliseVencida:: [INICIO-JOB]");
        verificarAnalisesVencidas();
        Logger.info("[FIM-JOB] ::VerificarAnaliseVencida:: [FIM-JOB]");
    }

    public void verificarAnalisesVencidas() {
        List<Processo> processos = Processo
                .find("objetoTramitavel.condicao.id <> :idCondicaoFinalizada AND objetoTramitavel.condicao.id <> :idCondicaoArquivada")
                .setParameter("idCondicaoFinalizada", Condicao.ANALISE_FINALIZADA)
                .setParameter("idCondicaoArquivada", Condicao.ARQUIVADO).fetch();

        processos.forEach(processo -> {

            try {

                verificaPrazoDeVencimento(processo);
                commitTransaction();

            } catch (Exception e) {

                Logger.error(Mensagem.ERRO_ARQUIVAR_PROCESSO.getTexto(processo.numero));
                Logger.error(e.getMessage());
                rollbackTransaction();

            }

        });

    }

    public void verificaPrazoDeVencimento(Processo processo) {

        if(Helper.getDiferencaDias(new Date(), processo.dataCadastro) > 180l) {

            processo.tramitacao.tramitar(processo, AcaoTramitacao.ARQUIVAR_PROTOCOLO);

            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(processo.objetoTramitavel.id), processo.caracterizacao.atividadesCaracterizacao.get(0).atividade.siglaSetor);

            Analise.alterarStatusLicenca(StatusCaracterizacaoEnum.ARQUIVADO.codigo, processo.numero);

        }

    }

}
