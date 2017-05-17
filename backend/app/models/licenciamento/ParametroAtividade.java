package models.licenciamento;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

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
