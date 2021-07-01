package models;

import models.licenciamento.*;
import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.DateUtil;

import javax.persistence.*;
import java.util.*;

import static security.Auth.getUsuarioSessao;

@Entity
@Table(schema = "analise", name = "parecer_coordenador_geo_analise_geo")
public class ParecerCoordenadorAnaliseGeo extends GenericModel {

	public static final String SEQ = "analise.parecer_coordenador_geo_analise_geo_id_seq";

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

	@Column(name = "data_parecer")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataParecer;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_usuario_coordenador_geo", referencedColumnName = "id")
	public UsuarioAnalise usuario;

	@Column(name = "id_historico_tramitacao")
	public Long idHistoricoTramitacao;

	public Date getDataParecer() {
		return dataParecer;
	}

	public void finalizar(AnaliseGeo analiseGeo, UsuarioAnalise coordenador) throws Exception {

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.PARECER_VALIDADO)) {

			AnaliseTecnica analiseTecnicaAnterior = AnaliseTecnica.findUltimaByAnalise(analiseGeo.analise);
			UsuarioAnalise usuarioAnalistaTecnico;

			if(analiseTecnicaAnterior != null){

				usuarioAnalistaTecnico = analiseTecnicaAnterior.analistaTecnico.usuario;

			} else {

				usuarioAnalistaTecnico = UsuarioAnalise.findByAnalistaTecnico(AnalistaTecnico.distribuicaoAutomaticaAnalistaTecnico(coordenador.usuarioEntradaUnica.setorSelecionado.sigla, analiseGeo.analise));

			}

			AnaliseTecnica analiseTecnica = analiseGeo.geraAnaliseTecnica().save();
			analiseTecnica.geraLicencasAnaliseTecnica(analiseGeo.licencasAnalise);
			analiseTecnica.analistaTecnico = new AnalistaTecnico(analiseTecnica, usuarioAnalistaTecnico).save();

			ParecerAnalistaGeo parecerAnalistaGeo = ParecerAnalistaGeo.getUltimoParecer(analiseGeo.pareceresAnalistaGeo);

			analiseGeo.dataFim = new Date();
			analiseGeo.ativo = false;
			analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.VALIDAR_PARECER_GEO_COORDENADOR, getUsuarioSessao(), usuarioAnalistaTecnico);
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), getUsuarioSessao());

			enviarEmailJuridico(analiseGeo, analiseGeo.analise.processo.caracterizacao, parecerAnalistaGeo, analiseTecnica);

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.SOLICITAR_AJUSTES)) {

			AnalistaGeo analista = AnalistaGeo.findByAnaliseGeo(analiseGeo.id);
			UsuarioAnalise analistaGeo = UsuarioAnalise.findById(analista.usuario.id);

			analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_COORDENADOR, getUsuarioSessao(), analistaGeo);
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), getUsuarioSessao());

			analiseGeo.dataVencimentoPrazo = DateUtil.somaDiasEmData(DateUtil.somaDiasEmData(analiseGeo.dataCadastro, Configuracoes.PRAZO_ANALISE_GEO) , DiasAnalise.intervalosTramitacoesAnaliseGeo(analiseGeo.analise.processo.getHistoricoTramitacao()));
			analiseGeo.analise.diasAnalise.preencheDiasAnaliseGeo();
			analiseGeo.analise.diasAnalise._save();

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.PARECER_NAO_VALIDADO)) {

			UsuarioAnalise analistaGeoDestino = UsuarioAnalise.findById(this.analiseGeo.idAnalistaDestino);
			AnalistaGeo analistaGeo = AnalistaGeo.find("id_analise_geo = :id_analise_geo")
					.setParameter("id_analise_geo", analiseGeo.id).first();

			analistaGeo.usuario = analistaGeoDestino;
			analistaGeo._save();

			analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO, getUsuarioSessao(), analistaGeoDestino);
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), getUsuarioSessao());

			analiseGeo.dataVencimentoPrazo = DateUtil.somaDiasEmData(new Date(), Configuracoes.PRAZO_ANALISE_GEO);
			analiseGeo.analise.diasAnalise.preencheDiasAnaliseGeo();
			analiseGeo.analise.diasAnalise._save();

		}

		this.usuario = coordenador;
		this.dataParecer = new Date();
		analiseGeo._save();

		HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id);
		this.idHistoricoTramitacao = historicoTramitacao.idHistorico;

		this.save();

	}

	public void enviarEmailJuridico(AnaliseGeo analiseGeo, Caracterizacao caracterizacao, ParecerAnalistaGeo parecerAnalistaGeo, AnaliseTecnica analiseTecnica) throws Exception {

		List<String> destinatarios = new ArrayList<>();
		destinatarios.add(Configuracoes.DESTINATARIO_JURIDICO);
		destinatarios.add(Configuracoes.DESTINATARIO_JURIDICO2);

		TipoDocumentoLicenciamento tipoDocumentoLicenciamento = TipoDocumentoLicenciamento.findByCodigo(TipoDocumentoLicenciamento.DOCUMENTO_FUNDIARIO);

		SolicitacaoDocumentoCaracterizacao solicitacaoDocumentoCaracterizacao = SolicitacaoDocumentoCaracterizacao.findByIdTipoDocumentoAndCaracterizacao(tipoDocumentoLicenciamento.id, caracterizacao);
		DocumentoLicenciamento documentoFundiario = DocumentoLicenciamento.findById(solicitacaoDocumentoCaracterizacao.documento.id);

		ParecerJuridico parecerJuridico = new ParecerJuridico(analiseGeo, parecerAnalistaGeo, analiseTecnica, documentoFundiario);
		parecerJuridico.save();
		parecerJuridico.linkParecerJuridico = Configuracoes.APP_URL + "app/index.html#!/parecer-juridico/" + parecerJuridico.id;

		EmailParecerJuridico emailParecerJuridico = new EmailParecerJuridico(analiseGeo, parecerAnalistaGeo, destinatarios, parecerJuridico);
		emailParecerJuridico.enviar();

	}

}