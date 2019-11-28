package models.validacaoParecer;

import models.AnaliseGeo;
import models.ParecerAnalistaGeo;
import models.TipoResultadoAnalise;
import models.UsuarioAnalise;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.ViewHistoricoTramitacao;

public class ParecerValidadoGeoGerente extends TipoResultadoAnaliseChain<AnaliseGeo> {

    public ParecerValidadoGeoGerente() {
        super(TipoResultadoAnalise.PARECER_VALIDADO);
    }

    @Override
    protected void validaParecer(AnaliseGeo analiseGeo, AnaliseGeo novaAnaliseGeo, UsuarioAnalise usuarioExecutor) {

        analiseGeo.validarTipoResultadoValidacaoGerente();

        analiseGeo._save();

        ParecerAnalistaGeo parecerAnalistaGeo = ParecerAnalistaGeo.find("analiseGeo", analiseGeo).first();

        if (parecerAnalistaGeo.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {

            analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.VALIDAR_INDEFERIMENTO_GEO_PELO_GERENTE, usuarioExecutor);
            ViewHistoricoTramitacao.setSetor(ViewHistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
            return;
        }

        if (parecerAnalistaGeo.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {

            analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.VALIDAR_DEFERIMENTO_GEO_PELO_GERENTE, usuarioExecutor);
            ViewHistoricoTramitacao.setSetor(ViewHistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
        }

    }
}
