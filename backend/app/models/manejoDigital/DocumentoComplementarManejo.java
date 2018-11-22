package models.manejoDigital;

import models.Documento;
import play.data.validation.Required;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(schema = "analise", name = "documento_complementar_manejo")
@PrimaryKeyJoinColumn(name = "id_documento", referencedColumnName = "id")
public class DocumentoComplementarManejo extends Documento {

	@Required
	@ManyToOne
	@JoinColumn(name = "id_analise_tecnica_manejo")
	public AnaliseTecnicaManejo analiseTecnicaManejo;
}