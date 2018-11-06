package builders;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StringType;

import models.Processo;

public class ProcessoBuilder extends CriteriaBuilder<Processo> { 
	
	private static final String EMPREENDIMENTO_ALIAS = "emp";
	private static final String PESSOA_EMPREENDIMENTO_ALIAS = "pes";
	private static final String MUNICIPIO_EMPREENDIMENTO_ALIAS = "mun";
	private static final String ESTADO_EMPREENDIMENTO_ALIAS = "est";
	private static final String ANALISE_ALIAS = "ana";
	private static final String ANALISE_JURIDICA_ALIAS = "anj";
	private static final String CARACTERIZACOES_ALIAS = "carac";
	private static final String ATIVIDADE_CARACTERIZACAO_ALIAS = "atc";
	private static final String ATIVIDADE_ALIAS = "atv";
	private static final String TIPOLOGIA_ATIVIDADE_ALIAS = "tip";
	private static final String OBJETO_TRAMITAVEL_ALIAS = "obt";
	private static final String CONSULTOR_JURIDICO_ALIAS = "coj";
	private static final String ANALISE_TECNICA_ALIAS = "ant";
	private static final String ANALISTA_TECNICO_ALIAS = "att";
	private static final String ATIVIDADE_CNAE_ALIAS = "atvc";
	private static final String TIPO_CARACTERIZACAO_ATIVIDADE_ALIAS = "tca";
	private static final String GERENTE_TECNICO_ALIAS = "gte";
	private static final String DIA_ANALISE_ALIAS = "da";
	
	public ProcessoBuilder addEmpreendimentoAlias() {
		
		addAlias("empreendimento", EMPREENDIMENTO_ALIAS);
		
		return this;		
	}
	
	public ProcessoBuilder addPessoaEmpreendimentoAlias() {
		
		addEmpreendimentoAlias();
		
		addAlias(EMPREENDIMENTO_ALIAS+".pessoa", PESSOA_EMPREENDIMENTO_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addMunicipioEmpreendimentoAlias() {
		
		addEmpreendimentoAlias();
		
		addAlias(EMPREENDIMENTO_ALIAS+".municipio", MUNICIPIO_EMPREENDIMENTO_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addEstadoEmpreendimentoAlias() {
		
		addMunicipioEmpreendimentoAlias();
		
		addAlias(MUNICIPIO_EMPREENDIMENTO_ALIAS+".estado", ESTADO_EMPREENDIMENTO_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addAnaliseAlias() {
		
		addAlias("analises", ANALISE_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addDiasAnaliseAlias() {
		
		addAnaliseAlias();
		
		addAlias(ANALISE_ALIAS+".diasAnalise", DIA_ANALISE_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addAnaliseJuridicaAlias() {
		
		addAnaliseAlias();
		
		addAlias(ANALISE_ALIAS+".analisesJuridica", ANALISE_JURIDICA_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addCaracterizacoesAlias() {
		
		addAlias("caracterizacoes", CARACTERIZACOES_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addAtividadeCaracterizacaoAlias() {
		
		addCaracterizacoesAlias();
		
		addAlias(CARACTERIZACOES_ALIAS+".atividadesCaracterizacao", ATIVIDADE_CARACTERIZACAO_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addAtividadeAlias() {
		
		addAtividadeCaracterizacaoAlias();
		
		addAlias(ATIVIDADE_CARACTERIZACAO_ALIAS+".atividade", ATIVIDADE_ALIAS);
		
		return this;
	}
	
	
	public ProcessoBuilder addTipologiaAtividadeAlias() {
		
		addAtividadeAlias();
		
		addAlias(ATIVIDADE_ALIAS+".tipologia", TIPOLOGIA_ATIVIDADE_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addObjetoTramitavelAlias() {
		
		addAtividadeCaracterizacaoAlias();
		
		addAlias("objetoTramitavel", OBJETO_TRAMITAVEL_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addConsultorJuridicoAlias() {
		
		addAnaliseJuridicaAlias();
		
		addAlias(ANALISE_JURIDICA_ALIAS+".consultoresJuridicos", CONSULTOR_JURIDICO_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addAnaliseTecnicaAlias(boolean isLeftOuterJoin) {
		
		addAnaliseAlias();
		
		if (isLeftOuterJoin){
			
			addAlias(ANALISE_ALIAS+".analisesTecnicas", ANALISE_TECNICA_ALIAS, JoinType.LEFT_OUTER_JOIN);
			
		} else {
			
			addAlias(ANALISE_ALIAS+".analisesTecnicas", ANALISE_TECNICA_ALIAS);
		}
		
		return this;
	}
	
	public ProcessoBuilder addAnalistaTecnicoAlias(boolean isLeftOuterJoin) {
		
		addAnaliseTecnicaAlias(isLeftOuterJoin);
		
		if (isLeftOuterJoin){
			
			addAlias(ANALISE_TECNICA_ALIAS+".analistasTecnicos", ANALISTA_TECNICO_ALIAS, JoinType.LEFT_OUTER_JOIN);
			
		} else {
			
			addAlias(ANALISE_TECNICA_ALIAS+".analistasTecnicos", ANALISTA_TECNICO_ALIAS);
		}
		
		return this;
	}	
	
	public ProcessoBuilder addAtividadeCnaeAlias() {
		
		addAtividadeCaracterizacaoAlias();
		
		addAlias(ATIVIDADE_CARACTERIZACAO_ALIAS+".atividadesCnae", ATIVIDADE_CNAE_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addTipoCaracterizacaoAtividade() {
		
		addAtividadeCnaeAlias();
		
		addAlias(ATIVIDADE_CNAE_ALIAS + ".tiposCaracterizacaoAtividades", TIPO_CARACTERIZACAO_ATIVIDADE_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addGerenteTecnicoAlias() {
		
		addAnaliseTecnicaAlias(false);
			
		addAlias(ANALISE_TECNICA_ALIAS+".gerentesTecnicos", GERENTE_TECNICO_ALIAS);
		
		return this;
	}	
	
	public ProcessoBuilder comTiposLicencas(){
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("(SELECT string_agg(t.sigla, '-') ");
		sb.append("FROM licenciamento.tipo_licenca t ");
		sb.append("INNER JOIN licenciamento.caracterizacao c ON t.id = c.id_tipo_licenca ");
		sb.append("INNER JOIN analise.rel_processo_caracterizacao r ON c.id = r.id_caracterizacao ");
		sb.append("WHERE r.id_processo = {alias}.id) AS licencas");
				
		addProjection(Projections.sqlProjection(sb.toString(), new String[]{"licencas"}, new org.hibernate.type.Type[]{StringType.INSTANCE}));
		
		return this;
	}
	
	public ProcessoBuilder groupByIdProcesso(){
		
		addProjection(Projections.groupProperty("id").as("idProcesso"));
		
		return this;
	}	
	
	public ProcessoBuilder groupByNumeroProcesso(){
		
		addProjection(Projections.groupProperty("numero").as("numero"));
		
		return this;
	}
	
	public ProcessoBuilder groupByCpfCnpjEmpreendimento(){
		
		addPessoaEmpreendimentoAlias();
		
		addProjection(Projections.groupProperty(PESSOA_EMPREENDIMENTO_ALIAS+".cpf").as("cpfEmpreendimento"));
		addProjection(Projections.groupProperty(PESSOA_EMPREENDIMENTO_ALIAS+".cnpj").as("cnpjEmpreendimento"));
		
		return this;
	}	
	
	public ProcessoBuilder groupByDenominacaoEmpreendimento(){
		
		addEmpreendimentoAlias();
		
		addProjection(Projections.groupProperty(EMPREENDIMENTO_ALIAS+".denominacao").as("denominacaoEmpreendimento"));
		
		return this;
	}
	
	public ProcessoBuilder groupByMunicipioEmpreendimento(){
		
		addMunicipioEmpreendimentoAlias();
		addProjection(Projections.groupProperty(MUNICIPIO_EMPREENDIMENTO_ALIAS+".nome").as("municipioEmpreendimento"));
		
		addEstadoEmpreendimentoAlias();
		addProjection(Projections.groupProperty(ESTADO_EMPREENDIMENTO_ALIAS+".id").as("siglaEstadoEmpreendimento"));
		
		return this;
	}
	
	public ProcessoBuilder groupByDataVencimentoPrazoAnalise(){
		
		addAnaliseAlias();		
		addProjection(Projections.groupProperty(ANALISE_ALIAS+".dataVencimentoPrazo").as("dataVencimentoPrazoAnalise"));
		
		return this;
	}
	
	public ProcessoBuilder groupByDataCadastroAnalise(){
		
		addAnaliseAlias();		
		addProjection(Projections.groupProperty(ANALISE_ALIAS+".dataCadastro").as("dataCadastroAnalise"));
		
		return this;
	}
	
	public ProcessoBuilder groupByDataVencimentoPrazoAnaliseJuridica(){
		
		addAnaliseJuridicaAlias();
		addProjection(Projections.groupProperty(ANALISE_JURIDICA_ALIAS+".dataVencimentoPrazo").as("dataVencimentoPrazoAnaliseJuridica"));
		
		return this;
	}
	
	public ProcessoBuilder groupByDataFinalAnaliseJuridica() {
		
		addAnaliseJuridicaAlias();
		addProjection(Projections.groupProperty(ANALISE_JURIDICA_ALIAS+".dataFim").as("dataConclusaoAnaliseJuridica"));
		
		return this;
	}
	
	public ProcessoBuilder groupByDataFinalAnaliseTecnica(boolean isLeftOuterJoin) {
		
		addAnaliseTecnicaAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_TECNICA_ALIAS+".dataFim").as("dataConclusaoAnaliseTecnica"));
		
		return this;
	}	
	
	public ProcessoBuilder groupByRevisaoSolicitadaAnaliseJuridica(){
		
		addAnaliseJuridicaAlias();
		addProjection(Projections.groupProperty(ANALISE_JURIDICA_ALIAS+".revisaoSolicitada").as("revisaoSolicitadaAnaliseJuridica"));
		
		return this;
	}

	public ProcessoBuilder groupByNotificacaoAtendidaAnaliseJuridica() {

		addAnaliseJuridicaAlias();
		addProjection(Projections.groupProperty(ANALISE_JURIDICA_ALIAS+".notificacaoAtendida").as("notificacaoAtendida"));

		return this;
	}

	public ProcessoBuilder groupByNotificacaoAtendidaAnaliseTecnica(boolean isLeftOuterJoin) {

		addAnaliseTecnicaAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_TECNICA_ALIAS+".notificacaoAtendida").as("notificacaoAtendida"));

		return this;
	}
	
	public ProcessoBuilder groupByIdAnaliseJuridica(){
		
		addAnaliseJuridicaAlias();
		addProjection(Projections.groupProperty(ANALISE_JURIDICA_ALIAS+".id").as("idAnaliseJuridica"));
		
		return this;
	}	
	
	
	public ProcessoBuilder groupByDataVencimentoPrazoAnaliseTecnica(boolean isLeftOuterJoin) {
		
		addAnaliseTecnicaAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_TECNICA_ALIAS+".dataVencimentoPrazo").as("dataVencimentoPrazoAnaliseTecnica"));
		
		return this;
	}
	
	public ProcessoBuilder groupByRevisaoSolicitadaAnaliseTecnica(boolean isLeftOuterJoin) {
		
		addAnaliseTecnicaAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_TECNICA_ALIAS+".revisaoSolicitada").as("revisaoSolicitadaAnaliseTecnica"));
		
		return this;	
	}
	
	public ProcessoBuilder groupByIdAnaliseTecnica(boolean isLeftOuterJoin) {
		
		addAnaliseTecnicaAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_TECNICA_ALIAS+".id").as("idAnaliseTecnica"));
		
		return this;
	}
	
	public ProcessoBuilder groupByIdAnalise() {
		
		addProjection(Projections.groupProperty(ANALISE_ALIAS+".id").as("idAnalise"));
		
		return this;
	}
	
	public ProcessoBuilder filtrarPorNumeroProcesso(String numeroProcesso) {
		
		if (StringUtils.isNotEmpty(numeroProcesso)) {
			
			addRestriction(Restrictions.ilike("numero", numeroProcesso, MatchMode.ANYWHERE));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarAnaliseJuridicaAtiva() {
		
		addAnaliseJuridicaAlias();
		addRestriction(Restrictions.eq(ANALISE_JURIDICA_ALIAS+".ativo", true));
		
		return this;
	}	
	
	
	public ProcessoBuilder filtrarPorCpfCnpjEmpreendimento(String cpfCnpj) {
		
		if (StringUtils.isNotEmpty(cpfCnpj)) {

			addPessoaEmpreendimentoAlias();
			
			criteria.add(Restrictions.or(
					Restrictions.ilike(PESSOA_EMPREENDIMENTO_ALIAS+".cpf", cpfCnpj, MatchMode.START), 
					Restrictions.ilike(PESSOA_EMPREENDIMENTO_ALIAS+".cnpj", cpfCnpj, MatchMode.START)
			));
		}
		
		return this;
	}	
	
	public ProcessoBuilder filtrarPorIdMunicipio(Long idMunicipio) {
		
		if (idMunicipio != null) {
			
			addMunicipioEmpreendimentoAlias();
			addRestriction(Restrictions.eq(MUNICIPIO_EMPREENDIMENTO_ALIAS+".id", idMunicipio));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarPorIdTipologia(Long idTipologia) {
		
		if (idTipologia != null) {
			
			addTipologiaAtividadeAlias();
			addRestriction(Restrictions.eq(TIPOLOGIA_ATIVIDADE_ALIAS+".id", idTipologia));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarPorIdAtividade(Long idAtividade) {
		
		if (idAtividade != null) {
			
			addAtividadeAlias();
			addRestriction(Restrictions.eq(ATIVIDADE_ALIAS+".id", idAtividade));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarPorIdCondicao(Long idCondicao) {
		
		if (idCondicao != null) {
			
			addObjetoTramitavelAlias();
			addRestriction(Restrictions.eq(OBJETO_TRAMITAVEL_ALIAS+".condicao.idCondicao", idCondicao));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarPorIdConsultorJuridico(Long idConsultorJuridico) {
		
		if (idConsultorJuridico != null) {
			
			addConsultorJuridicoAlias();
			addRestriction(Restrictions.eq(CONSULTOR_JURIDICO_ALIAS+".usuario.id", idConsultorJuridico));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarPorIdUsuarioValidacao(Long idUsuarioValidacao) {
		
		if (idUsuarioValidacao != null) {
			
			addAnaliseJuridicaAlias();
			addRestriction(Restrictions.eq(ANALISE_JURIDICA_ALIAS+".usuarioValidacao.id", idUsuarioValidacao));
		}
		
		return this;
	}

	public ProcessoBuilder filtrarPorPeriodoProcesso(Date periodoInicial, Date periodoFinal) {
		
		if (periodoInicial != null) {
			
			addRestriction(Restrictions.ge("dataCadastro", periodoInicial));
		}
		
		if (periodoFinal != null) {
			
			//Somando um dia a mais no periodo final para resolver o problema da data com hora
			periodoFinal = new Date(periodoFinal.getTime() + TimeUnit.DAYS.toMillis(1));
			
			addRestriction(Restrictions.le("dataCadastro", periodoFinal));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarPorIdAnalistaTecnico(Long idAnalistaTecnico, boolean isLeftOuterJoin) {
		
		if (idAnalistaTecnico != null) {
			
			addAnalistaTecnicoAlias(isLeftOuterJoin);
			addRestriction(Restrictions.eq(ANALISTA_TECNICO_ALIAS+".usuario.id", idAnalistaTecnico));
		}
		
		return this;		
	}
	
	public ProcessoBuilder filtrarAnaliseTecnicaAtiva(boolean isLeftOuterJoin) {
		
		addAnaliseTecnicaAlias(isLeftOuterJoin);
		
		if (isLeftOuterJoin) {
			
			addRestriction(Restrictions.or(
					Restrictions.isNull(ANALISE_TECNICA_ALIAS+".ativo"),
					Restrictions.eq(ANALISE_TECNICA_ALIAS+".ativo", true)
			));
			
		} else {
			
			addRestriction(Restrictions.eq(ANALISE_TECNICA_ALIAS+".ativo", true));
		}
		
		return this;		
	}
	
	public ProcessoBuilder filtrarPorIdSetor(Integer idSetor) {
		
		if (idSetor != null) {
			
			addTipoCaracterizacaoAtividade();
			addRestriction(Restrictions.eq(TIPO_CARACTERIZACAO_ATIVIDADE_ALIAS+".setor.id", idSetor));
			addRestriction(Restrictions.eqProperty(TIPO_CARACTERIZACAO_ATIVIDADE_ALIAS+".atividade.id", ATIVIDADE_CARACTERIZACAO_ALIAS+".atividade.id"));
		}
		
		return this;
	}
	
	
	public ProcessoBuilder filtrarPorIdUsuarioValidacaoTecnica(Long idUsuarioValidacao) {
		
		if (idUsuarioValidacao != null) {
			
			addAnaliseTecnicaAlias(false);
			addRestriction(Restrictions.eq(ANALISE_TECNICA_ALIAS+".usuarioValidacao.id", idUsuarioValidacao));
		}
		
		return this;
	}	
	
	public ProcessoBuilder filtrarPorIdsSetores(List<Integer> idsSetores) {
		
		if (idsSetores != null && idsSetores.size() > 0) {
			
			addTipoCaracterizacaoAtividade();			
			addRestriction(Restrictions.in(TIPO_CARACTERIZACAO_ATIVIDADE_ALIAS+".setor.id", idsSetores));
			addRestriction(Restrictions.eqProperty(TIPO_CARACTERIZACAO_ATIVIDADE_ALIAS+".atividade.id", ATIVIDADE_CARACTERIZACAO_ALIAS+".atividade.id"));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarPorIdUsuarioValidacaoTecnicaGerente(Long idUsuarioValidacaoGerente) {
		
		if (idUsuarioValidacaoGerente != null) {
			
			addAnaliseTecnicaAlias(false);
			addRestriction(Restrictions.eq(ANALISE_TECNICA_ALIAS+".usuarioValidacaoGerente.id", idUsuarioValidacaoGerente));
		}
		
		return this;
	}		
	
	public ProcessoBuilder filtrarPorIdGerenteTecnico(Long idGerenteTecnico) {
		
		if (idGerenteTecnico != null) {
			
			addGerenteTecnicoAlias();			
			addRestriction(Restrictions.eq(GERENTE_TECNICO_ALIAS+".usuario.id", idGerenteTecnico));
		}
		
		return this;		
	}
	
	public ProcessoBuilder orderByDataVencimentoPrazoAnaliseJuridica() {
		
		addOrder(Order.asc("dataVencimentoPrazoAnaliseJuridica"));
		
		return this;
	}		
	
	public ProcessoBuilder orderByDataVencimentoPrazoAnaliseTecnica() {
		
		addOrder(Order.asc("dataVencimentoPrazoAnaliseTecnica"));
		
		return this;		
	}
	
	public ProcessoBuilder groupByDiasAnalise(){
		
		addDiasAnaliseAlias();
		addProjection(Projections.groupProperty(DIA_ANALISE_ALIAS+".qtdeDiasAnalise").as("totalDiasAnalise"));
		
		return this;
	}
	
	public ProcessoBuilder groupByDiasAnaliseTecnica(){
		
		addDiasAnaliseAlias();
		addProjection(Projections.groupProperty(DIA_ANALISE_ALIAS+".qtdeDiasTecnica").as("diasAnaliseTecnica"));
		
		return this;
	}
	
	public ProcessoBuilder groupByDiasAnaliseJuridica(){
		
		addDiasAnaliseAlias();
		addProjection(Projections.groupProperty(DIA_ANALISE_ALIAS+".qtdeDiasJuridica").as("diasAnaliseJuridica"));
		
		return this;
	}
	
	public ProcessoBuilder groupByDiasAprovador(){
		
		addDiasAnaliseAlias();
		addProjection(Projections.groupProperty(DIA_ANALISE_ALIAS+".qtdeDiasAprovador").as("diasAprovador"));
		
		return this;
	}
	
	public ProcessoBuilder count() {
		
		addProjection(Projections.countDistinct("id").as("total"));
		
		return this;
	}

	public ProcessoBuilder groupByRenovacao() {

		addProjection(Projections.groupProperty("renovacao").as("renovacao"));

		return this;
	}
	
	public static class FiltroProcesso {
		
		public String numeroProcesso;
		public String cpfCnpjEmpreendimento;
		public Long idMunicipioEmpreendimento;
		public Long idTipologiaEmpreendimento;
		public Long idAtividadeEmpreendimento;
		public Long idCondicaoTramitacao;
		public Boolean filtrarPorUsuario;
		public Date periodoInicial;
		public Date periodoFinal;
		public Long paginaAtual;
		public Long itensPorPagina;
		public boolean isAnaliseJuridica;
		public boolean isAnaliseTecnica;
		public boolean isAnaliseTecnicaOpcional;
		public Long idAnalistaTecnico;
		public Integer idSetorGerencia;
		public Integer idSetorCoordenadoria;
		public Long idConsultorJuridico;
		
		public FiltroProcesso() {
			
		}
	}
}
