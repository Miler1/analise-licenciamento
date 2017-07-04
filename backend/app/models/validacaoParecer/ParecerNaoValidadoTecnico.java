package models.validacaoParecer;

import java.util.ArrayList;

import exceptions.ValidacaoException;
import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.AnalistaTecnico;
import models.ConsultorJuridico;
import models.TipoResultadoAnalise;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;
import utils.Mensagem;

public class ParecerNaoValidadoTecnico extends TipoResultadoAnaliseChain<AnaliseTecnica> {

	public ParecerNaoValidadoTecnico() {
		super(TipoResultadoAnalise.PARECER_NAO_VALIDADO);
	}

	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, Usuario usuarioExecultor) {
		
		analiseTecnica.tipoResultadoValidacao = novaAnaliseTecnica.tipoResultadoValidacao;
		analiseTecnica.parecerValidacao = novaAnaliseTecnica.parecerValidacao;		
		analiseTecnica.ativo = false;
		
		validarAnaliseTecnica(analiseTecnica, novaAnaliseTecnica);
		
		analiseTecnica._save();
					
		criarNovaAnalise(analiseTecnica, novaAnaliseTecnica.analistasTecnicos.get(0).usuario);
		
		analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_TECNICO, usuarioExecultor);
	}

	private void criarNovaAnalise(AnaliseTecnica analiseTecnica, Usuario usuarioAnalista) {
		
		AnaliseTecnica novaAnalise = new AnaliseTecnica();
		
		novaAnalise.analise = analiseTecnica.analise;
		novaAnalise.dataVencimentoPrazo = analiseTecnica.dataVencimentoPrazo;
		novaAnalise.revisaoSolicitada = true;
		novaAnalise.ativo = true;
		
		novaAnalise.analistasTecnicos = new ArrayList<>();
		AnalistaTecnico analistaTecnico = new AnalistaTecnico(novaAnalise, usuarioAnalista);
		novaAnalise.analistasTecnicos.add(analistaTecnico);
		
		novaAnalise._save();
	}

	private void validarAnaliseTecnica(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica) {
		
		analiseTecnica.validarTipoResultadoValidacao();
		
		analiseTecnica.validarParecerValidacao();
		
		if (novaAnaliseTecnica.analistasTecnicos == null || novaAnaliseTecnica.analistasTecnicos.isEmpty()) {
			
			throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_CONSULTOR_NAO_INFORMADO);
		}
		
	}
}