package builders;

import models.Processo;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StringType;
import security.Auth;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessoBuilder extends CriteriaBuilder<Processo> {

	private static final String EMPREENDIMENTO_ALIAS = "emp";
	private static final String PESSOA_EMPREENDIMENTO_ALIAS = "pes";
	private static final String MUNICIPIO_EMPREENDIMENTO_ALIAS = "mun";
	private static final String ESTADO_EMPREENDIMENTO_ALIAS = "est";
	private static final String ANALISE_ALIAS = "ana";
	private static final String PROCESSO_ANTERIOR_ALIAS = "poa";
	private static final String PROCESSO_ANTERIOR_ANALISE_ALIAS = "paa";
	private static final String PROCESSO_ANTERIOR_ANALISE_GEO_ALIAS = "pga";
	private static final String PROCESSO_ANTERIOR_ANALISE_TECNICA_ALIAS = "pta";
	private static final String PROCESSO_ANTERIOR_ANALITAS_GEO_ALIAS = "pag";
	private static final String PROCESSO_ANTERIOR_ANALITAS_TECNICOS_ALIAS = "pat";
	private static final String ANALISE_JURIDICA_ALIAS = "anj";
	private static final String CARACTERIZACAO_ALIAS = "carac";
	private static final String ATIVIDADE_CARACTERIZACAO_ALIAS = "atc";
	private static final String ATIVIDADE_ALIAS = "atv";
	private static final String TIPOLOGIA_ATIVIDADE_ALIAS = "tip";
	private static final String OBJETO_TRAMITAVEL_ALIAS = "obt";
	private static final String HISTORICO_OBJETO_TRAMITAVEL_ALIAS = "hot";
	private static final String CRITERIA_HISTORICO_OBJETO_TRAMITAVEL_ALIAS = "cht";
	private static final String CONSULTOR_JURIDICO_ALIAS = "coj";
	private static final String ANALISE_TECNICA_ALIAS = "ant";
	private static final String ANALISE_GEO_ALIAS = "ang";
	private static final String ANALISTA_TECNICO_ALIAS = "att";
	private static final String ANALISTA_GEO_ALIAS = "agt";
	private static final String DIRETOR_ALIAS = "dt";
	private static final String ATIVIDADE_CNAE_ALIAS = "atvc";
	private static final String TIPO_CARACTERIZACAO_ATIVIDADE_ALIAS = "tca";
	private static final String COORDENADOR_ALIAS = "coord";
	private static final String DIA_ANALISE_ALIAS = "da";
	private static final String CONDICAO_ALIAS = "ca";
	private static final String DESVINCULO_ANALISE_GEO_ALIAS = "dea";
	private static final String DESVINCULO_ANALISE_TECNICA_ALIAS = "deat";
	private static final String DESVINCULO_ANALISTA_GEO_DESTINO_ALIAS = "dagd";
	private static final String DESVINCULO_ANALISTA_TECNICO_DESTINO_ALIAS = "datd";
	private static final String DESVINCULO_ANALISTA_GEO_SOLICITANTE_ALIAS = "dags";
	private static final String DESVINCULO_ANALISTA_TECNICO_SOLICITANTE_ALIAS = "dats";
	private static final String PORTE_EMPREENDIMENTO_ALIAS = "pe";
	private static final String POTENCIAL_POLUIDOR_ALIAS = "pp";

	public ProcessoBuilder addEmpreendimentoAlias() {

		addAlias("empreendimento", EMPREENDIMENTO_ALIAS);

		return this;
	}

	public ProcessoBuilder addCondicaoAlias() {

		addObjetoTramitavelAlias();

		addAlias(OBJETO_TRAMITAVEL_ALIAS+".condicao", CONDICAO_ALIAS);

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

	public ProcessoBuilder addProcessoAnaliseGeoAnteriorAlias() {

		addAlias("processoAnterior", PROCESSO_ANTERIOR_ALIAS, JoinType.LEFT_OUTER_JOIN);
		addAlias(PROCESSO_ANTERIOR_ALIAS + ".analise", PROCESSO_ANTERIOR_ANALISE_ALIAS, JoinType.LEFT_OUTER_JOIN);
		addAlias(PROCESSO_ANTERIOR_ANALISE_ALIAS + ".analisesGeo", PROCESSO_ANTERIOR_ANALISE_GEO_ALIAS, JoinType.LEFT_OUTER_JOIN);
		addAlias(PROCESSO_ANTERIOR_ANALISE_GEO_ALIAS + ".analistasGeo", PROCESSO_ANTERIOR_ANALITAS_GEO_ALIAS, JoinType.LEFT_OUTER_JOIN);
		return this;
	}

	public ProcessoBuilder addProcessoAnaliseTecnicaAnteriorAlias() {

		addAlias("processoAnterior", PROCESSO_ANTERIOR_ALIAS, JoinType.LEFT_OUTER_JOIN);
		addAlias(PROCESSO_ANTERIOR_ALIAS + ".analise", PROCESSO_ANTERIOR_ANALISE_ALIAS, JoinType.LEFT_OUTER_JOIN);
		addAlias(PROCESSO_ANTERIOR_ANALISE_ALIAS + ".analisesTecnicas", PROCESSO_ANTERIOR_ANALISE_TECNICA_ALIAS, JoinType.LEFT_OUTER_JOIN);
		addAlias(PROCESSO_ANTERIOR_ANALISE_TECNICA_ALIAS + ".analistaTecnico", PROCESSO_ANTERIOR_ANALITAS_TECNICOS_ALIAS, JoinType.LEFT_OUTER_JOIN);
		return this;
	}

	public ProcessoBuilder addAnaliseAlias() {

		addAlias("analise", ANALISE_ALIAS);

		return this;
	}

	public ProcessoBuilder addDesvinculoAnaliseGeoAlias(Boolean isLeftOuterJoin) {

		addAnaliseGeoAlias(isLeftOuterJoin);
		addAlias(ANALISE_GEO_ALIAS + ".desvinculos", DESVINCULO_ANALISE_GEO_ALIAS, JoinType.LEFT_OUTER_JOIN);

		return this;
	}

	public ProcessoBuilder addDesvinculoAnaliseTecnicaAlias() {

		addAnaliseTecnicaAlias(true);
		addAlias(ANALISE_TECNICA_ALIAS + ".desvinculos", DESVINCULO_ANALISE_TECNICA_ALIAS, JoinType.LEFT_OUTER_JOIN);

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

	public ProcessoBuilder addAnaliseGeoAlias() {

		addAnaliseAlias();

//		
		addAlias(ANALISE_ALIAS+".analisesGeo", ANALISE_GEO_ALIAS, JoinType.LEFT_OUTER_JOIN);

		return this;
	}

	public ProcessoBuilder addAnaliseTecnicaAlias() {

		addAnaliseAlias();

		addAlias(ANALISE_ALIAS+".analisesTecnicas", ANALISE_TECNICA_ALIAS, JoinType.LEFT_OUTER_JOIN);
//		

		return this;
	}

	public ProcessoBuilder addCaracterizacaoAlias() {

		addAlias("caracterizacao", CARACTERIZACAO_ALIAS);

		return this;
	}

	public ProcessoBuilder addAtividadeCaracterizacaoAlias() {

		addCaracterizacaoAlias();

		addAlias(CARACTERIZACAO_ALIAS +".atividadesCaracterizacao", ATIVIDADE_CARACTERIZACAO_ALIAS);

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

	public ProcessoBuilder addHistoricoObjetoTramitavelAlias(Boolean isLeftOuterJoin) {

		addAtividadeCaracterizacaoAlias();

		if(isLeftOuterJoin) {

			addAlias(OBJETO_TRAMITAVEL_ALIAS + ".historicoTramitacoes", HISTORICO_OBJETO_TRAMITAVEL_ALIAS, JoinType.LEFT_OUTER_JOIN);

		} else {

			addAlias(OBJETO_TRAMITAVEL_ALIAS + ".historicoTramitacoes", HISTORICO_OBJETO_TRAMITAVEL_ALIAS);

		}

		return this;

	}

	public ProcessoBuilder addConsultorJuridicoAlias() {

		addAnaliseJuridicaAlias();

		addAlias(ANALISE_JURIDICA_ALIAS+".consultoresJuridicos", CONSULTOR_JURIDICO_ALIAS);

		return this;
	}

	public ProcessoBuilder addAnaliseGeoAlias(Boolean isLeftOuterJoin) {

		addAnaliseAlias();

		if (isLeftOuterJoin){

			addAlias(ANALISE_ALIAS+".analisesGeo", ANALISE_GEO_ALIAS, JoinType.LEFT_OUTER_JOIN);

		} else {
//
			addAlias(ANALISE_ALIAS+".analisesGeo", ANALISE_GEO_ALIAS);
		}

		return this;
	}

	public ProcessoBuilder addAnalistaGeoAlias() {

		addAlias("analise", ANALISE_ALIAS, JoinType.LEFT_OUTER_JOIN);
		addAlias(ANALISE_ALIAS+".analisesGeo", ANALISE_GEO_ALIAS, JoinType.LEFT_OUTER_JOIN);
		addAlias(ANALISE_GEO_ALIAS+".analistasGeo", ANALISTA_GEO_ALIAS, JoinType.LEFT_OUTER_JOIN);

		return this;
	}

	public ProcessoBuilder addAnaliseTecnicaAlias(Boolean isLeftOuterJoin) {

		addAnaliseAlias();

		if (isLeftOuterJoin){

			addAlias(ANALISE_ALIAS+".analisesTecnicas", ANALISE_TECNICA_ALIAS, JoinType.LEFT_OUTER_JOIN);

		} else {
//
			addAlias(ANALISE_ALIAS+".analisesTecnicas", ANALISE_TECNICA_ALIAS);
		}

		return this;
	}

	public ProcessoBuilder addAnalistaGeoDesvinculoDestinoAlias(Boolean isLeftOuterJoin) {

		addAnaliseGeoAlias(isLeftOuterJoin);

		if (isLeftOuterJoin){

			addAlias(ANALISE_GEO_ALIAS+".analistasGeo", ANALISTA_GEO_ALIAS, JoinType.LEFT_OUTER_JOIN);

		} else {
//
			addAlias(ANALISE_GEO_ALIAS+".analistasGeo", ANALISTA_GEO_ALIAS);
		}

		return this;
	}

	public ProcessoBuilder addAnalistaGeoDesvinculoDestinoAlias() {

		addDesvinculoAnaliseGeoAlias(false);
		addAlias(DESVINCULO_ANALISE_GEO_ALIAS + ".analistaGeoDestino", DESVINCULO_ANALISTA_GEO_DESTINO_ALIAS, JoinType.LEFT_OUTER_JOIN);

		return this;

	}

	public ProcessoBuilder addAnalistaGeoDesvinculoSolicitanteAlias() {

		addDesvinculoAnaliseGeoAlias(false);
		addAlias(DESVINCULO_ANALISE_GEO_ALIAS + ".analistaGeo", DESVINCULO_ANALISTA_GEO_SOLICITANTE_ALIAS, JoinType.LEFT_OUTER_JOIN);

		return this;

	}

	public ProcessoBuilder addAnalistaTecnicaDesvinculoDestinoAlias() {

		addDesvinculoAnaliseGeoAlias(true);
		addAlias(DESVINCULO_ANALISE_TECNICA_ALIAS + ".analistaTecnicoDestino", DESVINCULO_ANALISTA_TECNICO_DESTINO_ALIAS, JoinType.LEFT_OUTER_JOIN);

		return this;

	}

	public ProcessoBuilder addAnalistaTecnicaDesvinculoSolicitanteAlias() {

		addDesvinculoAnaliseTecnicaAlias();
		addAlias(DESVINCULO_ANALISE_TECNICA_ALIAS + ".analistaTecnico", DESVINCULO_ANALISTA_TECNICO_SOLICITANTE_ALIAS, JoinType.LEFT_OUTER_JOIN);

		return this;

	}

	public ProcessoBuilder addAnalistaTecnicoAlias(Boolean isLeftOuterJoin) {

		addAnaliseTecnicaAlias(isLeftOuterJoin);

		if (isLeftOuterJoin){

			addAlias(ANALISE_TECNICA_ALIAS + ".analistaTecnico", ANALISTA_TECNICO_ALIAS, JoinType.LEFT_OUTER_JOIN);

		} else {

			addAlias(ANALISE_TECNICA_ALIAS + ".analistaTecnico", ANALISTA_TECNICO_ALIAS);
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

	public ProcessoBuilder addPorteEmpreendimentoAlias() {

		addAtividadeCaracterizacaoAlias();

		addAlias(ATIVIDADE_CARACTERIZACAO_ALIAS +".porteEmpreendimento", PORTE_EMPREENDIMENTO_ALIAS);

		return this;
	}

	public ProcessoBuilder addPotencialPoluidorAlias() {

		addAtividadeAlias();

		addAlias(ATIVIDADE_ALIAS +".potencialPoluidor", POTENCIAL_POLUIDOR_ALIAS);

		return this;
	}

	public ProcessoBuilder comTiposLicencas(){

		StringBuilder sb = new StringBuilder();

		sb.append("(SELECT string_agg(t.sigla, '-') ");
		sb.append("FROM licenciamento.tipo_licenca t ");
		sb.append("INNER JOIN licenciamento.caracterizacao c ON t.id = c.id_tipo_licenca ");
		sb.append("INNER JOIN analise.processo p ON c.id = p.id_caracterizacao ");
		sb.append("WHERE p.id = {alias}.id) AS licencas");

		addProjection(Projections.sqlProjection(sb.toString(), new String[]{"licencas"}, new org.hibernate.type.Type[]{StringType.INSTANCE}));

		return this;
	}

	public ProcessoBuilder groupByIdProcesso(){

		addProjection(Projections.groupProperty("id").as("idProcesso"));

		return this;
	}

	public ProcessoBuilder groupByIdAnalistaGeoAnterior(){

		addProcessoAnaliseGeoAnteriorAlias();

		addProjection(Projections.groupProperty(PROCESSO_ANTERIOR_ANALITAS_GEO_ALIAS +  ".usuario.id").as("idAnalistaGeoAnterior"));

		return this;
	}

	public ProcessoBuilder groupByIdAnalistaTecnicoAnterior(){

		addProcessoAnaliseTecnicaAnteriorAlias();

		addProjection(Projections.groupProperty(PROCESSO_ANTERIOR_ANALITAS_TECNICOS_ALIAS +  ".usuario.id").as("idAnalistaTecnicoAnterior"));

		return this;
	}

	public ProcessoBuilder groupByIdAnalistaGeo(){

		addAnalistaGeoAlias();

		addProjection(Projections.groupProperty(ANALISTA_GEO_ALIAS +  ".usuario.id").as("idAnalistaGeo"));

		return this;
	}

	public ProcessoBuilder groupByIdAnalistaTecnico(){

		addAnalistaTecnicoAlias(true);

		addProjection(Projections.groupProperty(ANALISTA_TECNICO_ALIAS +  ".usuario.id").as("idAnalistaTecnico"));

		return this;
	}

	public ProcessoBuilder groupByNumeroProcesso(){

		addProjection(Projections.groupProperty("numero").as("numero"));

		return this;
	}

	public ProcessoBuilder groupByCaracterizacao(){

		addCaracterizacaoAlias();
		addProjection(Projections.groupProperty(CARACTERIZACAO_ALIAS+".status.id").as("statusCaracterizacao"));

		return this;
	}

	public ProcessoBuilder groupByCpfCnpjEmpreendimento(){

		addEmpreendimentoAlias();

		addProjection(Projections.groupProperty(EMPREENDIMENTO_ALIAS+".cpfCnpj").as("cpfEmpreendimento"));

		return this;
	}

	public ProcessoBuilder groupByObjetoTramitavel(){

		addObjetoTramitavelAlias();
		addProjection(Projections.groupProperty(OBJETO_TRAMITAVEL_ALIAS + ".condicao.id").as("idCondicaoTramitacao"));

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
		addProjection(Projections.groupProperty(MUNICIPIO_EMPREENDIMENTO_ALIAS+".id").as("idMunicipioEmpreendimento"));

		addEstadoEmpreendimentoAlias();
		addProjection(Projections.groupProperty(ESTADO_EMPREENDIMENTO_ALIAS+".id").as("siglaEstadoEmpreendimento"));

		return this;
	}

	public ProcessoBuilder groupByDataVencimentoPrazoAnalise(){

		addAnaliseAlias();
		addProjection(Projections.groupProperty(ANALISE_ALIAS+".dataVencimentoPrazo").as("dataVencimentoPrazoAnalise"));

		return this;
	}

	public ProcessoBuilder groupByDataVencimentoPrazoAnaliseGeo(Boolean isLeftOuterJoin){

		addAnaliseGeoAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_GEO_ALIAS+".dataVencimentoPrazo").as("dataVencimentoPrazoAnaliseGeo"));

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

	public ProcessoBuilder groupByDataFinalAnaliseGeo() {

		addAnaliseGeoAlias();
		addProjection(Projections.groupProperty(ANALISE_GEO_ALIAS+".dataFim").as("dataConclusaoAnaliseGeo"));

		return this;
	}

	public ProcessoBuilder groupByPrazoAnaliseCoordenador() {

		addObjetoTramitavelAlias();
		addProjection(Projections.groupProperty(ANALISE_ALIAS + ".dataVencimentoPrazo").as("prazoAnaliseCoordenador"));

		return this;

	}

	public ProcessoBuilder groupByDataFinalAnaliseTecnica(Boolean isLeftOuterJoin) {

		addAnaliseTecnicaAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_TECNICA_ALIAS+".dataFim").as("dataConclusaoAnaliseTecnica"));

		return this;
	}

	public ProcessoBuilder groupByRevisaoSolicitadaAnaliseJuridica(){

		addAnaliseJuridicaAlias();
		addProjection(Projections.groupProperty(ANALISE_JURIDICA_ALIAS+".revisaoSolicitada").as("revisaoSolicitadaAnaliseJuridica"));

		return this;
	}

	public ProcessoBuilder groupByDataFinalAnaliseGeo(Boolean isLeftOuterJoin) {

		addAnaliseGeoAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_GEO_ALIAS+".dataFim").as("dataConclusaoAnaliseGeo"));

		return this;
	}

	public ProcessoBuilder groupByRevisaoSolicitadaAnaliseGeo(){

		addAnaliseGeoAlias();
		addProjection(Projections.groupProperty(ANALISE_GEO_ALIAS+".revisaoSolicitada").as("revisaoSolicitadaAnaliseGeo"));

		return this;
	}

	public ProcessoBuilder groupByNotificacaoAtendidaAnaliseJuridica() {

		addAnaliseJuridicaAlias();
		addProjection(Projections.groupProperty(ANALISE_JURIDICA_ALIAS+".notificacaoAtendida").as("notificacaoAtendida"));

		return this;
	}

	public ProcessoBuilder groupByNotificacaoAtendidaAnaliseGeo() {

		addAnaliseGeoAlias();
		addProjection(Projections.groupProperty(ANALISE_GEO_ALIAS+".notificacaoAtendida").as("notificacaoAtendida"));

		return this;
	}

	public ProcessoBuilder groupByNotificacaoAtendidaAnaliseTecnica(Boolean isLeftOuterJoin) {

		addAnaliseTecnicaAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_TECNICA_ALIAS+".notificacaoAtendida").as("notificacaoAtendida"));

		return this;
	}

	public ProcessoBuilder groupByIdAnaliseJuridica(){

		addAnaliseJuridicaAlias();
		addProjection(Projections.groupProperty(ANALISE_JURIDICA_ALIAS+".id").as("idAnaliseJuridica"));

		return this;
	}

	public ProcessoBuilder groupByNotificacaoAtendidaAnaliseGeo(Boolean isLeftOuterJoin) {

		addAnaliseGeoAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_GEO_ALIAS+".notificacaoAtendida").as("notificacaoAtendida"));

		return this;
	}

	public ProcessoBuilder groupByIdAnaliseGeo(){

		addAnaliseGeoAlias();
		addProjection(Projections.groupProperty(ANALISE_GEO_ALIAS+".id").as("idAnaliseGeo"));

		return this;
	}

	public ProcessoBuilder groupByIdAnaliseTecnica(){

		addAnaliseTecnicaAlias(true);
		addProjection(Projections.groupProperty(ANALISE_TECNICA_ALIAS+".id").as("idAnaliseTecnica"));

		return this;
	}


	public ProcessoBuilder groupByDataVencimentoPrazoAnaliseTecnica(Boolean isLeftOuterJoin) {

		addAnaliseTecnicaAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_TECNICA_ALIAS+".dataVencimentoPrazo").as("dataVencimentoPrazoAnaliseTecnica"));

		return this;
	}

	public ProcessoBuilder groupByDataVencimentoPrazoAnaliseGeo(boolean isLeftOuterJoin) {

		addAnaliseGeoAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_GEO_ALIAS+".dataVencimentoPrazo").as("dataVencimentoPrazoAnaliseGeo"));

		return this;
	}

	public ProcessoBuilder groupByRevisaoSolicitadaAnaliseTecnica(Boolean isLeftOuterJoin) {

		addAnaliseTecnicaAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_TECNICA_ALIAS+".revisaoSolicitada").as("revisaoSolicitadaAnaliseTecnica"));

		return this;
	}

	public ProcessoBuilder groupByRevisaoSolicitadaAnaliseGeo(Boolean isLeftOuterJoin) {

		addAnaliseGeoAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_GEO_ALIAS+".revisaoSolicitada").as("revisaoSolicitadaAnaliseGeo"));

		return this;
	}

	public ProcessoBuilder groupByIdAnaliseTecnica(Boolean isLeftOuterJoin) {

		addAnaliseTecnicaAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_TECNICA_ALIAS+".id").as("idAnaliseTecnica"));

		return this;
	}

	public ProcessoBuilder groupByIdAnaliseGeo(Boolean isLeftOuterJoin) {

		addAnaliseGeoAlias(isLeftOuterJoin);
		addProjection(Projections.groupProperty(ANALISE_GEO_ALIAS+".id").as("idAnaliseGeo"));

		return this;
	}

	public ProcessoBuilder groupByIdAnalise() {

		addProjection(Projections.groupProperty(ANALISE_ALIAS+".id").as("idAnalise"));

		return this;
	}

	public ProcessoBuilder groupByDesvinculoAnaliseGeo() {

		addAnalistaGeoDesvinculoDestinoAlias();
		addProjection(Projections.groupProperty(DESVINCULO_ANALISTA_GEO_DESTINO_ALIAS + ".login").as("loginUsuarioDestino"));

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

	public ProcessoBuilder filtrarAnaliseGeoAtiva() {

		addAnaliseGeoAlias();
		addRestriction(Restrictions.eq(ANALISE_GEO_ALIAS+".ativo", true));

		return this;
	}


	public ProcessoBuilder filtrarPorCpfCnpjEmpreendimento(String cpfCnpj) {

		if (StringUtils.isNotEmpty(cpfCnpj)) {

			addEmpreendimentoAlias();

			addRestriction(Restrictions.ilike(EMPREENDIMENTO_ALIAS+".cpfCnpj", cpfCnpj, MatchMode.START));
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

	public ProcessoBuilder filtrarPorListaIdCondicao(List<Long> listaIdCondicao) {

		if (listaIdCondicao != null && !listaIdCondicao.isEmpty()) {

			addObjetoTramitavelAlias();
			addRestriction(Restrictions.in(OBJETO_TRAMITAVEL_ALIAS + ".condicao.idCondicao", listaIdCondicao));

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

	public ProcessoBuilder filtrarIdCoordenador(Long idUsuarioCoordenador) {

		if (idUsuarioCoordenador != null) {

			addObjetoTramitavelAlias();
			addRestriction(Restrictions.eq(OBJETO_TRAMITAVEL_ALIAS + ".usuarioResponsavel.id", idUsuarioCoordenador));

		}

		return this;

	}

	public ProcessoBuilder filtrarIdDiretor(Long idUsuarioDiretor) {

		if (idUsuarioDiretor != null) {

			addObjetoTramitavelAlias();
			addRestriction(Restrictions.eq(OBJETO_TRAMITAVEL_ALIAS + ".usuarioResponsavel.id", idUsuarioDiretor));

		}

		return this;

	}

	public ProcessoBuilder filtrarIdSecretario(Long idUsuarioSecretario) {

		if (idUsuarioSecretario != null) {

			addObjetoTramitavelAlias();
			addRestriction(Restrictions.eq(OBJETO_TRAMITAVEL_ALIAS + ".usuarioResponsavel.id", idUsuarioSecretario));

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

	public ProcessoBuilder filtrarPorIdAnalistaTecnico(Long idAnalistaTecnico, Boolean isLeftOuterJoin) {

		if (idAnalistaTecnico != null) {

			addAnalistaTecnicoAlias(isLeftOuterJoin);
			addRestriction(Restrictions.eq(ANALISTA_TECNICO_ALIAS + ".usuario.id", idAnalistaTecnico));
		}

		return this;
	}

	public ProcessoBuilder filtrarPorIdAnalistaGeo(Long idAnalistaGeo, Boolean isLeftOuterJoin) {

		if (idAnalistaGeo != null) {

			addAnalistaGeoDesvinculoDestinoAlias(isLeftOuterJoin);
			addRestriction(Restrictions.eq(ANALISTA_GEO_ALIAS+".usuario.id", idAnalistaGeo));

		}

		return this;
	}

	public ProcessoBuilder filtrarPorAnaliseAiva(Boolean analiseAtiva){

		addAnaliseAlias();
		addRestriction(Restrictions.eq(ANALISE_ALIAS+".ativo", analiseAtiva));
		return this;

	}

	public ProcessoBuilder filtrarPorIdDiretor(Long idDiretor, boolean isLeftOuterJoin) {

		if (idDiretor != null) {

			addRestriction(Restrictions.eq(DIRETOR_ALIAS+".usuario.id", idDiretor));

		}

		return this;
	}

	public ProcessoBuilder filtrarAnaliseTecnicaAtiva(Boolean isLeftOuterJoin) {

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

	public ProcessoBuilder filtrarDesvinculoAnaliseGeoComResposta(Boolean isLeftOuterJoin) {

		addAnaliseGeoAlias(isLeftOuterJoin);
		addDesvinculoAnaliseGeoAlias(true);
		addAnalistaGeoDesvinculoDestinoAlias();

		addRestriction(Restrictions.or(
				Restrictions.isEmpty(ANALISE_GEO_ALIAS + ".desvinculos"),
				Restrictions.eq(DESVINCULO_ANALISTA_GEO_DESTINO_ALIAS + ".login", Auth.getUsuarioSessao().login))
		);

		return this;

	}

	public ProcessoBuilder filtrarDesvinculoAnaliseGeoSemResposta() {

		addAnaliseGeoAlias(true);
		addDesvinculoAnaliseGeoAlias(false);
		addAnalistaGeoDesvinculoSolicitanteAlias();
		addDesvinculoAnaliseTecnicaAlias();

		addRestriction(Restrictions.and(
				Restrictions.isNull(DESVINCULO_ANALISE_TECNICA_ALIAS + ".analiseTecnica"),
				Restrictions.or(
						Restrictions.isEmpty(ANALISE_GEO_ALIAS + ".desvinculos"),
				        Restrictions.and(
				        		Restrictions.eq(DESVINCULO_ANALISTA_GEO_SOLICITANTE_ALIAS + ".login", Auth.getUsuarioSessao().login)),
								Restrictions.isNull(DESVINCULO_ANALISE_GEO_ALIAS + ".aprovada")
						)
				)
		);

		return this;

	}

	public ProcessoBuilder filtrarDesvinculoAnaliseTecnicaComResposta(Boolean isLeftOuterJoin) {

		addAnaliseTecnicaAlias(isLeftOuterJoin);
		addDesvinculoAnaliseTecnicaAlias();
		addAnalistaTecnicaDesvinculoDestinoAlias();

		addRestriction(Restrictions.or(
				Restrictions.isEmpty(ANALISE_TECNICA_ALIAS + ".desvinculos"),
				Restrictions.eq(DESVINCULO_ANALISTA_TECNICO_DESTINO_ALIAS + ".login", Auth.getUsuarioSessao().login))
		);

		return this;

	}

	public ProcessoBuilder filtrarDesvinculoAnaliseTecnicaSemResposta() {

		addAnaliseTecnicaAlias(true);
		addDesvinculoAnaliseTecnicaAlias();
		addAnalistaTecnicaDesvinculoSolicitanteAlias();

		addRestriction(Restrictions.or(
				Restrictions.isEmpty(ANALISE_TECNICA_ALIAS + ".desvinculos"),
				Restrictions.and(
						Restrictions.eq(DESVINCULO_ANALISTA_TECNICO_SOLICITANTE_ALIAS + ".login", Auth.getUsuarioSessao().login)),
						Restrictions.isNull(DESVINCULO_ANALISE_TECNICA_ALIAS + ".aprovada")
				)
		);

		return this;

	}

	public ProcessoBuilder filtrarAnaliseGeoAtiva(Boolean isLeftOuterJoin) {

		addAnaliseGeoAlias(isLeftOuterJoin);

		if (isLeftOuterJoin) {

			addRestriction(Restrictions.or(
					Restrictions.isNull(ANALISE_GEO_ALIAS+".ativo"),
					Restrictions.eq(ANALISE_GEO_ALIAS+".ativo", true)
			));

		} else {

			addRestriction(Restrictions.eq(ANALISE_GEO_ALIAS+".ativo", true));
		}

		return this;
	}

	public ProcessoBuilder filtrarPorSiglaSetor(String siglaSetor) {

		if (siglaSetor != null) {

			addTipoCaracterizacaoAtividade();
			addAtividadeAlias();
			addRestriction(Restrictions.eq(ATIVIDADE_ALIAS+".siglaSetor", siglaSetor));
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


	public ProcessoBuilder filtrarPorIdUsuarioValidacaoGeo(Long idUsuarioValidacao) {

		if (idUsuarioValidacao != null) {

			addAnaliseGeoAlias(false);
			addRestriction(Restrictions.eq(ANALISE_GEO_ALIAS+".usuarioValidacao.id", idUsuarioValidacao));
		}

		return this;
	}

	public ProcessoBuilder filtrarPorSiglaSetores(List<String> siglasSetores) {

		if (siglasSetores != null && siglasSetores.size() > 0) {

			addTipoCaracterizacaoAtividade();
			addAtividadeAlias();
			addRestriction(Restrictions.in(ATIVIDADE_ALIAS+".siglaSetor", siglasSetores));
			addRestriction(Restrictions.eqProperty(TIPO_CARACTERIZACAO_ATIVIDADE_ALIAS+".atividade.id", ATIVIDADE_CARACTERIZACAO_ALIAS+".atividade.id"));
		}

		return this;
	}

	public ProcessoBuilder filtrarPorIdUsuarioValidacaoTecnicaCoordenador(Long idUsuarioValidacaoCoordenador) {

		if (idUsuarioValidacaoCoordenador != null) {

			addAnaliseTecnicaAlias(false);
			addRestriction(Restrictions.eq(ANALISE_TECNICA_ALIAS+".usuarioValidacaoCoordenador.id", idUsuarioValidacaoCoordenador));
		}

		return this;
	}

	public ProcessoBuilder filtrarPorIdUsuarioValidacaoGeoCoordenador(Long idUsuarioValidacaoCoordenador) {

		if (idUsuarioValidacaoCoordenador != null) {

			addAnaliseGeoAlias(false);
			addRestriction(Restrictions.eq(ANALISE_GEO_ALIAS+".usuarioValidacaoCoordenador.id", idUsuarioValidacaoCoordenador));
		}

		return this;
	}

	public ProcessoBuilder filtrarPorIdCoordenador(Long idCoordenador) {

		if (idCoordenador != null) {

			addRestriction(Restrictions.eq(COORDENADOR_ALIAS + ".usuario.id", idCoordenador));
		}

		return this;
	}

	public ProcessoBuilder orderByPrazoAnaliseCoordenador() {

		addOrder(Order.asc("prazoAnaliseCoordenador"));

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

	public ProcessoBuilder orderByDataVencimentoPrazoAnaliseGeo() {

		addOrder(Order.asc("dataVencimentoPrazoAnaliseGeo"));

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

	public ProcessoBuilder groupByDiasAnaliseGeo(){

		addDiasAnaliseAlias();
		addProjection(Projections.groupProperty(DIA_ANALISE_ALIAS+".qtdeDiasGeo").as("diasAnaliseGeo"));

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

	public ProcessoBuilder groupByRetificacao() {

		addProjection(Projections.groupProperty("retificacao").as("retificacao"));

		return this;
	}

	public ProcessoBuilder groupByIdOrigemNotificacao() {

		addProjection(Projections.groupProperty("origemNotificacao.id").as("idOrigemNotificacao"));

		return this;
	}

	public ProcessoBuilder groupByPorte() {

		addPorteEmpreendimentoAlias();

		addProjection(Projections.groupProperty(PORTE_EMPREENDIMENTO_ALIAS+".nome")
				.as("porteEmpreendimento"));

		return this;
	}

	public ProcessoBuilder groupByPotencialPoluidor() {

		addPotencialPoluidorAlias();

		addProjection(Projections.groupProperty(POTENCIAL_POLUIDOR_ALIAS+".nome")
				.as("potencialPoluidorAtividade"));

		return this;
	}

	public static class FiltroProcesso {

		public String numeroProcesso;
		public String cpfCnpjEmpreendimento;
		public Long idMunicipioEmpreendimento;
		public Long idTipologiaEmpreendimento;
		public Long idAtividadeEmpreendimento;
		public Long idCondicaoTramitacao;
		public List<Long> listaIdCondicaoTramitacao;
		public Boolean filtrarPorUsuario = false;
		public Date periodoInicial;
		public Date periodoFinal;
		public Long paginaAtual;
		public Long itensPorPagina;
		public Boolean isAnaliseJuridica = false;
		public Boolean isAnaliseTecnica = false;
		public Boolean isAnaliseTecnicaOpcional = false;
		public Long idAnalistaTecnico;
		public Long idDiretor;
		public Boolean isAnaliseGeo = false;
		public Boolean isAnaliseGeoOpcional = false;
		public Boolean isCoordenador = false;
		public Boolean isCoordenadorGeo = false;
		public boolean isDiretor;
		public boolean isSecretario;
		public Long idAnalistaGeo;
		public String siglaSetorGerencia;
		public String siglaSetorCoordenadoria;
		public Long idConsultorJuridico;
		public Long idUsuarioLogado;
		public Boolean isConsultarProcessos = false;
		public Boolean analiseAtiva = false;

		public FiltroProcesso() {

		}
	}
}