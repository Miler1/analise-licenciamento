package models;


import exceptions.ValidacaoException;
import models.licenciamento.AtividadeCaracterizacao;
import models.licenciamento.AtividadeCaracterizacaoParametros;
import models.licenciamento.TipoLicenca;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import serializers.InconsistenciaSerializer;
import utils.*;

import javax.persistence.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

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
	public InconsistenciaTecnicaDocumento inconsistenciaTecnicaDocumento;

	@Transient
	public String tipoDeInconsistenciaTecnica;

	public enum TipoDeInconsistenciaTecnica { TIPO_LICENCA, ATIVIDADE, PARAMETRO, QUESTIONARIO ,DOCUMENTO }

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

			if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.TIPO_LICENCA.name())) {

				this.saveAnexos(this.anexos);

				this.inconsistenciaTecnicaTipoLicenca.inconsistenciaTecnica = this;

				this.inconsistenciaTecnicaTipoLicenca.save();

			}

			if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.ATIVIDADE.name())) {

				this.saveAnexos(this.anexos);

				this.inconsistenciaTecnicaAtividade.inconsistenciaTecnica = this;

				this.inconsistenciaTecnicaAtividade.save();

			}

			if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.PARAMETRO.name())) {

				this.saveAnexos(this.anexos);

				this.inconsistenciaTecnicaParametro.inconsistenciaTecnica = this;

				this.inconsistenciaTecnicaParametro.save();

			}

//            if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.QUESTIONARIO.name())) {
//
//				this.saveAnexos(this.anexos);
//
//				this.inconsistenciaTecnicaQuestionario.inconsistenciaTecnica = this;
//
//				this.inconsistenciaTecnicaQuestionario.save();
//
//            }

//            if (inconsistenciaTecnica.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.DOCUMENTO.name())) {
//
//				this.saveAnexos(this.anexos);
//
//				this.inconsistenciaTecnicaDocumento.inconsistenciaTecnica = this;
//
//				this.inconsistenciaTecnicaDocumento.save();
//
//            }

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

		if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.DOCUMENTO.name())) {
			InconsistenciaTecnicaDocumento.findById(this.inconsistenciaTecnicaDocumento.id)._delete();
		}

		InconsistenciaTecnica i = InconsistenciaTecnica.findById(this.id);

		i.deleteAnexos();

		i.delete();

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
}
