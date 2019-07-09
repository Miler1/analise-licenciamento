package models.validacaoParecer;

import models.AnaliseGeo;
import models.TipoResultadoAnalise;
import models.UsuarioAnalise;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;

public class ParecerValidadoGeoGerente extends TipoResultadoAnaliseChain<AnaliseGeo> {

    public ParecerValidadoGeoGerente() {
        super(TipoResultadoAnalise.PARECER_VALIDADO);
    }

    @Override
    protected void validaParecer(AnaliseGeo analiseGeo, AnaliseGeo novaAnaliseGeo, UsuarioAnalise usuarioExecutor) {

        analiseGeo.tipoResultadoValidacaoGerente = novaAnaliseGeo.tipoResultadoValidacaoGerente;
        analiseGeo.parecerValidacaoGerente = novaAnaliseGeo.parecerValidacaoGerente;
        analiseGeo.usuarioValidacaoGerente = usuarioExecutor;

        analiseGeo.validarTipoResultadoValidacaoGerente();

        analiseGeo._save();

        if (analiseGeo.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {

            analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.VALIDAR_INDEFERIMENTO_GEO_PELO_GERENTE, usuarioExecutor);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
            return;
        }

        if (analiseGeo.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {

            analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.VALIDAR_DEFERIMENTO_GEO_PELO_GERENTE, usuarioExecutor);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
        }

    }
}
