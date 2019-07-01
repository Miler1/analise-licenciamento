package models.validacaoParecer;

import models.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;

import java.util.ArrayList;
import java.util.List;

public class SolicitarAjustesGeoGerente extends TipoResultadoAnaliseChain<AnaliseGeo> {

    public SolicitarAjustesGeoGerente() {

        super(TipoResultadoAnalise.SOLICITAR_AJUSTES);
    }

    @Override
    protected void validaParecer(AnaliseGeo analiseGeo, AnaliseGeo novaAnaliseGeo, UsuarioAnalise usuarioExecutor) {

        analiseGeo.tipoResultadoValidacaoGerente = novaAnaliseGeo.tipoResultadoValidacaoGerente;
        analiseGeo.parecerValidacaoGerente = novaAnaliseGeo.parecerValidacaoGerente;
        analiseGeo.usuarioValidacaoGerente = usuarioExecutor;
        analiseGeo.ativo = false;

        analiseGeo.validarTipoResultadoValidacaoGerente();
        analiseGeo.validarParecerValidacaoGerente();

        analiseGeo._save();

        AnaliseGeo copia = analiseGeo.gerarCopia(false);

        copia._save();

        /**
         * Workaround para persistir as licenças e os pareceres técnicos restrições
         */
        for(LicencaAnalise licencaAnalise: copia.licencasAnalise) {

            licencaAnalise._save();

            licencaAnalise.saveCondicionantes();
            licencaAnalise.saveRecomendacoes();
        }

        List<ParecerGeoRestricao> pareceresGeoRestricoesSalvar = new ArrayList<>(copia.pareceresGeoRestricoes);
        copia.pareceresGeoRestricoes.clear();
        copia.updatePareceresGeoRestricoes(pareceresGeoRestricoesSalvar);

        analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_GERENTE, usuarioExecutor);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
    }
}