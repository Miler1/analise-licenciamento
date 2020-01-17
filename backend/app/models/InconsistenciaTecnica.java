package models;


import exceptions.ValidacaoException;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.*;

import javax.persistence.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

@Entity
@Table(schema="analise", name="inconsistencia_tecnica")
public class InconsistenciaTecnica extends GenericModel{

	public static final String SEQ = "analise.inconsistencia_tecnica_id_seq";

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator=SEQ)
	@SequenceGenerator(name=SEQ, sequenceName=SEQ, allocationSize=1)
	public Long id;

	@Required
	@Column(name="descricao_inconsistencia")
	public String descricaoInconsistencia;

	@Required
	@Column(name="tipo_inconsistencia")
	public String tipoInconsistencia;

	@ManyToOne
	@JoinColumn(name="id_analise_tecnica")
	public AnaliseTecnica analiseTecnica;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(schema="analise", name="rel_documento_inconsistencia_tecnica",
			joinColumns=@JoinColumn(name="id_inconsistencia_tecnica"),
			inverseJoinColumns=@JoinColumn(name="id_documento"))
	public List<Documento> anexos;
	@OneToOne(mappedBy = "inconsistenciaTecnica")
	public InconsistenciaTecnicaTipoLicenca inconsistenciaTecnicaTipoLicenca;

	@OneToOne(mappedBy = "inconsistenciaTecnica")
	public InconsistenciaTecnicaAtividade inconsistenciaTecnicaAtividade;

	@OneToOne(mappedBy = "inconsistenciaTecnica")
	public InconsistenciaTecnicaParametro inconsistenciaTecnicaParametro;

	@OneToOne(mappedBy = "inconsistenciaTecnica")
	public InconsistenciaTecnicaQuestionario inconsistenciaTecnicaQuestionario;

	@OneToOne(mappedBy = "inconsistenciaTecnica")
	public InconsistenciaTecnicaDocumentoAdministrativo inconsistenciaTecnicaDocumentoAdministrativo;

	@OneToOne(mappedBy = "inconsistenciaTecnica")
	public InconsistenciaTecnicaDocumentoTecnicoAmbiental inconsistenciaTecnicaDocumentoTecnicoAmbiental;

	@Transient
	public String tipoDeInconsistenciaTecnica;

	public enum TipoDeInconsistenciaTecnica { TIPO_LICENCA, ATIVIDADE, PARAMETRO, QUESTIONARIO, DOCUMENTO_ADMINISTRATIVO, DOCUMENTO_TECNICO_AMBIENTAL }

	public void setTipoDeInconsistenciaTecnica() {
		if (this.inconsistenciaTecnicaTipoLicenca != null) {
			this.tipoDeInconsistenciaTecnica = TipoDeInconsistenciaTecnica.TIPO_LICENCA.name();

		} else if (this.inconsistenciaTecnicaAtividade != null) {
			this.tipoDeInconsistenciaTecnica = TipoDeInconsistenciaTecnica.ATIVIDADE.name();

		} else if (this.inconsistenciaTecnicaParametro != null) {
			this.tipoDeInconsistenciaTecnica = TipoDeInconsistenciaTecnica.PARAMETRO.name();

		} else if (this.inconsistenciaTecnicaQuestionario != null) {
			this.tipoDeInconsistenciaTecnica = TipoDeInconsistenciaTecnica.QUESTIONARIO.name();

		} else if (this.inconsistenciaTecnicaDocumentoAdministrativo != null) {
			this.tipoDeInconsistenciaTecnica = TipoDeInconsistenciaTecnica.DOCUMENTO_ADMINISTRATIVO.name();

		} else if (this.inconsistenciaTecnicaDocumentoTecnicoAmbiental != null) {
			this.tipoDeInconsistenciaTecnica = TipoDeInconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL.name();

		}
	}

	public InconsistenciaTecnica salvaInconsistenciaTecnica() {

		if (this.descricaoInconsistencia == null || this.descricaoInconsistencia.equals("")) {

			throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);
		}

		if (this.tipoInconsistencia == null || this.tipoInconsistencia.equals("")) {

			throw new ValidacaoException(Mensagem.CAMPOS_OBRIGATORIOS);
		}

		InconsistenciaTecnica novaInconsistenciaTecnica = null;

		if (this.id != null) {

			novaInconsistenciaTecnica = InconsistenciaTecnica.findById(this.id);
			novaInconsistenciaTecnica.descricaoInconsistencia = this.descricaoInconsistencia;
			novaInconsistenciaTecnica.tipoInconsistencia = this.tipoInconsistencia;
			novaInconsistenciaTecnica.analiseTecnica = this.analiseTecnica;
			novaInconsistenciaTecnica.id = this.id;
			novaInconsistenciaTecnica.saveAnexos(this.anexos);
			novaInconsistenciaTecnica.save();

			return novaInconsistenciaTecnica;

		} else {

			InconsistenciaTecnica inconsisenciaTecnicaNova = new InconsistenciaTecnica();

			if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.TIPO_LICENCA.name())) {

				inconsisenciaTecnicaNova.saveAnexos(this.anexos);

				inconsisenciaTecnicaNova = this;

				inconsisenciaTecnicaNova.inconsistenciaTecnicaTipoLicenca.inconsistenciaTecnica = this;

				inconsisenciaTecnicaNova.inconsistenciaTecnicaTipoLicenca.save();

			}

			if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.ATIVIDADE.name())) {

				inconsisenciaTecnicaNova.saveAnexos(this.anexos);

				inconsisenciaTecnicaNova = this;

				inconsisenciaTecnicaNova.inconsistenciaTecnicaAtividade.inconsistenciaTecnica = this;

				inconsisenciaTecnicaNova.inconsistenciaTecnicaAtividade.save();

			}

			if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.PARAMETRO.name())) {

				inconsisenciaTecnicaNova.saveAnexos(this.anexos);

				inconsisenciaTecnicaNova = this;

				inconsisenciaTecnicaNova.inconsistenciaTecnicaParametro.inconsistenciaTecnica = this;

				inconsisenciaTecnicaNova.inconsistenciaTecnicaParametro.save();

			}

            if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.QUESTIONARIO.name())) {

				inconsisenciaTecnicaNova.saveAnexos(this.anexos);

				inconsisenciaTecnicaNova = this;

				inconsisenciaTecnicaNova.inconsistenciaTecnicaQuestionario.inconsistenciaTecnica = this;

				inconsisenciaTecnicaNova.inconsistenciaTecnicaQuestionario.save();

            }

            if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.DOCUMENTO_ADMINISTRATIVO.name())) {

				inconsisenciaTecnicaNova.saveAnexos(this.anexos);

				inconsisenciaTecnicaNova = this;

				inconsisenciaTecnicaNova.inconsistenciaTecnicaDocumentoAdministrativo.inconsistenciaTecnica = this;

				inconsisenciaTecnicaNova.inconsistenciaTecnicaDocumentoAdministrativo.save();

            }

			if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL.name())) {

				inconsisenciaTecnicaNova.saveAnexos(this.anexos);

				inconsisenciaTecnicaNova = this;

				inconsisenciaTecnicaNova.inconsistenciaTecnicaDocumentoTecnicoAmbiental.inconsistenciaTecnica = this;

				inconsisenciaTecnicaNova.inconsistenciaTecnicaDocumentoTecnicoAmbiental.save();

			}

		}
		return this;
	}

	public void excluiInconsistenciaTecnica (){

		if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.TIPO_LICENCA.name())) {
			InconsistenciaTecnicaTipoLicenca.findById(this.inconsistenciaTecnicaTipoLicenca.id)._delete();
		}

		if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.ATIVIDADE.name())) {
			InconsistenciaTecnicaAtividade.findById(this.inconsistenciaTecnicaAtividade.id)._delete();
		}

		if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.PARAMETRO.name())) {
			InconsistenciaTecnicaParametro.findById(this.inconsistenciaTecnicaParametro.id)._delete();
		}

		if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.QUESTIONARIO.name())) {
			InconsistenciaTecnicaQuestionario.findById(this.inconsistenciaTecnicaQuestionario.id)._delete();
		}

		if (this.tipoDeInconsistenciaTecnica.equals(TipoDeInconsistenciaTecnica.DOCUMENTO_ADMINISTRATIVO.name())) {
			InconsistenciaTecnicaDocumentoAdministrativo.findById(this.inconsistenciaTecnicaDocumentoAdministrativo.id)._delete();
		}

		if (this.tipoDeInconsistenciaTecnica.equals(TipoDeInconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL.name())) {
			InconsistenciaTecnicaDocumentoTecnicoAmbiental.findById(this.inconsistenciaTecnicaDocumentoTecnicoAmbiental.id)._delete();
		}

	}

	public void deleteAnexos() {

		for (Documento anexo : this.anexos) {

			File documento = anexo.getFile();
			documento.delete();
		}

	}

	public void saveAnexos(List<Documento> novosAnexos) {

		TipoDocumento tipo = TipoDocumento.findById(TipoDocumento.DOCUMENTO_INCONSISTENCIA_TECNICA);

		if (this.anexos == null)
			this.anexos = new ArrayList<>();

		Iterator<Documento> docsCadastrados = anexos.iterator();
		List<Documento> documentosDeletar = new ArrayList<>();

		while (docsCadastrados.hasNext()) {

			Documento docCadastrado = docsCadastrados.next();

			if (ListUtil.getById(docCadastrado.id, novosAnexos) == null) {

				docsCadastrados.remove();

				// remove o documeto do banco apenas se ele não estiver relacionado
				// com outra análises
				List<AnaliseTecnica> analiseTecnicaRelacionadas = docCadastrado.getAnaliseTecnicasRelacionadas();
				if(analiseTecnicaRelacionadas.size() == 0) {

					documentosDeletar.add(docCadastrado);
				}
			}
		}

		for (Documento novoAnexo : novosAnexos) {

			if (novoAnexo.id == null) {

				novoAnexo.tipo = tipo;

				novoAnexo.save();
				this.anexos.add(novoAnexo);
			}
		}

		ModelUtil.deleteAll(documentosDeletar);
	}

	public String getTipoDeInconsistenciaTecnica() {
		if (this.inconsistenciaTecnicaTipoLicenca != null) {
			tipoDeInconsistenciaTecnica = InconsistenciaTecnica.TipoDeInconsistenciaTecnica.TIPO_LICENCA.name();
		}

		else if (this.inconsistenciaTecnicaAtividade != null) {
			tipoDeInconsistenciaTecnica = InconsistenciaTecnica.TipoDeInconsistenciaTecnica.ATIVIDADE.name();
		}

		else if(this.inconsistenciaTecnicaParametro != null) {
			tipoDeInconsistenciaTecnica = InconsistenciaTecnica.TipoDeInconsistenciaTecnica.PARAMETRO.name();
		}

		else if(this.inconsistenciaTecnicaQuestionario != null) {
			tipoDeInconsistenciaTecnica = InconsistenciaTecnica.TipoDeInconsistenciaTecnica.QUESTIONARIO.name();
		}

		else if(this.inconsistenciaTecnicaDocumentoAdministrativo != null) {
			tipoDeInconsistenciaTecnica = InconsistenciaTecnica.TipoDeInconsistenciaTecnica.DOCUMENTO_ADMINISTRATIVO.name();
		}

		else if(this.inconsistenciaTecnicaDocumentoTecnicoAmbiental != null) {
			tipoDeInconsistenciaTecnica = InconsistenciaTecnica.TipoDeInconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL.name();
		}

		else if (this.tipoDeInconsistenciaTecnica.equals(TipoDeInconsistenciaTecnica.DOCUMENTO_TECNICO_AMBIENTAL.name())) {
			inconsistenciaTecnicaDocumentoTecnicoAmbiental.findById(this.inconsistenciaTecnicaDocumentoTecnicoAmbiental.id)._delete();
		}

		return tipoDeInconsistenciaTecnica;
	}
}
