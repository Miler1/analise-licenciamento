package models.validacaoParecer;

import models.AnaliseTecnica;
import models.LicencaAnalise;
import models.ParecerTecnicoRestricao;
import models.TipoResultadoAnalise;
import models.UsuarioAnalise;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;

import java.util.ArrayList;

public class SolicitarAjustesTecnicoAprovador extends TipoResultadoAnaliseChain<AnaliseTecnica> {

	public SolicitarAjustesTecnicoAprovador() {
		
		super(TipoResultadoAnalise.SOLICITAR_AJUSTES);
	}
	
	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, UsuarioAnalise usuarioExecutor) {
		   
		analiseTecnica.tipoResultadoValidacaoAprovador = novaAnaliseTecnica.tipoResultadoValidacaoAprovador;
		analiseTecnica.parecerValidacaoAprovador = novaAnaliseTecnica.parecerValidacaoAprovador;
		analiseTecnica.usuarioValidacaoAprovador = usuarioExecutor;
		analiseTecnica.ativo = false;
			
		analiseTecnica.validarTipoResultadoValidacaoAprovador();
		analiseTecnica.validarParecerValidacaoAprovador();
			
		analiseTecnica._save();
		
		AnaliseTecnica copia = analiseTecnica.gerarCopia(false);
		
		/**
		 * Quando o ajuste for do aprovador para o coordenador deve-se manter a validação do coordenador e gerente
		 */
		copia.setValidacaoCoordenadorTecnico(analiseTecnica);
		copia.setValidacaoCoordenador(analiseTecnica);
		
		copia._save();
		
		/**
		 * Workaround para persistir as licenças e os pareceres técnicos restrições
		 */		
		for(LicencaAnalise licencaAnalise: copia.licencasAnalise) {
			
			licencaAnalise._save();
			
			licencaAnalise.saveRecomendacoes();
		}		
		
		ArrayList<ParecerTecnicoRestricao> pareceresTecnicosRestricoesSalvar = new ArrayList<>(copia.pareceresTecnicosRestricoes);
		copia.pareceresTecnicosRestricoes.clear();		
		copia.updatePareceresTecnicosRestricoes(pareceresTecnicosRestricoesSalvar);
			
		analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_ANALISE_TECNICA_APROVADOR, usuarioExecutor);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
	}
}