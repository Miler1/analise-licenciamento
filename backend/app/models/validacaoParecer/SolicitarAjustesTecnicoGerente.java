package models.validacaoParecer;

import java.util.ArrayList;

import models.AnaliseTecnica;
import models.LicencaAnalise;
import models.ParecerTecnicoRestricao;
import models.TipoResultadoAnalise;
import models.portalSeguranca.Setor;
import models.portalSeguranca.UsuarioLicenciamento;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;

public class SolicitarAjustesTecnicoGerente extends TipoResultadoAnaliseChain<AnaliseTecnica> {

	public SolicitarAjustesTecnicoGerente() {
		
		super(TipoResultadoAnalise.SOLICITAR_AJUSTES);
	}
	
	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, UsuarioLicenciamento usuarioExecutor) {
		   
		analiseTecnica.tipoResultadoValidacaoGerente = novaAnaliseTecnica.tipoResultadoValidacaoGerente;
		analiseTecnica.parecerValidacaoGerente = novaAnaliseTecnica.parecerValidacaoGerente;
		analiseTecnica.usuarioValidacaoGerente = usuarioExecutor;
		analiseTecnica.ativo = false;
			
		analiseTecnica.validarTipoResultadoValidacaoGerente();
		analiseTecnica.validarParecerValidacaoGerente();
			
		analiseTecnica._save();
		
		AnaliseTecnica copia = analiseTecnica.gerarCopia(false);
		
		copia._save();
		
		/**
		 * Workaround para persistir as licenças e os pareceres técnicos restrições
		 */		
		for(LicencaAnalise licencaAnalise: copia.licencasAnalise) {
			
			licencaAnalise._save();
			
			licencaAnalise.saveCondicionantes();
			licencaAnalise.saveRecomendacoes();
		}			
		
		ArrayList<ParecerTecnicoRestricao> pareceresTecnicosRestricoesSalvar = new ArrayList<>(copia.pareceresTecnicosRestricoes);
		copia.pareceresTecnicosRestricoes.clear();		
		copia.updatePareceresTecnicosRestricoes(pareceresTecnicosRestricoesSalvar);
			
		analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_GERENTE, usuarioExecutor);
		Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
	}
}