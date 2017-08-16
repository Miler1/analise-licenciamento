package models.validacaoParecer;

import java.util.ArrayList;

import exceptions.ValidacaoException;
import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.AnalistaTecnico;
import models.ConsultorJuridico;
import models.GerenteTecnico;
import models.TipoResultadoAnalise;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoTramitacao;
import utils.Mensagem;

public class ParecerNaoValidadoTecnico extends TipoResultadoAnaliseChain<AnaliseTecnica> {

	public ParecerNaoValidadoTecnico() {
		super(TipoResultadoAnalise.PARECER_NAO_VALIDADO);
	}

	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, Usuario usuarioExecutor) {
		
		analiseTecnica.tipoResultadoValidacao = novaAnaliseTecnica.tipoResultadoValidacao;
		analiseTecnica.parecerValidacao = novaAnaliseTecnica.parecerValidacao;		
		analiseTecnica.usuarioValidacao = usuarioExecutor;
		analiseTecnica.ativo = false;
		
		validarAnaliseTecnica(analiseTecnica, novaAnaliseTecnica);
		
		analiseTecnica._save();
		
		if (novaAnaliseTecnica.hasGerentes()) {
			
			criarNovaAnaliseComGerente(analiseTecnica, novaAnaliseTecnica.getGerenteTecnico().usuario, usuarioExecutor);
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_TECNICO_PELO_COORD_ENCAMINHANDO_GERENTE, usuarioExecutor);
			
		} else {
			
			criarNovaAnaliseComAnalista(analiseTecnica, novaAnaliseTecnica.getAnalistaTecnico().usuario, usuarioExecutor);
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_TECNICO_ENCAMINHANDO_TECNICO, usuarioExecutor);
		}
	}

	private void salvarNovaAnalise(AnaliseTecnica novaAnalise, AnaliseTecnica analiseTecnica, Usuario usuarioValidacao) {
			
		novaAnalise.analise = analiseTecnica.analise;
		novaAnalise.dataCadastro = analiseTecnica.dataCadastro;
		novaAnalise.dataVencimentoPrazo = analiseTecnica.dataVencimentoPrazo;
		novaAnalise.revisaoSolicitada = true;
		novaAnalise.ativo = true;
		novaAnalise.usuarioValidacao = usuarioValidacao;
		
		novaAnalise._save();
	}
	
	private void criarNovaAnaliseComGerente(AnaliseTecnica analiseTecnica, Usuario usuarioGerente, Usuario usuarioValidacao) {
		
		AnaliseTecnica novaAnalise = new AnaliseTecnica();
		
		novaAnalise.gerentesTecnicos = new ArrayList<>();
		GerenteTecnico gerenteTecnico = new GerenteTecnico(novaAnalise, usuarioGerente);
		novaAnalise.gerentesTecnicos.add(gerenteTecnico);
		
		salvarNovaAnalise(novaAnalise, analiseTecnica, usuarioValidacao);
	}
	
	private void criarNovaAnaliseComAnalista(AnaliseTecnica analiseTecnica, Usuario usuarioAnalista, Usuario usuarioValidacao) {
		
		AnaliseTecnica novaAnalise = new AnaliseTecnica();
		
		novaAnalise.analistasTecnicos = new ArrayList<>();
		AnalistaTecnico analistaTecnico = new AnalistaTecnico(novaAnalise, usuarioAnalista);
		novaAnalise.analistasTecnicos.add(analistaTecnico);
		
		salvarNovaAnalise(novaAnalise, analiseTecnica, usuarioValidacao);
	}	
	

	private void validarAnaliseTecnica(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica) {
		
		analiseTecnica.validarTipoResultadoValidacao();
		
		analiseTecnica.validarParecerValidacao();
		
		if ((novaAnaliseTecnica.gerentesTecnicos == null || novaAnaliseTecnica.gerentesTecnicos.isEmpty()) &&
			(novaAnaliseTecnica.analistasTecnicos == null || novaAnaliseTecnica.analistasTecnicos.isEmpty())) {
			
			throw new ValidacaoException(Mensagem.ANALISE_TECNICA_GERENTE_ANALISTA_NAO_INFORMADO);
		}		
	}
}
