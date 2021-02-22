package models.validacaoParecer;

import models.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;

import java.util.ArrayList;
import java.util.List;

public class SolicitarAjustesGeoCoordenador extends TipoResultadoAnaliseChain<AnaliseGeo> {

    public SolicitarAjustesGeoCoordenador() {

        super(TipoResultadoAnalise.SOLICITAR_AJUSTES);
    }

    @Override
    protected void validaParecer(AnaliseGeo analiseGeo, AnaliseGeo novaAnaliseGeo, UsuarioAnalise usuarioExecutor) {

        analiseGeo.ativo = false;
        analiseGeo.validarTipoResultadoValidacaoCoordenador();
        analiseGeo.validarParecerValidacaoCoordenador();

        analiseGeo._save();

        AnaliseGeo copia = analiseGeo.gerarCopia(false);

        copia._save();

        /**
         * Workaround para persistir as licenças e os pareceres técnicos restrições
         */
        for(LicencaAnalise licencaAnalise: copia.licencasAnalise) {

            licencaAnalise._save();

            licencaAnalise.saveRecomendacoes();
        }

        List<ParecerGeoRestricao> pareceresGeoRestricoesSalvar = new ArrayList<>(copia.pareceresGeoRestricoes);
        copia.pareceresGeoRestricoes.clear();
        copia.updatePareceresGeoRestricoes(pareceresGeoRestricoesSalvar);

        analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_COORDENADOR, usuarioExecutor);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
    }
}