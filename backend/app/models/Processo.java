package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import builders.CriteriaBuilder;
import builders.ProcessoBuilder;
import builders.ProcessoBuilder.FiltroProcesso;
import exceptions.AppException;
import exceptions.ValidacaoException;
import models.licenciamento.Caracterizacao;
import models.licenciamento.Empreendimento;
import models.portalSeguranca.Perfil;
import models.portalSeguranca.Setor;
import models.portalSeguranca.Usuario;
import models.tramitacao.AcaoDisponivelObjetoTramitavel;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.Condicao;
import models.tramitacao.HistoricoTramitacao;
import models.tramitacao.ObjetoTramitavel;
import models.tramitacao.Tramitacao;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import security.Auth;
import security.InterfaceTramitavel;
import security.UsuarioSessao;
import utils.Configuracoes;
import utils.DateUtil;
import utils.ListUtil;
import utils.Mensagem;

@Entity
@Table(schema="analise", name="processo")
public class Processo extends GenericModel implements InterfaceTramitavel{
	
	private static final String SEQ = "analise.processo_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;
	
	@Required
	public String numero;
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_empreendimento")
	public Empreendimento empreendimento;
	
	@Column(name = "id_objeto_tramitavel")
	public Long idObjetoTramitavel;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_objeto_tramitavel", referencedColumnName = "id_objeto_tramitavel", insertable=false, updatable=false)
	public ObjetoTramitavel objetoTramitavel;
	
	@ManyToMany
	@JoinTable(schema="analise", name="rel_processo_caracterizacao", 
		joinColumns= @JoinColumn(name="id_processo"),
		inverseJoinColumns = @JoinColumn(name="id_caracterizacao"))
	public List<Caracterizacao> caracterizacoes;
	
	@OneToMany(mappedBy="processo")
	public List<Analise> analises;
	
	@Required
	@Column(name="data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_processo_anterior")
	public Processo processoAnterior;

	@Transient
	public transient Tramitacao tramitacao = new Tramitacao();
	
	@Transient
	public Analise analise;
	
	@Override
	public Processo save() {

		// Inicia a tramitacao e chama o metodo salvaObjetoTramitavel() que cria o Processo ja com o objeto tramitavel setado que eh obrigatorio.
		tramitacao.iniciar(this, null, Tramitacao.LICENCIAMENTO_AMBIENTAL);
			
		return this;
	}

	@Override
	public Long getIdObjetoTramitavel() {
		return this.idObjetoTramitavel;
	}

	@Override
	public void setIdObjetoTramitavel(Long idObjetoTramitavel) {
		this.idObjetoTramitavel = idObjetoTramitavel;
	}

	@Override
	public List<AcaoDisponivelObjetoTramitavel> getAcoesDisponiveisTramitacao() {
		
		if (this.idObjetoTramitavel == null)
			return null;

		ObjetoTramitavel objetoTramitavel = ObjetoTramitavel.findById(this.idObjetoTramitavel);
		return objetoTramitavel.acoesDisponiveis;
		
	}

	@Override
	public void salvaObjetoTramitavel() {
		super.save();
	}
	
	public void vincularConsultor(Usuario consultor, Usuario usuarioExecutor) {
		
		ConsultorJuridico.vincularAnalise(consultor, AnaliseJuridica.findByProcesso(this), usuarioExecutor);
		
		tramitacao.tramitar(this, AcaoTramitacao.VINCULAR_CONSULTOR, usuarioExecutor, consultor);
		Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.objetoTramitavel.id), usuarioExecutor);
		
	}
	
	public void vincularAnalista(Usuario analista, Usuario usuarioExecutor, String justificativaCoordenador) {
		
		AnalistaTecnico.vincularAnalise(analista, AnaliseTecnica.findByProcesso(this), usuarioExecutor, justificativaCoordenador);
		
		tramitacao.tramitar(this, AcaoTramitacao.VINCULAR_ANALISTA, usuarioExecutor, analista);
		Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.objetoTramitavel.id), usuarioExecutor);
		
	}
	
	public void vincularGerenteTecnico(Usuario gerente, Usuario usuarioExecutor) {
		
		GerenteTecnico.vincularAnalise(gerente, usuarioExecutor, AnaliseTecnica.findByProcesso(this));
		
		tramitacao.tramitar(this, AcaoTramitacao.VINCULAR_GERENTE, usuarioExecutor, gerente);
		Setor.setHistoricoTramitacao(HistoricoTramitacao.getUltimaTramitacao(this.objetoTramitavel.id), usuarioExecutor);
	}	

	private static ProcessoBuilder commonFilterProcesso(FiltroProcesso filtro, UsuarioSessao usuarioSessao) {
		
		ProcessoBuilder processoBuilder = new ProcessoBuilder()
			.filtrarPorNumeroProcesso(filtro.numeroProcesso)
			.filtrarPorIdMunicipio(filtro.idMunicipioEmpreendimento)
			.filtrarPorCpfCnpjEmpreendimento(filtro.cpfCnpjEmpreendimento)
			.filtrarPorIdTipologia(filtro.idTipologiaEmpreendimento)
			.filtrarPorIdAtividade(filtro.idAtividadeEmpreendimento)
			.filtrarPorIdCondicao(filtro.idCondicaoTramitacao)
			.filtrarPorPeriodoProcesso(filtro.periodoInicial, filtro.periodoFinal);
			
				
		commonFilterProcessoAnaliseJuridica(processoBuilder, filtro, usuarioSessao.id);
		
		commonFilterProcessoAnaliseTecnica(processoBuilder, filtro, usuarioSessao);
		
		commonFilterProcessoAprovador(processoBuilder, filtro, usuarioSessao);
		
		return processoBuilder;
	}

	private static void commonFilterProcessoAprovador(ProcessoBuilder processoBuilder, FiltroProcesso filtro, 
			UsuarioSessao usuarioSessao) {
		
		if (usuarioSessao.perfilSelecionado.id != Perfil.APROVADOR) {
			return;
		}
		
		processoBuilder.filtrarPorIdConsultorJuridico(filtro.idConsultorJuridico);
		
		if (filtro.idSetorCoordenadoria != null) {
			
			Setor setor = Setor.findById(filtro.idSetorCoordenadoria);
			
			/**
			 * Nível 1 corresponde aos filhos e nível 2 aos netos e assim por diante na hieraquia. 
			 * Neste caso, colocamos o nível 1, pois a hierarquia é Coordenadoria/Gerência(1)
			 */
			if (setor != null) {
				
				processoBuilder.filtrarPorIdsSetores(setor.getIdsSetoresByNivel(1));
			}
		}
		
		if (filtro.filtrarPorUsuario != null && filtro.filtrarPorUsuario && filtro.idCondicaoTramitacao != null && 
			filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_ASSINATURA_APROVADOR)){
			
			Setor setor = Setor.findById(usuarioSessao.setorSelecionado.id);
			
			/**
			 * Nível 1 corresponde aos filhos e nível 2 aos netos e assim por diante na hieraquia. 
			 * Neste caso, colocamos o nível dois, pois a hierarquia é Diretoria/Coordenadoria(1)/Gerência(2)
			 */
			if (setor != null) {
				
				processoBuilder.filtrarPorIdsSetores(setor.getIdsSetoresByNivel(2));
			}
		}		
	}

	private static void commonFilterProcessoAnaliseJuridica(ProcessoBuilder processoBuilder,
			FiltroProcesso filtro, Long idUsuarioLogado) {

		if (!filtro.isAnaliseJuridica) {
			
			return;
		}		
		
		processoBuilder.filtrarAnaliseJuridicaAtiva();
		
		if (filtro.filtrarPorUsuario != null && filtro.filtrarPorUsuario && filtro.idCondicaoTramitacao != null && 
		   (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_ANALISE_JURIDICA) || 
			filtro.idCondicaoTramitacao.equals(Condicao.EM_ANALISE_JURIDICA))) {
					
			processoBuilder.filtrarPorIdConsultorJuridico(idUsuarioLogado);
		}
		
		if (filtro.idCondicaoTramitacao != null && 
			   filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VALIDACAO_JURIDICA)) {
						
				processoBuilder.filtrarPorIdUsuarioValidacao(idUsuarioLogado);
		}

		
	}
	
	private static void commonFilterProcessoAnaliseTecnica(ProcessoBuilder processoBuilder, FiltroProcesso filtro,
			UsuarioSessao usuarioSessao) {
		
		if (!filtro.isAnaliseTecnica) {
			
			return;
		}
		
		processoBuilder.filtrarAnaliseTecnicaAtiva(filtro.isAnaliseTecnicaOpcional);
		processoBuilder.filtrarPorIdSetor(filtro.idSetorGerencia);
		
		if (filtro.filtrarPorUsuario == null || !filtro.filtrarPorUsuario || filtro.idCondicaoTramitacao == null) {
			
			processoBuilder.filtrarPorIdAnalistaTecnico(filtro.idAnalistaTecnico, false);
			
			return;		
		}
		
		if (usuarioSessao.setorSelecionado == null) {
			
			throw new ValidacaoException(Mensagem.ANALISE_TECNICA_USUARIO_SEM_SETOR);
		}
		
		if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_ANALISE_TECNICA) || 
				filtro.idCondicaoTramitacao.equals(Condicao.EM_ANALISE_TECNICA)) {
			
			processoBuilder.filtrarPorIdAnalistaTecnico(usuarioSessao.id, filtro.isAnaliseTecnicaOpcional);
			processoBuilder.filtrarPorIdSetor(usuarioSessao.setorSelecionado.id);
			
		} else {
			
			processoBuilder.filtrarPorIdAnalistaTecnico(filtro.idAnalistaTecnico, false);
		}
		
		if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VINCULACAO_TECNICA_PELO_GERENTE)) {
			
			processoBuilder.filtrarPorIdGerenteTecnico(usuarioSessao.id);
			processoBuilder.filtrarPorIdSetor(usuarioSessao.setorSelecionado.id);
		}
		
		/**
		 * Nível 1 corresponde aos filhos e nível 2 aos netos e assim por diante na hieraquia. 
		 * Neste caso, colocamos o nível 1, pois a hierarquia é Coordenadoria/Gerência(1)
		 */
		if (filtro.idSetorGerencia == null && filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VINCULACAO_TECNICA_PELO_COORDENADOR)) {
			
			processoBuilder.filtrarPorIdsSetores(usuarioSessao.setorSelecionado.getIdsSetoresByNivel(1));			
		}
		
		/**
		 * Nível 1 corresponde aos filhos e nível 2 aos netos e assim por diante na hieraquia. 
		 * Neste caso, colocamos o nível 1, pois a hierarquia é Coordenadoria/Gerência(1)
		 */
		if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR)) {
							
			processoBuilder.filtrarPorIdUsuarioValidacaoTecnica(usuarioSessao.id);
			processoBuilder.filtrarPorIdsSetores(usuarioSessao.setorSelecionado.getIdsSetoresByNivel(1));	
		}
		
		if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VALIDACAO_TECNICA_PELO_GERENTE)) {
			
			processoBuilder.filtrarPorIdUsuarioValidacaoTecnicaGerente(usuarioSessao.id);
			processoBuilder.filtrarPorIdSetor(usuarioSessao.setorSelecionado.id);
		}		
	}

	public static List listWithFilter(FiltroProcesso filtro, UsuarioSessao usuarioSessao) {
				
		ProcessoBuilder processoBuilder = commonFilterProcesso(filtro, usuarioSessao)
			.comTiposLicencas()
			.groupByIdProcesso()
			.groupByNumeroProcesso()
			.groupByCpfCnpjEmpreendimento()
			.groupByDenominacaoEmpreendimento()
			.groupByMunicipioEmpreendimento()
			.groupByDataVencimentoPrazoAnalise()
			.groupByIdAnalise()
			.groupByDiasAnalise()
			.groupByDataCadastroAnalise();
									
		listWithFilterAnaliseJuridica(processoBuilder, filtro);
		
		listWithFilterAnaliseTecnica(processoBuilder, filtro);
		
		listWithFilterAprovador(processoBuilder, usuarioSessao);
					
		return processoBuilder
			.fetch(filtro.paginaAtual.intValue(), filtro.itensPorPagina.intValue())				
			.list();		
	}
	

	private static void listWithFilterAnaliseJuridica(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {
		
		if (!filtro.isAnaliseJuridica) {
			
			return;
		}
		
		processoBuilder.groupByIdAnaliseJuridica()
			.groupByDataFinalAnaliseJuridica()
			.groupByDataVencimentoPrazoAnaliseJuridica()
			.groupByRevisaoSolicitadaAnaliseJuridica()	
			.groupByDiasAnaliseJuridica()
			.groupByNotificacaoAtendidaAnaliseJuridica()
			.orderByDataVencimentoPrazoAnaliseJuridica();
	}
	
	private static void listWithFilterAnaliseTecnica(ProcessoBuilder processoBuilder,
			FiltroProcesso filtro) {
		
		if (!filtro.isAnaliseTecnica) {
			
			return;
		}
		
		processoBuilder.groupByIdAnaliseTecnica(filtro.isAnaliseTecnicaOpcional)
			.groupByDataVencimentoPrazoAnaliseTecnica(filtro.isAnaliseTecnicaOpcional)
			.groupByRevisaoSolicitadaAnaliseTecnica(filtro.isAnaliseTecnicaOpcional)
			.groupByDataFinalAnaliseTecnica(filtro.isAnaliseTecnicaOpcional)
			.groupByDiasAnaliseTecnica()
			.groupByNotificacaoAtendidaAnaliseTecnica(filtro.isAnaliseTecnicaOpcional)
			.orderByDataVencimentoPrazoAnaliseTecnica();
	}
	
	private static void listWithFilterAprovador(ProcessoBuilder processoBuilder, UsuarioSessao usuarioSessao) {
		
		if (usuarioSessao.perfilSelecionado.id != Perfil.APROVADOR) {
			
			return;
		}
		
		processoBuilder.groupByIdAnalise()
			.groupByDiasAprovador();
	}

	public static Long countWithFilter(FiltroProcesso filtro, UsuarioSessao usuarioSessao) {
		
		ProcessoBuilder processoBuilder = commonFilterProcesso(filtro, usuarioSessao)
			.addPessoaEmpreendimentoAlias()
			.addEstadoEmpreendimentoAlias()
			.addAnaliseAlias()
			.count();
						
		countWithFilterAnaliseJuridica(processoBuilder, filtro);
		
		countWithFilterAnaliseTecnica(processoBuilder, filtro);
			
		Object qtdeTotalItens = processoBuilder.unique();
		
		return ((Map<String, Long>) qtdeTotalItens).get("total"); 
	}
	
	private static void countWithFilterAnaliseTecnica(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {
		
		if (!filtro.isAnaliseTecnica) {
			
			return;
		}
		
		processoBuilder.addAnaliseTecnicaAlias(filtro.isAnaliseTecnicaOpcional);		
	}

	private static void countWithFilterAnaliseJuridica(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {
		
		if (!filtro.isAnaliseJuridica) {
			
			return;
		}
		
		processoBuilder.addAnaliseJuridicaAlias();		
	}

	public Caracterizacao getCaracterizacao() {
		return caracterizacoes.get(0);
	}
	
	public Analise getAnalise() {

		if(this.analise != null)
			return this.analise;

		if(this.analises != null && !this.analises.isEmpty())
			for(Analise analise : this.analises)
				if(analise.ativo)
					this.analise = analise;
		
		if(this.analise == null)
			this.analise = Analise.findByProcesso(this);

		return this.analise;
		
	}
	
	//Retorna o historico da tramitação com o tempo que o objeto tramitavel permaneceu na condição
	public List<HistoricoTramitacao> getHistoricoTramitacao() {

		List<HistoricoTramitacao> historicosTramitacoes = HistoricoTramitacao.getByObjetoTramitavel(this.idObjetoTramitavel);

		Date dataAtual = new Date();

		//Lógica que verifica os dias que ficou na condição
		for (int i = 0; i < historicosTramitacoes.size(); i++) {

			if(i == 0)
				historicosTramitacoes.get(i).tempoPermanencia = DateUtil.getDiferencaEmDiasHorasMinutos(historicosTramitacoes.get(i).dataInicial, dataAtual);
			else
				historicosTramitacoes.get(i).tempoPermanencia = DateUtil.getDiferencaEmDiasHorasMinutos(historicosTramitacoes.get(i).dataInicial, historicosTramitacoes.get(i - 1).dataInicial);
		}
		
		//Lógica que adiciona a data final da condição
		for (int i = historicosTramitacoes.size() - 1; i >= 0; i--) {

			if(i == 0)
				historicosTramitacoes.get(i).dataFinal = null;
			else
				historicosTramitacoes.get(i).dataFinal = historicosTramitacoes.get(i - 1).dataInicial;
		}

		return historicosTramitacoes;

	}
	
	public List<String> getTiposLicenca() {
		
		List<String> tiposLicenca = new ArrayList<>();
		
		for(Caracterizacao caracterizacao : this.caracterizacoes) {
			
			String temp = caracterizacao.tipoLicenca.nome + " (" + caracterizacao.tipoLicenca.validadeEmAnos + " anos)";
			tiposLicenca.add(temp);
			
		}
		
		return tiposLicenca;
		
	}
	
	public List<Caracterizacao> getCaracterizacoesNaoArquivadas() {
		
		List<Caracterizacao> caracterizacoes = new ArrayList<>();
		
		for(Caracterizacao caracterizacao : this.caracterizacoes)
			if(!caracterizacao.isArquivada())
				caracterizacoes.add(caracterizacao);
		
		return caracterizacoes;
		
	}

}
