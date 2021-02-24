package models.validacaoParecer;

import exceptions.ValidacaoException;
import models.AnaliseTecnica;
import models.AnalistaTecnico;
import models.TipoResultadoAnalise;
import models.UsuarioAnalise;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import utils.Mensagem;

public class ParecerNaoValidadoTecnicoCoordenador extends TipoResultadoAnaliseChain<AnaliseTecnica> {

	public ParecerNaoValidadoTecnicoCoordenador() {
		super(TipoResultadoAnalise.PARECER_NAO_VALIDADO);
	}

	@Override
	protected void validaParecer(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica, UsuarioAnalise usuarioExecutor) {
		
		analiseTecnica.tipoResultadoValidacaoCoordenador = novaAnaliseTecnica.tipoResultadoValidacaoCoordenador;
		analiseTecnica.parecerValidacaoCoordenador = novaAnaliseTecnica.parecerValidacaoCoordenador;
		analiseTecnica.usuarioValidacaoCoordenador = usuarioExecutor;
		analiseTecnica.ativo = false;
		
		validarAnaliseTecnica(analiseTecnica, novaAnaliseTecnica);
		
		analiseTecnica._save();
					
		criarNovaAnalise(analiseTecnica, novaAnaliseTecnica.analistaTecnico.usuario, usuarioExecutor);
		
		analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_TECNICO_PELO_COORDENADOR, usuarioExecutor);
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
		novaAnalise.usuarioValidacaoCoordenador = usuarioValidacao;
		novaAnalise.analistaTecnico = new AnalistaTecnico(novaAnalise, usuarioAnalista);
		
		novaAnalise._save();

	}

	private void validarAnaliseTecnica(AnaliseTecnica analiseTecnica, AnaliseTecnica novaAnaliseTecnica) {
		
		analiseTecnica.validarTipoResultadoValidacaoCoordenador();
		
		analiseTecnica.validarParecerValidacaoCoordenador();
		
		if (novaAnaliseTecnica.analistaTecnico == null) {
			
			throw new ValidacaoException(Mensagem.ANALISE_JURIDICA_CONSULTOR_NAO_INFORMADO);
		}
		
	}
}
