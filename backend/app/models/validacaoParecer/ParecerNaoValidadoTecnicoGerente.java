package models.validacaoParecer;

import exceptions.ValidacaoException;
import models.AnaliseTecnica;
import models.AnalistaTecnico;
import models.TipoResultadoAnalise;
import models.UsuarioAnalise;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import utils.Mensagem;

public class ParecerNaoValidadoTecnicoGerente extends TipoResultadoAnaliseChain<AnaliseTecnica> {

	public ParecerNaoValidadoTecnicoGerente() {
		super(TipoResultadoAnalise.PARECER_NAO_VALIDADO);
	}

	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, UsuarioAnalise usuarioExecutor) {
		
		analiseTecnica.tipoResultadoValidacaoGerente = novaAnaliseTecnica.tipoResultadoValidacaoGerente;
		analiseTecnica.parecerValidacaoGerente = novaAnaliseTecnica.parecerValidacaoGerente;
		analiseTecnica.usuarioValidacaoGerente = usuarioExecutor;
		analiseTecnica.ativo = false;
		
		validarAnaliseTecnica(analiseTecnica, novaAnaliseTecnica);
		
		analiseTecnica._save();
					
		criarNovaAnalise(analiseTecnica, novaAnaliseTecnica.analistaTecnico.usuario, usuarioExecutor);
		
		analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_TECNICO_PELO_GERENTE, usuarioExecutor);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), usuarioExecutor);
	}

	private void criarNovaAnalise(AnaliseTecnica analiseTecnica, UsuarioAnalise usuarioAnalista, UsuarioAnalise usuarioValidacao) {
		
		AnaliseTecnica novaAnalise = new AnaliseTecnica();
		
		novaAnalise.analise = analiseTecnica.analise;
		novaAnalise.dataCadastro = analiseTecnica.dataCadastro;
		novaAnalise.dataVencimentoPrazo = analiseTecnica.dataVencimentoPrazo;
		novaAnalise.revisaoSolicitada = true;
		novaAnalise.ativo = true;
		novaAnalise.usuarioValidacao = analiseTecnica.usuarioValidacao;
		novaAnalise.usuarioValidacaoGerente = usuarioValidacao;
		novaAnalise.analistaTecnico = new AnalistaTecnico(novaAnalise, usuarioAnalista);
		
		novaAnalise._save();

	}

	private void validarAnaliseTecnica(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica) {
		
		analiseTecnica.validarTipoResultadoValidacaoGerente();
		
		analiseTecnica.validarParecerValidacaoGerente();
		
		if (novaAnaliseTecnica.analistaTecnico == null) {
			
			throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_CONSULTOR_NAO_INFORMADO);
		}
		
	}
}
