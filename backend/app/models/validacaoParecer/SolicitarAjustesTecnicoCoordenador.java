package models.validacaoParecer;

import models.AnaliseTecnica;
import models.LicencaAnalise;
import models.ParecerTecnicoRestricao;
import models.TipoResultadoAnalise;
import models.UsuarioAnalise;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;

import java.util.ArrayList;

public class SolicitarAjustesTecnicoCoordenador extends TipoResultadoAnaliseChain<AnaliseTecnica> {

    public SolicitarAjustesTecnicoCoordenador() {

        super(TipoResultadoAnalise.SOLICITAR_AJUSTES);
    }

    @Override
    protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, UsuarioAnalise usuarioExecutor) {

        analiseTecnica.tipoResultadoValidacaoCoordenador = novaAnaliseTecnica.tipoResultadoValidacaoCoordenador;
        analiseTecnica.parecerValidacaoCoordenador = novaAnaliseTecnica.parecerValidacaoCoordenador;
        analiseTecnica.usuarioValidacaoCoordenador = usuarioExecutor;
        analiseTecnica.ativo = false;

        analiseTecnica.validarTipoResultadoValidacaoCoordenador();
        analiseTecnica.validarParecerValidacaoCoordenador();

        analiseTecnica._save();

        AnaliseTecnica copia = analiseTecnica.gerarCopia(false);

        copia._save();

        /**
         * Workaround para persistir as licenças e os pareceres técnicos restrições
         */
        for (LicencaAnalise licencaAnalise : copia.licencasAnalise) {

            licencaAnalise._save();

            licencaAnalise.saveRecomendacoes();
        }

        ArrayList<ParecerTecnicoRestricao> pareceresTecnicosRestricoesSalvar = new ArrayList<>(copia.pareceresTecnicosRestricoes);
        copia.pareceresTecnicosRestricoes.clear();
        copia.updatePareceresTecnicosRestricoes(pareceresTecnicosRestricoesSalvar);

        analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_COORDENADOR, usuarioExecutor);
        HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
    }
}