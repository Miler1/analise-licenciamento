package models.validacaoParecer;

import models.AnaliseGeo;
import models.ParecerAnalistaGeo;
import models.TipoResultadoAnalise;
import models.UsuarioAnalise;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;

public class ParecerValidadoGeoCoordenador extends TipoResultadoAnaliseChain<AnaliseGeo> {

    public ParecerValidadoGeoCoordenador() {
        super(TipoResultadoAnalise.PARECER_VALIDADO);
    }

    @Override
    protected void validaParecer(AnaliseGeo analiseGeo, AnaliseGeo novaAnaliseGeo, UsuarioAnalise usuarioExecutor) {

        analiseGeo.validarTipoResultadoValidacaoCoordenador();

        analiseGeo._save();

        ParecerAnalistaGeo parecerAnalistaGeo = ParecerAnalistaGeo.find("analiseGeo", analiseGeo).first();

        if (parecerAnalistaGeo.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.INDEFERIDO)) {

            analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.VALIDAR_INDEFERIMENTO_GEO_PELO_COORDENADOR, usuarioExecutor);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
            return;
        }

        if (parecerAnalistaGeo.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.DEFERIDO)) {

            analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.VALIDAR_DEFERIMENTO_GEO_PELO_COORDENADOR, usuarioExecutor);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
        }

    }
}
