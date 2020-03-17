package models;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Entity
@Table(schema = "analise", name = "vistoria")
public class Vistoria extends GenericModel {

	public static final String SEQ = "analise.vistoria_id_seq";

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_parecer_analista_tecnico", referencedColumnName = "id")
	public ParecerAnalistaTecnico parecerAnalistaTecnico;

	@Column(name = "tx_conclusao")
	public String conclusao;

	@Column(name = "realizada")
	public Boolean realizada;

	@OneToOne
	@Required
	@JoinColumn(name = "id_documento_rit")
	public Documento documentoRit;

	@OneToOne(mappedBy = "vistoria", cascade = CascadeType.ALL)
	public InconsistenciaVistoria inconsistenciaVistoria;

	@Column(name = "data")
	public Date data;

	@Column(name = "hora")
	@Temporal(TemporalType.TIMESTAMP)
	public Date hora;

	@Column(name = "tx_descricao")
	public String descricao;

	@Column(name = "tx_cursos_dagua")
	public String cursosDagua;

	@Column(name = "tx_tipologia_vegetal")
	public String tipologiaVegetal;

	@Column(name = "tx_app")
	public String app;

	@Column(name = "tx_ocorrencia")
	public String ocorrencia;

	@Column(name = "tx_residuos_liquidos")
	public String residuosLiquidos;

	@Column(name = "tx_outras_informacoes")
	public String outrasInformacoes;

	@OneToMany(mappedBy = "vistoria")
	public List<EquipeVistoria> equipe;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(schema="analise", name="rel_documento_vistoria",
			joinColumns=@JoinColumn(name="id_vistoria"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> anexos;

	@OneToOne
	@JoinColumn(name = "id_documento_relatorio_tecnico_vistoria", referencedColumnName = "id")
	public Documento documentoRelatorioTecnicoVistoria;

	public Documento updateDocumentoRIT(Documento novoDocumentoRIT, Vistoria vAntiga) {

		if(vAntiga != null) {
			if(this.documentoRit != null && this.documentoRit.id == null) {
				vAntiga.documentoRit._delete();
			}
		}

		if(this.documentoRit != null) {

			if(this.documentoRit.id == null) {

				this.documentoRit.tipo = TipoDocumento.findById(TipoDocumento.DOCUMENTO_RIT);
				this.documentoRit._save();
			}
		}

		return this.documentoRit;
	}

	public List<Documento> updateDocumentos(List<Documento> novosDocumentos, Vistoria vAntiga) {

		if(vAntiga != null) {

			AtomicReference<Boolean> removido = new AtomicReference<>(true);

			vAntiga.anexos.forEach(anexoA -> {
				if(this.anexos.stream().anyMatch(anexo -> anexoA.id.equals(anexo.id))){
					removido.set(false);
				}
				if(removido.get()) {
					anexoA._delete();
				} else {
					removido.set(true);
				}
			});

		}


		TipoDocumento tipoDocumentoVistoria = TipoDocumento.findById(TipoDocumento.DOCUMENTO_VISTORIA);

		this.anexos = new ArrayList<>();

		for (Documento documento : novosDocumentos) {

			if (!documento.tipo.id.equals(TipoDocumento.DOCUMENTO_RIT)) {

				if (documento.id != null) {

					documento = Documento.findById(documento.id);

				} else {

					if (documento.tipo.id.equals(tipoDocumentoVistoria.id)) {

						documento.tipo = tipoDocumentoVistoria;

					}

					documento = documento.save();

				}

				this.anexos.add(documento);
			}
		}

		return this.anexos;

	}

	public Vistoria salvar() {

		if(this.inconsistenciaVistoria != null) {

			this.inconsistenciaVistoria.vistoria = this;

		}

		if(!this.equipe.isEmpty()) {

			this.equipe.forEach(analista -> analista.vistoria = this);

		}

		if(this.id == null){

			this.documentoRit = this.updateDocumentoRIT(this.documentoRit, null);

			this.updateDocumentos(this.anexos, null);

			return this.save();

		} else {

			Vistoria vAntiga = findById(this.id);

			vAntiga.documentoRit = this.updateDocumentoRIT(this.documentoRit, vAntiga);

			vAntiga.anexos = this.updateDocumentos(this.anexos, vAntiga);

			vAntiga.equipe.forEach(e -> {
				e._delete();
			});

			vAntiga.equipe = this.equipe;
			vAntiga.equipe.forEach(e -> {
				e.usuario = UsuarioAnalise.findById(e.usuario.id);
				e.vistoria = vAntiga;
				e._save();
			});

			vAntiga.realizada = this.realizada;
			vAntiga.data = this.data;
			vAntiga.conclusao = this.conclusao;
			vAntiga.hora = this.hora;
			vAntiga.descricao = this.descricao;
			vAntiga.cursosDagua = this.cursosDagua;
			vAntiga.tipologiaVegetal = this.tipologiaVegetal;
			vAntiga.app = this.app;
			vAntiga.ocorrencia = this.ocorrencia;
			vAntiga.residuosLiquidos = this.residuosLiquidos;
			vAntiga.outrasInformacoes = this.outrasInformacoes;

			vAntiga._save();
			return vAntiga;
		}
	}

	public static Vistoria findByIdParecer(Long parecerAnalistaTecnicoId){

		Vistoria vistoria= Vistoria.find("id_parecer_analista_tecnico", parecerAnalistaTecnicoId).first();

		return vistoria;

	}


}
