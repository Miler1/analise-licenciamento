package models;

import exceptions.ValidacaoException;
import models.licenciamento.SobreposicaoCaracterizacaoAtividade;
import models.licenciamento.SobreposicaoCaracterizacaoComplexo;
import models.licenciamento.SobreposicaoCaracterizacaoEmpreendimento;
import models.licenciamento.StatusCaracterizacaoEnum;

import models.tramitacao.AcaoTramitacao;
import models.tramitacao.Condicao;
import models.tramitacao.HistoricoTramitacao;
import org.apache.commons.lang.StringUtils;
import play.db.jpa.GenericModel;
import utils.Mensagem;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static models.licenciamento.Caracterizacao.OrigemSobreposicao.*;
import static models.tramitacao.Condicao.AGUARDANDO_RESPOSTA_COMUNICADO;

@Entity
@Table(schema = "analise", name = "parecer_analista_geo")
public class ParecerAnalistaGeo extends GenericModel {

	public static final String SEQ = "analise.parecer_analista_geo_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	@Column(name = "id")
	public Long id;

	@OneToOne
	@JoinColumn(name = "id_analise_geo")
	public AnaliseGeo analiseGeo;

	@ManyToOne
	@JoinColumn(name = "id_tipo_resultado_analise")
	public TipoResultadoAnalise tipoResultadoAnalise;

	@Column(name = "parecer")
	public String parecer;

	@Column(name = "situacao_fundiaria")
	public String situacaoFundiaria;

	@Column(name = "analise_temporal")
	public String analiseTemporal;

	@Column(name = "conclusao")
	public String conclusao;

	@Column(name = "data_parecer")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataParecer;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_usuario_analista_geo", referencedColumnName = "id")
	public UsuarioAnalise usuario;

	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(schema="analise", name="rel_documento_parecer_analista_geo",
			joinColumns=@JoinColumn(name="id_parecer_analista_geo"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> documentos;

	@Column(name = "id_historico_tramitacao")
	public Long idHistoricoTramitacao;

	public Date getDataParecer() {
		return this.dataParecer;
	}

	private void validarParecer(AnaliseGeo analise) {

		if (StringUtils.isBlank(this.parecer))
			throw new ValidacaoException(Mensagem.ANALISE_PARECER_NAO_PREENCHIDO);
	}

	private void validarTipoResultadoAnalise() {

		if (this.tipoResultadoAnalise == null) {
			throw new ValidacaoException(Mensagem.ANALISE_FINAL_PROCESSO_NAO_PREENCHIDA);
		}

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.DEFERIDO) && this.parecer.equals("")) {
			throw new ValidacaoException(Mensagem.ANALISE_DESPACHO_NAO_PREENCHIDO);
		}

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.INDEFERIDO) && this.parecer.equals("")) {
			throw new ValidacaoException(Mensagem.ANALISE_JUSTIFICATIVA_NAO_PREENCHIDA);
		}

		//TODO PUMA-SQ1 Adicionar validacao para o Tipo Resultado Análise "EMITIR NOTIFICACAO"

	}

	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public void aguardarResposta(UsuarioAnalise usuarioExecutor){

		AnaliseGeo analiseGeoBanco = AnaliseGeo.findById(this.analiseGeo.id);

		analiseGeoBanco.analise.processo.tramitacao.tramitar(analiseGeoBanco.analise.processo, AcaoTramitacao.AGUARDAR_RESPOSTA_COMUNICADO, usuarioExecutor);
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeoBanco.analise.processo.objetoTramitavel.id), usuarioExecutor);
	}


	public void finalizar(UsuarioAnalise usuarioExecutor) throws Exception {

		AnaliseGeo analiseGeoBanco = AnaliseGeo.findById(this.analiseGeo.id);

		analiseGeoBanco.update(this.analiseGeo);
		validarParecer(this.analiseGeo);
		validarTipoResultadoAnalise();

		analiseGeoBanco._save();

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.DEFERIDO)) {

			Gerente gerente = Gerente.distribuicaoAutomaticaGerente(usuarioExecutor.usuarioEntradaUnica.setorSelecionado.sigla, analiseGeoBanco);

			if(analiseGeoBanco.analise.processo.caracterizacao.origemSobreposicao.equals(EMPREENDIMENTO)){

				List<SobreposicaoCaracterizacaoEmpreendimento> sobreposicoesCaracterizacaoEmpreendimento = analiseGeoBanco.analise.processo.caracterizacao.sobreposicoesCaracterizacaoEmpreendimento.stream().distinct()
						.filter(distinctByKey(sobreposicaoCaracterizacaoEmpreendimento -> sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.codigo)).collect(Collectors.toList());

				for (SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento : sobreposicoesCaracterizacaoEmpreendimento) {

					if (sobreposicaoCaracterizacaoEmpreendimento != null){

						analiseGeoBanco.enviarEmailComunicado(analiseGeoBanco.analise.processo.caracterizacao, this, sobreposicaoCaracterizacaoEmpreendimento);
					}

				}
				this.aguardarResposta(usuarioExecutor);
				analiseGeoBanco.analise.processo.objetoTramitavel.condicao = Condicao.findById(AGUARDANDO_RESPOSTA_COMUNICADO);


			} else if (analiseGeoBanco.analise.processo.caracterizacao.origemSobreposicao.equals(ATIVIDADE)){

				List<SobreposicaoCaracterizacaoAtividade> sobreposicoesCaracterizacaoAtividade =  analiseGeoBanco.analise.processo.caracterizacao.atividadesCaracterizacao.stream().map(atividadeCaracterizacao -> atividadeCaracterizacao.sobreposicaoCaracterizacaoAtividade).collect(Collectors.toList());

				for (SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade : sobreposicoesCaracterizacaoAtividade) {

					if(sobreposicaoCaracterizacaoAtividade != null){

						analiseGeoBanco.enviarEmailComunicado(analiseGeoBanco.analise.processo.caracterizacao, this, sobreposicaoCaracterizacaoAtividade);
					}

				}
				this.aguardarResposta(usuarioExecutor);
				analiseGeoBanco.analise.processo.objetoTramitavel.condicao = Condicao.findById(AGUARDANDO_RESPOSTA_COMUNICADO);


			} else if (analiseGeoBanco.analise.processo.caracterizacao.origemSobreposicao.equals(COMPLEXO)) {

				List<SobreposicaoCaracterizacaoComplexo> sobreposicoesCaracterizacaoComplexo = analiseGeoBanco.analise.processo.caracterizacao.sobreposicoesCaracterizacaoComplexo.stream().distinct()
						.filter(distinctByKey(sobreposicaoCaracterizacaoComplexo -> sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.codigo)).collect(Collectors.toList());

				for (SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo : sobreposicoesCaracterizacaoComplexo) {

					if(sobreposicaoCaracterizacaoComplexo != null){

						analiseGeoBanco.enviarEmailComunicado(analiseGeoBanco.analise.processo.caracterizacao, this, sobreposicaoCaracterizacaoComplexo);
					}

				}
				this.aguardarResposta(usuarioExecutor);
				analiseGeoBanco.analise.processo.objetoTramitavel.condicao = Condicao.findById(AGUARDANDO_RESPOSTA_COMUNICADO);


			} else if(analiseGeoBanco.analise.processo.caracterizacao.origemSobreposicao.equals(SEM_SOBREPOSICAO)) {

				gerente.save();

				analiseGeoBanco.analise.processo.tramitacao.tramitar(analiseGeoBanco.analise.processo, AcaoTramitacao.DEFERIR_ANALISE_GEO_VIA_GERENTE, usuarioExecutor, UsuarioAnalise.findByGerente(gerente));
				HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeoBanco.analise.processo.objetoTramitavel.id), usuarioExecutor);

			}
		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.INDEFERIDO)) {

			Gerente gerente = Gerente.distribuicaoAutomaticaGerente(usuarioExecutor.usuarioEntradaUnica.setorSelecionado.sigla, analiseGeoBanco);

			gerente.save();
			analiseGeoBanco.analise.processo.tramitacao.tramitar(analiseGeoBanco.analise.processo, AcaoTramitacao.INDEFERIR_ANALISE_GEO_VIA_GERENTE, usuarioExecutor, UsuarioAnalise.findByGerente(gerente));
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeoBanco.analise.processo.objetoTramitavel.id), usuarioExecutor);

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.EMITIR_NOTIFICACAO)) {

			List<Notificacao> notificacoes = this.analiseGeo.notificacoes;
			notificacoes = notificacoes.stream().filter(notificacao -> notificacao.id == null).collect(Collectors.toList());

			if (notificacoes.size() != 1) {

				throw new ValidacaoException(Mensagem.ERRO_SALVAMENTO_NOTIFICACAO);
			}

			analiseGeoBanco.enviarEmailNotificacao(notificacoes.get(0), this, this.analiseGeo.documentos);
			analiseGeoBanco.alterarStatusLicenca(StatusCaracterizacaoEnum.NOTIFICADO.codigo, this.analiseGeo.analise.processo.numero);

			analiseGeoBanco.analise.processo.tramitacao.tramitar(analiseGeoBanco.analise.processo, AcaoTramitacao.NOTIFICAR, usuarioExecutor, "Notificado");
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeoBanco.analise.processo.objetoTramitavel.id), usuarioExecutor);

		}

		this.usuario = usuarioExecutor;
		this.documentos = this.analiseGeo.documentos.stream().filter(documento -> documento.tipo.id.equals(TipoDocumento.DOCUMENTO_ANALISE_TEMPORAL) || documento.tipo.id.equals(TipoDocumento.PARECER_ANALISE_GEO)).collect(Collectors.toList());
		this.dataParecer = new Date();

		HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(analiseGeoBanco.analise.processo.objetoTramitavel.id);
		this.idHistoricoTramitacao = historicoTramitacao.idHistorico;

		this._save();

	}

}
