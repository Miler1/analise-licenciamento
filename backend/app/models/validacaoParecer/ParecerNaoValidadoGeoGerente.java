package models.validacaoParecer;

import exceptions.ValidacaoException;
import models.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import utils.Mensagem;

import java.util.ArrayList;

public class ParecerNaoValidadoGeoGerente extends TipoResultadoAnaliseChain<AnaliseGeo> {

    public ParecerNaoValidadoGeoGerente() {
        super(TipoResultadoAnalise.PARECER_NAO_VALIDADO);
    }

    @Override
    protected void validaParecer(AnaliseGeo analiseGeo, AnaliseGeo novaAnaliseGeo, UsuarioAnalise usuarioExecutor) {

        analiseGeo.tipoResultadoValidacaoGerente = novaAnaliseGeo.tipoResultadoValidacaoGerente;
        analiseGeo.parecerValidacaoGerente = novaAnaliseGeo.parecerValidacaoGerente;
        analiseGeo.usuarioValidacaoGerente = usuarioExecutor;
        analiseGeo.ativo = false;

        validarAnaliseTecnica(analiseGeo, novaAnaliseGeo);

        analiseGeo._save();

        criarNovaAnalise(analiseGeo, novaAnaliseGeo.getAnalistaGeo().usuario, usuarioExecutor);

        analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_TECNICO_PELO_GERENTE, usuarioExecutor);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
    }

    private void criarNovaAnalise(AnaliseGeo analiseGeo, UsuarioAnalise usuarioAnalista, UsuarioAnalise usuarioValidacao) {

        AnaliseGeo novaAnalise = new AnaliseGeo();

        novaAnalise.analise = analiseGeo.analise;
        novaAnalise.dataCadastro = analiseGeo.dataCadastro;
        novaAnalise.dataVencimentoPrazo = analiseGeo.dataVencimentoPrazo;
        novaAnalise.revisaoSolicitada = true;
        novaAnalise.ativo = true;
        novaAnalise.usuarioValidacao = analiseGeo.usuarioValidacao;
        novaAnalise.usuarioValidacaoGerente = usuarioValidacao;

        novaAnalise.analistasGeo = new ArrayList<>();
        AnalistaGeo analistaGeo = new AnalistaGeo(novaAnalise, usuarioAnalista);
        novaAnalise.analistasGeo.add(analistaGeo);

        novaAnalise._save();
    }

    private void validarAnaliseTecnica(AnaliseGeo analiseGeo, AnaliseGeo novaAnaliseGeo) {

        analiseGeo.validarTipoResultadoValidacaoGerente();

        analiseGeo.validarParecerValidacaoGerente();

        if (novaAnaliseGeo.analistasGeo == null || novaAnaliseGeo.analistasGeo.isEmpty()) {

            throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_CONSULTOR_NAO_INFORMADO);
        }

    }
}
