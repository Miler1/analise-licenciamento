package models;

import builders.ProcessoBuilder;
import builders.ProcessoBuilder.FiltroProcesso;
import com.vividsolutions.jts.geom.Geometry;
import enums.TipoSobreposicaoDistanciaEnum;
import exceptions.ValidacaoException;
import models.EntradaUnica.CodigoPerfil;
import models.licenciamento.*;
import models.tramitacao.*;
import org.hibernate.criterion.Restrictions;
import org.geotools.feature.SchemaException;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import security.Auth;
import security.InterfaceTramitavel;
import services.IntegracaoEntradaUnicaService;
import utils.*;

import javax.persistence.*;
import javax.validation.ValidationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static models.licenciamento.Caracterizacao.OrigemSobreposicao.*;

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

	@OneToOne
	@JoinColumn(name = "id_caracterizacao", referencedColumnName = "id")
	public Caracterizacao caracterizacao;

	@OneToOne(mappedBy = "processo")
	public Analise analise;

	@Column
	public Boolean renovacao;

	@Required
	@Column(name="data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	@ManyToOne
	@JoinColumn(name = "id_processo_anterior")
	public Processo processoAnterior;

	@Column
	public Boolean retificacao;

	@Column
	public Boolean ativo;

	@ManyToOne
	@JoinColumn(name = "id_origem_notificacao")
	public OrigemNotificacao origemNotificacao;

	@Transient
	public transient Tramitacao tramitacao = new Tramitacao();

	@Transient
	public DesvinculoAnaliseGeo desvinculoAnaliseGeoRespondido;

	@Transient
	public static int indexDadosRestricoes;

	@Transient
	public static int indexDadosAtividades;

	@Transient
	public static int indexDadosGeometriasAtividade;

	@Transient
	public static int indexDadosGeometriasComplexo;

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

	public void vincularConsultor(UsuarioAnalise consultor, UsuarioAnalise usuarioExecutor) {

		ConsultorJuridico.vincularAnalise(consultor, AnaliseJuridica.findByProcessoAtivo(this), usuarioExecutor);

		tramitacao.tramitar(this, AcaoTramitacao.VINCULAR_CONSULTOR, usuarioExecutor, consultor);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.objetoTramitavel.id), usuarioExecutor);

	}

	public void vincularAnalista(UsuarioAnalise analista, UsuarioAnalise usuarioExecutor, String justificativaCoordenador) {

		AnalistaTecnico.vincularAnalise(analista, AnaliseTecnica.findByProcessoAtivo(this), usuarioExecutor, justificativaCoordenador);
		tramitacao.tramitar(this, AcaoTramitacao.VINCULAR_ANALISTA, usuarioExecutor, analista);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.objetoTramitavel.id), usuarioExecutor);

	}

	public void vincularGerente(UsuarioAnalise gerente, UsuarioAnalise usuarioExecutor) {
		
		Gerente.vincularAnalise(gerente, usuarioExecutor, AnaliseTecnica.findByProcessoAtivo(this));
		tramitacao.tramitar(this, AcaoTramitacao.VINCULAR_GERENTE, usuarioExecutor, gerente);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.objetoTramitavel.id), usuarioExecutor);

	}

	private static ProcessoBuilder commonFilterProcesso(FiltroProcesso filtro, UsuarioAnalise usuarioSessao) {

		ProcessoBuilder processoBuilder = new ProcessoBuilder()
				.filtrarPorNumeroProcesso(filtro.numeroProcesso)
				.filtrarPorIdMunicipio(filtro.idMunicipioEmpreendimento)
				.filtrarPorCpfCnpjEmpreendimento(filtro.cpfCnpjEmpreendimento)
				.filtrarPorIdTipologia(filtro.idTipologiaEmpreendimento)
				.filtrarPorIdAtividade(filtro.idAtividadeEmpreendimento)
				.filtrarPorIdCondicao(filtro.idCondicaoTramitacao)
				.filtrarPorPeriodoProcesso(filtro.periodoInicial, filtro.periodoFinal);

		commonFilterProcessoAnaliseTecnica(processoBuilder, filtro, usuarioSessao);

		commonFilterProcessoAnaliseGeo(processoBuilder, filtro, usuarioSessao);

		commonFilterProcessoGerente(processoBuilder, filtro, usuarioSessao);

		commonFilterProcessoDiretor(processoBuilder, filtro, usuarioSessao);

		commonFilterProcessoPresidente(processoBuilder, filtro, usuarioSessao);

		commonFilterConsultarProcesso(processoBuilder, filtro, usuarioSessao);

		if(filtro.analiseAtiva){
			processoBuilder.filtrarPorAnaliseAiva(filtro.analiseAtiva);
		}

		return processoBuilder;

	}

	private static void commonFilterConsultarProcesso(ProcessoBuilder processoBuilder, FiltroProcesso filtroProcesso, UsuarioAnalise usuarioSessao) {

		if(!filtroProcesso.isConsultarProcessos) {

			return;

		}

		if(usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.ANALISTA_GEO)) {

			processoBuilder.filtrarPorIdAnalistaGeo(usuarioSessao.id, true);
			//processoBuilder.filtrarAnaliseGeoAtiva(false);
			processoBuilder.filtrarDesvinculoAnaliseGeoSemResposta();

		} else if(usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.ANALISTA_TECNICO)) {

			processoBuilder.filtrarPorIdAnalistaTecnico(usuarioSessao.id, true);
//			processoBuilder.filtrarAnaliseTecnicaAtiva(false);
			processoBuilder.filtrarDesvinculoAnaliseTecnicaSemResposta();

		} else if(usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.GERENTE)) {

			processoBuilder.filtrarPorIdAnalistaGeo(filtroProcesso.idAnalistaGeo, true);
			processoBuilder.filtrarPorIdAnalistaTecnico(filtroProcesso.idAnalistaTecnico, true);
			processoBuilder.filtrarPorIdDiretor(filtroProcesso.idDiretor, true);

		} else if(usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.DIRETOR)) {

			processoBuilder.filtrarPorIdDiretor(filtroProcesso.idDiretor, true);

		}

		processoBuilder.filtrarPorListaIdCondicao(filtroProcesso.listaIdCondicaoTramitacao);

		if (!usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.DIRETOR) &&
				!usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.PRESIDENTE)) {

			processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);

		}

	}

	private static void commonFilterProcessoAprovador(ProcessoBuilder processoBuilder, FiltroProcesso filtro,
													  UsuarioAnalise usuarioSessao) {

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

		if (!usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.APROVADOR)) {

			return;
		}

		processoBuilder.filtrarPorIdConsultorJuridico(filtro.idConsultorJuridico);

		if (filtro.siglaSetorCoordenadoria != null) {

			br.ufla.lemaf.beans.pessoa.Setor setor = integracaoEntradaUnica.getSetorBySigla(filtro.siglaSetorCoordenadoria);

			if (setor != null) {

				processoBuilder.filtrarPorSiglaSetores(integracaoEntradaUnica.getSiglasSetoresByNivel(setor.sigla, 1));
			}
		}

		if (filtro.filtrarPorUsuario != null && filtro.filtrarPorUsuario && filtro.idCondicaoTramitacao != null &&
				filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_ASSINATURA_PRESIDENE)){

			br.ufla.lemaf.beans.pessoa.Setor setor = integracaoEntradaUnica.getSetorBySigla(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);

			if (setor != null) {

				processoBuilder.filtrarPorSiglaSetores(integracaoEntradaUnica.getSiglasSetoresByNivel(setor.sigla, 2));
			}

		}

	}

	private static void commonFilterProcessoAnaliseTecnica(ProcessoBuilder processoBuilder, FiltroProcesso filtro,
														   UsuarioAnalise usuarioSessao) {

		if (!filtro.isAnaliseTecnica) {

			return;

		}

		processoBuilder.filtrarDesvinculoAnaliseTecnicaComResposta(true);
		processoBuilder.filtrarAnaliseTecnicaAtiva(filtro.isAnaliseTecnicaOpcional);
		processoBuilder.filtrarPorSiglaSetor(filtro.siglaSetorGerencia);

		if (usuarioSessao.usuarioEntradaUnica.setorSelecionado == null) {

			throw new ValidacaoException(Mensagem.ANALISE_TECNICA_USUARIO_SEM_SETOR);

		}

		if(filtro.idCondicaoTramitacao != null) {

			if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_ANALISE_TECNICA) ||
					filtro.idCondicaoTramitacao.equals(Condicao.EM_ANALISE_TECNICA)) {

				processoBuilder.filtrarPorIdAnalistaTecnico(usuarioSessao.id, true);
				processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);

			} else if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VINCULACAO_TECNICA_PELO_GERENTE)) {

				processoBuilder.filtrarPorIdGerente(usuarioSessao.id);
				processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);

			} else if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VALIDACAO_TECNICA_PELO_GERENTE)) {

				processoBuilder.filtrarPorIdUsuarioValidacaoTecnicaGerente(usuarioSessao.id);
				processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);

			}

		} else {

			if(filtro.idAnalistaTecnico == null) {

				filtro.idAnalistaTecnico = usuarioSessao.id;

			}

			processoBuilder.filtrarPorIdAnalistaTecnico(filtro.idAnalistaTecnico, false);

		}

	}

	private static void commonFilterProcessoGerente(ProcessoBuilder processoBuilder, FiltroProcesso filtro,
													UsuarioAnalise usuarioSessao) {

		if (!filtro.isGerente) {

			return;

		}

		if (usuarioSessao.usuarioEntradaUnica.setorSelecionado == null) {

			throw new ValidacaoException(Mensagem.ANALISE_GEO_USUARIO_SEM_SETOR);

		}

		if (filtro.listaIdCondicaoTramitacao != null && !filtro.listaIdCondicaoTramitacao.isEmpty()) {

			processoBuilder.filtrarPorListaIdCondicao(filtro.listaIdCondicaoTramitacao);

		}

		if (filtro.filtrarPorUsuario) {

			processoBuilder.filtrarIdGerente(usuarioSessao.id);

		}

		if (filtro.idAnalistaGeo != null) {

			processoBuilder.filtrarPorIdAnalistaGeo(filtro.idAnalistaGeo, false);

		}

		if (filtro.idAnalistaTecnico != null) {

			processoBuilder.filtrarPorIdAnalistaTecnico(filtro.idAnalistaTecnico, false);

		}

		processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);

	}

	private static void commonFilterProcessoDiretor(ProcessoBuilder processoBuilder, FiltroProcesso filtro,
													UsuarioAnalise usuarioSessao) {

		if (!filtro.isDiretor) {

			return;

		}

		if (usuarioSessao.usuarioEntradaUnica.setorSelecionado == null) {

			throw new ValidacaoException(Mensagem.NENHUM_DIRETOR_ENCONTRADO);

		}

		if (filtro.listaIdCondicaoTramitacao != null && !filtro.listaIdCondicaoTramitacao.isEmpty() || filtro.idCondicaoTramitacao != null) {

			processoBuilder.filtrarPorIdCondicao(filtro.idCondicaoTramitacao);

		}

		if (filtro.idAnalistaGeo != null){

			processoBuilder.filtrarPorIdAnalistaGeo(filtro.idAnalistaGeo, false);

		}

		if (filtro.idAnalistaTecnico != null){

			processoBuilder.filtrarPorIdAnalistaTecnico(filtro.idAnalistaTecnico, false);

		}

		if (filtro.filtrarPorUsuario) {

			processoBuilder.filtrarIdDiretor(usuarioSessao.id);

		}

		if (filtro.analiseAtiva){

			processoBuilder.filtrarPorAnaliseAiva(filtro.analiseAtiva);

		}

	}

	private static void commonFilterProcessoPresidente(ProcessoBuilder processoBuilder, FiltroProcesso filtro,
													UsuarioAnalise usuarioSessao) {

		if (!filtro.isPresidente) {

			return;

		}

		if (usuarioSessao.usuarioEntradaUnica.setorSelecionado == null) {

			throw new ValidacaoException(Mensagem.NENHUM_PRESIDENTE_ENCONTRADO);

		}

		if (filtro.listaIdCondicaoTramitacao != null && !filtro.listaIdCondicaoTramitacao.isEmpty() || filtro.idCondicaoTramitacao != null) {

			processoBuilder.filtrarPorIdCondicao(filtro.idCondicaoTramitacao);

		}

		if (filtro.idAnalistaGeo != null){

			processoBuilder.filtrarPorIdAnalistaGeo(filtro.idAnalistaGeo, false);

		}

		if (filtro.idAnalistaTecnico != null){

			processoBuilder.filtrarPorIdAnalistaTecnico(filtro.idAnalistaTecnico, false);

		}

		if (filtro.filtrarPorUsuario) {

			processoBuilder.filtrarIdPresidente(usuarioSessao.id);

		}

	}

	private static void commonFilterProcessoAnaliseGeo(ProcessoBuilder processoBuilder, FiltroProcesso filtro,
													   UsuarioAnalise usuarioSessao) {

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

		if (!filtro.isAnaliseGeo) {

			return;
		}

		processoBuilder.filtrarDesvinculoAnaliseGeoComResposta(true);
		processoBuilder.filtrarAnaliseGeoAtiva(filtro.isAnaliseGeoOpcional);
		processoBuilder.filtrarPorSiglaSetor(filtro.siglaSetorGerencia);

		if (filtro.idAnalistaGeo == null) {
			filtro.idAnalistaGeo = filtro.idUsuarioLogado;
		}

		if (filtro.listaIdCondicaoTramitacao != null && !filtro.listaIdCondicaoTramitacao.isEmpty()) {

			processoBuilder.filtrarPorListaIdCondicao(filtro.listaIdCondicaoTramitacao);

		}

		if (filtro.filtrarPorUsuario == null || !filtro.filtrarPorUsuario || filtro.idCondicaoTramitacao == null) {

			processoBuilder.filtrarPorIdAnalistaGeo(filtro.idAnalistaGeo, false);
			processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);

//			return;
		}

		if (usuarioSessao.usuarioEntradaUnica.setorSelecionado == null) {

			throw new ValidacaoException(Mensagem.ANALISE_GEO_USUARIO_SEM_SETOR);
		}

		if(filtro.idCondicaoTramitacao != null) {

			if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_ANALISE_GEO) ||
					filtro.idCondicaoTramitacao.equals(Condicao.EM_ANALISE_GEO)) {

				processoBuilder.filtrarPorIdAnalistaGeo(usuarioSessao.id, filtro.isAnaliseGeoOpcional);

				processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);
			
			} else {

				processoBuilder.filtrarPorIdAnalistaGeo(filtro.idAnalistaGeo, false);
			}

			if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VINCULACAO_GEO_PELO_GERENTE)) {

				processoBuilder.filtrarPorIdGerente(usuarioSessao.id);

				processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);
			}

			if (filtro.siglaSetorGerencia == null && filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VINCULACAO_GEO_PELO_GERENTE)) {

				processoBuilder.filtrarPorSiglaSetores(integracaoEntradaUnica.getSiglasSetoresByNivel(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla, 1));
			}

			if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VALIDACAO_GEO_PELO_GERENTE)) {

				processoBuilder.filtrarPorIdUsuarioValidacaoGeo(usuarioSessao.id);

				processoBuilder.filtrarPorSiglaSetores(integracaoEntradaUnica.getSiglasSetoresByNivel(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla, 1));
			}

			if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VALIDACAO_GEO_PELO_GERENTE)) {

				processoBuilder.filtrarPorIdUsuarioValidacaoGeoGerente(usuarioSessao.id);
				processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);
			}

			if (filtro.idCondicaoTramitacao.equals(Condicao.NOTIFICADO_PELO_ANALISTA_GEO)) {

				processoBuilder.filtrarPorIdUsuarioValidacaoGeo(usuarioSessao.id);

				processoBuilder.filtrarPorSiglaSetores(integracaoEntradaUnica.getSiglasSetoresByNivel(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla, 1));
			}

			if (filtro.idCondicaoTramitacao.equals(Condicao.NOTIFICADO_PELO_ANALISTA_GEO)) {

				processoBuilder.filtrarPorIdUsuarioValidacaoGeoGerente(usuarioSessao.id);
				processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);
			}

		} else {

			if(filtro.idAnalistaGeo == null) {

				filtro.idAnalistaGeo = usuarioSessao.id;

			}

			processoBuilder.filtrarPorIdAnalistaGeo(filtro.idAnalistaGeo, false);

		}

	}

	public static List getProcessosAnteriores(Processo processoAnterior) {

		List processosAnteriores = new ArrayList<>();
		Processo processoAnteriorAuxiliar;

		while (processoAnterior != null) {

			processosAnteriores.add(processoAnterior);

			processoAnterior.getInfoProcesso();

			if (processoAnterior.processoAnterior != null) {

				processoAnteriorAuxiliar = Processo.findById(processoAnterior.processoAnterior.id);
				processoAnterior = processoAnteriorAuxiliar;

			} else {

				processoAnterior = null;
			}

		}

		return processosAnteriores;

	}

	public static List listWithFilter(FiltroProcesso filtro, UsuarioAnalise usuarioSessao) {

		ProcessoBuilder processoBuilder = commonFilterProcesso(filtro, usuarioSessao)
				.comTiposLicencas()
				.groupByIdProcesso()
				.groupByNumeroProcesso()
				.groupByObjetoTramitavel()
				.groupByCpfCnpjEmpreendimento()
//				.groupByDenominacaoEmpreendimento()
				.groupByMunicipioEmpreendimento()
				.groupByDataVencimentoPrazoAnalise()
				.groupByDataVencimentoPrazoAnaliseGeo(!filtro.isAnaliseGeo)
				.groupByIdAnalise()
				.groupByIdAnaliseGeo(!filtro.isAnaliseGeo)
				.groupByIdAnalise()
				.groupByIdAnaliseGeo()
				.groupByIdAnaliseTecnica()
				.groupByDiasAnalise()
				.groupByDataCadastroAnalise()
				.groupByDataFinalAnaliseGeo(!filtro.isAnaliseGeo)
				.groupByDataFinalAnaliseTecnica(!filtro.isAnaliseTecnica)
				.groupByRenovacao()
				.groupByRetificacao()
				.groupByCaracterizacao()
				.groupByIdOrigemNotificacao()
				.groupByDiasAnaliseGeo()
				.groupByDiasAnaliseTecnica()
				.groupByIdAnalistaGeoAnterior()
				.groupByIdAnalistaGeo()
				.groupByIdAnalistaTecnicoAnterior()
				.groupByIdAnalistaTecnico();

		listWithFilterAnaliseJuridica(processoBuilder, filtro);

		listWithFilterAnaliseTecnica(processoBuilder, filtro);

		listWithFilterAnaliseGeo(processoBuilder, filtro);

		listWithFilterDiretor(processoBuilder, filtro);

		listWithFilterPresidente(processoBuilder, filtro);

		listWithFilterGerente(processoBuilder, filtro);

		listWithFilterConsultaProcessos(processoBuilder, filtro);

		return processoBuilder
				.fetch(filtro.paginaAtual.intValue(), filtro.itensPorPagina.intValue())
				.list();
	}

	private static void listWithFilterConsultaProcessos(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {

		if (!filtro.isConsultarProcessos) {

			return;
		}

		processoBuilder
				.groupByDataVencimentoPrazoAnaliseGeo(true)
				.groupByDataFinalAnaliseGeo(!filtro.isAnaliseGeo)
				.groupByDataFinalAnaliseTecnica(!filtro.isAnaliseTecnica)
				.groupByPrazoAnaliseGerente();

	}

	private static void listWithFilterGerente(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {

		if (!filtro.isGerente) {

			return;
		}

		processoBuilder.groupByIdAnaliseGeo(true)
				.groupByIdAnaliseTecnica(true)
				.groupByPrazoAnaliseGerente()
				.orderByPrazoAnaliseGerente();

	}

	private static void listWithFilterDiretor(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {

		if (!filtro.isDiretor) {

			return;
		}

		processoBuilder.groupByIdAnaliseGeo(true)
				.groupByIdAnaliseTecnica(true)
				.groupByPrazoAnaliseGerente()
				.orderByPrazoAnaliseGerente();

	}

	private static void listWithFilterPresidente(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {

		if (!filtro.isPresidente) {

			return;
		}

		processoBuilder.groupByIdAnaliseGeo(true)
				.groupByIdAnaliseTecnica(true)
				.groupByPrazoAnaliseGerente()
				.orderByPrazoAnaliseGerente();

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
				.groupByNotificacaoAtendidaAnaliseTecnica(filtro.isAnaliseTecnicaOpcional)
				.groupByIdAnaliseTecnica(true)
				.orderByDataVencimentoPrazoAnaliseTecnica();

	}

	private static void listWithFilterAnaliseGeo(ProcessoBuilder processoBuilder,
												 FiltroProcesso filtro) {

		if (!filtro.isAnaliseGeo) {

			return;
		}

		processoBuilder.groupByIdAnaliseGeo(filtro.isAnaliseGeoOpcional)
				.groupByDataVencimentoPrazoAnaliseGeo(filtro.isAnaliseGeoOpcional)
				.groupByRevisaoSolicitadaAnaliseGeo(filtro.isAnaliseGeoOpcional)
				.groupByDataFinalAnaliseGeo(filtro.isAnaliseGeoOpcional)
				.groupByDesvinculoAnaliseGeo()
				.groupByNotificacaoAtendidaAnaliseGeo(filtro.isAnaliseGeoOpcional)
				.orderByDataVencimentoPrazoAnaliseGeo();
	}

	private static void listWithFilterAprovador(ProcessoBuilder processoBuilder, UsuarioAnalise usuarioSessao) {

		if (!usuarioSessao.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.APROVADOR)) {

			return;
		}

		processoBuilder.groupByIdAnalise()
				.groupByDiasAprovador();
	}

	public static Long countWithFilter(FiltroProcesso filtro, UsuarioAnalise usuarioSessao) {

		ProcessoBuilder processoBuilder = commonFilterProcesso(filtro, usuarioSessao)
//				.addPessoaEmpreendimentoAlias()
//				.addEstadoEmpreendimentoAlias()
				.addAnaliseAlias()
				.count();

		countWithFilterAnaliseJuridica(processoBuilder, filtro);

		countWithFilterAnaliseTecnica(processoBuilder, filtro);

		countWithFilterAnaliseGeo(processoBuilder, filtro);

		countWithFilterGerente(processoBuilder, filtro);

		countWithFilterDiretor(processoBuilder, filtro);

		countWithFilterPresidente(processoBuilder, filtro);

		Object qtdeTotalItens = processoBuilder.unique();

		return ((Map<String, Long>) qtdeTotalItens).get("total");
	}

	private static void countWithFilterAnaliseTecnica(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {

		if (!filtro.isAnaliseTecnica) {

			return;
		}

		processoBuilder.addAnaliseTecnicaAlias(filtro.isAnaliseTecnicaOpcional);

	}

	private static void countWithFilterAnaliseGeo(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {

		if (!filtro.isAnaliseGeo) {

			return;
		}

		processoBuilder.addAnaliseGeoAlias(filtro.isAnaliseGeoOpcional);
	}

	private static void countWithFilterGerente(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {

		if (!filtro.isGerente) {

			return;

		}

	}

	private static void countWithFilterDiretor(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {

		if (!filtro.isDiretor) {

			return;

		}

	}

	private static void countWithFilterPresidente(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {

		if (!filtro.isPresidente) {

			return;

		}

	}

	private static void countWithFilterAnaliseJuridica(ProcessoBuilder processoBuilder, FiltroProcesso filtro) {

		if (!filtro.isAnaliseJuridica) {

			return;
		}

		processoBuilder.addAnaliseJuridicaAlias();
	}

	//Retorna o historico da tramitação com o tempo que o objeto tramitavel permaneceu na condição
	public List<HistoricoTramitacao> getHistoricoTramitacao() {

		List<Long> idsObjetosTramitaveis = new ArrayList<>();

		Processo processoAnterior = this.processoAnterior;

		idsObjetosTramitaveis.add(this.idObjetoTramitavel);

		while (processoAnterior != null) {

			idsObjetosTramitaveis.add(processoAnterior.idObjetoTramitavel);
			processoAnterior = processoAnterior.processoAnterior;

		}

		List<HistoricoTramitacao> historicosTramitacoes = HistoricoTramitacao.getByObjetoTramitavel(idsObjetosTramitaveis);
//		List<HistoricoTramitacao> historicosTramitacoes = HistoricoTramitacao.getHistoricoTramitacaoByPerfil(this.idObjetoTramitavel, Auth.getUsuarioSessao().usuarioEntradaUnica.perfilSelecionado.codigo);


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

	public List<HistoricoTramitacao> getHistoricoTramitacaoAnaliseGeo() {

		List<HistoricoTramitacao> historicoTramitacoes = new ArrayList<>();

		return this.getHistoricoTramitacao()
				.stream()
				.filter(tramitacao -> this.analise != null && this.analise.analiseGeo != null)
				.collect(Collectors.toList());

	}

	public List<HistoricoTramitacao> getHistoricoTramitacaoAnaliseTecnica() {

		AnaliseGeo a = AnaliseGeo.findUltimaByAnalise(this.analise);

		Date dataPrimeiroHistorico = a.analise.processo.getHistoricoTramitacao()
				.stream()
				.filter(tramitacao -> tramitacao.idAcao.equals(AcaoTramitacao.VALIDAR_PARECER_GEO_GERENTE))
				.findFirst()
				.map(HistoricoTramitacao::getDataInicial).orElseThrow(ValidationException::new);

		return this.getHistoricoTramitacao()
				.stream()
				.filter(tramitacao -> tramitacao.dataInicial.equals(dataPrimeiroHistorico) || tramitacao.dataInicial.after(dataPrimeiroHistorico))
				.collect(Collectors.toList());

	}

	public List<Caracterizacao> getCaracterizacoesNaoArquivadas() {

		List<Caracterizacao> caracterizacoes = new ArrayList<>();

		for(Caracterizacao caracterizacao : this.empreendimento.caracterizacoes)
			if(!caracterizacao.isArquivada())
				caracterizacoes.add(caracterizacao);

		return caracterizacoes;

	}

	public static Processo findByNumProcesso(String numProcesso) {

		return Processo.find("numero", numProcesso).first();
	}

	public DadosProcessoVO getDadosProcesso() {

		return new DadosProcessoVO(this.caracterizacao, preencheListaAtividades(this.caracterizacao), preencheListaRestricoes(this.caracterizacao));

	}

	private static List<CamadaGeoAtividadeVO> preencheListaAtividades(Caracterizacao caracterizacao) {

		List<CamadaGeoAtividadeVO> atividades = new ArrayList<>();
		indexDadosAtividades = 0;

		for (AtividadeCaracterizacao atividadeCaracterizacao : caracterizacao.atividadesCaracterizacao) {

			indexDadosGeometriasAtividade = 0;
			indexDadosAtividades++;

			List<GeometriaAtividadeVO> geometrias = atividadeCaracterizacao.geometriasAtividade.stream().map(GeometriaAtividade::convertToVO).collect(Collectors.toList());
			atividades.add(new CamadaGeoAtividadeVO(atividadeCaracterizacao, geometrias));

		}

		return atividades;

	}

	public static List<CamadaGeoRestricaoVO> preencheListaRestricoes(Caracterizacao caracterizacao) {

		List<CamadaGeoRestricaoVO> restricoes = new ArrayList<>();
		indexDadosRestricoes = 0;

		if(caracterizacao.origemSobreposicao.equals(EMPREENDIMENTO)) {

			restricoes.addAll(caracterizacao.sobreposicoesCaracterizacaoEmpreendimento.stream().map(SobreposicaoCaracterizacaoEmpreendimento::convertToVO).collect(Collectors.toList()));

		} else if(caracterizacao.origemSobreposicao.equals(ATIVIDADE)) {

			List<SobreposicaoCaracterizacaoAtividade> sobreposicoesCaracterizacaoAtividades = new ArrayList<>();
			caracterizacao.atividadesCaracterizacao.stream()
					.filter(atividadeCaracterizacao -> !atividadeCaracterizacao.sobreposicoesCaracterizacaoAtividade.isEmpty())
					.forEach(atividadeCaracterizacao -> sobreposicoesCaracterizacaoAtividades.addAll(atividadeCaracterizacao.sobreposicoesCaracterizacaoAtividade));

			restricoes.addAll(sobreposicoesCaracterizacaoAtividades.stream().map(SobreposicaoCaracterizacaoAtividade::convertToVO).collect(Collectors.toList()));

		} else if(caracterizacao.origemSobreposicao.equals(COMPLEXO)) {

			restricoes.addAll(caracterizacao.sobreposicoesCaracterizacaoComplexo.stream().map(SobreposicaoCaracterizacaoComplexo::convertToVO).collect(Collectors.toList()));

		}

		return restricoes;

	}

	public static String getDescricaoRestricao(TipoSobreposicao tipoSobreposicao, Geometry sobreposicao, Double distancia) {

		if(TipoSobreposicaoDistanciaEnum.getList().contains(tipoSobreposicao.codigo)) {

			return "Distância " + Helper.formatBrDecimal(distancia / 1000, 2) + " km";

		}

		return getDescricao(sobreposicao);

	}

	public static String getDescricao(Geometry geometry) {

		switch (geometry.getGeometryType().toUpperCase()) {

			case "POINT" :

				return Helper.formatarCoordenada(geometry.getCoordinate());

			case "LINESTRING":

				return "Extensão " + Helper.formatBrDecimal(GeoCalc.length(geometry)/1000, 2) + " km";

			default:

				return "Área " + Helper.formatBrDecimal(GeoCalc.areaHectare(geometry),2) + " ha";

		}

	}

	public Processo getInfoProcesso() {

		br.ufla.lemaf.beans.Empreendimento empreendimentoEU = new IntegracaoEntradaUnicaService().findEmpreendimentosByCpfCnpj(this.empreendimento.cpfCnpj);
		this.empreendimento.empreendimentoEU = empreendimentoEU;
		this.empreendimento.area = GeoCalc.area(GeoJsonUtils.toGeometry(this.empreendimento.empreendimentoEU.localizacao.geometria)) / 1000;

		UsuarioAnalise usuario = Auth.getUsuarioSessao();

		this.analise.analiseTecnica = AnaliseTecnica.findByProcesso(this);
		this.analise.analiseGeo = AnaliseGeo.findByProcesso(this);

		if(this.analise.analisesGeo == null || this.analise.analisesGeo.isEmpty()){

			this.analise.analisesGeo = AnaliseGeo.findAllByProcesso(this.numero);
			this.analise.analiseGeo = this.analise.analisesGeo.stream()
					.max(Comparator.comparing(AnaliseGeo::getId)).orElse(null);
		}

		if(usuario.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.ANALISTA_GEO)) {

			this.analise.analiseGeo.pareceresAnalistaGeo = this.analise.analiseGeo.pareceresAnalistaGeo.stream()
					.filter(parecerAnalistaGeo -> parecerAnalistaGeo.usuario.id.equals(usuario.id))
					.collect(Collectors.toList());

		} else if(usuario.usuarioEntradaUnica.perfilSelecionado.codigo.equals(CodigoPerfil.ANALISTA_TECNICO)) {

			if (this.analise.analiseTecnica != null ) {

				this.analise.analiseTecnica.pareceresAnalistaTecnico = this.analise.analiseTecnica.pareceresAnalistaTecnico.stream()
						.filter(parecerAnalistaGeo -> parecerAnalistaGeo.analistaTecnico.id.equals(usuario.id))
						.collect(Collectors.toList());

			}

		}

		return this;

	}

	public List<EmpreendimentoCamandaGeo> getEmpreendimentoCamandasGeo() {

		return EmpreendimentoCamandaGeo.find("id_empreendimento = :id_empreendimento")
				.setParameter("id_empreendimento", this.empreendimento.id).fetch();

	}

	public static CamadaGeoComplexoVO preencheComplexo(Caracterizacao caracterizacao) {

		Processo.indexDadosGeometriasComplexo = 0;

		return new CamadaGeoComplexoVO(caracterizacao, caracterizacao.geometriasComplexo.stream().map(GeometriaComplexo::convertToVO).collect(Collectors.toList()));

	}
	
	public DesvinculoAnaliseGeo buscaDesvinculoPeloProcessoGeo() {

		DesvinculoAnaliseGeo desvinculoAnaliseGeo = DesvinculoAnaliseGeo.find("id_analise_geo = :id and id_usuario = :idUsuario")
				.setParameter("id", this.analise.analisesGeo.get(0).id)
				.setParameter("idUsuario", this.analise.analisesGeo.get(0).analistasGeo.get(0).usuario.id)
				.first();

		return desvinculoAnaliseGeo;
	}

	public DesvinculoAnaliseTecnica buscaDesvinculoPeloProcessoTecnico() {

		DesvinculoAnaliseTecnica desvinculoAnaliseTecnica = DesvinculoAnaliseTecnica.find("id_analise_tecnica = :id and id_usuario = :idUsuario")
				.setParameter("id", this.analise.analisesTecnicas.get(0).id)
				.setParameter("idUsuario", this.analise.analisesTecnicas.get(0).analistaTecnico.usuario.id)
				.first();

		return desvinculoAnaliseTecnica;
	}

	public List<Notificacao> getNotificacoes() {
		return this.analise.getNotificacoes();
	}

	public void inativar() {
		this.ativo = false;
		this._save();
	}

	public static Processo findLastByNumber(String numero) {
		return find("numero = :num ORDER BY id DESC").setParameter("num", numero).first();
	}

	public List<Notificacao> inicializaNotificacoes() {

		List<Notificacao> notificacoes = this.getNotificacoes();

		if (!notificacoes.isEmpty()) {
			return this.inicializaNotificacoes(this);
		}
		return this.inicializaNotificacoes(this.processoAnterior);
	}

	private List<Notificacao> inicializaNotificacoes(Processo processo) {
		return processo.getNotificacoes().stream().peek(n -> {
			n.setJustificativa();
			n.setDocumentosParecer();
			n.setDiasConclusao();
		}).sorted(Comparator.comparing(Notificacao::getDataNotificacao).reversed()).collect(Collectors.toList());
	}

	public File gerarShape() throws IOException, SchemaException {
		return this.caracterizacao.gerarShapeEmpreendimento();
	}

	public File gerarShapeAtividades() throws IOException, SchemaException {
		return this.caracterizacao.gerarShapeAtividade();
	}

}
