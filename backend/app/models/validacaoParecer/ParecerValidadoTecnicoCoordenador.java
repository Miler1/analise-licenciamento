package models.validacaoParecer;

import models.AnaliseTecnica;
import models.TipoResultadoAnalise;
import models.UsuarioAnalise;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;

public class ParecerValidadoTecnicoCoordenador extends TipoResultadoAnaliseChain<AnaliseTecnica> {

    public ParecerValidadoTecnicoCoordenador() {
        super(TipoResultadoAnalise.PARECER_VALIDADO);
    }

    @Override
    protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, UsuarioAnalise usuarioExecutor) {

        analiseTecnica.tipoResultadoValidacaoCoordenador = novaAnaliseTecnica.tipoResultadoValidacaoCoordenador;
        analiseTecnica.parecerValidacaoCoordenador = novaAnaliseTecnica.parecerValidacaoCoordenador;
        analiseTecnica.usuarioValidacaoCoordenador = usuarioExecutor;

        analiseTecnica.validarTipoResultadoValidacaoCoordenador();

        analiseTecnica._save();

        if (analiseTecnica.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.INDEFERIDO)) {

            analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.VALIDAR_INDEFERIMENTO_TECNICO_PELO_COORDENADOR, usuarioExecutor);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
            return;
        }

        if (analiseTecnica.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.DEFERIDO)) {

            analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.VALIDAR_DEFERIMENTO_TECNICO_PELO_COORDENADOR, usuarioExecutor);
            HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
        }

    }
}