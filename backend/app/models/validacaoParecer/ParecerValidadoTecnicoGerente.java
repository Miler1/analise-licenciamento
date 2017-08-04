package models.validacaoParecer;

import java.util.Calendar;
import java.util.Date;

import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.TipoResultadoAnalise;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;

public class ParecerValidadoTecnicoGerente extends TipoResultadoAnaliseChain<AnaliseTecnica> {
	
	public ParecerValidadoTecnicoGerente() {
		super(TipoResultadoAnalise.PARECER_VALIDADO);
	}

	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, Usuario usuarioExecultor) {

		analiseTecnica.tipoResultadoValidacaoGerente = novaAnaliseTecnica.tipoResultadoValidacaoGerente;
		analiseTecnica.parecerValidacaoGerente = novaAnaliseTecnica.parecerValidacaoGerente;
		analiseTecnica.usuarioValidacaoGerente = usuarioExecultor;
		
		analiseTecnica.validarTipoResultadoValidacaoGerente();		
		
		analiseTecnica._save();
		
		//TODO Alterar a ação da tramitação quando o banco terminar de inserir as tramitações do gerente
//		if (analiseTecnica.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {
//			
//			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.VALIDAR_INDEFERIMENTO_TECNICO, usuarioExecultor);				
//			return;
//		}
//		
//		if (analiseTecnica.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {
//			
//			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.VALIDAR_DEFERIMENTO_TECNICO, usuarioExecultor);
//		}
//		
	}
}
