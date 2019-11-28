package models.validacaoParecer;

import models.*;
import models.licenciamento.Caracterizacao;
import models.licenciamento.StatusCaracterizacao;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.ViewHistoricoTramitacao;
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

        ParecerAnalistaGeo parecerAnalistaGeo = ParecerAnalistaGeo.find("analiseGeo", analiseGeo).first();

        if (parecerAnalistaGeo.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {

            List<Long> idsCaracterizacoes = ListUtil.getIds(analiseGeo.analise.processo.empreendimento.caracterizacoes);

            Caracterizacao.setStatusCaracterizacao(idsCaracterizacoes, StatusCaracterizacao.ARQUIVADO);

            analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.VALIDAR_INDEFERIMENTO_TECNICO_PELO_COORDENADOR, usuarioExecutor);
            ViewHistoricoTramitacao.setSetor(ViewHistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
            return;
        }

        if (parecerAnalistaGeo.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {

            analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.VALIDAR_DEFERIMENTO_TECNICO_PELO_COORDENADOR, usuarioExecutor);
            ViewHistoricoTramitacao.setSetor(ViewHistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
        }

    }
}
