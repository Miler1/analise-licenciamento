package models.validacaoParecer;

import exceptions.ValidacaoException;
import models.*;
import models.Coordenador;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
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
		
		if (novaAnaliseTecnica.hasCoordenadores()) {
			
			criarNovaAnaliseComCoordenador(analiseTecnica, novaAnaliseTecnica.getCoordenador().usuario, usuarioExecutor);
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_TECNICO_PELO_COORD_ENCAMINHANDO_COORDENADOR, usuarioExecutor);
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);

		} else {
			
			criarNovaAnaliseComAnalista(analiseTecnica, novaAnaliseTecnica.analistaTecnico.usuario, usuarioExecutor);
			
			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_TECNICO_ENCAMINHANDO_TECNICO, usuarioExecutor);
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
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
	
	private void criarNovaAnaliseComCoordenador(AnaliseTecnica analiseTecnica, UsuarioAnalise usuarioCoordenador, UsuarioAnalise usuarioValidacao) {
		
		AnaliseTecnica novaAnalise = new AnaliseTecnica();
		
		novaAnalise.coordenadores = new ArrayList<>();
		Coordenador coordenador = new Coordenador(novaAnalise, usuarioCoordenador);
		novaAnalise.coordenadores.add(coordenador);
		
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
		
		if ((novaAnaliseTecnica.coordenadores == null || novaAnaliseTecnica.coordenadores.isEmpty()) &&
			(novaAnaliseTecnica.analistaTecnico == null)) {
			
			throw new ValidacaoException(Mensagem.ANALISE_TECNICA_COORDENADOR_ANALISTA_NAO_INFORMADO);
		}		
	}
}
