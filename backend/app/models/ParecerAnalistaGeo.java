package models;

import exceptions.ValidacaoException;
import models.licenciamento.*;

import models.tramitacao.AcaoTramitacao;
import models.tramitacao.Condicao;
import models.tramitacao.HistoricoTramitacao;
import org.apache.commons.lang.StringUtils;
import utils.Mensagem;

import javax.persistence.*;
import javax.validation.ValidationException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static models.licenciamento.Caracterizacao.OrigemSobreposicao.*;
import static models.tramitacao.Condicao.AGUARDANDO_RESPOSTA_COMUNICADO;
import static models.tramitacao.AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_COORDENADOR;

@Entity
@Table(schema = "analise", name = "parecer_analista_geo")
public class ParecerAnalistaGeo extends ParecerAnalista {

	public static final String SEQ = "analise.parecer_analista_geo_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	@Column(name = "id")
	public Long id;

	@OneToOne
	@JoinColumn(name = "id_analise_geo")
	public AnaliseGeo analiseGeo;

	@Column(name = "situacao_fundiaria")
	public String situacaoFundiaria;

	@Column(name = "analise_temporal")
	public String analiseTemporal;

	@Column(name = "conclusao")
	public String conclusao;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_usuario_analista_geo", referencedColumnName = "id")
	public UsuarioAnalise usuario;

	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(schema="analise", name="rel_documento_parecer_analista_geo",
			joinColumns=@JoinColumn(name="id_parecer_analista_geo"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> documentos;

	@OneToOne
	@JoinColumn(name = "id_documento", referencedColumnName = "id")
	public Documento documentoParecer;

	@OneToOne
	@JoinColumn(name = "id_carta_imagem", referencedColumnName = "id")
	public Documento cartaImagem;

	private void validarParecer() {

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

	public static ParecerAnalistaGeo getUltimoParecer(List<ParecerAnalistaGeo> pareceresAnalistaGeos) {

		return pareceresAnalistaGeos.stream().max(Comparator.comparing(ParecerAnalistaGeo::getDataParecer)).orElseThrow(ValidationException::new);

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

	public static boolean verificaTipoSobreposicaoComunicado(SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento) {

		if (sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.id == TipoSobreposicao.TERRA_INDIGENA_ZA || sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.id == TipoSobreposicao.UC_FEDERAL_APA_DENTRO ||
				sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.id == TipoSobreposicao.UC_ESTADUAL_PI_FORA || sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.id == TipoSobreposicao.UC_MUNICIPAL) {

			return true;
		}

		return false;
	}

	public static boolean verificaTipoSobreposicaoComunicado(SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo) {

		if (sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.id == TipoSobreposicao.TERRA_INDIGENA_ZA || sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.id == TipoSobreposicao.UC_FEDERAL_APA_DENTRO ||
				sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.id == TipoSobreposicao.UC_ESTADUAL_PI_FORA || sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.id == TipoSobreposicao.UC_MUNICIPAL) {

			return true;
		}

		return false;
	}

	public static boolean verificaTipoSobreposicaoComunicado(SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade) {

		return sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.id == TipoSobreposicao.TERRA_INDIGENA_ZA || sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.id == TipoSobreposicao.UC_FEDERAL_APA_DENTRO ||
				sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.id == TipoSobreposicao.UC_ESTADUAL_PI_FORA || sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.id == TipoSobreposicao.UC_MUNICIPAL;
	}

	public void finalizar(UsuarioAnalise usuarioExecutor) throws Exception {

		AnaliseGeo analiseGeoBanco = AnaliseGeo.findById(this.analiseGeo.id);
		boolean possuiComunicado = false;
		boolean pararProcesso = false;
		
		validarParecer();
		validarTipoResultadoAnalise();

		this.usuario = usuarioExecutor;
		this.dataParecer = new Date();

		if(this.documentos != null && !this.documentos.isEmpty()) {
			this.updateDocumentos(this.documentos);
		}

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.DEFERIDO)) {

			CoordenadorGeo coordenador = CoordenadorGeo.distribuicaoAutomaticaCoordenador(usuarioExecutor.usuarioEntradaUnica.setorSelecionado.sigla, analiseGeoBanco);

			if(analiseGeoBanco.analise.processo.caracterizacao.origemSobreposicao.equals(EMPREENDIMENTO)){

				List<SobreposicaoCaracterizacaoEmpreendimento> sobreposicoesCaracterizacaoEmpreendimento = analiseGeoBanco.analise.processo.caracterizacao.sobreposicoesCaracterizacaoEmpreendimento.stream().distinct()
						.filter(distinctByKey(sobreposicaoCaracterizacaoEmpreendimento -> sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.codigo)).collect(Collectors.toList());

				for (SobreposicaoCaracterizacaoEmpreendimento sobreposicaoCaracterizacaoEmpreendimento : sobreposicoesCaracterizacaoEmpreendimento) {

					if(sobreposicaoCaracterizacaoEmpreendimento != null) {

						for (Orgao orgaoResponsavel : sobreposicaoCaracterizacaoEmpreendimento.tipoSobreposicao.orgaosResponsaveis) {

							if (!orgaoResponsavel.sigla.equals(OrgaoEnum.IPHAN.codigo) && !orgaoResponsavel.sigla.equals(OrgaoEnum.IBAMA.codigo)) {

								analiseGeoBanco.enviarEmailComunicado(analiseGeoBanco.analise.processo.caracterizacao, this, sobreposicaoCaracterizacaoEmpreendimento, orgaoResponsavel);
								possuiComunicado = true;
								pararProcesso =  verificaTipoSobreposicaoComunicado(sobreposicaoCaracterizacaoEmpreendimento);
							}

						}
					}

				}

			} else if (analiseGeoBanco.analise.processo.caracterizacao.origemSobreposicao.equals(ATIVIDADE)){

				List<SobreposicaoCaracterizacaoAtividade> sobreposicoesCaracterizacaoAtividade = new ArrayList<>();
				analiseGeoBanco.analise.processo.caracterizacao.atividadesCaracterizacao.stream()
						.filter(atividadeCaracterizacao -> !atividadeCaracterizacao.sobreposicoesCaracterizacaoAtividade.isEmpty())
						.forEach(atividadeCaracterizacao -> sobreposicoesCaracterizacaoAtividade.addAll(atividadeCaracterizacao.sobreposicoesCaracterizacaoAtividade));

				for (SobreposicaoCaracterizacaoAtividade sobreposicaoCaracterizacaoAtividade : sobreposicoesCaracterizacaoAtividade) {

					if(sobreposicaoCaracterizacaoAtividade != null) {

						for (Orgao orgaoResponsavel : sobreposicaoCaracterizacaoAtividade.tipoSobreposicao.orgaosResponsaveis) {

							if (!orgaoResponsavel.sigla.equals(OrgaoEnum.IPHAN.codigo) && !orgaoResponsavel.sigla.equals(OrgaoEnum.IBAMA.codigo)) {

								analiseGeoBanco.enviarEmailComunicado(analiseGeoBanco.analise.processo.caracterizacao, this, sobreposicaoCaracterizacaoAtividade, orgaoResponsavel);
								possuiComunicado = true;
								pararProcesso =  verificaTipoSobreposicaoComunicado(sobreposicaoCaracterizacaoAtividade);
							}

						}
					}

				}

			} else if (analiseGeoBanco.analise.processo.caracterizacao.origemSobreposicao.equals(COMPLEXO)) {

				List<SobreposicaoCaracterizacaoComplexo> sobreposicoesCaracterizacaoComplexo = analiseGeoBanco.analise.processo.caracterizacao.sobreposicoesCaracterizacaoComplexo.stream().distinct()
						.filter(distinctByKey(sobreposicaoCaracterizacaoComplexo -> sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.codigo)).collect(Collectors.toList());

				for (SobreposicaoCaracterizacaoComplexo sobreposicaoCaracterizacaoComplexo : sobreposicoesCaracterizacaoComplexo) {

					if(sobreposicaoCaracterizacaoComplexo != null) {

						for (Orgao orgaoResponsavel : sobreposicaoCaracterizacaoComplexo.tipoSobreposicao.orgaosResponsaveis) {

							if (!orgaoResponsavel.sigla.equals(OrgaoEnum.IPHAN.codigo) && !orgaoResponsavel.sigla.equals(OrgaoEnum.IBAMA.codigo)) {

								analiseGeoBanco.enviarEmailComunicado(analiseGeoBanco.analise.processo.caracterizacao, this, sobreposicaoCaracterizacaoComplexo, orgaoResponsavel);
								possuiComunicado = true;
								pararProcesso =  verificaTipoSobreposicaoComunicado(sobreposicaoCaracterizacaoComplexo);
							}

						}
					}

				}

			}

			if (possuiComunicado && pararProcesso) {

				this.aguardarResposta(usuarioExecutor);
				analiseGeoBanco.analise.processo.objetoTramitavel.condicao = Condicao.findById(AGUARDANDO_RESPOSTA_COMUNICADO);

			} else {

				coordenador.save();

				analiseGeoBanco.analise.processo.tramitacao.tramitar(analiseGeoBanco.analise.processo, AcaoTramitacao.DEFERIR_ANALISE_GEO_VIA_COORDENADOR, usuarioExecutor, UsuarioAnalise.findByCoordenadorGeo(coordenador));
				HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeoBanco.analise.processo.objetoTramitavel.id), usuarioExecutor);

			}

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.INDEFERIDO)) {

			CoordenadorGeo coordenador = CoordenadorGeo.distribuicaoAutomaticaCoordenador(usuarioExecutor.usuarioEntradaUnica.setorSelecionado.sigla, analiseGeoBanco);

			coordenador.save();
			analiseGeoBanco.analise.processo.tramitacao.tramitar(analiseGeoBanco.analise.processo, AcaoTramitacao.INDEFERIR_ANALISE_GEO_VIA_COORDENADOR, usuarioExecutor, UsuarioAnalise.findByCoordenadorGeo(coordenador));
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeoBanco.analise.processo.objetoTramitavel.id), usuarioExecutor);

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.EMITIR_NOTIFICACAO)) {

			List<Notificacao> notificacoes = this.analiseGeo.notificacoes;
			notificacoes = notificacoes.stream().filter(notificacao -> notificacao.id == null).collect(Collectors.toList());

			if (notificacoes.size() != 1) {

				throw new ValidacaoException(Mensagem.ERRO_SALVAMENTO_NOTIFICACAO);

			}

			analiseGeoBanco.enviarEmailNotificacao(notificacoes.get(0), this.save(), this.analiseGeo.documentos);
			Analise.alterarStatusLicenca(StatusCaracterizacaoEnum.NOTIFICADO.codigo, this.analiseGeo.analise.processo.numero);

			analiseGeoBanco.analise.processo.tramitacao.tramitar(analiseGeoBanco.analise.processo, AcaoTramitacao.NOTIFICAR_PELO_ANALISTA_GEO, usuarioExecutor, "Notificado");
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeoBanco.analise.processo.objetoTramitavel.id), usuarioExecutor);

		}

		HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(analiseGeoBanco.analise.processo.objetoTramitavel.id);
		this.idHistoricoTramitacao = historicoTramitacao.idHistorico;

		this._save();

	}

	private List<Documento> updateDocumentos(List<Documento> novosDocumentos) {

		TipoDocumento tipoParecer = TipoDocumento.findById(TipoDocumento.PARECER_ANALISE_GEO);
		TipoDocumento tipoNotificacao = TipoDocumento.findById(TipoDocumento.DOCUMENTO_NOTIFICACAO_ANALISE_GEO);
		TipoDocumento tipoDocumentoAnaliseTemporal = TipoDocumento.findById(TipoDocumento.DOCUMENTO_ANALISE_TEMPORAL);

		this.documentos = new ArrayList<>();

		for (Documento documento : novosDocumentos) {

			if(documento.id != null) {

				documento = Documento.findById(documento.id);

			} else {

				if (documento.tipo.id.equals(tipoParecer.id)) {

					documento.tipo = tipoParecer;

				} else if (documento.tipo.id.equals(tipoNotificacao.id)) {

					documento.tipo = tipoNotificacao;

				} else if (documento.tipo.id.equals(tipoDocumentoAnaliseTemporal.id)) {

					documento.tipo = tipoDocumentoAnaliseTemporal;

				}

				documento = documento.save();

			}

			this.documentos.add(documento);

		}

		return this.documentos;

	}

	public static ParecerAnalistaGeo findParecerByProcesso(Processo processo) {

		HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(processo.idObjetoTramitavel);

		if(historicoTramitacao.idAcao.equals(SOLICITAR_AJUSTES_PARECER_GEO_PELO_COORDENADOR)) {

			return ParecerAnalistaGeo.getUltimoParecer(processo.analise.analiseGeo.pareceresAnalistaGeo);

		}

		return new ParecerAnalistaGeo();

	}

	@Override
	public List<Documento> getDocumentos() {
		return this.documentos;
	}

	@Override
	public List<Documento> getDocumentosParecer() {
		return this.documentos.stream().filter(Documento::isNotificacaoAnaliseGeo).collect(Collectors.toList());
	}
}
