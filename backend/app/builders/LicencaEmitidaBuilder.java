package builders;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StringType;

import models.Processo;
import models.licenciamento.Licenca;
import models.licenciamento.LicencaEmitida;
import utils.IlikeNoAccents;

public class LicencaEmitidaBuilder extends CriteriaBuilder<LicencaEmitida> { 
	
	private static final String CARACTERIZACAO_ALIAS = "carac";
	private static final String PROCESSO_ALIAS = "proc";
	private static final String EMPREENDIMENTO_ALIAS = "emp";
	private static final String PESSOA_EMPREENDIMENTO_ALIAS = "pes";
	private static final String MUNICIPIO_EMPREENDIMENTO_ALIAS = "mun";
	private static final String ESTADO_EMPREENDIMENTO_ALIAS = "est";
	private static final String TIPO_LICENCA_ALIAS = "tlic";
	private static final String ATIVIDADE_CARACTERIZACAO_ALIAS = "atc";
	private static final String ATIVIDADE_ALIAS = "atv";

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

	public LicencaEmitidaBuilder groupByOrigemLicenca(){
		
		addProjection(Projections.groupProperty("tipoDispensa").as("origemLicenca"));
		
		return this;
	}		
	
	public LicencaEmitidaBuilder groupByIdLicenca(){
		
		addProjection(Projections.groupProperty("id").as("idLicenca"));
		
		return this;
	}	
	
	public LicencaEmitidaBuilder groupByNumeroLicenca(){
		
		addProjection(Projections.groupProperty("numero").as("numeroLincenca"));
		
		return this;
	}
	
	public LicencaEmitidaBuilder groupByNumeroProcesso(){
		
		addCaracterizacaoAlias();
		
		addProjection(Projections.groupProperty(CARACTERIZACAO_ALIAS+".numeroProcesso").as("numeroProcesso"));
		
		return this;
	}

	public LicencaEmitidaBuilder groupByIdProcesso(){
		
		addProcessoAlias();
		
		addProjection(Projections.groupProperty(PROCESSO_ALIAS+".id").as("idProcesso"));
		
		return this;
	}
	
	public LicencaEmitidaBuilder groupByCpfCnpjEmpreendimento(){
		
		addPessoaEmpreendimentoAlias();
		
		addProjection(Projections.groupProperty(PESSOA_EMPREENDIMENTO_ALIAS+".cpf").as("cpfEmpreendimento"));
		addProjection(Projections.groupProperty(PESSOA_EMPREENDIMENTO_ALIAS+".cnpj").as("cnpjEmpreendimento"));
		
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
	
	public LicencaEmitidaBuilder filtrarPorNumeroProcesso(String numeroProcesso) {
		
		if (StringUtils.isNotEmpty(numeroProcesso)) {
			
			addCaracterizacaoAlias();
			
			addRestriction(getNumeroProcessoRestricao(numeroProcesso));
		}
		
		return this;
	}
	
	private Criterion getNumeroProcessoRestricao(String numeroProcesso) {
		
		return Restrictions.ilike(CARACTERIZACAO_ALIAS+".numeroProcesso", numeroProcesso, MatchMode.ANYWHERE);
	}	
	
	public LicencaEmitidaBuilder filtrarPorCpfCnpjEmpreendimento(String cpfCnpj) {
		
		if (StringUtils.isNotEmpty(cpfCnpj)) {

			addPessoaEmpreendimentoAlias();
			
			criteria.add(Restrictions.or(
					getCpfEmpreendimentoRestricao(cpfCnpj), 
					getCnpjEmpreendimentoRestricao(cpfCnpj)
			));
		}
		
		return this;
	}

	private Criterion getCnpjEmpreendimentoRestricao(String cnpj) {
		return Restrictions.ilike(PESSOA_EMPREENDIMENTO_ALIAS+".cnpj", cnpj, MatchMode.START);
	}

	private Criterion getCpfEmpreendimentoRestricao(String cpf) {
		return Restrictions.ilike(PESSOA_EMPREENDIMENTO_ALIAS+".cpf", cpf, MatchMode.START);
	}
	
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
		
		if (StringUtils.isNotEmpty(pesquisa)) {

			addPessoaEmpreendimentoAlias();
			addMunicipioEmpreendimentoAlias();
			
			criteria.add(Restrictions.or(
				getNumeroLicencaRestricao(pesquisa),
				getNumeroProcessoRestricao(pesquisa),
				getCpfEmpreendimentoRestricao(pesquisa), 
				getCnpjEmpreendimentoRestricao(pesquisa),
				getDenominacaoEmpreendimentoRestricao(pesquisa),
				getMunicipioRestricao(pesquisa)
			));
		}
		
		return this;
	}	
	
	public LicencaEmitidaBuilder orderByNumeroProcesso() {
		
		addOrder(Order.asc("numeroProcesso"));
		
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
		
		public FiltroLicenca() {
	
		}
	}
}
