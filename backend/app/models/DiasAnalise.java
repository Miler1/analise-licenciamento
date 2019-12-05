package models;

import models.tramitacao.AcaoTramitacao;
import models.tramitacao.Condicao;
import models.tramitacao.HistoricoTramitacao;
import play.db.jpa.GenericModel;
import utils.DateUtil;
import javax.persistence.*;
import javax.validation.ValidationException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema="analise", name="dia_analise")
public class DiasAnalise extends GenericModel{
	
	public static final String SEQ = "analise.dia_analise_id_seq";
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;	
	
	@OneToOne
	@JoinColumn(name="id_analise")
	public Analise analise;
	
	@Column(name="quantidade_dias_juridica")
	public Integer qtdeDiasJuridica;
	
	@Column(name="quantidade_dias_tecnica")
	public Integer qtdeDiasTecnica;

	@Column(name="quantidade_dias_geo")
	public Integer qtdeDiasGeo;

	@Column(name="quantidade_dias_analise")
	public Integer qtdeDiasAnalise;
	
	@Column(name="quantidade_dias_aprovador")
	public Integer qtdeDiasAprovador;
	
	@Column(name="quantidade_dias_notificacao")
	public Integer qtdeDiasNotificacao;
	
	public DiasAnalise(Analise analise) {

		this.analise = analise;
		this.qtdeDiasAnalise = 0;
		this.qtdeDiasGeo = 0;

	}
	
	public DiasAnalise() {}

	private static Integer verificaUltimaTramitacao(List<HistoricoTramitacao> historicoTramitacao) {

		HistoricoTramitacao ultimaTramitacao = historicoTramitacao.stream()
				.filter(tramitacao -> tramitacao.idAcao.equals(AcaoTramitacao.DEFERIR_ANALISE_GEO_VIA_GERENTE) ||
						tramitacao.idAcao.equals(AcaoTramitacao.INDEFERIR_ANALISE_GEO_VIA_GERENTE) ||
						tramitacao.idAcao.equals(AcaoTramitacao.SOLICITAR_DESVINCULO))
				.max(Comparator.comparing(HistoricoTramitacao::getDataInicial)).orElseThrow(ValidationException::new);
		int dias = 0;

		if(ultimaTramitacao.idAcao.equals(AcaoTramitacao.DEFERIR_ANALISE_GEO_VIA_GERENTE) || ultimaTramitacao.idAcao.equals(AcaoTramitacao.INDEFERIR_ANALISE_GEO_VIA_GERENTE)) {

			final Date ultimoDeferirOuIndeferir = ultimaTramitacao.dataInicial;

			boolean temSolicitacaoDeAjustesFinal = historicoTramitacao.stream()
					.anyMatch(tramitacao -> tramitacao.dataInicial.after(ultimoDeferirOuIndeferir) && tramitacao.idAcao.equals(AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_GERENTE));

			if(!temSolicitacaoDeAjustesFinal) {

				dias += DateUtil.getDiferencaEmDias(ultimoDeferirOuIndeferir, new Date());

			}

		} else if(ultimaTramitacao.idAcao.equals(AcaoTramitacao.SOLICITAR_DESVINCULO) && ultimaTramitacao.idCondicaoFinal.equals(Condicao.AGUARDANDO_ANALISE_GEO)) {

			Processo processo = Processo.find("objetoTramitavel.id", ultimaTramitacao.idObjetoTramitavel).first();
			Desvinculo ultimoDesvinculo = processo.analise.analiseGeo.desvinculos.stream().max(Comparator.comparing(Desvinculo::getDataSolicitacao)).orElseThrow(ValidationException::new);

			if(ultimoDesvinculo.dataResposta == null) {

				dias += DateUtil.getDiferencaEmDias(ultimaTramitacao.dataInicial, new Date());

			}

		}

		return dias;

	}

	private static Integer intervalos(List<HistoricoTramitacao> historicoTramitacao) {

		Date dataParecerAnalistaGeo = null;
		Date dataDesvinculoAnalistaGeo = null;
		int dias = 0;

		boolean temTramitacao = historicoTramitacao.stream()
				.anyMatch(tramitacao -> tramitacao.idAcao.equals(AcaoTramitacao.DEFERIR_ANALISE_GEO_VIA_GERENTE) ||
						tramitacao.idAcao.equals(AcaoTramitacao.INDEFERIR_ANALISE_GEO_VIA_GERENTE) ||
						tramitacao.idAcao.equals(AcaoTramitacao.SOLICITAR_DESVINCULO));

		if(temTramitacao) {

			for (HistoricoTramitacao tramitacao : historicoTramitacao) {

				if (tramitacao.idAcao.equals(AcaoTramitacao.DEFERIR_ANALISE_GEO_VIA_GERENTE) || tramitacao.idAcao.equals(AcaoTramitacao.INDEFERIR_ANALISE_GEO_VIA_GERENTE)) {

					dataParecerAnalistaGeo = tramitacao.dataInicial;

				} else if (tramitacao.idAcao.equals(AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_GERENTE) && dataParecerAnalistaGeo != null) {

					dias += DateUtil.getDiferencaEmDias(dataParecerAnalistaGeo, tramitacao.dataInicial);

				} else if (tramitacao.idAcao.equals(AcaoTramitacao.SOLICITAR_DESVINCULO)) {

					dataDesvinculoAnalistaGeo = tramitacao.dataInicial;

				} else if (tramitacao.idAcao.equals(AcaoTramitacao.NEGAR_SOLICITACAO_DESVINCULO) && dataDesvinculoAnalistaGeo != null) {

					dias += DateUtil.getDiferencaEmDias(dataDesvinculoAnalistaGeo, tramitacao.dataInicial);

				}

			}

			dias += verificaUltimaTramitacao(historicoTramitacao);

		}

		return dias;

	}

	public static Integer intervalosTramitacoesAnaliseGeo(List<HistoricoTramitacao> historicoTramitacao) {

		historicoTramitacao = historicoTramitacao.stream().sorted(Comparator.comparing(HistoricoTramitacao::getDataInicial)).collect(Collectors.toList());

		boolean temParecerNaoAprovado = historicoTramitacao.stream().anyMatch(tramitacao -> tramitacao.idAcao.equals(AcaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO));
		boolean temParecerDesvinculoAprovado = historicoTramitacao.stream().anyMatch(tramitacao -> tramitacao.idAcao.equals(AcaoTramitacao.APROVAR_SOLICITACAO_DESVINCULO));

		if (temParecerNaoAprovado || temParecerDesvinculoAprovado) {

			HistoricoTramitacao primeiraTramitacao = historicoTramitacao.stream().filter(tramitacao -> tramitacao.idAcao.equals(AcaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO) || tramitacao.idAcao.equals(AcaoTramitacao.APROVAR_SOLICITACAO_DESVINCULO)).max(Comparator.comparing(HistoricoTramitacao::getDataInicial)).orElseThrow(ValidationException::new);

			Processo processo = Processo.find("objetoTramitavel.id", primeiraTramitacao.idObjetoTramitavel).first();

			int intervaloAtePrimeiraTramitacao = DateUtil.getDiferencaEmDias(processo.analise.analiseGeo.dataCadastro, primeiraTramitacao.dataInicial);

			List<HistoricoTramitacao> historicoAposPrimeiraTramitacao = historicoTramitacao.stream().filter(tramitacao -> tramitacao.dataInicial.equals(primeiraTramitacao.dataInicial) || tramitacao.dataInicial.after(primeiraTramitacao.dataInicial)).collect(Collectors.toList());

			return intervaloAtePrimeiraTramitacao + intervalos(historicoAposPrimeiraTramitacao);

		} else {

			return intervalos(historicoTramitacao);

		}

	}

	public void preencheDiasAnaliseGeo() {

		List<HistoricoTramitacao> historicoTramitacao = this.analise.processo.getHistoricoTramitacao();

		this.qtdeDiasGeo = DateUtil.getDiferencaEmDias(this.analise.analiseGeo.dataCadastro, new Date()) - intervalosTramitacoesAnaliseGeo(historicoTramitacao);

	}

}
