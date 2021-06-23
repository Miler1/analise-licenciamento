package models.validacaoParecer;

import exceptions.ValidacaoException;
import models.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import utils.Mensagem;

import java.util.ArrayList;

public class ParecerNaoValidadoGeo extends TipoResultadoAnaliseChain<AnaliseGeo> {

    public ParecerNaoValidadoGeo() {
        super(TipoResultadoAnalise.PARECER_NAO_VALIDADO);
    }

    @Override
    protected void validaParecer(AnaliseGeo analiseGeo, AnaliseGeo novaAnaliseGeo, UsuarioAnalise usuarioExecutor) {

        analiseGeo.tipoResultadoValidacao = novaAnaliseGeo.tipoResultadoValidacao;
        analiseGeo.parecerValidacao = novaAnaliseGeo.parecerValidacao;
        analiseGeo.usuarioValidacao = usuarioExecutor;
        analiseGeo.ativo = false;

        validarAnaliseGeo(analiseGeo, novaAnaliseGeo);

        analiseGeo._save();

        if (novaAnaliseGeo.hasCoordenadores()) {

            criarNovaAnaliseComCoordenador(analiseGeo, novaAnaliseGeo.getCoordenador().usuario, usuarioExecutor);

            analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_GEO_PELO_COORDENADOR, usuarioExecutor);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);

        } else {

            criarNovaAnaliseComAnalista(analiseGeo, novaAnaliseGeo.getAnalistaGeo().usuario, usuarioExecutor);

            analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO, usuarioExecutor);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), usuarioExecutor);
        }
    }

    private void salvarNovaAnalise(AnaliseGeo novaAnalise, AnaliseGeo analiseGeo, UsuarioAnalise usuarioValidacao) {

        novaAnalise.analise = analiseGeo.analise;
        novaAnalise.dataCadastro = analiseGeo.dataCadastro;
        novaAnalise.dataVencimentoPrazo = analiseGeo.dataVencimentoPrazo;
        novaAnalise.revisaoSolicitada = true;
        novaAnalise.ativo = true;
        novaAnalise.usuarioValidacao = usuarioValidacao;

        novaAnalise._save();
    }

    private void criarNovaAnaliseComCoordenador(AnaliseGeo analiseGeo, UsuarioAnalise usuarioCoordenador, UsuarioAnalise usuarioValidacao) {

        AnaliseGeo novaAnalise = new AnaliseGeo();

        novaAnalise.coordenadores = new ArrayList<>();
        CoordenadorGeo coordenadorGeo = new CoordenadorGeo(novaAnalise, usuarioCoordenador);
        novaAnalise.coordenadores.add(coordenadorGeo);

        salvarNovaAnalise(novaAnalise, analiseGeo, usuarioValidacao);
    }

    private void criarNovaAnaliseComAnalista(AnaliseGeo analiseGeo, UsuarioAnalise usuarioAnalista, UsuarioAnalise usuarioValidacao) {

        AnaliseGeo novaAnalise = new AnaliseGeo();

        novaAnalise.analistasGeo = new ArrayList<>();
        AnalistaGeo analistaGeo = new AnalistaGeo(novaAnalise, usuarioAnalista);
        novaAnalise.analistasGeo.add(analistaGeo);

        salvarNovaAnalise(novaAnalise, analiseGeo, usuarioValidacao);
    }


    private void validarAnaliseGeo(AnaliseGeo analiseGeo, AnaliseGeo novaAnaliseGeo) {

        analiseGeo.validarTipoResultadoValidacao();

        analiseGeo.validarParecerValidacao();

        if ((novaAnaliseGeo.coordenadores == null || novaAnaliseGeo.coordenadores.isEmpty()) &&
                (novaAnaliseGeo.analistasGeo == null || novaAnaliseGeo.analistasGeo.isEmpty())) {

            throw new ValidacaoException(Mensagem.ANALISE_GEO_COORDENADOR_ANALISTA_NAO_INFORMADO);
        }
    }
}