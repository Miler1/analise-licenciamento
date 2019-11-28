package models.validacaoParecer;

import exceptions.ValidacaoException;
import models.AnaliseTecnica;
import models.AnalistaTecnico;
import models.Gerente;
import models.TipoResultadoAnalise;
import models.UsuarioAnalise;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.ViewHistoricoTramitacao;
import utils.Mensagem;

import java.util.ArrayList;

public class ParecerNaoValidadoTecnico extends TipoResultadoAnaliseChain<AnaliseTecnica> {

	public ParecerNaoValidadoTecnico() {
		super(TipoResultadoAnalise.PARECER_NAO_VALIDADO);
	}

	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, UsuarioAnalise usuarioExecutor) {
		
		analiseTecnica.tipoResultadoValidacao = novaAnaliseTecnica.tipoResultadoValidacao;
		analiseTecnica.parecerValidacao = novaAnaliseTecnica.parecerValidacao;		
		analiseTecnica.usuarioValidacao = usuarioExecutor;
		analiseTecnica.ativo = false;
		
		validarAnaliseTecnica(analiseTecnica, novaAnaliseTecnica);
		
		analiseTecnica._save();
		
		if (novaAnaliseTecnica.hasGerentes()) {
			
			criarNovaAnaliseComGerente(analiseTecnica, novaAnaliseTecnica.getGerente().usuario, usuarioExecutor);
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_TECNICO_PELO_COORD_ENCAMINHANDO_GERENTE, usuarioExecutor);
			ViewHistoricoTramitacao.setSetor(ViewHistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);

		} else {
			
			criarNovaAnaliseComAnalista(analiseTecnica, novaAnaliseTecnica.analistaTecnico.usuario, usuarioExecutor);
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_TECNICO_ENCAMINHANDO_TECNICO, usuarioExecutor);
			ViewHistoricoTramitacao.setSetor(ViewHistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
		}
	}

	private void salvarNovaAnalise(AnaliseTecnica novaAnalise, AnaliseTecnica analiseTecnica, UsuarioAnalise usuarioValidacao) {
			
		novaAnalise.analise = analiseTecnica.analise;
		novaAnalise.dataCadastro = analiseTecnica.dataCadastro;
		novaAnalise.dataVencimentoPrazo = analiseTecnica.dataVencimentoPrazo;
		novaAnalise.revisaoSolicitada = true;
		novaAnalise.ativo = true;
		novaAnalise.usuarioValidacao = usuarioValidacao;
		
		novaAnalise._save();
	}
	
	private void criarNovaAnaliseComGerente(AnaliseTecnica analiseTecnica, UsuarioAnalise usuarioGerente, UsuarioAnalise usuarioValidacao) {
		
		AnaliseTecnica novaAnalise = new AnaliseTecnica();
		
		novaAnalise.gerentes = new ArrayList<>();
		Gerente gerente = new Gerente(novaAnalise, usuarioGerente);
		novaAnalise.gerentes.add(gerente);
		
		salvarNovaAnalise(novaAnalise, analiseTecnica, usuarioValidacao);
	}
	
	private void criarNovaAnaliseComAnalista(AnaliseTecnica analiseTecnica, UsuarioAnalise usuarioAnalista, UsuarioAnalise usuarioValidacao) {
		
		AnaliseTecnica novaAnalise = new AnaliseTecnica();
		novaAnalise.analistaTecnico = new AnalistaTecnico(novaAnalise, usuarioAnalista);
		
		salvarNovaAnalise(novaAnalise, analiseTecnica, usuarioValidacao);
	}

	private void validarAnaliseTecnica(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica) {
		
		analiseTecnica.validarTipoResultadoValidacao();
		
		analiseTecnica.validarParecerValidacao();
		
		if ((novaAnaliseTecnica.gerentes == null || novaAnaliseTecnica.gerentes.isEmpty()) &&
			(novaAnaliseTecnica.analistaTecnico == null)) {
			
			throw new ValidacaoException(Mensagem.ANALISE_TECNICA_GERENTE_ANALISTA_NAO_INFORMADO);
		}		
	}
}
