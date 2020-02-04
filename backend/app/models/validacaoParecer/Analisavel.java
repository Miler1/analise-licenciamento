package models.validacaoParecer;

import models.TipoResultadoAnalise;
import models.licenciamento.TipoAnalise;
import play.db.jpa.GenericModel;

public abstract class Analisavel extends GenericModel {

	public abstract TipoResultadoAnalise getTipoResultadoValidacao();

	public abstract TipoAnalise getTipoAnalise();
}
