package models.tramitacao;

import br.ufla.lemaf.tramitacao.exception.TramitacaoException;
import br.ufla.lemaf.tramitacao.service.TramitacaoService;
import br.ufla.lemaf.tramitacao.vo.*;
import exceptions.AppException;
import models.UsuarioAnalise;
import models.tramitacao.enums.FluxoTramitacao;
import models.tramitacao.enums.TipoObjetoTramitavel;
import play.Logger;
import utils.Mensagem;

import java.util.ArrayList;
import java.util.List;

public class Tramitacao {
	
	public TramitacaoService service = new TramitacaoService();
	
	private final int POSICAO_FLUXO = 0;
	private final int POSICAO_ACAO = 1;
	private final int POSICAO_TIPO_OBJETO_TRAMITAVEL= 2;
	
	//Ao inserir um fluxo tambem inserir um array com os seguintes valores: id fluxo tramitação, id ação inicial do fluxo e id do tipo objeto tramitavel na respectiva ordem.
	public static final Long []LICENCIAMENTO_AMBIENTAL = {FluxoTramitacao.PROCESSO_ANALISE_LICENCIAMENTO.getId(), AcaoTramitacao.VINCULAR_CONSULTOR, TipoObjetoTramitavel.LICENCIAMENTO_AMBIENTAL.getId()};
	public static final Long []MANEJO_DIGITAL = {FluxoTramitacao.PROCESSO_MANEJO_DIGITAL.getId(), AcaoTramitacao.INICIAR_ANALISE_SHAPE, TipoObjetoTramitavel.MANEJO_DIGITAL.getId()};
	
	/*Método que inicia o fluxo de tramitação recebendo o objeto tramitavel 
	(Que implementa a interface Tramitavel e o usuario destino do objeto tramitavel) */
	public void iniciar(Tramitavel objetoTramitavel, UsuarioAnalise usuarioDestino, Long[] fluxo) {
		
		if (objetoTramitavel == null)
			throw new IllegalArgumentException(Mensagem.TRAMITACAO_OBJETO_TRAMITAVEL_OBRIGATORIO.getTexto());
		
		if (fluxo == null)
			throw new IllegalArgumentException(Mensagem.TRAMITACAO_FLUXO_OBRIGATORIO.getTexto());
		
		try {
			InicioTramitacaoRequestVO request = new InicioTramitacaoRequestVO()
									.withIdFluxo(fluxo[POSICAO_FLUXO])
									.withIdTipoObjetoTramitavel(fluxo[POSICAO_TIPO_OBJETO_TRAMITAVEL]);
			
			if (usuarioDestino != null) {
				request.withUsuarioDestino(usuarioDestino.id);
				request.withUsuarioExecutor(usuarioDestino.id);
			}
			
			InicioTramitacaoResponseVO response = service.iniciarTramitacao(request);
			objetoTramitavel.setIdObjetoTramitavel(response.getIdObjetoTramitavel()); // Aqui já é setado objeto Tramitavel criado
			objetoTramitavel.salvaObjetoTramitavel();
			
		} catch (Exception e) {
		    	Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.EXCEPTION_TRAMITACAO);
		}
	}
	
	// Método que tramita recebendo o objeto tramitavel, ação da tramitação.
		public void tramitar(Tramitavel tramitavel, Long idAcaoTramitacao) {
			
			List<Tramitavel> tramitaveis = new ArrayList<Tramitavel>();
			tramitaveis.add(tramitavel);
			
			this.tramitar(tramitaveis, idAcaoTramitacao, null, null, null);
		}
	
	// Método que tramita recebendo o objeto tramitavel, ação da tramitação e o usuario que está realizando a tramitação
	public void tramitar(Tramitavel tramitavel, Long idAcaoTramitacao, UsuarioAnalise usuarioExecutor) {
		
		List<Tramitavel> tramitaveis = new ArrayList<Tramitavel>();
		tramitaveis.add(tramitavel);
		
		this.tramitar(tramitaveis, idAcaoTramitacao, usuarioExecutor, null, null);
	}
	
	// Método que tramita recebendo o objeto tramitavel, ação da tramitação, o usuario que está realizando a tramitação e uma observação
	public void tramitar(Tramitavel tramitavel, Long idAcaoTramitacao, UsuarioAnalise usuarioExecutor, String observacao) {
		
		List<Tramitavel> tramitaveis = new ArrayList<Tramitavel>();
		tramitaveis.add(tramitavel);
		
		this.tramitar(tramitaveis, idAcaoTramitacao, usuarioExecutor, null, observacao);
	}
	
	// Método que tramita recebendo o objeto tramitavel, ação da tramitação, o usuario que está realizando a tramitação, o usuario para qual será tramitado o objeto e uma observação
	public void tramitar(Tramitavel tramitavel, Long idAcaoTramitacao, UsuarioAnalise usuarioExecutor, UsuarioAnalise usuarioDestino, String observacao) {
		
		List<Tramitavel> tramitaveis = new ArrayList<Tramitavel>();
		tramitaveis.add(tramitavel);
		
		this.tramitar(tramitaveis, idAcaoTramitacao, usuarioExecutor, usuarioDestino,  observacao);
	}
	
	// Método que tramita recebendo o objeto tramitavel, ação da tramitação, o usuario que está realizando a tramitação, o usuario para qual será tramitado o objeto e uma observação
		public void tramitar(Tramitavel tramitavel, Long idAcaoTramitacao, UsuarioAnalise usuarioExecutor, UsuarioAnalise usuarioDestino) {
			
			List<Tramitavel> tramitaveis = new ArrayList<Tramitavel>();
			tramitaveis.add(tramitavel);
			
			this.tramitar(tramitaveis, idAcaoTramitacao, usuarioExecutor, usuarioDestino,  null);
		}
		
	// Método que tramita recebendo uma lista de objetos tramitaveis, ação da tramitação
	public void tramitar(List<? extends Tramitavel> tramitaveis, Long idAcaoTramitacao) {
			
		this.tramitar(tramitaveis, idAcaoTramitacao, null, null, null);
	}
	
	// Método que tramita recebendo uma lista de objetos tramitaveis, ação da tramitação, o usuario que está realizando a tramitação e o usuario destino da tramitação
	public void tramitar(List<? extends Tramitavel> tramitaveis, Long idAcaoTramitacao, UsuarioAnalise usuarioExecutor, UsuarioAnalise usuarioDestino) {
		
		this.tramitar(tramitaveis, idAcaoTramitacao, usuarioExecutor, usuarioDestino, null);
	}
	
	/*Método que tramita recebendo uma lista de objetos tramitaveis, ação da tramitação, o usuario que está realizando a tramitação, o usuario destino da tramitação e uma observação
	Esse método criar o objeto VO necessario para solicitar a tramitação*/
	private void tramitar(List<? extends Tramitavel> tramitaveis, Long idAcaoTramitacao, UsuarioAnalise usuarioExecutor, UsuarioAnalise usuarioDestino, String observacao) {

		validateTramitar(tramitaveis, idAcaoTramitacao, usuarioExecutor);

		TramiteRequestVO tramites = new TramiteRequestVO();

		for (Tramitavel tramitavel : tramitaveis) {

			TramiteVO tramite = new TramiteVO()
					.withObjetoTramitavel(tramitavel.getIdObjetoTramitavel())
					.withAcao(idAcaoTramitacao)
					.withObservacao(observacao);

			if (usuarioExecutor != null)
				tramite.withUsuarioExecutor(usuarioExecutor.id);
			
			if (usuarioDestino != null)
				tramite.withUsuarioDestino(usuarioDestino.id);

			tramites.addTramite(tramite);
		}

		tramitar(tramites);
	}
	
	// Método que utiliza a interface de tramitação para tramita os objetos
	public void tramitar(TramiteRequestVO tramites) {

		try {

			TramiteResponseVO response = service.tramitar(tramites);

			if (response == null || !TramiteResponseVO.SUCESSO.equals(response.getStatus()))
				throw new TramitacaoException(response.getMensagem());

		} catch (Exception e) {
			Logger.error(e, e.getMessage());
			throw new AppException(Mensagem.EXCEPTION_TRAMITACAO);
		}
	}
	
	// Método que valida os parametros da tramitação
	private void validateTramitar(List<? extends Tramitavel> tramitaveis, Long idAcaoTramitacao, UsuarioAnalise usuarioExecutor) {

		if (tramitaveis == null || tramitaveis.isEmpty())
			throw new IllegalArgumentException(Mensagem.TRAMITACAO_OBJETO_OBRIGATORIO_TRAMITAR.getTexto());

		for (Tramitavel tramitavel : tramitaveis) {

			if (tramitavel == null || tramitavel.getIdObjetoTramitavel() == null)
				throw new IllegalArgumentException(Mensagem.TRAMITACAO_OBJETO_TRAMITAVEL_VAZIO.getTexto());
		}

		if (idAcaoTramitacao == null)
			throw new IllegalArgumentException(Mensagem.TRAMITACAO_ACAO_OBRIGATORIA.getTexto());
	}
	
	// Retorna o historico do objeto tramitavél
	public List<HistoricoTramitacao> findHistorico(Tramitavel tramitavel) {
		
		return HistoricoTramitacao.find("idObjetoTramitavel = :idObjetoTramitavel) order by data desc")
				.setParameter("idObjetoTramitavel", tramitavel.getIdObjetoTramitavel())
				.fetch();
	}
	
	// Retorna se a ação está disponivel para o objeto tramitavel
	public boolean isAcaoDisponivel(Long idAcao, Tramitavel objeto) {
		
		if (idAcao == null)
			throw new IllegalArgumentException(Mensagem.TRAMITACAO_ACAO_DISPONIVEL_OBRIGATORIA.getTexto());
		
		if (objeto == null || objeto.getIdObjetoTramitavel() == null)
			throw new IllegalArgumentException(Mensagem.TRAMITACAO_OBJETO_TRAMITAVEL_VAZIO.getTexto());
		
		AcaoDisponivelObjetoTramitavel acaoDisponivel = AcaoDisponivelObjetoTramitavel.find("idAcao = :idAcao AND idObjetoTramitavel = :idObjetoTramitavel")
				.setParameter("idAcao", idAcao)
				.setParameter("idObjetoTramitavel", objeto.getIdObjetoTramitavel())
				.first();

		return acaoDisponivel != null;
	}
	
	//Verifica se o usuário é o responsavel pelo usuário
	public boolean isUsuarioResponsavelObjeto(Long idPessoa, Tramitavel objeto) {
		
		ObjetoTramitavel objetoTramitavel = ObjetoTramitavel.find("usuarioResponsavel.id = :idPessoa AND id = :idObjetoTramitavel")
				.setParameter("idPessoa", idPessoa)
				.setParameter("idObjetoTramitavel", objeto.getIdObjetoTramitavel())
				.first();

		return objetoTramitavel != null;
	}
	
	public List<AcaoDisponivelObjetoTramitavel> findAcoesDisponiveisObjetosTramitaveis(List<Long> idsObjetosTramitaveis) {
		
		if (idsObjetosTramitaveis == null || idsObjetosTramitaveis.isEmpty())
			return null;
		
		return AcaoDisponivelObjetoTramitavel.find("idObjetoTramitavel IN (:idsObjetosTramitaveis)")
				.setParameter("idsObjetosTramitaveis", idsObjetosTramitaveis).fetch();
	}
}
