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
import models.licenciamento.Licenca;

public class LicencaBuilder extends CriteriaBuilder<Licenca> { 
	
	private static final String CARACTERIZACAO_ALIAS = "carac";
	private static final String PROCESSOS_ALIAS = "pro";
	private static final String EMPREENDIMENTO_ALIAS = "emp";
	private static final String PESSOA_EMPREENDIMENTO_ALIAS = "pes";
	private static final String MUNICIPIO_EMPREENDIMENTO_ALIAS = "mun";
	private static final String ESTADO_EMPREENDIMENTO_ALIAS = "est";
	private static final String TIPO_LICENCA_ALIAS = "tlic";
	private static final String ATIVIDADE_CARACTERIZACAO_ALIAS = "atc";
	private static final String ATIVIDADE_ALIAS = "atv";
	private static final String TIPOLOGIA_ATIVIDADE_ALIAS = "tip";

	public LicencaBuilder addCaracterizacaoAlias() {
		
		addAlias("caracterizacao", CARACTERIZACAO_ALIAS);
		
		return this;
	}
	
	public LicencaBuilder addProcessosAlias() {
		
		addCaracterizacaoAlias();
		
		addAlias(CARACTERIZACAO_ALIAS+".processos", PROCESSOS_ALIAS);
		
		return this;
	}
	
	public LicencaBuilder addEmpreendimentoAlias() {
		
		addCaracterizacaoAlias();
		
		addAlias(CARACTERIZACAO_ALIAS+".empreendimento", EMPREENDIMENTO_ALIAS);
		
		return this;		
	}
	
	public LicencaBuilder addPessoaEmpreendimentoAlias() {
		
		addEmpreendimentoAlias();
		
		addAlias(EMPREENDIMENTO_ALIAS+".pessoa", PESSOA_EMPREENDIMENTO_ALIAS);
		
		return this;
	}
	
	public LicencaBuilder addMunicipioEmpreendimentoAlias() {
		
		addEmpreendimentoAlias();
		
		addAlias(EMPREENDIMENTO_ALIAS+".municipio", MUNICIPIO_EMPREENDIMENTO_ALIAS);
		
		return this;
	}
	
	public LicencaBuilder addEstadoEmpreendimentoAlias() {
		
		addMunicipioEmpreendimentoAlias();
		
		addAlias(MUNICIPIO_EMPREENDIMENTO_ALIAS+".estado", ESTADO_EMPREENDIMENTO_ALIAS);
		
		return this;
	}
	
	public LicencaBuilder addTipoLicencaAlias() {
		
		addCaracterizacaoAlias();
		
		addAlias(CARACTERIZACAO_ALIAS+".tipoLicenca", TIPO_LICENCA_ALIAS);
		
		return this;
	}	
	
	public LicencaBuilder addAtividadeCaracterizacaoAlias() {
		
		addCaracterizacaoAlias();
		
		addAlias(CARACTERIZACAO_ALIAS+".atividadeCaracterizacao", ATIVIDADE_CARACTERIZACAO_ALIAS);
		
		return this;
	}
	
	public LicencaBuilder addAtividadeAlias() {
		
		addAtividadeCaracterizacaoAlias();
		
		addAlias(ATIVIDADE_CARACTERIZACAO_ALIAS+".atividade", ATIVIDADE_ALIAS);
		
		return this;
	}
	
	
	public LicencaBuilder addTipologiaAtividadeAlias() {
		
		addAtividadeAlias();
		
		addAlias(ATIVIDADE_ALIAS+".tipologia", TIPOLOGIA_ATIVIDADE_ALIAS);
		
		return this;
	}

	public LicencaBuilder groupByIdLicenca(){
		
		addProjection(Projections.groupProperty("id").as("idLincenca"));
		
		return this;
	}	
	
	public LicencaBuilder groupByNumeroLicenca(){
		
		addProjection(Projections.groupProperty("numero").as("numeroLincenca"));
		
		return this;
	}
	
	public LicencaBuilder groupByNumeroProcesso(){
		
		addProcessosAlias();
		
		addProjection(Projections.groupProperty(PROCESSOS_ALIAS+".numero").as("numeroProcesso"));
		
		return this;
	}
	
	public LicencaBuilder groupByCpfCnpjEmpreendimento(){
		
		addPessoaEmpreendimentoAlias();
		
		addProjection(Projections.groupProperty(PESSOA_EMPREENDIMENTO_ALIAS+".cpf").as("cpfEmpreendimento"));
		addProjection(Projections.groupProperty(PESSOA_EMPREENDIMENTO_ALIAS+".cnpj").as("cnpjEmpreendimento"));
		
		return this;
	}	
	
	public LicencaBuilder groupByDenominacaoEmpreendimento(){
		
		addEmpreendimentoAlias();
		
		addProjection(Projections.groupProperty(EMPREENDIMENTO_ALIAS+".denominacao").as("denominacaoEmpreendimento"));
		
		return this;
	}
	
	public LicencaBuilder groupByMunicipioEmpreendimento(){
		
		addMunicipioEmpreendimentoAlias();
		addProjection(Projections.groupProperty(MUNICIPIO_EMPREENDIMENTO_ALIAS+".nome").as("municipioEmpreendimento"));
		
		addEstadoEmpreendimentoAlias();
		addProjection(Projections.groupProperty(ESTADO_EMPREENDIMENTO_ALIAS+".id").as("siglaEstadoEmpreendimento"));
		
		return this;
	}
	
	public LicencaBuilder groupByLicenca(){
				
		addProjection(Projections.groupProperty("sigla").as("licenca"));
		
		return this;
	}

	public LicencaBuilder filtrarPorNumeroLicenca(String numeroLicenca) {
		
		if (StringUtils.isNotEmpty(numeroLicenca)) {
			
			addRestricton(Restrictions.ilike("numero", numeroLicenca, MatchMode.ANYWHERE));
		}
		
		return this;
	}
	
	public LicencaBuilder filtrarPorNumeroProcesso(String numeroProcesso) {
		
		if (StringUtils.isNotEmpty(numeroProcesso)) {
			
			addRestricton(Restrictions.ilike(PROCESSOS_ALIAS+".numero", numeroProcesso, MatchMode.ANYWHERE));
		}
		
		return this;
	}
	
	public LicencaBuilder filtrarPorCpfCnpjEmpreendimento(String cpfCnpj) {
		
		if (StringUtils.isNotEmpty(cpfCnpj)) {

			addPessoaEmpreendimentoAlias();
			
			criteria.add(Restrictions.or(
					Restrictions.ilike(PESSOA_EMPREENDIMENTO_ALIAS+".cpf", cpfCnpj, MatchMode.START), 
					Restrictions.ilike(PESSOA_EMPREENDIMENTO_ALIAS+".cnpj", cpfCnpj, MatchMode.START)
			));
		}
		
		return this;
	}
	
	public LicencaBuilder filtrarPorIdAtividade(Long idAtividade) {
		
		if (idAtividade != null) {
			
			addAtividadeAlias();
			addRestricton(Restrictions.eq(ATIVIDADE_ALIAS+".id", idAtividade));
		}
		
		return this;
	}
	
	public LicencaBuilder filtrarPorDenominacaoEmpreendimento(String denominacaoEmpreendimento) {
		
		if (StringUtils.isNotEmpty(denominacaoEmpreendimento)) {
			
			addEmpreendimentoAlias();
			addRestricton(Restrictions.ilike(EMPREENDIMENTO_ALIAS+".numero", denominacaoEmpreendimento, MatchMode.ANYWHERE));
		}
		
		return this;
	}
	
	public LicencaBuilder filtrarPorIdMunicipio(Long idMunicipio) {
		
		if (idMunicipio != null) {
			
			addMunicipioEmpreendimentoAlias();
			addRestricton(Restrictions.eq(MUNICIPIO_EMPREENDIMENTO_ALIAS+".id", idMunicipio));
		}
		
		return this;
	}
	
	public LicencaBuilder filtrarPorMunicipio(String municipio) {
		
		if (StringUtils.isNotEmpty(municipio)) {
			
			addMunicipioEmpreendimentoAlias();
			addRestricton(Restrictions.ilike(MUNICIPIO_EMPREENDIMENTO_ALIAS+".nome", municipio, MatchMode.ANYWHERE));
		}
		
		return this;
	}	
		
	public LicencaBuilder filtrarPorIdLicenca(Long idLicenca) {
		
		if (idLicenca != null) {
			
			addRestricton(Restrictions.eq(".id", idLicenca));
		}
		
		return this;
	}
	
	//TODO Adicionar filtro por situação da licença
	
	public LicencaBuilder filtrarPorPeriodoVencimento(Date periodoInicial, Date periodoFinal) {
		
		if (periodoInicial != null) {
			
			addRestricton(Restrictions.ge("dataCadastro", periodoInicial));
		}
		
		if (periodoFinal != null) {
			
			//Somando um dia a mais no periodo final para resolver o problema da data com hora
			periodoFinal = new Date(periodoFinal.getTime() + TimeUnit.DAYS.toMillis(1));
			
			addRestricton(Restrictions.le("dataValidade", periodoFinal));
		}
		
		return this;
	}
	
	public LicencaBuilder orderByNumeroProcesso() {
		
		addOrder(Order.asc("numeroProcesso"));
		
		return this;
	}
	
	public LicencaBuilder count() {
		
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
