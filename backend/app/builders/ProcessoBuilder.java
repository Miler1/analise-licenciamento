package builders;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

import models.Processo;
import utils.IlikeNoAccents;

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
	
	public ProcessoBuilder addAnaliseJuridicaAlias() {
		
		addAlias(ANALISE_ALIAS+".analisesJuridica", ANALISE_JURIDICA_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addCaracterizacoesAlias() {
		
		addAlias("caracterizacoes", CARACTERIZACOES_ALIAS);
		
		return this;
	}
	
	public ProcessoBuilder addAtividadeCaracterizacaoAlias() {
		
		addCaracterizacoesAlias();
		
		addAlias(CARACTERIZACOES_ALIAS+".atividadeCaracterizacao", ATIVIDADE_CARACTERIZACAO_ALIAS);
		
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
		
		addAlias(ANALISE_JURIDICA_ALIAS+".consultoresJuridicos", CONSULTOR_JURIDICO_ALIAS);
		
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
	
	public ProcessoBuilder groupByDataVencimentoPrazoAnaliseJuridica(){
		
		addAnaliseJuridicaAlias();
		addProjection(Projections.groupProperty(ANALISE_JURIDICA_ALIAS+".dataVencimentoPrazo").as("dataVencimentoPrazoAnaliseJuridica"));
		
		return this;
	}
	
	public ProcessoBuilder groupByRevisaoSolicitadaAnaliseJuridica(){
		
		addAnaliseJuridicaAlias();
		addProjection(Projections.groupProperty(ANALISE_JURIDICA_ALIAS+".revisaoSolicitada").as("revisaoSolicitadaAnaliseJuridica"));
		
		return this;
	}	
	
	public ProcessoBuilder filtrarPorNumeroProcesso(String numeroProcesso) {
		
		if (StringUtils.isNotEmpty(numeroProcesso)) {
			
			addRestricton(Restrictions.ilike("numero", numeroProcesso, MatchMode.ANYWHERE));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarAnaliseJuridicaAtiva() {
		
		addAnaliseJuridicaAlias();
		addRestricton(Restrictions.eq(ANALISE_JURIDICA_ALIAS+".ativo", true));
		
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
			addRestricton(Restrictions.eq(MUNICIPIO_EMPREENDIMENTO_ALIAS+".id", idMunicipio));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarPorIdTipologia(Long idTipologia) {
		
		if (idTipologia != null) {
			
			addTipologiaAtividadeAlias();
			addRestricton(Restrictions.eq(TIPOLOGIA_ATIVIDADE_ALIAS+".id", idTipologia));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarPorIdAtividade(Long idAtividade) {
		
		if (idAtividade != null) {
			
			addAtividadeAlias();
			addRestricton(Restrictions.eq(ATIVIDADE_ALIAS+".id", idAtividade));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarPorIdCondicao(Long idCondicao) {
		
		if (idCondicao != null) {
			
			addObjetoTramitavelAlias();
			addRestricton(Restrictions.eq(OBJETO_TRAMITAVEL_ALIAS+".condicao.idCondicao", idCondicao));
		}
		
		return this;
	}
	
	public ProcessoBuilder filtrarPorIdConsultorJuridico(Long idConsultorJuridico) {
		
		if (idConsultorJuridico != null) {
			
			addConsultorJuridicoAlias();
			addRestricton(Restrictions.eq(CONSULTOR_JURIDICO_ALIAS+".usuario.id", idConsultorJuridico));
		}
		
		return this;
	}	
	
	public ProcessoBuilder orderByDataVencimentoPrazoAnaliseJuridica() {
		
		addOrder(Order.asc("dataVencimentoPrazoAnaliseJuridica"));
		
		return this;
	}		
	
	public ProcessoBuilder count() {
		
		addProjection(Projections.countDistinct("id").as("total"));
		
		return this;
	}
	
	public static class FiltroProcesso {
		
		public String numeroProcesso;
		public String cpfCnpjEmpreendimento;
		public Long idMunicipioEmpreendimento;
		public Long idTipologiaEmpreendimento;
		public Long idAtividadeEmpreendimento;
		public Long idCondicaoTramitacao;
		public Long paginaAtual;
		public Long itensPorPagina;
		
		public FiltroProcesso() {
			
		}
	}
}
