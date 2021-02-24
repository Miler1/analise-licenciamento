package models;

import models.tramitacao.AcaoTramitacao;
import models.tramitacao.HistoricoTramitacao;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.DateUtil;

import javax.persistence.*;
import java.util.Date;

import static security.Auth.getUsuarioSessao;

@Entity
@Table(schema = "analise", name = "parecer_coordenador_analise_tecnica")
public class ParecerCoordenadorAnaliseTecnica extends GenericModel {

	public static final String SEQ = "analise.parecer_coordenador_analise_tecnica_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	@Column(name = "id")
	public Long id;

	@OneToOne
	@JoinColumn(name = "id_analise_tecnica")
	public AnaliseTecnica analiseTecnica;

	@ManyToOne
	@JoinColumn(name = "id_tipo_resultado_analise")
	public TipoResultadoAnalise tipoResultadoAnalise;

	@Column(name = "parecer")
	public String parecer;

	@Column(name = "data_parecer")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dataParecer;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_usuario_coordenador", referencedColumnName = "id")
	public UsuarioAnalise usuario;

	@Column(name = "id_historico_tramitacao")
	public Long idHistoricoTramitacao;

	public Date getDataParecer() {
		return dataParecer;
	}

	public void finalizar(AnaliseTecnica analiseTecnica, UsuarioAnalise coordenador) {

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.PARECER_VALIDADO)) {

			Diretor diretor = Diretor.distribuicaoAutomaticaDiretor(analiseTecnica.analise);
			diretor.save();

			analiseTecnica.geraLicencasAnaliseTecnica(analiseTecnica.licencasAnalise);
			analiseTecnica.analistaTecnico = new AnalistaTecnico(analiseTecnica, diretor.usuario).save();

			analiseTecnica.dataFim = new Date();
			analiseTecnica.ativo = false;

			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.VALIDAR_PARECER_TECNICO_COORDENADOR, getUsuarioSessao(), UsuarioAnalise.findByDiretor(diretor));
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), getUsuarioSessao());

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.SOLICITAR_AJUSTES)) {

			AnalistaTecnico analista = AnalistaTecnico.findByAnaliseTecnica(analiseTecnica.id);
			UsuarioAnalise analistaTecnico = UsuarioAnalise.findById(analista.usuario.id);

			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.SOLICITAR_AJUSTES_PARECER_TECNICO_PELO_COORDENADOR, getUsuarioSessao(), analistaTecnico);
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), getUsuarioSessao());

			analiseTecnica.dataVencimentoPrazo = DateUtil.somaDiasEmData(DateUtil.somaDiasEmData(analiseTecnica.dataCadastro, Configuracoes.PRAZO_ANALISE_TECNICA) , DiasAnalise.intervalosTramitacoesAnaliseTecnica(analiseTecnica.analise.processo.getHistoricoTramitacao()));
			analiseTecnica.analise.diasAnalise.preencheDiasAnaliseTecnica();
			analiseTecnica.analise.diasAnalise._save();

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.PARECER_NAO_VALIDADO)) {

			UsuarioAnalise analistaTecnicoDestino = UsuarioAnalise.findById(this.analiseTecnica.idAnalistaDestino);
			AnalistaTecnico analistaTecnico = AnalistaTecnico.find("id_analise_tecnica = :id_analise_tecnica")
					.setParameter("id_analise_tecnica", analiseTecnica.id).first();

			analistaTecnico.usuario = analistaTecnicoDestino;
			analistaTecnico._save();

			analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.INVALIDAR_PARECER_TECNICO_ENCAMINHANDO_TECNICO, getUsuarioSessao(), analistaTecnicoDestino);
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), getUsuarioSessao());

			analiseTecnica.dataVencimentoPrazo = DateUtil.somaDiasEmData(new Date(), Configuracoes.PRAZO_ANALISE_TECNICA);
			analiseTecnica.analise.diasAnalise.preencheDiasAnaliseTecnica();
			analiseTecnica.analise.diasAnalise._save();

		}

		this.usuario = coordenador;
		this.dataParecer = new Date();
		analiseTecnica._save();

		HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id);
		this.idHistoricoTramitacao = historicoTramitacao.idHistorico;

		this.save();

	}

}