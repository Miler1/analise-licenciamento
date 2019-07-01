package models.validacaoParecer;

import models.AnaliseGeo;
import models.AnaliseTecnica;
import models.TipoResultadoAnalise;
import models.licenciamento.Caracterizacao;
import models.licenciamento.StatusCaracterizacao;
import models.UsuarioAnalise;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import utils.ListUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ParecerValidadoGeo extends TipoResultadoAnaliseChain<AnaliseGeo> {

    public ParecerValidadoGeo() {
        super(TipoResultadoAnalise.PARECER_VALIDADO);
    }

    @Override
    protected void validaParecer(AnaliseGeo analiseGeo, AnaliseGeo novaAnaliseGeo, UsuarioAnalise usuarioExecutor) {

        analiseGeo.tipoResultadoValidacao = novaAnaliseGeo.tipoResultadoValidacao;
        analiseGeo.parecerValidacao = novaAnaliseGeo.parecerValidacao;
        analiseGeo.usuarioValidacao = usuarioExecutor;


        analiseGeo.validarTipoResultadoValidacao();

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        analiseGeo.dataFim = c.getTime();

        analiseGeo._save();

        if (analiseGeo.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {

            List<Long> idsCaracterizacoes = ListUtil.getIds(analiseGeo.analise.processo.caracterizacoes);

            Caracterizacao.setStatusCaracterizacao(idsCaracterizacoes, StatusCaracterizacao.ARQUIVADO);

            analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.VALIDAR_INDEFERIMENTO_TECNICO_PELO_COORDENADOR, usuarioExecutor);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
            return;
        }

        if (analiseGeo.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {

            analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.VALIDAR_DEFERIMENTO_TECNICO_PELO_COORDENADOR, usuarioExecutor);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
        }

    }
}
