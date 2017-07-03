package models.validacaoParecer;

import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.TipoResultadoAnalise;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;

public class SolicitarAjustesTecnico extends TipoResultadoAnaliseChain<AnaliseTecnica> {

	public SolicitarAjustesTecnico() {
		
		super(TipoResultadoAnalise.SOLICITAR_AJUSTES);
	}
	
	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, Usuario usuarioExecultor) {
		   
		analiseTecnica.tipoResultadoValidacao = novaAnaliseTecnica.tipoResultadoValidacao;
		analiseTecnica.parecerValidacao = novaAnaliseTecnica.parecerValidacao;
		analiseTecnica.usuarioValidacao = usuarioExecultor;
		analiseTecnica.ativo = false;
			
		analiseTecnica.validarTipoResultadoValidacao();
		analiseTecnica.validarParecerValidacao();
			
		analiseTecnica._save();
		
		AnaliseTecnica copia = analiseTecnica.gerarCopia();
		
		copia._save();
		
		analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO, usuarioExecultor);
	}
}