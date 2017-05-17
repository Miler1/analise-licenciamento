package models.licenciamento;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(schema = "licenciamento", name = "tipo_caracterizacao")
public class TipoCaracterizacao extends Model {

	public static final Long DISPENSA = 1l;
	public static final Long DECLARATORIO = 2l;
	public static final Long SIMPLIFICADO = 3l;
	public static final Long ORDINARIO = 4l;
	
	public String nome;
}
