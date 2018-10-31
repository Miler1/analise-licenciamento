package models.validacaoParecer;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.AnaliseTecnica;
import models.TipoResultadoAnalise;
import models.licenciamento.Caracterizacao;
import models.licenciamento.StatusCaracterizacao;
import models.portalSeguranca.Setor;
import models.portalSeguranca.UsuarioLicenciamento;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import utils.ListUtil;

public class ParecerValidadoTecnico extends TipoResultadoAnaliseChain<AnaliseTecnica> {
	
	public ParecerValidadoTecnico() {
		super(TipoResultadoAnalise.PARECER_VALIDADO);
	}

	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, UsuarioLicenciamento usuarioExecutor) {

		analiseTecnica.tipoResultadoValidacao = novaAnaliseTecnica.tipoResultadoValidacao;
		analiseTecnica.parecerValidacao = novaAnaliseTecnica.parecerValidacao;
		analiseTecnica.usuarioValidacao = usuarioExecutor;
		
		analiseTecnica.validarTipoResultadoValidacao();
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());		
		analiseTecnica.dataFim = c.getTime();		
		
		analiseTecnica._save();
		
		if (analiseTecnica.tipoResultadoAnalise.id == TipoResultadoAnalise.INDEFERIDO) {
			
			List<Long> idsCaracterizacoes = ListUtil.getIds(analiseTecnica.analise.processo.caracterizacoes);
			
			Caracterizacao.setStatusCaracterizacao(idsCaracterizacoes, StatusCaracterizacao.ARQUIVADO);
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.VALIDAR_INDEFERIMENTO_TECNICO_PELO_COORDENADOR, usuarioExecutor);
			Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
			return;
		}
		
		if (analiseTecnica.tipoResultadoAnalise.id == TipoResultadoAnalise.DEFERIDO) {
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.VALIDAR_DEFERIMENTO_TECNICO_PELO_COORDENADOR, usuarioExecutor);
			Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
		}
		
	}
}
