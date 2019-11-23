package models.validacaoParecer;

import models.AnaliseTecnica;
import models.LicencaAnalise;
import models.ParecerTecnicoRestricao;
import models.TipoResultadoAnalise;
import models.UsuarioAnalise;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.ViewHistoricoTramitacao;

import java.util.ArrayList;

public class SolicitarAjustesTecnico extends TipoResultadoAnaliseChain<AnaliseTecnica> {

	public SolicitarAjustesTecnico() {
		
		super(TipoResultadoAnalise.SOLICITAR_AJUSTES);
	}
	
	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, UsuarioAnalise usuarioExecutor) {
		   
		analiseTecnica.tipoResultadoValidacao = novaAnaliseTecnica.tipoResultadoValidacao;
		analiseTecnica.parecerValidacao = novaAnaliseTecnica.parecerValidacao;
		analiseTecnica.usuarioValidacao = usuarioExecutor;
		analiseTecnica.ativo = false;
			
		analiseTecnica.validarTipoResultadoValidacao();
		analiseTecnica.validarParecerValidacao();
			
		analiseTecnica._save();
		
		AnaliseTecnica copia = analiseTecnica.gerarCopia(false);
		
		/**
		 * Quando o ajuste for do coordenador para o gerente deve-se manter a validação do gerente
		 */
		copia.setValidacaoGerente(analiseTecnica);
		
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
		
		if (copia.hasGerentes()){
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_COORDENADOR_PARA_GERENTE, usuarioExecutor);
			ViewHistoricoTramitacao.setSetor(ViewHistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
		} else {
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_COORDENADOR_PARA_ANALISTA, usuarioExecutor);
			ViewHistoricoTramitacao.setSetor(ViewHistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
		}
	}
}