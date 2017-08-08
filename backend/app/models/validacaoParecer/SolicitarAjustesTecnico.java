package models.validacaoParecer;

import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.LicencaAnalise;
import models.TipoResultadoAnalise;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;

public class SolicitarAjustesTecnico extends TipoResultadoAnaliseChain<AnaliseTecnica> {

	public SolicitarAjustesTecnico() {
		
		super(TipoResultadoAnalise.SOLICITAR_AJUSTES);
	}
	
	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, Usuario usuarioExecutor) {
		   
		analiseTecnica.tipoResultadoValidacao = novaAnaliseTecnica.tipoResultadoValidacao;
		analiseTecnica.parecerValidacao = novaAnaliseTecnica.parecerValidacao;
		analiseTecnica.usuarioValidacao = usuarioExecutor;
		analiseTecnica.ativo = false;
			
		analiseTecnica.validarTipoResultadoValidacao();
		analiseTecnica.validarParecerValidacao();
			
		analiseTecnica._save();
		
		AnaliseTecnica copia = analiseTecnica.gerarCopia();
		
		/**
		 * Quando o ajuste for do coordenador para o gerente deve-se manter a validação do gerente
		 */
		copia.setValidacaoGerente(analise);
		
		copia._save();
		
		/**
		 * Workaround para persistir as licenças e os pareceres técnicos restrições
		 */		
		copia.update(copia);
						
		analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_COORDENADOR, usuarioExecutor);
	}
}