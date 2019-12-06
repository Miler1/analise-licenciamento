package models;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Mensagem;

import javax.persistence.*;
import javax.print.Doc;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema="analise", name="inconsistencia_vistoria")
public class InconsistenciaVistoria extends GenericModel{

	public static final String SEQ = "analise.inconsistencia_vistoria_id_seq";

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@OneToOne
	@JoinColumn(name="id_vistoria")
	public Vistoria vistoria;

	@Required
	@Column(name="descricao_inconsistencia")
	public String descricaoInconsistencia;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(schema="analise", name="rel_documento_inconsistencia_vistoria",
			joinColumns=@JoinColumn(name="id_inconsistencia_vistoria"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> anexos;

	private void updateAnexos(List<Documento> novosAnexos) {

		this.anexos.stream()
				.filter(documento -> {

					List<Long> idsAnexos = novosAnexos.stream().filter(anexo -> anexo.id != null).map(anexo -> anexo.id).collect(Collectors.toList());

					return documento.id != null && idsAnexos.stream().noneMatch(idAnexo -> idAnexo.equals(documento.id));

				}).forEach(Documento::_delete);

		this.anexos.clear();

		novosAnexos.stream()
				.filter(documento -> documento.id != null)
				.forEach(anexo -> this.anexos.add(Documento.findById(anexo.id)));

		novosAnexos.stream().
				filter(documento -> documento.id == null)
				.forEach(anexo -> this.anexos.add(anexo.save()));

	}

	private InconsistenciaVistoria update() {

		InconsistenciaVistoria inconsistenciaVistoria = InconsistenciaVistoria.findById(this.id);
		inconsistenciaVistoria.vistoria = this.vistoria;
		inconsistenciaVistoria.descricaoInconsistencia = this.descricaoInconsistencia;
		inconsistenciaVistoria.updateAnexos(this.anexos);

		return inconsistenciaVistoria.save();

	}

	public InconsistenciaVistoria salvar() {

		if(this.id != null) {

			return this.update();

		}

		this.anexos.forEach(Documento::save);

		return this.save();

	}

	public String deletar() {

		this.anexos.clear();

		this._delete();

		return Mensagem.INCONSISTENCIA_VISTORIA_EXCLUIDA_SUCESSO.getTexto();

	}

}
