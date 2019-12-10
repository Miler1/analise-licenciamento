package models.licenciamento;

import exceptions.ValidacaoException;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Mensagem;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "licenciamento", name = "questionario_3")
public class Questionario3 extends GenericModel {

	private static final String SEQ = "licenciamento.questionario_3_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@Required
	@OneToOne
	@JoinColumn(name="id_caracterizacao", referencedColumnName = "id")
	public Caracterizacao caracterizacao;

	@Required
	@Column(name="data_cadastro")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataCadastro;

	@Required
	@Column(name="ca")
	public Boolean consumoAgua;

	@Column(name="ca_asub")
	public Boolean consumoAguaAguaSubterranea;

	@Column(name="ca_asub_consumo_medio")
	public Double consumoAguaAguaSubterraneaConsumoMedio;

	@Column(name="ca_asub_uso_domestico")
	public Boolean consumoAguaAguaSubterraneaUsoDomestico;

	@Column(name="ca_asub_uso_industrial")
	public Boolean consumoAguaAguaSubterraneaUsoIndustrial;

	@Column(name="ca_asub_vazao")
	public Double consumoAguaAguaSubterraneaVazao;

	@Column(name="ca_asup")
	public Boolean consumoAguaAguaSuperficial;

	@Column(name="ca_asup_consumo_medio")
	public Double consumoAguaAguaSuperficialConsumoMedio;

	@Column(name="ca_asup_uso_domestico")
	public Boolean consumoAguaAguaSuperficialUsoDomestico;

	@Column(name="ca_asup_uso_industrial")
	public Boolean consumoAguaAguaSuperficialUsoIndustrial;

	@Column(name="ca_asup_vazao")
	public Double consumoAguaAguaSuperficialVazao;

	@Column(name="ca_rp")
	public Boolean consumoAguaRedePublica;

	@Column(name="ca_rp_consumo_medio")
	public Double consumoAguaRedePublicaConsumoMedio;

	@Column(name="ca_rp_uso_domestico")
	public Boolean consumoAguaRedePublicaUsoDomestico;

	@Column(name="ca_rp_uso_industrial")
	public Boolean consumoAguaRedePublicaUsoIndustrial;

	@Column(name="ca_rp_vazao")
	public Double consumoAguaRedePublicaVazao;

	@Required
	@Column(name="ef")
	public Boolean efluentes;

	@Column(name="ef_dom")
	public Boolean efluentesDomestico;

	@Column(name="ef_dom_destino_final_fossa_filtro")
	public Boolean efluentesDomesticoDestinoFinalFossaFiltro;

	@Column(name="ef_dom_destino_final_fossa_septica")
	public Boolean efluentesDomesticoDestinoFinalFossaSeptica;

	@Column(name="ef_dom_destino_final_outros")
	public Boolean efluentesDomesticoDestinoFinalOutros;

	@Column(name="ef_dom_destino_final_outros_espec")
	public String efluentesDomesticoDestinoFinalOutrosEspecificar;

	@Column(name="ef_dom_destino_final_remocao_oleo_graxa")
	public Boolean efluentesDomesticoDestinoFinalRemocaoOleoGraxa;

	@Column(name="ef_dom_regime_carga")
	public String efluentesDomesticoRegimeCarga;

	@Column(name="ef_dom_tipo_tratamento_ete")
	public Boolean efluentesDomesticoTipoTratamentoEte;

	@Column(name="ef_dom_tipo_tratamento_fossa_filtro")
	public Boolean efluentesDomesticoTipoTratamentoFossaFiltro;

	@Column(name="ef_dom_tipo_tratamento_fossa_septica")
	public Boolean efluentesDomesticoTipoTratamentoFossaSeptica;

	@Column(name="ef_dom_tipo_tratamento_outros")
	public Boolean efluentesDomesticoTipoTratamentoOutros;

	@Column(name="ef_dom_tipo_tratamento_outros_espec")
	public String efluentesDomesticoTipoTratamentoOutrosEspecificar;

	@Column(name="ef_dom_tipo_tratamento_remocao_oleo_graxa")
	public Boolean efluentesDomesticoTipoTratamentoRemocaoOleoGraxa;

	@Column(name="ef_dom_vazao_media")
	public Double efluentesDomesticoVazaoMedia;

	@Column(name="ef_ind")
	public Boolean efluentesIndustrial;

	@Column(name="ef_ind_destino_final_fossa_filtro")
	public Boolean efluentesIndustrialDestinoFinalFossaFiltro;

	@Column(name="ef_ind_destino_final_fossa_septica")
	public Boolean efluentesIndustrialDestinoFinalFossaSeptica;

	@Column(name="ef_ind_destino_final_outros")
	public Boolean efluentesIndustrialDestinoFinalOutros;

	@Column(name="ef_ind_destino_final_outros_espec")
	public String efluentesIndustrialDestinoFinalOutrosEspecificar;

	@Column(name="ef_ind_destino_final_remocao_oleo_graxa")
	public Boolean efluentesIndustrialDestinoFinalRemocaoOleoGraxa;

	@Column(name="ef_ind_regime_carga")
	public String efluentesIndustrialRegimeCarga;

	@Column(name="ef_ind_tipo_tratamento_ete")
	public Boolean efluentesIndustrialTipoTratamentoEte;

	@Column(name="ef_ind_tipo_tratamento_fossa_filtro")
	public Boolean efluentesIndustrialTipoTratamentoFossaFiltro;

	@Column(name="ef_ind_tipo_tratamento_fossa_septica")
	public Boolean efluentesIndustrialTipoTratamentoFossaSeptica;

	@Column(name="ef_ind_tipo_tratamento_outros")
	public Boolean efluentesIndustrialTipoTratamentoOutros;

	@Column(name="ef_ind_tipo_tratamento_outros_espec")
	public String efluentesIndustrialTipoTratamentoOutrosEspecificar;

	@Column(name="ef_ind_tipo_tratamento_remocao_oleo_graxa")
	public Boolean efluentesIndustrialTipoTratamentoRemocaoOleoGraxa;

	@Column(name="ef_ind_vazao_media")
	public Double efluentesIndustrialVazaoMedia;

	@Required
	@Column(name="ne")
	public Double numeroEmpregados;

	@Required
	@Column(name="rs")
	public Boolean residuosSolidos;

	@Column(name="rs_dom")
	public Boolean residuosSolidosDomestico;

	@Column(name="rs_dom_quantidade_media")
	public Double residuosSolidosDomesticoQuantidadeMedia;

	@Column(name="rs_dom_tipo_coleta_propria")
	public Boolean residuosSolidosDomesticoTipoColetaPropria;

	@Column(name="rs_dom_tipo_coleta_publica")
	public Boolean residuosSolidosDomesticoTipoColetaPublica;

	@Column(name="rs_dom_tipo_coleta_terceiros")
	public Boolean residuosSolidosDomesticoTipoColetaTerceiros;

	@Column(name="rs_dom_tipo_coleta_terceiros_espec")
	public String residuosSolidosDomesticoTipoColetaTerceirosEspecificar;

	@Column(name="rs_dom_tratamento_disposicao_aca")
	public Boolean residuosSolidosDomesticoTratamentoDisposicaoAterroCeuAberto;

	@Column(name="rs_dom_tratamento_disposicao_as")
	public Boolean residuosSolidosDomesticoTratamentoDisposicaoAterroSanitario;

	@Column(name="rs_dom_tratamento_disposicao_compostagem")
	public Boolean residuosSolidosDomesticoTratamentoDisposicaoCompostagem;

	@Column(name="rs_dom_tratamento_disposicao_inc")
	public Boolean residuosSolidosDomesticoTratamentoDisposicaoIncineracao;

	@Column(name="rs_dom_tratamento_disposicao_outros")
	public Boolean residuosSolidosDomesticoTratamentoDisposicaoOutros;

	@Column(name="rs_dom_tratamento_disposicao_outros_espec")
	public String residuosSolidosDomesticoTratamentoDisposicaoOutrosEspecificar;

	@Column(name="rs_dom_tratamento_disposicao_reciclagem")
	public Boolean residuosSolidosDomesticoTratamentoDisposicaoReciclagem;

	@Column(name="rs_esc")
	public Boolean residuosSolidosEscritorio;

	@Column(name="rs_esc_quantidade_media")
	public Double residuosSolidosEscritorioQuantidadeMedia;

	@Column(name="rs_esc_tipo_coleta_propria")
	public Boolean residuosSolidosEscritorioTipoColetaPropria;

	@Column(name="rs_esc_tipo_coleta_publica")
	public Boolean residuosSolidosEscritorioTipoColetaPublica;

	@Column(name="rs_esc_tipo_coleta_terceiros")
	public Boolean residuosSolidosEscritorioTipoColetaTerceiros;

	@Column(name="rs_esc_tipo_coleta_terceiros_espec")
	public String residuosSolidosEscritorioTipoColetaTerceirosEspecificar;

	@Column(name="rs_esc_tratamento_disposicao_aca")
	public Boolean residuosSolidosEscritorioTratamentoDisposicaoAterroCeuAberto;

	@Column(name="rs_esc_tratamento_disposicao_as")
	public Boolean residuosSolidosEscritorioTratamentoDisposicaoAterroSanitario;

	@Column(name="rs_esc_tratamento_disposicao_inc")
	public Boolean residuosSolidosEscritorioTratamentoDisposicaoIncineracao;

	@Column(name="rs_esc_tratamento_disposicao_outros")
	public Boolean residuosSolidosEscritorioTratamentoDisposicaoOutros;

	@Column(name="rs_esc_tratamento_disposicao_outros_espec")
	public String residuosSolidosEscritorioTratamentoDisposicaoOutrosEspecificar;

	@Column(name="rs_esc_tratamento_disposicao_reciclagem")
	public Boolean residuosSolidosEscritorioTratamentoDisposicaoReciclagem;

	@Column(name="rs_indperig")
	public Boolean residuosSolidosIndustrialPerigosos;

	@Column(name="rs_indperig_quantidade_media")
	public Double residuosSolidosIndustrialPerigososQuantidadeMedia;

	@Column(name="rs_indperig_tipo_coleta_propria")
	public Boolean residuosSolidosIndustrialPerigososTipoColetaPropria;

	@Column(name="rs_indperig_tipo_coleta_publica")
	public Boolean residuosSolidosIndustrialPerigososTipoColetaPublica;

	@Column(name="rs_indperig_tipo_coleta_terceiros")
	public Boolean residuosSolidosIndustrialPerigososTipoColetaTerceiros;

	@Column(name="rs_indperig_tipo_coleta_terceiros_espec")
	public String residuosSolidosIndustrialPerigososTipoColetaTerceirosEspecificar;

	@Column(name="rs_indperig_tratamento_disposicao_aca")
	public Boolean residuosSolidosIndustrialPerigososTratamentoDisposicaoAterroCeuAberto;

	@Column(name="rs_indperig_tratamento_disposicao_aterro_industrial")
	public Boolean residuosSolidosIndustrialPerigososTratamentoDisposicaoAterroIndustrial;

	@Column(name="rs_indperig_tratamento_disposicao_as")
	public Boolean residuosSolidosIndustrialPerigososTratamentoDisposicaoAterroSanitario;

	@Column(name="rs_indperig_tratamento_disposicao_inc")
	public Boolean residuosSolidosIndustrialPerigososTratamentoDisposicaoIncineracao;

	@Column(name="rs_indperig_tratamento_disposicao_outros")
	public Boolean residuosSolidosIndustrialPerigososTratamentoDisposicaoOutros;

	@Column(name="rs_indperig_tratamento_disposicao_outros_espec")
	public String residuosSolidosIndustrialPerigososTratamentoDisposicaoOutrosEspecificar;

	@Column(name="rs_indperig_tratamento_disposicao_reciclagem")
	public Boolean residuosSolidosIndustrialPerigososTratamentoDisposicaoReciclagem;

	@Column(name="rs_ind")
	public Boolean residuosSolidosIndustrial;

	@Column(name="rs_ind_quantidade_media")
	public Double residuosSolidosIndustrialQuantidadeMedia;

	@Column(name="rs_ind_tipo_coleta_propria")
	public Boolean residuosSolidosIndustrialTipoColetaPropria;

	@Column(name="rs_ind_tipo_coleta_publica")
	public Boolean residuosSolidosIndustrialTipoColetaPublica;

	@Column(name="rs_ind_tipo_coleta_terceiros")
	public Boolean residuosSolidosIndustrialTipoColetaTerceiros;

	@Column(name="rs_ind_tipo_coleta_terceiros_espec")
	public String residuosSolidosIndustrialTipoColetaTerceirosEspecificar;

	@Column(name="rs_ind_tratamento_disposicao_aca")
	public Boolean residuosSolidosIndustrialTratamentoDisposicaoAterroCeuAberto;

	@Column(name="rs_ind_tratamento_disposicao_aterro_industrial")
	public Boolean residuosSolidosIndustrialTratamentoDisposicaoAterroIndustrial;

	@Column(name="rs_ind_tratamento_disposicao_as")
	public Boolean residuosSolidosIndustrialTratamentoDisposicaoAterroSanitario;

	@Column(name="rs_ind_tratamento_disposicao_inc")
	public Boolean residuosSolidosIndustrialTratamentoDisposicaoIncineracao;

	@Column(name="rs_ind_tratamento_disposicao_outros")
	public Boolean residuosSolidosIndustrialTratamentoDisposicaoOutros;

	@Column(name="rs_ind_tratamento_disposicao_outros_espec")
	public String residuosSolidosIndustrialTratamentoDisposicaoOutrosEspecificar;

	@Column(name="rs_ind_tratamento_disposicao_reaproveitamento_processo")
	public Boolean residuosSolidosIndustrialTratamentoDisposicaoReaproveitamentoProcesso;

	@Column(name="rs_ind_tratamento_disposicao_reciclagem")
	public Boolean residuosSolidosIndustrialTratamentoDisposicaoReciclagem;

	@Column(name="rs_outros")
	public Boolean residuosSolidosOutros;

	@Column(name="rs_outros_quantidade_media")
	public Double residuosSolidosOutrosQuantidadeMedia;

	@Column(name="rs_outros_tipo_coleta_propria")
	public Boolean residuosSolidosOutrosTipoColetaPropria;

	@Column(name="rs_outros_tipo_coleta_publica")
	public Boolean residuosSolidosOutrosTipoColetaPublica;

	@Column(name="rs_outros_tipo_coleta_terceiros")
	public Boolean residuosSolidosOutrosTipoColetaTerceiros;

	@Column(name="rs_outros_tipo_coleta_terceiros_espec")
	public String residuosSolidosOutrosTipoColetaTerceirosEspecificar;

	@Column(name="rs_outros_tratamento_disposicao_aca")
	public Boolean residuosSolidosOutrosTratamentoDisposicaoAterroCeuAberto;

	@Column(name="rs_outros_tratamento_disposicao_as")
	public Boolean residuosSolidosOutrosTratamentoDisposicaoAterroSanitario;

	@Column(name="rs_outros_tratamento_disposicao_inc")
	public Boolean residuosSolidosOutrosTratamentoDisposicaoIncineracao;

	@Column(name="rs_outros_tratamento_disposicao_outros")
	public Boolean residuosSolidosOutrosTratamentoDisposicaoOutros;

	@Column(name="rs_outros_tratamento_disposicao_outros_espec")
	public String residuosSolidosOutrosTratamentoDisposicaoOutrosEspecificar;

	@Column(name="rs_outros_origem")
	public String residuosSolidosOutrosOrigem;


	private void setarAtributos(Caracterizacao caracterizacao) {
		this.caracterizacao = caracterizacao;
		this.dataCadastro = new Date();
	}

	public void validar(Caracterizacao caracterizacao) {

		if(caracterizacao == null || caracterizacao.questionario3 == null) {
			throw new ValidacaoException(Mensagem.CARACTERIZACAO_PERGUNTAS_NAO_RESPONDIDAS);
		}

		if(!
				(
						verificarRespostaNumeroEmpregados()
								&& verificarRespostaConsumoAgua()
								&& verificarRespostaEfluentes()
								&& verificarRespostaResiduosSolidos()
				)
		) {
			throw new ValidacaoException(Mensagem.CARACTERIZACAO_PERGUNTAS_NAO_RESPONDIDAS);
		}

		this.setarAtributos(caracterizacao);
	}

	private Boolean isTrue(Boolean value) {
		return (value != null && value == true);
	}

	private Boolean isFalseOrNull(Boolean value) {
		return (value == null || value == false);
	}

	private Boolean isNumber(Double value) {
		return value != null;
	}

	private Boolean exist(Object value) {
		return value != null;
	}

	private Boolean textoNaoVazio(String value) {
		return value != null && value.length() > 0;
	}

	private Boolean verificarRespostaNumeroEmpregados() {

		if(isNumber(this.numeroEmpregados)){
			return true;
		}

		return false;
	}

	private Boolean verificarRespostaConsumoAgua() {

		if(isFalseOrNull(this.consumoAgua)) {
			return true;
		}
		else if(isTrue(this.consumoAgua)) {
			return verificarRespostaConsumoAguaTrue();
		}

		return false;
	}

	private Boolean verificarRespostaConsumoAguaTrue() {

		if(!
				(
						isTrue(this.consumoAguaRedePublica) ||
								isTrue(this.consumoAguaAguaSubterranea) ||
								isTrue(this.consumoAguaAguaSuperficial)
				)
		) {
			return false;
		}

		return verificarRespostaConsumoAguaRedePublica() &&
				verificarRespostaConsumoAguaAguaSubterranea() &&
				verificarRespostaConsumoAguaAguaSuperficial();
	}

	private Boolean verificarRespostaConsumoAguaRedePublica() {

		if(isTrue(this.consumoAguaRedePublica)) {
			if(isNumber(this.consumoAguaRedePublicaConsumoMedio) &&
					(
							isTrue(this.consumoAguaRedePublicaUsoDomestico) ||
									isTrue(this.consumoAguaRedePublicaUsoIndustrial)
					) &&
					isNumber(this.consumoAguaRedePublicaVazao)
			) {
				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	private Boolean verificarRespostaConsumoAguaAguaSubterranea() {

		if(isTrue(this.consumoAguaAguaSubterranea)) {
			if(isNumber(this.consumoAguaAguaSubterraneaConsumoMedio) &&
					(
							isTrue(this.consumoAguaAguaSubterraneaUsoDomestico) ||
									isTrue(this.consumoAguaAguaSubterraneaUsoIndustrial)
					) &&
					isNumber(this.consumoAguaAguaSubterraneaVazao)
			) {
				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	private Boolean verificarRespostaConsumoAguaAguaSuperficial() {

		if(isTrue(this.consumoAguaAguaSuperficial)) {
			if(isNumber(this.consumoAguaAguaSuperficialConsumoMedio) &&
					(
							isTrue(this.consumoAguaAguaSuperficialUsoDomestico) ||
									isTrue(this.consumoAguaAguaSuperficialUsoIndustrial)
					) &&
					isNumber(this.consumoAguaAguaSuperficialVazao)
			) {
				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	private Boolean verificarRespostaEfluentes() {

		if(isFalseOrNull(this.efluentes)) {
			return true;
		}
		else if(isTrue(this.efluentes)) {
			return verificarRespostaEfluentesTrue();
		}

		return false;
	}

	private Boolean verificarRespostaEfluentesTrue() {

		if(!
				(
						isTrue(this.efluentesDomestico) ||
								isTrue(this.efluentesIndustrial)
				)
		) {
			return false;
		}

		return verificarRespostaEfluentesDomestico() &&
				verificarRespostaEfluentesIndustrial();
	}

	private Boolean verificarRespostaEfluentesDomestico() {

		if(isTrue(this.efluentesDomestico)) {
			if(isNumber(this.efluentesDomesticoVazaoMedia) &&
					exist(this.efluentesDomesticoRegimeCarga) &&
					(
							isTrue(this.efluentesDomesticoTipoTratamentoEte) ||
									isTrue(this.efluentesDomesticoTipoTratamentoFossaSeptica) ||
									isTrue(this.efluentesDomesticoTipoTratamentoFossaFiltro) ||
									isTrue(this.efluentesDomesticoTipoTratamentoRemocaoOleoGraxa) ||
									(
											isTrue(this.efluentesDomesticoTipoTratamentoOutros) &&
													textoNaoVazio(this.efluentesDomesticoTipoTratamentoOutrosEspecificar)
									)
					) &&
					(
							isTrue(this.efluentesDomesticoDestinoFinalFossaSeptica) ||
									isTrue(this.efluentesDomesticoDestinoFinalFossaFiltro) ||
									isTrue(this.efluentesDomesticoDestinoFinalRemocaoOleoGraxa) ||
									(
											isTrue(this.efluentesDomesticoDestinoFinalOutros) &&
													textoNaoVazio(this.efluentesDomesticoDestinoFinalOutrosEspecificar)
									)
					)
			) {
				if(isTrue(this.efluentesDomesticoTipoTratamentoOutros)) {
					if(!textoNaoVazio(this.efluentesDomesticoTipoTratamentoOutrosEspecificar)) {
						return false;
					}
				}
				if(isTrue(this.efluentesDomesticoDestinoFinalOutros)) {
					if(!textoNaoVazio(this.efluentesDomesticoDestinoFinalOutrosEspecificar)) {
						return false;
					}
				}
				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	private Boolean verificarRespostaEfluentesIndustrial() {

		if(isTrue(this.efluentesIndustrial)) {
			if(isNumber(this.efluentesIndustrialVazaoMedia) &&
					exist(this.efluentesIndustrialRegimeCarga) &&
					(
							isTrue(this.efluentesIndustrialTipoTratamentoEte) ||
									isTrue(this.efluentesIndustrialTipoTratamentoFossaSeptica) ||
									isTrue(this.efluentesIndustrialTipoTratamentoFossaFiltro) ||
									isTrue(this.efluentesIndustrialTipoTratamentoRemocaoOleoGraxa) ||
									(
											isTrue(this.efluentesIndustrialTipoTratamentoOutros) &&
													textoNaoVazio(this.efluentesIndustrialTipoTratamentoOutrosEspecificar)
									)
					) &&
					(
							isTrue(this.efluentesIndustrialDestinoFinalFossaSeptica) ||
									isTrue(this.efluentesIndustrialDestinoFinalFossaFiltro) ||
									isTrue(this.efluentesIndustrialDestinoFinalRemocaoOleoGraxa) ||
									(
											isTrue(this.efluentesIndustrialDestinoFinalOutros) &&
													textoNaoVazio(this.efluentesIndustrialDestinoFinalOutrosEspecificar)
									)
					)
			) {
				if(isTrue(this.efluentesIndustrialTipoTratamentoOutros)) {
					if(!textoNaoVazio(this.efluentesIndustrialTipoTratamentoOutrosEspecificar)) {
						return false;
					}
				}
				if(isTrue(this.efluentesIndustrialDestinoFinalOutros)) {
					if(!textoNaoVazio(this.efluentesIndustrialDestinoFinalOutrosEspecificar)) {
						return false;
					}
				}
				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	private Boolean verificarRespostaResiduosSolidos() {

		if(isFalseOrNull(this.residuosSolidos)) {
			return true;
		}
		else if(isTrue(this.residuosSolidos)) {
			return verificarRespostaResiduosSolidosTrue();
		}

		return false;
	}

	private Boolean verificarRespostaResiduosSolidosTrue() {

		if(!
				(
						isTrue(this.residuosSolidosDomestico) ||
								isTrue(this.residuosSolidosEscritorio) ||
								isTrue(this.residuosSolidosIndustrial) ||
								isTrue(this.residuosSolidosIndustrialPerigosos) ||
								isTrue(this.residuosSolidosOutros)
				)
		) {
			return false;
		}

		return verificarRespostaResiduosSolidosDomestico() &&
				verificarRespostaResiduosSolidosEscritorio() &&
				verificarRespostaResiduosSolidosIndustrial() &&
				verificarRespostaResiduosSolidosIndustrialPerigosos() &&
				verificarRespostaResiduosSolidosOutros();
	}

	private Boolean verificarRespostaResiduosSolidosDomestico() {

		if(isTrue(this.residuosSolidosDomestico)) {
			if(isNumber(this.residuosSolidosDomesticoQuantidadeMedia) &&
					(
							isTrue(this.residuosSolidosDomesticoTratamentoDisposicaoAterroSanitario) ||
									isTrue(this.residuosSolidosDomesticoTratamentoDisposicaoAterroCeuAberto) ||
									isTrue(this.residuosSolidosDomesticoTratamentoDisposicaoIncineracao) ||
									isTrue(this.residuosSolidosDomesticoTratamentoDisposicaoReciclagem) ||
									isTrue(this.residuosSolidosDomesticoTratamentoDisposicaoCompostagem) ||
									(
											isTrue(this.residuosSolidosDomesticoTratamentoDisposicaoOutros) &&
													textoNaoVazio(this.residuosSolidosDomesticoTratamentoDisposicaoOutrosEspecificar)
									)
					) &&
					(
							isTrue(this.residuosSolidosDomesticoTipoColetaPublica) ||
									isTrue(this.residuosSolidosDomesticoTipoColetaPropria) ||
									(
											isTrue(this.residuosSolidosDomesticoTipoColetaTerceiros) &&
													textoNaoVazio(this.residuosSolidosDomesticoTipoColetaTerceirosEspecificar)
									)
					)
			) {
				if(isTrue(this.residuosSolidosDomesticoTratamentoDisposicaoOutros)) {
					if(!textoNaoVazio(this.residuosSolidosDomesticoTratamentoDisposicaoOutrosEspecificar)) {
						return false;
					}
				}
				if(isTrue(this.residuosSolidosDomesticoTipoColetaTerceiros)) {
					if(!textoNaoVazio(this.residuosSolidosDomesticoTipoColetaTerceirosEspecificar)) {
						return false;
					}
				}
				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	private Boolean verificarRespostaResiduosSolidosEscritorio() {

		if(isTrue(this.residuosSolidosEscritorio)) {
			if(isNumber(this.residuosSolidosEscritorioQuantidadeMedia) &&
					(
							isTrue(this.residuosSolidosEscritorioTratamentoDisposicaoAterroSanitario) ||
									isTrue(this.residuosSolidosEscritorioTratamentoDisposicaoAterroCeuAberto) ||
									isTrue(this.residuosSolidosEscritorioTratamentoDisposicaoIncineracao) ||
									isTrue(this.residuosSolidosEscritorioTratamentoDisposicaoReciclagem) ||
									(
											isTrue(this.residuosSolidosEscritorioTratamentoDisposicaoOutros) &&
													textoNaoVazio(this.residuosSolidosEscritorioTratamentoDisposicaoOutrosEspecificar)
									)
					) &&
					(
							isTrue(this.residuosSolidosEscritorioTipoColetaPublica) ||
									isTrue(this.residuosSolidosEscritorioTipoColetaPropria) ||
									(
											isTrue(this.residuosSolidosEscritorioTipoColetaTerceiros) &&
													textoNaoVazio(this.residuosSolidosEscritorioTipoColetaTerceirosEspecificar)
									)
					)
			) {
				if(isTrue(this.residuosSolidosEscritorioTratamentoDisposicaoOutros)) {
					if(!textoNaoVazio(this.residuosSolidosEscritorioTratamentoDisposicaoOutrosEspecificar)) {
						return false;
					}
				}
				if(isTrue(this.residuosSolidosEscritorioTipoColetaTerceiros)) {
					if(!textoNaoVazio(this.residuosSolidosEscritorioTipoColetaTerceirosEspecificar)) {
						return false;
					}
				}
				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	private Boolean verificarRespostaResiduosSolidosIndustrial() {

		if(isTrue(this.residuosSolidosIndustrial)) {
			if(isNumber(this.residuosSolidosIndustrialQuantidadeMedia) &&
					(
							isTrue(this.residuosSolidosIndustrialTratamentoDisposicaoAterroSanitario) ||
									isTrue(this.residuosSolidosIndustrialTratamentoDisposicaoAterroCeuAberto) ||
									isTrue(this.residuosSolidosIndustrialTratamentoDisposicaoIncineracao) ||
									isTrue(this.residuosSolidosIndustrialTratamentoDisposicaoReciclagem) ||
									isTrue(this.residuosSolidosIndustrialTratamentoDisposicaoAterroIndustrial) ||
									isTrue(this.residuosSolidosIndustrialTratamentoDisposicaoReaproveitamentoProcesso) ||
									(
											isTrue(this.residuosSolidosIndustrialTratamentoDisposicaoOutros) &&
													textoNaoVazio(this.residuosSolidosIndustrialTratamentoDisposicaoOutrosEspecificar)
									)
					) &&
					(
							isTrue(this.residuosSolidosIndustrialTipoColetaPublica) ||
									isTrue(this.residuosSolidosIndustrialTipoColetaPropria) ||
									(
											isTrue(this.residuosSolidosIndustrialTipoColetaTerceiros) &&
													textoNaoVazio(this.residuosSolidosIndustrialTipoColetaTerceirosEspecificar)
									)
					)
			) {
				if(isTrue(this.residuosSolidosIndustrialTratamentoDisposicaoOutros)) {
					if(!textoNaoVazio(this.residuosSolidosIndustrialTratamentoDisposicaoOutrosEspecificar)) {
						return false;
					}
				}
				if(isTrue(this.residuosSolidosIndustrialTipoColetaTerceiros)) {
					if(!textoNaoVazio(this.residuosSolidosIndustrialTipoColetaTerceirosEspecificar)) {
						return false;
					}
				}
				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	private Boolean verificarRespostaResiduosSolidosIndustrialPerigosos() {

		if(isTrue(this.residuosSolidosIndustrialPerigosos)) {
			if(isNumber(this.residuosSolidosIndustrialPerigososQuantidadeMedia) &&
					(
							isTrue(this.residuosSolidosIndustrialPerigososTratamentoDisposicaoAterroSanitario) ||
									isTrue(this.residuosSolidosIndustrialPerigososTratamentoDisposicaoAterroCeuAberto) ||
									isTrue(this.residuosSolidosIndustrialPerigososTratamentoDisposicaoIncineracao) ||
									isTrue(this.residuosSolidosIndustrialPerigososTratamentoDisposicaoReciclagem) ||
									isTrue(this.residuosSolidosIndustrialPerigososTratamentoDisposicaoAterroIndustrial) ||
									(
											isTrue(this.residuosSolidosIndustrialPerigososTratamentoDisposicaoOutros) &&
													textoNaoVazio(this.residuosSolidosIndustrialPerigososTratamentoDisposicaoOutrosEspecificar)
									)
					) &&
					(
							isTrue(this.residuosSolidosIndustrialPerigososTipoColetaPublica) ||
									isTrue(this.residuosSolidosIndustrialPerigososTipoColetaPropria) ||
									(
											isTrue(this.residuosSolidosIndustrialPerigososTipoColetaTerceiros) &&
													textoNaoVazio(this.residuosSolidosIndustrialPerigososTipoColetaTerceirosEspecificar)
									)
					)
			) {
				if(isTrue(this.residuosSolidosIndustrialPerigososTratamentoDisposicaoOutros)) {
					if(!textoNaoVazio(this.residuosSolidosIndustrialPerigososTratamentoDisposicaoOutrosEspecificar)) {
						return false;
					}
				}
				if(isTrue(this.residuosSolidosIndustrialPerigososTipoColetaTerceiros)) {
					if(!textoNaoVazio(this.residuosSolidosIndustrialPerigososTipoColetaTerceirosEspecificar)) {
						return false;
					}
				}
				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	private Boolean verificarRespostaResiduosSolidosOutros() {

		if(isTrue(this.residuosSolidosOutros)) {
			if(textoNaoVazio(this.residuosSolidosOutrosOrigem) &&
					isNumber(this.residuosSolidosOutrosQuantidadeMedia) &&
					(
							isTrue(this.residuosSolidosOutrosTratamentoDisposicaoAterroSanitario) ||
									isTrue(this.residuosSolidosOutrosTratamentoDisposicaoAterroCeuAberto) ||
									isTrue(this.residuosSolidosOutrosTratamentoDisposicaoIncineracao) ||
									(
											isTrue(this.residuosSolidosOutrosTratamentoDisposicaoOutros) &&
													textoNaoVazio(this.residuosSolidosOutrosTratamentoDisposicaoOutrosEspecificar)
									)
					) &&
					(
							isTrue(this.residuosSolidosOutrosTipoColetaPublica) ||
									isTrue(this.residuosSolidosOutrosTipoColetaPropria) ||
									(
											isTrue(this.residuosSolidosOutrosTipoColetaTerceiros) &&
													textoNaoVazio(this.residuosSolidosOutrosTipoColetaTerceirosEspecificar)
									)
					)
			) {
				if(isTrue(this.residuosSolidosOutrosTratamentoDisposicaoOutros)) {
					if(!textoNaoVazio(this.residuosSolidosOutrosTratamentoDisposicaoOutrosEspecificar)) {
						return false;
					}
				}
				if(isTrue(this.residuosSolidosOutrosTipoColetaTerceiros)) {
					if(!textoNaoVazio(this.residuosSolidosOutrosTipoColetaTerceirosEspecificar)) {
						return false;
					}
				}
				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}
}
