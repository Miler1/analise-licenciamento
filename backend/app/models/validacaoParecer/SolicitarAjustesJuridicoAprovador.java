package models.validacaoParecer;

import java.util.ArrayList;

import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.LicencaAnalise;
import models.ParecerTecnicoRestricao;
import models.TipoResultadoAnalise;
import models.portalSeguranca.Setor;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;

public class SolicitarAjustesJuridicoAprovador extends TipoResultadoAnaliseChain<AnaliseJuridica> {

	public SolicitarAjustesJuridicoAprovador() {
		
		super(TipoResultadoAnalise.SOLICITAR_AJUSTES);
	}
	
	@Override
	protected void validaParecer(AnaliseJuridica analiseJuridica, AnaliseJuridica novaAnaliseJuridica, Usuario usuarioExecutor) {
		   
		analiseJuridica.tipoResultadoValidacaoAprovador = novaAnaliseJuridica.tipoResultadoValidacaoAprovador;
		analiseJuridica.parecerValidacaoAprovador = novaAnaliseJuridica.parecerValidacaoAprovador;
		analiseJuridica.usuarioValidacaoAprovador = usuarioExecutor;
		analiseJuridica.ativo = false;
			
		analiseJuridica.validarTipoResultadoValidacaoAprovador();
		analiseJuridica.validarParecerValidacaoAprovador();
			
		analiseJuridica._save();
		
		AnaliseJuridica copia = analiseJuridica.gerarCopia();
		
		/**
		 * Quando o ajuste for do aprovador para o coordenador deve-se manter a validação do coordenador
		 */
		copia.setValidacaoCoordenador(analiseJuridica);
		
		copia._save();
			
		analiseJuridica.analise.processo.tramitacao.tramitar(analiseJuridica.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_ANALISE_JURIDICA_APROVADOR, usuarioExecutor);
		Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(analiseJuridica.analise.processo.objetoTramitavel.id), usuarioExecutor);
	}
}