package models.licenciamento;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "licenciamento", name = "parametro_atividade")
public class ParametroAtividade extends Model {

	public static final String UNIDADE_HECTARES = "Ha";
	public static final String UNIDADE_METROS_QUADRADOS = "m²";
	
	public String nome;
	
	public String codigo;
	
	public String unidade;
	
	public boolean isArea() {
		
		return this.unidade != null && 
				(this.unidade.equals(UNIDADE_HECTARES) || 
				this.unidade.equals(UNIDADE_METROS_QUADRADOS));
	}
}
