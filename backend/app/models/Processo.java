package models;

import builders.ProcessoBuilder;
import builders.ProcessoBuilder.FiltroProcesso;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import enums.CamadaGeoEnum;
import exceptions.ValidacaoException;
import models.EntradaUnica.CodigoPerfil;
import models.licenciamento.*;
import models.tramitacao.*;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import security.InterfaceTramitavel;
import services.IntegracaoEntradaUnicaService;
import utils.*;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

	@Column
	public Boolean renovacao;

	@Required
	@Column(name="data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	@ManyToOne
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

	public void vincularConsultor(UsuarioAnalise consultor, UsuarioAnalise usuarioExecutor) {

		ConsultorJuridico.vincularAnalise(consultor, AnaliseJuridica.findByProcesso(this), usuarioExecutor);

		tramitacao.tramitar(this, AcaoTramitacao.VINCULAR_CONSULTOR, usuarioExecutor, consultor);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.objetoTramitavel.id), usuarioExecutor);

	}

	public void vincularAnalista(UsuarioAnalise analista, UsuarioAnalise usuarioExecutor, String justificativaCoordenador) {

		AnalistaTecnico.vincularAnalise(analista, AnaliseTecnica.findByProcesso(this), usuarioExecutor, justificativaCoordenador);

		tramitacao.tramitar(this, AcaoTramitacao.VINCULAR_ANALISTA, usuarioExecutor, analista);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(this.objetoTramitavel.id), usuarioExecutor);

	}

	
	public void vincularGerente(UsuarioAnalise gerente, UsuarioAnalise usuarioExecutor) {
		
		Gerente.vincularAnalise(gerente, usuarioExecutor, AnaliseTecnica.findByProcesso(this));
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


//		commonFilterProcessoAnaliseJuridica(processoBuilder, filtro, usuarioSessao.id);

		commonFilterProcessoAnaliseTecnica(processoBuilder, filtro, usuarioSessao);

		commonFilterProcessoAnaliseGeo(processoBuilder, filtro, usuarioSessao);

		commonFilterProcessoAprovador(processoBuilder, filtro, usuarioSessao);

		return processoBuilder;
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
				filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_ASSINATURA_APROVADOR)){

			br.ufla.lemaf.beans.pessoa.Setor setor = integracaoEntradaUnica.getSetorBySigla(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);

			if (setor != null) {

				processoBuilder.filtrarPorSiglaSetores(integracaoEntradaUnica.getSiglasSetoresByNivel(setor.sigla, 2));
			}
		}
	}

//	private static void commonFilterProcessoAnaliseJuridica(ProcessoBuilder processoBuilder,
//			FiltroProcesso filtro, Long idUsuarioLogado) {
//
//		if (!filtro.isAnaliseJuridica) {
//
//			return;
//		}
//
//		processoBuilder.filtrarAnaliseJuridicaAtiva();
//
//		if (filtro.filtrarPorUsuario != null && filtro.filtrarPorUsuario && filtro.idCondicaoTramitacao != null &&
//		   (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_ANALISE_JURIDICA) ||
//			filtro.idCondicaoTramitacao.equals(Condicao.EM_ANALISE_JURIDICA))) {
//
//			processoBuilder.filtrarPorIdConsultorJuridico(idUsuarioLogado);
//		}
//
//		if (filtro.idCondicaoTramitacao != null &&
//			   filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VALIDACAO_JURIDICA)) {
//
//				processoBuilder.filtrarPorIdUsuarioValidacao(idUsuarioLogado);
//		}
//
//
//	}

	private static void commonFilterProcessoAnaliseTecnica(ProcessoBuilder processoBuilder, FiltroProcesso filtro,
														   UsuarioAnalise usuarioSessao) {

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

		if (!filtro.isAnaliseTecnica) {

			return;
		}

		processoBuilder.filtrarAnaliseTecnicaAtiva(filtro.isAnaliseTecnicaOpcional);
		processoBuilder.filtrarPorSiglaSetor(filtro.siglaSetorGerencia);

		if (filtro.filtrarPorUsuario == null || !filtro.filtrarPorUsuario || filtro.idCondicaoTramitacao == null) {

			processoBuilder.filtrarPorIdAnalistaTecnico(filtro.idAnalistaTecnico, false);

			return;
		}

		if (usuarioSessao.usuarioEntradaUnica.setorSelecionado == null) {

			throw new ValidacaoException(Mensagem.ANALISE_TECNICA_USUARIO_SEM_SETOR);
		}

		if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_ANALISE_TECNICA) ||
				filtro.idCondicaoTramitacao.equals(Condicao.EM_ANALISE_TECNICA)) {

			processoBuilder.filtrarPorIdAnalistaTecnico(usuarioSessao.id, filtro.isAnaliseTecnicaOpcional);

			processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);
		} else {

			processoBuilder.filtrarPorIdAnalistaTecnico(filtro.idAnalistaTecnico, false);
		}

		if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VINCULACAO_TECNICA_PELO_GERENTE)) {

			processoBuilder.filtrarPorIdGerente(usuarioSessao.id);

			processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);
		}

		if (filtro.siglaSetorGerencia == null && filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VINCULACAO_TECNICA_PELO_COORDENADOR)) {

			processoBuilder.filtrarPorSiglaSetores(integracaoEntradaUnica.getSiglasSetoresByNivel(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla,1));
		}

		if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VALIDACAO_TECNICA_PELO_COORDENADOR)) {

			processoBuilder.filtrarPorIdUsuarioValidacaoTecnica(usuarioSessao.id);

			processoBuilder.filtrarPorSiglaSetores(integracaoEntradaUnica.getSiglasSetoresByNivel(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla,1));
		}

		if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VALIDACAO_TECNICA_PELO_GERENTE)) {

			processoBuilder.filtrarPorIdUsuarioValidacaoTecnicaGerente(usuarioSessao.id);
			processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);
		}
	}

	private static void commonFilterProcessoAnaliseGeo(ProcessoBuilder processoBuilder, FiltroProcesso filtro,
													   UsuarioAnalise usuarioSessao) {

		IntegracaoEntradaUnicaService integracaoEntradaUnica = new IntegracaoEntradaUnicaService();

		if (!filtro.isAnaliseGeo) {

			return;
		}

		processoBuilder.filtrarAnaliseGeoAtiva(filtro.isAnaliseGeoOpcional);
		processoBuilder.filtrarPorSiglaSetor(filtro.siglaSetorGerencia);

		if (filtro.idAnalistaGeo == null) {
			filtro.idAnalistaGeo = filtro.idUsuarioLogado;
		}

		if (filtro.filtrarPorUsuario == null || !filtro.filtrarPorUsuario || filtro.idCondicaoTramitacao == null) {

			processoBuilder.filtrarPorIdAnalistaGeo(filtro.idAnalistaGeo, false);
			processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);

			return;
		}

		if (usuarioSessao.usuarioEntradaUnica.setorSelecionado == null) {

			throw new ValidacaoException(Mensagem.ANALISE_GEO_USUARIO_SEM_SETOR);
		}

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

			processoBuilder.filtrarPorSiglaSetores(integracaoEntradaUnica.getSiglasSetoresByNivel(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla,1));
		}

		if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VALIDACAO_GEO_PELO_GERENTE)) {

			processoBuilder.filtrarPorIdUsuarioValidacaoGeo(usuarioSessao.id);

			processoBuilder.filtrarPorSiglaSetores(integracaoEntradaUnica.getSiglasSetoresByNivel(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla,1));
		}

		if (filtro.idCondicaoTramitacao.equals(Condicao.AGUARDANDO_VALIDACAO_GEO_PELO_GERENTE)) {

			processoBuilder.filtrarPorIdUsuarioValidacaoGeoGerente(usuarioSessao.id);
			processoBuilder.filtrarPorSiglaSetor(usuarioSessao.usuarioEntradaUnica.setorSelecionado.sigla);
		}
	}

	public static List listWithFilter(FiltroProcesso filtro, UsuarioAnalise usuarioSessao) {

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
				.groupByDataCadastroAnalise()
				.groupByRenovacao();

		listWithFilterAnaliseJuridica(processoBuilder, filtro);

		listWithFilterAnaliseTecnica(processoBuilder, filtro);

		listWithFilterAnaliseGeo(processoBuilder, filtro);

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

	private static void listWithFilterAnaliseGeo(ProcessoBuilder processoBuilder,
												 FiltroProcesso filtro) {

		if (!filtro.isAnaliseGeo) {

			return;
		}

		processoBuilder.groupByIdAnaliseGeo(filtro.isAnaliseGeoOpcional)
				.groupByDataVencimentoPrazoAnaliseGeo(filtro.isAnaliseGeoOpcional)
				.groupByRevisaoSolicitadaAnaliseGeo(filtro.isAnaliseGeoOpcional)
				.groupByDataFinalAnaliseGeo(filtro.isAnaliseGeoOpcional)
				.groupByDiasAnaliseGeo()
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
				.addPessoaEmpreendimentoAlias()
				.addEstadoEmpreendimentoAlias()
				.addAnaliseAlias()
				.count();

		countWithFilterAnaliseJuridica(processoBuilder, filtro);

		countWithFilterAnaliseTecnica(processoBuilder, filtro);

		countWithFilterAnaliseGeo(processoBuilder, filtro);

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

	//Retorna o historico da tramitação com o tempo que o objeto tramitavel anterior permaneceu na condição
	public List<HistoricoTramitacao> getHistoricoTramitacaoAnterior() {

		if (this.processoAnterior == null) {

			return null;
		}

		Processo processoAnterior = Processo.findById(this.processoAnterior.id);
		List<HistoricoTramitacao> historicosTramitacoes = HistoricoTramitacao.getByObjetoTramitavel(processoAnterior.idObjetoTramitavel);

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

	public boolean isProrrogacao() {

		long diff = Math.abs(this.caracterizacoes.get(0).getLicenca().dataValidade.getTime() - new Date().getTime());
		long dias = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 5l;

		return Configuracoes.DIAS_PRORROGACAO < dias;
	}

	public boolean isArquivavel() {

		for (Caracterizacao caracterizacao : this.caracterizacoes) {

			if (caracterizacao.numeroProcessoAntigo == null && !caracterizacao.status.nome.equals(StatusCaracterizacao.ARQUIVADO)
					&& !caracterizacao.status.nome.equals(StatusCaracterizacao.CANCELADO)) {

				return false;
			}
		}

		return true;
	}

	public static Processo findByNumProcesso(String numProcesso) {

		return Processo.find("numero", numProcesso).first();
	}

	public List<CamadaGeoAtividade> getDadosAreaProjeto (){

		List<CamadaGeoAtividade> dadosAreaProjeto = new ArrayList<>();

		Caracterizacao caracterizacao = this.getCaracterizacao();

		int index = 0;

		for (AtividadeCaracterizacao atividadeCaracterizacao : caracterizacao.atividadesCaracterizacao) {

			List<CamadaGeo> camadasGeo= new ArrayList<>();

			for (GeometriaAtividade geometria : atividadeCaracterizacao.geometriasAtividade) {

				for (Geometry geometrie : GeoCalc.getGeometries(geometria.geometria)) {

					index = index + 1;

					CamadaGeo camadaGeo = new CamadaGeo(CamadaGeoEnum.ATIVIDADE.nome +"_" + index, CamadaGeoEnum.ATIVIDADE.tipo +"_" + index,
							getDescricaoAtividade(geometrie), GeoCalc.areaHectare(geometrie), geometrie);
					camadaGeo.geometriaAtividade = geometria;

					camadasGeo.add(camadaGeo);
				}

			}

			CamadaGeoAtividade camadaGeoAtividade = new CamadaGeoAtividade(atividadeCaracterizacao.atividade, camadasGeo);

			dadosAreaProjeto.add(camadaGeoAtividade);

		}

		return dadosAreaProjeto;
	}

	private String getDescricaoAtividade (Geometry geometry) {

		String descricao = "";

		switch (geometry.getGeometryType().toUpperCase()) {

			case "POINT" :
				descricao = "Coordenadas [" + String.valueOf(((Point) geometry).getY()) + ", " + String.valueOf(((Point) geometry).getX()) + "]";
				break;
			case "LINESTRING":

				descricao = "Extensão " + Helper.formatBrDecimal(GeoCalc.length(geometry)/1000, 2) + " km";
				break;
			case "POLYGON":
				descricao = "Área " + Helper.formatBrDecimal(GeoCalc.areaHectare(geometry),2) + " ha";
				break;
		}

		return descricao;
	}



}
