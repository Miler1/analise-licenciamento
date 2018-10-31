package models.validacaoParecer;

import models.AnaliseTecnica;
import models.TipoResultadoAnalise;
import models.portalSeguranca.Setor;
import models.portalSeguranca.UsuarioLicenciamento;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;

public class ParecerValidadoTecnicoGerente extends TipoResultadoAnaliseChain<AnaliseTecnica> {
	
	public ParecerValidadoTecnicoGerente() {
		super(TipoResultadoAnalise.PARECER_VALIDADO);
	}

	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, UsuarioLicenciamento usuarioExecutor) {

		analiseTecnica.tipoResultadoValidacaoGerente = novaAnaliseTecnica.tipoResultadoValidacaoGerente;
		analiseTecnica.parecerValidacaoGerente = novaAnaliseTecnica.parecerValidacaoGerente;
		analiseTecnica.usuarioValidacaoGerente = usuarioExecutor;
		
		analiseTecnica.validarTipoResultadoValidacaoGerente();		
		
		analiseTecnica._save();
		
		if (analiseTecnica.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.VALIDAR_INDEFERIMENTO_TECNICO_PELO_GERENTE, usuarioExecutor);
			Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
			return;
		}
		
		if (analiseTecnica.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.VALIDAR_DEFERIMENTO_TECNICO_PELO_GERENTE, usuarioExecutor);
			Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
		}
		
	}
}
