package models;

import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.db.jpa.GenericModel;
import javax.persistence.*;
import java.util.Date;

import static security.Auth.getUsuarioSessao;

@Entity
@Table(schema = "analise", name = "parecer_gerente_analise_geo")
public class ParecerGerenteAnaliseGeo extends GenericModel {

	public static final String SEQ = "analise.parecer_gerente_analise_geo_id_seq";

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
	@JoinColumn(name = "id_usuario_gerente", referencedColumnName = "id")
	public UsuarioAnalise usuario;

	@Column(name = "id_historico_tramitacao")
	public Long idHistoricoTramitacao;

	public void finalizar(AnaliseGeo analiseGeo, UsuarioAnalise gerente) {

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.PARECER_VALIDADO)) {

			UsuarioAnalise usuarioAnalistaTecnico = UsuarioAnalise.findByAnalistaTecnico(AnalistaTecnico.distribuicaoAutomaticaAnalistaTecnico(gerente.usuarioEntradaUnica.setorSelecionado.sigla, analiseGeo));
			AnaliseGeo analiseGeoBanco = AnaliseGeo.findById(analiseGeo.id);

			AnaliseTecnica analiseTecnica = analiseGeoBanco.geraAnaliseTecnica().save();
			analiseTecnica.geraLicencasAnaliseTecnica(analiseGeoBanco.licencasAnalise);
			analiseTecnica.analistaTecnico = new AnalistaTecnico(analiseTecnica, usuarioAnalistaTecnico).save();

			analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.VALIDAR_PARECER_GEO_GERENTE, getUsuarioSessao(), usuarioAnalistaTecnico);
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), getUsuarioSessao());

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.SOLICITAR_AJUSTES)) {

			AnalistaGeo analista = AnalistaGeo.findByAnaliseGeo(analiseGeo.id);
			UsuarioAnalise analistaGeo = UsuarioAnalise.findById(analista.usuario.id);

			analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_GEO_PELO_GERENTE, getUsuarioSessao(), analistaGeo);
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), getUsuarioSessao());

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.PARECER_NAO_VALIDADO)) {

			UsuarioAnalise analistaGeoDestino = UsuarioAnalise.findById(analiseGeo.idAnalistaDestino);
			AnalistaGeo analistaGeo = AnalistaGeo.find("id_analise_geo = :id_analise_geo")
					.setParameter("id_analise_geo", analiseGeo.id).first();

			analistaGeo.usuario = analistaGeoDestino;
			analistaGeo._save();

			analiseGeo.analise.processo.tramitacao.tramitar(analiseGeo.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_GEO_ENCAMINHANDO_GEO, getUsuarioSessao(), analistaGeoDestino);
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id), getUsuarioSessao());

		}

		this.usuario = gerente;
		this.analiseGeo = analiseGeo;
		this.dataParecer = new Date();

		HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(analiseGeo.analise.processo.objetoTramitavel.id);
		this.idHistoricoTramitacao = historicoTramitacao.idHistorico;

		this.save();

	}

}
