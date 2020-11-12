package builders;

import models.StatusLicenca;
import models.licenciamento.LicencaEmitida;
import models.licenciamento.StatusCaracterizacao;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import utils.IlikeNoAccents;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LicencaEmitidaBuilder extends CriteriaBuilder<LicencaEmitida> { 
	
	private static final String CARACTERIZACAO_ALIAS = "carac";
	private static final String PROCESSO_ALIAS = "proc";
	private static final String LICENCA_ALIAS = "lic";
	private static final String DISPENSA_ALIAS = "dic";
	private static final String LICENCA_SUSPENSAO_ALIAS = "lsus";
	private static final String LICENCA_CANCELADA_ALIAS = "lca";
	private static final String DISPENSA_CANCELADA_ALIAS = "dca";
	private static final String EMPREENDIMENTO_ALIAS = "emp";
	private static final String PESSOA_EMPREENDIMENTO_ALIAS = "pes";
	private static final String MUNICIPIO_EMPREENDIMENTO_ALIAS = "mun";
	private static final String ESTADO_EMPREENDIMENTO_ALIAS = "est";
	private static final String TIPO_LICENCA_ALIAS = "tlic";
	private static final String ATIVIDADE_CARACTERIZACAO_ALIAS = "atc";
	private static final String ATIVIDADE_ALIAS = "atv";
	private static final String DLA_ALIAS = "dla";

	public LicencaEmitidaBuilder addCaracterizacaoAlias() {
		
		addAlias("caracterizacao", CARACTERIZACAO_ALIAS);
		
		return this;
	}


	public LicencaEmitidaBuilder addEmpreendimentoAlias() {

		addCaracterizacaoAlias();

		addAlias(CARACTERIZACAO_ALIAS+".empreendimento", EMPREENDIMENTO_ALIAS);

		return this;
	}
	
	public LicencaEmitidaBuilder addProcessoAlias() {
		
		addCaracterizacaoAlias();
		
		addAlias(CARACTERIZACAO_ALIAS+".processos", PROCESSO_ALIAS, JoinType.LEFT_OUTER_JOIN);
		
		return this;
	}

	public LicencaEmitidaBuilder addLicencaAlias() {

		addAlias("licenca", LICENCA_ALIAS, JoinType.LEFT_OUTER_JOIN);

		return this;
	}

	public LicencaEmitidaBuilder addDispensaLicenciamentoAlias() {

		addAlias("dispensa_licenciamento", DISPENSA_ALIAS, JoinType.LEFT_OUTER_JOIN);

		return this;
	}

	public LicencaEmitidaBuilder addLicencaSuspensaoAlias() {

		addLicencaAlias();

		addAlias(LICENCA_ALIAS+".suspensao", LICENCA_SUSPENSAO_ALIAS);

		return this;
	}
	
	public LicencaEmitidaBuilder addPessoaEmpreendimentoAlias() {
		
		addEmpreendimentoAlias();
		
		addAlias(EMPREENDIMENTO_ALIAS+".pessoa", PESSOA_EMPREENDIMENTO_ALIAS);
		
		return this;
	}
	
	public LicencaEmitidaBuilder addMunicipioEmpreendimentoAlias() {
		
		addEmpreendimentoAlias();
		
		addAlias(EMPREENDIMENTO_ALIAS+".municipio", MUNICIPIO_EMPREENDIMENTO_ALIAS);
		
		return this;
	}
	
	public LicencaEmitidaBuilder addEstadoEmpreendimentoAlias() {
		
		addMunicipioEmpreendimentoAlias();
		
		addAlias(MUNICIPIO_EMPREENDIMENTO_ALIAS+".estado", ESTADO_EMPREENDIMENTO_ALIAS);
		
		return this;
	}
	
	public LicencaEmitidaBuilder addTipoLicencaAlias() {
		
		addCaracterizacaoAlias();
		
		addAlias(CARACTERIZACAO_ALIAS+".tipoLicenca", TIPO_LICENCA_ALIAS);
		
		return this;
	}	
	
	public LicencaEmitidaBuilder addAtividadeCaracterizacaoAlias() {
		
		addCaracterizacaoAlias();
		
		addAlias(CARACTERIZACAO_ALIAS+".atividadesCaracterizacao", ATIVIDADE_CARACTERIZACAO_ALIAS);
		
		return this;
	}
	
	public LicencaEmitidaBuilder addAtividadeAlias() {
		
		addAtividadeCaracterizacaoAlias();
		
		addAlias(ATIVIDADE_CARACTERIZACAO_ALIAS+".atividade", ATIVIDADE_ALIAS);
		
		return this;
	}

	public LicencaEmitidaBuilder addDlaAlias() {

		addAlias("dla", DLA_ALIAS, JoinType.LEFT_OUTER_JOIN);

		return this;
	}

	public LicencaEmitidaBuilder groupByOrigemLicenca(){
		
		addProjection(Projections.groupProperty("tipoDispensa").as("origemLicenca"));
		
		return this;
	}		
	
	public LicencaEmitidaBuilder groupByIdLicenca(){

		addLicencaAlias();
		addDlaAlias();

		addProjection(Projections.groupProperty(LICENCA_ALIAS+".id").as("idLicenca"));
		addProjection(Projections.groupProperty(DLA_ALIAS+".id").as("idDla"));
		
		return this;
	}	
	
	public LicencaEmitidaBuilder groupByNumeroLicenca(){
		
		addProjection(Projections.groupProperty("numero").as("numeroLincenca"));
		
		return this;
	}
	
	public LicencaEmitidaBuilder groupByNumeroProcesso(){
		
		addCaracterizacaoAlias();
		
		addProjection(Projections.groupProperty(CARACTERIZACAO_ALIAS+".numero").as("numero"));
		
		return this;
	}

	public LicencaEmitidaBuilder groupByIdProcesso(){
		
		addProcessoAlias();
		
		addProjection(Projections.groupProperty(PROCESSO_ALIAS+".id").as("idProcesso"));
		
		return this;
	}
	
	public LicencaEmitidaBuilder groupByCpfCnpjEmpreendimento(){
		
		addEmpreendimentoAlias();
		
		addProjection(Projections.groupProperty(EMPREENDIMENTO_ALIAS+".cpfCnpj").as("cpfEmpreendimento"));
		
		return this;
	}	
	
	public LicencaEmitidaBuilder groupByDenominacaoEmpreendimento(){
		
		addEmpreendimentoAlias();
		
		addProjection(Projections.groupProperty(EMPREENDIMENTO_ALIAS+".denominacao").as("denominacaoEmpreendimento"));
		
		return this;
	}
	
	public LicencaEmitidaBuilder groupByMunicipioEmpreendimento(){
		
		addMunicipioEmpreendimentoAlias();
		addProjection(Projections.groupProperty(MUNICIPIO_EMPREENDIMENTO_ALIAS+".nome").as("municipioEmpreendimento"));
		
		addEstadoEmpreendimentoAlias();
		addProjection(Projections.groupProperty(ESTADO_EMPREENDIMENTO_ALIAS+".id").as("siglaEstadoEmpreendimento"));
		
		return this;
	}
	
	public LicencaEmitidaBuilder groupByLicenca(){
		
		addTipoLicencaAlias();
		
		addProjection(Projections.groupProperty(TIPO_LICENCA_ALIAS+".sigla").as("tipoLicenca"));
		
		return this;
	}
	
	public LicencaEmitidaBuilder groupByTipoCaracterizacao(){
		
		addCaracterizacaoAlias();
		
		addProjection(Projections.groupProperty(CARACTERIZACAO_ALIAS+".tipo.id").as("tipoCaracterizacao"));
		
		return this;
	}

	public LicencaEmitidaBuilder groupByStatusCaracterizacao(){

		addCaracterizacaoAlias();

		addProjection(Projections.groupProperty(CARACTERIZACAO_ALIAS+".status.id").as("statusCaracterizacao"));

		return this;
	}

	public LicencaEmitidaBuilder groupByAtivo() {

		addProjection(Projections.groupProperty("ativo").as("ativo"));

		return this;
	}

	public LicencaEmitidaBuilder filtrarPorNumeroLicenca(String numeroLicenca) {
		
		if (StringUtils.isNotEmpty(numeroLicenca)) {
			
			addRestriction(getNumeroLicencaRestricao(numeroLicenca));
		}
		
		return this;
	}
	
	private Criterion getNumeroLicencaRestricao(String numeroLicenca) {
		
		return Restrictions.ilike("numero", numeroLicenca, MatchMode.ANYWHERE);
	}

	private Criterion getStatusAtivoLicencaRestricao(Boolean ativo) {
		addLicencaAlias();
		return Restrictions.eq("ativo", ativo);
	}

	private Criterion getStatusSuspensaLicencaRestricao(Boolean suspenso) {

		addLicencaSuspensaoAlias();

		return Restrictions.eq(LICENCA_SUSPENSAO_ALIAS+".ativo", suspenso);
	}

	private Criterion getStatusCanceladaLicencaRestricao(Boolean isLeftOuterJoin) {

		addLicencaAlias();

		addAlias(LICENCA_ALIAS+".licencaCancelada", LICENCA_CANCELADA_ALIAS, JoinType.LEFT_OUTER_JOIN);

		return Restrictions.eq("ativo", false);
	}

	private Criterion getStatusCanceladaDispensaRestricao(Boolean isLeftOuterJoin) {

		addDlaAlias();

		addAlias(DLA_ALIAS+".dispensaCancelada", DISPENSA_CANCELADA_ALIAS, JoinType.LEFT_OUTER_JOIN);

		return Restrictions.eq("ativo", false);
	}

	private Criterion getStatusRenovacaoLicencaRestricao(boolean renovado) {

		addCaracterizacaoAlias();

		return Restrictions.eq(CARACTERIZACAO_ALIAS+".renovacao", renovado);
	}

	private Criterion getStatusLicencaCancelada() {

		addCaracterizacaoAlias();

		return Restrictions.eq(CARACTERIZACAO_ALIAS+".status.id",StatusCaracterizacao.CANCELADO);
	}

	private Criterion getStatusLicencaDeferida() {

		addCaracterizacaoAlias();

		return Restrictions.eq(CARACTERIZACAO_ALIAS+".status.id",StatusCaracterizacao.DEFERIDO);
	}

	private Criterion getStatusLicencaSuspensa() {

		addCaracterizacaoAlias();

		return Restrictions.eq(CARACTERIZACAO_ALIAS+".status.id",StatusCaracterizacao.SUSPENSO);
	}

	private Criterion getStatusLicencaVencida() {

		addCaracterizacaoAlias();

		return Restrictions.eq(CARACTERIZACAO_ALIAS+".status.id", StatusCaracterizacao.VENCIDA);
	}
	
	public LicencaEmitidaBuilder filtrarPorNumeroProcesso(String numeroProcesso) {
		
		if (StringUtils.isNotEmpty(numeroProcesso)) {
			
			addCaracterizacaoAlias();
			
			addRestriction(getNumeroProcessoRestricao(numeroProcesso));
		}
		
		return this;
	}
	
	private Criterion getNumeroProcessoRestricao(String numeroProcesso) {
		
		return Restrictions.ilike(CARACTERIZACAO_ALIAS+".numero", numeroProcesso, MatchMode.ANYWHERE);
	}	
	
	public LicencaEmitidaBuilder filtrarPorCpfCnpjEmpreendimento(String cpfCnpj) {
		
		if (StringUtils.isNotEmpty(cpfCnpj)) {

			addEmpreendimentoAlias();
			
			criteria.add(Restrictions.or(
					getCpfCnpjEmpreendimentoRestricao(cpfCnpj)
			));
		}
		
		return this;
	}

	private Criterion getCpfCnpjEmpreendimentoRestricao(String cpfCnpj) {
		return Restrictions.ilike(EMPREENDIMENTO_ALIAS+".cpfCnpj", cpfCnpj, MatchMode.START);
	}

//	private Criterion getCpfEmpreendimentoRestricao(String cpf) {
//		return Restrictions.ilike(PESSOA_EMPREENDIMENTO_ALIAS+".cpf", cpf, MatchMode.START);
//	}
	
	public LicencaEmitidaBuilder filtrarPorIdAtividade(Long idAtividade) {
		
		if (idAtividade != null) {
			
			addAtividadeAlias();
			addRestriction(Restrictions.eq(ATIVIDADE_ALIAS+".id", idAtividade));
		}
		
		return this;
	}
	
	public LicencaEmitidaBuilder filtrarPorDenominacaoEmpreendimento(String denominacaoEmpreendimento) {
		
		if (StringUtils.isNotEmpty(denominacaoEmpreendimento)) {
			
			addEmpreendimentoAlias();
			addRestriction(getDenominacaoEmpreendimentoRestricao(denominacaoEmpreendimento));
		}
		
		return this;
	}

	private Criterion getDenominacaoEmpreendimentoRestricao(String denominacaoEmpreendimento) {		
		return new IlikeNoAccents(EMPREENDIMENTO_ALIAS+".denominacao", denominacaoEmpreendimento);
	}
	
	public LicencaEmitidaBuilder filtrarPorIdMunicipio(Long idMunicipio) {
		
		if (idMunicipio != null) {
			
			addMunicipioEmpreendimentoAlias();
			addRestriction(Restrictions.eq(MUNICIPIO_EMPREENDIMENTO_ALIAS+".id", idMunicipio));
		}
		
		return this;
	}

	private Criterion getMunicipioRestricao(String municipio) {
		return new IlikeNoAccents(MUNICIPIO_EMPREENDIMENTO_ALIAS+".nome", municipio);
	}	
		
	public LicencaEmitidaBuilder filtrarPorIdTipoLicenca(Long idTipoLicenca) {
		
		if (idTipoLicenca != null) {
			
			addRestriction(Restrictions.eq(TIPO_LICENCA_ALIAS+".id", idTipoLicenca));
		}
		
		return this;
	}

	public LicencaEmitidaBuilder filtrarPorStatusLicenca(StatusLicenca statusLicenca) {

		if (statusLicenca != null) {

			switch (statusLicenca) {

				case DEFERIDO:
					addRestriction(this.getStatusLicencaDeferida());
					break;

				case VENCIDO:
					addRestriction(this.getStatusLicencaVencida());
					break;

				case CANCELADA:
//					addRestriction(Restrictions.or(this.getStatusCanceladaLicencaRestricao(true),this.getStatusCanceladaDispensaRestricao(true)));
					addRestriction(this.getStatusLicencaCancelada());
					break;

				case RENOVADA:
					addRestriction(this.getStatusRenovacaoLicencaRestricao(true));
					break;

				case SUSPENSA:
					addRestriction(this.getStatusSuspensaLicencaRestricao(true));
					break;
			}
		}

		return this;
	}

	
	//TODO Adicionar filtro por situação da licença
	
	public LicencaEmitidaBuilder filtrarPorPeriodoVencimento(Date periodoInicial, Date periodoFinal) {
		
		if (periodoInicial != null) {
			
			addRestriction(Restrictions.ge("dataCadastro", periodoInicial));
		}
		
		if (periodoFinal != null) {
			
			//Somando um dia a mais no periodo final para resolver o problema da data com hora
			periodoFinal = new Date(periodoFinal.getTime() + TimeUnit.DAYS.toMillis(1));
			
			addRestriction(Restrictions.le("dataValidade", periodoFinal));
		}
		
		return this;
	}
	
	public LicencaEmitidaBuilder filtrarPorCamposPesquisaRapida(String pesquisa) {

//		criteria.add(Restrictions.and(getStatusAtivoLicencaRestricao(true)));

		if (StringUtils.isNotEmpty(pesquisa)) {

//			addPessoaEmpreendimentoAlias();
			addMunicipioEmpreendimentoAlias();
			
			criteria.add(Restrictions.or(
				getNumeroLicencaRestricao(pesquisa),
				getNumeroProcessoRestricao(pesquisa),
				getCpfCnpjEmpreendimentoRestricao(pesquisa),
//				getCnpjEmpreendimentoRestricao(pesquisa),
				getDenominacaoEmpreendimentoRestricao(pesquisa),
				getMunicipioRestricao(pesquisa)
			));
		}
		
		return this;
	}	
	
	public LicencaEmitidaBuilder orderByNumeroProcesso() {
		
		addOrder(Order.asc("numero"));
		
		return this;
	}
	
	public LicencaEmitidaBuilder count() {
		
		addProjection(Projections.countDistinct("id").as("total"));
		
		return this;
	}
	
	public static class FiltroLicenca {
		
		public String numeroLicenca;
		public String numeroProcesso;
		public String cpfCnpjEmpreendimento;
		public Long idAtividadeEmpreendimento;
		public String denominacaoEmpreendimento;
		public Long idMunicipioEmpreendimento;
		public Long idLicenca;
		public Date periodoInicial;
		public Date periodoFinal;
		public Long paginaAtual;
		public Long itensPorPagina;
		public String pesquisa;
		public StatusLicenca statusLicenca;
		
		public FiltroLicenca() {
	
		}
	}
}
