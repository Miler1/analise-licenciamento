package builders;

import models.manejoDigital.ProcessoManejo;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class ProcessoManejoBuilder extends CriteriaBuilder<ProcessoManejo> {

	private static final String EMPREENDIMENTO_ALIAS = "emp";
	private static final String MUNICIPIO_EMPREENDIMENTO_ALIAS = "mun";
	private static final String ESTADO_EMPREENDIMENTO_ALIAS = "est";
	private static final String TIPO_LICENCA_ALIAS = "tpla";
	private static final String ATIVIDADE_ALIAS = "atv";
	private static final String TIPOLOGIA_ATIVIDADE_ALIAS = "tip";
	private static final String OBJETO_TRAMITAVEL_ALIAS = "obt";

	public static class FiltroProcessoManejo {

		public String numeroProcesso;
		public String cpfCnpjEmpreendimento;
		public Long idMunicipioEmpreendimento;
		public Long idTipologia;
		public Long idAtividade;
		public Long idManejoDigital;
		public Long idStatusLicenca;
		public Long paginaAtual;
		public Long itensPorPagina;

		public FiltroProcessoManejo() {

		}
	}

	public ProcessoManejoBuilder addEmpreendimentoAlias() {

		addAlias("empreendimento", EMPREENDIMENTO_ALIAS);

		return this;
	}

	public ProcessoManejoBuilder addMunicipioEmpreendimentoAlias() {

		addEmpreendimentoAlias();

		addAlias(EMPREENDIMENTO_ALIAS+".municipio", MUNICIPIO_EMPREENDIMENTO_ALIAS);

		return this;
	}

	public ProcessoManejoBuilder addEstadoEmpreendimentoAlias() {

		addMunicipioEmpreendimentoAlias();

		addAlias(MUNICIPIO_EMPREENDIMENTO_ALIAS+".estado", ESTADO_EMPREENDIMENTO_ALIAS);

		return this;
	}

	public ProcessoManejoBuilder addAtividadeAlias() {

		addAlias("atividadeManejo", ATIVIDADE_ALIAS);

		return this;
	}

	public ProcessoManejoBuilder addTipologiaAtividadeAlias() {

		addAtividadeAlias();

		addAlias(ATIVIDADE_ALIAS+".tipologiaManejo", TIPOLOGIA_ATIVIDADE_ALIAS);

		return this;
	}

	public ProcessoManejoBuilder addObjetoTramitavelAlias() {

		addAlias("objetoTramitavel", OBJETO_TRAMITAVEL_ALIAS);

		return this;
	}

	public ProcessoManejoBuilder addTipoLicencaAlias() {

		addAlias("tipoLicenca", TIPO_LICENCA_ALIAS);

		return this;
	}

	public ProcessoManejoBuilder filtrarPorNumeroProcesso(String numeroProcesso) {

		if (StringUtils.isNotEmpty(numeroProcesso)) {

			addRestriction(Restrictions.ilike("numeroProcesso", numeroProcesso, MatchMode.ANYWHERE));
		}

		return this;
	}

	public ProcessoManejoBuilder filtrarPorIdMunicipio(Long idMunicipio) {

		if (idMunicipio != null) {

			addMunicipioEmpreendimentoAlias();
			addRestriction(Restrictions.eq(MUNICIPIO_EMPREENDIMENTO_ALIAS+".id", idMunicipio));
		}

		return this;
	}

	public ProcessoManejoBuilder filtrarPorCpfCnpjEmpreendimento(String cpfCnpj) {

		if (StringUtils.isNotEmpty(cpfCnpj)) {

			addEmpreendimentoAlias();

			criteria.add(
					Restrictions.ilike(EMPREENDIMENTO_ALIAS+".cpfCnpj", cpfCnpj, MatchMode.START)
			);
		}

		return this;
	}

	public ProcessoManejoBuilder filtrarPorIdTipologia(Long idTipologia) {

		if (idTipologia != null) {

			addTipologiaAtividadeAlias();
			addRestriction(Restrictions.eq(TIPOLOGIA_ATIVIDADE_ALIAS+".id", idTipologia));
		}

		return this;
	}

	public ProcessoManejoBuilder filtrarPorIdAtividade(Long idAtividade) {

		if (idAtividade != null) {

			addAtividadeAlias();
			addRestriction(Restrictions.eq(ATIVIDADE_ALIAS+".id", idAtividade));
		}

		return this;
	}

	public ProcessoManejoBuilder filtrarPorIdCondicao(Long idCondicao) {

		if (idCondicao != null) {

			addObjetoTramitavelAlias();
			addRestriction(Restrictions.eq(OBJETO_TRAMITAVEL_ALIAS+".condicao.idCondicao", idCondicao));
		}

		return this;
	}

	public ProcessoManejoBuilder filtrarPorIdTipoLicenca(Long idCondicao) {

		if (idCondicao != null) {

			addTipoLicencaAlias();
			addRestriction(Restrictions.eq(TIPO_LICENCA_ALIAS+".id", idCondicao));
		}

		return this;
	}

	public ProcessoManejoBuilder groupByIdProcesso(){

		addProjection(Projections.groupProperty("id").as("id"));

		return this;
	}

	public ProcessoManejoBuilder groupByNumeroProcesso(){

		addProjection(Projections.groupProperty("numeroProcesso").as("numeroProcesso"));

		return this;
	}

	public ProcessoManejoBuilder groupByCpfCnpjEmpreendimento(){

		addEmpreendimentoAlias();

		addProjection(Projections.groupProperty(EMPREENDIMENTO_ALIAS+".cpfCnpj").as("cpfCnpjEmpreendimento"));

		return this;
	}

	public ProcessoManejoBuilder groupByDenominacaoEmpreendimento(){

		addEmpreendimentoAlias();

		addProjection(Projections.groupProperty(EMPREENDIMENTO_ALIAS+".denominacao").as("denominacaoEmpreendimento"));

		return this;
	}

	public ProcessoManejoBuilder groupByMunicipioEmpreendimento(){

		addMunicipioEmpreendimentoAlias();
		addProjection(Projections.groupProperty(MUNICIPIO_EMPREENDIMENTO_ALIAS+".nome").as("municipioEmpreendimento"));

		addEstadoEmpreendimentoAlias();
		addProjection(Projections.groupProperty(ESTADO_EMPREENDIMENTO_ALIAS+".id").as("siglaEstadoEmpreendimento"));

		return this;
	}

	public ProcessoManejoBuilder groupByTipoLicencaManejo(){

		addTipoLicencaAlias();

		addProjection(Projections.groupProperty(TIPO_LICENCA_ALIAS+".codigo").as("tipoLicencaManejo"));

		return this;
	}

	public ProcessoManejoBuilder groupByCondicao() {

		addObjetoTramitavelAlias();

		addProjection(Projections.groupProperty(OBJETO_TRAMITAVEL_ALIAS+".condicao.idCondicao").as("idCondicao"));

		return this;
	}

	public ProcessoManejoBuilder groupByRevisaoSolicitada() {

		addProjection(Projections.groupProperty("revisaoSolicitada").as("revisaoSolicitada"));

		return this;
	}

	public ProcessoManejoBuilder count() {

		addProjection(Projections.countDistinct("id").as("total"));

		return this;
	}
}
