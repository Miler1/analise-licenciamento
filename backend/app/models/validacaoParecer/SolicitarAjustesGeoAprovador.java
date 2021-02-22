package models.validacaoParecer;

import models.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;

import java.util.ArrayList;

public class SolicitarAjustesGeoAprovador extends TipoResultadoAnaliseChain<AnaliseGeo> {

    public SolicitarAjustesGeoAprovador() {

        super(TipoResultadoAnalise.SOLICITAR_AJUSTES);
    }

    @Override
    protected void validaParecer(AnaliseGeo analiseGeo, AnaliseGeo novaAnaliseGeo, UsuarioAnalise usuarioExecutor) {

        analiseGeo.tipoResultadoValidacaoAprovador = novaAnaliseGeo.tipoResultadoValidacaoAprovador;
        analiseGeo.parecerValidacaoAprovador = novaAnaliseGeo.parecerValidacaoAprovador;
        analiseGeo.usuarioValidacaoAprovador = usuarioExecutor;
        analiseGeo.ativo = false;

        analiseGeo.validarTipoResultadoValidacaoAprovador();
        analiseGeo.validarParecerValidacaoAprovador();

        analiseGeo._save();

        AnaliseGeo copia = analiseGeo.gerarCopia(false);

        /**
         * Quando o ajuste for do aprovador para o coordenador deve-se manter a validação do coordenador e coordenador
         */
        copia.setValidacaoCoordenador(analiseGeo);

        copia._save();

        /**
         * Workaround para persistir as licenças e os pareceres técnicos restrições
         */
        for(LicencaAnalise licencaAnalise: copia.licencasAnalise) {

            licencaAnalise._save();

            licencaAnalise.saveRecomendacoes();
        }

        ArrayList<ParecerGeoRestricao> pareceresGeoRestricoesSalvar = new ArrayList<>(copia.pareceresGeoRestricoes);
        copia.pareceresGeoRestricoes.clear();
        copia.updatePareceresGeoRestricoes(pareceresGeoRestricoesSalvar);

        analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_ANALISE_GEO_SECRETARIO, usuarioExecutor);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
    }
}