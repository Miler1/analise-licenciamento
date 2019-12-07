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

	public InconsistenciaTecnica (String descricaoInconsistencia, String tipoInconsistencia, AnaliseTecnica analiseTecnica, List<Documento> anexos){

		this.analiseTecnica = analiseTecnica;
		this.tipoInconsistencia = tipoInconsistencia;
		this.descricaoInconsistencia = descricaoInconsistencia;
		this.anexos = anexos;

	}

	public InconsistenciaTecnica (){

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

		} else {

			if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.TIPO_LICENCA.name())) {

				novaInconsistenciaTecnica = new InconsistenciaTecnica(this.descricaoInconsistencia, this.tipoInconsistencia, this.analiseTecnica, this.anexos);

				novaInconsistenciaTecnica.saveAnexos(this.anexos);

				novaInconsistenciaTecnica.save();

				InconsistenciaTecnicaTipoLicenca inconsistenciaTecnicaTipoLicenca = new InconsistenciaTecnicaTipoLicenca(this.inconsistenciaTecnicaTipoLicenca.tipoLicenca, novaInconsistenciaTecnica);

				inconsistenciaTecnicaTipoLicenca.save();

				novaInconsistenciaTecnica.tipoDeInconsistenciaTecnica = this.tipoDeInconsistenciaTecnica;

			}

			if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.ATIVIDADE.name())) {

				novaInconsistenciaTecnica = new InconsistenciaTecnica(this.descricaoInconsistencia, this.tipoInconsistencia, this.analiseTecnica, this.anexos);

				novaInconsistenciaTecnica.saveAnexos(this.anexos);

				novaInconsistenciaTecnica.save();

				InconsistenciaTecnicaAtividade inconsistenciaTecnicaAtividade = new InconsistenciaTecnicaAtividade(this.inconsistenciaTecnicaAtividade.atividadeCaracterizacao, novaInconsistenciaTecnica);

				inconsistenciaTecnicaAtividade.save();

				novaInconsistenciaTecnica.tipoDeInconsistenciaTecnica = this.tipoDeInconsistenciaTecnica;

			}

			if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.PARAMETRO.name())) {

				novaInconsistenciaTecnica = new InconsistenciaTecnica(this.descricaoInconsistencia, this.tipoInconsistencia, this.analiseTecnica, this.anexos);

				novaInconsistenciaTecnica.saveAnexos(this.anexos);

				novaInconsistenciaTecnica.save();

				InconsistenciaTecnicaParametro inconsistenciaTecnicaAtividadeParametro = new InconsistenciaTecnicaParametro(this.inconsistenciaTecnicaParametro.parametroAtividade, novaInconsistenciaTecnica);

				inconsistenciaTecnicaAtividadeParametro.save();

				novaInconsistenciaTecnica.tipoDeInconsistenciaTecnica = this.tipoDeInconsistenciaTecnica;

			}

//            if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.QUESTIONARIO.name())) {
//
//                novaInconsistenciaTecnica = new Inconsistencia(this.descricaoInconsistencia, this.tipoInconsistencia, this.analiseTecnica, this.anexos);
//
//                novaInconsistenciaTecnica.saveAnexos(inconsistenciaTecnica.anexos);
//                novaInconsistenciaTecnica.save();
//
//            }

//            if (inconsistenciaTecnica.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.DOCUMENTO.name())) {
//
//                novaInconsistenciaTecnica = new InconsistenciaTecnica(this.descricaoInconsistencia, this.tipoInconsistencia, inconsistenciaTecnica.analiseTecnica, inconsistenciaTecnica.anexos);
//
//                novaInconsistenciaTecnica.saveAnexos(this.anexos);
//                novaInconsistenciaTecnica.save();
//
//            }

		}
		return novaInconsistenciaTecnica;
	}

	public InconsistenciaTecnica buscarInconsistenciaTecnica (){

		InconsistenciaTecnica inconsistenciaTecnica = InconsistenciaTecnica.findById(this.id);

		if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.TIPO_LICENCA.name())) {
			inconsistenciaTecnica.inconsistenciaTecnicaTipoLicenca = InconsistenciaTecnicaTipoLicenca.find("id_inconsistencia_tecnica = :idInconsistenciaTecnica ")
					.setParameter("idInconsistenciaTecnica", this.id).first();
		}

        if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.ATIVIDADE.name())) {
            inconsistenciaTecnica.inconsistenciaTecnicaAtividade = InconsistenciaTecnicaAtividade.find("id_inconsistencia_tecnica = :idInconsistenciaTecnica")
                    .setParameter("idInconsistenciaTecnica", this.id).first();
        }

        if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.PARAMETRO.name())) {
            inconsistenciaTecnica.inconsistenciaTecnicaParametro = InconsistenciaTecnicaParametro.find("id_inconsistencia_tecnica = :idInconsistenciaTecnica AND id_parametro = :idParametroAtividade")
                    .setParameter("idInconsistenciaTecnica", this.id)
					.setParameter("idParametroAtividade", this.inconsistenciaTecnicaParametro.parametroAtividade.id).first();

            if(inconsistenciaTecnica.inconsistenciaTecnicaParametro == null ){
            	inconsistenciaTecnica.tipoInconsistencia = "";
            	inconsistenciaTecnica.descricaoInconsistencia = "";
            	inconsistenciaTecnica.anexos = new ArrayList<>();
            	inconsistenciaTecnica.id = null;

			}
        }

		if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.QUESTIONARIO.name())) {
			inconsistenciaTecnica.inconsistenciaTecnicaQuestionario = InconsistenciaTecnicaQuestionario.find("id_inconsistencia_tecnica = :idInconsistenciaTecnica")
					.setParameter("idInconsistenciaTecnica", this.id).first();
		}

		if (this.tipoDeInconsistenciaTecnica.equals(InconsistenciaTecnica.TipoDeInconsistenciaTecnica.DOCUMENTO.name())) {
			inconsistenciaTecnica.inconsistenciaTecnicaDocumento = InconsistenciaTecnicaDocumento.find("id_inconsistencia_tecnica = :idInconsistenciaTecnica")
					.setParameter("idInconsistenciaTecnica", this.id).first();
		}


		return inconsistenciaTecnica;
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
