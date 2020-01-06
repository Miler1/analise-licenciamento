package models;
import exceptions.ValidacaoException;

import models.tramitacao.AcaoTramitacao;
import models.tramitacao.Condicao;
import models.tramitacao.HistoricoTramitacao;
import org.apache.commons.lang.StringUtils;
import play.db.jpa.GenericModel;
import utils.Mensagem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema = "analise", name = "parecer_analista_tecnico")
public class ParecerAnalistaTecnico extends GenericModel {

	public static final String SEQ = "analise.parecer_analista_tecnico_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	@Column(name = "id")
	public Long id;

	@ManyToOne
	@JoinColumn(name = "id_analise_tecnica")
	public AnaliseTecnica analiseTecnica;

	@OneToOne
	@JoinColumn(name = "id_tipo_resultado_analise")
	public TipoResultadoAnalise tipoResultadoAnalise;

	@Column(name = "data")
	public Date data;

	@OneToOne
	@JoinColumn(name = "id_usuario_analista_tecnico", referencedColumnName = "id")
	public UsuarioAnalise analistaTecnico;

	@Column(name = "do_processo")
	public String doProcesso;

	@Column(name = "da_analise_tecnica")
	public String daAnaliseTecnica;

	@Column(name = "da_conclusao")
	public String daConclusao;

	@Column(name = "parecer")
	public String parecer;

	@Column(name = "validade_permitida")
	public Integer validadePermitida;

	@OneToMany(mappedBy = "parecerAnalistaTecnico", orphanRemoval=true)
	public List<Condicionante> condicionantes;

	@OneToMany(mappedBy = "parecerAnalistaTecnico", orphanRemoval=true)
	public List<Restricao> restricoes;

	@Column(name="finalidade_atividade")
	public String finalidadeAtividade;

	@OneToOne(mappedBy = "parecerAnalistaTecnico")
	public Vistoria vistoria;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(schema="analise", name="rel_documento_parecer_analista_tecnico",
			joinColumns=@JoinColumn(name="id_parecer_analista_tecnico"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> documentos;

	@Column(name = "id_historico_tramitacao")
	public Long idHistoricoTramitacao;

	private void finalizaParecerDeferido(AnaliseTecnica analiseTecnica) {

		if(this.vistoria != null) {

			this.vistoria.parecerAnalistaTecnico = this;
			this.vistoria = this.vistoria.salvar();

		}

		if(this.condicionantes != null && !this.condicionantes.isEmpty()) {

			this.condicionantes.forEach(condicionante -> {
				condicionante.parecerAnalistaTecnico = this;
				condicionante._save();
			});

		}

		if(this.restricoes != null && !this.restricoes.isEmpty()) {

			this.restricoes.forEach(restricao -> {
				restricao.parecerAnalistaTecnico = this;
				restricao._save();
			});

		}

		Gerente gerente = Gerente.distribuicaoAutomaticaGerenteAnaliseTecnica(this.analistaTecnico.usuarioEntradaUnica.setorSelecionado.sigla, analiseTecnica);
		gerente._save();

		analiseTecnica.analise.processo.tramitacao.tramitar(analiseTecnica.analise.processo, AcaoTramitacao.DEFERIR_ANALISE_TECNICA_VIA_GERENTE, this.analistaTecnico, UsuarioAnalise.findByGerente(gerente));
		HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnica.analise.processo.objetoTramitavel.id), this.analistaTecnico);

	}

	public void finalizar(UsuarioAnalise usuarioExecutor) {

		AnaliseTecnica analiseTecnicaBanco = AnaliseTecnica.findById(this.analiseTecnica.id);

		validarParecer();
		validarTipoResultadoAnalise();

		this.analistaTecnico = usuarioExecutor;
		this.data = new Date();

		if(this.documentos != null && !this.documentos.isEmpty()) {
			this.updateDocumentos(this.documentos);
		}

		if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.DEFERIDO)) {

			this.finalizaParecerDeferido(analiseTecnicaBanco);

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.INDEFERIDO)) {

			Gerente gerente = Gerente.distribuicaoAutomaticaGerenteAnaliseTecnica(usuarioExecutor.usuarioEntradaUnica.setorSelecionado.sigla, analiseTecnicaBanco);
			gerente.save();

			if(this.vistoria != null) {

				this.vistoria.parecerAnalistaTecnico = this;
				this.vistoria = this.vistoria.salvar();

			}

			analiseTecnicaBanco.analise.processo.tramitacao.tramitar(analiseTecnicaBanco.analise.processo, AcaoTramitacao.INDEFERIR_ANALISE_TECNICA_VIA_GERENTE, usuarioExecutor, UsuarioAnalise.findByGerente(gerente));
			HistoricoTramitacao.setSetor(HistoricoTramitacao.getUltimaTramitacao(analiseTecnicaBanco.analise.processo.objetoTramitavel.id), usuarioExecutor);

		} else if (this.tipoResultadoAnalise.id.equals(TipoResultadoAnalise.EMITIR_NOTIFICACAO)) {

			// rayds

		}

		HistoricoTramitacao historicoTramitacao = HistoricoTramitacao.getUltimaTramitacao(analiseTecnicaBanco.analise.processo.objetoTramitavel.id);
		this.idHistoricoTramitacao = historicoTramitacao.idHistorico;
		this._save();

	}

	private List<Documento> updateDocumentos(List<Documento> novosDocumentos) {

		TipoDocumento tipoAutoInfracao = TipoDocumento.findById(TipoDocumento.AUTO_INFRACAO);
		TipoDocumento tipoParecer = TipoDocumento.findById(TipoDocumento.PARECER_ANALISE_TECNICA);

		this.documentos = new ArrayList<>();

		for (Documento documento : novosDocumentos) {

			if(documento.id != null) {

				documento = Documento.findById(documento.id);

			} else {

				if(documento.tipo.id.equals(tipoAutoInfracao.id)) {

					documento.tipo = tipoAutoInfracao;

				} else if (documento.tipo.id.equals(tipoParecer.id)) {

					documento.tipo = tipoParecer;

				}

				documento = documento.save();

			}

			this.documentos.add(documento);

		}

		return this.documentos;

	}

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

	}

}
