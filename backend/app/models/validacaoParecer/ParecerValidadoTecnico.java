package models.validacaoParecer;

import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.TipoResultadoAnalise;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;

public class ParecerValidadoTecnico extends TipoResultadoAnaliseChain<AnaliseTecnica> {
	
	public ParecerValidadoTecnico() {
		super(TipoResultadoAnalise.PARECER_VALIDADO);
	}

	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, Usuario usuarioExecultor) {

		analiseTecnica.tipoResultadoValidacao = novaAnaliseTecnica.tipoResultadoValidacao;
		analiseTecnica.parecerValidacao = novaAnaliseTecnica.parecerValidacao;
		analiseTecnica.usuarioValidacao = usuarioExecultor;
		
		analiseTecnica.validarTipoResultadoValidacao();
		
		analiseTecnica._save();
		
		if (analiseTecnica.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.VALIDAR_INDEFERIMENTO_TECNICO, usuarioExecultor);				
			return;
		}
		
		if (analiseTecnica.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.VALIDAR_DEFERIMENTO_TECNICO, usuarioExecultor);
		}
		
	}
}
