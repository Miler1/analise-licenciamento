package models.validacaoParecer;

import models.AnaliseGeo;
import models.AnaliseJuridica;
import models.AnaliseTecnica;
import models.TipoResultadoAnalise;
import models.licenciamento.TipoAnalise;
import play.db.jpa.GenericModel;

public abstract class Analisavel extends GenericModel {

	public abstract TipoResultadoAnalise getTipoResultadoValidacao();

	public abstract TipoAnalise getTipoAnalise();

//	public void inativar(){
//
//		TipoAnalise tipoAnalise = this.getTipoAnalise();
//
//		if(tipoAnalise.equals(TipoAnalise.GEO)){
//			AnaliseGeo analise = (AnaliseGeo)(this);
//			analise.ativo = false;
//			analise._save();
//		}
//
//		if(tipoAnalise.equals(TipoAnalise.TECNICA)){
//			AnaliseTecnica analise = (AnaliseTecnica)(this);
//			analise.ativo = false;
//			analise._save();
//		}
//
//		if(tipoAnalise.equals(TipoAnalise.JURIDICA)){
//			AnaliseJuridica analise = (AnaliseJuridica)(this);
//			analise.ativo = false;
//			analise._save();
//		}
//	}
}
